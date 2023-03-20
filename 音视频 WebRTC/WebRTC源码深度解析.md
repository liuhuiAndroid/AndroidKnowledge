#### WebRTC的整体架构

直播的由来

1. 音视频的起源：1876年贝尔发明了电话；1872年奔跑的马
2. 实时互动直播的起源：WebEx
3. 传统直播的起源：Adobe

直播的技术方向

1. 以WebRTC为代表的实时互动直播：音视频会议、在线互动课堂
2. 以CDN为代表的传统直播吗：直播购物、娱乐直播、游戏直播

实时互动直播的难点：

1. 音视频服务质量与实时性之间有矛盾
2. 需要解决回音、噪音等问题

实时互动直播：

1. 实时互动直播要自己实现传输的可靠性
2. 实时互动直播要实现回音消除
3. 实时互动直播对实时性要求苛刻
4. 由于TCP重传超时的退避机制导致不适合实时传输

实时互动直播的几个重要指标：

1. 实时通信延迟指标
2. 音频服务质量评测指标
3. 视频服务质量评测指标

为什么要选择WebRTC？

WebRTC整体架构

 

#### WebRTC源码分析环境的搭建

1. 如何获取WebRTC源码
   1. 从官方源码地址获取
      1. www.webrtc.org
      2. https://webrtc.googlesource.com/src
      3. 下载depot_tools
      4. 配置PATH环境变量
      5. 下载源码
   2. 从声网提供的镜像获取
      1. https://webrtc.org.cn/mirror/
   3. Windows下编译WebRTC
      1. https://avdancedu.com/2bafd6cf/
2. 编译安装必备的工具
   1. WinSDK：查看Windows下编译WebRTC
   2. VS 2019
3. 如何编译WebRTC源码
   1. 使用gn，它与cmake功能相同
   2. 使用ninja，它与make功能相同
   3. 编译WebRTC的参数含义
      1. gn gen out/Default
      2. gn clean out/Default
      3. gn args --list out/Default
      4. gn args --list out/Default --list=is_debug
4. 运行—Demo调试
5. 详解WebRTC的目录结构

 ```shell
 # 测试从官网下载编译成功
 cd /Users/sec/Documents/github-ndk
 # 安装 depot_tools
 git clone https://chromium.googlesource.com/chromium/tools/depot_tools.git
 # 把 depot_tools 加入环境变量
 vim ~/.bash_profile
 export PATH="/Users/sec/Documents/github-ndk/depot_tools:$PATH"
 source ~/.bash_profile
 # clone 源代码
 mkdir webrtc
 cd webrtc
 fetch --nohooks webrtc
 gclient sync
 # 编译
 cd src
 git checkout master
 git new-branch study-master
 git merge master
 gn gen out/Default
 ninja -C out/Default
 
 # git config配置,主要作用是配置git缓存区，配置git限速和超时时间，防止网络状态不佳时拉区代码失败
 git config --global http.postBuffer 1048576000
 git config --global http.lowSpeedLimit 0
 git config --global http.lowSpeedTime 999999
 git config --global http.version HTTP/1.1
 
 vi ~/.bashrc
 alias python='/usr/local/bin/python2'
 source ~/.bashrc
 ```



#### 开启WebRTC源码分析之路

一对一实时通信架构

peerconnection_client的构成

几个重要的信令

1. 用户登录信令 SignIn
2. 用户退出信令 SignOut
3. 用户等待信令 Wait
4. 透传信令 Message

媒体协商

源码分析—操作界面的编写

源码分析—主体流畅

源码分析—媒体协商

源码分析—视频的渲染



#### 深入理解WebRTC的线程模型

1. 线程基础知识
2. 常见的线程模型 
3. WebRTC的线程模型
4. 源码分析—WebRTC线程间的切换
5. 源码分析—从接口层到实现层
6. 源码分析—信号处理



#### WebRTC媒体协商

1. 媒体协商的意义
2. SDP协议
3. Offer的创建
4. Answer的创建
5. SetLocalDescription做了哪些事儿
6. SetRemoteDescription做了哪些事儿



#### NetEQ

NetEQ的作用是进行抖动控制和丢包隐藏，通过该技术可以让音频更平滑

NetEQ的位置

消除抖动的基本原理

NetEQ整体架构

NetEQ用到的

几种缓冲区

NetEQ中的MCU和DSP



#### Simulcast与SVC

什么是Simulcast

开启Simulcast的三种方式

1. Munging SDP方式
2. 老的rid方式（firefox）
3. 新的rid方式（规范）

Simulcast的实现

SVC分层

WebRTC开启SVC

VP9中的SVC码流

