import 'package:flutter/material.dart';

import 'favorite_animation_icon.dart';

class FavoriteGesture extends StatefulWidget {
  static const double defaultSize = 100;
  final Widget child;
  final double size;

  const FavoriteGesture({required this.child, this.size = defaultSize, Key? key}) : super(key: key);

  @override
  State<FavoriteGesture> createState() => _FavoriteGestureState();
}

class _FavoriteGestureState extends State<FavoriteGesture> {
  final GlobalKey _key = GlobalKey();

  // 保存当前需要展示的icon
  List<Offset> iconOffsets = [];

  // temp表示最近的一次双击坐标
  Offset temp = Offset.zero;

  Offset _globalToLocal(Offset global) {
    RenderBox renderBox = _key.currentContext?.findRenderObject() as RenderBox;
    return renderBox.globalToLocal(global);
  }

  @override
  Widget build(BuildContext context) {
    var iconStack = Stack(
        children: iconOffsets
            .map((e) => FavoriteAnimationIcon(
                  key: Key(e.toString()),
                  size: widget.size,
                  position: e,
                  onAnimationComplete: () {
                    iconOffsets.remove(e);
                  },
                ))
            .toList());

    return GestureDetector(
        key: _key,
        child: Stack(children: [
          widget.child,
          iconStack,
        ]),
        onDoubleTapDown: (details) {
          temp = _globalToLocal(details.globalPosition);
        },
        onDoubleTap: () {
          // 添加坐标到集合中，触发一次重绘制。根据坐标集合来在不同的坐标上渲染出icon
          setState(() => iconOffsets.add(temp));
        });
  }
}
