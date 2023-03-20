package com.demo.x264live;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;

import com.demo.x264live.live.LivePusher;

// ffplay -stats -f h264 codec.h264
public class MainActivity extends AppCompatActivity {

    private LivePusher livePusher;

    // 申请地址：https://link.bilibili.com/p/center/index#/my-room/start-live
    // private String url = "rtmp://live-push.bilivideo.com/live-bvc/?streamname=live_103156760_37204143&key=8487d2e88a32ae9ebf34f3a903501151&schedule=rtmp&pflag=1";
    // private String url = "rtmp://101.132.108.8:1935/live/mylive";
    private String url = "rtmp://live-push.bilivideo.com/live-bvc/?streamname=live_103156760_37204143&key=ca45c802baa38c7af7c071734d5c1386&schedule=rtmp&pflag=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        checkPermission();
        livePusher = new LivePusher(this, 800, 480, 800_000, 10, Camera.CameraInfo.CAMERA_FACING_BACK);
        //  设置摄像头预览的界面
        livePusher.setPreviewDisplay(surfaceView.getHolder());
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

    public void switchCamera(View view) {
        livePusher.switchCamera();
    }

    public void startLive(View view) {
        livePusher.startLive(url);
    }

    public void stopLive(View view) {
        livePusher.stopLive();
    }

}
