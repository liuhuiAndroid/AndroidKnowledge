// TODO Implement this library.
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:path_provider/path_provider.dart';

class HiDownloadPage extends StatefulWidget {
  const HiDownloadPage({Key? key}) : super(key: key);

  @override
  State<HiDownloadPage> createState() => _HiDownloadPageState();
}

class _HiDownloadPageState extends State<HiDownloadPage> {
  get _download =>
      ElevatedButton(onPressed: _doDownload, child: const Text('download'));
  File? imageFile;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('download')),
      body: Column(
        children: [_download, if (imageFile != null) Image.file(imageFile!)],
      ),
    );
  }

  void _doDownload() async {
    String? path;
    path = await HiDownload().download(
        downloadUrl: 'https://www.devio.org/img/bg-flutter-new.jpg',
        fileName: 'bg-flutter-new.jpg',
        listener: (int total, int received, bool done) {
          if (done) {
            setState(() {
              imageFile = File(path!);
            });
          }
        });
  }
}

/// flutter pub add path_provider
/// https://pub.flutter-io.cn/packages/path_provider

typedef DownloadListener = void Function(int total, int received, bool done);

class HiDownload {
  int _total = 0, _received = 0;

  Future<String> download(
      {required String downloadUrl,
      required String fileName,
      required DownloadListener listener}) async {
    debugPrint('start download.');
    var uri = Uri.parse(downloadUrl);
    var request = http.Request('GET', uri);
    var response = await http.Client().send(request);
    _total = response.contentLength ?? 0;
    var path = (await getApplicationDocumentsDirectory()).path;
    final file = File('$path/$fileName');
    final writeFile = file.openSync(mode: FileMode.write);
    // 采用 stream 避免内存占用，提升性能
    response.stream.listen((value) {
      writeFile.writeFromSync(value);
      _received += value.length;
      listener(_total, _received, false);
      debugPrint("progress:${_received / _total}");
    }).onDone(() async {
      await writeFile.close();
      listener(_total, _received, true);
      debugPrint("progress:done---");
    });
    return file.absolute.path;
  }
}
