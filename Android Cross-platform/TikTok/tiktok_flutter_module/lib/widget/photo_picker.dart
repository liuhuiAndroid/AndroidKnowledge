import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:tiktok_flutter_module/widget/t_image.dart';

import '../main.dart';

/// 职责：
/// 1、展示完整的图片；
/// 2、提供切换图片的功能；
class PhotoPickerPage extends StatelessWidget {
  final String fileUrl;
  final double? height;
  final double? width;

  PhotoPickerPage(this.fileUrl, {this.height, this.width});

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: [
        Align(
            alignment: Alignment.center,
            child: GestureDetector(
              child: TImage(fileUrl, height: height, width: width),
              onTap: () {
                router.popRoute();
              },
            )),
        Align(
            alignment: Alignment.bottomCenter,
            child: Padding(
                padding: EdgeInsets.only(bottom: 85),
                child: GestureDetector(
                  child: Container(
                      alignment: Alignment.center,
                      color: Color(0xff2626262),
                      width: 190,
                      height: 36,
                      child: Text('更换背景',
                          style: TextStyle(color: Colors.white70, fontSize: 14, decoration: TextDecoration.none))),
                  onTap: () async {
                    var pickedFile = await ImagePicker().pickImage(source: ImageSource.gallery);
                    var path = pickedFile?.path;
                    if (path != null) {
                      router.popRoute(params: path);
                    }
                  },
                )))
      ],
    );
  }
}
