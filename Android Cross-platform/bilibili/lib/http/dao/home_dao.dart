import 'package:flutter_bili_app/http/request/home_request.dart';
import 'package:hi_net/hi_net.dart';
import 'package:flutter_bili_app/model/home_mo.dart';

class HomeDao {
  //https://api.devio.org/uapi/fa/home/推荐?pageIndex=1&pageSize=10
  static get(String categoryName, {int pageIndex = 1, int pageSize = 1}) async {
    HomeRequest request = HomeRequest();
    request.pathParams = categoryName;
    request.add("pageIndex", pageIndex).add("pageSize", pageSize);
    var result = await HiNet.getInstance().fire(request);
    print(result);
    return HomeMo.fromJson(result['data']);
  }
}
