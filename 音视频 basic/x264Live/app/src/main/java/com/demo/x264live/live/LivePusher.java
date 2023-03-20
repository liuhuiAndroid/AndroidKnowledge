package com.demo.x264live.live;

import android.app.Activity;
import android.util.Log;
import android.view.SurfaceHolder;

import com.demo.x264live.FileUtils;
import com.demo.x264live.live.channel.AudioChannel;
import com.demo.x264live.live.channel.VideoChannel;

public class LivePusher {

    static {
        System.loadLibrary("x264live");
    }

    private AudioChannel audioChannel;
    private VideoChannel videoChannel;

    public LivePusher(Activity activity, int width, int height, int bitrate,
                      int fps, int cameraId) {
        native_init();
        videoChannel = new VideoChannel(this,activity, width, height, bitrate, fps, cameraId);
        audioChannel = new AudioChannel();
    }

    public void setPreviewDisplay(SurfaceHolder surfaceHolder) {
        videoChannel.setPreviewDisplay(surfaceHolder);
    }

    public void switchCamera() {
        videoChannel.switchCamera();
    }
    private void onPrepare(boolean isConnect) {
        //通知UI
    }
    public void startLive(String path) {
        native_start(path);
        videoChannel.startLive();
        audioChannel.startLive();
    }

    public void stopLive(){
        videoChannel.stopLive();
        audioChannel.stopLive();
        native_stop();
    }

    public void sendAudio(byte[] buffer, int len) {
        nativeSendAudio(buffer, len);
    }

    // 初始化x264
    public native void native_init();

    // 设置配置参数
    public native void native_setVideoEncInfo(int width, int height, int fps, int bitrate);

    // 开始连接服务器
    public native void native_start(String path);

    // 推流，YUV数据
    public native void native_pushVideo(byte[] data);

    public native void native_stop();

    public native void native_release();

    // jni回调java层的方法  byte[] data    char *data
    private void postData(byte[] data) {
        Log.i("rtmp", "postData: "+data.length);
        FileUtils.writeBytes(data);
        FileUtils.writeContent(data);
    }

    public native int initAudioEnc(int sampleRate, int channels);

    private native void nativeSendAudio(byte[] buffer, int len);
}
