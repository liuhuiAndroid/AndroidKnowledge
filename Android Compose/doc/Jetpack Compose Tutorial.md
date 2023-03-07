Jetpack Compose Tutorial
Jetpack Compose 教程

Jetpack Compose is a modern toolkit for building native Android UI.
Jetpack Compose 是用于构建原生 Android 界面的新工具包。

Jetpack Compose simplifies and accelerates UI development on Android with less code, powerful tools, and intuitive Kotlin APIs.
它使用更少的代码、强大的工具和直观的 Kotlin API，可以帮助您简化并加快 Android 界面开发。

In this tutorial, you'll build a simple UI component with declarative functions.
在本教程中，您将使用声明性的函数构建一个简单的界面组件。

You won't be editing any XML layouts or using the Layout Editor.
您无需修改任何 XML 布局，也不需要使用布局编辑器。

Instead, you will call Jetpack Compose functions to say what elements you want, and the Compose compiler will do the rest.
只需要调用 Jetpack Compose 函数来声明您想要的元素，Compose 编译器即会完成后面的所有工作。

Note: Jetpack Compose 1.0 is now stable! See the Jetpack release notes for the latest updates.
注意：Jetpack Compose 1.0 现在为稳定版本。如需了解最新更新，请参阅 Jetpack 版本说明。

Lesson 1: Composable functions
第 1 课：可组合函数

Jetpack Compose is built around composable functions.
Jetpack Compose 是围绕可组合函数构建的。

These functions let you define your app's UI programmatically by describing how it should look and providing data dependencies,
rather than focusing on the process of the UI's construction (initializing an element, attaching it to a parent, etc.).
这些函数可让您以程序化方式定义应用的界面，只需描述应用界面的外观并提供数据依赖项，而不必关注界面的构建过程（初始化元素，将其附加到父项等）。

To create a composable function, just add the @Composable annotation to the function name.
如需创建可组合函数，只需将 @Composable 注解添加到函数名称中即可。

Add a text element
添加文本元素

To begin, download the most recent version of Android Studio Arctic Fox, and create an app using the Empty Compose Activity template.
开始前，请下载最新版本的 Android Studio Arctic Fox，然后使用 Empty Compose Activity 模板创建应用。

The default template already contains some Compose elements, but let's build it up step by step.
默认模板已包含一些 Compose 元素，但我们下面要逐步进行构建。

First, we’ll display a “Hello world!” text by adding a text element inside the onCreate method.
首先，通过在 onCreate 方法内添加文本元素，让系统显示“Hello world!”文本。

You do this by defining a content block, and calling the Text() function.
可以通过定义内容块并调用 Text() 函数来实现此目的。

The setContent block defines the activity's layout where we call composable functions.
setContent 块定义了 activity 的布局，我们会在其中调用可组合函数。

Composable functions can only be called from other composable functions.
可组合函数只能从其他可组合函数调用。

Jetpack Compose uses a Kotlin compiler plugin to transform these composable functions into the app's UI elements.
Jetpack Compose 使用 Kotlin 编译器插件将这些可组合函数转换为应用的界面元素。

For example, the Text() function that is defined by the Compose UI library displays a text label on the screen.
例如，由 Compose 界面库定义的 Text() 函数会在屏幕上显示一个文本标签。

Define a composable function
定义可组合函数

To make a function composable, add the @Composable annotation.
如需使函数成为可组合函数，请添加 @Composable 注解。

To try this out, define a MessageCard() function which is passed a name, and uses it to configure the text element.
如需尝试此操作，请定义一个 MessageCard() 函数并向其传递一个名称，然后该函数就会使用该名称配置文本元素。

Preview your function in Android Studio
在 Android Studio 中预览函数

Android Studio lets you preview your composable functions within the IDE, instead of installing the app to an Android device or emulator.
Android Studio 允许您在 IDE 中预览可组合函数，无需将应用安装到 Android 设备或模拟器中。

The composable function must provide default values for any parameters.
可组合函数必须为任何参数提供默认值。

For this reason, you can't preview the MessageCard() function directly.
因此，您无法直接预览 MessageCard() 函数，

Instead, let’s make a second function named PreviewMessageCard(), which calls MessageCard() with an appropriate parameter.
而是需要创建另一个名为 PreviewMessageCard() 的函数，由该函数使用适当的参数调用 MessageCard()。

Add the @Preview annotation before @Composable.
请在 @Composable 上方添加 @Preview 注解。

Rebuild your project.
重新构建您的项目。

The app itself doesn't change, since the new PreviewMessageCard() function isn't called anywhere, but Android Studio adds a preview window.
由于新的 PreviewMessageCard() 函数未在任何位置受到调用，因此应用本身不会更改，但 Android Studio 会添加一个预览窗口。

This window shows a preview of the UI elements created by composable functions marked with the @Preview annotation.
此窗口会显示由标有 @Preview 注解的可组合函数创建的界面元素的预览。

To update the previews at any time, click the refresh button at the top of the preview window.
任何时候，如需更新预览，请点击预览窗口顶部的刷新按钮。
