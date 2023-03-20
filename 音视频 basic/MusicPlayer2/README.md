FFmpeg 解码音频

Audacity：https://github.com/audacity/audacity

Audacity 是一个易用、多轨音频录制和编辑的自由、开源、跨平台音乐软件。可以在 Windows, Mac OS X, GNU/Linux 和其他操作系统上使用。

```c
struct SwrContext *swr_alloc_set_opts(
  // 重采样上下文
  struct SwrContext *s,    
  // 输出的layout, 如：5.1声道…
  int64_t out_ch_layout, 
  // 输出的样本格式。Float, S16, S24
  enum AVSampleFormat out_sample_fmt, 
  // 输出的样本率。可以不变。
  int out_sample_rate,
  // 输入的layout。
  int64_t  in_ch_layout, 
  // 输入的样本格式。
  enum AVSampleFormat  in_sample_fmt, 
  // 输入的样本率。
  int  in_sample_rate,       
  // 日志，不用管，可直接传0
  int log_offset, 
  void *log_ctx);
```

针对音频的播放速度，可以通过样本率的改变而改变。

待了解：opensl es

音视频同步一般以音频为准

OpenSL ES步骤：
1、创建接口对象
2、设置混音器
3、创建播放器（录音器）
4、设置缓冲队列和回调函数
5、设置播放状态
6、启动回调函数