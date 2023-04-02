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

##### alert 和 console 你不知道的调试小技巧

alert()：简单的弹窗提示

区别Alert.alert()，这个是RN的对话框工具

console.log：最常用的控制台输出

console日志输出分级

console.log字符串模版和占位符三种方式：%o、%s、%d

console.log添加样式：%c颜色和字号

- color：orange
- font-size:x-large、x-medium、x-small

console.log输出组件树

console.log输出表格日志

console.log输出分组日志

##### Dimension 和 useWindowDimension 获取屏幕信息

##### Platform 获取平台属性

平台属性：OS、Version、constants

判断：isPad、isTv

平台选择：Platform.select()

##### StyleSheet 构建灵活样式表

基础使用

创建多个样式表

样式合并：StyleSheet.compose

- 和[]写法的区别：compose效率更高

样式平铺：StyleSheet.flatten

绝对填充：StyleSheet.absoluteFill

头发丝尺寸：StyleSheet.hairlineWidth

##### Linking 一个 api 节省 50 行代码

打开链接：openURL()、canOpenURL()

跳转应用设置：Linking.openSettings()

安卓隐式跳转：Linking.sendIntent()

获取初始化url：getInitialURL()

##### PixelRatio 像素比例工具

获取屏幕像素密度：PixelRatio.get()

获取字体缩放比例（仅安卓）：PixelRatio.getFontScale()

获取布局大小：PixelRatio.getPixelSizeForLayoutSize()

获取就近值：PixelRatio.roundToNearestPixel()

##### BackHandler 安卓返回键适配

添加监听：BackHandler.addEventListener()

移除监听：BackHandler.removeEventListener()

退出应用：BackHandler.exitApp()

社区hook：@react-native-community/hooks

##### PermissionsAndroid 轻松解决权限问题

检查权限：PermissionsAndroid.check()

申请权限：PermissionsAndroid.request()

- 切记原生manifest注册权限

申请多个权限：PermissionsAndroid.requestMultiple()

##### Vibration 简单好用的震动交互

原生申明权限：android.permission.VIBRATE

发起震动：Vibration.vibrate()

Android时间模式：[100,500,200,500]

iOS时间模式：[100,200,300,400]

重复震动：repeat

##### ToastAndroid 安卓平台的提示

弹出提示：ToastAndroid.show()

弹出提示：ToastAndroid.showWithGravity()

偏移量：ToastAndroid.showWithGravityAndOffset()

##### Transform 矩阵变换的伪3D效果

水平移动：translateX

垂直移动：translateY

X轴旋转：rotateX

Y轴旋转：rotateY

Z轴旋转：rotateZ、rotate

##### Keyboard 键盘操作有神器

注册键盘监听：Keyboard.addListener()

注销键盘监听：EmitterSubscription.remove()

隐藏键盘：Keyboard.dismiss()

### RN动画系统

##### 简单示例学习基础动画方法

演示一个简单的平移动画效果

使用 Animated.View、Animated.Value、useRef

##### 四大动画类型

平移、旋转、缩放、渐变

##### 六种支持动画的组件

Animated.Image

Animated.View

Animated.ScrollView

Animated.FlatList

Animated.Text

Animated.SectionList

##### 平移动画的多种属性支持

marginLeft、marginRight、marginTop、marginBottom

translateX、translateY

left、right、top、bottom

##### Animated.decay 衰减动画函数

velocity：初始速度

deceleration：衰减系数

##### Animated.spring 弹性动画函数

toValue：目标值

弹性模型：三组配置：bounciness	tension	|	speed	friction	|	stiffness	damping	mass

bounciness（弹性）：控制弹性，越大越弹，默认值18

speed（速度）：控制弹的速度，默认值12

tension（张力）：控制速度，越大速度越快，默认值40

friction（摩擦）：控制弹性与过冲，越小越弹，默认值17

stiffness（刚度）：弹簧刚度系数，越大越弹，默认为100

damping（阻尼）：弹簧运动因摩擦力而收到阻尼，越小越弹，默认值为10

mass（质量）：附着在弹簧末端的物体的质量，越大惯性越大，动画越难停下，越小惯性越小，动画很快停下，默认值1

其他弹性参数：

velocity（速度）：附着在弹簧上物体的初始速度，默认值0

overshootClamping（过冲）：弹簧是否应夹紧而不应弹跳，默认为false

restDisplacementThreshold（恢复位移阈值）：从静止状态开始的位移阈值，低于该阈值，弹簧应被视为静止状态，默认为0.001

restspeedthread（弹簧静止速度），单位为像素/秒，默认为0.001

delay（延迟）：延迟后启动动画，默认为0

##### Animated.timing 时间动画函数

easing：时间动画函数

- 四种内置动画：
  - back	回拉
  - bounce	弹跳
  - ease	平缓
  - elastic	弹性
- 三种标准函数：
  - linear	一次方函数
  - quad	二次方函数
  - cubic	三次方函数
- 四种补充函数：
  - bezier	贝塞尔：https://cubic-bezier.com
  - circle	环形
  - sin	正弦
  - exp	指数
- 自由组合动画函数：
  - Easing.in(Easing.bounce)：加速+弹跳
  - Easing.out(Easing.exp)：减速+指数
  - Easing.inOut(Easing.elastic)：加减速+弹性
- 所有组合函数效果
  - https://easings.net/

##### Animated.ValueXY 矢量动画

矢量属性：ValueXY

##### 四种组合动画

Animated.parallel()	并发

Animated.sequence()	序列

Animated.stagger()	有序/交错

Animated.delay()	延迟

##### 跟随动画延迟难题

传统写法及问题所在

跟随动画零延迟的实现

##### 自定义 Modal 背景动画

slide动画，自定义渐变

fade动画，自定义平移

##### LayoutAnimation 超级简单又强大的布局动画

Android 手动启动布局动画

布局动画的应用场景和优势

学习几个演示案例

### 账号密码本

##### 初始化项目和页面框架

新建并初始化工程：新建、安装、编译、运行

完成基础项目配置：名称、图标

完成基础页面框架：删除demo代码，重写主页标题栏

##### 自定义封装添加账号弹窗

首页增加添加按钮，调起弹窗

自定义添加账号弹窗、封装事件、ref转发

实现弹窗ui

##### 使用 UUID和AsyncStorage 保存账号数据

生成唯一id

- react-native-get-random-values

AsyncStorage：数据存储

- 集成async-storage

   @react-native-async-storage/async-storage

- 保存数据：AsyncStorage.setItem()

- 读取数据：AsyncStorage.getItem()

##### 绘制账号列表 ui

##### 账号列表实现展开收起功能

##### 添加账号后实时刷新列表

##### 实现账号列表细节交互

点击列表行，跳转账号详情并支持修改

增加长按删除交互

标题栏增加密码显示开关

##### 项目打包发布

创建应用签名

编写打包脚本

打release包并安装发布



