#include <jni.h>
#include <string>
#include <android/log.h>
#include "MusicCallJava.h"
#include "MusicFFmpeg.h"
#include "MusicPlayStatus.h"

extern "C" {
#include "libavcodec/avcodec.h"
#include "libavformat/avformat.h"
#include "libswscale/swscale.h"
#include "libavutil/imgutils.h"
#include "libswresample/swresample.h"
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_music_player_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = avcodec_configuration();
    return env->NewStringUTF(hello.c_str());
}

_JavaVM *javaVM = NULL;
MusicCallJava *callJava = NULL;
MusicFFmpeg *ffmpeg = NULL;
MusicPlayStatus *playStatus = NULL;

extern "C"
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    jint result = -1;
    javaVM = vm;
    JNIEnv *env;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        return result;
    }
    return JNI_VERSION_1_4;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_music_player_MusicPlayer_n_1prepare(JNIEnv *env, jobject instance, jstring source_) {
    // 把Java层的source改成native层的source
    const char *source = env->GetStringUTFChars(source_, 0);
    if (ffmpeg == NULL) {
        if (callJava == NULL) {
            callJava = new MusicCallJava(javaVM, env, &instance);
        }
        playStatus = new MusicPlayStatus();
        ffmpeg = new MusicFFmpeg(playStatus, callJava, source);
        ffmpeg->prepared();
    }
    env->ReleaseStringUTFChars(source_, source);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_music_player_MusicPlayer_n_1start(JNIEnv *env, jobject thiz) {
    if (ffmpeg != NULL) {
        ffmpeg->start();
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_music_player_MusicPlayer_n_1seek(JNIEnv *env, jobject thiz, jint position) {
    if (ffmpeg != NULL) {
        ffmpeg->seek(position);
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_music_player_MusicPlayer_n_1resume(JNIEnv *env, jobject thiz) {
    if (ffmpeg != NULL) {
        ffmpeg->resume();
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_music_player_MusicPlayer_n_1pause(JNIEnv *env, jobject thiz) {
    if (ffmpeg != NULL) {
        ffmpeg->pause();
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_music_player_MusicPlayer_n_1mute(JNIEnv *env, jobject thiz, jint mute) {
    if (ffmpeg != NULL) {
        ffmpeg->setMute(mute);
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_music_player_MusicPlayer_n_1volume(JNIEnv *env, jobject thiz, jint percent) {
    if (ffmpeg != NULL) {
        ffmpeg->setVolume(percent);
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_music_player_MusicPlayer_n_1speed(JNIEnv *env, jobject thiz, jfloat speed) {
    if (ffmpeg != NULL) {
        ffmpeg->setSpeed(speed);
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_music_player_MusicPlayer_n_1pitch(JNIEnv *env, jobject thiz, jfloat pitch) {
    if (ffmpeg != NULL) {
        ffmpeg->setPitch(pitch);
    }
}

bool nExit = true;

extern "C"
JNIEXPORT void JNICALL
Java_com_music_player_MusicPlayer_n_1stop(JNIEnv *env, jobject thiz) {
    if (!nExit) {
        return;
    }
// 正在退出 只调用一次
    nExit = false;
    if (ffmpeg != NULL) {
        ffmpeg->release();
        delete (ffmpeg);
        if (callJava != NULL) {
            delete (callJava);
            callJava = NULL;
        }
        if (playStatus != NULL) {
            delete (playStatus);
            playStatus = NULL;
        }
    }
    nExit = true;
}