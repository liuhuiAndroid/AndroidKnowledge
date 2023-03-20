//
// Created by sec on 2022/11/6.
//

#ifndef MUSICPLAYER_MUSICQUEUE_H
#define MUSICPLAYER_MUSICQUEUE_H

#include "queue"
#include "pthread.h"
#include "AndroidLog.h"
#include "MusicPlayStatus.h"

extern "C"
{
#include "libavcodec/avcodec.h"
}

class MusicQueue {

public:
    std::queue<AVPacket *> queuePacket;
    pthread_mutex_t mutexPacket;
    pthread_cond_t condPacket;
    MusicPlayStatus *playStatus = NULL;

public:
    MusicQueue(MusicPlayStatus *playStatus);
    ~MusicQueue();
    int putAvPacket(AVPacket *packet);
    int getAvPacket(AVPacket *packet);
    int getQueueSize();

    void clearAvPacket();
};

#endif //MUSICPLAYER_MUSICQUEUE_H
