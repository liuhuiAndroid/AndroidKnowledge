package com.demo.bilibili;

import android.media.projection.MediaProjection;
import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 传输层
 */
public class ScreenLive extends Thread {

    static {
        System.loadLibrary("bilibili");
    }

    private String url;
    // 正在直播
    private boolean isLiving;
    // 录屏工具类
    private MediaProjection mediaProjection;
    private LinkedBlockingQueue<RTMPPackage> queue = new LinkedBlockingQueue<>();

    public void addPackage(RTMPPackage rtmpPackage) {
        if (!isLiving) {
            return;
        }
        queue.add(rtmpPackage);
    }

    // 开启推送模式
    public void startLive(String url, MediaProjection mediaProjection) {
        this.url = url;
        this.mediaProjection = mediaProjection;
        start();
    }

    @Override
    public void run() {
        if (!connect(url)) {
            Log.i("ScreenLive", "run: ----------->推送失败");
            return;
        }
        VideoCodec videoCodec = new VideoCodec(this);
        videoCodec.startLive(mediaProjection);
        AudioCodec audioCodec = new AudioCodec(this);
        audioCodec.startLive();
        isLiving = true;
        while (isLiving) {
            try {
                RTMPPackage rtmpPackage = queue.take();
                if (rtmpPackage.getBuffer() != null && rtmpPackage.getBuffer().length != 0) {
                    Log.i("ScreenLive", "run: ----------->推送 " + rtmpPackage.getBuffer().length);
                    sendData(rtmpPackage.getBuffer(), rtmpPackage.getBuffer().length, rtmpPackage.getTms(), rtmpPackage.getType());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private native boolean sendData(byte[] data, int len, long tms, int type);

    private native boolean connect(String url);

}
