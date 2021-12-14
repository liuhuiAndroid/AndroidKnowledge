### OkHttp的简单使用

##### 1. 创建一个 OkHttp 的实例

```groovy
OkHttpClient client = new OkHttpClient.Builder().build();
```

##### 2. 创建 Request

```kotlin
Request request = new Request.Builder().url("http://hencoder.com").build();
```

##### 3. 创建 Call 并发起网络请求

```kotlin
client.newCall(request).enqueue(new Callback() {
    @Override
    public void onResponse(Call call, okhttp3.Response response){

    }

    @Override
    public void onFailure(Call call, IOException e) {
    }
});
```

###  OkHttp源码分析

**源码分析总结**

- OkHttpClient 相当于配置中⼼，所有的请求都会共享这些配置(例如出错是否重试、共享的连接池)。 OkHttpClient 中的配置主要有:

  1. Dispatcher dispatcher：调度器，⽤于调度后台发起的⽹络请求，有后台总请求数和单主机总请求数的控制。

  2. List<Protocol> protocols：⽀持的应⽤用层协议，即 HTTP/1.1、HTTP/2 等。

  3. List<ConnectionSpec> connectionSpecs：应⽤层⽀持的 Socket 设置，即使⽤明⽂传输(⽤于HTTP)还是某个版本的TLS(⽤于HTTPS)。

  4. List<Interceptor> interceptors：⼤多数时候使用的 Interceptor 都应该配置到这⾥。

  5. List<Interceptor> networkInterceptors：直接和⽹络请求交互的 Interceptor 配

     置到这⾥，例如如果你想查看返回的 301 报⽂或者未解压的 Response Body，需要在这⾥看。

  6. CookieJar cookieJar：管理 Cookie 的控制器。OkHttp 提供了 Cookie 存取的判断⽀持(即什么时候需要存 Cookie，什么时候需要读取 Cookie，但没有给出具体的存取实现。 如果需要存取 Cookie，你得⾃己写实现，例如用 Map 存在内存⾥，或者⽤别的方式存在本地存储或者数据库。

  7. Cache cache：Cache 存储的配置。默认是没有，如果需要用，得⾃己配置出 Cache 存储的⽂件位置以及存储空间上限。

  8. HostnameVerifier hostnameVerifier：⽤于验证 HTTPS 握手过程中下载到的证书所属者是否和⾃己要访问的主机名一致。

  9. CertificatePinner certificatePinner：⽤于设置 HTTPS 握⼿过程中针对某个 Host 的 Certificate Public Key Pinner，即把⽹站证书链中的每一个证书公钥直接拿来提前配置进 OkHttpClient 里去，以跳过本地根证书，直接从代码⾥进⾏认证。这种用法⽐较少见，⼀般⽤于防⽌⽹站证书被人仿制。

  10. Authenticator authenticator：⽤于⾃动重新认证。配置之后，在请求收到 401 状态码的响应是，会直接调用 authenticator ，⼿动加入 Authorization header 之后自动重新发起请求。

  11. boolean followRedirects：遇到重定向的要求是，是否自动 follow。

  12. boolean followSslRedirects：在重定向时，如果原先请求的是 http 而重定向的目标是 https，或者原先请求的是 https ⽽重定向的⽬标是 http，是否依然自动 follow。(记得， 不是「是否自动 follow HTTPS URL 重定向的意思，⽽是是否自动 follow 在 HTTP 和 HTTPS 之间切换的重定向)

  13. boolean retryOnConnectionFailure：在请求失败的时候是否自动重试。注意，⼤多数的请求失败并不属于 OkHttp 所定义的「需要重试」，这种重试只适用于「同一个域名的多个 IP 切换重试」「Socket 失效重试」等情况。

  14. int connectTimeout：建立连接(TCP 或 TLS)的超时时间。

  15. int readTimeout：发起请求到读到响应数据的超时时间。

  16. int writeTimeout：发起请求并被目标服务器接受的超时时间。(为什么?因为有时候对方服务器可能由于某种原因⽽不读取你的 Request)

- newCall(Request) ⽅法会返回一个 RealCall 对象，它是 Call 接⼝的实现。当调⽤ RealCall.execute() 的时候， RealCall.getResponseWithInterceptorChain() 会被调用，它会发起⽹络请求并拿到返回的响应，装进⼀一个 Response 对象并作为返回值返回; RealCall.enqueue() 被调用的时候大同小异，区别在于 enqueue() 会使⽤用 Dispatcher 的线程池来把请求放在后台线程进⾏，但实质上使用的同样也是 getResponseWithInterceptorChain() ⽅法。

- getResponseWithInterceptorChain() ⽅法做的事：把所有配置好的 Interceptor 放在 一个 List ⾥，然后作为参数，创建一个 RealInterceptorChain 对象，并调⽤ chain.proceed(request) 来发起请求和获取响应。

- 在 RealInterceptorChain 中，多个 Interceptor 会依次调用⾃己的 intercept() 方法。这个⽅法会做三件事：

  - 对请求进行预处理

  - 预处理之后，重新调⽤ RealInterceptorChain.proceed() 把请求交给下一个 Interceptor

  - 在下一个 Interceptor 处理完成并返回之后，拿到 Response 进行后续处理

    当然了，最后⼀个 Interceptor 的任务只有一个：做真正的网络请求并拿到响应

- 从上到下，每级 Interceptor 做的事:

  1. ⾸先是开发者使用 addInterceptor(Interceptor) 所设置的 Interceptor，它们会按照开发者的要求，在所有其他 Interceptor 处理之前，进⾏最早的预处理工作，以及在收到 Response 之后，做最后的善后工作。如果你有统⼀的 header 要添加，可以在这里设置;
  2. 然后是 RetryAndFollowUpInterceptor：它负责在请求失败时的重试，以及重定向的自动后续请求。它的存在，可以让重试和重定向对于开发者是⽆感知的;
  3. BridgeInterceptor：它负责一些不影响开发者开发，但影响 HTTP 交互的一些额外预处理。例如，Content-Length 的计算和添加、gzip 的⽀持(Accept-Encoding: gzip)、 gzip 压缩数据的解包，都是发⽣在这里;
  4. CacheInterceptor：它负责 Cache 的处理。把它放在后⾯的⽹络交互相关 Interceptor 的前⾯的好处是，如果本地有了可⽤的 Cache，⼀个请求可以在没有发⽣实质⽹络交互的情况下就返回缓存结果，⽽完全不需要开发者做出任何的额外⼯作，让 Cache 更加⽆感知;
  5. ConnectInterceptor：它负责建⽴连接。在这⾥，OkHttp 会创建出⽹络请求所需要的 TCP 连接(如果是 HTTP)，或者是建立在 TCP 连接之上的 TLS 连接(如果是 HTTPS)， 并且会创建出对应的 HttpCodec 对象(⽤于编码解码 HTTP 请求);
  6. 然后是开发者使⽤ addNetworkInterceptor(Interceptor) 所设置的，它们的行为逻辑和使⽤ addInterceptor(Interceptor) 创建的⼀样，但由于位置不同，所以这⾥创建的 Interceptor 会看到每个请求和响应的数据(包括重定向以及重试的⼀些中间请求和响应)，并且看到的是完整原始数据，⽽不是没有加 Content-Length 的请求数据，或者 Body 还没有被 gzip 解压的响应数据。多数情况，这个方法不需要被使用;
  7. CallServerInterceptor：它负责实质的请求与响应的 I/O 操作，即往 Socket 里写入请求数据，和从 Socket ⾥读取响应数据。
