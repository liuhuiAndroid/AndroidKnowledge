package com.demo.mix;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioFormat;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

//MP3-->MP31
public class MusicProcess {

    private static float normalizeVolume(int volume) {
        return volume / 100f * 1;
    }

    /**
     * 混音
     */
    public static void mixPcm(
            String pcm1Path,
            String pcm2Path,
            String toPath,
            int volume1,    // 0-100
            int volume2     // 0-100
    ) throws IOException {
        float vol1 = normalizeVolume(volume1);
        float vol2 = normalizeVolume(volume2);
        // 一次读2k数据
        byte[] buffer1 = new byte[2048];
        byte[] buffer2 = new byte[2048];
        // 待输出数据
        byte[] buffer3 = new byte[2048];
        FileInputStream is1 = new FileInputStream(pcm1Path);
        FileInputStream is2 = new FileInputStream(pcm2Path);
        // 输出文件
        FileOutputStream fileOutputStream = new FileOutputStream(toPath);
        short temp2, temp1; // 两个short变量相加会大于short
        int temp;
        boolean end1 = false, end2 = false;
        while (!end1 || !end2) {
            if (!end1) {
                end1 = (is1.read(buffer1) == -1);
                // 音乐的pcm数据写入到 buffer3
                System.arraycopy(buffer1, 0, buffer3, 0, buffer1.length);
            }
            if (!end2) {
                end2 = (is2.read(buffer2) == -1);
                // 一个声音2个字节
                for (int i = 0; i < buffer2.length; i += 2) {
                    // 或运算，低字节在前，高字节在后
                    // 有兼容性问题，需要先转码将通道数改成一样
                    temp1 = (short) ((buffer1[i] & 0xff) | (buffer1[i + 1] & 0xff) << 8);
                    temp2 = (short) ((buffer2[i] & 0xff) | (buffer2[i + 1] & 0xff) << 8);
                    // 音乐和视频声音各占一半
                    temp = (int) (temp1 * vol1 + temp2 * vol2);
                    // 移除无效数据
                    if (temp > 32767) {
                        temp = 32767;
                    } else if (temp < -32768) {
                        temp = -32768;
                    }
                    buffer3[i] = (byte) (temp & 0xFF);
                    buffer3[i + 1] = (byte) ((temp >>> 8) & 0xFF);
                }
                fileOutputStream.write(buffer3);
            }
        }
        is1.close();
        is2.close();
        fileOutputStream.close();
    }

    public void mixAudioTrack(
            final String videoInput,
            final String audioInput,
            final String output,
            final Integer startTimeUs,
            final Integer endTimeUs,
            int videoVolume,  //视频声音大小
            int aacVolume     //音频声音大小
    ) throws Exception {
        // 声音输出成pcm数据
        final File videoPcmFile = new File(Environment.getExternalStorageDirectory() + "/DCIM", "video.pcm");
        final File musicPcmFile = new File(Environment.getExternalStorageDirectory() + "/DCIM", "music.pcm");
        decodeToPCM(videoInput, videoPcmFile.getAbsolutePath(), startTimeUs, endTimeUs);
        decodeToPCM(audioInput, musicPcmFile.getAbsolutePath(), startTimeUs, endTimeUs);
        final File mixPcmFile = new File(Environment.getExternalStorageDirectory() + "/DCIM", "mix.pcm");
        mixPcm(videoPcmFile.getAbsolutePath(), musicPcmFile.getAbsolutePath(), mixPcmFile.getAbsolutePath(), videoVolume, aacVolume);
        // PCM 转 WAV
        new PcmToWavUtil(
                44100, AudioFormat.CHANNEL_IN_STEREO, 2, AudioFormat.ENCODING_PCM_16BIT
        ).pcmToWav(mixPcmFile.getAbsolutePath(), output);
    }

    // 视频文件和音频文件截取并且输出PCM数据
    @SuppressLint("WrongConstant")
    public void decodeToPCM(String musicPath, String outPath, int startTime, int endTime) throws Exception {
        if (endTime < startTime) {
            return;
        }
        // 解封装
        MediaExtractor mediaExtractor = new MediaExtractor();
        mediaExtractor.setDataSource(musicPath);
        int audioTrack = selectTrack(mediaExtractor);
        mediaExtractor.selectTrack(audioTrack);
        mediaExtractor.seekTo(startTime, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
        MediaFormat oriAudioFormat = mediaExtractor.getTrackFormat(audioTrack);
        int maxBufferSize = 100 * 1000;
        if (oriAudioFormat.containsKey(MediaFormat.KEY_MAX_INPUT_SIZE)) {
            maxBufferSize = oriAudioFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE);
        }
        ByteBuffer buffer = ByteBuffer.allocateDirect(maxBufferSize);
        MediaCodec mediaCodec = MediaCodec.createDecoderByType(oriAudioFormat.getString((MediaFormat.KEY_MIME)));
        mediaCodec.configure(oriAudioFormat, null, null, 0);
        File pcmFile = new File(outPath);
        FileChannel writeChannel = new FileOutputStream(pcmFile).getChannel();
        mediaCodec.start();
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        int outputBufferIndex;
        while (true) {
            int decodeInputIndex = mediaCodec.dequeueInputBuffer(100000);
            if (decodeInputIndex >= 0) {
                long sampleTimeUs = mediaExtractor.getSampleTime();
                if (sampleTimeUs == -1) {
                    break;
                } else if (sampleTimeUs < startTime) {
                    mediaExtractor.advance();
                    continue;
                } else if (sampleTimeUs > endTime) {
                    break;
                }
                info.size = mediaExtractor.readSampleData(buffer, 0);
                info.presentationTimeUs = sampleTimeUs;
                info.flags = mediaExtractor.getSampleFlags();
                byte[] content = new byte[buffer.remaining()];
                buffer.get(content);
                // 输出文件，方便查看
                FileUtils.writeContent(content);
                ByteBuffer inputBuffer = mediaCodec.getInputBuffer(decodeInputIndex);
                inputBuffer.put(content);
                mediaCodec.queueInputBuffer(decodeInputIndex, 0, info.size, info.presentationTimeUs, info.flags);
                // 释放上一帧的压缩数据
                mediaExtractor.advance();
            }
            outputBufferIndex = mediaCodec.dequeueOutputBuffer(info, 100_000);
            while (outputBufferIndex >= 0) {
                ByteBuffer decodeOutputBuffer = mediaCodec.getOutputBuffer(outputBufferIndex);
                writeChannel.write(decodeOutputBuffer);//MP3  1   pcm2
                mediaCodec.releaseOutputBuffer(outputBufferIndex, false);
                outputBufferIndex = mediaCodec.dequeueOutputBuffer(info, 100_000);
            }
        }
        writeChannel.close();
        mediaExtractor.release();
        mediaCodec.stop();
        mediaCodec.release();
        Log.i("MusicProcess", "mixAudioTrack: 转换完毕");
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
