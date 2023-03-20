package com.demo.mp3;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
    }

    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        }
        return false;
    }

    public void clip(View view) {
        new Thread(() -> {
            final String aacPath = new File(Environment.getExternalStorageDirectory() + "/DCIM",
                    "mp3-clip-origin.mp3").getAbsolutePath();
            final String outPath = new File(Environment.getExternalStorageDirectory() + "/DCIM",
                    "mp3-clip-result.mp3").getAbsolutePath();
            try {
                copyAssetsToSdCard("music.mp3", aacPath);
                MusicProcess musicProcess = new MusicProcess();
                musicProcess.clip(aacPath, outPath, 10 * 1000 * 1000, 15 * 1000 * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void copyAssetsToSdCard(String assetsName, String path) throws IOException {
        AssetFileDescriptor assetFileDescriptor = getAssets().openFd(assetsName);
        FileChannel from = new FileInputStream(assetFileDescriptor.getFileDescriptor()).getChannel();
        FileChannel to = new FileOutputStream(path).getChannel();
        from.transferTo(assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength(), to);
    }

}