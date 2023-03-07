Intrinsic measurements in Compose layouts
Compose 布局中的固有特性测量

One of the rules of Compose is that you should only measure your children once; measuring children twice throws a runtime exception.
Compose 有一项规则，即，子项只能测量一次，测量两次就会引发运行时异常。

However, there are times when you need some information about your children before measuring them.
但是，有时需要先收集一些关于子项的信息，然后再测量子项。

Intrinsics lets you query children before they're actually measured.
借助固有特性，您可以先查询子项，然后再进行实际测量。

To a composable, you can ask for its intrinsicWidth or intrinsicHeight:
对于可组合项，您可以查询其 intrinsicWidth 或 intrinsicHeight：

(min|max)IntrinsicWidth: Given this height, what's the minimum/maximum width you can paint your content properly?
(min|max)IntrinsicWidth：给定此高度，可以正确绘制内容的最小/最大宽度是多少？

(min|max)IntrinsicHeight: Given this width, what's the minimum/maximum height you can paint your content properly?
(min|max)IntrinsicHeight：给定此宽度，可以正确绘制内容的最小/最大高度是多少？

For example, if you ask the minIntrinsicHeight of a Text with infinite width, it'll return the height of the Text as if the text was drawn in a single line.
例如，如果您查询具有无限 width 的 Text 的 minIntrinsicHeight，它将返回 Text 的 height，就好像该文本是在单行中绘制的一样。

Note: Asking for intrinsics measurements doesn't measure the children twice.
注意：请求固有特性测量不会两次测量子项。

Children are queried for their intrinsic measurements before they're measured and then,
based on that information the parent calculates the constraints to measure its children with.
系统在测量子项前会先查询其固有测量值，然后父项会根据这些信息计算测量其子项时使用的约束条件。

Intrinsics in action
固有特性的实际运用

Imagine that we want to create a composable that displays two texts on the screen separated by a divider like this:
假设我们需要创建一个可组合项，该可组合项在屏幕上显示两个用分隔线隔开的文本，如下所示：

Two text elements side by side, with a vertical divider between them

How can we do this?
我们该怎么做？

We can have a Row with two Texts inside that expands as much as they can and a Divider in the middle.
我们可以将两个 Text 放在同一 Row，并在其中最大程度地扩展，另外在中间放置一个 Divider。

We want the Divider to be as tall as the tallest Text and thin (width = 1.dp).
我们需要将分隔线的高度设置为与最高的 Text 相同，粗细设置为 width = 1.dp。

@Composable
fun TwoTexts(
    text1: String,
    text2: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
                .wrapContentWidth(Alignment.Start),
            text = text1
        )
        Divider(
            color = Color.Black,
            modifier = Modifier.fillMaxHeight().width(1.dp)
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
                .wrapContentWidth(Alignment.End),
            text = text2
        )
    }
}

@Preview
@Composable
fun TwoTextsPreview() {
    MaterialTheme {
        Surface {
            TwoTexts(text1 = "Hi", text2 = "there")
        }
    }
}

If we preview this, we see that the Divider expands to the whole screen and that's not what we want:
预览时，我们发现分隔线扩展到整个屏幕，这并不是我们想要的效果：

Two text elements side by side, with a divider between them, but the divider stretches down below the bottom of the text

This happens because Row measures each child individually and the height of Text cannot be used to constraint the Divider.
之所以出现这种情况，是因为 Row 会逐个测量每个子项，并且 Text 的高度不能用于限制 Divider。

We want the Divider to fill the available space with a given height.
我们希望 Divider 以一个给定的高度来填充可用空间。

For that, we can use the height(IntrinsicSize.Min) modifier .
为此，我们可以使用 height(IntrinsicSize.Min) 修饰符。

height(IntrinsicSize.Min) sizes its children being forced to be as tall as their minimum intrinsic height.
height(IntrinsicSize.Min) 可将其子项的高度强行调整为最小固有高度。

As it's recursive, it'll query Row and its children minIntrinsicHeight.
由于该修饰符具有递归性，因此它将查询 Row 及其子项 minIntrinsicHeight。

Applying that to our code, it'll work as expected:
将其应用到代码中，就能达到预期的效果：

@Composable
fun TwoTexts(
    text1: String,
    text2: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.height(IntrinsicSize.Min)) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
                .wrapContentWidth(Alignment.Start),
            text = text1
        )
        Divider(
            color = Color.Black,
            modifier = Modifier.fillMaxHeight().width(1.dp)
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
                .wrapContentWidth(Alignment.End),
            text = text2
        )
    }
}

With preview:
预览如下：

Two text elements side by side, with a vertical divider between them

The Row composable's minIntrinsicHeight will be the maximum minIntrinsicHeight of its children.
Row 可组合项的 minIntrinsicHeight 将作为其子项的最大 minIntrinsicHeight。

The Divider element's minIntrinsicHeight is 0 as it doesn't occupy space if no constraints are given; the Text minIntrinsicHeight will be that of the text given a specific width.
Divider 元素的 minIntrinsicHeight 为 0，因为如果没有给出约束条件，它不会占用任何空间；如果给出特定 width，Text minIntrinsicHeight 将为文本的高度。

Therefore, the Row element's height constraint will be the max minIntrinsicHeight of the Texts.
因此，Row 元素的 height 约束条件将为 Text 的最大 minIntrinsicHeight；

Divider will then expand its height to the height constraint given by the Row.
而 Divider 会将其 height 扩展为 Row 给定的 height 约束条件。

Intrinsics in your custom layouts

When creating a custom Layout or layout modifier, intrinsic measurements are calculated automatically based on approximations.

Therefore, the calculations might not be correct for all layouts. These APIs offer options to override these defaults.

To specify the instrinsics measures of your custom Layout, override the minIntrinsicWidth, minIntrinsicHeight, maxIntrinsicWidth, and maxIntrinsicHeight of the MeasurePolicy interface when creating it.

@Composable
fun MyCustomComposable(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    return object : MeasurePolicy {
        override fun MeasureScope.measure(
            measurables: List<Measurable>,
            constraints: Constraints
        ): MeasureResult {
            // Measure and layout here
        }

        override fun IntrinsicMeasureScope.minIntrinsicWidth(
            measurables: List<IntrinsicMeasurable>,
            height: Int
        ) = {
            // Logic here
        }

        // Other intrinsics related methods have a default value,
        // you can override only the methods that you need.
    }
}
When creating your custom layout modifier, override the related methods in the LayoutModifier interface.


fun Modifier.myCustomModifier(/* ... */) = this.then(object : LayoutModifier {

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        // Measure and layout here
    }

    override fun IntrinsicMeasureScope.minIntrinsicWidth(
        measurable: IntrinsicMeasurable,
        height: Int
    ): Int = {
        // Logic here
    }

    // Other intrinsics related methods have a default value,
    // you can override only the methods that you need.
})
Learn more
Learn more about intrinsic measurements in the Intrinsics section of the Layouts in Jetpack Compose codelab.