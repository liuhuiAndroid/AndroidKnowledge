package com.demo.webrtc;

import android.graphics.SurfaceTexture;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.TextureView;
import android.view.ViewGroup;

import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class EncoderPlayerLiveH264 implements Preview.OnPreviewOutputUpdateListener , ImageAnalysis.Analyzer {

    private static final String TAG = "david";
    //    1   打开摄像头   并且渲染值textureView
    int width = 720;
    int height = 1280;
    private TextureView textureView;
    private HandlerThread handlerThread;
    private CameraX.LensFacing currentFacing = CameraX.LensFacing.BACK;
    private MediaCodec mediaCodec;
    // 引用
    private SocketLive socketLive;

    public EncoderPlayerLiveH264(MainActivity mainActivity, TextureView textureView) {
        this.textureView = textureView;
        handlerThread = new HandlerThread("Analyze-thread");
        handlerThread.start();
        CameraX.bindToLifecycle(mainActivity, getPreView(),getAnalysis());
    }

    private Preview getPreView() {
//        480 *640  是 1 不是  2 Cmaera
        PreviewConfig previewConfig = new PreviewConfig.Builder().setTargetResolution(new Size(width, height))
                .setLensFacing(currentFacing).build();
        Preview preview = new Preview(previewConfig);
        preview.setOnPreviewOutputUpdateListener(this);
        return preview;
    }

//作图向 获取
    private ImageAnalysis getAnalysis() {
        ImageAnalysisConfig imageAnalysisConfig = new ImageAnalysisConfig.Builder()
                .setCallbackHandler(new Handler(handlerThread.getLooper()))
                .setLensFacing(currentFacing)
                .setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
                .setTargetResolution(new Size(width, height))
                .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis(imageAnalysisConfig);
        imageAnalysis.setAnalyzer(this);
        return imageAnalysis;
    }

//当摄像头切换了
    @Override
    public void onUpdated(Preview.PreviewOutput output) {

        SurfaceTexture surfaceTexture = output.getSurfaceTexture();
        if (textureView.getSurfaceTexture() != surfaceTexture) {
            if (textureView.isAvailable()) {
                // 当切换摄像头时，会报错
                ViewGroup parent = (ViewGroup) textureView.getParent();
                parent.removeView(textureView);
                parent.addView(textureView, 0);
                parent.requestLayout();
            }
            textureView.setSurfaceTexture(surfaceTexture);
        }
    }

    private byte[] y;
    private byte[] u;
    private byte[] v;
    private byte[] nv21;
    byte[] nv21_rotated;
    byte[] nv12;
    private boolean isStart = false;
    int frameIndex;

//    摄像头不活了一帧   回调 analyze
    @Override
    public void analyze(ImageProxy image, int rotationDegrees) {
        if (!isStart) {
            return;
        }
//        音视频会议的入口
        Log.i(TAG, "analyze: " + image.getPlanes()[0].getRowStride());
//yuv的数据
        ImageProxy.PlaneProxy[] planes =  image.getPlanes();
        synchronized (this) {
            if (y == null) {
//            初始化y v  u
                y = new byte[planes[0].getBuffer().limit() - planes[0].getBuffer().position()];
                u = new byte[planes[1].getBuffer().limit() - planes[1].getBuffer().position()];
                v = new byte[planes[2].getBuffer().limit() - planes[2].getBuffer().position()];
            }
        }
        planes[0].getBuffer().get(y);
        planes[1].getBuffer().get(u);
        planes[2].getBuffer().get(v);
//得到图片的宽度
        Size size = new Size(image.getWidth(), image.getHeight());
        int width = size.getHeight();
        int heigth = image.getWidth();
        if (nv21 == null) {
            nv21 = new byte[heigth * width * 3 / 2];
            nv21_rotated = new byte[heigth * width * 3 / 2];
        }
//        y  u v
        ImageUtil.yuvToNv21(y, u, v, nv21, heigth, width);

        ImageUtil.nv21_rotate_to_90(nv21, nv21_rotated, heigth, width);
        byte[] temp = ImageUtil.nv21toNV12(nv21_rotated, nv12);

//        在这里作为起点  帧的数据    temp
        Log.i(TAG, "analyze: 帧大小" + temp.length);

//        数据进行编码  ----》 网络传输到另外一段

        if (mediaCodec == null) {
            if (mediaCodec == null) {
                initCodec(size);
            }
        }
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
//temp  ----> 编码
//        空闲的容器索引
        int inIndex =mediaCodec.dequeueInputBuffer(100000);
        if (inIndex >= 0) {
//            对应的容器
            ByteBuffer byteBuffer = mediaCodec.getInputBuffer(inIndex);
            byteBuffer.clear();
            byteBuffer.put(temp, 0, temp.length);
//            录制的时候 pts  nv21  nv12  yuv420  训练营 音视频
            long presentationTimeUs = computePresentationTime(frameIndex);
//通知dsp编码 1.30音视频
            mediaCodec.queueInputBuffer(inIndex, 0, temp.length, presentationTimeUs,
                    0);
            frameIndex++;
        }

//        数据

        int outIndex = mediaCodec.dequeueOutputBuffer(info, 100000);

        if (outIndex >= 0) {
//            H264数据在这里
            ByteBuffer byteBuffer = mediaCodec.getOutputBuffer(outIndex);
//输出 纯纯实验
//            byte[] ba = new byte[byteBuffer.remaining()];
//            byteBuffer.get(ba);
//            FileUtils.writeBytes(ba);
//            FileUtils.writeContent(ba);
            dealFrame(byteBuffer, info);

            mediaCodec.releaseOutputBuffer(outIndex, false);

        }
    }
    private byte[] sps_pps_buf;
    public static final int NAL_I = 5;
    public static final int NAL_SPS= 7;

    private void dealFrame(ByteBuffer bb, MediaCodec.BufferInfo bufferInfo) {
        int type =  bb.get(4)&0x1F;
        if (type == NAL_SPS) {
//            缓存到全局变量中
            sps_pps_buf = new byte[bufferInfo.size];
            bb.get(sps_pps_buf);
//此时不发送 会议其他人
        } else if (type == NAL_I) {
//            I帧
            final byte[] bytes = new byte[bufferInfo.size];
//200833+67
            bb.get(bytes);
            byte[] newBuf = new byte[sps_pps_buf.length + bytes.length];
//            放到最前面
            System.arraycopy(sps_pps_buf, 0, newBuf, 0, sps_pps_buf.length);
//            I帧的数据放到后面
            System.arraycopy(bytes, 0, newBuf, sps_pps_buf.length, bytes.length);
            socketLive.sendData(newBuf);
            Log.v(TAG, "视频数据  " + Arrays.toString(bytes));
//            网络方式的fasongchuq
        }else {
            final byte[] bytes = new byte[bufferInfo.size];
            bb.get(bytes);
//            写到了这里 我们成功推送给这个会议室的其他人
            socketLive.sendData(bytes);
            Log.v(TAG, "视频数据  " + Arrays.toString(bytes));


        }
    }

    private long computePresentationTime(long frameIndex) {
        return frameIndex * 1000000 / 15;
    }

    private void initCodec(Size size) {
        try {
            mediaCodec =  MediaCodec.createEncoderByType("video/avc");
            final MediaFormat format = MediaFormat.createVideoFormat("video/avc",
                    size.getHeight(), size.getWidth());
            //设置帧率
            format.setInteger(MediaFormat.KEY_COLOR_FORMAT,
                    MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible);
            format.setInteger(MediaFormat.KEY_FRAME_RATE, 15);
            format.setInteger(MediaFormat.KEY_BIT_RATE, 8000_000);
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 2);//2s一个I帧
            //编码了
            mediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mediaCodec.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startCapture(SocketLive socketLive) {

        isStart = true;
        this.socketLive = socketLive;
    }
//    什么时候把数据
}
