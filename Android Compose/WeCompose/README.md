# WeCompose
扔物线版微信

material
foundation
animation
ui
runtime

val name = mutableStateOf("compose")
使用 name.value

// 加在可能进入recompose过程的变量上
var name by remember { mutableStateOf("compose") }
使用 name

val nums = mutableStateListOf(1,2,3)
val map = mutableStateMapOf(1 to "One", 2 to "Two")

@Stable 不要轻易重写equals

val big by mutableStateOf(false)
val anim = remember{ Animatable(48.dp, Dp.VectorConverter) }
LaunchedEffect(big){
    // 设置初始值
    anim.snapTo(if(big) 144.dp else 0.sp)
    anim.animateTo(if(big) 96.dp else 48.sp)
}

val size by animateDpAsState(if(big) 96.dp else 48.sp)

val bigTransition = updateTransition(big)
val size by bigTransition.animateDp{ if(big) 144.dp else 0.sp }
val cornerSize by bigTransition.animateDp{ if(big) 96.dp else 48.sp }

Modifier.drawWithContent
Modifier.drawBehind 专门用来绘制背景
Modifier.drawWithCache
Modifier.layout

View系统使用ComposeView.setContent使用Compose
Compose可以使用AndroidView这个Composable函数操作View系统