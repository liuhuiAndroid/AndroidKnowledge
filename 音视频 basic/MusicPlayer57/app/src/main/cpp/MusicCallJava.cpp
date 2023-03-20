//
// Created by sec on 2022/11/6.
//

#include "MusicCallJava.h"

void MusicCallJava::onCallPrepared(int type) {
    if (type == MAIN_THREAD) {
        jniEnv->CallVoidMethod(jObj, jMid_prepared);
    } else if (type == CHILD_THREAD) {
        JNIEnv *jniEnv;
        if (javaVM->AttachCurrentThread(&jniEnv, 0) != JNI_OK) {
            if (LOG_DEBUG) {
                LOGE("get child thread jnienv worng");
            }
            return;
        }
        jniEnv->CallVoidMethod(jObj, jMid_prepared);
        javaVM->DetachCurrentThread();
    }
}

MusicCallJava::MusicCallJava(_JavaVM *javaVM, JNIEnv *env, jobject *jobj) {
    this->javaVM = javaVM;
    this->jniEnv = env;
    this->jObj = *jobj;
    this->jObj = env->NewGlobalRef(jObj);
    jclass jlz = jniEnv->GetObjectClass(jObj);
    if (!jlz) {
        if (LOG_DEBUG) {
            LOGE("get jclass wrong")
        }
        return;
    }
    jMid_prepared = env->GetMethodID(jlz, "onCallPrepared", "()V");
    jMid_timeInfo = env->GetMethodID(jlz, "onCallTimeInfo", "(II)V");
    jMid_load = env->GetMethodID(jlz, "onCallLoad", "(Z)V");
    jMid_renderyuv = env->GetMethodID(jlz, "onCallRenderYUV", "(II[B[B[B)V");
}

//回调   java
void MusicCallJava::onCallTimeInfo(int type, int curr, int total) {
    if (type == MAIN_THREAD) {
        jniEnv->CallVoidMethod(jObj, jMid_timeInfo, curr, total);
    } else if (type == CHILD_THREAD) {
        JNIEnv *jniEnv;
        if (javaVM->AttachCurrentThread(&jniEnv, 0) != JNI_OK) {
            if (LOG_DEBUG) {
                LOGE("call onCallTimeInfo worng");
            }
            return;
        }
        jniEnv->CallVoidMethod(jObj, jMid_timeInfo, curr, total);
        javaVM->DetachCurrentThread();
    }
}

MusicCallJava::~MusicCallJava() {

}

void MusicCallJava::onCallLoad(int type, bool load) {
    if (type == MAIN_THREAD) {
        jniEnv->CallVoidMethod(jObj, jMid_load, load);
    } else if (type == CHILD_THREAD) {
        JNIEnv *jniEnv;
        if (javaVM->AttachCurrentThread(&jniEnv, 0) != JNI_OK) {
            return;
        }
        jniEnv->CallVoidMethod(jObj, jMid_load, load);
        javaVM->DetachCurrentThread();
    }
}

void MusicCallJava::onCallRenderYUV(int width, int height, uint8_t *fy, uint8_t *fu, uint8_t *fv) {
    JNIEnv *jniEnv;
    if (javaVM->AttachCurrentThread(&jniEnv, 0) != JNI_OK) {
        return;
    }
    jbyteArray y = jniEnv->NewByteArray(width * height);
    jniEnv->SetByteArrayRegion(y, 0, width * height, reinterpret_cast<const jbyte *>(fy));
    jbyteArray u = jniEnv->NewByteArray(width * height / 4);
    jniEnv->SetByteArrayRegion(u, 0, width * height / 4, reinterpret_cast<const jbyte *>(fu));
    jbyteArray v = jniEnv->NewByteArray(width * height / 4);
    jniEnv->SetByteArrayRegion(v, 0, width * height / 4, reinterpret_cast<const jbyte *>(fv));

    jniEnv->CallVoidMethod(jObj, jMid_renderyuv, width, height, y, u, v);
    jniEnv->DeleteLocalRef(y);
    jniEnv->DeleteLocalRef(u);
    jniEnv->DeleteLocalRef(v);
    javaVM->DetachCurrentThread();
}