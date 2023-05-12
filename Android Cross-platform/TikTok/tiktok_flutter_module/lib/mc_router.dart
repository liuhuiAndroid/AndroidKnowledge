import 'dart:async';

import 'package:flutter/material.dart';
import 'package:tiktok_flutter_module/page/camera_page/camera_page.dart';
import 'package:tiktok_flutter_module/page/mine_page/mine_page.dart';

import 'gen/assets.gen.dart';
import 'page/player_page/player_page.dart';
import 'widget/photo_picker.dart';

class MCRouter extends RouterDelegate<List<RouteSettings>>
    with ChangeNotifier, PopNavigatorRouterDelegateMixin<List<RouteSettings>> {
  static const String minePage = '/mine';
  static const String photoPicker = '/photo_picker';
  static const String playerPage = '/player';
  static const String cameraPage = '/camera';

  static const String key = 'key';
  static const String value = 'value';

  static const String key_url = 'url';
  static const String key_height = 'height';
  static const String key_width = 'width';

  final List<Page> _pages = [];

  late Completer<Object?> _boolResultCompleter;

  @override
  Widget build(BuildContext context) {
    return Navigator(key: navigatorKey, pages: _pages, onPopPage: _onPopPage);
  }

  @override
  GlobalKey<NavigatorState> get navigatorKey => GlobalKey<NavigatorState>();

  @override
  Future<void> setNewRoutePath(List<RouteSettings> configuration) async {}

  @override
  Future<bool> popRoute({Object? params}) {
    if (params != null) {
      _boolResultCompleter.complete(params);
    }
    if (_canPop()) {
      _pages.removeLast();
      notifyListeners();
      return Future.value(true);
    }
    return _confirmExit();
  }

  bool _onPopPage(Route route, dynamic result) {
    if (!route.didPop(result)) return false;

    if (_canPop()) {
      _pages.removeLast();
      return true;
    } else {
      return false;
    }
  }

  // 判断page栈长度，为空则即将退出App；不为空则返回true表示页面关闭
  bool _canPop() {
    return _pages.length > 1;
  }

  void replace({required String name, dynamic arguments}) {
    if (_pages.isNotEmpty) {
      _pages.removeLast();
    }
    push(name: name, arguments: arguments);
  }

  Future<Object?> push({required String name, dynamic arguments}) async {
    _boolResultCompleter = Completer<Object?>();
    _pages.add(_createPage(RouteSettings(name: name, arguments: arguments)));
    notifyListeners();
    return _boolResultCompleter.future;
  }

  MaterialPage _createPage(RouteSettings routeSettings) {
    Widget page;

    var args = routeSettings.arguments;

    switch (routeSettings.name) {
      case minePage:
        page = MinePage();
        break;
      case photoPicker:
        String? url;
        String height = '';
        String width = '';

        if (args is Map<String, String>) {
          url = args[MCRouter.key_url];
          height = args[MCRouter.key_height] ?? height;
          width = args[MCRouter.key_width] ?? width;
        }

        page = PhotoPickerPage(url ?? Assets.image.defaultPhoto.keyName);
        break;
      case playerPage:
        page = PlayerPage(routeSettings.arguments?.toString() ?? '');
        break;
      case cameraPage:
        page = CameraPage();
        break;
      default:
        page = const Scaffold();
    }
    return MaterialPage(
        child: page,
        key: Key(routeSettings.name!) as LocalKey,
        name: routeSettings.name,
        arguments: routeSettings.arguments);
  }

  Future<bool> _confirmExit() async {
    final result = await showDialog<bool>(
      context: navigatorKey.currentContext!,
      builder: (BuildContext context) {
        return AlertDialog(
          content: const Text('确认退出吗'),
          actions: [
            TextButton(onPressed: () => Navigator.pop(context, true), child: const Text('取消')),
            TextButton(onPressed: () => Navigator.pop(context, false), child: const Text('确定'))
          ],
        );
      },
    );
    return result ?? true;
  }
}
