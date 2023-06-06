/// 在线转换工具：https://www.devio.org/io/tools/json-to-dart/
/// 字段不能为私有
/// 普通构造函数
/// 声明为 XXX.fromJson 的命名构造函数
/// 声明为 Map<String, dynamic> toJson 成员函数
class DataModel {
  int? code;
  Data? data;
  String? msg;

  DataModel({this.code, this.data, this.msg});

  DataModel.fromJson(Map<String, dynamic> json) {
    code = json['code'];
    data = json['data'] != null ? Data.fromJson(json['data']) : null;
    msg = json['msg'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['code'] = code;
    data['data'] = data;
    data['msg'] = msg;
    return data;
  }
}

class Data {
  int? code;
  String? method;
  JsonParams? jsonParams;
  String? requestPrams;

  Data({this.code, this.method, this.jsonParams, this.requestPrams});

  Data.fromJson(Map<String, dynamic> json) {
    code = json['code'];
    method = json['method'];
    requestPrams = json['requestPrams'];
    jsonParams = json['jsonParams'] != null
        ? JsonParams.fromJson(json['jsonParams'])
        : null;
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['code'] = code;
    data['method'] = method;
    data['jsonParams'] = jsonParams;
    data['requestPrams'] = requestPrams;
    return data;
  }
}

class JsonParams {
  String? name;

  JsonParams(this.name);

  JsonParams.fromJson(Map<String, dynamic> json) {
    name = json['name'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['name'] = name;
    return data;
  }
}
