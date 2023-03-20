#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_study_ffmpeg_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_study_ffmpeg_MainActivity_stringFromJNI2(
        JNIEnv *env,
        jobject thiz) {
    std::string hello = "Hello from C++2";
    return env->NewStringUTF(hello.c_str());
}