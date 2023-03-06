### 架构

##### MVP为什么比MVC更适合Android开发

MVC容易导致Activity代码膨胀

- layout.xml 是静态布局，无法帮助Controller分担数据绑定逻辑

  可利用 DataBinding / Jetpack Compose 解决此问题

- Activity作为页面载体，需要处理页面生命周期中的逻辑

  Jetpack Lifecycle	Jetpack ViewModel

  抽象BaseActivity统一处理生命周期事件

- 需要分担一些异步任务调度的工作

  AsyncTask	Kotlin Coroutine	RxJava	Kotlin Flow

  基于线程池封装自己的异步任务调度框架

Activity中的代码，为什么不能膨胀

- 性能角度：Activity在使用期间，会驻留在内存
- 分层架构角度：某个层次过厚，不利于解耦合

Activity被划入View层的好处

- 用户行为，统一由View层接收、处理和分发
- 数据绑定逻辑和页面生命周期的职责，同时被划入View层，Activity、Presenter的职责都变得更清晰
- Presenter实例的生命周期可以更短，不必和Activity生命周期保持一致

依赖倒置

- 依赖抽象接口，而非具体实现
- 解决实现替换难问题

MVP为什么比MVC更适合Android开发

- MVP：单一职责原则 依赖倒置
- 解决Activity代码膨胀问题
- 解决实现替换难问题
- 解决逻辑复用难问题

##### MVVM就是MVP再加上DataBinding吗？不是

MVVM中的数据流向

MVVM重点解决的问题

- 关注点分离，让页面的构建和业务的建模之间松耦合，让这两部分的开发者彼此互不关注，只共同关注页面建模（ViewModel）
- 数据驱动视图，ViewModel的职责是帮View托管数据和状态，由ViewModel中的数据变化去驱动页面的变化，解决View调用一致性问题

MVP vs MVVM

- 由于依赖倒置的存在，MVP更像是一种接口提炼方式，适应场景更多，而MVVM专注于现有页面的功能开发
- ViewModel是针对View建模，导致ViewModel和View之间存在耦合，ViewModel不利于代码复用
- Databinding、Jetpack ViewModel、Jetpack LiveData、Jetpack Compose、Kotlin Flow等官方工具库和官方实践，都是为了解决Android MVC时期存在的种种问题，这些工具，大多符合MVVM和MVI思想

##### Jetpack给架构模式带来了怎样的改变？

MVVM + Jetpack

Jetpack 是一个由多个库组成的套件，可帮助开发者遵循最佳实践、减少样板代码并编写可在各种 Android 版本和设备中一致运行的代码，让开发者可将精力集中于真正重要的编码工作。

https://developer.android.com/jetpack

Jetpack 提升一致性、研发效率、兼容性

- Jetpack MVVM

  - Jetpack Lifecycle ViewModel，Activity生命周期一致性

    在“生命周期管理一致性的基础上”，解决：

    - 把托管的状态，与生命周期对齐，不受Activity实例回收的影响
    - 让Activity与Fragment共享作用域

  - Jetpack Lifecycle LiveData，感知Activity生命周期的数据持有者

    可观察到生命周期感知到数据持有者

    - 感知到页面退出后，不再分发和接收数据

  - Jetpack DataBinding，使布局中的View组件和模型进行数据绑定

- Jetpack View

  - Jetpack Compose，用组合函数、DSL实现非xml方式的页面
  - Jetpack Material Desigh组件，提供Material Design风格的组件
  - Jetpack Paging3，页面加载和分页库
  - Jetpack ViewPager2，实现滑动形式的View或Fragment

- Model层

  - Jetpack Room，创建、管理SQLite中的持久化数据的ORM框架
  - Jetpack Work，调度和排期后台任务

##### Compose给MVVM带来了怎样的改变？

使用Jetpack DataBinding容易遇到的问题

- View和Model发生耦合
- xml中的代码有数据映射的逻辑

DataBinding虽然支持在xml中写代码，但不提倡写复杂的业务逻辑，只提倡写绑定ViewModel的逻辑。

MVVM的基础是页面建模，并不是数据绑定框架。

声明式编程

Compose 带来自由组合、风格一致、高易读性的UI代码

##### Room给MVVM带来了怎样的改变？

专注业务建模、提高数据一致性

##### 你做过组件化吗？如何实现组件化？

组件化：一种基于复用的软件开发方式，通过定义、实现和集成独立的组件形成系统。

组件化能解决什么问题？

- 复用：复用组件形成系统
  - 组件级别的代码复用
  - 组件的即插即用
    - 编译时，通过脚本插拔
    - 运行时，动态下发，热插拔 -> 插件化
  - 组件的变种复用
- 隔离：独立的组件
  - 明确人员分工
  - 降低测试成本
  - 提升编译速度

路由式代码解耦

