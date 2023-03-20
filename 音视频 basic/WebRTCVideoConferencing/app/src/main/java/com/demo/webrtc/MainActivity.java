package com.demo.webrtc;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IPeerConnection {

    // 本地预览
    TextureView localTextureView;
    // 显示远端的用户
    List<Surface> surfaceList = new ArrayList<>();
    EncoderPlayerLiveH264 encoderPlayerLiveH264;
    SocketLive socketLive;
    // 解码，一个人对应一个解码器
    List<DecodecPlayerLiveH264> decoderList = new ArrayList<>();

    // 已经使用过了索引
    private int surfaceIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
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

    private void initView() {
        SurfaceView surfaceView = findViewById(R.id.removeSurfaceView);
        SurfaceView surfaceView1 = findViewById(R.id.removeSurfaceView1);
        SurfaceView surfaceView2 = findViewById(R.id.removeSurfaceView2);
        localTextureView = findViewById(R.id.localTextureView);
        ArrayList<SurfaceView> surfaceViews = new ArrayList<>();
        surfaceViews.add(surfaceView);
        surfaceViews.add(surfaceView1);
        surfaceViews.add(surfaceView2);
        for (SurfaceView view : surfaceViews) {
            view.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    surfaceList.add(holder.getSurface());
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {

                }
            });
        }
        socketLive = new SocketLive(this);
    }

    public void connectRoom(View view) {
        socketLive.start(this);
        encoderPlayerLiveH264 = new EncoderPlayerLiveH264(this, localTextureView);
        encoderPlayerLiveH264.startCapture(socketLive);
    }

    // 别人用户  会简历长连接 别人 跟你简历
    @Override
    public void newConnection(String remoteIp) {
//会议室有一个人进来 是
        DecodecPlayerLiveH264 decodecPlayerLiveH264 = new DecodecPlayerLiveH264();
        decodecPlayerLiveH264.initDecoder(remoteIp, surfaceList.get(surfaceIndex++));
        decoderList.add(decodecPlayerLiveH264);
    }

    // 回调是远端的数据发送到了我的
    @Override
    public void remoteReceiveData(String remoteIp, byte[] data) {
        //  remoteIp      到 decoderList   里面去寻找 DecodecPlayerLiveH264
        DecodecPlayerLiveH264 decodecPlayerLiveH264 = findDecodec(remoteIp);
        if (decodecPlayerLiveH264 != null) {
            decodecPlayerLiveH264.sendData(data);
        }
    }

    private DecodecPlayerLiveH264 findDecodec(String remoteIp) {
        for (DecodecPlayerLiveH264 decodecPlayerLiveH264 : decoderList) {
            if (decodecPlayerLiveH264.getRemoteIp().equals(remoteIp)) {
                return decodecPlayerLiveH264;
            }
        }
        return null;
    }

}
