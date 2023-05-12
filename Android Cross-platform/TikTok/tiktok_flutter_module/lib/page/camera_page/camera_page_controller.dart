import 'package:camera/camera.dart';
import 'package:get/get.dart';

import '../../channel_util.dart';

class CameraPageController extends GetxController {
  CameraController? _cameraController;

  late List<CameraDescription> _cameras;

  var _cameraControllerObs = Rx<CameraController?>(null);

  CameraController? get cameraController => _cameraControllerObs.value;

  var _cameraIndex = 0.obs;

  int get cameraIndex => _cameraIndex.value;

  set cameraIndex(int index) => _cameraIndex.value = index;

  var _flash = false.obs;

  bool get flash => _flash.value;

  set flash(bool enable) => _flash.value = enable;

  var _recording = false.obs;

  bool get recording => _recording.value;

  set recording(bool recording) => _recording.value = recording;

  void onCloseTap() {
    ChannelUtil.closeCamera();
  }

  void init() {
    // 1、等待camera初始化
    availableCameras().then((value) {
      print('MOOC: available camera : $value');
      _cameras = value;
      _initCameraController();
    });
  }

  void onSwitchCamera() {
    if (_cameras.length > 1) {
      cameraIndex = cameraIndex == 0 ? 1 : 0;
      _initCameraController();
    }
  }

  void _initCameraController() {
    print('MOOC: _initCameraController, cameraIndex: $cameraIndex');
    // 2、选择具体camera来创建camera controller
    _cameraController = CameraController(_cameras[cameraIndex], ResolutionPreset.max);
    // 3、等待cameraController初始化
    _cameraController?.initialize().then((_) {
      print('MOOC: _initCameraController, refresh controller: $cameraIndex');

      _cameraControllerObs.value = _cameraController;
    }).catchError((Object e) {
      if (e is CameraException) {
        switch (e.code) {
          case 'CameraAccessDenied':
            print('MOOC: User denied camera access.');
            break;
          default:
            print('MOOC: Handle other errors.');
            break;
        }
      }
    });
  }

  Future<void> takePhotoAndUpload() async {
    if (recording) {
      recording = false;
      XFile? file = await _cameraController?.stopVideoRecording();
      print('MOOC: take video : ${file?.path}');
    } else {
      recording = true;
      _cameraController?.startVideoRecording();
    }
    // todo: 上传服务端
  }

  void onSwitchFlash() {
    _cameraController?.setFlashMode(flash ? FlashMode.off : FlashMode.always);
    flash = !flash;
  }
}
