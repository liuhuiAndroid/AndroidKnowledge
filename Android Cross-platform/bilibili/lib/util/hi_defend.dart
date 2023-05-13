import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';

class HiDefend {
  run(Widget app) {
    //框架异常
    FlutterError.onError = (FlutterErrorDetails details) async {
      //线上环境，走上报逻辑
      if (kReleaseMode) {
        Zone.current.handleUncaughtError(details.exception, details.stack!);
      } else {
        //开发期间，走Console抛出
        FlutterError.dumpErrorToConsole(details);
      }
    };
    runZonedGuarded(() {
      runApp(app);
    }, (e, s) => _reportError(e, s));
  }

  ///通过接口上报异常
  _reportError(Object error, StackTrace s) {
    print('kReleaseMode:$kReleaseMode');
    print('catch error:$error');
  }
}
