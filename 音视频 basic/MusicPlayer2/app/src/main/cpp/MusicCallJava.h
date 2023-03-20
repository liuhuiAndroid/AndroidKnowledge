//
// Created by sec on 2022/11/6.
//

#ifndef MUSICPLAYER_MUSICCALLJAVA_H
#define MUSICPLAYER_MUSICCALLJAVA_H

#include "jni.h"
#include <linux/stddef.h>
#include "AndroidLog.h"

#define MAIN_THREAD 0
#define CHILD_THREAD 1

class MusicCallJava {

public:
    _JavaVM *javaVM = NULL;
    JNIEnv *jniEnv = NULL;
    jobject jObj;
    jmethodID jMid_prepared;

public:
    MusicCallJava(_JavaVM *javaVM, JNIEnv *env, jobject *jObj);

    ~MusicCallJava();

    void onCallPrepared(int type);

};

#endif //MUSICPLAYER_MUSICCALLJAVA_H
