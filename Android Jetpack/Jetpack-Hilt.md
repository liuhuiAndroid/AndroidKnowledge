Hilt 官方文档：https://developer.android.com/training/dependency-injection/hilt-android?hl=zh-cn

- API
  - @HiltAndroidApp
  - @AndroidEntryPoint
  - @Singleton 全局单例
  - @ActivityScoped Activity内单例
  - @InstallIn
  - @Provides 提供具体注入逻辑

- 依赖注入有什么用？

  内部的值不由自己来提供，交给外部。

  - 自动加载
  - 自动加载的关键：数据共享
  - 不共享的数据需要依赖注入吗？也需要；可能需要在多处使用且逻辑一样，少些重复代码

- Hilt 有什么用？

  - 更方便地进行依赖注入

- 要用 Hilt 吗？

  - Dagger2
    - Dagger 需要手动注入
    - 需要自行定义 Scope
    - Dagger 复杂且完备；Hilt 对 Dagger 场景化，专门应用于 Android 开发
    - Dagger 有一定的上手成本
    - Dagger 依赖关系无法追踪；Hilt 可以追踪
    - Dagger 编译前生成代码，增加初次编译时间
  - Koin
    - 比 Dagger 方便
    - 运行时动态进行

- ButterKnife 是依赖注入吗？不是依赖注入，是视图绑定