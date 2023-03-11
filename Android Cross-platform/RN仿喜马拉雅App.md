

TypeScript

安装 nodejs watchman

React Native 中文网：https://reactnative.cn/；安装完整原生环境

```shell
nodejs watchman
npm install -g yarn
npx react-native init AwesomeProject
cd AwesomeProject
yarn android
# 或者
yarn react-native run-android
npx react-native init ximalaya --template react-native-template-typescript
yarn add babel-plugin-module-resolver
```

VSCode插件：prettier

command + M 重新加载

react-native-config：多环境

babel-plugin-module-resolver 插件解决绝对路径问题，配置 babel.config.js

React Navigation 5：官方推荐的导航器，官网：https://reactnavigation.org/docs/getting-started

redux

react-native-svg

react-native-iconfont-cli

axios



### 附录

##### 混合开发

封装 React Native Android 原生模块供 React Native JS 使用

1. 编写原生模块的相关 Java 代码

2. 暴露接口与数据交互

   借助 React Native 的 ReactContextBaseJavaModule 类

   @ReactMethod 向 JS 暴露方法

   原生模块通过 Callback 或 Promise 进行数据传递；可以通过 RCTDeviceEventEmitter 向 JS 发送多次事件

3. 注册与导出 React Native 原生模块

   实现 ReactPackage 向 React Native 注册原生模块，ReactPackage 实现类需要注册到 Application，就可以交给 JS 来使用了

将 React Native 集成到现有的 Android 应用中

1. 创建一个 React Native 项目

   可以通过 react-native init 来初始化一个 React Native 项目

2. 为已存在的 Android 应用添加 React Native 所需要的依赖

   配置 maven 和依赖；配置权限；指定 ndk 需要兼容的架构

3. 创建 index.js 并添加 React Native 代码

4. 创建一个 Activity 来承载 React Native，在这个 Activity 中创建一个 ReactRootView 来作为 React Native 服务的容器

5. 启动 React Native 的 Packager 服务，运行应用

6. 可选：根据需要添加更多 React Native 的组件

7. 运行、调试、打包、发布应用

##### 打包发布 Android 项目

1. JS Bundle 包放入 Android 项目内使用 Android Studio 打包
2. React Native 官方推荐的方式

##### React Native升级与适配指南

自动升级：react-native-git-upgrade

手动升级，每个大版本升级一次；建议使用

##### React Native 新架构实战课



