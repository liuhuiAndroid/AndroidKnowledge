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

