import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_network/data_model.dart';
import 'package:http/http.dart' as http;

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
          _doPostBtn,
          _doPostJson,
          Text('result: $resultShow')
        ],
      ),
    );
  }

  /// send get request
  Future<void> _doGetRequest() async {
    var uri =
        Uri.parse('https://api.devio.org/uapi/test/test?requestPrams=ChatGPT2');
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
