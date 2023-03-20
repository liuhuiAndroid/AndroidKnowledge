#include <jni.h>
#include <string>
#include <android/bitmap.h>
extern "C"{
#include "gif_lib.h"
}
//8  1   16  2  24  3
struct GifBean{
    int current_frame;
    int total_frame;
    int *delays;
};

jclass stringClass;

//extern "C"
//JNIEXPORT jlong JNICALL
//Java_com_maniu_gifframework_GifHandler_loadGif1(JNIEnv *env, jclass clazz, jstring path_) {
//    stringClass = env->FindClass( "java/lang/String");
//    return -1;
//}
//
//extern "C"
//JNIEXPORT jlong JNICALL
//Java_com_maniu_gifframework_GifHandler_loadGif2(JNIEnv *env, jclass clazz, jstring path_) {
//    const char *path = env->GetStringUTFChars(path_, 0);
//    env->ReleaseStringUTFChars(path_, path);
//    return -1;
//}
//
//
//extern "C"
//JNIEXPORT jlong JNICALL
//Java_com_maniu_gifframework_GifHandler_loadGif3(JNIEnv *env, jclass clazz, jstring path_) {
//    jclass tempClass = env->FindClass( "java/lang/String");
//    stringClass = static_cast<jclass>(env->NewGlobalRef(tempClass));
////    合适的地方销毁
//     env ->DeleteLocalRef( stringClass);
//    /* Is the global reference created successfully? */
//    if (stringClass == NULL) {
//        return NULL; /* out of memory exception thrown */
//    }
//    return -1;
//}
////弱全局引用
//extern "C"
//JNIEXPORT jlong JNICALL
//Java_com_maniu_gifframework_GifHandler_loadGif4(JNIEnv *env, jclass clazz, jstring path_) {
//    jclass tempClass = env->FindClass( "java/lang/String");
////    创建弱全局引用
//    stringClass = static_cast<jclass>(env->NewWeakGlobalRef(tempClass));
////    合适的地方销毁
//    env ->DeleteLocalRef( stringClass);
//    /* Is the global reference created successfully? */
//    if (stringClass == NULL) {
//        return NULL; /* out of memory exception thrown */
//    }
//    return -1;
//}
//
