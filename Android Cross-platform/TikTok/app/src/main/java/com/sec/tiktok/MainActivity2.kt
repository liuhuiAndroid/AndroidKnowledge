package com.sec.tiktok


import android.util.Log
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class MainActivity2 : FlutterActivity() {

    companion object {
        const val METHOD_GET_FLUTTER_INFO = "getFlutterInfo"
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        // 1、初始化MethodChannel，并传入名称，名称需要和Dart侧完全一致
        val channel = MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            "methodChannel"
        ) // 2、注册MethodChannel处理器
        channel.setMethodCallHandler { methodCall: MethodCall, result: MethodChannel.Result ->
            if (methodCall.method == METHOD_GET_FLUTTER_INFO) {
                val name = methodCall.argument<String>("name")
                val version = methodCall.argument<String>("version")
                val language = methodCall.argument<String>("language")
                val androidApi = methodCall.argument<Int>("android_api")
                Log.d(
                    "flutter",
                    "name= $name; version= $version; language= $language; androidApi= $androidApi"
                )
                result.success("ACK from android")
            }
        }
    }
}
