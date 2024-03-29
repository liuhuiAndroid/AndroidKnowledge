import 'dart:async';

import 'package:bilibili/http/dao/login_dao.dart';
import 'package:bilibili/navigator/bottom_navigator.dart';
import 'package:bilibili/navigator/hi_navigator.dart';
import 'package:bilibili/page/dark_mode_page.dart';
import 'package:bilibili/page/login_page.dart';
import 'package:bilibili/page/notice_page.dart';
import 'package:bilibili/page/registration_page.dart';
import 'package:bilibili/page/video_detail_page.dart';
import 'package:bilibili/provider/theme_provider.dart';
import 'package:bilibili/util/hi_defend.dart';
import 'package:bilibili/util/toast.dart';
import 'package:flutter/material.dart';
import 'package:hi_cache/hi_cache.dart';
import 'package:hi_net/core/hi_error.dart';
import 'package:hi_net/hi_net.dart';
import 'package:provider/provider.dart';

import 'model/video_model.dart';
import 'provider/hi_provider.dart';

void main() {
  HiDefend().run(const BiliApp());
}

class BiliApp extends StatefulWidget {
  const BiliApp({super.key});

  @override
  State<StatefulWidget> createState() => _BiliAppState();
}

class _BiliAppState extends State<BiliApp> {
  final BiliRouteDelegate _routeDelegate = BiliRouteDelegate();

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<HiCache>(
        //进行初始化
        future: HiCache.preInit(),
        builder: (BuildContext context, AsyncSnapshot<HiCache> snapshot) {
          //定义route
          var widget = snapshot.connectionState == ConnectionState.done
              ? Router(routerDelegate: _routeDelegate)
              : const Scaffold(
                  body: Center(child: CircularProgressIndicator()),
                );

          return MultiProvider(
              providers: topProviders,
              child: Consumer<ThemeProvider>(builder: (BuildContext context,
                  ThemeProvider themeProvider, Widget? child) {
                return MaterialApp(
                  home: widget,
                  theme: themeProvider.getTheme(),
                  darkTheme: themeProvider.getTheme(isDarkMode: true),
                  themeMode: themeProvider.getThemeMode(),
                  title: 'Flutter Bili',
                );
              }));
        });
  }
}

class BiliRouteDelegate extends RouterDelegate<BiliRoutePath>
    with ChangeNotifier, PopNavigatorRouterDelegateMixin<BiliRoutePath> {
  @override
  final GlobalKey<NavigatorState> navigatorKey;

  //为Navigator设置一个key，必要的时候可以通过navigatorKey.currentState来获取到NavigatorState对象
  BiliRouteDelegate() : navigatorKey = GlobalKey<NavigatorState>() {
    //实现路由跳转逻辑
    HiNavigator.getInstance().registerRouteJump(
        RouteJumpListener(onJumpTo: (RouteStatus routeStatus, {Map? args}) {
      _routeStatus = routeStatus;
      if (routeStatus == RouteStatus.detail) {
        videoModel = args!['videoMo'];
      }
      notifyListeners();
    }));
    //设置网络错误拦截器
    HiNet.getInstance().setErrorInterceptor((error) {
      if (error is NeedLogin) {
        //清空失效的登录令牌
        HiCache.getInstance().remove(LoginDao.BOARDING_PASS);
        //拉起登录
        HiNavigator.getInstance().onJumpTo(RouteStatus.login);
      }
    });
  }

  RouteStatus _routeStatus = RouteStatus.home;
  List<MaterialPage> pages = [];
  VideoModel? videoModel;

  @override
  Widget build(BuildContext context) {
    var index = getPageIndex(pages, routeStatus);
    List<MaterialPage> tempPages = pages;
    if (index != -1) {
      //要打开的页面在栈中已存在，则将该页面和它上面的所有页面进行出栈
      //tips 具体规则可以根据需要进行调整，这里要求栈中只允许有一个同样的页面的实例
      tempPages = tempPages.sublist(0, index);
    }
    var page;
    if (routeStatus == RouteStatus.home) {
      //跳转首页时将栈中其它页面进行出栈，因为首页不可回退
      pages.clear();
      page = pageWrap(BottomNavigator());
    } else if (routeStatus == RouteStatus.darkMode) {
      page = pageWrap(const DarkModePage());
    } else if (routeStatus == RouteStatus.detail) {
      page = pageWrap(VideoDetailPage(videoModel!));
    } else if (routeStatus == RouteStatus.registration) {
      page = pageWrap(const RegistrationPage());
    } else if (routeStatus == RouteStatus.notice) {
      page = pageWrap(NoticePage());
    } else if (routeStatus == RouteStatus.login) {
      page = pageWrap(const LoginPage());
    }
    //重新创建一个数组，否则pages因引用没有改变路由不会生效
    tempPages = [...tempPages, page];
    //通知路由发生变化
    HiNavigator.getInstance().notify(tempPages, pages);
    pages = tempPages;
    return WillPopScope(
      //fix Android物理返回键，无法返回上一页问题@https://github.com/flutter/flutter/issues/66349
      onWillPop: () async =>
          !(await navigatorKey.currentState?.maybePop() ?? false),
      child: Navigator(
        key: navigatorKey,
        pages: pages,
        // 返回上一页回调 onPopPage
        onPopPage: (route, result) {
          if (route.settings is MaterialPage) {
            //登录页未登录返回拦截
            if ((route.settings as MaterialPage).child is LoginPage) {
              if (!hasLogin) {
                showWarnToast("请先登录");
                return false;
              }
            }
          }
          // 执行返回操作，控制是否可以返回
          if (!route.didPop(result)) {
            return false;
          }
          var tempPages = [...pages];
          pages.removeLast();
          //通知路由发生变化
          HiNavigator.getInstance().notify(pages, tempPages);
          return true;
        },
      ),
    );
  }

  RouteStatus get routeStatus {
    if (_routeStatus != RouteStatus.registration && !hasLogin) {
      return _routeStatus = RouteStatus.login;
    } else {
      return _routeStatus;
    }
  }

  bool get hasLogin => LoginDao.getBoardingPass() != null;

  @override
  Future<void> setNewRoutePath(BiliRoutePath configuration) async {}
}

///定义路由数据，path
class BiliRoutePath {
  final String location;

  BiliRoutePath.home() : location = "/";

  BiliRoutePath.detail() : location = "/detail";
}
