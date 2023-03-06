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

官网安装 Flutter SDK，配置 Flutter 和 Dart 环境变量

https://flutter.cn/docs/development/tools/sdk/releases

```shell
// Flutter 环境变量
export PUB_HOSTED_URL=https://pub.flutter-io.cn
export FLUTTER_STORAGE_BASE_URL=https://storage.flutter-io.cn
export PATH_TO_FLUTTER_GIT_DIRECTORY=[替换成你自己的Flutter解压目录]
export PATH=$PATH_TO_FLUTTER_GIT_DIRECTORY/bin:$PATH
export PATH=$PATH_TO_FLUTTER_GIT_DIRECTORY/bin/cache/dart-sdk/bin:$PATH
```

```shell
flutter create sec
# 诊断 SDK
flutter doctor -v
flutter run
flutter clean
```

Android Studio 安装 Flutter 和 Dart 插件

### No.4 Flutter实现混合式开发

三端分离模式

将Flutter工程作为原生工程的子模块，维持原有的原生工程管理方式不变。Project type：Module

进入Android同级目录，创建flutter-module，在原生项目添加依赖，开发双端功能，打包aar、framework

Flutter-Native 消息通道

- BasicMessageChannel

  用于传递字符串和半结构化的信息

- EventChannel

  用于数据流的通信

- MethodChannel

  用于传递方法调用

### No.5 Flutter3.x新特性

Flutter3.x 一统全平台

稳定了Mac和Linux平台

常用功能扩展：无障碍功能

### No.6 Flutter 编译原理及多场景的调试优化技术

Flutter 编译方式及编译原理

断点调试方式

日志调试

Hot Reload

devTool 工具

Inspector 布局调试

### No.7 实战--混合式开发框架搭建及项目架构设计

混合式开发项目创建

页面导航框架的选择与使用

实战项目分析

整体架构设计

### No.8 实战--Android原生项目进行改造

介绍当前已有的 Android 项目

原生项目开发流程

结合实战项目分析 Flutter 的覆盖面

核心模块 Flutter 化

### No.9 实战--Flutter视频播放器封装及播放列表开发

Flutter 常用播放器介绍

FijkPlayer 状态机

FijkPlayer 的使用方法

播放器封装

播放器插件化





