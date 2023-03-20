package com.demo.x264live.cameraxpush;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.demo.x264live.live.LivePusher;

public class AudioChannel {

    private LivePusher livePusher;
    private int sampleRate;
    private int channelConfig;
    private int minBufferSize;
    private byte[] buffer;
    private Handler handler;
    private HandlerThread handlerThread;
    private AudioRecord audioRecord;

    public AudioChannel(int sampleRate, int channels, LivePusher livePusher) {
        this.livePusher = livePusher;
        this.sampleRate = sampleRate;
        // 双通道应该传的值 一律用单通道
        channelConfig = channels == 2 ? AudioFormat.CHANNEL_IN_STEREO : AudioFormat.CHANNEL_IN_MONO;
        // 参考值
        minBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig,
                AudioFormat.ENCODING_PCM_16BIT);
        // 初始化faac软编，inputByteNum=最小容器
        int inputByteNum = livePusher.initAudioEnc(sampleRate, channels);
        buffer = new byte[inputByteNum];
        // 取最大值，小了会溢出
        minBufferSize = inputByteNum > minBufferSize ? inputByteNum : minBufferSize;
        handlerThread = new HandlerThread("Audio-Record");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    public void start() {
        // 子线程编码
        handler.post(() -> {
            // 读取麦克风的数据
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    sampleRate, channelConfig,
                    AudioFormat.ENCODING_PCM_16BIT, minBufferSize);
            // 开始录音
            audioRecord.startRecording();
            while (audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                // len：实际长度
                int len = audioRecord.read(buffer, 0, buffer.length);
                if (len > 0) {
                    livePusher.sendAudio(buffer, len / 2);
                }
            }
        });
    }
}
