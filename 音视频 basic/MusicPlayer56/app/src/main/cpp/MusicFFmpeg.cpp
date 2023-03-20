//
// Created by sec on 2022/11/6.
//

#include "MusicFFmpeg.h"

// C函数无法访问CPP函数的任何变量，此函数是C函数
void *decodeFFmpeg(void *data) {
    // C函数转换成CPP函数
    MusicFFmpeg *musicFFmpeg = (MusicFFmpeg *) data;
    musicFFmpeg->decodeFFmpegThread();
    // 退出当前线程
    pthread_exit(&musicFFmpeg->decodeThread);
}

void MusicFFmpeg::prepared() {
    // 第一个参数：句柄
    // 指定decodeFFmpeg函数运行在子线程
    pthread_create(&decodeThread, NULL, decodeFFmpeg, this);
}

// 死循环执行解码
void MusicFFmpeg::decodeFFmpegThread() {
    // 注册所有的组件
    av_register_all();
    // 初始化网络模块
    avformat_network_init();
    // 初始化总上下文
    pFormatCtx = avformat_alloc_context();
    if (avformat_open_input(&pFormatCtx, url, NULL, NULL) != 0) {
        if (LOG_DEBUG) {
            LOGE("can not open url :%s", url)
        }
        return;
    }
    if (avformat_find_stream_info(pFormatCtx, NULL) < 0) {
        if (LOG_DEBUG) {
            LOGE("can not find streams from %s", url)
        }
        return;
    }
    // 解封装，找音频索引
    for (int i = 0; i < pFormatCtx->nb_streams; i++) {
        if (pFormatCtx->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
            if (audio == NULL) {
                audio = new MusicAudio(playStatus, pFormatCtx->streams[i]->codecpar->sample_rate,
                                       callJava);
                audio->streamIndex = i;
                // 获取解码器上下文
                audio->codecpar = pFormatCtx->streams[i]->codecpar;
                // duration 微妙->秒
                // DTS（Decoding Time Stamp）：即解码时间戳，这个时间戳的意义在于告诉播放器该在什么时候解码这一帧的数据。
                // PTS（Presentation Time Stamp）：即显示时间戳，这个时间戳用来告诉播放器该在什么时候显示这一帧的数据。
                audio->duration = pFormatCtx->duration / AV_TIME_BASE;
                audio->time_base = pFormatCtx->streams[i]->time_base;
                duration = audio->duration;
            }
        }
    }

    // 获取音频解码器
    AVCodec *dec = avcodec_find_decoder(audio->codecpar->codec_id);
    if (!dec) {
        if (LOG_DEBUG) {
            LOGE("can not find decoder")
        }
        return;
    }

    // 获取解码器上下文
    audio->avCodecContext = avcodec_alloc_context3(dec);
    if (!audio->avCodecContext) {
        if (LOG_DEBUG) {
            LOGE("can not alloc new decodecctx");
        }
        return;
    }

    // 把音乐编码参数取出赋值给codecpar
    if (avcodec_parameters_to_context(audio->avCodecContext, audio->codecpar) < 0) {
        if (LOG_DEBUG) {
            LOGE("can not fill decodecctx");
        }
        return;
    }

    // 打开解码器
    if (avcodec_open2(audio->avCodecContext, dec, 0) != 0) {
        if (LOG_DEBUG) {
            LOGE("cant not open audio strames");
        }
        return;
    }
    callJava->onCallPrepared(CHILD_THREAD);
}

// 解码
void MusicFFmpeg::start() {
    if (audio == NULL) {
        if (LOG_DEBUG) {
            LOGE("audio is null")
            return;
        }
    }
    audio->play();
    int count = 0;
    while (playStatus != NULL && !playStatus->exit) {
        if (playStatus->seek) {
            continue;
        }
        // 控制下队列大小
        if (audio->queue->getQueueSize() > 40) {
            continue;
        }
        AVPacket *avPacket = av_packet_alloc();
        // 读数据
        if (av_read_frame(pFormatCtx, avPacket) == 0) {
            if (avPacket->stream_index == audio->streamIndex) {
                //解码操作
                count++;
                audio->queue->putAvPacket(avPacket);
            } else {
                av_packet_free(&avPacket);
                av_free(avPacket);
            }
        } else {
            av_packet_free(&avPacket);
            av_free(avPacket);
            while (playStatus != NULL && !playStatus->exit) {
                if (audio->queue->getQueueSize() > 0) {
                    continue;
                } else {
                    playStatus->exit = true;
                    break;
                }
            }
        }
    }
}

MusicFFmpeg::MusicFFmpeg(MusicPlayStatus *playStatus, MusicCallJava *callJava, const char *url) {
    this->playStatus = playStatus;
    this->callJava = callJava;
    this->url = url;
    // 实例化锁
    pthread_mutex_init(&seek_mutex, NULL);
    pthread_mutex_init(&init_mutex, NULL);
}

MusicFFmpeg::~MusicFFmpeg() {
    pthread_mutex_destroy(&seek_mutex);
    pthread_mutex_destroy(&init_mutex);
}

void MusicFFmpeg::seek(jint position) {
    if (duration <= 0) {
        return;
    }
    if (position >= 0 && position <= duration) {
        if (audio != NULL) {
            playStatus->seek = true;
            audio->queue->clearAvPacket();
            audio->clock = 0;
            audio->last_time = 0;
            // seek的过程中加锁
            pthread_mutex_lock(&seek_mutex);
            // 微妙转换成秒
            int64_t rel = position * AV_TIME_BASE;
            // 真正seek的方法
            avformat_seek_file(pFormatCtx, -1, INT64_MIN, rel, INT64_MAX, 0);
            pthread_mutex_unlock(&seek_mutex);
            playStatus->seek = false;
        }
    }
}

void MusicFFmpeg::resume() {
    if (audio != NULL) {
        audio->resume();
    }
}

void MusicFFmpeg::pause() {
    if (audio != NULL) {
        audio->pause();
    }
}

void MusicFFmpeg::setMute(jint mute) {
    if (audio != NULL) {
        audio->setMute(mute);
    }
}

void MusicFFmpeg::setVolume(jint percent) {
    if (audio != NULL) {
        audio->setVolume(percent);
    }
}

void MusicFFmpeg::setSpeed(jfloat speed) {
    if (audio != NULL) {
        audio->setSpeed(speed);
    }

}

void MusicFFmpeg::setPitch(jfloat pitch) {
    if (audio != NULL) {
        audio->setPitch(pitch);
    }
}

void MusicFFmpeg::release() {
    if (LOG_DEBUG) {
        LOGE("开始释放Ffmpeg")
    }
    playStatus->exit = true;
    int sleepCount = 0;
    pthread_mutex_lock(&init_mutex);
//    while (!exit) {
//        if (sleepCount > 1000) {
//            exit = true;
//        }
//        if (LOG_DEBUG) {
//            LOGE("wait ffmpeg  exit %d", sleepCount)
//        }
//        sleepCount++;
//        //暂停10毫秒
//        av_usleep(1000 * 10);
//    }

    if (audio != NULL) {
        audio->release();
        delete (audio);
        audio = NULL;
    }

    if (LOG_DEBUG) {
        LOGE("释放 封装格式上下文")
    }
    if (pFormatCtx != NULL) {
        avformat_close_input(&pFormatCtx);
        avformat_free_context(pFormatCtx);
        pFormatCtx = NULL;
    }
    if (LOG_DEBUG) {
        LOGE("释放 callJava")
    }
    if (callJava != NULL) {
        callJava = NULL;
    }
    if (LOG_DEBUG) {
        LOGE("释放 playStatus")
    }
    if (playStatus != NULL) {
        playStatus = NULL;
    }
    pthread_mutex_unlock(&init_mutex);
}
