package com.demo.camera;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class BackgroundRender implements GLSurfaceView.Renderer {

    private String TAG = BackgroundRender.class.getSimpleName();

    // OpenGL 初始化
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        // surface被创建后需要做的处理
        // Set the background frame color
        // 清除之前存留的数据
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        // 渲染窗口大小发生改变或者屏幕方法发生变化时候回调
        GLES20.glViewport(0, 0, width, height);
    }

    // 真正渲染，类似于 onDraw
    @Override
    public void onDrawFrame(GL10 gl10) {
        //执行渲染工作
        //Redraw background color
        // 渲染成黑色
        GLES20.glClearColor(0f, 0f, 0f, 0f);
    }

}
