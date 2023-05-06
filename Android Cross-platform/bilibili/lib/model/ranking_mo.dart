import 'package:flutter_bili_app/model/video_model.dart';

///解放生产力：在线json转dart https://www.devio.org/io/tools/json-to-dart/
class RankingMo {
  late int total;
  late List<VideoModel> list;

  RankingMo.fromJson(Map<String, dynamic> json) {
    total = json['total'];
    if (json['list'] != null) {
      list = new List<VideoModel>.empty(growable: true);
      json['list'].forEach((v) {
        list.add(new VideoModel.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['total'] = this.total;
    data['list'] = this.list.map((v) => v.toJson()).toList();
    return data;
  }
}
