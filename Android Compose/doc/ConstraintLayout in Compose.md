ConstraintLayout in Compose
Compose 中的 ConstraintLayout

ConstraintLayout can help place composables relative to others on the screen,
ConstraintLayout 有助于根据可组合项的相对位置将它们放置在屏幕上，

and is an alternative to using multiple nested Row, Column, Box and custom layout elements.
它是使用多个嵌套 Row、Column、Box 和自定义布局元素的替代方案。

ConstraintLayout is useful when implementing larger layouts with more complicated alignment requirements.
在实现对齐要求比较复杂的较大布局时，ConstraintLayout 很有用。

To use ConstraintLayout in Compose, you need to add this dependency in your build.gradle:
如需使用 Compose 中的 ConstraintLayout，您需要在 build.gradle 中添加以下依赖项：

implementation "androidx.constraintlayout:constraintlayout-compose:1.0.0-beta02"

Warning: The constraintLayout-compose artifact has a different versioning than Jetpack Compose.
警告：constraintLayout-compose 工件的版本不同于 Jetpack Compose。

Check the latest version in the ConstraintLayout release page.
在 ConstraintLayout 版本页面中检查最新版本。

Note: In the View system, ConstraintLayout was the recommended way to create large and complex layouts,
as a flat view hierarchy was better for performance than nested views are.
注意：在 View 系统中，我们建议使用 ConstraintLayout 创建复杂的大型布局，因为扁平视图层次结构比嵌套视图的性能更好。

However, this is not a concern in Compose, which is able to efficiently handle deep layout hierarchies.
不过，这在 Compose 中不是什么问题，因为它能够高效地处理较深的布局层次结构。

Note: Whether to use ConstraintLayout or not for a particular UI in Compose is up to the developer's preference.
注意：是否将 ConstraintLayout 用于 Compose 中的特定界面取决于开发者的偏好。

In the Android View system, ConstraintLayout was used as a way to build more performant layouts, but this is not a concern in Compose.
在 Android View 系统中，使用 ConstraintLayout 作为构建更高性能布局的一种方法，但这在 Compose 中并不是问题。

When needing to choose, consider if ConstraintLayout helps with the readability and maintainability of the composable.
在需要进行选择时，请考虑 ConstraintLayout 是否有助于提高可组合项的可读性和可维护性。

ConstraintLayout in Compose works with a DSL:
Compose 中的 ConstraintLayout 支持 DSL：

References are created using createRefs() or createRefFor(), and each composable in ConstraintLayout needs to have a reference associated with it.
引用是使用 createRefs() 或 createRefFor() 创建的，ConstraintLayout 中的每个可组合项都需要有与之关联的引用。

Constraints are provided using the constrainAs() modifier, which takes the reference as a parameter and lets you specify its constraints in the body lambda.
约束条件是使用 constrainAs() 修饰符提供的，该修饰符将引用作为参数，可让您在主体 lambda 中指定其约束条件。

Constraints are specified using linkTo() or other helpful methods.
约束条件是使用 linkTo() 或其他有用的方法指定的。

parent is an existing reference that can be used to specify constraints towards the ConstraintLayout composable itself.
parent 是一个现有的引用，可用于指定对 ConstraintLayout 可组合项本身的约束条件。

Here's an example of a composable using a ConstraintLayout:
下面是使用 ConstraintLayout 的可组合项的示例：

@Composable
fun ConstraintLayoutContent() {
    ConstraintLayout {
        // Create references for the composables to constrain
        val (button, text) = createRefs()

        Button(
            onClick = { /* Do something */ },
            // Assign reference "button" to the Button composable
            // and constrain it to the top of the ConstraintLayout
            modifier = Modifier.constrainAs(button) {
                top.linkTo(parent.top, margin = 16.dp)
            }
        ) {
            Text("Button")
        }

        // Assign reference "text" to the Text composable
        // and constrain it to the bottom of the Button composable
        Text("Text", Modifier.constrainAs(text) {
            top.linkTo(button.bottom, margin = 16.dp)
        })
    }
}

This code constrains the top of the Button to the parent with a margin of 16.dp and a Text to the bottom of the Button also with a margin of 16.dp.
此代码使用 16.dp 的外边距来约束 Button 顶部到父项的距离，同样使用 16.dp 的外边距来约束 Text 到 Button 底部的距离。

For more examples of how to work with ConstraintLayout, try the layouts codelab.
如需查看有关如何使用 ConstraintLayout 的更多示例，请参考布局 Codelab。

Decoupled API

In the ConstraintLayout example, constraints are specified inline, with a modifier in the composable they're applied to.
在 ConstraintLayout 示例中，约束条件是在应用它们的可组合项中使用修饰符以内嵌方式指定的。

However, there are situations when it's preferable to decouple the constraints from the layouts they apply to.
不过，在某些情况下，最好将约束条件与应用它们的布局分离开来。

For example, you might want to change the constraints based on the screen configuration, or animate between two constraint sets.
对于此类情况，您可以通过不同的方式使用 ConstraintLayout：

For cases like these, you can use ConstraintLayout in a different way:

Pass in a ConstraintSet as a parameter to ConstraintLayout.
将 ConstraintSet 作为参数传递给 ConstraintLayout。

Assign references created in the ConstraintSet to composables using the layoutId modifier.
使用 layoutId 修饰符将在 ConstraintSet 中创建的引用分配给可组合项。

@Composable
fun DecoupledConstraintLayout() {
    BoxWithConstraints {
        val constraints = if (minWidth < 600.dp) {
            decoupledConstraints(margin = 16.dp) // Portrait constraints
        } else {
            decoupledConstraints(margin = 32.dp) // Landscape constraints
        }

        ConstraintLayout(constraints) {
            Button(
                onClick = { /* Do something */ },
                modifier = Modifier.layoutId("button")
            ) {
                Text("Button")
            }

            Text("Text", Modifier.layoutId("text"))
        }
    }
}

private fun decoupledConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val button = createRefFor("button")
        val text = createRefFor("text")

        constrain(button) {
            top.linkTo(parent.top, margin = margin)
        }
        constrain(text) {
            top.linkTo(button.bottom, margin)
        }
    }
}

Then, when you need to change the constraints, you can just pass a different ConstraintSet.
然后，当您需要更改约束条件时，只需传递不同的 ConstraintSet 即可。

Learn more
了解详情

Learn more about ConstraintLayout in Compose in the Constraint Layout section of the Layouts in Jetpack Compose codelab,
and see the APIs in action in the Compose samples that use ConstraintLayout.
如需详细了解 Compose 中的 ConstraintLayout，请参阅 Jetpack Compose Codelab 中的布局的约束布局部分，
以及使用 ConstraintLayout 的 Compose 示例中的 API 实际运用。
