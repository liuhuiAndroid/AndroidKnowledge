package com.demo.x264live.cameraxpush;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.x264live.R;
import com.demo.x264live.live.LivePusher;

public class MainActivity extends AppCompatActivity {

    // H264码流
    private LivePusher livePusher;
    private TextureView textureView;
    // 申请地址：https://link.bilibili.com/p/center/index#/my-room/start-live
    private String url = "rtmp://live-push.bilivideo.com/live-bvc/?streamname=live_103156760_37204143&key=2a0a6d0d9ba98139c143cd2a9bc1f0b3&schedule=rtmp&pflag=1";
    private VideoChanel videoChanel;
    private AudioChannel audioChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camerax);
        checkPermission();
        textureView = findViewById(R.id.textureView);
        checkPermission();
        livePusher = new LivePusher(this, 800, 480, 800_000, 10, Camera.CameraInfo.CAMERA_FACING_BACK);
        livePusher.startLive(url);
        videoChanel = new VideoChanel(this, textureView, livePusher);
        audioChannel = new AudioChannel(44100, 2, livePusher);
    }

    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
            }, 1);
        }
        return false;
    }

    public void switchCamera(View view) {
        livePusher.switchCamera();
    }

    public void startLive(View view) {
        videoChanel.startLive();
        audioChannel.start();
    }

    public void stopLive(View view) {
        livePusher.stopLive();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        livePusher.native_release();
    }
}
