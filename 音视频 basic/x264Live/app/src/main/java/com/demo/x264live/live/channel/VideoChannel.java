package com.demo.x264live.live.channel;

import android.app.Activity;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import com.demo.x264live.live.LivePusher;

public class VideoChannel implements Camera.PreviewCallback, CameraHelper.OnChangedSizeListener {

    private LivePusher mLivePusher;
    private CameraHelper cameraHelper;
    private int mBitrate;
    private int mFps;
    private boolean isLiving;

    public VideoChannel(LivePusher livePusher, Activity activity, int width, int height, int bitrate, int fps, int cameraId) {
        mLivePusher = livePusher;
        mBitrate = bitrate;
        mFps = fps;
        cameraHelper = new CameraHelper(activity, cameraId, width, height);
        //1、让camerahelper的
        cameraHelper.setPreviewCallback(this);
        //2、回调 真实的摄像头数据宽、高
        cameraHelper.setOnChangedSizeListener(this);
    }

    public void setPreviewDisplay(SurfaceHolder surfaceHolder) {
        cameraHelper.setPreviewDisplay(surfaceHolder);
    }

    /**
     * 得到nv21数据 已经旋转好的  推流  之前 要初始化     初始 MediaFormater     x264
     *  数据   --过来了    yuv
     * @param data
     * @param camera
     */
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (isLiving) {
            mLivePusher.native_pushVideo(data);
        }
    }

    public void switchCamera() {
        cameraHelper.switchCamera();
    }
    /**
     * 真实摄像头数据的宽、高
     * @param w
     * @param h
     */
    @Override
    public void onChanged(int w, int h) {
        //初始化编码器
        mLivePusher.native_setVideoEncInfo(h, w, mFps, mBitrate);
    }

    public void startLive() {
        isLiving = true;
    }

    public void stopLive() {
        isLiving = false;
    }
}
