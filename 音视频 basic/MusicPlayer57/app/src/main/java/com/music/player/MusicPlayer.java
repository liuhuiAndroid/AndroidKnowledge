package com.music.player;

import android.text.TextUtils;
import android.util.Log;

import com.music.player.opengl.MusicGLSurfaceView;

public class MusicPlayer {

    private int duration = 0;

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

    private IPlayerListener playerListener;

    public void setPlayerListener(IPlayerListener playerListener) {
        this.playerListener = playerListener;
    }

    private MusicGLSurfaceView musicGLSurfaceView;

    public void setMusicGLSurfaceView(MusicGLSurfaceView musicGLSurfaceView) {
        this.musicGLSurfaceView = musicGLSurfaceView;
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

    public void onCallTimeInfo(int currentTime, int totalTime) {
        if (playerListener == null) {
            return;
        }
        duration = totalTime;
        playerListener.onCurrentTime(currentTime, totalTime);
    }

    public void seek(int position) {
        n_seek(position);
    }

    private native void n_seek(int position);

    private native void n_resume();

    private native void n_pause();

    private native void n_mute(int mute);

    private native void n_volume(int percent);

    private native void n_speed(float speed);

    private native void n_pitch(float pitch);

    public void pause() {
        n_pause();
    }

    public void resume() {
        n_resume();
    }

    public void setMute(int mute) {
        n_mute(mute);
    }

    public void setVolume(int percent) {
        if (percent >= 0 && percent <= 100) {
            n_volume(percent);
        }
    }

    public void setSpeed(float speed) {
        n_speed(speed);
    }

    public void setPitch(float pitch) {
        n_pitch(pitch);
    }


    public void stop() {
        new Thread(() -> n_stop()).start();
    }

    private native void n_stop();

    public void onCallLoad(boolean load) {
    }

    public void onCallRenderYUV(int width, int height, byte[] y, byte[] u, byte[] v) {
        if (this.musicGLSurfaceView != null) {
            this.musicGLSurfaceView.setYUVData(width, height, y, u, v);
        }
    }

    public int getDuration() {
        return duration;
    }
}
