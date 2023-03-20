package com.demo.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class CameraView extends GLSurfaceView {

    private CameraRender renderer;

    public CameraView(Context context) {
        super(context);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 设置版本
        setEGLContextClientVersion(2);
        renderer = new CameraRender(this);
        setRenderer(renderer);
        // 刷新方式：
        // RENDERMODE_WHEN_DIRTY 手动刷新，調用requestRender();
        // RENDERMODE_CONTINUOUSLY 自動刷新，大概16ms自動回調一次onDrawFrame方法
        // 注意必须在 setRenderer 后面
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

}
