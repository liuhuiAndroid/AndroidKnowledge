#### ffplay.c的意义

ffplay.c是FFmpeg源码⾃带的播放器，调⽤FFmpeg和SDL API实现⼀个⾮常有⽤的播放器。 

例如哔哩哔哩著名开源项⽬ijkplayer也是基于ffplay.c进⾏⼆次开发。

ffplay实现了播放器的主体功能，掌握其原理对于我们独⽴开发播放器⾮常有帮助。 



#### FFplay框架分析 



#### FFplay数据结构分析

- VideoState：播放器封装
- Clock：时钟封装
- MyAVPacketList和PacketQueue队列
- Frame 和 FrameQueue队列



#### AudioParams ⾳频参数



#### Decoder解码器封装



####  数据读取线程

从ffplay框架分析我们可以看到，ffplay有专⻔的线程read_thread()读取数据，且在调⽤av_read_frame 读取数据包之前需要做例如打开⽂件，查找配置解码器，初始化⾳视频输出等准备阶段，主要包括三⼤步骤：

1. 准备⼯作
   1. avformat_alloc_context 创建上下⽂
2. For循环读取数据
3. 退出线程处理

