package com.face.identity;

import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;

public class CameraHelper implements Camera.PreviewCallback {

    private static final String TAG = "David";
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;
    private int mCameraId;
    private Camera mCamera;
    private byte[] buffer;
    private Camera.PreviewCallback mPreviewCallback;
    private Camera.Size size;
    public CameraHelper(int cameraId) {
        mCameraId = cameraId;
    }

    public void switchCamera() {
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        stopPreview();
        startPreview();
    }

    public int getCameraId() {
        return mCameraId;
    }

    public void stopPreview() {
        if (mCamera != null) {
            //预览数据回调接口
            mCamera.setPreviewCallback(null);
            //停止预览
            mCamera.stopPreview();
            //释放摄像头
            mCamera.release();
            mCamera = null;
        }
    }

    public void startPreview() {
        try {
            //获得camera对象
            mCamera = Camera.open(mCameraId);
            //配置camera的属性
            Camera.Parameters parameters = mCamera.getParameters();
            //设置预览数据格式为nv21
            parameters.setPreviewFormat(ImageFormat.NV21);
            //这是摄像头宽、高
            parameters.setPreviewSize(WIDTH, HEIGHT);
            // 设置摄像头 图像传感器的角度、方向
            mCamera.setParameters(parameters);
            size = parameters.getPreviewSize();
            Log.i(TAG, "----------startPreview  width : " + size.width + "    height: " + size.height);
            buffer = new byte[WIDTH * HEIGHT * 3 / 2];
            //数据缓存区
            mCamera.addCallbackBuffer(buffer);
            mCamera.setPreviewCallbackWithBuffer(this);
            //设置预览画面
            SurfaceTexture surfaceTexture = new SurfaceTexture(11);
            mCamera.setPreviewTexture(surfaceTexture);
            mCamera.startPreview();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void setPreviewCallback(Camera.PreviewCallback previewCallback) {
        mPreviewCallback = previewCallback;
    }


    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        // data数据依然是倒的
        mPreviewCallback.onPreviewFrame(data, camera);
        camera.addCallbackBuffer(buffer);
    }

}
