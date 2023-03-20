#ifndef BILIRTMP_JAVACALLHELPER_H
#define BILIRTMP_JAVACALLHELPER_H

#include <jni.h>
// 标记线程 因为子线程需要attach
#define THREAD_MAIN 1
#define THREAD_CHILD 2

class JavaCallHelper {
public:
    JavaCallHelper(JavaVM *_javaVM, JNIEnv *_env, jobject &_jobj);

    void postH264(char *data, int length, int thread = THREAD_MAIN);

public:
    JavaVM *javaVM;
    JNIEnv *env;
    jobject jobj;
    jmethodID jmid_postData;
};

#endif //BILIRTMP_JAVACALLHELPER_H
