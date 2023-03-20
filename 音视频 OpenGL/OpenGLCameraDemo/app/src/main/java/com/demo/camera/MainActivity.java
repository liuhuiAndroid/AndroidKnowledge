package com.demo.camera;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 顶点程序：矢量图形
 * 片元程序：上色
 */
public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        glSurfaceView = (GLSurfaceView) findViewById(R.id.glSurfaceView);
        // GLContext 设置 OpenGLES2.0
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new TriangleRender());
        // glSurfaceView.setRenderer(new BackgroundRender());
        // 设置渲染方式
        // RENDERMODE_WHEN_DIRTY : 表示被动渲染，只有在调用requestRender或者onResume等方法时才会进行渲染。
        // 一般选择 RENDERMODE_WHEN_DIRTY
        // RENDERMODE_CONTINUOUSLY : 表示持续渲染，16ms刷新一次
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

}
