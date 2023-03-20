package com.music.player.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class MusicGLSurfaceView extends GLSurfaceView {

    private MusicRender musicRender;

    public MusicGLSurfaceView(Context context) {
        this(context, null);
    }

    public MusicGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        musicRender = new MusicRender(context);
        setRenderer(musicRender);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public void setYUVData(int width, int height, byte[] y, byte[] u, byte[] v) {
        if (musicRender != null) {
            musicRender.setYUVRenderData(width, height, y, u, v);
            requestRender();
        }
    }
}
