import 'package:flutter/services.dart';

class ChannelUtil {
  static MethodChannel _methodChannel = MethodChannel('CommonChannel');

  static closeCamera() {
    _methodChannel.invokeMethod("closeCamera");
  }
}