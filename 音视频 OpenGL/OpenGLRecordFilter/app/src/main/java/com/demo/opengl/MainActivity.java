package com.demo.opengl;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.opengl.widget.RecordButton;

/**
 * OpenGL录制滤镜视频 FBO EGL
 *
 * FBO帧缓冲对象，它的主要作用一般就是用作离屏渲染，
 * 例如做Camera相机图像采集进行后期处理时就可能会用到FBO。假如相机出图的是OES纹理，为了方便后期处理，
 * 一般先将OES纹理通过FBO转换成普通的2D纹理，然后再通过FBO等增加美颜等其他各种特效滤镜，
 * 最后将FBO一路流送进编码器进行编码，另外一路渲染到屏幕上进行预览显示。
 *
 * FBO总结起来就是可以暂时将未处理完的帧不直接渲染到屏幕上，而是渲染到离屏Buffer中缓存起来，在恰当的时机再取出来渲染到屏幕。
 */
public class MainActivity extends AppCompatActivity implements RecordButton.OnRecordListener, RadioGroup.OnCheckedChangeListener {

    private CameraView cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraView = findViewById(R.id.cameraView);
        RecordButton btn_record = findViewById(R.id.btn_record);
        btn_record.setOnRecordListener(this);

        //速度
        RadioGroup rgSpeed = findViewById(R.id.rg_speed);
        rgSpeed.setOnCheckedChangeListener(this);
        checkPermission();
    }

    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            }, 1);

        }
        return false;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.btn_extra_slow:
                cameraView.setSpeed(CameraView.Speed.MODE_EXTRA_SLOW);
                break;
            case R.id.btn_slow:
                cameraView.setSpeed(CameraView.Speed.MODE_SLOW);
                break;
            case R.id.btn_normal:
                cameraView.setSpeed(CameraView.Speed.MODE_NORMAL);
                break;
            case R.id.btn_fast:
                cameraView.setSpeed(CameraView.Speed.MODE_FAST);
                break;
            case R.id.btn_extra_fast:
                cameraView.setSpeed(CameraView.Speed.MODE_EXTRA_FAST);
                break;
        }
    }

    @Override
    public void onRecordStart() {
        cameraView.startRecord();
    }

    @Override
    public void onRecordStop() {
        Log.i("tuch", "onRecordStop: ----------------->");
        cameraView.stopRecord();
    }

}
