//
// Created by sec on 2022/11/11.
//

#ifndef MUSICPLAYER_MUSICVIDEO_H
#define MUSICPLAYER_MUSICVIDEO_H

#include "MusicPlayStatus.h"
#include "MusicCallJava.h"
#include "MusicQueue.h"
#include "MusicAudio.h"

extern "C"
{
#include "libavcodec/avcodec.h"
#include "libavutil/time.h"
#include <libavutil/imgutils.h>
#include <libswscale/swscale.h>
}

class MusicVideo {
public:
    MusicQueue *queue = NULL;
    int streamIndex = -1;
    AVCodecContext *avCodecContext = NULL;
    AVCodecParameters *codecpar = NULL;
    MusicPlayStatus *playStatus = NULL;
    MusicCallJava *wlCallJava = NULL;
    pthread_mutex_t codecMutex;
    pthread_t thread_play;
    double clock = 0;
    // 与音频的差值
    double delayTime = 0;
    // 默认休眠时间
    double defaultDelayTime = 0.04;
    MusicAudio *audio = NULL;
    AVRational time_base;
public:
    MusicVideo(MusicPlayStatus *playStatus, MusicCallJava *wlCallJava);
    ~MusicVideo();
    void play();
    double getDelayTime(double diff);
    double getFrameDiffTime(AVFrame *avFrame);
};

#endif //MUSICPLAYER_MUSICVIDEO_H
