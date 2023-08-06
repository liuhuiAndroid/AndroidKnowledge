import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_network/data_model.dart';
import 'package:http/http.dart' as http;
import 'package:xml2json/xml2json.dart';

/// stf
/// flutter pub add http
class HttpICan extends StatefulWidget {
  const HttpICan({Key? key}) : super(key: key);

  @override
  State<HttpICan> createState() => _HttpICanState();
}

class _HttpICanState extends State<HttpICan> {
  var resultShow = '';

  get _doGetBtn => ElevatedButton(
      onPressed: _doGetRequest, child: const Text('send get request'));

  get _doGetXmlBtn => ElevatedButton(
      onPressed: _doGetXmlRequest, child: const Text('send get request'));

  get _doPostBtn => ElevatedButton(
      onPressed: _doPostRequest, child: const Text('send post request'));

  get _doPostJson => ElevatedButton(
      onPressed: _doPostJsonRequest,
      child: const Text('send post json request'));

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('http'),
      ),
      body: Column(
        children: [
          _doGetBtn,
          _doGetXmlBtn,
          _doPostBtn,
          _doPostJson,
          Text('result: $resultShow')
        ],
      ),
    );
  }

  /// send get request
  Future<void> _doGetRequest() async {
    var uri = Uri.parse(
        'https://www.maigel.com/servlet/async.ashx?operationType=17&publisher=zhanhui210609');

    var response = await http.get(uri);
    if (response.statusCode == 200) {
      setState(() {
        resultShow = response.body;
      });
    } else {
      setState(() {
        resultShow = "请求失败：code=${response.statusCode}, body=${response.body}";
      });
    }
  }

  /// send get request
  Future<void> _doGetXmlRequest() async {
    var uri = Uri.parse('http://live.maigel.com/videos/103482.xml');

    var response = await http.get(uri);
    if (response.statusCode == 200) {
      final myTransformer = Xml2Json();
      String result = response.body;
      myTransformer.parse(result);
      var json1 = myTransformer.toBadgerfish();
      debugPrint('json1$json1');

      myTransformer.parse(result);
      var json2 = myTransformer.toGData();
      Utf8Decoder utf8decoder = const Utf8Decoder();
      json2 = utf8decoder.convert(json2.codeUnits);
      debugPrint('json2$json2');

      myTransformer.parse(result);
      var json3 = myTransformer.toParker();
      debugPrint('json3$json3');

      myTransformer.parse(result);
      var json4 = myTransformer.toParkerWithAttrs();
      debugPrint('json4$json4');

      setState(() {
        resultShow = jsonEncode(json2);
      });
    } else {
      setState(() {
        resultShow = "请求失败：code=${response.statusCode}, body=${response.body}";
      });
    }
  }

  /// send post request
  Future<void> _doPostRequest() async {
    var uri = Uri.parse('https://api.devio.org/uapi/test/test');
    var params = {"requestPrams": "doPost:ChatGPT"};
    var response = await http.post(uri, body: params);
    if (response.statusCode == 200) {
      setState(() {
        resultShow = response.body;
      });
    } else {
      setState(() {
        resultShow = "请求失败：code=${response.statusCode}, body=${response.body}";
      });
    }
  }

  /// send post request
  Future<void> _doPostJsonRequest() async {
    var uri = Uri.parse('https://api.devio.org/uapi/test/testJson');
    var params = {"name": "doPostJson:ChatGPT"};
    var response = await http.post(uri,
        headers: {"content-type": "application/json"},
        body: jsonEncode(params));
    if (response.statusCode == 200) {
      var map = jsonDecode(response.body);
      // setState(() {
      //   resultShow = map['msg'];
      // });
      var dataModel = DataModel.fromJson(map);
      setState(() {
        resultShow = dataModel.data?.jsonParams?.name ?? "";
      });
    } else {
      setState(() {
        resultShow = "请求失败：code=${response.statusCode}, body=${response.body}";
      });
    }
  }
}
