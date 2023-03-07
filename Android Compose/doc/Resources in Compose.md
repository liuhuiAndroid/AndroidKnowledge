- https://developer.android.google.cn/jetpack/compose/resources

Resources in Compose
Compose 中的资源

Jetpack Compose can access the resources defined in your Android project.
Jetpack Compose 可以访问您的 Android 项目中定义的资源。

This document explains some of the APIs Compose offers to do so.
本文将介绍 Compose 为此提供的一些 API。

Resources are the additional files and static content that your code uses, such as bitmaps, layout definitions, user interface strings, animation instructions, and more.
资源是指代码使用的附加文件和静态内容，例如位图、布局定义、界面字符串、动画说明等。

If you're not familiar with resources in Android, check out the App resources overview guide.
如果您不熟悉 Android 中的资源，请参阅应用资源概览指南。

Strings
字符串

The most common type of resource are your Strings.
最常见的资源类型就是字符串。

Use the stringResource API to retrieve a string statically defined in your XML resources.
使用 stringResource API 检索在 XML 资源中静态定义的字符串。

// In the res/values/strings.xml file
// <string name="compose">Jetpack Compose</string>

// In your Compose code
Text(
    text = stringResource(R.string.compose)
)

stringResource also works with positional formatting.
stringResource 也支持位置格式设置。

// In the res/values/strings.xml file
// <string name="congratulate">Happy %1$s %2$d</string>

// In your Compose code
Text(
    text = stringResource(R.string.congratulate, "New Year", 2021)
)

String plurals
字符串复数

Compose doesn't offer a direct method to retrieve String plurals yet.
Compose 目前还未提供直接检索 String 复数的方法。

However, you can use the traditional approach with the getQuantityString method of the Resources class.
不过，您可以结合使用传统方法与 Resources 类的 getQuantityString 方法。

To access Resources from the current Context, use the LocalContext composition local.
如需从当前 Context 访问 Resources，请使用 LocalContext CompositionLocal。

Read more about Composition locals in the Interoperability documentation.
如需详细了解 CompositionLocal，请参阅互操作性文档。

// In the res/strings.xml file
// <plurals name="runtime_format">
//    <item quantity="one">%1$d minute</item>
//    <item quantity="other">%1$d minutes</item>
// </plurals>

// In your Compose code
val resources = LocalContext.current.resources

Text(
    text = resources.getQuantityString(
        R.plurals.runtime_format, quantity, quantity
    )
)

Dimensions
尺寸
Similarly, use the dimensionResource API to get dimensions from a resource XML file.
同样，使用 dimensionResource API 从资源 XML 文件获取尺寸。

// In the res/values/dimens.xml file
// <dimen name="padding_small">8dp</dimen>

// In your Compose code
val smallPadding = dimensionResource(R.dimen.padding_small)
Text(
    text = "...",
    modifier = Modifier.padding(smallPadding)
)

Colors
颜色
If you're adopting Compose incrementally in your app, use the colorResource API to get colors from a resource XML file.
如果您在应用中增量采用 Compose，请使用 colorResource API 从资源 XML 文件中获取颜色。

// In the res/colors.xml file
// <color name="colorGrey">#757575</color>

// In your Compose code
Divider(color = colorResource(R.color.colorGrey))

colorResource works as expected with static colors, but it flattens color state list resources.
colorResource 可以按预期处理静态颜色，但它会拼合颜色状态列表资源。

Warning: Prefer colors from the theme rather than hard-coded colors.
警告：最好使用来自主题的颜色，而不是硬编码的颜色。

Even though it's possible to access colors using the colorResource function,
it's recommended that the colors of your app are defined in a MaterialTheme which can be accessed from your composables like MaterialTheme.colors.primary.
虽然可以使用 colorResource 函数访问颜色，但建议在 MaterialTheme 中定义应用的颜色，后者可通过 MaterialTheme.colors.primary 之类的可组合项访问。

Learn more about this in the Theming in Compose documentation.
有关详情，请参阅 Compose 中的主题文档。

Vector assets and image resources
矢量资源和图像资源

Use the painterResource API to load either vector drawables or rasterized asset formats like PNGs.
使用 painterResource API 加载矢量可绘制对象或光栅化资源格式（例如 PNG）。

You don't need to know the type of the drawable, simply use painterResource in Image composables or paint modifiers.
您无需了解可绘制对象的类型，只需在 Image 可组合项或 paint 修饰符中使用 painterResource。

// Files in res/drawable folders. For example:
// - res/drawable-nodpi/ic_logo.xml
// - res/drawable-xxhdpi/ic_logo.png

// In your Compose code
Icon(
    painter = painterResource(id = R.drawable.ic_logo),
    contentDescription = null // decorative element
)

painterResource decodes and parses the content of the resource on the main thread.
painterResource 可在主线程中解码并解析资源的内容。

Note: Unlike Context.getDrawable, painterResource can only load BitmapDrawable and VectorDrawable platform Drawable types.
注意：与 Context.getDrawable 不同，painterResource 只能加载 BitmapDrawable 和 VectorDrawable平台 Drawable 类型。

Animated Vector Drawables
带动画的矢量可绘制对象

Use the animatedVectorResource API to load an animated vector drawable XML.
使用 animatedVectorResource API 加载带动画的矢量可绘制对象 XML。

The method returns an AnimatedImageVector instance.
该方法会返回一个 AnimatedImageVector 实例。

In order to display the animated image, use the painterFor method to create a Painter that can be used in Image and Icon composables.
为显示带动画的图像，请使用 painterFor 方法创建可在 Image 和 Icon 可组合项中使用的 Painter。

The boolean atEnd parameter of the painterFor method indicates whether the image should be drawn at the end of all the animations.
painterFor 方法的布尔值 atEnd 参数指示是否应在所有动画结束时绘制图像。

If used with a mutable state, changes to this value trigger the corresponding animation.
如果与可变状态结合使用，更改此值将触发相应的动画。

// Files in res/drawable folders. For example:
// - res/drawable/animated_vector.xml

// In your Compose code
val image = animatedVectorResource(id = R.drawable.animated_vector)
val atEnd by remember { mutableStateOf(false) }
Icon(
    painter = image.painterFor(atEnd = atEnd),
    contentDescription = null // decorative element
)

Icons
图标

Jetpack Compose comes with the Icons object that is the entry point for using Material Icons in Compose.
Jetpack Compose 附带 Icons 对象，该对象是在 Compose 中使用 Material 图标的入口点。

There are five distinct icon themes: Filled, Outlined, Rounded, TwoTone, and Sharp.
共有以下五种不同的图标主题：Filled、Outlined、Rounded、TwoTone 和 Sharp。

Each theme contains the same icons, but with a distinct visual style.
每个主题包含相同的图标，但视觉风格不同。

You should typically choose one theme and use it across your application for consistency.
通常，您应该选择一种主题，并在整个应用中保持使用这一主题，从而确保一致性。

To draw an icon, you can use the Icon composable which applies tint and provides layout size matching the icon.
如需绘制图标，您可以使用 Icon 可组合项，该可组合项将应用色调并提供与图标匹配的布局尺寸。

import androidx.compose.material.Icon

Icon(Icons.Rounded.Menu, contentDescription = "Localized description")

Some of the most commonly used icons are available as part of the androidx.compose.material dependency.
一些最常用的图标可用作 androidx.compose.material 依赖项的一部分。

To use any of the other Material icons, add the material-icons-extended dependency to the build.gradle file.
如需使用其他任何 Material 图标，请将 material-icons-extended 依赖项添加到 build.gradle 文件。

dependencies {
  ...
  implementation "androidx.compose.material:material-icons-extended:$compose_version"
}

Note: Icons maintain the same names defined by Material, but with their snake_case name converted to PascalCase.
注意：图标保留由 Material 定义的相同名称，但将其 snake_case 名称转换为 PascalCase。

For example, add_alarm becomes AddAlarm. Icons that start with a number, such as 360, are prefixed with _, becoming _360.
例如，add_alarm 会变为 AddAlarm。以数字（例如 360）开头的图标将附带 _ 前缀，变为 _360。

Warning: material-icons-extended is a large library and can affect your APK size.
警告：material-icons-extended 是一个大型库，可能会影响您的 APK 大小。

For that reason, consider using R8/Proguard in production builds to remove the resources that aren't used.
因此，请考虑在正式 build 中使用 R8/Proguard 来移除未使用的资源。

Furthermore due to the large size, your project's build time and Android Studio's previews loading time could increase during development.
此外，由于大小较大，您的项目的构建时间和 Android Studio 的预览加载时间在开发过程中也可能会增加。

On the other hand, material-icons-extended lets you iterate more rapidly through quick access to the latest versions of all icons.
另一方面，material-icons-extended 可让您快速访问所有图标的最新版本，实现更快迭代。

You can also use this library to copy the source code of the icon to have a fixed copy in your app,
or alternatively, import the icons using Android Studio's Import vector asset feature.
您还可以使用此库来复制图标的源代码，以便在应用中保留一份固定副本，或者使用 Android Studio 的导入矢量资源功能导入图标。

Fonts
字体

To use fonts in Compose, download and bundle the font files directly in your APKs by placing them in the res/font folder.
如需在 Compose 中使用字体，请下载字体文件，并将其放在 res/font 文件夹中以直接捆绑到 APK 中。

Note: Downloadable fonts are not yet supported in Compose.
注意：Compose 尚不支持可下载字体。

Load each font using the Font API and create a FontFamily with them that you can use in TextStyle instances to create your own Typography.
使用 Font API 加载每种字体，并使用这些字体创建一个 FontFamily，您可以在 TextStyle 实例中使用该 FontFamily 来创建您自己的 Typography。

The following is code taken from the Crane compose sample and its Typography.kt file.
以下代码摘自 Crane Compose 示例及其 Typography.kt 文件。

// Define and load the fonts of the app
private val light = Font(R.font.raleway_light, FontWeight.W300)
private val regular = Font(R.font.raleway_regular, FontWeight.W400)
private val medium = Font(R.font.raleway_medium, FontWeight.W500)
private val semibold = Font(R.font.raleway_semibold, FontWeight.W600)

// Create a font family to use in TextStyles
private val craneFontFamily = FontFamily(light, regular, medium, semibold)

// Use the font family to define a custom typography
val craneTypography = Typography(
    defaultFontFamily = craneFontFamily,
    /* ... */
)

// Pass the typography to a MaterialTheme that will create a theme using
// that typography in the part of the UI hierarchy where this theme is used
@Composable
fun CraneTheme(content: @Composable () -> Unit) {
    MaterialTheme(typography = craneTypography) {
        content()
    }
}

Learn more about typography in the Theming in Compose documentation.
如需详细了解排版，请参阅 Compose 中的主题文档。
