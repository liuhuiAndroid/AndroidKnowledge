Flow 的 Terminal Operator

```
count
collect/collectLatest
toList/toSet/toCollection
single/first
flod/reduce
asLiveData
launchIn/produceIn/broadcastIn
```

Flow 的创建

```kotlin
flow {
    for (i in 1..5) {
        delay(100)
        emit(i)
    }
    // 监听 Flow 的创建
    .onStart { println("Starting flow") }
    // 监听 Flow 的结束
    .onCompletion { cause ->
        // 判断是否有异常
        if (cause != null)
            println("Flow completed exceptionally")
        else
            println("Done")
    }
    // 捕获来自上游的异常
    .catch{ println("catch exception") }
    .collect{
    	println(it)
    }
}
// flowOf
flowOf(1,2,3,4,5)
    .onEach {
        delay(100)
    }
	// 只有在正常结束时才会被调用
	.onCompleted { println("Completed...") }
    .collect{
        println(it)
    }
// asFlow
listOf(1, 2, 3, 4, 5).asFlow()
    .onEach {
        delay(100)
    }.collect {
        println(it)
    }
// channelFlow
channelFlow {
        for (i in 1..5) {
            delay(100)
            send(i)
        }
    }.collect{
        println(it)
    }
```

Flow 切换线程：flowOn

```
flow {
    for (i in 1..5) {
        delay(100)
        emit(i)
    }
}.map {
    it * it
}.flowOn(Dispatchers.IO)
    .collect {
        println(it)
    }
```

Flow 的取消

```
withTimeoutOrNull(2500) {
    flow {
        for (i in 1..5) {
        delay(1000)
        emit(i)
    }
    }.collect {
        println(it)
    }
}
```

