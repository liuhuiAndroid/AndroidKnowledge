#### FFmpeg过滤器框架分析

ffmpeg的filter⽤起来是和Gstreamer的plugin是⼀样的概念，通过avfilter_link，将各个创建好的filter按 ⾃⼰想要的次序链接到⼀起，然后avfilter_graph_config之后，就可以正常使⽤。 ⽐较常⽤的滤镜有：scale、trim、overlay、rotate、movie、yadif。scale 滤镜⽤于缩放，trim 滤镜⽤于帧级剪切，overlay 滤镜⽤于视频叠加，rotate 滤镜实现旋转，movie 滤镜可以加载第三⽅的视频，yadif 滤镜可以去隔⾏。 

- AVFilterGraph：对filters系统的整体管理
- AVFilter：定义filter本身的能⼒
- AVFilterContext：filter实例，管理filter与外部的联系
- AVFilterLink：定义两个filters之间的连接
- AVFilterPad：定义filter的输⼊/输出接⼝
- AVFilterInOut：过滤器链输⼊/输出的链接列表

这⾥需要重点提的是两个特别的filter，⼀个是buffer，⼀个是buffersink

- 滤波器buffer代表filter graph中的源头，原始数据就往这个filter节点输⼊的；
- ⽽滤波器buffersink代表filter graph中的输出节点，处理完成的数据从这个filter节点输出。 



#### 函数使⽤ 

```c
// 获取FFmpeg中定义的filter，调⽤该⽅法前需要先调⽤avfilter_register_all();进⾏滤波器注册 
AVFilter avfilter_get_by_name(const char name);

// 往源滤波器buffer中输⼊待处理的数据 
int av_buffersrc_add_frame(AVFilterContext ctx, AVFrame frame);

// 从⽬的滤波器buffersink中获取处理完的数据 
int av_buffersink_get_frame(AVFilterContext ctx, AVFrame frame);

// 创建⼀个滤波器图filter graph 
AVFilterGraph *avfilter_graph_alloc(void);

// 创建⼀个滤波器实例AVFilterContext，并添加到AVFilterGraph中 
int avfilter_graph_create_filter(AVFilterContext **filt_ctx, const AVFilter *filt, const char name, const char args, void *opaque, AVFilterGraph *graph_ctx);

// 连接两个滤波器节点 
int avfilter_link(AVFilterContext *src, unsigned srcpad, AVFilterContext *dst, unsigned dstpad);
```



#### AVFilter主体框架流程

在利⽤AVFilter进⾏⾳视频数据处理前先将在进⾏的处理流程绘制出来，现在以FFmpeg filter官⽅⽂档中的⼀个例⼦为例进⾏说明。 

这个例⼦的处理流程如上所示，⾸先使⽤split滤波器将input流分成两路流（main和tmp），然后分别对两路流进⾏处理。对于tmp流，先经过crop滤波器进⾏裁剪处理，再经过flip滤波器进⾏垂直⽅向上的翻转操作，输出的结果命名为flip流。再将main流和flip流输⼊到overlay滤波器进⾏合成操作。上图的input就是上⾯提过的buffer源滤波器，output就是上⾯的提过的buffersink滤波器。上图中每个节点都是⼀个AVFilterContext，每个连线就是AVFliterLink。所有这些信息都统⼀由AVFilterGraph来管理。

