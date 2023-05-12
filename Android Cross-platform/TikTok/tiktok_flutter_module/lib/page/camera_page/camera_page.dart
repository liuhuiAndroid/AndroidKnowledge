import 'package:camera/camera.dart';
import 'package:flutter/material.dart';
import 'package:tiktok_flutter_module/page/camera_page/camera_page_controller.dart';
import 'package:tiktok_flutter_module/widget/t_image.dart';
import 'package:get/get.dart';
import 'package:image_picker/image_picker.dart';

import '../../gen/assets.gen.dart';

class CameraPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _CameraPageStage();
}

class _CameraPageStage extends State<CameraPage> {
  late CameraPageController _controller;

  @override
  void initState() {
    super.initState();
    _controller = CameraPageController();

    _controller.init();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        home: Stack(children: [
      // 相机视频预览区域
      Container(
          width: double.infinity,
          height: double.infinity,
          child: Obx(() {
            return _controller.cameraController == null ||
                    !_controller.cameraController!.value.isInitialized
                ? Container(color: Colors.black)
                : CameraPreview(_controller.cameraController!);
          })),
      Padding(
        padding: EdgeInsets.only(top: 38, left: 19),
        child: GestureDetector(
          child: TImage(Assets.image.close.path, height: 18, width: 18),
          onTap: () {
            _controller.onCloseTap();
          },
        ),
      ),
      Align(
        alignment: Alignment.topRight,
        child: Padding(
            padding: EdgeInsets.only(top: 35, right: 14),
            child: Column(
              children: [
                _buildIcon(Assets.image.rotate.path, '翻转', () => _controller.onSwitchCamera()),
                SizedBox(height: 16),
                _buildIcon(
                    Assets.image.clock.path,
                    '倒计时',
                    () => Future.delayed(
                        Duration(seconds: 3), () => _controller.takePhotoAndUpload())),
                SizedBox(height: 16),
                Obx(() => _buildIcon(
                    _controller.flash ? Assets.image.flashOn.path : Assets.image.flashOff.path,
                    '闪光灯',
                    () => _controller.onSwitchFlash())),
              ],
            )),
      ),
      // 相册
      Align(
        alignment: Alignment.bottomLeft,
        child: Padding(
          padding: EdgeInsets.only(left: 62, bottom: 110),
          child: GestureDetector(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                mainAxisAlignment: MainAxisAlignment.end,
                children: [
                  TImage(Assets.image.gallery.path, height: 40, width: 40),
                  SizedBox(height: 10),
                  Text(
                    '相册',
                    style: TextStyle(
                        color: Colors.white, fontSize: 12, decoration: TextDecoration.none),
                  )
                ],
              ),
              onTap: () async {
                var pickedFile = await ImagePicker().pickVideo(source: ImageSource.gallery);
                var path = pickedFile?.path;
                if (path != null) {
                  print('MOOC= upload picture: $path');
                }
              }),
        ),
      ),
      Align(
        alignment: Alignment.bottomCenter,
        child: Padding(
          padding: EdgeInsets.only(bottom: 115),
          child: GestureDetector(
              onTap: () => _controller.takePhotoAndUpload(),
              child: Obx(() => Container(
                    height: 75,
                    width: 75,
                    child: Padding(
                      padding: EdgeInsets.symmetric(vertical: 17, horizontal: 22),
                      child: TImage(Assets.image.flashOn.path, height: 33, width: 33),
                    ),
                    decoration: BoxDecoration(
                      color: _controller.recording ? Colors.grey : Color(0xffff2c54),
                      borderRadius: BorderRadius.all(Radius.circular(50)),
                      border: Border.all(width: 4, color: Colors.white),
                    ),
                  ))),
        ),
      )
    ]));
  }

  /// 构建控制Button，图片+文本，垂直结构
  Widget _buildIcon(String iconPath, String title, GestureTapCallback? onTap) {
    return GestureDetector(
        child: Column(children: [
          TImage(iconPath, width: 25, height: 25),
          Text(title,
              style: TextStyle(color: Colors.white, fontSize: 10, decoration: TextDecoration.none))
        ]),
        onTap: onTap);
  }
}
