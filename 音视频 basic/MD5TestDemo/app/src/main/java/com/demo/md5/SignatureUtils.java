package com.demo.md5;

import android.content.Context;

public class SignatureUtils {

    static {
        System.loadLibrary("md5");
    }

    public static native String signatureParams(String params);

    public static native void signatureVerify(Context context);
}
