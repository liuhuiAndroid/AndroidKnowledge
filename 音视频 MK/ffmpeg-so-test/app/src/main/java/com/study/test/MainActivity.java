package com.study.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // 导入要用的C++库
    static {
        System.loadLibrary("learn-ffmpeg");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.text_view)).setText(_test() + "\n" + stringFromJNI());
    }

    // native修饰的是由C/C++提供的方法
    public native String stringFromJNI();

    public native String stringFromJNI2();

    public native String _test();
}