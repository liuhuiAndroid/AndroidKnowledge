###### 官方协程框架的运用

- kotlinx.coroutines

  - 官方协程框架，基于标准库实现的特性封装
  - <https://github.com/Kotlin/kotlinx.coroutines>

- 协程框架的引入

  ```
  // 标准库
  implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version"
  // 协程基础库
  implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5'
  // 协程Android库，提供Android UI调度器
  implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.5'
  ```

- Kotlin 协程的启动模式

  - DEFAULT 立即开始调度协程体，调度前若取消则直接取消
  - ATOMIC 立即开始调度，且在第一个挂起点前不能被取消
  - LAZY 只有在需要 ( start / join / await ) 时开始调度
  - UNDISPATCHED 立即在当前线程执行协程体，直到遇到第一个挂起点

  ```
  GlobalScope.launch(start = CoroutineStart.DEFAULT){
      log(-1)
      delay(1000L)
  	log(-2)
  }
  ```

- Kotlin 协程的调度器

  - Default 线程池，适合 CPU 密集型
  - Main UI 线程
  - Unconfined 直接执行
  - IO 线程池，比 Default 多了队列，适合 IO 密集型

- 其他特性

  - Channel：热数据流，并发安全的通信机制
  - Flow：冷数据流，协程的响应式API
  - Select：可对多个挂起事件进行等待

###### 回调转协程的完整写法

- 不支持取消的写法

  ```
  suspend fun <T> Call<T>.awaitNonCancellable(): T = suspendCoroutine{
      ...
  }
  ```

- 支持取消的写法

  ```
  suspend fun <T> Call<T>.await(): T = suspendCancellableCoroutine{
      continuation ->
      continuation.invokeOnCancellation{
          cancel()
      }
      ...
  }
  ```

- Retrofit 回调转协程，官方也有同样实现

  ```
  suspend fun <T> Call<T>.await(): T = suspendCancellableCoroutine{
      continuation ->
      continuation.invokeOnCancellation{
          cancel()
      }
      enqueue(object:Callback<T>{
          override fun onFailure(call: Call<T>,t: Throwable){
              continuation.resumeWithException(t)
          }
          override fun onResponse(call: Call<T>,response: Response<T>){
              response.takeIf{ it.isSuccessful }?.body()?.also{ continuation.resume(it) }?			continuation.resumeWithException(HttpException(response))
          }
      })
  }
  ```

- Handler

  ```
  suspend fun <T> Handler.run(block: ()-> T) = suspendCoroutine<T>{
      continuation ->
      post{
          try{
              continuation.resume(block())
          }catch(e: Exception){
              continuation.resumeWithException(e)
          }
      }
  }
  
  suspend fun <T> Handler.runDelay(delay: Long, block: ()-> T) = suspendCancellableCoroutine<T>{
      continuation ->
      val message = Message.obtain(this){
          try{
              continuation.resume(block())
          }catch(e: Exception){
              continuation.resumeWithException(e)
          }
      }.also{
          it.obj = continuation
      }
      continuation.invokeOnCancellation{
      	removeCallbacksAndMessage(continuation)
      }
      sendMessageDelayed(message,delay)
  }
  
  suspend fun main(){
      Looper.prepareMainLooper()
      GlobalScope.launch{
          val handler = Handler(Looper.getMainLooper())
          val result = handler.run{ "Hello" }
          val delayedResult = handler.runDelay(1000){ "World" }
  	    Looper.getMainLooper().quit()
      }
  }
  ```

###### Channel

- 非阻塞的通信基础设施
- 类似于 BlockingQueue + 挂起函数
- Channel 的分类
  - RENDEZVOUS
    - 不见不散，send 调用后挂起直到 receive 到达
  - UNLIMITED
    - 无限容量，send 调用后直接返回
  - CONFLATED
    - 保留最新，receive 只能获得最近一次 send 的值
  - BUFFERED
    - 默认容量，可通过程序参数设置默认大小，默认为64
  - FIXED
    - 固定容量，通过参数执行缓存大小
- Channel 的关闭
  - 调用 close 关闭 Channel
  - 关闭后调用 send 抛异常，isClosedForSend 返回 true
  - 关闭后调用 receive 可接收缓存的数据
  - 缓存消费完后 receive 抛异常，isClosedForReceive 返回 true
- Channel 的迭代
- Channel 的协程 Builder
  - produce：启动一个生产者协程，返回 ReceiveChannel
  - actor：启动一个消费者协程，返回 SendChannel
  - 以上 Builder 启动的协程结束后自动关闭对应的 Channel

###### Select

- Select 是一个IO多路复用的概念
- 协程的 Select 用于挂起函数的多路复用

###### 案例：统计代码行数

###### Flow

- Sequence

- 读取 Flow

  ```
  suspend fun main(){
      val intFlow = flow{
          ...
      }
      intFlow.collect{
          log(it)
      }
  }
  ```

- 使用调度器

  ```
  GlobalScope.launch(dispatcher){
      val intFlow = flow{
          ...
      }
      intFlow.flowOn(Dispatchers.IO).collect{
          log(it)
      }
  }
  ```

- 异常处理

  ```
  flow{
      emit(1)
      throw Exception("div 0")
  }.catch{
      log("caught error")
  }.onCompletion{
      t: Throwable? -> log("finally.")
  }
  ```

- Flow 的取消

  - Flow 的运行依赖于协程
  - Flow 的取消取决于 collect 所在协程的取消
  - collect 作为挂起函数可以响应所在协程的取消状态

- 从集合创建 Flow

  ```
  listOf(1,2,3,4).asFlow()
  setOf(1,2,3,4).asFlow()
  flowOf(1,2,3,4)
  ```

- 从 Channel 创建 Flow

- Flow 元素并发生成

  ```
  channelFlow{
      send(1)
      withContext(Dispatchers.IO){
          send(2)
      }
  }
  ```

- Back Pressure

  - buffer：指定固定容量的缓存
  - conflate：保留最新的值
  - collectLatest：新值发送时取消之前的

###### 案例：协程在 Android 中的应用

- 协程实现对话框

  ```
  suspend fun Context.alert(title:String,message: String): Boolean = suspendCancellableCoroutine{
      continuation ->
      
      AlertDialog.Builder(this)
      	.setNegativeButton("No"){ dialog, _->
              dialog.dismiss()
              continuation.resume(false)
      	}
      	.setPositiveButton("Yes"){ dialog, _->
              dialog.dismiss()
              continuation.resume(true)
      	}
      	.setTitle(title)
      	.setMessage(message)
      	.setOnCancelListener{
              continuation.resume(false)
      	}
      	.create()
      	.alse{
              continuation.invokeOnCancellation{
                  dialog.dismiss()
              }
      	}
      	.show()
  }
  
  lifecycleScope.launch{
      val myChoice = alert("Warning","Do you want this?")
      toast("myChoice = $myChoice")
  }
  ```

  