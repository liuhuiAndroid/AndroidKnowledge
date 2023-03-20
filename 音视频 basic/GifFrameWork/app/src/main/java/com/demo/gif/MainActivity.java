package com.demo.gif;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Bitmap bitmap;
    private GifHandler gifHandler;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        imageView = (ImageView) findViewById(R.id.image);
    }

    private Handler myHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            int delay = gifHandler.updateFrame(bitmap);
            myHandler.sendEmptyMessageDelayed(1, delay);
            imageView.setImageBitmap(bitmap);
        }
    };

    public void verifyStoragePermissions(Activity activity) {
        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"};
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ndkLoadGif(View view) {
        new Thread(() -> {
            try {
                String outPath = new File(Environment.getExternalStorageDirectory() + "/DCIM",
                        "soogif.gif").getAbsolutePath();
                copyAssetsToSdCard("soogif.gif", outPath);
                File file = new File(outPath);
                gifHandler = GifHandler.load(file.getAbsolutePath());
                // 获取宽高
                int width = gifHandler.getWidth();
                int height = gifHandler.getHeight();
                Log.i(TAG, "宽=" + width + "，高=" + height);
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                // 渲染图片，返回下一帧的时间
                int delay = gifHandler.updateFrame(bitmap);
                runOnUiThread(() -> {
                    // 渲染第一帧
                    imageView.setImageBitmap(bitmap);
                    myHandler.sendEmptyMessageDelayed(1, delay);
                });
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
