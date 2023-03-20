package com.music.player;

import android.text.TextUtils;
import android.util.Log;

public class MusicPlayer {

    private static final String TAG = "MusicPlayer";
    // 数据源
    private String source;

    private OnPreparedListener mOnPreparedListener;

    public void setSource(String source) {
        this.source = source;
    }

    public void setOnPreparedListener(OnPreparedListener onPreparedListener) {
        this.mOnPreparedListener = onPreparedListener;
    }

    public void prepare() {
        if (TextUtils.isEmpty(source)) {
            Log.d(TAG, "source not be empty");
            return;
        }
        new Thread(() -> n_prepare(source)).start();
    }

    /**
     * c++回调java的方法
     */
    public void onCallPrepared() {
        if (mOnPreparedListener != null) {
            mOnPreparedListener.onPrepared();
        }
    }

    public native void n_prepare(String source);

    public native void n_start();

    public void start() {
        if (TextUtils.isEmpty(source)) {
            Log.d(TAG, "source is empty");
            return;
        }
        new Thread(() -> n_start()).start();
    }
}
