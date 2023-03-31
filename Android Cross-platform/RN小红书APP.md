### ReactNative优势

- 快速开发，降本增效
- 合并两端人员，促进技术融合
- 迎合大前端趋势，构建全栈能力

##### ReactNative 和 Flutter 的比较

- React Native 学习曲线相对较为平缓，更利于边学边做，Flutter 独特的开发方式和巨量的 widget 列表让人望而生畏
- Flutter 性能优于 React Native，但是 React Native 近几年的发展更新已逐步缩小差距
- 学习 React Native 对于往大前端全栈方向发展要更有优势，而 Flutter 目前在 Web、小程序、桌面端表现平平
- React Native 天然支持热修复，而 Flutter 需要其他方案支持
- Flutter 出自 Google，对于 Android 开发有情感优势，而 React Native 出自 Facebook，需要重新学习前端语言
- 二者都是非常优秀的跨平台方案，从需选择，理性看待，不要简单用优/劣划分

### 开发环境搭建

官网：https://reactnative.dev/

VSCode

```shell
brew install node
brew install watchman
npx react-native init AwesomeProject --version X.XX.X
npm install
gradle sync
adb devices
npm run android
```

### 前端基础

### 原生基础

### React基础知识和工程结构

index.js：默认入口文件						

App.js：RN组件

package.json：全局大管家文件		

- 基础字段：name、version、private

- 自定义脚本：scripts

  ```json
  "android_devDebug": "react-native run-android --variant=devDebug",
  "android_prdDebug": "react-native run-android --variant=prdDebug"
  ```

- 包依赖：dependencies、devDependencies

  ```shell
  npm install xxx
  npm uninstall xxx
  npm install -save-dev xxx
  ```

package-lock.json：自动生成

babel.config.js：翻译器配置				

app.json：定义工程配置常量

node_modules：npm安装的项目

##### class 组件

- 有状态（state），每次都是修改的同一个状态
- 基于生命周期的管理
- 面向对象的好处：易于理解，适合新手

##### 函数式组件（用的更多）

- 无状态，每次刷新都是生成一个新的状态
- 基于状态变化的管理
- 函数式的好处：简洁，模版代码少，易于复用

##### 函数式组件的优势和常用hook

- 函数式组件的3种写法
- props和useState管理ui数据
- 常用hook：useState、useEffect、useRef、useWindowDimension、useColorScheme

##### JSX语法：高效开发源自于此

- 拆分渲染、内联样式与样式表、样式组合
- 标准写法和简略写法
- 条件渲染、三元表达式、列表渲染、数组渲染等

##### RN计数器

### 系统组件精讲

##### View：ui构建的基石，一切页面的起点

##### Text 使用占比最高的组件，使用简单，深入复杂

字体属性：fontSize	fontFamily	fontWeight

行数以及修饰模式：numberOfLines	ellipsizeMode

是否可选中以及选中色号：selectable	selectionColor

点击和长按：onPress	onLongPress

跟随系统字号：allowFontScaling

文字嵌套及注意事项

文本对其：textAlign	textAlignVertical

文本装饰：textDecorationStyle、textDecorationLine

文字阴影：textShadowColor、textShadowOffset、textShadowRadius一起使用

##### Image构建精美ui

图片源的两种类型：source

缩放模式：resizeMode

blurRadius：曾经的难题现在如此简单

##### ImageBackground-View和Image的结合

style 和 imageStyle

ref 和 imageRef

##### TextInput唯一且强大的输入组件

字体样式：和Text一致

自动聚焦：autoFocus和focus()

自动失焦：blurOnSubmit和blur()

隐藏光标：caretHidden

默认输入：defaultValue

可编辑性：editable

键盘类型：keyboardType

确定键配置：returnKeyType

最大长度：maxLength

多行输入：multiline和numberOfLines

焦点回调：onBlur和onFocus

内容回调：onChange和onChangeText

选中相关：selection、selectionColor、selectTextOnFocus

对齐方式：textAlign和textAlignVertical

安全模式：secureTextEntry

##### TouchableOpacity最好用的点击组件

透明度渐变阈值：activeOpacity

点击事件：onPress、onLongPress、delayLongPress

点击事件起止：onPressIn、onPressOut

##### TouchableHighlight使用略显麻烦的点击组件

所有点击类事件和TouchableOpacity相同

只支持一个子节点

使用陷阱：必须复写onPress

##### TouchbaleWithoutFeedback几乎不用的点击组件

只支持一个子节点，且自身不支持样式

##### Button 使用简单但样式固定

title：设置按钮显示文字

color ：设置按钮颜色

应用场景：对按钮的UI没有要求

##### 强大的 Pressable

##### ScrollView 基础滚动组件

添加子节点：固定子元素、列表渲染、数组渲染

内容包裹样式：contentContainerStyle

滚动键盘消失：keyboardDismissMode

点击收起键盘：keyboardShouldPersistTaps

滚动开始与结束：onMomentumScrollBegin/End

滚动距离监听：onScroll（IOS：scrollEventThrottle）

超出滚动：overScrollMode

分页滚动：pagingEnabled，滚动方向：horizontal

滚动开关：scrollEnable

初始滚动：contentOffset

是否展示滚动条：showsVerticalScrollIndicator/Horizontal

吸顶元素：stickyHeaderIndices

api：scrollTo()、scrollToEnd()

##### FlatList 高性能列表组件

基础使用：data、renderItem、keyExtractor

ScrollView属性：内容容器、滚动条、滚动监听、键盘模式等

横向纵向：horizontal

表头：ListHeaderComponent

表尾：ListFooterComponent

空元素：ListEmptyComponent

分割线元素：ItemSeparatorComponent

初始渲染元素：initialNumToRender

反向：inverted

多列排布：numColumns

滚动到指定元素：scrollToIndex()、scrollToItem()不推荐

滚动指定距离：scrollToOffset()

滚动到底：scrollToEnd()

##### SectionList 多类型分组列表

基础使用：sections、renderItem、keyExtractor

ScrollView属性：内容容器、滚动条、滚动监听、键盘模式等

表头：ListHeaderComponent

表尾：ListFooterComponent

分组头部：renderSectionHeader

分割线元素：ItemSeparatorComponent

分组吸顶：stickySectionHeadersEnabled

滚动api：scrollToLocation()

##### RefreshControl 下拉刷新

下拉刷新：refreshing、onRefresh

上拉加载：onEndReached、onEndReachedThreshold

##### Modal 自定义弹窗

控制显示：visible

渲染内容：children

安卓返回关闭：onRequestClose

背景透明：transparent

状态栏透明：statusBarTranslucent

动画方式：animationType

状态回调：onShow、onDismiss

背景动画：伏笔

##### StatusBar 适配状态栏

内容深浅模式：barStyle

背景颜色：backgroundColor

动画切换：animated

透明悬浮：translucent

隐藏状态栏：hidden

api：setBackgroundColor()、setBarStyle()、setHidden()、setTranslucent()

##### Switch 开关切换

指定开关：value

状态回调：onValueChange

背景颜色：trackColor

前景颜色：thumbColor

### 常用API



### RN动画系统



### 账号密码本



