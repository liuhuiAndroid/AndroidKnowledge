package com.demo.camera;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private Camera.Size size;
    private Camera mCamera;
    private MediaCodec mediaCodec;

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        startPreview();
        initMediaCodec();
    }

    private void initMediaCodec() {
        try {
            mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
            final MediaFormat format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC,
                    size.height, size.width);
            format.setInteger(MediaFormat.KEY_COLOR_FORMAT,
                    MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible);
            format.setInteger(MediaFormat.KEY_FRAME_RATE, 15);
            format.setInteger(MediaFormat.KEY_BIT_RATE, 4000_000);
            // 2s一个I帧
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 2);
            mediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mediaCodec.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    byte[] buffer;

    private void startPreview() {
        // 打开Camera
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        // 预览参数
        Camera.Parameters parameters = mCamera.getParameters();
        // 预览尺寸
        size = parameters.getPreviewSize();
        try {
            mCamera.setPreviewDisplay(getHolder());
            // 旋转90度，数据未旋转，预览画面会旋转
            mCamera.setDisplayOrientation(90);
            // 既又不要浪费内存，又不要给小了数据不完整
            // 一个字节8位，ARBG一个像素4个字节，NV21 = YUV420SP_NV21 和 YUV 摆放不一样但是大小一样
            // 一个16进制4位，两个16进制8位，ARBG八个16进制21位=4个字节
            // Y = size.width * size.height
            // U = size.width * size.height / 4
            // V = size.width * size.height / 4
            int bitsPerPixel = ImageFormat.getBitsPerPixel(ImageFormat.NV21);
            buffer = new byte[size.width * size.height * bitsPerPixel / 8];
            // buffer = new byte[size.width * size.height * 3 / 2];
            // 设置缓冲区
            mCamera.addCallbackBuffer(buffer);
            // 通知回调 onPreviewFrame
            mCamera.setPreviewCallbackWithBuffer(this);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    private volatile boolean isCapture;

    public void startCapture() {
        isCapture = true;
    }

//    @Override
//    public void onPreviewFrame(byte[] bytes, Camera camera) {
//        if (isCapture) {
//            byte[] newBytes = new byte[bytes.length];
//            portraitData2Raw(bytes, newBytes, size.width, size.height);
//            // 保存图片
//            capture(newBytes);
//            isCapture = false;
//        }
//        mCamera.addCallbackBuffer(bytes);
//    }

    // 数据顺时针旋转90度，矩阵变换
    public static void portraitData2Raw(byte[] data, byte[] output, int width, int height) {
        int y_len = width * height;
        // uv数据高为y数据高的一半
        int uvHeight = height >> 1;
        int k = 0;
        for (int j = 0; j < width; j++) {
            for (int i = height - 1; i >= 0; i--) {
                output[k++] = data[width * i + j];
            }
        }
        for (int j = 0; j < width; j += 2) {
            for (int i = uvHeight - 1; i >= 0; i--) {
                output[k++] = data[y_len + width * i + j];
                output[k++] = data[y_len + width * i + j + 1];
            }
        }
    }

    int index = 0;

    /**
     * 保存照片
     */
    public void capture(byte[] temp) {
        String fileName = "IMG_" + index++ + ".jpg";
        File pictureFile = new File(Environment.getExternalStorageDirectory() + "/DCIM/", fileName);
        if (!pictureFile.exists()) {
            try {
                pictureFile.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(pictureFile);
                // ImageFormat.NV21 and ImageFormat.YUY2 for now
                // 将 NV21 data 保存成 YuvImage
                YuvImage image = new YuvImage(temp, ImageFormat.NV21, size.height, size.width, null);
                // 图像压缩;将NV21格式图片，以质量100压缩成Jpeg，并得到JPEG数据流
                image.compressToJpeg(
                        new Rect(0, 0, image.getWidth(), image.getHeight()),
                        100, fileOutputStream
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    byte[] nv21_rotated;

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        if (isCapture) {
            nv21_rotated = new byte[bytes.length];
            portraitData2Raw(bytes, nv21_rotated, size.width, size.height);
            // nv21格式转换成nv12(yuv420)
            byte[] temp = nv21toNV12(nv21_rotated);
            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
            int inIndex = mediaCodec.dequeueInputBuffer(100000);
            if (inIndex >= 0) {
                ByteBuffer byteBuffer = mediaCodec.getInputBuffer(inIndex);
                byteBuffer.clear();
                byteBuffer.put(temp, 0, temp.length);
                mediaCodec.queueInputBuffer(inIndex, 0, temp.length, 0, 0);
            }
            // 网络传输一定需要在每个I帧之前，先传sps pps
            int outIndex = mediaCodec.dequeueOutputBuffer(info, 100000);
            if (outIndex >= 0) {
                ByteBuffer byteBuffer = mediaCodec.getOutputBuffer(outIndex);
                byte[] ba = new byte[byteBuffer.remaining()];
                byteBuffer.get(ba);
                writeBytes(ba);
                writeContent(ba);
                mediaCodec.releaseOutputBuffer(outIndex, false);
            }
        }
        mCamera.addCallbackBuffer(bytes);
    }

    byte[] nv12;

    byte[] nv21toNV12(byte[] nv21) {
        int size = nv21.length;
        nv12 = new byte[size];
        int len = size * 2 / 3;
        System.arraycopy(nv21, 0, nv12, 0, len);
        int i = len;
        while (i < size - 1) {
            nv12[i] = nv21[i + 1];
            nv12[i + 1] = nv21[i];
            i += 2;
        }
        return nv12;
    }

    public String writeContent(byte[] array) {
        char[] HEX_CHAR_TABLE = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        StringBuilder sb = new StringBuilder();
        for (byte b : array) {
            sb.append(HEX_CHAR_TABLE[(b & 0xf0) >> 4]);
            sb.append(HEX_CHAR_TABLE[b & 0x0f]);
        }
        FileWriter writer = null;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileWriter(Environment.getExternalStorageDirectory() + "/DCIM/codec.txt", true);
            writer.write(sb.toString());
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public void writeBytes(byte[] array) {
        FileOutputStream writer = null;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileOutputStream(Environment.getExternalStorageDirectory() + "/DCIM/codec.h264", true);
            writer.write(array);
            writer.write('\n');
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
