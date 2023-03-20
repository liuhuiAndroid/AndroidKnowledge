package com.demo.md5;

import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SignatureUtils.signatureVerify(this);
    }
}
