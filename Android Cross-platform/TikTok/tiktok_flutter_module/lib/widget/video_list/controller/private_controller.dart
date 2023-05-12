import 'package:tiktok_flutter_module/widget/video_list/controller/video_controller.dart';
import 'package:tiktok_flutter_module/widget/video_list/server_data.dart';

class PrivateController extends VideoController {
  @override
  String get spKey => 'private_video';

  @override
  String get videoData => ServerData.fetchPrivateFromServer();
}