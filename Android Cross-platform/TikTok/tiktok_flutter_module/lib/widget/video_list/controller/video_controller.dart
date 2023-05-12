import 'dart:convert';
import 'dart:math';

import 'package:shared_preferences/shared_preferences.dart';

import '../model/video_model.dart';

abstract class VideoController {
  List<VideoModel>? dataList;

  /// 针对不同的类型视频，采用不同的key存储
  String get spKey;
  /// 返回不同类型的视频内容
  String get videoData;

  int get count => Random().nextInt(1000);

  Future<void> init() async {
    // 首先判断一级缓存——即内存中是否有数据
    print('MOOC- init video controller');
    if (dataList == null) {
      print('MOOC- model is null');
      // 如果没有，则从二级/三级缓存查找
      dataList = await fetchVideoData();
    }
  }

  // 缺点：
  // 1、需要针对json的每个字段创建get方法，一旦字段多了会非常繁琐
  // 2、需要保证map的字段和json的字段完全一致， 容易出错

  // 从服务端拉取视频Json字符串类型表示的视频数据
  Future<List<VideoModel>> fetchVideoData() async {
    var sp = await SharedPreferences.getInstance();
    var modelStr = sp.getString(spKey);
    if (modelStr != null && modelStr.isNotEmpty) {
      // 二级缓存中找到数据，直接使用
      print('MOOC- 2/use sp data');

      var list = jsonDecode(modelStr) as List<dynamic>;
      // jsonDecode获取到的是“List<Map>”，需要转换成List<VideoModel>
      // List<Map> => List<VideoModel>

      return list.map((e) => VideoModel.fromJson(e)).toList();
    } else {
      // 二级缓存未找到数据，走三级缓存
      var list = jsonDecode(videoData) as List<dynamic>;
      var sp = await SharedPreferences.getInstance();
      sp.setString(spKey, videoData);
      print('MOOC- 3/fetch data from server');

      return list.map((e) => VideoModel.fromJson(e)).toList();
    }
  }
}
