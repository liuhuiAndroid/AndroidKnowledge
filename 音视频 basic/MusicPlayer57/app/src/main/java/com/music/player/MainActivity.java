package com.music.player;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.SeekBar;

import com.music.player.databinding.ActivityMainBinding;
import com.music.player.ui.utils.DisplayUtil;

import java.io.File;

public class MainActivity extends AppCompatActivity implements IPlayerListener {

    static {
        System.loadLibrary("player");
    }

    public ActivityMainBinding binding;

    public MusicPlayer musicPlayer;

    public int position;

    public boolean seek = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 5);
        }

        binding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                position = progress * musicPlayer.getDuration() / 100;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seek = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicPlayer.seek(position);
                seek = false;
            }
        });


        musicPlayer = new MusicPlayer();

        musicPlayer.setMusicGLSurfaceView(binding.wlglsurfaceview);
        musicPlayer.setPlayerListener(this);
        musicPlayer.setOnPreparedListener(() -> musicPlayer.start());
    }

    public void seekTo(int position) {
        musicPlayer.seek(position);
    }

    public void begin(View view) {
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "input.mp4");
        musicPlayer.setSource(file.getAbsolutePath());
        musicPlayer.prepare();
    }

    public void stop(View view) {
        musicPlayer.stop();
    }

    public void pause(View view) {
        musicPlayer.pause();
    }

    public void resume(View view) {
        musicPlayer.resume();
    }

    @Override
    public void onLoad(boolean load) {

    }

    @Override
    public void onCurrentTime(int currentTime, int totalTime) {
        if (!seek && totalTime > 0) {
            runOnUiThread(() -> {
                binding.seekbar.setProgress(currentTime * 100 / totalTime);
                binding.tvTime.setText(DisplayUtil.secdsToDateFormat(currentTime)
                        + "/" + DisplayUtil.secdsToDateFormat(totalTime));
            });
        }
    }

    @Override
    public void onError(int code, String msg) {

    }

    @Override
    public void onPause(boolean pause) {

    }

    @Override
    public void onDbValue(int db) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public String onNext() {
        return null;
    }

    public void left(View view) {
        musicPlayer.setMute(1);
    }

    public void right(View view) {
        musicPlayer.setMute(0);
    }

    public void center(View view) {
        musicPlayer.setMute(2);
    }

    public void speed1(View view) {
        musicPlayer.setSpeed(1.5f);
        musicPlayer.setPitch(1.0f);
    }

    public void speed2(View view) {
        musicPlayer.setSpeed(1.5f);
        musicPlayer.setPitch(1.5f);
    }
}