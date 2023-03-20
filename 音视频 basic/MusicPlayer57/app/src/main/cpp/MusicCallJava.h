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
    jmethodID jMid_timeInfo;
    jmethodID jMid_load;
    jmethodID jMid_renderyuv;

public:
    MusicCallJava(_JavaVM *javaVM, JNIEnv *env, jobject *jObj);

    ~MusicCallJava();

    void onCallPrepared(int type);

    void onCallTimeInfo(int type, int curr, int total);

    void onCallLoad(int i, bool b);

    void onCallRenderYUV(int width, int height, uint8_t *fy, uint8_t *fu, uint8_t *fv);
};

#endif //MUSICPLAYER_MUSICCALLJAVA_H
