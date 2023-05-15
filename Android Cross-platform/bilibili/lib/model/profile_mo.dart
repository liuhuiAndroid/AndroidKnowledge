import 'package:bilibili/model/home_mo.dart';

///个人中心Mo
class ProfileMo {
  late String name;
  late String face;
  late int fans;
  late int favorite;
  late int like;
  late int coin;
  late int browsing;
  late List<BannerMo> bannerList;
  late List<Course> courseList;
  late List<Benefit> benefitList;

  ProfileMo.fromJson(Map<String, dynamic> json) {
    name = json['name'];
    face = json['face'];
    fans = json['fans'];
    favorite = json['favorite'];
    like = json['like'];
    coin = json['coin'];
    browsing = json['browsing'];
    if (json['bannerList'] != null) {
      bannerList = List<BannerMo>.empty(growable: true);
      json['bannerList'].forEach((v) {
        bannerList.add(BannerMo.fromJson(v));
      });
    }
    if (json['courseList'] != null) {
      courseList = List<Course>.empty(growable: true);
      json['courseList'].forEach((v) {
        courseList.add(Course.fromJson(v));
      });
    }
    if (json['benefitList'] != null) {
      benefitList = List<Benefit>.empty(growable: true);
      json['benefitList'].forEach((v) {
        benefitList.add(Benefit.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['name'] = name;
    data['face'] = face;
    data['fans'] = fans;
    data['favorite'] = favorite;
    data['like'] = like;
    data['coin'] = coin;
    data['browsing'] = browsing;
    data['bannerList'] = bannerList.map((v) => v.toJson()).toList();
    data['courseList'] = courseList.map((v) => v.toJson()).toList();
    data['benefitList'] = benefitList.map((v) => v.toJson()).toList();
    return data;
  }
}

class Course {
  late String name;
  late String cover;
  late String url;
  late int group;

  Course.fromJson(Map<String, dynamic> json) {
    name = json['name'];
    cover = json['cover'];
    url = json['url'];
    group = json['group'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['name'] = name;
    data['cover'] = cover;
    data['url'] = url;
    data['group'] = group;
    return data;
  }
}

class Benefit {
  late String name;
  late String url;

  Benefit.fromJson(Map<String, dynamic> json) {
    name = json['name'];
    url = json['url'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['name'] = name;
    data['url'] = url;
    return data;
  }
}
