基于mediasoup

- 基础知识

  - C++语言基础
  - 服务器基础
    - MAC配置中文man手册

- 高性能网络编程

  - 网络编程基础

  - 异步I/O事件处理

    - 所谓异步IO，是指以事件触发的机制来对IO操作进行处理。与多进程和多线程技术相比，异步I/O技术的最大优势是系统开销小，系统不必创建进程/线程，也不必维护这些进程/线程，从而大大减少了系统的开销。
    - 通过fork实现高性能网络服务器，不足较多

      - 每收到一个连接就创建一个子进程
      - 父进程负责接收连接
      - 通过fork创建子进程

      问题：资源被长期占用；分配子进程花费时间长
    - 通过select实现高性能服务器，IO复用，性能强

      - 遍历文件描述符集中的所有描述，找出有变化的描述符
      - 对于侦听的socket和数据处理的socket要区别对待
      - socket必须设置为非阻塞方式工作
    
  - 通过epoll实现高性能服务器，IO复用，更加高效

    - 比较有名的异步事件处理库
      - **libevent**
      - libevthp
      - libuv
      - libev

  - 使用I/O事件处理库libevent实现高性能网络服务器

- 网络传输协议

  - TCP/IP详解
    - IP协议详解
    - TCP协议
  - UDP/RTP/RTCP 详解

- WebRTC协议 栈

  - WebRTC协议

    - libsrtp

  - ##### SDP协议与WebRTC媒体协商

- 音视频通话

  - 各流媒体服务器的比较
    - 多人音视频架构
      - Mesh 方案
      - MCU 方案
      - SFU 方案
    - 流媒体服务器架构
      - Licode
      - Janus SFU架构
      - Medooze架构
      - **Mediasoup架构**
  - mediasoup服务器的布署与使用
  - mediasoup的信令系统
  - mediasoup源码分析
    - mediasoup库的架构讲解
    - mediasoup_JS_的作用
    - WebRTC中的C++类关系图
    - mediasoup启动详解
    - 匿名管道进程间通信的原理
    - 实战通过socketpair进行进程间通信
    - mediasoup下channel创建的详细过程
    - mediasoup中的消息确信与发送事件机制
    - mediasoup的主业务流程
    - mediasoup连接的创建
    - mediasoup数据流转
    - WebRTC流媒体服务器大规模布署方案
    - 哪种服务器性能好？
    - mediasoup在 Centos下该如何安装？
    - mediasoup安装好后看不对远端视频
    - mediasoup在Ubuntu18.04上安装报错
    - 单台mediasoup流媒体服务器能承载多少路流？
  
  
