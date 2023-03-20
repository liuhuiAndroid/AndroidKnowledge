package com.demo.webrtc;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;

public class DecodecPlayerLiveH264 {

    //    解码器
    private MediaCodec mediaCodec;
    //    远端IP
    private String remoteIp;

    public String getRemoteIp() {
        return remoteIp;
    }

    public void initDecoder(String remoteIp, Surface surface) {
        this.remoteIp = remoteIp;

        try {
            mediaCodec = MediaCodec.createDecoderByType("video/avc");
//            类似于hashmap
            final MediaFormat format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, 720, 1280);
            format.setInteger(MediaFormat.KEY_BIT_RATE, 720 * 1280);
            format.setInteger(MediaFormat.KEY_FRAME_RATE, 15);
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);
            mediaCodec.configure(format, surface, null, 0);
            mediaCodec.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendData(byte[] data) {
//        空闲
        int index = mediaCodec.dequeueInputBuffer(100000);
        if (index >= 0) {
            ByteBuffer inputBuffer = mediaCodec.getInputBuffer(index);
            inputBuffer.clear();
            inputBuffer.put(data, 0, data.length);
//            dsp芯片解码
            mediaCodec.queueInputBuffer(index,
                    0, data.length, System.currentTimeMillis(), 0);
        }

        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        int outIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 100000);

        if (outIndex >= 0) {
            mediaCodec.releaseOutputBuffer(outIndex, true);
        }
    }
}
