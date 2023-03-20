//
// Created by sec on 2022/11/6.
//

#ifndef MUSICPLAYER_MUSICFFMPEG_H
#define MUSICPLAYER_MUSICFFMPEG_H

#include "pthread.h"
#include "MusicCallJava.h"
#include "MusicPlayStatus.h"
#include "MusicAudio.h"
#include "MusicVideo.h"

extern "C"
{
#include <libavutil/time.h>
#include <libavformat/avformat.h>
};

class MusicFFmpeg {

public:
    MusicCallJava *callJava = NULL;
    const char *url = NULL;
    pthread_t decodeThread;
    AVFormatContext *pFormatCtx = NULL;
    MusicAudio *audio = NULL;
    MusicVideo *video = NULL;
    MusicPlayStatus *playStatus = NULL;
    // 音频总时长，单位秒
    int duration = 0;
    pthread_mutex_t seek_mutex;
    pthread_mutex_t init_mutex;
    bool exit = false;
public:
    MusicFFmpeg(MusicPlayStatus *playStatus, MusicCallJava *callJava, const char *url);

    ~MusicFFmpeg();

    // 初始化FFmpeg
    void prepared();

    void decodeFFmpegThread();

    void start();

    void seek(jint i);

    void resume();

    void pause();

    void setMute(jint i);

    void setVolume(jint i);

    void setSpeed(jfloat d);

    void setPitch(jfloat d);

    void release();

    int getCodecContext(AVCodecParameters *codecpar, AVCodecContext **avCodecContext);
};

#endif //MUSICPLAYER_MUSICFFMPEG_H
