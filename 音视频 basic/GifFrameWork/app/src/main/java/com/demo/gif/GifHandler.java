package com.demo.gif;

import android.graphics.Bitmap;

public class GifHandler {

    // 地址8个字节
    long gifNativePtr;

    static {
        System.loadLibrary("gif");
    }

    public int getWidth() {
        return getWidth(gifNativePtr);
    }

    public int getHeight() {
        return getHeight(gifNativePtr);
    }

    public int updateFrame(Bitmap bitmap) {
        return updateFrame(gifNativePtr, bitmap);
    }

    private GifHandler(long gifNativePtr) {
        this.gifNativePtr = gifNativePtr;
    }

    public static GifHandler load(String path) {
        long gifNativePtr = loadGif(path);
        return new GifHandler(gifNativePtr);
    }

    // 开始加载 gif 文件
    public static native long loadGif(String path);

    // 获取宽
    public static native int getWidth(long gifNativePtr);

    // 获取高
    public static native int getHeight(long gifNativePtr);

    // 渲染图片，返回下一帧的时间
    public static native int updateFrame(long gifNativePtr, Bitmap bitmap);
}
