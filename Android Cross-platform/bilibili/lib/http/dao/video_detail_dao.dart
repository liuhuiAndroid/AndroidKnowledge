import 'package:bilibili/http/request/video_detail_request.dart';
import 'package:bilibili/model/video_detail_mo.dart';
import 'package:hi_net/hi_net.dart';

///详情页Dao
class VideoDetailDao {
  // https://api.devio.org/uapi/fa/detail/BV19C4y1s7Ka
  static get(String vid) async {
    VideoDetailRequest request = VideoDetailRequest();
    // 添加查询参数
    request.pathParams = vid;
    var result = await HiNet.getInstance().fire(request);
    print(result);
    return VideoDetailMo.fromJson(result['data']);
  }
}
