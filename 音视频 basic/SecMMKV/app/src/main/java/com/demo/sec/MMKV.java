package com.demo.sec;

import android.os.Environment;

public class MMKV {

    static {
        System.loadLibrary("sec");
    }

    private long nativeHandle;

    private MMKV(long handle) {
        nativeHandle = handle;
    }

    static private String rootDir = null;

    // 仅仅初始化文件夹
    public static String initialize() {
        String root = Environment.getExternalStorageDirectory() + "/mmkv";
        // 初始化 MMKV 框架
        return initialize(root);
    }

    // 仅仅初始化文件夹
    public static String initialize(String rootDir) {
        MMKV.rootDir = rootDir;
        jniInitialize(MMKV.rootDir);
        return rootDir;
    }

    // 实例化 MMKV 对象，物理内存映射
    public static MMKV defaultMMKV() {
        long handle = getDefaultMMKV();
        return new MMKV(handle);
    }

    // 存数据
    public void putInt(String key, int value) {
        putInt(nativeHandle, key, value);
    }

    private static native void jniInitialize(String rootDir);

    private native static long getDefaultMMKV();

    private native void putInt(long handle, String key, int value);

    private native int getInt(long handle, String key, int defaultValue);

    public int getInt(String key, int defaultValue) {
        return getInt(nativeHandle, key, defaultValue);
    }
    //

}
