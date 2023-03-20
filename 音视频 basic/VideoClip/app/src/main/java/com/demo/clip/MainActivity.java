package com.demo.clip;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.jaygoo.widget.RangeSeekBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {

    VideoView videoView;
    RangeSeekBar rangeSeekBar;
    SeekBar musicSeekBar;
    SeekBar voiceSeekBar;
    int musicVolume = 0;
    int voiceVolume = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission(this);
        videoView = findViewById(R.id.videoView);
        rangeSeekBar = findViewById(R.id.rangeSeekBar);
        musicSeekBar = findViewById(R.id.musicSeekBar);
        voiceSeekBar = findViewById(R.id.voiceSeekBar);
        musicSeekBar.setMax(100);
        voiceSeekBar.setMax(100);
        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                musicVolume = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        voiceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                voiceVolume = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    Runnable runnable;
    int duration = 0;

    @Override
    protected void onResume() {
        super.onResume();
        final String aacPath = new File(Environment.getExternalStorageDirectory(), "music.mp3").getAbsolutePath();
        final String videoPath = new File(Environment.getExternalStorageDirectory(), "input.mp4").getAbsolutePath();
        try {
            copyAssets("music.mp3", aacPath);
            copyAssets("input.mp4", videoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        startPlay(new File(Environment.getExternalStorageDirectory(), "input.mp4").getAbsolutePath());
    }

    private void startPlay(String path) {
        ViewGroup.LayoutParams layoutParams = videoView.getLayoutParams();
        layoutParams.height = 675;
        layoutParams.width = 1285;
        videoView.setLayoutParams(layoutParams);
        videoView.setVideoPath(path);

        videoView.start();
        videoView.setOnPreparedListener(mp -> {
            duration = mp.getDuration() / 1000;
            mp.setLooping(true);
            rangeSeekBar.setRange(0, duration);
            rangeSeekBar.setValue(0, duration);
            rangeSeekBar.setEnabled(true);
            rangeSeekBar.requestLayout();
            rangeSeekBar.setOnRangeChangedListener((view, min, max, isFromUser) -> videoView.seekTo((int) min * 1000));
            final Handler handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (videoView.getCurrentPosition() >= rangeSeekBar.getCurrentRange()[1] * 1000) {
                        videoView.seekTo((int) rangeSeekBar.getCurrentRange()[0] * 1000);
                    }
                    handler.postDelayed(runnable, 1000);
                }
            };
            handler.postDelayed(runnable, 1000);
        });
    }

    public static boolean checkPermission(
            Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

        }
        return false;
    }

    public void music(View view) {
        File cacheDir = Environment.getExternalStorageDirectory();
        final File videoFile = new File(cacheDir, "input.mp4");
        final File audioFile = new File(cacheDir, "music.mp3");
        final File outputFile = new File(cacheDir, "output.mp4");
        new Thread() {
            @Override
            public void run() {
                try {
                    MusicProcess.mixAudioTrack(MainActivity.this,
                            videoFile.getAbsolutePath(),
                            audioFile.getAbsolutePath(),
                            outputFile.getAbsolutePath(),
                            (int) (rangeSeekBar.getCurrentRange()[0] * 1000 * 1000),
                            (int) (rangeSeekBar.getCurrentRange()[1] * 1000 * 1000),
                            voiceVolume,
                            musicVolume);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(() -> {
                    startPlay(new File(Environment.getExternalStorageDirectory(), "output.mp4").getAbsolutePath());
                    Toast.makeText(MainActivity.this, "剪辑完毕", Toast.LENGTH_SHORT).show();
                });
            }
        }.start();
    }

    private void copyAssets(String assetsName, String path) throws IOException {
        AssetFileDescriptor assetFileDescriptor = getAssets().openFd(assetsName);
        FileChannel from = new FileInputStream(assetFileDescriptor.getFileDescriptor()).getChannel();
        FileChannel to = new FileOutputStream(path).getChannel();
        from.transferTo(assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength(), to);
    }

}
