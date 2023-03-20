https://gitbook.cn/books/5aedb7582b543f6855b437af/index.html

1. Camera2 使用方法（重要）

   1. 使用 Camera2 的步骤
   2. 使用 SurfaceView 预览显示 Camera 数据
   3. 参考
      1. https://www.youtube.com/watch?v=Xtp3tH27OFs
      2. Camera2和1的区别：https://www.youtube.com/watch?v=KhqGphh6KPE 
      3. https://github.com/android/camera-samples
      4. https://developer.android.com/training/camera2

2. 使用 OpenGl ES 预览相机数据（重要）

   1. 使用 OpenGL ES 绘制相机数据必备的基本知识
      1. OpenGL ES 渲染流程
      2. EGL：为 OpenGL EG 提供上下文及窗口管理
      3. OpenGL ES 中的纹理

3. 使用 MediaCodec 实现相机录制（重要）

   1. 什么是 MediaCodec

      [MediaCodec 官方文档](https://developer.android.com/reference/android/media/MediaCodec) 全英文看着头疼，[MediaCodec 官方文档网友翻译](https://www.cnblogs.com/roger-yu/p/5635494.html)

      MediaCodec 是一个多媒体编解码处理类
   2. MediaCodec 的操作原理
   3. MediaCodec 的使用步骤
   4. 使用 MediaCodec 录制一段绘制到 Surface 上的数据
   5. 使用 FFmpeg+x264/openh264 进行录制

4. 音频录制

   1. AudioRecord 和 MediaRecorder 比较
      1. AudioRecord 基于字节流录音，MediaRecorder 基于文件录音
      2. AudioRecord 优点：可实时处理，缺点：输出 PCM 数据，需要用 AudioTrack 来处理
      3. MediaRecorder 优点：高度封装，操作简单，缺点：无法实时处理；MediaRecorder 内部也是调用了 AudioRecord
   2. AudioRecord 的工作流程
   3. MediaRecorder

5. 音视频混合

   1. 使用 MediaMuxer 进行音视频混合

      [MediaMuxer 官方文档地址](https://developer.android.com/reference/android/media/MediaMuxer)

   2. MediaMuxer 的工作流程

   3. 使用 FFmpeg 进行音视频混合

6. 多段视频拼接合成

   1. 使用 Android 原生实现视频合成

      MediaCodec + MediaExtractor + MediaMuxer
   2. 使用 mp4parser 合成多个视频：https://github.com/sannies/mp4parser
   3. 使用 FFmpeg 实现视频合成

7. 如何获取视频帧

   将视频的部分帧拿出在下面显示出来，并且添加上面的动态贴纸显示。

   1. 如何拿出视频帧？

      Android 平台下主要有两种拿视频帧的方法：

      1. 使用 ThumbnailUtils，一般用来拿去视频缩略图。
      2. 使用 MediaMetadataRetriever 的 getFrameAtTime() 拿视频帧

   2. 如何分解 Gif 图？

      1. Google 工具类 GifDecoder
      2. [Glide StandardGifDecoder](https://github.com/bumptech/glide/blob/7fb8b1258a3aabbedad3b8d15f59c8091498160a/third_party/gif_decoder/src/main/java/com/bumptech/glide/gifdecoder/StandardGifDecoder.java)

8. FFmpeg

9. 参考链接及项目

   1. 关于 OpenGl 的学习：[LearnOpenGL-CN](https://learnopengl-cn.readthedocs.io/zh/latest/intro/)
