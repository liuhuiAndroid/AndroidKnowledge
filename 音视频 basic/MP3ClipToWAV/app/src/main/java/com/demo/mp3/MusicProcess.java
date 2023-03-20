package com.demo.mp3;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class MusicProcess {

    private static final String TAG = "MusicProcess";

    @SuppressLint("WrongConstant")
    public void clip(String musicPath, String outPath, int startTime, int endTime) throws Exception {
        if (endTime < startTime) {
            return;
        }
        // MediaExtractor 解封装
        MediaExtractor mediaExtractor = new MediaExtractor();
        // 设置数据源
        mediaExtractor.setDataSource(musicPath);
        // 寻找音频流
        int audioTrack = selectTrack(mediaExtractor);
        mediaExtractor.selectTrack(audioTrack);
        // seek
        mediaExtractor.seekTo(startTime, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
        // 获取轨道信息
        MediaFormat originAudioFormat = mediaExtractor.getTrackFormat(audioTrack);
        // 容器大小，100k
        int maxBufferSize = 100 * 1000;
        if (originAudioFormat.containsKey(MediaFormat.KEY_MAX_INPUT_SIZE)) {
            maxBufferSize = originAudioFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE);
        }
        ByteBuffer buffer = ByteBuffer.allocateDirect(maxBufferSize);
        // 读取封装格式：audio/aac
        MediaCodec mediaCodec = MediaCodec.createDecoderByType(originAudioFormat.getString((MediaFormat.KEY_MIME)));
        mediaCodec.configure(originAudioFormat, null, null, 0);
        File pcmFile = new File(Environment.getExternalStorageDirectory() + "/DCIM", "mp3-clip-mid.pcm");
        FileChannel writeChannel = new FileOutputStream(pcmFile).getChannel();
        mediaCodec.start();
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        int outputBufferIndex;
        while (true) {
            int decodeInputIndex = mediaCodec.dequeueInputBuffer(100000);
            if (decodeInputIndex >= 0) {
                // 时间戳
                long sampleTimeUs = mediaExtractor.getSampleTime();
                if (sampleTimeUs == -1) {
                    // 结束
                    break;
                } else if (sampleTimeUs < startTime) {
                    // 裁剪时间戳前丢弃
                    mediaExtractor.advance();
                    continue;
                } else if (sampleTimeUs > endTime) {
                    // 裁剪时间戳后结束
                    break;
                }
                // 获取到压缩数据
                info.size = mediaExtractor.readSampleData(buffer, 0);
                // PTS
                info.presentationTimeUs = sampleTimeUs;
                info.flags = mediaExtractor.getSampleFlags();
                byte[] content = new byte[buffer.remaining()];
                buffer.get(content);
                // 输出到文件，为了查看
                FileUtils.writeContent(content);
                // 解码
                ByteBuffer inputBuffer = mediaCodec.getInputBuffer(decodeInputIndex);
                inputBuffer.put(content);
                // 通知解码
                mediaCodec.queueInputBuffer(decodeInputIndex, 0, info.size, info.presentationTimeUs, info.flags);
                // 释放压缩数据
                mediaExtractor.advance();
            }
            // 解码
            outputBufferIndex = mediaCodec.dequeueOutputBuffer(info, 100_000);
            while (outputBufferIndex >= 0) {
                ByteBuffer decodeOutputBuffer = mediaCodec.getOutputBuffer(outputBufferIndex);
                // 输出 PCM 数据
                writeChannel.write(decodeOutputBuffer);
                mediaCodec.releaseOutputBuffer(outputBufferIndex, false);
                outputBufferIndex = mediaCodec.dequeueOutputBuffer(info, 100_000);
            }
        }
        writeChannel.close();
        mediaExtractor.release();
        mediaCodec.stop();
        mediaCodec.release();
        File wavFile = new File(Environment.getExternalStorageDirectory() + "/DCIM", "mp3-clip-result.mp3");
        // PCM 转 WAV
        new PcmToWavUtil(
                44100, AudioFormat.CHANNEL_IN_STEREO, 2, AudioFormat.ENCODING_PCM_16BIT
        ).pcmToWav(pcmFile.getAbsolutePath(), wavFile.getAbsolutePath());
        Log.i(TAG, "mixAudioTrack: 转换完毕");
    }

    private int selectTrack(MediaExtractor mediaExtractor) {
        int numTracks = mediaExtractor.getTrackCount();
        for (int i = 0; i < numTracks; i++) {
            MediaFormat format = mediaExtractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("audio/")) {
                return i;
            }
        }
        return -1;
    }
}
