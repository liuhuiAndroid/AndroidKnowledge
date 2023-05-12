import 'package:fijkplayer/fijkplayer.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

import 'tiktok_player.dart';

class VideoView extends StatefulWidget {
  final Player player;
  const VideoView(this.player);

  @override
  State<StatefulWidget> createState() => _VideoViewState();
}

class _VideoViewState extends State<VideoView> {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: GestureDetector(
          child: Stack(children: [
            AbsorbPointer(absorbing: true, child: FijkView(player: widget.player)),
            if (widget.player.state == FijkState.paused)
              Align(
                child: Image.asset('asset/images/play.png', width: 70, height: 70),
                alignment: Alignment.center,
              ),
          ]),
          onTap: onTapVideo,
        ));
  }

  void onTapVideo() {
    print('onTapVideo');
    if (widget.player.state == FijkState.paused) {
      widget.player.start();
    } else {
      widget.player.pause();
    }
    setState(() {});
  }

  @override
  void dispose() {
    super.dispose();

    widget.player.release();
  }
}
