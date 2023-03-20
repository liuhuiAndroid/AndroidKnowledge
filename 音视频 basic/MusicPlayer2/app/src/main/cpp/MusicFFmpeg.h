//
// Created by sec on 2022/11/6.
//

#ifndef MUSICPLAYER_MUSICFFMPEG_H
#define MUSICPLAYER_MUSICFFMPEG_H

#include "pthread.h"
#include "MusicCallJava.h"
#include "MusicPlayStatus.h"
#include "MusicAudio.h"

extern "C"
{
#include <libavformat/avformat.h>
}

class MusicFFmpeg {

public:
    MusicCallJava *callJava = NULL;
    const char *url = NULL;
    pthread_t decodeThread;
    AVFormatContext *pFormatCtx = NULL;
    MusicAudio *audio = NULL;
    MusicPlayStatus *playStatus = NULL;

public:
    MusicFFmpeg(MusicPlayStatus *playStatus, MusicCallJava *callJava, const char *url);

    ~MusicFFmpeg();

    // 初始化FFmpeg
    void prepared();

    void decodeFFmpegThread();

    void start();

};

#endif //MUSICPLAYER_MUSICFFMPEG_H
