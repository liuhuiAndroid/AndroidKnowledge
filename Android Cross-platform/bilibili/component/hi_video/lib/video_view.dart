import 'package:chewie/chewie.dart' hide MaterialControls;
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:hi_base/color.dart';
import 'package:hi_base/view_util.dart';
import 'package:orientation/orientation.dart';
import 'package:video_player/video_player.dart';

// 通过hide隐藏源码UI，然后导入自定义播放器UI
import 'hi_video_controls.dart';

///播放器组件
class VideoView extends StatefulWidget {
  // 视频url
  final String url;

  // 视频封面
  final String cover;

  // 是否自动播放
  final bool autoPlay;

  // 是否循环播放
  final bool looping;

  // 视频缩放比例
  final double aspectRatio;

  // 自定义播放器UI，顶部
  final Widget? overlayUI;

  // 自定义播放器UI，弹幕
  final Widget? barrageUI;

  const VideoView(this.url,
      {Key? key,
      required this.cover,
      this.autoPlay = false,
      this.looping = false,
      this.aspectRatio = 16 / 9,
      this.overlayUI,
      this.barrageUI})
      : super(key: key);

  @override
  _VideoViewState createState() => _VideoViewState();
}

class _VideoViewState extends State<VideoView> {
  // video_player播放器Controller
  late VideoPlayerController _videoPlayerController;
  // chewie播放器Controller
  late ChewieController _chewieController;
  // 封面
  get _placeholder => FractionallySizedBox(
        widthFactor: 1,
        child: cachedImage(widget.cover),
      );

  // 进度条颜色配置
  get _progressColors => ChewieProgressColors(
      playedColor: primary,
      handleColor: primary,
      backgroundColor: Colors.grey,
      bufferedColor: primary[50]!);

  @override
  void initState() {
    super.initState();
    // 初始化播放器设置
    _videoPlayerController = VideoPlayerController.network(widget.url);
    _chewieController = ChewieController(
        videoPlayerController: _videoPlayerController,
        aspectRatio: widget.aspectRatio,
        autoPlay: widget.autoPlay,
        looping: widget.looping,
        // 未播放时封面
        placeholder: _placeholder,
        // 播放速度是否显示
        allowPlaybackSpeedChanging: false,
        customControls: MaterialControls(
          // 初始状态是否显示Loading
          showLoadingOnInitialize: false,
          // 是否显示中间大的播放按钮
          showBigPlayIcon: false,
          // 底部渐变，防止视频颜色浅无法看到底部控制器和图标
          bottomGradient: blackLinearGradient(),
          overlayUI: widget.overlayUI,
          barrageUI: widget.barrageUI,
        ),
        materialProgressColors: _progressColors);
    //fix iOS无法正常退出全屏播放问题
    _chewieController.addListener(_fullScreenListener);
  }

  @override
  void dispose() {
    _chewieController.removeListener(_fullScreenListener);
    _videoPlayerController.dispose();
    _chewieController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    // 取屏幕的宽度
    double screenWidth = MediaQuery.of(context).size.width;
    // 计算高度
    double playerHeight = screenWidth / widget.aspectRatio;
    return Container(
      width: screenWidth,
      height: playerHeight,
      color: Colors.grey,
      child: Chewie(
        controller: _chewieController,
      ),
    );
  }

  void _fullScreenListener() {
    Size size = MediaQuery.of(context).size;
    if (size.width > size.height) {
      OrientationPlugin.forceOrientation(DeviceOrientation.portraitUp);
    }
  }
}
