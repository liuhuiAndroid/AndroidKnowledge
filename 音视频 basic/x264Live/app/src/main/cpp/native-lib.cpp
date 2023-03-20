#include <jni.h>
#include <string>
#include <android/log.h>
#include <malloc.h>

extern "C" {
#include "librtmp/rtmp.h"
}

#include "safe_queue.h"
#include "VideoChannel.h"
#include "AudioChannel.h"
#include "seclog.h"
#include "JavaCallHelper.h"

VideoChannel *videoChannel = 0;
AudioChannel *audioChannel = 0;
JavaCallHelper *helper = 0;
int isStart = 0;
// 记录子线程的对象
pthread_t pid;
// 推流标志位
int readyPushing = 0;
// 阻塞式队列
SafeQueue<RTMPPacket *> packets;
uint32_t start_time;
RTMP *rtmp = 0;

//虚拟机的引用
JavaVM *javaVM = 0;

void callBack(RTMPPacket *packet) {
    if (packet) {
        if (packets.size() > 50) {
            packets.clear();
        }
        packet->m_nTimeStamp = RTMP_GetTime() - start_time;
        packets.push(packet);
    }
}

//RTMPPacket释放
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    javaVM = vm;
    LOGE("保存虚拟机的引用");
    return JNI_VERSION_1_4;
}

void releasePackets(RTMPPacket *&packet) {
    if (packet) {
        RTMPPacket_Free(packet);
        delete packet;
        packet = 0;
    }
}

void *start(void *args) {
    char *url = static_cast<char *>(args);
    do {
        rtmp = RTMP_Alloc();
        if (!rtmp) {
            LOGE("rtmp创建失败");
            break;
        }
        RTMP_Init(rtmp);
        //设置超时时间 5s
        rtmp->Link.timeout = 5;
        int ret = RTMP_SetupURL(rtmp, url);
        if (!ret) {
            LOGE("rtmp设置地址失败:%s", url);
            break;
        }
        //开启输出模式
        RTMP_EnableWrite(rtmp);
        ret = RTMP_Connect(rtmp, 0);
        if (!ret) {
            LOGE("rtmp连接地址失败:%s", url);
            break;
        }
        ret = RTMP_ConnectStream(rtmp, 0);
        LOGE("rtmp连接成功----------->:%s", url);
        if (!ret) {
            LOGE("rtmp连接流失败:%s", url);
            break;
        }
        //准备好了 可以开始推流了
        readyPushing = 1;
        //记录一个开始推流的时间
        start_time = RTMP_GetTime();
        packets.setWork(1);
        RTMPPacket *packet = 0;

        RTMPPacket *audioHeader = audioChannel->getAudioConfig();
        callBack(audioHeader);
        //循环从队列取包 然后发送
        while (isStart) {
            packets.pop(packet);
            if (!isStart) {
                break;
            }
            if (!packet) {
                continue;
            }
            // 给rtmp的流id
            packet->m_nInfoField2 = rtmp->m_stream_id;
            //发送包 1:加入队列发送
            ret = RTMP_SendPacket(rtmp, packet, 1);
            releasePackets(packet);
            if (!ret) {
                LOGE("发送数据失败");
                break;
            }
        }
        releasePackets(packet);
    } while (0);
    if (rtmp) {
        RTMP_Close(rtmp);
        RTMP_Free(rtmp);
    }
    delete url;
    return 0;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_demo_x264live_live_LivePusher_native_1init(JNIEnv *env, jobject thiz) {
    // 实例化编码层
    videoChannel = new VideoChannel;
    helper = new JavaCallHelper(javaVM, env, thiz);
    videoChannel->setVideoCallback(callBack);
    videoChannel->javaCallHelper = helper;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_demo_x264live_live_LivePusher_native_1setVideoEncInfo(JNIEnv *env, jobject thiz,
                                                               jint width, jint height, jint fps,
                                                               jint bitrate) {
    if (videoChannel) {
        videoChannel->setVideoEncInfo(width, height, fps, bitrate);
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_demo_x264live_live_LivePusher_native_1start(JNIEnv *env, jobject thiz, jstring path_) {
    // 链接rtmp服务器   子线程
    if (isStart) {
        return;
    }
    const char *path = env->GetStringUTFChars(path_, 0);
    char *url = new char[strlen(path) + 1];
    strcpy(url, path);
    // 开始直播
    isStart = 1;
    // 开启子线程连接B站服务器
    pthread_create(&pid, 0, start, url);
    env->ReleaseStringUTFChars(path_, path);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_demo_x264live_live_LivePusher_native_1pushVideo(JNIEnv *env, jobject thiz,
                                                         jbyteArray data_) {
    if (!videoChannel || !readyPushing) {
        return;
    }
    jbyte *data = env->GetByteArrayElements(data_, NULL);
    videoChannel->encodeData(data);
    env->ReleaseByteArrayElements(data_, data, 0);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_demo_x264live_live_LivePusher_native_1stop(JNIEnv *env, jobject thiz) {

}

extern "C"
JNIEXPORT void JNICALL
Java_com_demo_x264live_live_LivePusher_native_1release(JNIEnv *env, jobject thiz) {
    if (rtmp) {
        RTMP_Close(rtmp);
        RTMP_Free(rtmp);
        rtmp = 0;
    }
    if (videoChannel) {
        delete (videoChannel);
        videoChannel = 0;
    }
    if (helper) {
        delete (helper);
        helper = 0;
    }
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_demo_x264live_live_LivePusher_initAudioEnc(JNIEnv *env, jobject thiz, jint sample_rate,
                                                    jint channels) {
    //    初始化faac编码  音频
    audioChannel = new AudioChannel();
    audioChannel->setCallback(callBack);
    audioChannel->openCodec(sample_rate, channels);
    return audioChannel->getInputByteNum();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_demo_x264live_live_LivePusher_nativeSendAudio(JNIEnv *env, jobject thiz, jbyteArray buffer,
                                                       jint len) {
    if (!audioChannel || !readyPushing) {
        return;
    }
    jbyte *data = env->GetByteArrayElements(buffer, 0);
    audioChannel->encode(reinterpret_cast<int32_t *>(data), len);
    env->ReleaseByteArrayElements(buffer, data, 0);
}