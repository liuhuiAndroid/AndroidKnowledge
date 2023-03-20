package com.demo.opengl;

import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;

import androidx.camera.core.Preview;
import androidx.lifecycle.LifecycleOwner;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraRender implements GLSurfaceView.Renderer, Preview.OnPreviewOutputUpdateListener,
        SurfaceTexture.OnFrameAvailableListener {

    private static final String TAG = CameraRender.class.getSimpleName();
    private CameraView cameraView;
    private SurfaceTexture mCameraTexture;

    private ScreenFilter screenFilter;
    private int[] textures;
    float[] mtx = new float[16];

    public CameraRender(CameraView cameraView) {
        this.cameraView = cameraView;
        LifecycleOwner lifecycleOwner = (LifecycleOwner) cameraView.getContext();
        // 打开摄像头，设置摄像头监听
        CameraHelper cameraHelper = new CameraHelper(lifecycleOwner, this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        textures = new int[1];
        // 让 SurfaceTexture 与 Gpu 共享一个数据源
        mCameraTexture.attachToGLContext(textures[0]);
        // 监听摄像头数据回调
        mCameraTexture.setOnFrameAvailableListener(this);
        screenFilter = new ScreenFilter(cameraView.getContext());
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        screenFilter.setSize(width, height);
    }

    // 触发重新渲染
    @Override
    public void onDrawFrame(GL10 gl) {
        // 更新摄像头的数据，给了 gpu
        mCameraTexture.updateTexImage();
        // 不是数据
        mCameraTexture.getTransformMatrix(mtx);
        screenFilter.setTransformMatrix(mtx);
        // int 数据 byte[]，开始渲染
        screenFilter.onDraw(textures[0]);
    }

    // 摄像头预览到的数据
    @Override
    public void onUpdated(Preview.PreviewOutput output) {
        mCameraTexture = output.getSurfaceTexture();
    }

    // 当有数据过来回调
    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        // 手动触发重新渲染，触发 onDrawFrame
        cameraView.requestRender();
    }

}
