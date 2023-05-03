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

##### Flutter3.x新特性

##### Flutter3.x简介

- 与2022年Google I/O大会上发布
- 稳定支持macOS和Linux，正式开启全平台覆盖时代
- UI刷新率、内存等性能改进
- 全平台的功能扩展

##### Flutter3.x支持多平台运行实战演示

##### Flutter3.x无障碍功能支持的开发

- 实现全平台Accessibility支持
- 大字体
- 读屏器
- 高对比度

##### Flutter3.x新增dev tool增强

- 增强性能选项卡下的跟踪
- 网络标签页上的改进

##### Flutter3.x新特性--本章总结

- Flutter 3.x 一统全平台
- 稳定了 Mac 和 Linux 平台
- 常用功能扩展

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



