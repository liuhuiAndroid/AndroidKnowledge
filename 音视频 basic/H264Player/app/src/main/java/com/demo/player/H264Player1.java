package com.demo.player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class H264Player1 implements Runnable {
    private static final String TAG = "H264Player";
    private Context context;

    private String path;
//mediaCodec   手机硬件不一样    dsp  芯片  不一样
    //    解码H264  解压     android 硬编  兼容   dsp  1ms   7000k码率   700k码率    4k   8k
//    码率  直接奔溃 联发科  ----》     音频
    private MediaCodec mediaCodec;
//画面
    private Surface surface;

    public H264Player1(Context context, String path, Surface surface) {

        this.surface = surface;
        this.path = path;
        this.context = context;

        try {
//            h265  --ISO hevc
            mediaCodec = MediaCodec.createDecoderByType("video/avc");
            MediaFormat mediaformat = MediaFormat.createVideoFormat("video/avc", 368, 384);
            mediaformat.setInteger(MediaFormat.KEY_FRAME_RATE, 15);
            mediaCodec.configure(mediaformat, null, null, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//MediaExtractor  视频      画面H264

//    黄视频
    public void play() {
        mediaCodec.start();
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            decodeH264();
        } catch (Exception e) {
            Log.e(TAG, "run: "+e);
        }
    }
    private void decodeH264() {
        byte[] bytes = null;
        try {
//            偷懒   文件  加载内存     文件 1G  1G
            bytes = getBytes(path);
        } catch ( Exception e) {
            e.printStackTrace();
        }
//内部的队列     不是每一个都可以用
        ByteBuffer[] inputBuffers = mediaCodec.getInputBuffers();

//
        int startIndex = 0;
//总字节数
        int totalSize = bytes.length;
        while (true) {
            if (totalSize == 0 ||startIndex >= totalSize) {
                break;
            }
//            寻找索引
            int nextFrameStart =   findByFrame(bytes, startIndex+2, totalSize);

            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
//            查询哪一个bytebuffer能够用
            int inIndex =   mediaCodec.dequeueInputBuffer(10000);
            if (inIndex >= 0) {
//            找到了  david
                ByteBuffer byteBuffer = inputBuffers[inIndex];
                byteBuffer.clear();
                byteBuffer.put(bytes, startIndex, nextFrameStart - startIndex);
//
                mediaCodec.queueInputBuffer(inIndex, 0, nextFrameStart - startIndex, 0, 0);
                startIndex = nextFrameStart;
            }else {
                continue;
            }

//            得到数据
           int outIndex= mediaCodec.dequeueOutputBuffer(info, 10000);
//音视频   裁剪一段 true  1    false   2
            if (outIndex >= 0) {

//完整帧 I P  B帧
                ByteBuffer byteBuffer =mediaCodec.getOutputBuffer(outIndex);

                byteBuffer.position(info.offset);
                byteBuffer.limit(info.offset + info.size);
//图像  Java C++
                byte[] ba = new byte[byteBuffer.remaining()];
                byteBuffer.get(ba);

                YuvImage yuvImage = new YuvImage(ba, ImageFormat.NV21, 368, 384, null);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                yuvImage.compressToJpeg(new Rect(0, 0, 368, 384), 100, baos);
                byte[] jdata = baos.toByteArray();//rgb
                Bitmap bmp = BitmapFactory.decodeByteArray(jdata, 0, jdata.length);
                if (bmp != null) {
                    if (i >5) {
                        try {
                            File myCaptureFile = new File(Environment.getExternalStorageDirectory(), "img.png");
                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                            bmp.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                            bos.flush();
                            bos.close();
                        } catch ( Exception e) {
                            e.printStackTrace();
                        }
                    }
                    i++;
                }
                try {
                    Thread.sleep(33);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mediaCodec.releaseOutputBuffer(outIndex, false);
            }else {
//视频同步  不能  做到  1ms    60ms 差异   3600ms
            }

        }

    }

    int i = 0;
    private int findByFrame( byte[] bytes, int start, int totalSize) {

        int j = 0;
        for (int i = start; i < totalSize-4; i++) {
            if (bytes[i] == 0x00 && bytes[i + 1] == 0x00 && bytes[i + 2] == 0x00 && bytes[i + 3] == 0x01) {
                return i;
            }

        }
        return -1;
    }
    public   byte[] getBytes(String path) throws IOException {
        InputStream is =   new DataInputStream(new FileInputStream(new File(path)));
        int len;
        int size = 1024;
        byte[] buf;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        buf = new byte[size];
        while ((len = is.read(buf, 0, size)) != -1)
            bos.write(buf, 0, len);
        buf = bos.toByteArray();
        return buf;
    }
}
