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
}
