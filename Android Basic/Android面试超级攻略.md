### JVM

#####  JVM知识体系梳理

- 对 Java 语言本身的了解
- 是否能够定位内存、异步编程等方面的问题
- 对 JVM 进行调优

什么是 JVM？Java 虚拟机，其实是一种规范，是个抽象概念。

- 并不是真正的机器，而是由软件实现
- 可以有多种不同的实现方式
  - HotSpot VM（Sun JDK、Open JDK）
  - Dalvik VM、ART（Google 公司，不是严格意义上的 JVM）
  - J9 VM（IBM公司）
  - JRockit（BEA公司，已被整合进 HotSpot）

JVM 的职责：在硬件上运行 JVM 语言（可编译成 Java 字节码的语言：Java、Groovy、Kotlin）

- 执行字节码指令
  - 执行引擎
  - 运行时栈帧
  - 异步
- 加载字节码中的 Class 结构
  - Class 文件
  - 类
  - ClassLoader
- 分配和回收代码运行时的内存
  - 运行时数据区
  - 堆垃圾回收
  - 堆调优

Android 虚拟机 Dalvik & ART

- Dex 文件
- 虚拟寄存器
- Java 堆
- JIT & AOT

##### Android平台的虚拟机是基于栈的吗？

不是。

Android平台的虚拟机：Dalvik & ART

基于栈的虚拟机？

操作数栈还是虚拟机栈？是操作数栈

运行时栈帧

字节码指令集

基于寄存器的虚拟机 VS 基于栈的虚拟机 各自好处是什么？

JVM运行时数据区

---------------

虚拟机栈

- 记录线程内方法的执行状态

栈帧

- 栈中的元素，对应每一个方法的执行情况

Dalvik 虚拟寄存器

- 编译优化：以使用最少寄存器为前提，不改变语义，优化掉无用代码

如何看待基于栈的虚拟机设计？

- JVM 基于栈去设计的初衷之一，是压缩代码体积
  - Java 设计之初最重要的一个特性就是跨平台，支持嵌入式设备和手持设备
  - Java 设计之初，另一个特性是，支持远程传输执行字节码，要降低传输开销。
- 基于栈的虚拟机，字节码实现简单。
  - 指的是生成字节码的过程简单，而不是虚拟机本身简单，或者说是编译器的实现简单
  - 操作时，不用考虑寄存器的地址（绝对位置），只需要把想要操作的数据出栈、入栈，然后再实现如何对栈顶进行操作就可以了
- 基于栈的虚拟机，可移植性高
  - 为了提高效率，虚拟寄存器要映射到真实机器的寄存器，增加移植难度
  - 代码移植到其他硬件平台的时候，不用考虑真实机器寄存器的差异，因为操作栈的指令是通用的

如何看待 Android 平台基于寄存器的设计？

- 更快、更省内存
  - 指令条数少
  - 数据移动次数少、临时结果存放次数少
  - 映射真实机器的寄存器
- Android 不需要解决移植性问题
  - Android 平台的操作系统是统一的
- 用其他方式解决了代码体积问题
  - dex 文件优化

##### 为什么dex文件比class文件更适合移动端？

##### 你能不能自己写一个叫做java.lang.Object的类？

##### 所有被new出来的实例，都是放在堆中的吗？

##### GC为什么会导致应用程序卡顿？

##### Android平台虚拟机中的GC又是怎样的？

##### 双重检测的单例，为什么还要加volatile关键字？

##### JVM篇总结

### Android平台特性

##### 你做过哪些内存治理相关的工作？

1.  堆内存 内存抖动和内存泄漏
   1. 垃圾回收机制 STW
   2. 内存分配
   3. 堆内存OOM
      1. 虚拟机没有足够的内存为实例分配空间，并且垃圾回收器也没有空间可以回收的情况
      2. 为什么导致了申请的内存过大？——内存抖动
         1. 创建了大型实例
         2. 短时间内创建了非常多的小实例
2. 线程 线程数超限 OOM 野线程 管理
3. fd fd数超限 OOM
   1. 文件未正常关闭
   2. I/O未正常关闭
   3. Cursor未正常关闭

##### Android中，如何进行堆内存治理?

1. String 与内存抖动

2. Bitmap 内存治理

   Bitmap 是 Android 中图片在内存里的形态

3. Activity 内存泄漏

   1. 尽量别作为参数传递Activity
   2. Activity生命周期结束时取消异步状态
   3. 使用静态内部类，并且不持有或弱引用Activity

##### Android中，如何进行线程和FD治理？

OOM：pthread_create failed

- 系统分配线程栈失败

FD也叫文件句柄，在Linux里，文件、目录、块设备、字符设备、管道等都被抽象成了文件，所以在调用这些文件的时候，都会返回一个FD

- 控制FD的数量
- 回收
  - 及时关闭I/O操作
  - 及时关闭数据库和Cursor
  - 及时释放HandlerThread

##### 如何实现一个能加载海量数据的列表？

分页，回收复用

Jetpack Paging 3

RecyclerView 缓存复用

##### 当我们点击应用图标时，系统都做了什么？

- 冷启动

  App进程 -> Application -> First Activity

- 热启动

  将已启动过的Activity重新启动，或切到前台

- 温启动

  Application -> First Activity

得分点

- 冷热温启动的基本流程
- Zygote和APP进程启动
- AMS的作用
- Activity任务栈和生命周期

##### AMS，是如何帮助App启动Activity的？

##### 启动Activity之前，是如何为它分配任务栈的？

##### Activity是如何显示在屏幕上的？

##### Android中的屏幕刷新机制是怎样的？

VSync信号

##### 在SQLite中，without rowid的使用场景是什么？

##### Parcelable为什么速度优于 Serializable ？

序列化：将实例的状态转换为可以存储或传输的形式的过程

Parcelable 牺牲易用性换取极致的性能

Serializable：使用了反射获取信息

Parcelable：数据写入共享内存；kotlin 使用简化，编译时生成代码

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

### 工程思维

##### 如何从工程构建的角度出发，解决问题？

构建过程中，可以通过Gradle做些什么？

- 提升程序安全性
  - 代码混淆
  - 包签名
  - 应用加固、加壳、防破解
- 降低工程维护成本
  - 多渠道打包
  - 多dex打包，解决方法数超限问题
  - 组件化
  - 热修复
  - 注入监控代码
- 提高代码生产力
  - 消除样板代码
  - 提高方式一致性

##### Android工程的构建过程是怎样的？

阅读Gradle源码

Gradle 的启动阶段

1. 执行GradleMain.main()
2. 创建、注册GlobalScopeService
3. 注册META-INF/Services下的服务
4. 注册其他服务
5. 执行DefaultGradleLauncher.executeTasks()，开始构建

##### Gradle在构建阶段都做了什么？

1. 编译buildSrc
2. 创建project的实例（root project、sub project）
3. 解析project参数
4. 配置projects
   1. 每个project开始配置前执行beforeEvaluate回调
   2. 编译执行每个project的build.gradle文件
   3. 每个project配置完成后执行afterEvaluate回调
5. 同时给root project apply默认插件，注册默认服务
6. 调用projectEvaluated回调

##### Gradle中的Task是什么？

TaskGraph：有向无环图

Gradle的TaskGraph阶段

1. 处理要被过滤掉的Task
2. 确认要执行的默认Task
3. 解析符合执行条件的EntryTask（入口任务）
4. 递归找到目标Task的前置、后置Task
5. 填充TaskGraph，形成有向无环图

Gradle的RunTasks阶段

1. 处理任务过滤逻辑
2. 创建线程、处理并发逻辑
3. 执行Task的预处理
4. 遍历执行Action，Action开始执行之前调用beforeAction回调，执行完成后调用afterAction回调

##### AndroidGradlePlugin的工作原理

1. 环境检查
   1. 检查gradle版本，是否与AGP版本匹配
   2. 检查工程路径是否合法
   3. 检查子工程路径是否重复定义
2. 初始化
   1. 初始化BuildSessionImpl单例
   2. 是否应用了相同版本的AGP
   3. 初始化Profiler，创建profile文件
3. 工程配置
   1. 创建AndroidBuilder实例
   2. 应用Java插件
   3. 设置监听器，在构建结束后清理缓存
4. Extension配置
5. 创建Task
   1. 创建不依赖Variant的task
   2. 在afterEvaluate回调之后，创建以来Variant的task

##### 如何用工程手段，提高写代码的生产力？

代码自动生成技术

JavaPoet

##### 如何用字节码手段，实现热修复？

MultiDex插队法

- QQ空间超级补丁
- 大众点评

Dex全量替换法

- 微信Tinker

方法插桩法

