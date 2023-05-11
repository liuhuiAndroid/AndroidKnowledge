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
       // 刷新
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

3. 使用router

   ```dart
   MCRouter router = MCRouter();
   var ack = await router.push(name: MCRouter.secondPage, arguments: 'Hello from mainPage');
   ```

### No.8 实战--Android原生项目进行改造

##### Android原生项目改造

- 介绍当前已有的 Android 项目
- 原生项目开发流程
- 结合实战项目分析 Flutter 的覆盖面
- 核心模块 Flutter 化

##### 原生项目现状分析

##### 原生开发流程解析

##### Flutter 覆盖面分析

##### 当Android遇到Flutter

```kotlin
    private val friendFragment by lazy {
        FlutterFragment.withNewEngine().initialRoute("main/friend").build<FlutterFragment>()
    }
```

### No.9 实战--Flutter视频播放器封装及播放列表开发

##### Flutter视频播放器--本章导学

- Flutter 常用播放器介绍
- FijkPlayer 状态机
- FijkPlayer 的使用方法
- 播放器封装
- 播放器插件化
- 播放列表开发

##### Flutter 常用跨端播放器介绍及选择

- video_player：官方播放器
- video_player_plus：官方播放器扩展
- Chewie
- FijkPlayer

##### ijkPlayer状态机流程详解

##### ijkPlayer的集成与使用

https://pub.dev/packages/fijkplayer

```yaml
dependencies:
  flutter:
    sdk: flutter
  fijkplayer: ^0.10.1
```

##### 播放器封装及自定义插件

1. new Flutter Plugin

2. 主工程依赖插件工程

   ```yaml
   dependencies:
     player:
       path: player
   ```

3. 编写插件工程

4. 使用AbsorbPointer拦截事件

##### 基于Player插件的视频列表开发

### No10.网络请求及数据解析框架——视频下载

##### 视频下载--本章导学

- Flutter 中 Http 请求的几种方式
- Http 请求中的 IO 操作
- Json 数据解析
- Json 解析代码自动生成

##### 几种http请求的实现方式

- HttpClient：Dart官方自带
- http：第三方库

##### 基于 Http 请求的网络IO操作

- Restful API
- 拦截器
- Cookie管理
- 文件上传/下载

https://pub.dev/packages/dio

```yaml
dependencies:
  dio: ^4.0.6
```

##### Json 数据解析

- dart：convert 官方内置解析库
- Json 转化成 Map
- Json 转化成对象

##### Json转换成对象

```dart
import 'dart:convert';
import 'package:mc/video_page/video_model.dart';

class VideoController {
  // 本地mock数据，实际上是模拟网络数据
  static const _serverData = """{
    "title": "示例视频",
    "url": "https://sample-videos.com/video123/flv/240/big_buck_bunny_240p_10mb.flv",
    "playCount": 88
}""";
  late VideoModel model;
  void init() {
    model = fetchVideoData();
  }
  // 缺点：
  // 1、需要针对json的每个字段创建get方法，一旦字段多了会非常繁琐
  // 2、需要保证map的字段和json的字段完全一致， 容易出错
  // 从服务端拉取视频Json字符串类型表示的视频数据
  VideoModel fetchVideoData() {
    return VideoModel.fromJson(jsonDecode(_serverData));
  }
}
```

```dart
import 'package:json_annotation/json_annotation.dart';
part 'video_model.g.dart';

@JsonSerializable()
class VideoModel {
  String title = '';
  String url = '';
  int playCount = 0;

  VideoModel(this.title, this.url, this.playCount);

  factory VideoModel.fromJson(Map<String, dynamic> json) => _$VideoModelFromJson(json);

  Map<String, dynamic> toJson(VideoModel model) => _$VideoModelToJson(model);
}
```

##### 数据解析自动化框架

https://pub.dev/packages/json_serializable

```
dependencies:
  json_serializable: ^6.2.0
  json_annotation: ^4.5.0
  build_runner: ^2.1.11
```

````shell
flutter pub run build_runner build
````

### 第11章 实战--数据持久化与缓存结构设计——数据缓存

##### 数据持久化与缓存结构设计

- 三级缓存基本原理
- 三级缓存基本结构和设计方案
- 为视频播放器增加本地视频缓存
- 播放器列表数据源改造

##### Flutter数据持久化

shared_perferences 插件

https://pub.dev/packages/shared_preferences

```yaml
dependencies:
  shared_preferences: ^2.0.15
```

##### 三级缓存原理

##### Flutter中的三级缓存

##### 三级缓存实现

- 第三级：模拟从服务端获取数据
- 第二级：存入本地磁盘
- 第一级：读取到内存

```dart
import 'dart:convert';

import 'package:mc/video_page/server_data.dart';
import 'package:mc/video_page/video_model.dart';
import 'package:shared_preferences/shared_preferences.dart';

class VideoController {
  VideoModel? model;

  Future<void> init() async {
    // 首先判断一级缓存——即内存中是否有数据
    print('MOOC- init video controller');
    if (model == null) {
      print('MOOC- model is null');
      // 如果没有，则从二级/三级缓存查找
      model = await fetchVideoData();
    }
  }

  // 从服务端拉取视频Json字符串类型表示的视频数据
  Future<VideoModel> fetchVideoData() async {
    var sp = await SharedPreferences.getInstance();
    var modelStr = sp.getString("videoModel");
    if (modelStr != null && modelStr.isNotEmpty) {
      // 二级缓存中找到数据，直接使用
      print('MOOC- 2/use sp data');
      return VideoModel.fromJson(jsonDecode(modelStr));
    } else {
      // 二级缓存未找到数据，走三级缓存
      var model = jsonDecode(ServerData.fetchDataFromServer());
      var sp = await SharedPreferences.getInstance();
      sp.setString('videoModel', ServerData.fetchDataFromServer());
      print('MOOC- 3/fetch data from server');
      return VideoModel.fromJson(model);
    }
  }
}
```

##### 播放器缓存管理

- 为视频Url增加前缀
- 设置播放器缓存路径并打开缓存开关

##### 视频列表源数据改造

### No.12实战--动画特效与评论列表开发——互动模块

##### 互动模块

- 地毯式解析点赞动画特效流程
- 完成点赞基本功能的开发
- 分阶段拆解动画时间点
- Flutter动画初探
- 多种特效组合管理
- 评论列表底部弹窗

##### 点赞动画解析

- 绘制红心icon
- 红心逐渐出现
- 旋转红心
- 红心放大
- 红心逐渐消失
- 多红心管理

##### 绘制点赞动画

- 不影响底层Widget手势事件
- 脱离具体点赞场景
- 独立、通用Widget

##### 计算点赞坐标

##### Flutter动画指南

Animation 的几种状态

- forward：动画开始播放，动画进度逐渐变大
- reverse：动画反向播放，动画进度逐渐变小
- dismissed：动画结束且进度为0，对应reverse结束
- completed：动画结束且进度为1，对应forward结束

认识 AnimationController

- 管理动画的启动、暂停、倒放等等
- 监听播放进度
- 可以设置播放进度的上限和下限
- 派生自Animation，本身也是一种动画

##### 点赞特效开发

##### 点赞特效优化

##### 评论列表弹窗

### 第13章 实战--瀑布列表——个人中心视频列表

##### 本章导学

- 地毯式拆解个人中心页面结构及细节
- 搭建个人中心页面框架及路由
- 背景墙及图片选取控件
- 开发个人信息资料卡
- 开发圆形头像控件
- 上传player插件，并改为云依赖
- 移植VideoList控件

##### 个人中心页面结构分析

##### 个人中心页面框架搭建

##### 背景墙开发

```yaml
dependencies:
  flutter_gen_runner: ^4.3.0
  image_picker: ^0.8.5+3
```

flutter packages pub run build_runner build

##### 图片选择器

##### 图片选择器开发

##### GetX状态管理框架

- 实现局部刷新
- 响应式布局
- 简单易用
- 依赖管理

```dart
dependencies:
  get: ^4.5.0
```

##### 复杂页面的组合方式

- 自定义 TextCount 控件
- Row 及 Column 的使用
- 控件的多重组合方式

##### 圆形头像控件

##### 多tab联动切换

- 通过TabBar展示Tab标题
- 通过TabBarView展示Tab页
- 使用TabController实现联动切换

##### 视频列表优化

##### 播放器组件云依赖

- 上传播放器代码之Github
- 在项目中通过Github地址远程依赖

##### VideoList代码移植

### 第14章 实战--相机模块开发——上传视频

##### 本章导学

- 拍照页面拆解分析
- Native/Flutter页面来回跳转
- Flutter中相机的使用
- 相机切换、定时拍摄、闪光灯控制
- 本地视频提取

##### Flutter 相机页面分析

##### Flutter页面的跳转与关闭

- 在Native中添加Flutter页面容器

- 展示Flutter页面

- 退出Flutter页面并移除容器

  通过MessageChannel通知Android移除Fragment

##### 相机插件使用步骤及注意事项

- 添加Camera依赖
- 获取设备可用相机列表
- 创建并初始化CameraController
- 预览视频帧流

##### Flutter 相机开发

https://pub.dev/packages/camera

##### 多摄像头切换

##### 定时拍摄及闪光灯控制

##### 本地视频提取

##### 拍照功能开发

##### 录制视频

### 第15章 实战--Flutter 项目打包发布

##### Flutter项目打包

- Flutter 的不同打包方式
- Flutter 开发包的构建
- 对安装包进行签名
- Android 打包发布

##### Flutter 3种打包方式

- Debug：JIT编译，适用于开发阶段
- Release：AOT编译，适用于发布阶段
- Profile：AOT编译，适用于性能监控

##### Flutter 构建配置

##### 安装包签名

##### Flutter 构建安装包

### 第16章 Flutter多端适配

##### Flutter多端适配

- 依赖关系分析
- Flutter module 搬迁到 Web 工程
- Web 与移动端到不同之处

##### 跨平台依赖分析

##### Web端代码移植

```shell
flutter run -d chrome
pub get
```

判断Web端依赖video_player

##### 视频插件多端适配

##### Web视频自动播放

### 第17章 课程总结

##### 课程总结

- Flutter 技术概览及 Dart 语法特效
- Flutter/Native 混合开发基础技术
- Flutter 编译原理及调试优化
- 框架搭建及页面导航
- 原生项目改造
- 模块拆分及各个子模块的独立开发
