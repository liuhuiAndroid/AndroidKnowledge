package com.demo.opengl;

import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.opengl.EGLContext;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;

public class MediaRecorder {

    private MediaCodec mMediaCodec;
    private int mWidth;
    private int mHeight;
    private String mPath;
    private Surface mSurface;
    private Handler mHandler;
    // 编码封装格式 h264
    private MediaMuxer mMuxer;
    private EGLContext mGlContext;
    private EGLEnv eglEnv;
    private boolean isStart;
    private Context mContext;
    private long mLastTimeStamp;
    private int track;
    private float mSpeed;

    public MediaRecorder(Context context, String path, EGLContext glContext, int width, int
            height) {
        mContext = context.getApplicationContext();
        mPath = path;
        mWidth = width;
        mHeight = height;
        mGlContext = glContext;
    }

    public void start(float speed) throws IOException {
        mSpeed = speed;
        MediaFormat format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, mWidth, mHeight);
        //颜色空间 从 surface当中获得
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        //码率
        format.setInteger(MediaFormat.KEY_BIT_RATE, 1500_000);
        //帧率
        format.setInteger(MediaFormat.KEY_FRAME_RATE, 25);
        //关键帧间隔
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 10);
        //创建编码器
        mMediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
        //配置编码器
        mMediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        //输入数据
        mSurface = mMediaCodec.createInputSurface();
        //混合器 (复用器) 将编码的h.264封装为mp4
        mMuxer = new MediaMuxer(mPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        //开启编码
        mMediaCodec.start();
        //創建OpenGL 的 環境
        HandlerThread handlerThread = new HandlerThread("codec-gl");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
        mHandler.post(() -> {
            eglEnv = new EGLEnv(mContext, mGlContext, mSurface, mWidth, mHeight);
            isStart = true;
        });
    }

    public void fireFrame(final int textureId, final long timestamp) {
        // 主动拉取 opengl fbo 数据
        if (!isStart) {
            return;
        }
        // 录制用的opengl已经和handler的线程绑定了，所以需要在这个线程中使用录制的opengl
        mHandler.post(() -> {
            // opengl   能 1  不能2  draw  ---》surface
            eglEnv.draw(textureId, timestamp);
            // 获取对应的数据
            codec(false);
        });
    }

    private void codec(boolean endOfStream) {
        // 可以在这里输出h264数据
        if (endOfStream) {
            // 封装格式输出完成
            mMediaCodec.signalEndOfInputStream();
        }
        // 编码
        // ffplay -stats -f h264 codec.h264
        while (true) {
            // 输出成文件
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int index = mMediaCodec.dequeueOutputBuffer(bufferInfo, 10_000);
            if (index >= 0) {
                ByteBuffer buffer = mMediaCodec.getOutputBuffer(index);
                MediaFormat mediaFormat = mMediaCodec.getOutputFormat(index);
                byte[] outData = new byte[bufferInfo.size];
                buffer.get(outData);
                FileUtils.writeContent(outData);
                FileUtils.writeBytes(outData);
                // 可以在这里实现直播
                mMediaCodec.releaseOutputBuffer(index, false);
            }

            // 输出成MP4
//            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
//            int index = mMediaCodec.dequeueOutputBuffer(bufferInfo, 10_000);
//            // 需要更多数据
//            if (index == MediaCodec.INFO_TRY_AGAIN_LATER) {
//                //如果是结束那直接退出，否则继续循环
//                if (!endOfStream) {
//                    break;
//                }
//            } else if (index == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
//                //输出格式发生改变  第一次总会调用所以在这里开启混合器
//                MediaFormat newFormat = mMediaCodec.getOutputFormat();
//                track = mMuxer.addTrack(newFormat);
//                mMuxer.start();
//            } else if (index == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
//                //可以忽略
//            } else {
//                //调整时间戳
//                bufferInfo.presentationTimeUs = (long) (bufferInfo.presentationTimeUs / mSpeed);
//                //有时候会出现异常 ： timestampUs xxx < lastTimestampUs yyy for Video track
//                if (bufferInfo.presentationTimeUs <= mLastTimeStamp) {
//                    bufferInfo.presentationTimeUs = (long) (mLastTimeStamp + 1_000_000 / 25 / mSpeed);
//                }
//                mLastTimeStamp = bufferInfo.presentationTimeUs;
//
//                //正常则 index 获得缓冲区下标
//                ByteBuffer encodedData = mMediaCodec.getOutputBuffer(index);
//                //如果当前的buffer是配置信息，不管它 不用写出去
//                if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
//                    bufferInfo.size = 0;
//                }
//                if (bufferInfo.size != 0) {
//                    //设置从哪里开始读数据(读出来就是编码后的数据)
//                    encodedData.position(bufferInfo.offset);
//                    //设置能读数据的总长度
//                    encodedData.limit(bufferInfo.offset + bufferInfo.size);
//                    //写出为mp4
//                    mMuxer.writeSampleData(track, encodedData, bufferInfo);
//                }
//                // 释放这个缓冲区，后续可以存放新的编码后的数据啦
//                mMediaCodec.releaseOutputBuffer(index, false);
//                // 如果给了结束信号 signalEndOfInputStream
//                if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
//                    break;
//                }
//            }
        }
    }

    public void stop() {
        // 释放
        isStart = false;
        mHandler.post(() -> {
            codec(true);
            mMediaCodec.stop();
            mMediaCodec.release();
            mMediaCodec = null;
            mMuxer.stop();
            mMuxer.release();
            eglEnv.release();
            eglEnv = null;
            mMuxer = null;
            mSurface = null;
            mHandler.getLooper().quitSafely();
            mHandler = null;
        });
    }

}
