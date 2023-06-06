// TODO Implement this library.
/// stf
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_network/data_model.dart';
import 'package:http/http.dart' as http;

class FutureICan extends StatefulWidget {
  const FutureICan({Key? key}) : super(key: key);

  @override
  State<FutureICan> createState() => _FutureICanState();
}

class _FutureICanState extends State<FutureICan> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Future")),
      body: FutureBuilder<DataModel>(
        future: fetchGet(),
        builder: (BuildContext context, AsyncSnapshot<DataModel> snapshot) {
          switch (snapshot.connectionState) {
            case ConnectionState.none:
              return const Text('state:none');
            case ConnectionState.waiting:
              return const Center(child: CircularProgressIndicator());
            case ConnectionState.active:
              return const Text('state:active');
            case ConnectionState.done:
              if (snapshot.hasError) {
                return Text('${snapshot.error}',
                    style: const TextStyle(color: Colors.red));
              } else {
                return Column(
                  children: [
                    Text('code:${snapshot.data?.code}'),
                    Text('requestPrams:${snapshot.data?.data?.requestPrams}')
                  ],
                );
              }
          }
        },
      ),
    );
  }

  Future<DataModel> fetchGet() async {
    var uri =
        Uri.parse('https://api.devio.org/uapi/test/test?requestPrams=ChatGPT');
    var response = await http.get(uri);
    // 解决中文可能乱码问题
    Utf8Decoder utf8decoder = const Utf8Decoder();
    var result = json.decode(utf8decoder.convert(response.bodyBytes));
    return DataModel.fromJson(result);
  }
}
