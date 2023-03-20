### WebRTC原理与架构

##### WebRTC架构

##### WebRTC的目录结构

| 目录         | 功能                                                   | 目录            | 功能                                            |
| ------------ | ------------------------------------------------------ | --------------- | ----------------------------------------------- |
| api          | WebRTC接口层，浏览器都是通过该接口调用WebRTC           | pc              | Peer Connection，连接相关的逻辑层               |
| call         | 数据流的管理层，Call代表同一个端点的所有数据的流入流出 | p2p             | 端对端相关代码，stun，turn                      |
| video        | 与视频相关的逻辑                                       | rtc_base        | 基础代码，如线程、锁相关的统一接口代码          |
| audio        | 与音频相关的逻辑                                       | rtc_tool        | 音视频分析相关的工具代码                        |
| common_audio | 音频算法相关                                           | tool_webrtc     | WebRTC测试相关的工具代码，如网络模拟器          |
| common_video | 视频算法相关                                           | system_wrappers | 与具体操作系统相关的代码，如CPU特性，原子操作等 |
| media        | 与多媒体相关的逻辑处理，如编解码的逻辑处理             | stats           | 存放各种数据统计相关的类                        |
| logging      | 日志相关                                               | sdk             | 存放Android和iOS层代码。如视频的采集，渲染等    |
| module       | 最重要的目录，子模块                                   |                 |                                                 |

##### WebRTC运行机制

- MediaStream
- RTCPeerConnection
- RTCDataChannel

### Web服务器原理与Nodejs搭建

##### Web服务器工作原理

- Nodejs 特殊：用JS开发服务端程序
- Nginx 性能高，取代Apache
- Apache 老

##### Nodejs环境搭建

```shell
# 二进制库安装
brew install nodejs
brew install npm
brew install forever -g
# 源码安装
# 1. 下载 Nodejs 源码
# 2. 生成Makefile
./configure --prefix=/usr/local/nodejs
ls Makefile
# 3. make -j 4 && sudo make install
# 4. 环境变量
vi ~/.bashrc
```

##### 通过Nodejs开发一个最简单的http服务

- require 引入http模块
- 创建http服务
- 侦听端口

```js
// vi server.js
'use strict'
var http = require('http');
var app = http.createServer(function(req, res){
	res.writeHead(200, {'Content-Type':'text/plain'});
	res.end('Hello World\n');
}).listen(8080, '0.0.0.0');
// node server.js
// nohub node server.js &
// forever start server.js
// forever stop server.js
// 访问：http://localhost:8080/
```

##### 创建https服务

- 个人隐私及安全原因必须用https服务
- https是未来的趋势

Nodejs搭建https服务

- 生成https证书
- 引入https模块
- 指定证书位置，并创建https服务

```js
'use strict'
var https = require('https');
var fs = require('fs');
var options = {
  key  : fs.readFileSync('./cert/1557605_www.learningrtc.cn.key'),
  cert : fs.readFileSync('./cert/1557605_www.learningrtc.cn.pem')
}
var app = https.createServer(options, function(req, res){
	res.writeHead(200, {'Content-Type': 'text/plain'});
	res.end('HTTPS:Hello World!\n');
}).listen(443, '0.0.0.0');
```

##### 实现一个真正的 Web服务器

- 引入 express 模块：用于处理web服务
- 引入 serve-index 模块：将整个目录发布出来
- 指定发布目录

```js
'use strict'
// node自带
var http = require('http');
var https = require('https');
var fs = require('fs');
// 需要npm安装
// npm install serve-index express
var serveIndex = require('serve-index');
var express = require('express');
var app = express();
// 顺序不能换，先设置浏览目录
app.use(serveIndex('./public'));
// 发布静态目录
app.use(express.static('./public'));
var options = {
	key  : fs.readFileSync('./cert/1557605_www.learningrtc.cn.key'),
	cert : fs.readFileSync('./cert/1557605_www.learningrtc.cn.pem') 
}
var https_server = https.createServer(options, app);
https_server.listen(443, '0.0.0.0');
var http_server = http.createServer(app);
http_server.listen(80, '0.0.0.0');
// netstat -ntpl | grep 80：查看哪个进程占用80端口
// kill -9 xxx
```

### JavaScript必备知识回顾

##### JavaScript调试

##### 变量与基本运算

##### 判断循环与函数

### WebRTC设备管理

##### WebRTC获取音视频设备

enumerateDevices

- 基本格式

  var ePromise = navigator.mediaDevices.enumerateDevices

  JavaScript 中的 Promise 是异步调用中的一种方式

- MediaDevicesInfo

  - deviceID：设备ID
  - label：设备的名字，可读
  - kind：设备的种类
  - groupID：两个设备groupID相同，说明是同一个物理设备

##### 在页面中显示获取到的设备

### WebRTC音视频数据采集

##### WebRTC音视频数据采集

音视频采集API

- 基本格式

  var promise = navigator.mediaDevices.getUserMedia(constraints);

- MediaStreamConstraints

  可以对音频和视频做限制

##### WebRTC_API_适配

getUserMedia 的不同实现

- getUserMedia
- webkitGetUserMedia
- mozGetUserMedia

使用 Google 开源库 adapter.js：https://github.com/webrtchacks/adapter 或者 https://webrtc.github.io/adapter/adapter-latest.js

##### 获取音视频设备的访问权限

Safari 浏览器对权限的控制更加严格一些

navigator.mediaDevices.enumerateDevices() 之后再获取设备

##### 视频约束

- width
- height
- aspectRatio
- frameRate：帧率
- facingMode： user 前置摄像头，environment 后置摄像头
- resizeMode：是否需要裁剪

```js
var constraints = {
  video : {
    width: 640,	
    height: 480,
    frameRate:15,
    facingMode: 'enviroment',
    deviceId : deviceId ? {exact:deviceId} : undefined 
  }, 
  audio : false 
}
```

##### 音频约束

- volume：音量0-1
- sampleRate：采样率
- sampleSize：采样大小
- echoCancellation：回音消除
- autoGainControl：自动增音
- noiseSuppression：降噪
- latency：延迟大小
- channelCount：单声道和双声道
- deviceID：多个输入输出设备切换
- groupID：同一个物理设备

##### 视频特效

浏览器视频特效

- CSS filter, -webkit-filter/filter
- 如何将 video 与 filter 关联
- OpenGL/Metal/...

##### 从视频中获取图片

```
picture.getContext('2d').drawImage(videoplay, 0, 0, picture.width, picture.height);
```

##### WebRTC只采集音频数据

##### MediaStreamAPI及获取视频约束

- MediaStream.addTrack()
- MediaStream.removeTrack()
- MediaStream.getVideoTrack()
- MediaStream.getAudioTrack()
- MediaStream.onaddtrack
- MediaStream.onremovetrack
- MediaStream.onended

### WebRTC音视频录制实战

##### WebRTC录制基本知识

MediaRecoder

- 基本格式

  var mediaRecorder = new MediaRecorder(window.stream, options);

- 参数说明

  stream：媒体流，可以从 getUserMedia、video、audio或canvas获取

  options：限制选项

  - mimeType：音频还是视频以及格式
  - audioBitsPerSecond：音频码率
  - videoBitsPerSecond：视频码率
  - bitsPerSecond：整体码率

- MediaRecorder API

  - MediaRecorder.start(timeslice)

    开始录制媒体，timeslice是可选的，如果设置了会按时间切片存储数据

  - MediaRecorder.stop()

    停止录制，此时会触发包括最终Blob数据的dataavailable事件

  - MediaRecorder.pause：暂停录制

  - MediaRecorder.resume：恢复录制

  - MediaRecorder.isTypeSupported：检查录制支持的文件格式

- MediaRecorder 事件

  - MediaRecorder.ondataavailable

    每次记录一定时间的数据时会定期触发，如果没有指定时间片，则记录整个数据时会定期触发

  - MediaRecorder.onerror

    当有错误发生时，录制会被停止

- JavaScript 几种存储数据的方式

  - 字符串
  - Blob
  - ArrayBuffer
  - ArrayBufferView

##### 录制音视频实战

##### WebRTC采集屏面数据

捕获桌面：getDisplayMedia

- 基本格式

  var promise = navigator.mediaDevices.getDisplayMedia(constraints);

- constraints 可选

  constraints 中约束与 getUserMedia 函数中的一致。

### WebRTC信令服务器实现

##### 如何使用socket.io发送消息

- 给本次连接发消息

  socket.emit()

- 给某个房间内的所有人发消息

  io.in(room).emit()

- 除本连接外，给某个房间内所有人发消息

  socket.to(room).emit()
  
- 除本连接外，给所有人发消息

  socket.broadcast.emit()

Socket.IO客户端处理消息

- 发送action 命令
  - S：socket.emit('action');
  - C：socket.on('action', function(){...});
- 发送一个action命令，还有data数据
  - S：socket.emit('action', data);
  - C：socket.on('action', function(data){...})
- 发送一个action命令，还有两个数据
  - S：socket.emit('action', arg1, arg2);
  - C：socket.on('action', function(arg1, arg2){...})
- 发送一个action命令，在emit方法中包含回调函数
  - S：socket.emit('action', data, function(arg1, arg2){...})
  - C：socket.on('action', function(data, fn){fn('a', 'b')})

##### WebRTC信令服务器原理

为什么要使用socket.io

- socket.io 是 WebSocket 超集
- socket.io 有房间的概念
- socket.io 跨平台，跨终端，跨语言

##### WebRTC信令服务器的实现

1. 安装 socket.io
2. 引入 socket.io
3. 处理 connection 消息

##### 利用socket.io实现简单聊天室

### WebRTC网络基础补充：P2P/STUN/TURN/ICE知识

##### WebRTC网络传输基本知识

- NAT：内网地址转公网地址
  - 产生的原因
    - 由于IPv4的地址不够
    - 出于安全的考虑
  - 种类
    - 完全锥型NAT
    - 地址限制锥型NAT
    - 端口限制锥型NAT
    - 对称型NAT
- STUN：交换公网信息
- TURN：负责双方流媒体数据转发
- ICE：找最优路径

##### NAT打洞原理

- C1，C2向STUN发消息
- 交换公网IP及端口

##### NAT类型检测

##### STUN协议

- STUN存在的目的就是进行NAT穿越
- STUN是典型的客户端/服务器模式。客户端发送请求，服务端进行响应。

##### TURN协议

- 目的是解决对称NAT无法穿越的问题
- 其建立在STUN之上，消息格式使用STUN格式消息
- TURN Client要求服务端分配一个公共IP和Port用于接收或发送数据

##### ICE框架

- 收集 Candidate
- 对 Candidate Pair 排序
- 连通性检查

##### 网络分析方法 tcpdump 与 wireshark讲解

- Linux服务端用tcpdump
- 其他端 wireshark

##### 网络分析方法 tcpdump 与 wireshark实战

### 端对端1V1传输基本流程

##### 媒体能力协商过程

RTCPeerConnection

- 媒体协商
  - createOffer
  - createAnswer
  - setLocalDescription
  - setRemoteDescription
- Stream/Track
  - addTrack
  - removeTrack
- 传输相关方法
- 统计相关方法

##### 1:1连接的基本流程

##### 本机内的1:1音视频互通

##### 获取 offer/answer 创建的 SDP

### WebRTC核心之SDP详解

##### SDP规范

- 会话层
- 媒体层

SDP媒体信息

- 媒体格式
- 传输协议
- 传输IP和端口
- 媒体负载类型

##### WebRTC中的SDP

##### WebRTC中Offer_AnswerSDP

### 实现1V1音视频实时互动直播系统

##### STUN_TURN服务器搭建

##### 再论RTCPeerConnection

##### 直播系统中的信令及其逻辑关系

##### 实现1:1音视频实时互动信令服务器

##### 再论CreateOffer

##### WebRTC客户端状态机及处理逻辑

##### WebRTC客户端的实现

##### WebRTC客户端的实现

##### 共享远程桌面

### WebRTC核心之RTP 媒体控制与数据统计

##### RTPRReceiver发送器

##### RTPSender发送器

##### 传输速率的控制

##### WebRTC统计信息

### WebRTC非音视频数据传输

##### 传输非音视频数据基础知识

##### 端到端文本聊天

##### 文件实时传输

### WebRTC实时数据传输网络协议详解

##### RTP-SRTP协议头讲解

##### RTCP中的SR与RR报文

##### DTSL

##### wireshark分析rtp-rtcp包

### Android端与浏览器互通

##### Android与浏览器互通基本知识

##### WebRTCNative开发逻辑

##### 实战-权限申请-库的引入与界面

##### 实战-通过socket.io实现信令收发

##### 实战-Andorid与浏览器互通

