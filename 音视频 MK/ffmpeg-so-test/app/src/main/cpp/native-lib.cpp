#include <cstdio>
#include <cstring>
#include <string>
#include "util/LogUtil.h"
#include "jni.h"

#define JNI_CLASS_PATH "com/study/test/MainActivity"
#define JNI_STUDENT_PATH "com/study/test/Student"

//由于 FFmpeg 库是 C 语言实现的，告诉编译器按照 C 的规则进行编译
extern "C" {
#include <libavcodec/version.h>
#include <libavcodec/avcodec.h>
#include <libavformat/version.h>
#include <libavutil/version.h>
#include <libavfilter/version.h>
#include <libswresample/version.h>
#include <libswscale/version.h>
#include <jni.h>
};

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jstring JNICALL
Java_com_study_test_MainActivity_stringFromJNI(JNIEnv *env, jobject thiz) {
    char strBuffer[1024 * 4] = {0};
    strcat(strBuffer, "libavcodec : ");
    strcat(strBuffer, AV_STRINGIFY(LIBAVCODEC_VERSION));
    strcat(strBuffer, "\nlibavformat : ");
    strcat(strBuffer, AV_STRINGIFY(LIBAVFORMAT_VERSION));
    strcat(strBuffer, "\nlibavutil : ");
    strcat(strBuffer, AV_STRINGIFY(LIBAVUTIL_VERSION));
    strcat(strBuffer, "\nlibavfilter : ");
    strcat(strBuffer, AV_STRINGIFY(LIBAVFILTER_VERSION));
    strcat(strBuffer, "\nlibswresample : ");
    strcat(strBuffer, AV_STRINGIFY(LIBSWRESAMPLE_VERSION));
    strcat(strBuffer, "\nlibswscale : ");
    strcat(strBuffer, AV_STRINGIFY(LIBSWSCALE_VERSION));
    strcat(strBuffer, "\navcodec_configure : \n");
    strcat(strBuffer, avcodec_configuration());
    strcat(strBuffer, "\navcodec_license : ");
    strcat(strBuffer, avcodec_license());
    LOGCATE("GetFFmpegVersion\n%s", strBuffer);
    return env->NewStringUTF(strBuffer);
}

/**
 * @param env
 * @param thiz 调用这个方法的对象
 * @return
 */
JNIEXPORT jstring JNICALL
Java_com_study_test_MainActivity_stringFromJNI2(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("xxx");
}

JNIEXPORT jstring JNICALL
my_test_register(JNIEnv *env, jobject thiz) {
    // step 1
    jclass clazz = env->FindClass(JNI_STUDENT_PATH);
    // step 2
    jmethodID method_init_id = env->GetMethodID(clazz, "<init>", "()V");
    jmethodID method_set_id = env->GetMethodID(clazz, "setAge", "(I)V");
    jmethodID method_get_id = env->GetMethodID(clazz, "getAge", "()I");
    // step 3
    jobject obj = env->NewObject(clazz, method_init_id);
    // step 4
    env->CallVoidMethod(obj, method_set_id, 18);
    int age = env->CallIntMethod(obj, method_get_id);
    char tmp[] = {0,0,};
    sprintf(tmp, "%d", age);
    std::string hello = "test, year=";
    hello.append(tmp);
    return env->NewStringUTF(hello.c_str());
}

static JNINativeMethod g_methods[] = {
        {
                "_test",
                "()Ljava/lang/String;",
                (void *) my_test_register
        }
};

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;
    vm->GetEnv((void **) &env, JNI_VERSION_1_6);
    jclass clazz = env->FindClass(JNI_CLASS_PATH);
    env->RegisterNatives(clazz, g_methods, sizeof(g_methods) / sizeof(g_methods[0]));
    return JNI_VERSION_1_6;
}

#ifdef __cplusplus
}
#endif

