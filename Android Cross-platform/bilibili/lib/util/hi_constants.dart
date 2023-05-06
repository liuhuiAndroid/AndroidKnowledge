import 'package:flutter_bili_app/http/dao/login_dao.dart';

class HiConstants {
  static String authTokenK = "auth-token";
  static String authTokenV =
      "请更新最新的token-@https://coding.imooc.com/learn/questiondetail/y0K5g683G4zXe2QN.html";
  static String courseFlagK = "course-flag";
  static String courseFlagV = "fa";
  static const theme = "hi_theme";

  static headers() {
    ///设置请求头校验，注意留意：Console的log输出：flutter: received:
    Map<String, dynamic> header = {
      HiConstants.authTokenK: HiConstants.authTokenV,
      HiConstants.courseFlagK: HiConstants.courseFlagV
    };
    header[LoginDao.BOARDING_PASS] = LoginDao.getBoardingPass();
    return header;
  }
}
