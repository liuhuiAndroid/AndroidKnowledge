import 'dart:io';

import 'package:flutter/material.dart';

class TImage extends StatelessWidget {
  final String url;
  final fit;
  final shape;
  double? height;
  double? width;
  double? radius;

  static const double borderWidth = 4;

  TImage(this.url, {this.height, this.width, this.fit = BoxFit.cover, this.shape = Shape.NORMAL, this.radius});

  @override
  Widget build(BuildContext context) {
    switch (shape) {
      case Shape.NORMAL:
        if (url.contains('asset')) {
          return Image.asset(url, width: width, height: height, fit: fit);
        } else {
          return Image.file(File(url), width: width, height: height, fit: fit);
        }
      case Shape.CIRCLE:
        var image;
        if (url.contains('asset')) {
          image = AssetImage(url);
        } else {
          image = FileImage(File(url));
        }
        return Container(
          width: radius != null ? radius! * 2 + borderWidth * 2 : null,
          height: radius != null ? radius! * 2 + borderWidth * 2 : null,
          decoration: BoxDecoration(
              image: DecorationImage(image: image, fit: BoxFit.cover),
              borderRadius: BorderRadius.all(Radius.circular(50)),
              border: Border.all(color: Color(0xfffefdfd), width: borderWidth)),
        );
      default:
        if (url.contains('asset')) {
          return Image.asset(url, width: width, height: height, fit: fit);
        } else {
          return Image.file(File(url), width: width, height: height, fit: fit);
        }
    }
  }
}

enum Shape { NORMAL, CIRCLE }
