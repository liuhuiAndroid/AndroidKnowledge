ffmpeg：超快音视频编码器

```shell
# 基本信息
ffmpeg -h
ffmpeg -h > ffmpeg_h.log
# 高级信息
ffmpeg -h long
ffmpeg -h full > ffmpeg_h_long.log
# 所有信息
ffmpeg -h full
ffmpeg -h full > ffmpeg_h_full.log
# mac将findstr替换为grep
ffmpeg -h full | grep 264
# 两条命令
ffmpeg -i test_1920x1080.mp4 -acodec copy -vcodec libx264 -s 1280x720 test_1280x720.flv
ffmpeg -i test_1920x1080.mp4 -acodec copy -vcodec libx265 -s 1280x720 test_1280x720.mkv
# 查看具体分类所支持的参数
ffmpeg -h type=name
ffmpeg -h muxer=flv
ffmpeg -encoders | grep x264
ffmpeg -h encoder=libx264

# ffmpeg命令提取音视频数据
# 保留封装格式
ffmpeg -i test.mp4 -acodec copy -vn audio.mp4
ffmpeg -i test.mp4 -vcodec copy -an video.mp4
# 提取视频
# 保留编码格式
ffmpeg -i test.mp4 -vcodec copy -an test_copy.h264
# 强制格式
ffmpeg -i test.mp4 -vcodec libx264 -an test.h264
# 提取音频
# 保留编码格式
ffmpeg -i test.mp4 -acodec copy -vn test.aac
# 强制格式
ffmpeg -i test.mp4 -acodec libmp3lame -vn test.mp3

# ffmpeg命令提取像素格式
# 提取YUV
# 提取3秒数据，分辨率和源视频一致
ffmpeg -i test_1280x720.mp4 -t 3 -pix_fmt yuv420p yuv420p_orig.yuv
# 提取3秒数据，分辨率转为320x240
ffmpeg -i test_1280x720.mp4 -t 3 -pix_fmt yuv420p -s 320x240 yuv420p_320x240.yuv
# 提取RGB
# 提取3秒数据，分辨率转为320x240
ffmpeg -i test.mp4 -t 3 -pix_fmt rgb24 -s 320x240 rgb24_320x240.rgb
# RGB和YUV之间的转换
ffmpeg -s 320x240 -pix_fmt yuv420p -i yuv420p_320x240.yuv -pix_fmt rgb24 rgb24_320x240_2.rgb
# 提取PCM
ffmpeg -i buweishui.mp3 -ar 48000 -ac 2 -f s16le 48000_2_s16le.pcm
ffmpeg -i buweishui.mp3 -ar 48000 -ac 2 -sample_fmt s16 out_s16.wav
ffmpeg -i buweishui.mp3 -ar 48000 -ac 2 -codec:a pcm_s16le out2_s16le.wav
ffmpeg -i buweishui.mp3 -ar 48000 -ac 2 -f f32le 48000_2_f32le.pcm
ffmpeg -i test.mp4 -t 10 -vn -ar 48000 -ac 2 -f f32le 48000_2_f32le_2.pcm

# ffmpeg命令转封装
# 保持编码格式
ffmpeg -i test.mp4 -vcodec copy -acodec copy test_copy.ts
ffmpeg -i test.mp4 -codec copy test_copy2.ts
# 改变编码格式
ffmpeg -i test.mp4 -vcodec libx265 -acodec libmp3lame out_h265_mp3.mkv
# 修改帧率
ffmpeg -i test.mp4 -r 15 output2.mp4
# 修改视频码率，（此时音频也被重新编码）
ffmpeg -i test.mp4 -b 400k output_b.mkv 
# 修改视频码率
ffmpeg -i test.mp4 -b:v 400k output_bv.mkv
# 修改音频码率，如果不想重新编码video，需要加上-vcodec copy
ffmpeg -i test.mp4 -b:a 192k output_ba.mp4
# 修改音视频码率
ffmpeg -i test.mp4 -b:v 400k -b:a 192k output_bva.mp4
# 修改视频分辨率
ffmpeg -i test.mp4 -s 480x270 output_480x270.mp4
# 修改音频采样率
ffmpeg -i test.mp4 -ar 44100 output_44100hz.mp4

# ffmpeg 命令裁剪和合并视频
# 找三个不同的视频每个视频截取10秒内容，如果音视频格式不统一则强制统一为 -vcodec libx264 -acodec aac
ffmpeg -i 沙海02.mp4 -ss 00:05:00 -t 10 -codec copy 1.mp4
ffmpeg -i 复仇者联盟3.mp4 -ss 00:05:00 -t 10 -codec copy 2.mp4
ffmpeg -i 红海行动.mp4 -ss 00:05:00 -t 10 -codec copy 3.mp4
# 将上述1.mp4/2.mp4/3.mp4转成ts格式
ffmpeg -i 1.mp4 -codec copy -vbsf h264_mp4toannexb 1.ts
ffmpeg -i 2.mp4 -codec copy -vbsf h264_mp4toannexb 2.ts
ffmpeg -i 3.mp4 -codec copy -vbsf h264_mp4toannexb 3.ts
# 转成flv格式
ffmpeg -i 1.mp4 -codec copy 1.flv
ffmpeg -i 2.mp4 -codec copy 2.flv
ffmpeg -i 3.mp4 -codec copy 3.flv
# 分离某些封装格式（例如MP4/FLV/MKV等）中的H.264的时候，需要首先写入SPS和PPS，否则会导致分离出来的数据没有SPS、PPS而无法播放。H.264码流的SPS和PPS信息存储在AVCodecContext结构体的extradata中。需要使用ffmpeg中名称为“h264_mp4toannexb”的bitstream filter处理
# 开始拼接文件，建议以文件列表的方式进行拼接，建议转成TS格式再进行拼接
# 以MP4格式进行拼接
ffmpeg -f concat -i mp4list.txt -codec copy out_mp42.mp4
# 以TS格式进行拼接
ffmpeg -i "concat:1.ts|2.ts|3.ts" -codec copy out_ts.mp4
ffmpeg -f concat -i tslist.txt -codec copy out_ts2.mp4
# 以FLV格式进行拼接
ffmpeg -f concat -i flvlist.txt -codec copy out_flv2.mp4

# fmpeg命令图片和视频转换
# 截取一张图片
ffmpeg -i test.mp4 -y -f image2 -ss 00:00:02 -vframes 1 -s 640x360 test.jpg
ffmpeg -i test.mp4 -y -f image2 -ss 00:00:02 -vframes 1 -s 640x360 test.bmp
# 转换视频为图片（每帧一张图)
ffmpeg -i test.mp4 -t 5 -s 640x360 -r 15 frame%03d.jpg
# 图片转换为视频
ffmpeg -f image2 -i frame%03d.jpg -r 25 video.mp4

# ffmpeg命令GIF和视频转换
# 从视频中生成GIF图片
ffmpeg -i test.mp4 -t 5 -r 1 image1.gif
ffmpeg -i test.mp4 -t 5 -r 25 -s 640x360 image2.gif
# 将GIF转化为视频
ffmpeg -f gif -i image2.gif image2.mp4

# ffmpeg拉流
# 直播拉流
ffplay rtmp://server/live/streamName
# 对于不是rtmp的协议 -c copy要谨慎使用
ffmpeg -i rtmp://server/live/streamName -c copy dump.flv
# 直播推流
ffmpeg -re -i out.mp4 -c copy flv rtmp://server/live/streamName
# -re,表示按时间戳读取文件
# 参考：Nginx搭建rtmp流媒体服务器(Ubuntu16.04)https://www.jianshu.com/p/16741e363a77

# ffmpeg视频裁剪
ffmpeg -i input.jpg -vf crop=iw/3:ih:0:0 output.jpg
ffmpeg -i input.jpg -vf crop=iw/3:ih:iw/3:0 output.jpg
ffmpeg -i input.jpg -vf crop=iw/3:ih:iw/3*2:0 output.jpg

# FFmpeg 视频多宫格处理
ffmpeg -i 1.mp4 -i 2.mp4 -i 3.mp4 -i 4.mp4 -filter_complex "nullsrc=size=640x480[base];[0:v] setpts=PTSSTARTPTS,scale=320x240[upperleft];[1:v]setpts=PTS-STARTPTS,scale=320x240[upperright];[2:v]setpts=PTSSTARTPTS, scale=320x240[lowerleft];[3:v]setpts=PTSSTARTPTS,scale=320x240[lowerright];[base][upperleft]overlay=shortest=1[tmp1];[tmp1][upperright]overlay=s
hortest=1:x=320[tmp2];[tmp2][lowerleft]overlay=shortest=1:y=240[tmp3];[tmp3][lowerright]overlay=shortest
=1:x=320:y=240" out.mp4
```

ffplay：简单媒体播放器

```shell
# 所有信息
ffplay -h
ffplay -h > ffplay_h.log
# 播放控制
ffplay -volume 4 believe.mp4
# q, ESC 退出播放
# f 全屏切换

# ffplay命令:主要选项
# -x width 强制显示宽度
# -y height 强制显示高度
# -video_size size 设置帧尺寸
# -pixel_format format 格式设置像素格式
# -fs 以全屏模式启动
# -an 禁用音频
# -vn 禁用视频
# -ss pos 指定位置开始播放
# -t duration 设置播放视频/音频长度
# -volume vol 设置起始音量。音量范围[0 ~100]
# -loop number 设置播放循环次数
# -stats 打印多个回放统计信息，包括显示流持续时间，编解码器参数，流中的当前位置，以及音频/视频同步差值。默认情况下处于启用状态，要显式禁用它则需要指定-nostats。
# -sync type 同步类型 将主时钟设置为audio（type=audio），video（type=video）或external（type=ext），默认是audio为主时钟。
# -autoexit 视频播放完毕后退出。
# -codec:media_specifier codec_name 强制使用设置的多媒体解码器，media_specifier可用值为a（音频）， v（视频）和s字幕。比如-codec:v h264_qsv 强制视频采用h264_qsv解码
# -acodec codec_name 强制使用设置的音频解码器进行音频解码
# -vcodec codec_name 强制使用设置的视频解码器进行视频解码
# -infbuf 不限制输入缓冲区大小。尽可能快地从输入中读取尽可能多的数据。播放实时流时默认启用，如果未及时读取数据，则可能会丢弃数据。此选项将不限制缓冲区的大小。若需禁用则使用-noinfbuf
# 更多参考：http://www.ffmpeg.org/ffplay.html

# 播放本地文件
ffplay -window_title "test time" -ss 2 -t 10 -autoexit test.mp4
ffplay buweishui.mp3
# 播放网络流
ffplay -window_title "rtmp stream" rtmp://202.69.69.180:443/webcast/bshdlive-pc
# 强制指定解码器
# 强制指定mpeg4解码器：
ffplay -vcodec mpeg4 test.mp4
# 强制指定h264解码器：
ffplay -vcodec h264 test.mp4
# 禁用音频或视频
# 禁用音频：
ffplay test.mp4 -an
# 禁用视频：
ffplay test.mp4 -vn
# 播放YUV数据
ffplay -pixel_format yuv420p -video_size 320x240 -framerate 5 yuv420p_320x240.yuv
# 播放RGB数据
ffplay -pixel_format rgb24 -video_size 320x240 -i rgb24_320x240.rgb
ffplay -pixel_format rgb24 -video_size 320x240 -framerate 5 -i rgb24_320x240.rgb
# 播放PCM数据
ffplay -ar 48000 -ac 2 -f f32le 48000_2_f32le.pcm

# 简单过滤器
# 视频旋转
ffplay -h filter=transpose
ffplay -i test.mp4 -vf transpose=1
# 视频反转
ffplay test.mp4 -vf hflip
ffplay test.mp4 -vf vflip
# 视频旋转和反转
ffplay test.mp4 -vf hflip,transpose=1
# 音频变速播放
ffplay -i test.mp4 -af atempo=2
# 视频变速播放
ffplay -i test.mp4 -vf setpts=PTS/2
# 音视频同时变速
ffplay -i test.mp4 -vf setpts=PTS/2 -af atempo=2
# 更多参考：http://www.ffmpeg.org/ffmpeg-filters.html

# ffmpeg添加文字水印
# 将文字的水印加在视频的左上角
ffplay -i input.mp4 -vf "drawtext=fontsize=100:fontfile=FreeSerif.ttf:text='hello world':x=20:y=20"
# 将字体的颜色设置为绿色
ffplay -i input.mp4 -vf "drawtext=fontsize=100:fontfile=FreeSerif.ttf:text='hello world':fontcolor=green"
# 如果想调整文字水印显示的位置，调整 x 与 y 参数的数值即可
ffplay -i input.mp4 -vf "drawtext=fontsize=100:fontfile=FreeSerif.ttf:text='hello 
world':fontcolor=green:x=400:y=200"
# 修改透明度
ffplay -i input.mp4 -vf "drawtext=fontsize=100:fontfile=FreeSerif.ttf:text='hello 
world':fontcolor=green:x=400:y=200:alpha=0.5"
# 文字水印还可以增加一个框，然后给框加上背景颜色
ffplay -i input.mp4 -vf "drawtext=fontsize=100:fontfile=FreeSerif.ttf:text='hello 
world':fontcolor=green:box=1:boxcolor=yellow"
# 以本地时间作为水印内容
ffplay -i input.mp4 -vf "drawtext=fontsize=60:fontfile=FreeSerif.ttf:text='%{localtime\:%Y\-%m\-%d %H-%M-%S}':fontcolor=green:box=1:boxcolor=yellow"
# 在使用 ffmpeg 转码存储到文件时需要加上-re，否则时间不对
ffmpeg -re -i input.mp4 -vf 
"drawtext=fontsize=60:fontfile=FreeSerif.ttf:text='%{localtime\:%Y\-%m\-%d %H-%M-%S}':fontcolor=green:box=1:boxcolor=yellow" out.mp4
# 跑马灯效果
ffplay -i input.mp4 -vf "drawtext=fontsize=100:fontfile=FreeSerif.ttf:text='helloworld':x=mod(100*t\,w):y=abs(sin(t))*h*0.7"

# 图片水印
ffmpeg -i input.mp4 -vf "movie=logo.png[watermark];[in][watermark]overlay=x=10:y=10[out]" output.mp4
# 图片 logo.png 将会打入到 input.mp4 视频中
ffplay -i input.mp4 -vf "movie=logo2.png[watermark];[in][watermark]overlay=50:10[out]"
# 显示位置
ffplay -i input.mp4 -vf "movie=logo.png[watermark];[in][watermark]overlay=10:10[out]"
ffplay -i input.mp4 -vf "movie=logo.png[watermark];[in][watermark]overlay=main_w-overlay_w-10:10[out]"
ffplay -i input.mp4 -vf "movie=logo.png[watermark];[in][watermark]overlay=10:main_h-overlay_h-10[out]"
ffplay -i input.mp4 -vf "movie=logo.png[watermark];[in][watermark]overlay=main_w-overlay_w-10:main_hoverlay_h-10[out]"
# 跑马灯效果
ffplay -i input.mp4 -vf "movie=logo.png[watermark];[in][watermark]overlay=x=mod(50*t\,main_w):y=abs(sin(t))*h*0.7[out]"

# FFmpeg 生成画中画
# 显示画中画效果
ffplay -i input.mp4 -vf "movie=sub_320x240.mp4[sub];[in][sub]overlay=x=20:y=20[out]"
ffplay -i input.mp4 -vf "movie=sub_320x240.mp4[sub];[in][sub]overlay=x=20:y=20:eof_action=1[out]"
ffplay -i input.mp4 -vf "movie=sub_320x240.mp4[sub];[in][sub]overlay=x=20:y=20:shortest =1[out]"
# 缩放子画面尺寸
ffplay -i input.mp4 -vf "movie=sub_320x240.mp4,scale=640x480[sub];[in][sub]overlay=x=20:y=20[out]"
# 跑马灯
ffplay -i input.mp4 -vf "movie=sub_320x240.mp4[test];[in][test]overlay= 
x=mod(50*t\,main_w):y=abs(sin(t))*main_h*0.7[out]"
```

ffprobe：简单多媒体流分析器

```shell
# 所有信息
ffprobe -h
ffprobe -h > ffprobe_h.log
```

FFmpeg 命令参数说明

```shell
# 主要参数：
# -i 设定输入流
# -f 设定输出格式(format)
# -ss 开始时间
# -t 时间长度

# 音频参数：
# -aframes 设置要输出的音频帧数
# -b:a 音频码率
# -ar 设定采样率
# -ac 设定声音的Channel数
# -acodec 设定声音编解码器，如果用copy表示原始编解码数据必须被拷贝。
# -an 不处理音频
# -af 音频过滤器
ffmpeg -i test.mp4 -b:a 192k -ar 48000 -ac 2 -acodec libmp3lame -aframes 200 out2.mp3

# 视频参数：
# -vframes 设置要输出的视频帧数
# -b 设定视频码率
# -b:v 视频码率
# -r 设定帧速率
# -s 设定画面的宽与高
# -vn 不处理视频
# -aspect aspect 设置横纵比 4:3 16:9 或 1.3333 1.7777
# -vcodec 设定视频编解码器，如果用copy表示原始编解码数据必须被拷贝。
# -vf 视频过滤器
ffmpeg -i test.mp4 -vframes 300 -b:v 300k -r 30 -s 640x480 -aspect 16:9 -vcodec libx265
# 更多参考：http://www.ffmpeg.org/ffmpeg.html
```

