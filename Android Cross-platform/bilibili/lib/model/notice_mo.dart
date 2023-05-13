import 'home_mo.dart';

class NoticeMo {
  late int total;
  late List<BannerMo> list;

  NoticeMo.fromJson(Map<String, dynamic> json) {
    total = json['total'];
    if (json['list'] != null) {
      list = new List<BannerMo>.empty(growable: true);
      json['list'].forEach((v) {
        list.add(new BannerMo.fromJson(v));
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
