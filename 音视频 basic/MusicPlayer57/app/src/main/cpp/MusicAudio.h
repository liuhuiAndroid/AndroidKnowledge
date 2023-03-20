//
// Created by sec on 2022/11/6.
//

#ifndef MUSICPLAYER_MUSICAUDIO_H
#define MUSICPLAYER_MUSICAUDIO_H

#include "MusicQueue.h"
#include "MusicPlayStatus.h"
#include "MusicCallJava.h"
#include "SoundTouch.h"

extern "C"
{
#include "libavcodec/avcodec.h"
#include <libswresample/swresample.h>
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>
}

using namespace soundtouch;

class MusicAudio {

public:
    int streamIndex = -1;
    AVCodecContext *avCodecContext = NULL;
    AVCodecParameters *codecpar = NULL;
    MusicQueue *queue = NULL;
    MusicPlayStatus *playStatus = NULL;

    pthread_t thread_play;
    AVPacket *avPacket = NULL;
    AVFrame *avFrame = NULL;
    int ret = 0;
    uint8_t *buffer = NULL;
    int data_size = 0;
    int sample_rate = 0;

    // 引擎对象
    SLObjectItf engineObject = NULL;
    // 引擎接口
    SLEngineItf engineEngine = NULL;

    //混音器
    SLObjectItf outputMixObject = NULL;
    SLEnvironmentalReverbItf outputMixEnvironmentalReverb = NULL;
    SLEnvironmentalReverbSettings reverbSettings = SL_I3DL2_ENVIRONMENT_PRESET_STONECORRIDOR;

    //pcm
    SLObjectItf pcmPlayerObject = NULL;
    SLPlayItf pcmPlayerPlay = NULL;
    SLVolumeItf pcmVolumePlay = NULL;
    SLMuteSoloItf pcmMutePlay = NULL;

    //缓冲器队列接口
    SLAndroidSimpleBufferQueueItf pcmBufferQueue = NULL;

    // 音频总时长
    int duration = 0;
    // 时间单位
    AVRational time_base;
    // 当前frame时间
    double now_time;
    // 当前播放的时间，准确时间
    double clock;
    MusicCallJava *callJava = NULL;
    // 上一次调用时间
    double last_time;
    // 声道
    int mute = 2;


    // 倍速
    float speed = 1.0f;
    float pitch = 1.0f;
    SoundTouch *soundTouch = NULL;
    // 新的缓冲区
    SAMPLETYPE  *soundTouchBuffer = NULL;
    uint8_t *out_buffer = NULL;
    // 波处理完了没
    bool finished = true;
    // 新波的实际个数
    int nb = 0;
    int num = 0;
public:
    MusicAudio(MusicPlayStatus *playStatus, int sample_rate, MusicCallJava *callJava);

    ~MusicAudio();

    void play();

    int resampleAudio(void **pcmbuf);

    void initOpenSLES();

    int getCurrentSampleRateForOpensles(int sample_rate);

    void resume();

    void pause();

    void setMute(jint i);

    void setVolume(jint i);

    void setSpeed(jfloat d);

    void setPitch(jfloat d);

    int getSoundTouchData();

    void release();
};

#endif //MUSICPLAYER_MUSICAUDIO_H
