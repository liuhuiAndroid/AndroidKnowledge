- https://developer.android.google.cn/jetpack/compose/navigation

Navigating with Compose
使用 Compose 进行导航

The Navigation component provides support for Jetpack Compose applications.
Navigation 组件支持 Jetpack Compose 应用。

You can navigate between composables while taking advantage of the Navigation component’s infrastructure and features.
您可以在利用 Navigation 组件的基础架构和功能的同时，在可组合项之间导航。

Note: If you are not familiar with Compose, review the Jetpack Compose resources before continuing.
注意：如果您不熟悉 Compose，请先查看 Jetpack Compose 资源，然后再继续。

Setup
设置

To support Compose, use the following dependency in your app module’s build.gradle file:
如需支持 Compose，请在应用模块的 build.gradle 文件中使用以下依赖项：

dependencies {
    implementation "androidx.navigation:navigation-compose:2.4.0-alpha09"
}

Getting started
使用入门

The NavController is the central API for the Navigation component.
NavController 是 Navigation 组件的中心 API。

It is stateful and keeps track of the back stack of composables that make up the screens in your app and the state of each screen.
此 API 是有状态的，可以跟踪组成应用屏幕的可组合项的返回堆栈以及每个屏幕的状态。

You can create a NavController by using the rememberNavController() method in your composable:
您可以通过在可组合项中使用 rememberNavController() 方法来创建 NavController：

val navController = rememberNavController()

You should create the NavController in the place in your composable hierarchy where all composables that need to reference it have access to it.
您应该在可组合项层次结构中的适当位置创建 NavController，使所有需要引用它的可组合项都可以访问它。

This follows the principles of state hoisting and allows you to use the NavController and the state it provides via currentBackStackEntryAsState() to be used as the source of truth for updating composables outside of your screens.
这遵循状态提升的原则，并且允许您使用 NavController 及其通过 currentBackStackEntryAsState() 提供的状态作为更新屏幕外的可组合项的可信来源。

See Integration with the bottom nav bar for an example of this functionality.
有关此功能的示例，请参阅与底部导航栏集成。

Note: If you're using the Navigation component for fragments, you don't have to define new navigation graphs in Compose or use NavHost composables.
注意：如果您要使用基于 fragment 的 Navigation 组件，就不必在 Compose 中定义新的导航图，也不必使用 NavHost 可组合项。

See Interoperability for more information.
如需了解详情，请参阅互操作性。

Creating a NavHost
创建 NavHost

Each NavController must be associated with a single NavHost composable.
每个 NavController 都必须与一个 NavHost 可组合项相关联。

The NavHost links the NavController with a navigation graph that specifies the composable destinations that you should be able to navigate between.
NavHost 将 NavController 与导航图相关联，后者用于指定您应能够在其间进行导航的可组合项目的地。

As you navigate between composables, the content of the NavHost is automatically recomposed.
当您在可组合项之间进行导航时，NavHost 的内容会自动进行重组。

Each composable destination in your navigation graph is associated with a route.
导航图中的每个可组合项目的地都与一个路线相关联。

Key Term: Route is a String that defines the path to your composable.
关键术语：路线是一个 String，用于定义指向可组合项的路径。

You can think of it as an implicit deep link that leads to a specific destination.
您可以将其视为指向特定目的地的隐式深层链接。

Each destination should have a unique route.
每个目的地都应该有一条唯一的路线。

Creating the NavHost requires the NavController previously created via rememberNavController() and the route of the starting destination of your graph.
如需创建 NavHost，您需要使用之前通过 rememberNavController() 创建的 NavController，以及导航图的起始目的地的路线。

NavHost creation uses the lambda syntax from the Navigation Kotlin DSL to construct your navigation graph.
NavHost 创建使用 Navigation Kotlin DSL 中的 lambda 语法来构建导航图。

You can add to your navigation structure by using the composable() method.
您可以使用 composable() 方法向导航结构添加内容。

This method requires that you provide a route and the composable that should be linked to the destination:
此方法需要您提供一个路线以及应关联到相应目的地的可组合项：

NavHost(navController = navController, startDestination = "profile") {
    composable("profile") { Profile(/*...*/) }
    composable("friendslist") { FriendsList(/*...*/) }
    /*...*/
}

Note: the Navigation Component requires that you follow the Principles of Navigation and use a fixed starting destination.
注意：Navigation 组件要求您遵循导航原则并使用固定的起始目的地。

You should not use a composable value for the startDestination route.
您不应为 startDestination 路线使用可组合项值。

Navigate to a composable
导航到可组合项

To navigate to a composable destination in the navigation graph, you must use the navigate() method.
如需导航到导航图中的可组合项目的地，您必须使用 navigate() 方法。

navigate() takes a single String parameter that represents the destination’s route.
navigate() 接受代表目的地路线的单个 String 参数。

To navigate from a composable within the navigation graph, call navigate():
如需从导航图中的某个可组合项进行导航，请调用 navigate()：

@Composable
fun Profile(navController: NavController) {
    /*...*/
    Button(onClick = { navController.navigate("friends") }) {
        Text(text = "Navigate next")
    }
    /*...*/
}

You should only call navigate() as part of a callback and not as part of your composable itself, to avoid calling navigate() on every recomposition.
您应仅在回调中调用 navigate()，而不能在可组合项本身中调用它，以避免每次重组时都调用 navigate()。

By default, navigate() adds your new destination to the back stack.
默认情况下，navigate() 会将您的新目的地添加到返回堆栈中。

You can modify the behavior of navigate by attaching additional navigation options to our navigate() call:
您可以通过向我们的 navigate() 调用附加其他导航选项来修改 navigate 的行为：

// Pop everything up to the "home" destination off the back stack before
// navigating to the "friends" destination
navController.navigate(“friends”) {
    popUpTo("home")
}

// Pop everything up to and including the "home" destination off
// the back stack before navigating to the "friends" destination
navController.navigate("friends") {
    popUpTo("home") { inclusive = true }
}

// Navigate to the "search” destination only if we’re not already on
// the "search" destination, avoiding multiple copies on the top of the
// back stack
navController.navigate("search") {
    launchSingleTop = true
}

See the popUpTo guide for more use cases.
如需查看更多用例，请参阅 popUpTo 指南。

Note: The anim block cannot be used with Navigation Compose.
注意：anim 块不能与 Navigation Compose 一起使用。

Transition Animations in Navigation Compose is being tracked in this feature request.
系统会在此功能请求中跟踪 Navigation Compose 中的转换动画。

Navigate with arguments
使用参数进行导航

Navigation compose also supports passing arguments between composable destinations.
Navigation Compose 还支持在可组合项目的地之间传递参数。

In order to do this, you need to add argument placeholders to your route, similar to how you add arguments to a deep link when using the base navigation library:
为此，您需要向路线中添加参数占位符，就像在使用基础导航库时向深层链接中添加参数一样。

NavHost(startDestination = "profile/{userId}") {
    ...
    composable("profile/{userId}") {...}
}

By default, all arguments are parsed as strings.
默认情况下，所有参数都会被解析为字符串。

You can specify another type by using the arguments parameter to set a type:

NavHost(startDestination = "profile/{userId}") {
    ...
    composable(
        "profile/{userId}",
        arguments = listOf(navArgument("userId") { type = NavType.StringType })
    ) {...}
}

You should extract the NavArguments from the NavBackStackEntry that is available in the lambda of the composable() function.

composable("profile/{userId}") { backStackEntry ->
    Profile(navController, backStackEntry.arguments?.getString("userId"))
}

To pass the argument to the destination, you need to add the value to the route in place of the placeholder in the call to navigate:

navController.navigate("profile/user1234")

For a list of supported types, see Pass data between destinations.

Adding optional arguments
Navigation Compose also supports optional navigation arguments. Optional arguments differ from required arguments in two ways:

They must be included using query parameter syntax ("?argName={argName}")
They must have a defaultValue set, or have nullability = true (which implicitly sets the default value to null)
This means that all optional arguments must be explicitly added to the composable() function as a list:

composable(
    "profile?userId={userId}",
    arguments = listOf(navArgument("userId") { defaultValue = "me" })
) { backStackEntry ->
    Profile(navController, backStackEntry.arguments?.getString("userId"))
}

Now, even if there is no argument passed to the destination, the defaultValue of "me" will be used instead.

The structure of handling the arguments through the routes means that your composables remain completely independent of Navigation and are much more testable.

Deep links
Navigation Compose supports implicit deep links that can be defined as part of the composable() function as well. Add them as a list using navDeepLink():

val uri = "https://example.com"

composable(
    "profile?id={id}",
    deepLinks = listOf(navDeepLink { uriPattern = "$uri/{id}" })
) { backStackEntry ->
    Profile(navController, backStackEntry.arguments?.getString("id"))
}

These deep links let you associate a specific URL, action, and/or mime type with a composable. By default, these deep links are not exposed to external apps. To make these deep links externally available you must add the appropriate <intent-filter> elements to your app’s manifest.xml file. To enable the deep link above, you should add the following inside of the <activity> element of the manifest:

<activity …>
  <intent-filter>
    ...
    <data android:scheme="https" android:host="www.example.com" />
  </intent-filter>
</activity>

Navigation will automatically deep link into that composable when the deep link is triggered by another app.

These same deep links can also be used to build a PendingIntent with the appropriate deep link from a composable:

val id = "exampleId"
val context = LocalContext.current
val deepLinkIntent = Intent(
    Intent.ACTION_VIEW,
    "https://example.com/$id".toUri(),
    context,
    MyActivity::class.java
)

val deepLinkPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
    addNextIntentWithParentStack(deepLinkIntent)
    getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
}

You can then use this deepLinkPendingIntent like any other PendingIntent to open your app at the deep link destination.

Nested Navigation
Destinations can be grouped into a nested graph to modularize a particular flow in your app’s UI. An example of this could be a self-contained login flow.

The nested graph encapsulates its destinations. As with the root graph, a nested graph must have a destination identified as the start destination by its route. This is the destination that is navigated to when you navigate to the route associated with the nested graph.

To add a nested graph to your NavHost, you can use the navigation extension function:

NavHost(navController, startDestination = startRoute) {
    ...
    navigation(startDestination = nestedStartRoute, route = nested) {
        composable(nestedStartRoute) { ... }
    }
    ...
}

Integration with the bottom nav bar
By defining the NavController at a higher level in your composable hierarchy, you can connect Navigation with other components such as the BottomNavBar. Doing this allows you to navigate by selecting the icons in the bottom bar.

To link the items in a bottom navigation bar to routes in your navigation graph, it is recommended to define a sealed class, such as Screen seen here, that contains the route and string resource id for the destinations.

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Profile : Screen("profile", R.string.profile)
    object FriendsList : Screen("friendslist", R.string.friends_list)
}

Then place those items in a list that can be used by the BottomNavigationItem:

val items = listOf(
   Screen.Profile,
   Screen.FriendsList,
)

In your BottomNavigation composable, get the current NavBackStackEntry using the currentBackStackEntryAsState() function. This entry gives you access to the current NavDestination. The selected state of each BottomNavigationItem can then be determined by comparing the item's route with the route of the current destination and its parent destinations (to handle cases when you are using nested navigation) via the hierarchy helper method.

The item's route is also used to connect the onClick lambda to a call to navigate so that tapping on the item navigates to that item. By using the saveState and restoreState flags, the state and back stack of that item is correctly saved and restored as you swap between bottom navigation items.

val navController = rememberNavController()
Scaffold(
  bottomBar = {
    BottomNavigation {
      val navBackStackEntry by navController.currentBackStackEntryAsState()
      val currentDestination = navBackStackEntry?.destination
      items.forEach { screen ->
        BottomNavigationItem(
          icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
          label = { Text(stringResource(screen.resourceId)) },
          selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
          onClick = {
            navController.navigate(screen.route) {
              // Pop up to the start destination of the graph to
              // avoid building up a large stack of destinations
              // on the back stack as users select items
              popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
              }
              // Avoid multiple copies of the same destination when
              // reselecting the same item
              launchSingleTop = true
              // Restore state when reselecting a previously selected item
              restoreState = true
            }
          }
        )
      }
    }
  }
) { innerPadding ->
  NavHost(navController, startDestination = Screen.Profile.route, Modifier.padding(innerPadding)) {
    composable(Screen.Profile.route) { Profile(navController) }
    composable(Screen.FriendsList.route) { FriendsList(navController) }
  }
}

Here you take advantage of the NavController.currentBackStackEntryAsState() method to hoist the navController state out of the NavHost function, and share it with the BottomNavigation component. This means the BottomNavigation automatically has the most up-to-date state.

Interoperability
If you want to use the Navigation component with Compose, you have two options:

Define a navigation graph with the Navigation component for fragments.
Define a navigation graph with a NavHost in Compose using Compose destinations. This is possible only if all of the screens in the navigation graph are composables.
Therefore, the recommendation for hybrid apps is to use the fragment-based Navigation component and use fragments to hold view-based screens, Compose screens, and screens that use both views and Compose. Once each screen fragment in your app is a wrapper around a composable, the next step is to tie all of those screens together with Navigation Compose and remove all of the fragments.

Navigate from Compose with Navigation for fragments
In order to change destinations inside Compose code, you expose events that can be passed to and triggered by any composable in the hierarchy:

@Composable
fun MyScreen(onNavigate: (Int) -> ()) {
    Button(onClick = { onNavigate(R.id.nav_profile) } { /* ... */ }
}

In your fragment, you make the bridge between Compose and the fragment-based Navigation component by finding the NavController and navigating to the destination:

override fun onCreateView( /* ... */ ) {
    setContent {
        MyScreen(onNavigate = { dest -> findNavController().navigate(dest) })
    }
}

Alternatively, you can pass the NavController down your Compose hierarchy. However, exposing simple functions is much more reusable and testable.

Testing
We strongly recommended that you decouple the Navigation code from your composable destinations to enable testing each composable in isolation, separate from the NavHost composable.

The level of indirection provided by the composable lambda is what allows you to separate your Navigation code from the composable itself. This works in two directions:

Pass only parsed arguments into your composable
Pass lambdas that should be triggered by the composable to navigate, rather than the NavController itself.
For example, a Profile composable that takes in a userId as input and allows users to navigate to a friend’s profile page might have the signature of:

@Composable
fun Profile(
    userId: String,
    navigateToFriendProfile: (friendUserId: String) -> Unit
) {
 …
}

Here we see that the Profile composable works independently from Navigation, allowing it to be tested independently. The composable lambda would encapsulate the minimal logic needed to bridge the gap between the Navigation APIs and your composable:

composable(
    "profile?userId={userId}",
    arguments = listOf(navArgument("userId") { defaultValue = "me" })
) { backStackEntry ->
    Profile(backStackEntry.arguments?.getString("userId")) { friendUserId ->
        navController.navigate("profile?userId=$friendUserId")
    }
}

Learn more
了解详情

To learn more about Jetpack Navigation, see Get started with the Navigation component or take the Jetpack Compose Navigation codelab.
