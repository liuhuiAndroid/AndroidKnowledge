package com.music.player;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.music.player.databinding.ActivityMainBinding;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private AudioTrack audioTrack;

    static {
        System.loadLibrary("player");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView tv = binding.sampleText;
        tv.setText(stringFromJNI());
        binding.button.setOnClickListener(v -> {
            Log.i("TAG", Environment.getExternalStorageDirectory() + "/DCIM/");
//            File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "input.mp4");
//            File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "input.mp3");
//            playSound(file.getAbsolutePath());
            // 播放网络音乐
            playSound("http://mpge.5nd.com/2015/2015-11-26/69708/1.mp3");
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 5);
        }
    }

    public native String stringFromJNI();

    public native void playSound(String input);

    public void createTrack(int sampleRateInHz, int nb_channals) {
        int channelConfig;
        if (nb_channals == 1) {
            // 单通道
            channelConfig = AudioFormat.CHANNEL_OUT_MONO;
        } else if (nb_channals == 2) {
            // 双通道
            channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
        } else {
            // 默认单通道
            channelConfig = AudioFormat.CHANNEL_OUT_MONO;
        }
        // 缓冲区大小
        int bufferSize = AudioTrack.getMinBufferSize(sampleRateInHz,
                channelConfig, AudioFormat.ENCODING_PCM_16BIT);
        // 实例化时必须填写配置信息，一旦填写无法更改
        // int streamType, int sampleRateInHz, int channelConfig, int audioFormat,
        // streamType 流类型 AudioManager.STREAM_MUSIC
        // sampleRateInHz 采样率 44100 48000，从底层传进来
        // channelConfig 通道数 单通道 双通道
        // audioFormat 采样位数 8 16 32
        // bufferSizeInBytes 缓冲区大小
        // mode 流类型
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRateInHz, channelConfig,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);
        audioTrack.play();
    }

    // buffer pcm数据实际内容
    // length pcm数据实际长度
    public void playTrack(byte[] buffer, int length) {
        if (audioTrack != null && audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
            audioTrack.write(buffer, 0, length);
        }
    }
}