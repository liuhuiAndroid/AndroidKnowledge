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

import com.music.player.databinding.ActivityMainBinding;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    static {
        System.loadLibrary("player");
    }

    private ActivityMainBinding binding;
    private MusicPlayer musicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView tv = binding.sampleText;
        tv.setText(stringFromJNI());
        musicPlayer = new MusicPlayer();
        musicPlayer.setOnPreparedListener(() -> musicPlayer.start());
        binding.button.setOnClickListener(v -> {
            musicPlayer.setSource("http://mpge.5nd.com/2015/2015-11-26/69708/1.mp3");
            musicPlayer.prepare();
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 5);
        }
    }

    public native String stringFromJNI();

}