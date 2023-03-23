

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

#### 混合开发

混合开发场景

- 原生页面和 React Native 页面相互跳转
- 原生页面嵌入 React Native 模块
- React Native 页面嵌入原生模块

##### 将 React Native 集成到现有的 Android 应用中步骤

1. 创建一个 React Native 项目

   - 方式一：通过 npm 安装 react-native 的方式添加一个 React Native 项目
   - 方式二：通过 react-native init 来初始化一个 React Native 项目

2. 为已存在的 Android 应用添加 React Native 所需要的依赖

   1. 配置使用本地 React Native maven 目录

      ```groovy
      allprojects {
          repositories {
              maven {
                  url("$rootDir/../node_modules/react-native/android")
              }
          }
      }
      ```

   2. 配置依赖

      ```groovy
      dependencies {
        	// From node_modules
          implementation "com.facebook.react:react-native:+"
      }
      ```

   3. 如果需要用到 React Native 的 Dev Settings 功能（可以通过 Ctrl + M 打开）

      需要在 AndroidManifest.xml 文件中添加

      ```xml
      <activity android:name="com.facebook.react.devsupport.DevSettingsActivity"/>
      ```

   4. 指定 ndk 需要兼容的架构

      ```groovy
      defaultConfig {
        ndk {
          abiFilters "armeabi-v7a", "x86", "armeabi"
        }
      }
      ```

3. 创建 index.js 并添加你的 React Native 代码

   ```javascript
   // index.js
   import {AppRegistry} from 'react-native';
   import App from './js/App'
   AppRegistry.registerComponent(appName, () => App);
   
   // App.js
   import React, {Component} from 'react';
   import {Provider} from 'react-redux';
   import AppNavigator from './navigator/AppNavigators';
   import store from './store'
   
   type Props = {};
   export default class App extends Component<Props> {
       render() {
           return <Provider store={store}>
               <AppNavigator/>
           </Provider>
       }
   }
   ```

4. 创建一个 Activity 来承载 React Native，在这个 Activity 中创建一个 ReactRootView 来作为 React Native 服务的容器。再为 ReactInstanceManager 添加 Activity 的生命周期回调。打开双击R快速加载JS功能

   也可以通过 ReactActivity 作为 React Native 容器

   另外还需要 Application 实现 ReactApplication 并添加代码

   - 通过 ReactInstanceManager 的方式：灵活，可定制性强；
   - 通过继承 ReactActivity 的方式：简单，可定制性差；

5. 启动 React Native 的 Packager 服务，运行应用

   ```shell
   npm srart
   ```

6. （可选）根据需要添加更多 React Native 的组件

7. 运行、调试、打包、发布应用

   - 调试：通过 Ctrl + M 打开开发者菜单

   - 打包

     通过命令打包成 JS Bundle 然后放入 Android 项目的 assets 目录下和配置的 setBundleAssetName("index.android.bundle") 对应

     ```shell
     react-native bundle --platform android --dev false --entry-file index.js --bundle-output .//app/sec/main/assets/index.android.bundle --assets-dest .//app/sec/main/res
     ```

封装 React Native Android 原生模块供 React Native JS 使用

1. 编写原生模块的相关 Java 代码

2. 暴露接口与数据交互

   借助 React Native 的 ReactContextBaseJavaModule 类

   @ReactMethod 向 JS 暴露方法

   原生模块通过 Callback 或 Promise 进行数据传递；可以通过 RCTDeviceEventEmitter 向 JS 发送多次事件

3. 注册与导出 React Native 原生模块

   实现 ReactPackage 向 React Native 注册原生模块，ReactPackage 实现类需要注册到 Application，就可以交给 JS 来使用了

##### 打包发布 Android 项目

1. JS Bundle 包放入 Android 项目内使用 Android Studio 打包
2. React Native 官方推荐的方式

##### React Native升级与适配指南

自动升级：react-native-git-upgrade

手动升级，每个大版本升级一次；建议使用

##### React Native 新架构实战课



