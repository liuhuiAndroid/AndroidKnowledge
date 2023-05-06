import 'package:flutter_bili_app/http/request/base_request.dart';
import 'package:flutter_bili_app/http/request/cancel_favorite_request.dart';
import 'package:flutter_bili_app/http/request/favorite_list_request.dart';
import 'package:flutter_bili_app/http/request/favorite_request.dart';

// import 'package:flutter_bili_app/http/request/base_request.dart';
import 'package:flutter_bili_app/model/ranking_mo.dart';
import 'package:hi_net/hi_net.dart';

class FavoriteDao {
  // https://api.devio.org/uapi/fa/favorite/BV1qt411j7fV
  static favorite(String vid, bool favorite) async {
    BaseRequest request =
        favorite ? FavoriteRequest() : CancelFavoriteRequest();
    request.pathParams = vid;
    var result = await HiNet.getInstance().fire(request);
    print(result);
    return result;
  }

  //https://api.devio.org/uapi/fa/favorites?pageIndex=1&pageSize=10
  static favoriteList({int pageIndex = 1, int pageSize = 10}) async {
    FavoriteListRequest request = FavoriteListRequest();
    request.add("pageIndex", pageIndex).add("pageSize", pageSize);
    var result = await HiNet.getInstance().fire(request);
    print(result);
    return RankingMo.fromJson(result['data']);
  }
}
