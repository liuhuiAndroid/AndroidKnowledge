### No.2 Flutter整体介绍

##### 为什么选择Dart语言

复合Flutter声明式UI的布局方式

Dart的线程模型：微任务队列和事件队列

线程管理：4个不同的Runner Thread

##### 工程结构

```shell
flutter --version
flutter create sec
cd sec
flutter run
# 或者 Android Studio New Flutter Project
```

##### 布局方式及差异

声明式UI优点

1. 更适合做多设备适配
2. UI布局和控制逻辑通过reactive方式实现数据绑定
3. 更好实现UI局部刷新机制，只刷新更新的部分

### No.3 开发工具安装及环境搭建

- Flutter SDK 下载安装

  官网安装 Flutter SDK，配置 Flutter 和 Dart 环境变量

  官网地址：https://flutter.cn/docs/development/tools/sdk/releases

  ```shell
  open ~/.bash_profile
  // Flutter 环境变量
  export PUB_HOSTED_URL=https://pub.flutter-io.cn
  export FLUTTER_STORAGE_BASE_URL=https://storage.flutter-io.cn
  export PATH_TO_FLUTTER_GIT_DIRECTORY=[替换成你自己的Flutter解压目录]
  export PATH=$PATH_TO_FLUTTER_GIT_DIRECTORY/bin:$PATH
  export PATH=$PATH_TO_FLUTTER_GIT_DIRECTORY/bin/cache/dart-sdk/bin:$PATH
  ```

  ```shell
  flutter --version
  flutter create sec
  # 诊断 SDK
  flutter doctor -v
  flutter run
  flutter clean
  flutter pub get
  flutter pub update
  ```

- Android Studio 下载安装及功能配置

  Android Studio 安装 Flutter 和 Dart 插件

  String - Languages 配置 Flutter 和 Dart SDK 路径

  Flutter：/Users/sec/Documents/soft/flutter

  Dart：/Users/sec/Documents/soft/flutter/bin/cache/dart-sdk

### No.4 Flutter实现混合式开发

Flutter 很好地支持了以独立页面、甚至可以通过UI片段的方式集成到现有的应用中，即所谓的混合开发模式

混合开发模式：

- 统一管理模式

  将原生工程作为Flutter工程的子工程，由Flutter进行统一管理

- **三端分离模式**

  将Flutter工程作为原生工程的子模块，维持原有的原生工程管理方式不变

  1. 进入Android/iOS同级目录
  2. 创建flutter-module
  3. 在原生项目添加依赖
  4. 开发双端功能
  5. aar、framework

Flutter 五种工程模式：

- Flutter Application

  构建一个标准FlutterApp（统一管理模式）

  包含Dart层和平台层

- Flutter Plugin

  Flutter平台插件，包含Dart层与Native平台层的实现

  是一种特殊的Flutter Packages

- Flutter Package

  纯Dart插件工程，不依赖Flutter

  仅包含Dart层的实现，通常用来定义一些公共库

- **Flutter Module**

  创建一个Flutter模块（三端分离模式）

  以模块的形式分别嵌入原生项目

- Flutter Skeleton

  自动生成Flutter模版

  提供常用框架

Flutter-Native 消息通道

- BasicMessageChannel

  用于传递字符串和半结构化的信息

- EventChannel

  用于数据流的通信

- **MethodChannel**

  用于传递方法调用

### No.5 Flutter3.x新特性

##### Flutter3.x简介

- 与2022年Google I/O大会上发布
- 稳定支持macOS和Linux，正式开启全平台覆盖时代
- UI刷新率、内存等性能改进
- 全平台的功能扩展

##### Flutter3.x支持多平台运行实战演示

```shell
# iOS
open -a Simulator
```

##### Flutter3.x无障碍功能支持的开发

- 实现全平台Accessibility支持
- 大字体
- 读屏器
  - 主要用于移动设备
  - 通过语音反馈帮助视障用户获取屏幕内容
  - 使用手势或者键盘进行UI交互
  - 标准的Widget会自动生成无障碍树且支持小部件自定义

- 高对比度

##### Flutter3.x新增dev tool增强

- 增强性能选项卡下的跟踪
- 网络标签页上的改进

##### Flutter3.x新特性--本章总结

- Flutter 3.x 一统全平台
- 稳定了 Mac 和 Linux 平台
- 常用功能扩展

### No.6 Flutter 编译原理及多场景的调试优化技术

##### Flutter代码调试

- Flutter 编译方式及编译原理
- 断点调试方式
- 日志调试
- Hot Reload
- devTool 工具
- Inspector 布局调试

##### Flutter 底层编译原理及打包方式

Flutter编译方式

- Debug 模式
- Release 模式
- JIT：即时编译
- AOT：运行前编译

##### 通过断点的方式深入Dart代码运行时

Flutter 断点调试

- 标记断点
- Attach 进程
- 调试应用
- 查看信息

##### 基于JIT热更新的高效Log调试

Command + S 保存自动更新 / 点击 Flutter Hot Reload

##### 通过Inspector深入优化UI布局

Flutter DevTool 及布局调试

- Flutter 布局调试

  - 展示当前布局信息
  - 观察 Widget Tree

  可以使用 Flutter Performance 内 Open DevTools 在浏览器打开

- Time Line

- 内存状况

- 性能面板

- 调试器

- 网络

- 日志

##### 性能面板的使用技巧及Dart内存调优

Flutter Inspector

Flutter Performance

### No.7 实战--混合式开发框架搭建及项目架构设计

##### 首页框架搭建

- 混合式开发项目创建
- 页面导航框架的选择与使用
- 实战项目分析
- 整体架构设计

##### 实战-- 混合开发项目创建

1. New Flutter Project 选择 Module

2. setting.gradle 添加 flutter module

   ```groovy
   setBinding(new Binding([gradle: this]))
   evaluate(new File(settingDir, 'flutter_module/.android/include_flutter.groovy'))
   ```

3. build.gradle

   ```groovy
   implementation project(path: ':flutter')
   ```

##### 实战--实现页面导航管理

Navigator 2.0

1. 实现 SecondPage

2. 核心代码

   ```dart
   import 'dart:async';
   
   import 'package:flutter/material.dart';
   import 'package:mc/main.dart';
   import 'package:mc/second_page.dart';
   
   // 页面路由器
   class MCRouter extends RouterDelegate<List<RouteSettings>>
       with ChangeNotifier, PopNavigatorRouterDelegateMixin<List<RouteSettings>> {
     static const String mainPage = '/main';
     static const String secondPage = '/second';
   
     static const String key = 'key';
     static const String value = 'value';
   
     // 页面栈，存储当前页面结构
     final List<Page> _pages = [];
   
     late Completer<Object?> _boolResultCompleter;
   
     MCRouter() {
       _pages.add(_createPage(const RouteSettings(name: mainPage, arguments: [])));
     }
   
     @override
     Widget build(BuildContext context) {
       // 通过Navigator完成页面切换
       return Navigator(key: navigatorKey, pages: List.of(_pages), onPopPage: _onPopPage);
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
   
     // 弹出页面
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
       switch (routeSettings.name) {
         case mainPage:
           page = const MyHomePage(title: 'My Home Page');
           break;
         case secondPage:
           page = SecondPage(params: routeSettings.arguments?.toString() ?? '');
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
   ```

##### 实战--项目功能分析

### No.8 实战--Android原生项目进行改造

介绍当前已有的 Android 项目

原生项目开发流程

结合实战项目分析 Flutter 的覆盖面

核心模块 Flutter 化

### No.9 实战--Flutter视频播放器封装及播放列表开发

##### Flutter视频播放器--本章导学

- Flutter 常用播放器介绍
- FijkPlayer 状态机
- FijkPlayer 的使用方法
- 播放器封装
- 播放器插件化
- 播放列表开发

##### Flutter 常用跨端播放器介绍及选择

- video_player
- video_player_plus
- Chewie
- FijkPlayer

##### ijkPlayer状态机流程详解

##### ijkPlayer的集成与使用

##### 播放器封装及自定义插件（一）

##### 播放器封装及自定义插件（二）

##### 播放器封装及自定义插件（三）

##### 基于Player插件的视频列表开发（一）

##### 基于Player插件的视频列表开发（二）

##### Flutter视频播放器--本章总结



### 网络请求及数据解析框架——视频下载

##### 视频下载--本章导学

##### 几种http请求的实现方式

- HttpClient：Dart官方自带
- http：第三方库

##### 基于 Http 请求的网络IO操作

- Restful API
- 拦截器
- Cookie管理
- 文件上传/下载

##### Json 数据解析

- dart：convert 官方内置解析库

##### Json转换成对象

##### 数据解析自动化框架

##### 视频下载



### 第11章 实战--数据持久化与缓存结构设计——数据缓存

##### 数据持久化与缓存结构设计--本章导学

##### Flutter数据持久化

##### 三级缓存原理

##### Flutter中的三级缓存

##### 三级缓存实现

##### 播放器缓存管理

##### 视频列表源数据改造

##### 数据持久化与缓存结构设计--本章总结



### 第14章 实战--相机模块开发——上传视频

##### 本章导学

- 拍照页面拆解分析
- Native/Flutter页面来回跳转
- Flutter中相机的使用
- 相机切换、定时拍摄、闪光灯控制
- 本地视频提取

##### Flutter 相机页面分析

##### Flutter页面的跳转与关闭

##### 相机插件使用步骤及注意事项

##### Flutter 相机开发

##### 多摄像头切换

##### 定时拍摄及闪光灯控制

##### 本地视频提取

##### 拍照功能开发

##### 录制视频

##### 本章总结



### 第15章 实战--Flutter 项目打包发布6 节

##### Flutter项目打包--本章导学

##### Flutter 3种打包方式

- Debug
- Release
- Profile

##### Flutter 构建配置

##### 安装包签名

##### Flutter 构建安装包

##### Flutter项目打包--本章总结



### 第16章 Flutter多段适配

##### Flutter多端适配--本章导学

- 依赖关系分析
- Flutter module 搬迁到 Web 工程
- Web 与移动端到不同之处

##### 跨平台依赖分析

##### Web端代码移植

##### 视频插件多端适配

##### Web视频自动播放

##### Flutter多端适配--本章总结



### 第17章 课程总结

##### 课程总结



