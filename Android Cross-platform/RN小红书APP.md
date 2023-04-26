### 了解ReactNative优势和本套课程的学习目标

跨平台的优势和机遇

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

### 开发环境搭建和 demo 运行

官网：https://reactnative.dev/

VSCode

```shell
# 安装node和watchman
brew install node
# watchman：用来监视文件并且记录文件的改动情况
brew install watchman
# 使用cli命令创建ReactNative工程，并启动运行
npx react-native init AwesomeProject
npx react-native init AwesomeProject --version X.XX.X
# 安装js依赖，简写npm i
npm install
# 安装原生依赖
gradle sync
adb devices
# 运行Android程序
npm run android
# Mac下 .bash_profile 和 .zshrc 两者之间的区别
# .zshrc在电脑每次自启时都会生效（永久有效）
# .bash_profile中修改的环境变量只对当前窗口有效
```

### 前端基础

##### 学习js的基本使用，了解如何申明变量、函数、以及js的一些基本特性

##### 了解什么是ES5、ES6，掌握ES6常用的一些方法

EcmaScript5 EcmaScript6

- 变量申明：var let const
- 对象属性简写
- Object.assign对象合并
- 解构赋值
- 展开运算符
- 模板字符串
- Promise
- import和export

##### 了解什么是babel以及常见的配置

Babel是一个JavaScript编译器。就像其他编译器一样，编译过程分为三个阶段：解析、转换和打印输出。

也是一个工具链，主要用于将ES6+版本的代码甚至是react JSX语法转换为向后兼容的JavaScript语法，以便能够运行在当前和旧版本的浏览器或其他环境中。

##### 学习CSS-flex布局基础，为后面正式学习rn布局打下基础

横向和纵向布局：direction

主轴和交叉轴对齐：justifyContext、alignItems

元素的放大和缩小：flexGrow、flexShrink

##### npm是什么？npm如何检索、安装和卸载

##### 使用nrm管理npm源

安装nrm，切换taobao源

```shell
npm i -g nrm
nrm ls
nrm add taobao https://registry.npm.taobao.org/
nrm use taobao
nrm test taobao
```

### 原生基础

如何连接安卓设备以及开发中常用的adb命令

移动端ui结构和设备特性

RN开发中常见的原生文件

RN和原生组件对应关系

原生开发语言选择

移动端应用发布及主流的应用市场

移动端特有的生产热修复机制和应用场景

移动端设备的版本兼容选择

### React基础知识和工程结构

##### 工程目录结构，了解关键文件作用

index.js：默认入口文件

App.js：RN组件

package.json：全局大管家文件

package-lock.json：npm i自动生成，锁定版本

babel.config.js：翻译器配置				

app.json：定义工程配置常量

node_modules：npm安装的项目，里面的文件可以改，改完不能npm i

##### 构建通用源码目录结构，好的工程从模块划分开始

职责清晰：每个目录划分都规定具体的职责

功能全面：包含ui、数据、网络、常量、工具、环境等

独立接耦：一级目录之间没有职责交叉和耦合

src：modules、components、assets、constants、env、hooks、stores、utils、apis、theme

##### package.json全局大管家文件

- 基础字段：name、version、private

  ```js
  import {name, version} from './package.json';
  console.log(`name=${name},version=${version}`)
  ```

- 自定义脚本：scripts，使用 npm run 可以运行

  npm run android

  ```groovy
  flavorDimensions "type"
  productFlavors{
    dev{
      resValue("string", "app_name", "demo_dev")
    }
    prd{
      resValue("string", "app_name", "demo_prd")
    }
  }
  ```

  ```json
  "android_devDebug": "react-native run-android --variant=devDebug",
  "android_prdDebug": "react-native run-android --variant=prdDebug"
  ```

- 包依赖：dependencies、devDependencies

  dependencies：真实运行需要依赖的包

  devDependencies：开发阶段需要依赖的包

  ```shell
  npm install xxx
  npm uninstall xxx
  npm install -save-dev xxx
  ```


##### import和export，三种导入导出类型

import export json

```json
{
  "name": "jerry",
  "age": 12,
  "level": "top",
  "sex": true
}
```

```js
import Config from './src/constants/Config.json';
console.log(JSON.stringify(Config))
console.log(`name=${Config.name},age=${Config.age}`)
```

import export function

```js
// StringUtil.js
function test(){
  console.log('test...')
}
export default test;
// 2 导出函数
export function test2(){
  console.log('test...')
}
```

```js
import StringUtil from './src/utils/StringUtil';
test();
// 2
import {test, test2} from './src/utils/StringUtil';
test2();
```

import export view

```js
// TestView.js
import React from "react";
import { View } from "react-native";
export default () => {
  return (
  	<View style={{width:200, height:200, backgroundColor: 'blue'}}/>
  );
}
```

```js
import TestView from './src/components/TestView';
AppRegistry.registerComponent(appName, () => TestView);
```

##### class 组件的标准写法和生命周期

| class组件                                 | 函数式组件（用的更多）                   |
| ----------------------------------------- | ---------------------------------------- |
| 有状态（state），每次都是修改的同一个状态 | 无状态，每次刷新都是生成一个新的状态     |
| 基于生命周期的管理                        | 基于状态变化的管理                       |
| 面向对象的好处：易于理解，适合新手        | 函数式的好处：简洁，模版代码少，易于复用 |

- class 组件的标准写法和生命周期

```js
// ClassView.js
import React from "react";
import { View } from "react-native";

class ClassView extends React.Component{
  constructor(props){
    super(props);
    console.log("constructor ...");
  }
  
  componentDidMount(){
    // 加载成功，在render后执行
    console.log("componentDidMount ...");
  }
  
  componentWillUnmount(){
    console.log("componentWillUnmount ...");
  }
  
  render(){
    console.log("render ...");
    return (
    	<View style={{width:200, height:200, backgroundColor: 'blue'}}>
      
      </View>
    )
  }
}
export default ClassView;
```

```js
// App.js
import ClassView from './src/components/ClassView';

const App = () => {
  const [showClassView, setShowClassView] = useState(true);
  
  useEffect(() => {
    setTimeout(() => {
      setShowClassView(false);
    }, 2000);
  }, []);
  
  return (
  	<SafeAreaView>
    	<StatusBar
    		barStyle={'dark-content'}
				backgroundColor='#FFFFFF'
    	/>
    	<View style={styles.container}>
    		{ showClassView && <ClassView /> }
    	</View>
    </SafeAreaView>
  )
}
```

- props与state管理ui数据

props：外部传入的数据

state：内部引用的状态

- state和setState

```js
// ClassView.js
import React from "react";
import { View, Text } from "react-native";

class ClassView extends React.Component{
  constructor(props){
    super(props);
    // 解构
    const { name, age, level, sex} = props;
		console.log(`constructor name=${name},age=${age}`) 
    this.state ={
      address: "上海市"
    };
  }
  
  componentDidMount(){
    setTimeout(() => {
      this.setState(
      	address: "川沙"
      );
    }, 2000);
  }
  
  render(){
    const { name, age, level, sex} = this.props;
    const { address } = this.state;
		console.log(`render name=${name},age=${age}`) 
    return (
    	<View style={{width:200, height:200, backgroundColor: '#00bcd4'}}>
      	<Text style={{ fontSize:20, color: 'white' }}>
          {`render name=${name},age=${age}`}
        </Text>
        <Text style={{ fontSize:20, color: 'black' }}>
          {`render address=${address}`}
        </Text>
      </View>
    )
  }
}
export default ClassView;
```

```js
// App.js
import ClassView from './src/components/ClassView';

const App = () => {
  return (
  	<SafeAreaView>
    	<StatusBar
    		barStyle={'dark-content'}
				backgroundColor='#FFFFFF'
    	/>
    	<View style={styles.container}>
    		<ClassView name="jerry" age={12} level="top" sex={true} />
    	</View>
    </SafeAreaView>
  )
}
```

##### 函数式组件的优势和常用hook

- 函数式组件的3种写法

```js
// FunctionView.js
import React from "react";
import { View, Text } from "react-native";

function FunctionView() { 
  return {
    <View style={{width:200, height:200, backgroundColor: '#00bcd4'}}>
    	
    </View>
  };
}

// 箭头函数
const FunctionView2 = () => { 
  return {
    <View style={{width:200, height:200, backgroundColor: '#00bcd4'}}>
    	
    </View>
  };
}
export default FunctionView;

// 匿名导出
export default () => { 
  return {
    <View style={{width:200, height:200, backgroundColor: '#00bcd4'}}>
    	
    </View>
  };
}
```

```js
// App.js
import FunctionView from './src/components/FunctionView';

const App = () => {
  return (
  	<SafeAreaView>
    	<StatusBar
    		barStyle={'dark-content'}
				backgroundColor='#FFFFFF'
    	/>
    	<View style={styles.container}>
    		<FunctionView />
    	</View>
    </SafeAreaView>
  )
}
```

- props和useState管理ui数据

```js
// FunctionView.js
import React, { useState, useEffect } from "react";
import { View, Text } from "react-native";

// 匿名导出
export default (props) => { 
  // 解构
  const { name, age, level, sex} = props;
  
  const [address, setAddress] = useState("上海市");
  // []放监听对象，为空第一次渲染会回调
  useEffect(() => {
		console.log(`useEffect ...`)
    setTimeout(() => {
      setAddress("川沙");
    }, 2000);
  }, []);
  
  useEffect(() => {
		console.log(`update address=${address} ...`)
  }, [address]);
  
	console.log(`return ...`)
  return {
    <View style={{width:200, height:200, backgroundColor: '#00bcd4'}}>
    	<Text style={{ fontSize:20, color: 'white' }}>
      	{`name=${name},age=${age}`}
      </Text>
      <Text style={{ fontSize:20, color: 'yellow' }}>
      	{`address=${address}`}
      </Text>
    </View>
  };
}
```

```js
// App.js
import FunctionView from './src/components/FunctionView';

const App = () => {
  return (
  	<SafeAreaView>
    	<StatusBar
    		barStyle={'dark-content'}
				backgroundColor='#FFFFFF'
    	/>
    	<View style={styles.container}>
    		<FunctionView name="jerry" age={12} level="top" sex={true} />
    	</View>
    </SafeAreaView>
  )
}
```

- 常用hook：useState、useEffect、useRef、useWindowDimension、useColorScheme

```js
// FunctionView.js
import React, { useState, useEffect, useRef, useWindowDimension, useColorScheme } from "react";
import { View, Text, ScrollView } from "react-native";

export default (props) => { 
  const { name, age, level, sex} = props;
  const scrollViewRef = useRef(null);
  useEffect(() => {
    setTimeout(() => {
      scrollViewRef.current.scrollToEnd({animted: true});
    }, 2000);
  }, []);
  // 获取屏幕宽高
  const {width, height} = useWindowDimension();
  console.log(`useWindowDimension width=${width},height=${height} ...`)
  // 皮肤跟随系统
  const colorScheme = useColorScheme();
  console.log(`useColorScheme colorScheme=${colorScheme} ...`)
  return {
    <View style={{width:200, height:200, backgroundColor: '#00bcd4'}}>
    	<Text style={{ fontSize:20, color: 'white' }}>
      	{`name=${name},age=${age}`}
      </Text>
      <ScrollView ref={scrollViewRef}>
      	<Text style={{ fontSize: 20, color: 'white', marginVertical: 12 }}>AAA</Text>
      	<Text style={{ fontSize: 20, color: 'white', marginVertical: 12 }}>AAA</Text>
      	<Text style={{ fontSize: 20, color: 'white', marginVertical: 12 }}>AAA</Text>
      	<Text style={{ fontSize: 20, color: 'white', marginVertical: 12 }}>AAA</Text>
      </ScrollView>
    </View>
  };
}
```

##### JSX语法：高效开发源自于此

- 拆分渲染（方法和组件）、内联样式与样式表、样式组合

```js
// FunctionView.js
import React, { useState, useEffect } from "react";
import { View, Text } from "react-native";

export default (props) => { 
  const { name, age, level, sex} = props;
  const renderProps = () => {
    return (
    	<Text style={{ fontSize:20, color: 'white' }}>
      	{`name=${name},age=${age}`}
      </Text>
    );
  }
  const renderAddress = () => {
  	return (
  		<Text style={{ fontSize:20, color: 'yellow' }}>
      	{`address=${address}`}
      </Text>
  	)
  }
  return {
    <View style={{width:200, height:200, backgroundColor: '#00bcd4'}}>
    	{renderProps()}
      {renderAddress()}
    </View>
  };
}
```

props传View；（属性值、属性View和子View）

```js
export default (props) => { 
  const { name, age, level, sex, levelView} = props;
  return {
    <View style={{width:200, height:200, backgroundColor: '#00bcd4'}}>
      // 通过levelView={getLevelView()}来传递
      {levelView()}
      // 传子View
      {props.children}
    </View>
  };
}
```

内联样式与样式表

```js
// FunctionView.js
import React, { useState, useEffect } from "react";
import { View, Text, StyleSheet } from "react-native";

export default (props) => { 
  const { name, age, level, sex} = props;
  return {
    <View style={styles.root}>
    	<Text style={[styles.txt, styles.txtBold]}>
      	{`name=${name},age=${age}`}
      </Text>
    </View>
  };
}
const styles = StyleSheet.create({
	root: {
		width:200,
		height:200, 
		backgroundColor: '#00bcd4'
	},
	txt: {
		fontSize:20,
		color: 'white'
	},
	txtBold: {
		fontWeight: 'bold',
	},
	txtBlue: {
		color: 'blue'
	},
})
```

- 标准写法和简略写法

```js
// FunctionView.js
import React, { useState, useEffect } from "react";
import { View, Text } from "react-native";

export default (props) => { 
  const { name, age, level, sex} = props;
  // 标准写法
  const renderProps = () => {
    return (
    	<Text style={{ fontSize:20, color: 'white' }}>
      	{`name=${name},age=${age}`}
      </Text>
    );
  }
  // 简略写法
  const renderProps = () => (
    	<Text style={{ fontSize:20, color: 'white' }}>
      	{`name=${name},age=${age}`}
      </Text>
    )
  // 更简略写法，如index.js
  const renderProps = () => 
  		<Text style={{ fontSize:20, color: 'white' }}>
      	{`name=${name},age=${age}`}
      </Text>
  return {
    <View style={{width:200, height:200, backgroundColor: '#00bcd4'}}>
    	{renderProps()}
    </View>
  };
}
```

- 条件渲染、三元表达式、列表渲染、数组渲染等

```js
// FunctionView.js
import React, { useState, useEffect } from "react";
import { View, Text, ScrollView } from "react-native";

export default (props) => {
  const { name, age, level, sex} = props;
  const [levelUp, setLevelUp] = useState(false);
  useEffect(() => {
    setTimeout(() => {
      setLevelUp(true);
    }, 2000);
  }, []);
  const renderProps = () => (
    	levelUp?
    	<Text style={{ fontSize:20, color: 'white' }}>
      	{`name=${name},age=${age}`}
      </Text> :
      <Text style={{ fontSize:20, color: 'black' }}>
      	{`name2=${name},age2=${age}`}
      </Text>
    )
  const renderProps = () => (
    	levelUp &&
    	<Text style={{ fontSize:20, color: 'white' }}>
      	{`name=${name},age=${age}`}
      </Text>
    )
  const array = ['AAA', 'BBB', 'CCC']
  const getListView = () => {
  	const viewList = [];
  	for(let i = 0; i < 5; i++){
  		viewList.push(<Text style={{ fontSize: 20}}>`List item ${i}`</Text>);
  	}
  	return viewList;
  }
  return {
    <View style={{width:200, height:200, backgroundColor: '#00bcd4'}}>
    	<Text style={{ fontSize:20, color: 'white' }}>
      	{`levelUp=${levelUp?'高级':'低级'}`}
      </Text>
      {renderProps()}
      <ScrollView>
      	{array.map((item) => {
      		return (
      			<Text style={{ fontSize: 20}}>{item}</Text>
      		);
      	})}
      	// 简写
      	{array.map((item) => <Text style={{ fontSize: 20}}>{item}</Text>)}
      </ScrollView>
      // 数组渲染
      <ScrollView>
      	{getListView()}
      </ScrollView>
    </View>
  };
}
```

##### RN计数器

分别用class组件和函数式组件实现计数器练习；注意闭包的坑

- 计数器效果展示
- 使用函数式组件、setInterval定时器实现演示效果

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

- alert()：简单的弹窗提示

- 区别Alert.alert()，这个是RN的对话框工具，较少使用

- console.log：最常用的控制台输出

- console日志输出分级

- console.log字符串模版和占位符三种方式：%o、%s、%d

- console.log添加样式：%c颜色和字号

  - color：orange

  - font-size:x-large、x-medium、x-small


- console.log输出组件树

- console.log输出表格日志

- console.log输出分组日志

```js
alert('这是一条提示信息');
alert(123);
alert(false);

const buttons = [
  {text: '取消', onPress: () => console.log('取消')},
  {text: '确定', onPress: () => console.log('确定')},
];
Alert.alert('这是标题', '这是一条提示信息', buttons);

console.log('这是普通的日志输出');
console.info('信息日志输出');
console.debug('调试日志输出');
console.warn('警告日志输出');
console.error('错误日志输出');

console.log('我是个人开发者%s，我学习RN%d年半了', '张三', 2);
const obj = {name: '张三', age: 12};
console.log('我是一个对象:%o', obj);

// 在浏览器才会有效果
console.log('%c这行日志红色文字，字号大', 'color:red; font-size:x-large');
console.log('%c这行日志蓝色文字，字号中', 'color:blue; font-size:x-medium');
console.log('%c这行日志绿色文字，字号小', 'color:green; font-size:x-small');

// 调试时很有用
const viewLayout = (
    <View style={{ flexDirection: 'column' }}>
        <Text style={{ fontSize: 20, color: 'red' }} >
            文字显示
        </Text>
    </View>
);
console.log(viewLayout);

// 在浏览器才会有效果
const users = [
    {name: '张三', age: 12, hobby: '唱歌'},
    {name: '李四', age: 15, hobby: '跳舞'},
    {name: '王武', age: 18, hobby: '打篮球'},
];
console.table(users);

console.group();
console.log('第1行日志');
console.log('第2行日志');
console.log('第3行日志');
  console.group();
  console.log('二级分组第1行日志');
  console.log('二级分组第2行日志');
  console.log('二级分组第3行日志');
  console.groupEnd();
console.groupEnd();
```

##### Dimension 和 useWindowDimension 获取屏幕信息

- 获取屏幕宽高的两种方式
- 获取缩放、文字缩放
- Dimension.get传screen和window的区别：screen比window默认多状态栏高度
- addEventListener监听屏幕信息变化

```js
const { width, height, scale, fontScale } = Dimensions.get('window');
console.log(`width=${width}, height=${height}`);
console.log(`scale=${scale}, fontScale=${fontScale}`);
// hook 不能写在函数内
const { width, height, scale, fontScale } = useWindowDimensions();
console.log(`width=${width}, height=${height}`); 
console.log(`scale=${scale}, fontScale=${fontScale}`);

useEffect(() => {
    const subcription = Dimensions.addEventListener('change', (window, screen) => {
        console.log(window);
        console.log(screen);
    });
    return () => {
        subcription.remove();
    }
}, []);
```

##### Platform 获取平台属性

平台属性：OS、Version、constants

判断：isPad、isTv

平台选择：Platform.select()

```js
console.log(Platform.OS);
console.log(Platform.Version);
console.log(Platform.constants);
console.log(Platform.isPad);
console.log(Platform.isTV);

// 可以写在StyleSheet内
const style = Platform.select({
    android: {
        marginTop: 20,
    },
    ios: {
        marginTop: 0,
    },
    default: {
        marginTop: 10,
    },
});
console.log(style);
```

##### StyleSheet 构建灵活样式表

- 基础使用

- 创建多个样式表

- 样式合并：StyleSheet.compose
  - 和[]写法的区别：compose效率更高，可以定位到同一个对象


- 样式平铺：StyleSheet.flatten

- 绝对填充：StyleSheet.absoluteFill

- 头发丝尺寸：StyleSheet.hairlineWidth

```js
const styles = StyleSheet.create({})

const s1 = {
    fontSize: 18,
};
const s2 = {
    fontSize: 20,
    color: 'red',
};
const composeStyle = StyleSheet.compose(s1, s2);
console.log(composeStyle);

// 重复属性后面覆盖前面
const flattenStyle = StyleSheet.flatten([s1, s2]);
console.log(flattenStyle);

console.log(StyleSheet.absoluteFill);
// ...StyleSheet.absoluteFill 使用在 StyleSheet.create

console.log(StyleSheet.hairlineWidth); // 1px
console.log(1 / Dimensions.get('screen').scale);
```

##### Linking 一个 api 节省 50 行代码

- 打开链接：openURL()、canOpenURL()

  网页链接、地图定位、拨打电话、发送短信、发送邮件、应用跳转

- 跳转应用设置：Linking.openSettings()

- 安卓隐式跳转：Linking.sendIntent()

- 获取初始化url：getInitialURL()

```js
if (Linking.canOpenURL('https://www.baidu.com/')) {
  	// 跳转系统浏览器
    Linking.openURL('https://www.baidu.com/');
}
Linking.openURL('geo:37.2122, 12.222');
Linking.openURL('tel:10086');
Linking.openURL('smsto:10086');
Linking.openURL('mailto:10086@qq.com');
// 定义 action="View" category="DEFAULT&BROWSABLE" scheme="dagongjue" host="demo"
Linking.openURL('dagongjue://demo?name=李四');
Linking.openSettings();

if (Platform.OS === 'android') {
  	// 定义 action="com.dagongjue.demo.test"
    Linking.sendIntent('com.dagongjue.demo.test', [{key: 'name', value: '王武'}]);
}
// 前提应用是通过URL跳转过来的
console.log(Linking.getInitialURL());
```

##### PixelRatio 像素比例工具

- 获取屏幕像素密度：PixelRatio.get()

- 获取字体缩放比例（仅安卓）：PixelRatio.getFontScale()

- 获取布局像素大小：PixelRatio.getPixelSizeForLayoutSize()

- 获取就近值：PixelRatio.roundToNearestPixel()

```js
console.log(PixelRatio.get());
console.log(PixelRatio.getFontScale());
console.log(
    PixelRatio.getPixelSizeForLayoutSize(200)
);

const styles = StyleSheet.create({
    subView: {
        width: '100%',
        backgroundColor: 'green',
        height: PixelRatio.roundToNearestPixel(32.1),
    },
});
```

##### BackHandler 安卓返回键适配

添加监听：BackHandler.addEventListener()

移除监听：BackHandler.removeEventListener()

退出应用：BackHandler.exitApp()

社区hook：@react-native-community/hooks

```js
useEffect(() => {
    BackHandler.addEventListener('hardwareBackPress', backForAndroid)
    return () => {
        BackHandler.removeEventListener('hardwareBackPress', backForAndroid);
    }
}, []);

const backForAndroid = () => {
  	// false 不处理，true 需要消费
    return false;
}

BackHandler.exitApp();

// 社区hook，简化了上面的模版代码
npm install @react-native-community/hooks
import { useBackHandler } from '@react-native-community/hooks'
useBackHandler(()=> { return true })
```

##### PermissionsAndroid 轻松解决权限问题

- 检查权限：PermissionsAndroid.check()

- 申请权限：PermissionsAndroid.request()
  - 切记原生manifest注册权限


- 申请多个权限：PermissionsAndroid.requestMultiple()

```js
const needPermission = PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE;
PermissionsAndroid.check(
    needPermission
).then(result => {
    if (!result) {
        PermissionsAndroid.request(needPermission).then(status => {
            console.log(status);
            if (status === 'granted') {
                //获得
            } else if (status === 'denied') {
                //拒绝
            }
        });
    }
});
console.log(PermissionsAndroid.PERMISSIONS);

PermissionsAndroid.requestMultiple([
    PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE,
    PermissionsAndroid.PERMISSIONS.READ_EXTERNAL_STORAGE,
]);
```

##### Vibration 简单好用的震动交互

- 原生申明权限：android.permission.VIBRATE

- 发起震动：Vibration.vibrate()

- Android时间模式：[100,500,200,500]

- iOS时间模式：[100,200,300,400]

- 重复震动：repeat

```js
Vibration.vibrate();
Vibration.vibrate(1000);//just Android,iOS单次400ms
Vibration.cancel();
// Android，停100震500停200震200
Vibration.vibrate([100, 500, 200, 500]);
// iOS，指定停顿的时间
Vibration.vibrate([100, 200, 300, 400]);
// 循环震动
Vibration.vibrate([100, 200, 300, 400], true);
```

##### ToastAndroid 安卓平台的提示

- 弹出提示：ToastAndroid.show()

- 弹出提示：ToastAndroid.showWithGravity()

- 偏移量：ToastAndroid.showWithGravityAndOffset()

```js
ToastAndroid.show('这是一个提示', ToastAndroid.SHORT);
ToastAndroid.show('这是一个提示', ToastAndroid.LONG);
ToastAndroid.showWithGravity(
    '这是一个提示',
    ToastAndroid.LONG,
    ToastAndroid.TOP
);
ToastAndroid.showWithGravity(
    '这是一个提示',
    ToastAndroid.LONG,
    ToastAndroid.TOP,
    100, 200,
);
```

##### Transform 矩阵变换的伪3D效果

- 水平移动：translateX

- 垂直移动：translateY
- 整体缩放：scale
- 横向缩放：scaleX
- 纵向缩放：scaleY

- X轴旋转：rotateX

- Y轴旋转：rotateY

- Z轴旋转：rotateZ、rotate

```js
<View
    style={[
        {
            width: 100,
            height: 100,
            backgroundColor: '#3050ff',
            marginTop: 60,
            marginLeft: 60,
        },
        {
            transform: [
                // {translateX: 200}
                // {translateY: 150}
                {scale: 1.5},
                // {scaleX: 1.5},
                // {scaleY: 1.5}
                {rotateX: '45deg'},
                // {rotateY: '45deg'},
                {rotateZ: '45deg'}
                // {rotate: '45deg'},
            ],
        }
    ]}
/>
```

##### Keyboard 键盘操作有神器

注册键盘监听：Keyboard.addListener()

注销键盘监听：EmitterSubscription.remove()

隐藏键盘：Keyboard.dismiss()

```js
useEffect(() => {
    const showSubscription = Keyboard.addListener('keyboardDidShow', onKeyboardShow);
    const hideSubscription = Keyboard.addListener('keyboardDidHide', onKeyboardHide);
    return () => {
        showSubscription.remove();
        hideSubscription.remove();
    }
}, []);
const onKeyboardShow = () => {
    console.log('键盘出现');
}
const onKeyboardHide = () => {
    console.log('键盘隐藏');
}

Keyboard.dismiss();
```



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



### TypeScript快速进阶

##### TypeScript 介绍和优势

- JS的超集，JS有的TS都有，能运行JS就能运行TS

- 强大的类型系统提升了代码可阅读性、可维护性

- 类型推断、类型检查、代码提示

- 有助于在团队中推动严格的代码检查

##### TypeScript 安装和项目配置

```shell
# 安装TypeScript
npm i --save-dev typescript
# 生成tsconfig.json
tsc --init
# 安装类型申明（众多）
npm i --save-dev @types/react @types/react-native
```

##### number、string、boolean 三大基础类型

- 数值类型：number
- 字符串类型：string
- 布尔类型：boolean

```typescript
const num1:number = 12;
const num2:number = 12.32;
const add = (n1:number, n2:number): number => {
    return n1 + n2;
}
console.log(add(7, 8));
const num4: Number = new Number(13);
console.log(num4.valueOf())
const s1: string = 'hello'
const s2: string = `${s1}, my name is jerry.`
console.log(2 + '3')
const s3 = new String("String Object");
console.log(s3.valueOf())
const b1: boolean = true;
const b2: boolean = false;
const b3: boolean = !!null;
const b4: boolean = !!undefined;
const b5: boolean = 4 > 5;
const b6: Boolean = new Boolean(4 > 5);
console.log(b6.valueOf())
```

##### 数组、元组、枚举类型的使用

- 数组类型：Array
- 元组类型
- 枚举类型

```typescript
const a1: number[] = [1, 2, 3];
const a2: Array<number> = [1, 2, 3];
const a3: Array<number> = new Array(5);
const a4: Array<number | string> = new Array();
console.log(a1);
a3[1] = 12;
a4.push(3);
const t1: [string, number, boolean] = ['jerry', 12, true];
console.log(t1);
console.log(t1[2]);
enum Job {
    Teacher, Programmer, Cook
}
enum City {
    NanJing = '南京', WuXi = '无锡', HangZhou = '杭州'
}
console.log(Job.Programmer);
```

##### 函数类型

- 基础的函数申明
- 函数参数类型和返回值类型的申明
- 函数的可选参数和默认参数

```typescript
const f1: () => void = () => {
    console.log('f1 ...');
}
f1();
const f2: (s: sting) => void = () => {
    console.log(`f2 ${s} ...`);
}
f2('hello');
const add = (n1:number, n2:number): number => {
    return n1 + n2;
}
console.log(add(7, 8));
const f3: (name: string, age?: number) => void = (name: string, age?: number) => {
    console.log(`my name is ${name}, ${age || 0} years old.`);
}
f2('hello', 2);
const f4 = (name: string, age: number = 100) => {
    console.log(`my name is ${name}, ${age} years old.`);
}
f4('hello');
```

##### 类型文件和命名空间

- 局部类型文件和全局类型文件

  ```typescript
  // 局部类型文件
  // @types/index.d.ts
  type Student = {
      name: string;
      age: number;
      hobby?: string[];
  }
  
  // TSDemo.tsx
  const student: Student = {
      name: 'jerry',
      age: 12,
      hobby: undefine,
  } as Student;
  console.log(student);
  
  // 全局类型文件
  // typings.d.ts
  type Student = {
      name: string;
      age: number;
      hobby?: string[];
  }
  ```

- 使用命名空间

  ```typescript
  // @types/index.d.ts
  declare namespace Info {
      type Dog = {
          name: string;
          age: number;
      }
  }
  
  // TSDemo.tsx
  const dog: Info.Dog = {
      name: 'jerry',
      age: 12,
  } as Info.Dog
  ```

- 使用类型文件中的类型定义数据



### Context上下文

##### Context 上下文介绍和演示

在一个典型的React应用中，数据是通过**props属性逐层传递**，这种做法对于某些数据而言是极其繁琐的（如：登陆信息，UI主题），这些数据应用中许多组件都需要；而Context提供了一种在**组件间共享值**的方式，而不必显式地通过组件树逐层传递

##### Context 实例演示应用主题配置

- 分析实现效果，思考传统实现思路及问题

- 对比Context实现方式，体会Context的简洁和解耦

```tsx
// ThemeContext.tsx
import { createContext, useContext, useState } from 'react';
export const ThemeContext = createContext<string>('dark')
// use ThemeContext
import { ThemeContext } from './ThemeContext';
// 根节点
export default () => {
  return (
    <ThemeContext.Provider value='dark'>
      <View style={{ width: '100%' }}>
        <PageView />
      </View>
    </ThemeContext.Provider>
  )
}
// 叶子结点取值
const theme = useContext(ThemeContext);
// 动态修改
const [theme, setTheme] = useState<string>('dark');
```

- 使用state维护动态Context值

##### Context 内容小结

使用Context的思考：

- 因为Context本质上就是全局变量，大量使用Context会导致组件失去独立性，使组件复用性变差。

- 对于常规的组件间传值，可优先考虑组件组合、状态管理、单例导出等方式，不要过度使用Context。
- 使用Context实现全局登录信息传递

内容小结：

- Context传递全局数据，相比传统的通过props组件树逐级传递，代码更少，更加简洁，降低耦合。
- 大量使用Context会导致组件失去独立性，使组件复用性变差，除必要场景外，应优先思考其他解耦方案。



### HOC高阶组件

##### HOC 高阶组件介绍

- 什么是高阶函数？

  如果一个函数接受的参数为函数，或者返回值是一个新函数，则该函数就是高阶函数。

- 什么是高阶组件？

  如果一个组件的参数是组件，返回值是一个新组件，则该组件就是高阶组件。

- 高阶组件应用场景：解决什么问题？

  使用HOC高阶组件解决横切关注点问题，使得组件功能更加单一，组件逻辑服务组件ui，从而降低耦合性，增强组件的复用性。

##### HOC 高阶组件案例演示1

Hack渲染函数：首页添加按钮

```typescript
// withFloatButton.tsx
// 使用的时候包裹View就可以
import React from "react";
import { TouchableOpacity, Image, StyleSheet } from "react-native";

type IReactComponent = 
	React.ClassicComponentClass 
	| React.ComponentClass 
	| React.FunctionComponent 
	| React.ForwardExoticComponent<any>;

import icon_add from '../assets/images/icon_add.png'

export default <T extends IReactComponent>(OriginView: T): T => {
    const HOCView = (props: any) => {
        return (
        	<>
            	<OriginView {...props}/>
    			<TouchableOpacity
    				style={styles.addButton}
    				onPress={() => {
                        console.log(`onPress ...`);
                    }}
    			>
            		<Image style={styles.addImg} source={icon_add}/>
            	</TouchableOpacity>
            </>
        );
    }
    return HOCView as T;
}
const styles = StyleSheet.create({
    addButton: {
        position: 'absolute',
        bottom: 80,
        right: 28,
    },
    addImg: {
        width: 54,
        height: 54,
        resizeMode: 'contain',
    }
});
```

##### HOC 高阶组件案例演示2

Hack生命周期：首页上报设备信息

```typescript
// withFloatButton.tsx
import React from "react";
import { TouchableOpacity, Image, StyleSheet } from "react-native";

type IReactComponent = 
	React.ClassicComponentClass 
	| React.ComponentClass 
	| React.FunctionComponent 
	| React.ForwardExoticComponent<any>;

export default <T extends IReactComponent>(OriginView: T): T => {
    const HOCView = (props: any) => {
        useEffect(() => {
            reportDeviceInfo();
        }, []);

        const reportDeviceInfo = () => {
            // TODO 上报设备信息
        }
        return (
        	<>
            	<OriginView {...props}/>
            </>
        );
    }
    return HOCView as T;
}
```

##### 高阶组件使用思考

- 不要改变原始组件的原型
- 必要的话可以传多个参数

- 用【Hack生命周期】实现一个首页申请权限
- 用【Hack渲染函数】实现一个列表索引

HOC高阶组件在真实项目开发中是非常重要的解耦技巧，它解决的是横切点关注问题，把不同的职责和能力独立成高阶组件，增强了复用性，降低了耦合度。合理使用高阶组件可以使我们的代码更安全稳定，更好维护。



### memo与性能优化

##### memo 与性能优化 函数式组件和 class 组件拦截多余渲染的方法

memo解决什么问题？

1. 避免多余渲染
   1. 函数式组件：React.memo()
   2. class组件：shouldComponentUpdate()
2. 避免重复计算、重复创建对象

```typescript
// 函数式组件
// index.d.ts
type UserInfo = {
  avatar: string;
  name: string;
  desc: string;
}
// InfoView.tsx
type Props = {
  info: UserInfo
}
export default React.memo((props: Props) => {
  const { info } = props;
  console.log('render ...');
  return (
  	<View>
    	<Image style={drakStyles.img} source={{ uri: info.avatar }}/>
    	<Text style={drakStyles.txt}>{info.name}</Text>
			<View style={drakStyles.infoLayout}>
        <Text style={drakStyles.infoTxt}>{info.desc}</Text>
      </View>
    </View>
  );
}, (preProps: Props, nextProps: Props) => {
  // 判断是否需要重绘,true使用缓存
  // return preProps.info.avatar === nextProps.info.avatar &&
  //   preProps.info.name === nextProps.info.name &&
  //   preProps.info.desc === nextProps.info.desc;
  return JSON.stringify(preProps.info) === JSON.stringify(nextProps.info);
})
// MemoPage.tsx
export default () => {
  const {info, setInfo} = useState<UserInfo>({
    avatar: 'xxxx',
    name: 'xxx',
    desc: 'xxxxxxx'
  });
  return (
  	<View style={{width: '100%'}}>
    	<Button title='btn', onPress={() => {
        setInfo({
          avatar: 'xxxx222',
          name: 'xxx222',
          desc: 'xxxxxxx222'
        })
      }}/>
    	<InfoView info={info}/>
    </View>
  );
}
```

```typescript
// class组件
// index.d.ts
type UserInfo = {
  avatar: string;
  name: string;
  desc: string;
}
// InfoView.tsx
type Props = {
  info: UserInfo
}
export default class InfoView extends React.Component<Props, any> {
  constructor(props: Props){
    super(props);
  }
  shouldComponentUpdate(nextProps: Readonly<Props>, nextState: Readonly<any>,
                        nextContext: any): boolean {
    // true 重新渲染
    return JSON.stringify(this.props.info) !== JSON.stringify(nextProps.info);
  }
  render(): React.ReactNode {
    const { info } = this.props;
    return (
      <View>
        <Image style={drakStyles.img} source={{ uri: info.avatar }}/>
        <Text style={drakStyles.txt}>{info.name}</Text>
        <View style={drakStyles.infoLayout}>
          <Text style={drakStyles.infoTxt}>{info.desc}</Text>
        </View>
      </View>
    );
  }
}
```

##### 使用 useMemo 缓存计算结果

避免重复创建对象

- useMemo 缓存数据

- useMemo 缓存ui渲染

- useCallback 缓存回调函数对象

```typescript
// 问题：切换开关重新触发计算合计
// ConsumeList.tsx
import React, { useState, useMemo, useCallback } from 'react';
import {
  View,
  Button,
  StyleSheet,
  FlatList,
  Switch,
  Text,
  TouchableOpacity
} from 'react-native';
// 数据源
import { ListData, ListData2 } from '../constants/Data';
import { TypeColors } from '../constants/Data';

export default () => {
    const [data, setData] = useState<any>(ListData);
    const [showType, setShowType] = useState<boolean>(true);

  	// 使用useMemo解决重复计算问题
    // const calculateTotal = useMemo(() => {
    //     console.log('重新计算合计');
    //     return data.map((item: any) => item.amount)
    //         .reduce((pre: number, cur: number) => pre + cur);
    // }, [data])

    // 使用 useMemo 缓存 ui 
    const totalAmountView = useMemo(() => {
        const total = data.map((item: any) => item.amount)
            .reduce((pre: number, cur: number) => pre + cur);
        console.log('重新渲染合计');
        return (
            <View style={styles.totalLayout}>
                <Text style={styles.totalTxt}>{total}</Text>
                <Text style={styles.totalTxt}>合计：</Text>
            </View>
        );
    }, [data])

    // 缓存回调函数
    const onItemPress = useCallback((item: any, index: number) => () => {
        console.log(`点击第${item.index}行`);
    }, [])

    const renderItem = ({item, index}: any) => {
        const styles = StyleSheet.create({
            itemLayout: {
                width: '100%',
                padding: 16,
                flexDirection: 'column',
                borderBottomWidth: 1,
                borderBottomColor: '#E0E0E0',
            },
            labelRow: {
                width: '100%',
                flexDirection: 'row',
                alignItems: 'center',
            },
            valueRow: {
                marginTop: 10,
            },
            labelTxt: {
                flex: 1,
                fontSize: 14,
                color: '#666',
            },
            first: {
                flex: 0.4,
            },
            second: {
                flex: 0.3,
            },
            last: {
                flex: 0.6
            },
            valueTxt: {
                flex: 1,
                fontSize: 18,
                color: '#333',
                fontWeight: 'bold',
            },
            typeLayout: {
                flex: 0.3,
            },
            typeTxt: {
                width: 20,
                height: 20,
                textAlign: 'center',
                textAlignVertical: 'center',
                color: 'white',
                borderRadius: 4,
                fontWeight: 'bold',
            },
        });
        return (
            <TouchableOpacity
                style={styles.itemLayout}
                onPress={onItemPress(item, index)}
            >
                <View style={styles.labelRow}>
                    <Text style={[styles.labelTxt, styles.first]}>序号</Text>
                    {showType && <Text style={[styles.labelTxt, styles.second]}>类型</Text>}
                    <Text style={styles.labelTxt}>消费名称</Text>
                    <Text style={[styles.labelTxt, styles.last]}>消费金额</Text>
                </View>
                <View style={[styles.labelRow, styles.valueRow]}>
                    <Text style={[styles.valueTxt, styles.first]}>{item.index}</Text>
                    {showType && <View style={styles.typeLayout}>
                        <Text style={[
                            styles.typeTxt,
                            { backgroundColor: TypeColors[item.type] }
                        ]}>
                            {item.type}
                        </Text>
                    </View>}
                    <Text style={styles.valueTxt}>{item.name}</Text>
                    <Text style={[styles.valueTxt, styles.last]}>{item.amount}</Text>
                </View>
            </TouchableOpacity>
        );
    }

    return (
        <View style={styles.root}>
            <View style={styles.titleLayout}>
                <Text style={styles.titleTxt}>消费记账单</Text>
                <Switch
                    style={styles.switch}
                    value={showType}
                    onValueChange={value => setShowType(value)}
                />
                <Button
                    title='切换数据'
                    onPress={() => {
                        setData(ListData2)
                    }}
                />
            </View>
            <FlatList
                data={data}
                keyExtractor={(item, index) => `${item.index}-${item.name}`}
                renderItem={renderItem}
            />
            {/* <View style={styles.totalLayout}>
                <Text style={styles.totalTxt}>{calculateTotal}</Text>
                <Text style={styles.totalTxt}>合计：</Text>
            </View> */}
            {totalAmountView}
        </View>
    );
}

const styles = StyleSheet.create({
    root: {
        width: '100%',
        height: '100%',
        backgroundColor: 'white',
    },
    titleLayout: {
        width: '100%',
        height: 56,
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
    },
    titleTxt: {
        fontSize: 18,
        color: '#333',
        fontWeight: 'bold',
    },
    totalLayout: {
        width: '100%',
        height: 60,
        flexDirection: 'row-reverse',
        borderTopWidth: 1,
        borderTopColor: '#c0c0c0',
        alignItems: 'center',
        paddingHorizontal: 16,
    },
    totalTxt: {
        fontSize: 20,
        color: '#333',
        fontWeight: 'bold',
    },
    switch: {
        position: 'absolute',
        right: 16,
    },
})
```

##### useMemo 缓存 ui 以及 useCallback 缓存回调函数

useMemo 缓存 ui 在生产中更加常见

##### Hermes 引擎

- 提升启动速度
- 压缩包体积

```groovy
// build.gradle
project.ext.react = [
  // enableHermes: true 大约可以节省10% JS Bundle体积，默认已经开启
  enableHermes: true // clean and rebuild if changing
]
```



### ref转发

##### Ref转发案例演示1 外层操作原始组件

ref转发解决什么问题

- 使用自定义组件（组合模式）时，外层对原始组件的操作
- 函数式组件对外暴露实例（通常是api）

案例演示

- 使用自定义组件，外层操作原始组件

```typescript
import React, { useRef } from 'react';
import {
  StyleSheet,
  View,
  Button,
  TextInput
} from 'react-native';

import CustomInput from './CustomInput';

export default () => {
    const inputRef = useRef<TextInput>(null);
    return (
        <View style={styles.root}>
            <Button title='聚焦' onPress={() => {
              	// inputRef 可以操作原始输入框组件
                inputRef.current?.customFocus();
            }} />
            <CustomInput ref={inputRef} />
        </View>
    );
}

// CustomInput.tsx
import React, { useState, useRef, forwardRef, useImperativeHandle } from 'react';

export default forwardRef<TextInput, any>((props, ref) => {
  return(
  	...
    <TextInput
    	ref={inputRef}
    	style={styles.input}
    	value={value}
    	keyboardType='number-pad'
    	onChangeText={value => {
        LayoutAnimation.spring();
    		setValue(value);
    	}}
    	maxLength={11}/>
  )
}
```

##### Ref转发案例演示2 对外暴露api

案例演示：自定义组件对外暴露api

- class组件实现方式
- 函数式组件实现方式

```typescript
// 函数式组件实现方式
// CustomInput.tsx
import React, { useState, useRef, forwardRef, useImperativeHandle } from 'react';
export interface CustomInputRef {
    customFocus: () => void;
    customBlur: () => void;
}
export default forwardRef((props, ref) => {
    const inputRef = useRef<TextInput>(null);
    const customFocus = () => {
        inputRef.current?.focus();
    }
    const customBlur = () => {
        inputRef.current?.blur();
    }
    useImperativeHandle(ref, () => {
        return {
            customFocus,
            customBlur,
        };
    })
    return (
      	...
        <TextInput
      		ref={inputRef}
      		style={styles.input}
      		value={value}
      		keyboardType='number-pad'
      		onChangeText={value => {
      			LayoutAnimation.spring();
      			setValue(value);
      		}}
      		maxLength={11}
      		/>
    );
});
// RefDemo.tsx
import React, { useRef } from 'react';
import CustomInput, { CustomInputRef } from './CustomInput';

export default () => {
    const inputRef = useRef<CustomInput>(null);
    return (
        <View style={styles.root}>
            <Button title='聚焦' onPress={() => {
                inputRef.current?.customFocus();
            }} />
            <Button title='失焦' onPress={() => {
                inputRef.current?.customBlur();
            }} />
            <CustomInput ref={inputRef} />
        </View>
    );
}
```

```typescript
// class组件实现方式
// CustomInput.tsx
import React from 'react';
export default class CustomInput extends React.Component {
    inputRef = React.createRef<TextInput>();
    constructor(props: any) {
        super(props);
    }
    customFocus = () => {
        this.inputRef.current?.focus();
    }
    customBlur = () => {
        this.inputRef.current?.blur();
    }
    render() {
        return (
          	// ...
            <TextInput
                        ref={this.inputRef}
                        style={styles.input}
                        value={value}
                        keyboardType='number-pad'
                        onChangeText={value => {
                            LayoutAnimation.spring();
                            this.setState({
                                value,
                            })
                        }}
                        maxLength={11}
                    />
        );
    }
}

// RefDemo.tsx
import React, { useRef } from 'react';
import CustomInput2 from './CustomInput2';

export default () => {
    const inputRef = useRef<CustomInput2>(null);
    return (
        <View style={styles.root}>
            <Button title='聚焦' onPress={() => {
                inputRef.current?.customFocus();
            }} />
            <Button title='失焦' onPress={() => {
                inputRef.current?.customBlur();
            }} />
            <CustomInput2 ref={inputRef} />
        </View>
    );
}
```



### 桥接原生

##### 桥接原生介绍

为什么需要桥接原生?

- 实现react层JS实现不了的需求
  - 复杂、高性能组件：复杂表格、视频播放等
  - 原生层开发能力：传感器编程、widget等
  - 平台属性：系统信息、设备信息等
  - 对接三方应用：相机、相册、地图等

##### 桥接原生实现JS调用原生方法

- 编写并注册原生层方法
  - 实现 ReactPackage
  - 实现 ReactContextBaseJavaModule
    - @ReactMethod
- JS层调用原生方法

```js
// DemoPackage
public class DemoPackage implements ReactPackage {
    @NonNull
    @Override
    public List<NativeModule> createNativeModules(@NonNull ReactApplicationContext context) {
      	// 注册Module
        List<NativeModule> modules = new ArrayList<>();
        modules.add(new AppModule(context));
        return modules;
    }

    @NonNull
    @Override
    public List<ViewManager> createViewManagers(@NonNull ReactApplicationContext context) {
        List<ViewManager> viewManagers = new ArrayList<>();
        viewManagers.add(new InfoViewManager());
        viewManagers.add(new InfoViewGroupManager());
        return viewManagers;
    }
}
// AppModule
public class AppModule extends ReactContextBaseJavaModule {

    public AppModule(@Nullable ReactApplicationContext reactContext) {
        super(reactContext);
    }

  	// 返回原生模块注册的名称供JS使用
    @NonNull
    @Override
    public String getName() {
        return "App";
    }

  	// 桥接原生常量
    @Override
    public Map<String, Object> getConstants() {
        Map map = new HashMap<String, Object>();
        map.put("versionName", BuildConfig.VERSION_NAME);
        map.put("versionCode", BuildConfig.VERSION_CODE);
        return map;
    }

    @ReactMethod
    public void openGallery() {
        if (getCurrentActivity() == null) {
            return;
        }
        DeviceUtil.openGallery(getCurrentActivity());
    }

    @ReactMethod
    public void getVersionName(Promise promise) {
        String versionName = BuildConfig.VERSION_NAME;
        if (versionName == null) {
            promise.reject(new Throwable("获取版本失败"));
        } else {
            promise.resolve(versionName);
        }
    }
}
// MainApplication
private final ReactNativeHost mReactNativeHost =
    new ReactNativeHost(this) {
      @Override
      public boolean getUseDeveloperSupport() {
        return BuildConfig.DEBUG;
      }

      @Override
      protected List<ReactPackage> getPackages() {
        @SuppressWarnings("UnnecessaryLocalVariable")
        List<ReactPackage> packages = new PackageList(this).getPackages();
        // Packages that cannot be autolinked yet can be added manually here, for example:
        // packages.add(new MyReactNativePackage());
          packages.add(new DemoPackage());
        return packages;
      }

      @Override
      protected String getJSMainModuleName() {
        return "index";
      }
    };
// NativePage.tsx
import {
  NativeModules,
} from 'react-native';
const { App } = NativeModules;
App?.openGallery();
App?.getVersionName().then((data: string) => {
    console.log(`versionName=${data}`);
})
const { versionName, versionCode } = App;
console.log(`versionName=${versionName}, versionCode=${versionCode}`);
```

##### 桥接原生实现JS层获取原生常量

- 编写并注册原生常量方法
- JS层获取原生常量（同步获取）

代码合并在上面

##### 桥接原生原子组件 实现原生组件

1. 实现一个原生自定义组件View

```java
public class InfoView extends LinearLayout implements View.OnClickListener {

    // ...
    @Override
    public void onClick(View v) {
        // ...
        WritableMap params = Arguments.createMap();
        params.putString("shape", this.shape);
        ReactContext context = (ReactContext)getContext();
        context.getJSModule(RCTEventEmitter.class)
                .receiveEvent(getId(), "onShapeChange", params);
    }
}

public class InfoViewManager extends SimpleViewManager<InfoView> {
  	// JS层使用
    @NonNull
    @Override
    public String getName() {
        return "NativeInfoView";
    }

    @NonNull
    @Override
    protected InfoView createViewInstance(@NonNull ThemedReactContext context) {
        return new InfoView(context);
    }

  	// JS使用avatar属性
    @ReactProp(name = "avatar")
    public void setAvatar(InfoView view, String url) {
        view.setAvatar(url);
    }

    @ReactProp(name = "name")
    public void setName(InfoView view, String name) {
        view.setName(name);
    }

    @ReactProp(name = "desc")
    public void setDesc(InfoView view, String desc) {
        view.setDesc(desc);
    }

    @Nullable
    @Override
    public Map getExportedCustomBubblingEventTypeConstants() {
        return MapBuilder.builder()
                .put("onShapeChange",
                        MapBuilder.of(
                                "phasedRegistrationNames",
                                MapBuilder.of("bubbled", "onShapeChange")
                        )
                ).build();
    }

    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of("setShape", SET_SHAPE_CODE);
    }

    @Override
    public void receiveCommand(
            @NonNull InfoView view,
            String commandId,
            @Nullable ReadableArray args) {
        int command = Integer.parseInt(commandId);
        if (command == SET_SHAPE_CODE) {
            if (args != null && args.size() > 0) {
                String shape = args.getString(0);
                view.setShape(shape);
            }
        } else {
            // TODO
            super.receiveCommand(view, commandId, args);
        }
    }

    public static final int SET_SHAPE_CODE = 100;
}
```

##### 桥接原生原子组件 JS层调用原生组件

2. 创建ViewManager，实现 SimpleViewManager，用于接管原生自定义组件的属性和行为，并把ViewManager注册到ReactPackage中

3. 在JS层导入原生组件，并封装导出JS模块

```js
// NativeInfoView.tsx
import React, { useRef, useEffect } from 'react';
import {
  StyleSheet,
  View,
  requireNativeComponent,
  ViewProps,
  findNodeHandle,
  UIManager
} from 'react-native';

import { avatarUri } from '../constants/Uri';

type NativeInfoViewType = ViewProps | {
    // 这部分是自定义的属性
    avatar: string;
    name: string;
    desc: string;
    onShapeChange: (e: any) => void;
};

const NativeInfoView = requireNativeComponent<NativeInfoViewType>('NativeInfoView');

export default () => {

    const ref  = useRef(null);

    useEffect(() => {
        setTimeout(() => {
            sendCommand('setShape', ['round']);
        }, 3000);
    }, []);

    const sendCommand = (command: string, params: any[]) => {
        const viewId = findNodeHandle(ref.current);
        // @ts-ignore
        const commands = UIManager.NativeInfoView.Commands[command].toString();
        UIManager.dispatchViewManagerCommand(viewId, commands, params);
    }

    return (
        <NativeInfoView
            ref={ref}
            style={styles.infoView}
            avatar={avatarUri}
            name="尼古拉斯·段坤"
            desc="各位产品经理大家好，我是个人开发者张三，我学习RN两年半了，我喜欢安卓、RN、Flutter，Thank you!。"
            onShapeChange={(e: any) => {
                console.log(e.nativeEvent.shape);
            }}
        />
    );
}

const styles = StyleSheet.create({
    infoView: {
        width: '100%',
        height: '100%',
    },
})
```

##### 桥接原生原子组件 封装原生组件属性

4. ViewManager 使用 @ReactProp 定义组件属性

代码见上面InfoViewManager

##### 桥接原生原子组件 原生事件回调

5. 原生组件回调JS层方法

```java
public class InfoView extends LinearLayout implements View.OnClickListener {

    // ...
    @Override
    public void onClick(View v) {
        // ...
        WritableMap params = Arguments.createMap();
        params.putString("shape", this.shape);
        ReactContext context = (ReactContext)getContext();
        context.getJSModule(RCTEventEmitter.class)
                .receiveEvent(getId(), "onShapeChange", params);
    }
}

public class InfoViewManager extends SimpleViewManager<InfoView> {

    @Nullable
    @Override
    public Map getExportedCustomBubblingEventTypeConstants() {
      	// 将onShapeChange事件通过
        return MapBuilder.builder()
                .put("onShapeChange",
                        MapBuilder.of(
                                "phasedRegistrationNames",
                                MapBuilder.of("bubbled", "onShapeChange")
                        )
                ).build();
    }
}

// NativeInfoView.tsx
export default () => {
    return (
        <NativeInfoView
            ref={ref}
            style={styles.infoView}
            avatar={avatarUri}
            name="尼古拉斯·段坤"
            desc="各位产品经理大家好，我是个人开发者张三，我学习RN两年半了，我喜欢安卓、RN、Flutter，Thank you!。"
            onShapeChange={(e: any) => {
                console.log(e.nativeEvent.shape);
            }}
        />
    );
}
```

##### 桥接原生原子组件 原生组件公开api给JS调用

6. 公开原生组件方法给JS层调用

```typescript
import React, { useRef, useEffect } from 'react';
import {
  StyleSheet,
  View,
  requireNativeComponent,
  ViewProps,
  findNodeHandle,
  UIManager
} from 'react-native';
const NativeInfoView = requireNativeComponent<NativeInfoViewType>('NativeInfoView');

export default () => {
    useEffect(() => {
    		setTimeout(() => {
        		sendCommand('setShape', ['round']);
    		}	, 3000);
		}, []);
    const sendCommand = (command: string, params: any[]) => {
        const viewId = findNodeHandle(ref.current);
        // @ts-ignore
        const commands = UIManager.NativeInfoView.Commands[command].toString();
        UIManager.dispatchViewManagerCommand(viewId, commands, params);
    }

    return (
        <NativeInfoView
            ref={ref}
            style={styles.infoView}
            avatar={avatarUri}
            name="尼古拉斯·段坤"
            desc="各位产品经理大家好，我是个人开发者张三，我学习RN两年半了，我喜欢安卓、RN、Flutter，Thank you!。"
            onShapeChange={(e: any) => {
                console.log(e.nativeEvent.shape);
            }}
        />
    );
}
// InfoViewManager
public class InfoViewManager extends SimpleViewManager<InfoView> {
    // 注册支持哪些事件
  	@Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of("setShape", SET_SHAPE_CODE);
    }
  
  	// 处理事件
    @Override
    public void receiveCommand(
            @NonNull InfoView view,
            String commandId,
            @Nullable ReadableArray args) {
        int command = Integer.parseInt(commandId);
        if (command == SET_SHAPE_CODE) {
            if (args != null && args.size() > 0) {
                String shape = args.getString(0);
                view.setShape(shape);
            }
        } else {
            // TODO
            super.receiveCommand(view, commandId, args);
        }
    }
    public static final int SET_SHAPE_CODE = 100;
}
```

##### 桥接原生容器组件

- 实现一个原生容器组件
- 创建 ViewGroupManager，注册行为和 ViewManager 一致
- 在 JS 层导入原生组件，并封装导出 JS 模块
- 属性、方法回调、api调用和ViewManager一致

```typescript
// InfoViewGroup.java
public class InfoViewGroup extends LinearLayout {
    public InfoViewGroup(Context context) {
        super(context);
    }
}
// InfoViewGroupManager.java
public class InfoViewGroupManager extends ViewGroupManager<InfoViewGroup> {
    @NonNull
    @Override
    public String getName() {
        return "NativeInfoViewGroup";
    }

    @NonNull
    @Override
    protected InfoViewGroup createViewInstance(@NonNull ThemedReactContext context) {
        return new InfoViewGroup(context);
    }
}
// DemoPackage.java
public class DemoPackage implements ReactPackage {
    @NonNull
    @Override
    public List<NativeModule> createNativeModules(@NonNull ReactApplicationContext context) {
        List<NativeModule> modules = new ArrayList<>();
        modules.add(new AppModule(context));
        return modules;
    }

    @NonNull
    @Override
    public List<ViewManager> createViewManagers(@NonNull ReactApplicationContext context) {
        List<ViewManager> viewManagers = new ArrayList<>();
        viewManagers.add(new InfoViewManager());
        viewManagers.add(new InfoViewGroupManager());
        return viewManagers;
    }
}
// MainApplication.java
private final ReactNativeHost mReactNativeHost =
    new ReactNativeHost(this) {
      @Override
      public boolean getUseDeveloperSupport() {
        return BuildConfig.DEBUG;
      }

      @Override
      protected List<ReactPackage> getPackages() {
        @SuppressWarnings("UnnecessaryLocalVariable")
        List<ReactPackage> packages = new PackageList(this).getPackages();
        // Packages that cannot be autolinked yet can be added manually here, for example:
        // packages.add(new MyReactNativePackage());
          packages.add(new DemoPackage());
        return packages;
      }

      @Override
      protected String getJSMainModuleName() {
        return "index";
      }
    };

// NativeInfoViewGroup.tsx
import React from 'react';
import {
  StyleSheet,
  requireNativeComponent,
  ViewProps,
} from 'react-native';

type NativeInfoViewGroupType = ViewProps | {
    // 这部分是自定义的属性
};

const NativeInfoViewGroup = requireNativeComponent<NativeInfoViewGroupType>('NativeInfoViewGroup');

export default (props: any) => {

    const { children } = props;

    return (
        <NativeInfoViewGroup
            style={styles.infoView}
        >
            {children}
        </NativeInfoViewGroup>
    );
}

const styles = StyleSheet.create({
    infoView: {
        width: '100%',
        flexDirection: 'row',
    },
})

// NativePage.tsx
<NativeInfoViewGroup>
    <View style={styles.content}>
        <Image
            style={styles.avatarImg}
            source={{ uri: avatarUri }}
        />
        <View style={styles.nameLayout}>
            <Text style={styles.nameTxt}>尼古拉斯·段坤</Text>
            <Text style={styles.descTxt}>
                各位产品经理大家好，我是个人开发者张三，我学习RN两年半了，我喜欢安卓、RN、Flutter，Thank you!。
            </Text>
        </View>
    </View>
</NativeInfoViewGroup>
```



### 项目实战仿写小红书App

##### 实战仿写小红书 App 实战项目指导大纲

##### 实战仿写小红书App 功能演示和需求梳理

##### 实战仿写小红书App 项目初始化和资源准备

初始化工程与安装

完成基础项目配置：名称、图标、应用id

核对设计稿，准备资源图片，导入图片

##### 实战仿写小红书App 导入并配置TypeScript和AsyncStorage

项目配置TypeScript

```shell
# 安装TypeScript
npm install --save-dev typescript
# 生成tsconfig.json
tsc --init
# 安装类型申明
npm i --save-dev @types/react @types/react-native
```

AsyncStorage：数据存储

```shell
# 集成async-storage
npm i @react-native-async-storage/async-storage
# 保存数据：AsyncStorage.setItem()
# 读取数据：AsyncStorage.getItem()
```



### 项目实战仿写小红书App【路由管理和欢迎登陆页面】

##### 实战仿写小红书 App 路由管理安装和介绍

集成react-navigation

```shell
npm i @react-navigation/bottom-tabs
npm i @react-navigation/native
npm i @react-navigation/stack
npm i react-native-gesture-handler
npm i react-native-safe-area-context
npm i react-native-screens
```

##### 实战仿写小红书 App 构建导航栈并测试核心 Api

构建导航栈

- 在App.tsx根结点构建导航栈
- 配置导航栈属性

##### 实战仿写小红书 App 实现欢迎页面和快捷登陆页面

欢迎页面和登陆页面

- 开发欢迎页面，并设置3秒倒计时
- 开发登陆页面，并设置3秒倒计时
- 页面连续跳转

##### 实战仿写小红书 App 实现账号密码登陆页面

##### 实战仿写小红书 App 欢迎登陆首页三连跳

##### 实战仿写小红书 App 登陆页面交互细节优化



### 项目实战仿写小红书App【服务端接口与网络请求】

##### nodejs服务介绍及使用方法

为什么使用本地nodejs服务模拟数据？

初识egg.js：初始化、controller、静态资源

本地nodejs服务使用方法

```shell
npm install
npm run dev
```

##### 封装Axios请求

```shell
# 安装Axios库
npm i axios
# 封装request请求方法
request.ts
```

##### 封装接口配置

增加apis接口配置文件

使用简化接口配置名发起请求

##### 拦截接口响应

拦截接口响应（错误码）

##### 使用Mobx和缓存实现完整登陆流程

- 集成MobX库，介绍基础用法

```shell
npm i mobx mobx-react
```

- 编写UserStore，使用状态管理类隔离ui和数据

  ESM单例导出  

- 本地缓存登录信息，下次自动登录



### 项目实战仿写小红书App【构建应用自定义主Tab页】

##### 构建基础底部Tab

- 使用bottom-tab组件构建首页Tab页

- 实现自定义Tab样式

- 集成相册选择模块，跳转系统图库

  ```shell
  npm i react-native-image-picker
  ```

##### 自定义底部TabBar样式

##### 集成相册选择模块



### 高仿商业级小红书 App【构建应用首页瀑布流列表】

##### 搭建首页框架

- 创建首页组件和HomeStore

- 重新安装mobx版本

  ```shell
  npm uninstall mobx
  npm uninstall mobx-react
  npm i mobx@5.15.4 mobx-react@6.1.8
  
  npm i @babel/plugin-proposal-decorators
  # babel 添加插件
  plugins: [["@babel/plugin-proposal-decorators", {"legacy":true}]]
  # tsconfig.json添加属性
  "experimentalDecorators": true
  ```

- 实现ui到数据的基本流程

##### 实现列表显示和分页加载

- 使用FlatList加载文章列表
  - 固定尺寸Item
  - 完整数据显示
  - 数据分页加载
  - 添加列表Footer

##### 实现瀑布流布局

- 使用FlowList代替FlatList实现瀑布流布局
- 自定义ResizeImage组件，实现可变图片大小
- 自定义Heart组件实现点赞特效

##### 自定义Heart实现点赞特效

##### 封装TitleBar组件

##### 封装CategoryList频道组件

- 本地定义频道数据，并在store提供存取功能
- 使用横向ScrollView实现频道列表
- 使用自定义Modal实现频道编辑弹窗

##### 自定义频道编辑弹窗

##### 实现频道编辑数据修改

##### Toast和Loading小工具



### 高仿商业级小红书 App【实现文章详情及评论区展示】

##### 文章详情页面框架搭建

搭建页面框架，请求文章详情数据

使用SlidePage加载详情轮播图

实现嵌套评论列表

##### 实现详情图片浏览

##### 基本信息编写

##### 嵌套评论列表



### 高仿商业级小红书 App【实现购物列表及搜索无缝切换】

##### 实现商品列表

实现顶部搜索栏布局

实现Top10品类布局

使用FlatList实现商品列表页面

使用透明页面和Animation实现无缝搜索切换

##### 顶部Top10品类

##### 实现无缝搜索切换



### 项目实战仿写小红书App【实现消息页面及气泡展示】

##### 实现消息列表头部

##### 实现消息列表展示

##### 实现悬浮菜单效果



### 项目实战仿写小红书App【实现我的页面及侧拉菜单】

##### 我的页面头部信息

用ScrollView构建页面整体框架

实现页面头部布局，及对应数据请求加载

实现3个Tab对应列表数据加载

使用onLayout回调实现精确背景高度

使用自定义下拉刷新组件刷新全部数据

使用LayoutAnimation实现侧拉栏菜单效果

##### 动态高度和Tab切换

##### 列表渲染和空元素

##### 实现侧拉菜单与动效

##### 登陆流程闭环



### 项目实战仿写小红书App【项目发布与热修复实践】

##### RN热修复两大框架介绍

什么是热修复技术？

- 不需要下载应用包，不需要重新发起安装
- 小体积差分包下载、免安装重启生效

为什么会存在热修复技术？

- 移动端的特有属性：离线安装包
- 互联网竞争的短平快新：最小成本、最快速度、降低干扰

CodePush & **Pushy**

- CodePush：微软，官网：http://appcenter.ms/apps

- Pushy：RN中文网推荐，官网：https://pushy.reactnative.cn/

  | CodePush             | Pushy                    |
  | -------------------- | ------------------------ |
  | 功能强，支持生产测试 | 功能一般，不支持生产测试 |
  | 大厂信用背书         | 中文网推荐               |
  | 支持数据统计         | 服务器稳定               |
  | 服务器不稳定         | 补丁包体积较小           |
  | 初次补丁包较大       | 不支持数据统计           |

##### 安装Pushy模块与创建应用

1. cli和模块安装

   ```shell
   npm i -g react-native-update-cli
   npm i react-native-update
   ```

2. 禁用android的crunch优化

   ```groovy
   buildTypes{
   	release{
   		crunchPngs false
   	}
   }
   ```

3. 配置Bundle URL

   ```java
   @Override
   protected String getJSBundleFile(){
     return UpdateContext.getBundleUrl(MainActivity.this);
   }
   ```

4. 后续具体查看官网文档。。。

##### 代码集成热修复加载流程

代码自定义集成

1. 获取appKey
2. 检查更新
3. 下载更新
4. 切换版本
5. 生效与回滚
   - 确认生效：补丁安装成功，保存patch版本
   - 回滚补丁：补丁安装失败，上报信息

##### 发布补丁并成功加载

```shell
pushy uploadApk app-release.apk
pushy bundle --platform android
# meta info: {"forceUpdate":true}
```

##### 6处流程细节优化

1. 检查补丁更新封装成高阶组件
2. 不做大版本更新
3. 可以发布小功能
4. 区分布丁是否强制升级还是弹窗提示用户
5. 提醒用户是否重启
6. 设置页面显示补丁版本号name



### 课程总结与未来展望

- 前端基础、React基础、系统组件、系统Api
- TypeScript、Context、HOC、memo、ref
- 动画系统、原生桥接、路由管理、状态管理 ...
- React开发思维、函数式编程思想
- 组件接耦技巧、模块封装方法
- 项目开发流程、实战开发经验

TODO

- React开发web项目
- Taro开发小程序项目
