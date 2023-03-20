package com.music.player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.music.player.databinding.ActivityMainBinding;
import com.music.player.ui.utils.DisplayUtil;

import java.io.File;
import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    static {
        System.loadLibrary("player");
    }

    private ActivityMainBinding binding;

    private MusicReceiver mMusicReceiver = new MusicReceiver();
    DisplayUtil displayUtil = new DisplayUtil();

    private int totalTime;
    private int currentPosition;
    private boolean playState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 5);
        }

        Intent intent = new Intent(this, MusicService.class);
        startService(intent);

        binding.ivPlayOrPause.setOnClickListener(view -> {
            playState = !playState;
            if (playState) {
                binding.ivPlayOrPause.setImageResource(R.drawable.ic_play);
                pause();
            } else {
                binding.ivPlayOrPause.setImageResource(R.drawable.ic_pause);
                resume();
            }
        });
        binding.startPlay.setOnClickListener(view -> {
            optMusic(MusicService.ACTION_OPT_MUSIC_PLAY);
        });
        initMusicReceiver();
        binding.musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                currentPosition = totalTime * progress / 100;
                binding.tvCurrentTime.setText(displayUtil.duration2Time(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekTo(currentPosition);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
               while (true){
                   try {
                       Thread.sleep(500);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   optMusic(MusicService.ACTION_OPT_MUSIC_VOLUME);
               }
            }
        }).start();
    }

    private void seekTo(int position) {
        Intent intent = new Intent(MusicService.ACTION_OPT_MUSIC_SEEK_TO);
        intent.putExtra(MusicService.PARAM_MUSIC_SEEK_TO, position);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void pause() {
        optMusic(MusicService.ACTION_OPT_MUSIC_PAUSE);
    }

    public void resume() {
        optMusic(MusicService.ACTION_OPT_MUSIC_RESUME);
    }

    private void optMusic(final String action) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(action));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMusicReceiver);
    }

    private void initMusicReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicService.ACTION_STATUS_MUSIC_PLAY);
        intentFilter.addAction(MusicService.ACTION_STATUS_MUSIC_PAUSE);
        intentFilter.addAction(MusicService.ACTION_STATUS_MUSIC_DURATION);
        intentFilter.addAction(MusicService.ACTION_STATUS_MUSIC_COMPLETE);
        intentFilter.addAction(MusicService.ACTION_STATUS_MUSIC_PLAYER_TIME);
        /*注册本地广播*/
        LocalBroadcastManager.getInstance(this).registerReceiver(mMusicReceiver, intentFilter);
    }

    class MusicReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MusicService.ACTION_STATUS_MUSIC_PLAYER_TIME)) {
                int currentTime = intent.getIntExtra("currentTime", 0);
                int totalTime = intent.getIntExtra("totalTime", 0);
                playCurrentTime(currentTime, totalTime);
            }
        }
    }

    private void playCurrentTime(int currentTime, int totalTime) {
        binding.musicSeekBar.setProgress(currentTime * 100 / totalTime);
        this.totalTime = totalTime;
        binding.tvCurrentTime.setText(DisplayUtil.secdsToDateFormat(currentTime, totalTime));
        binding.tvTotalTime.setText(DisplayUtil.secdsToDateFormat(totalTime, totalTime));
    }

    public void left(View view) {
        optMusic(MusicService.ACTION_OPT_MUSIC_LEFT);
    }

    public void right(View view) {
        optMusic(MusicService.ACTION_OPT_MUSIC_RIGHT);
    }

    public void center(View view) {
        optMusic(MusicService.ACTION_OPT_MUSIC_CENTER);
    }

}