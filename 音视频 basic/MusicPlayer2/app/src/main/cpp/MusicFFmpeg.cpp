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
                audio = new MusicAudio(playStatus, pFormatCtx->streams[i]->codecpar->sample_rate);
                audio->streamIndex = i;
                // 获取解码器上下文
                audio->codecpar = pFormatCtx->streams[i]->codecpar;
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
        AVPacket *avPacket = av_packet_alloc();
        // 读数据
        if (av_read_frame(pFormatCtx, avPacket) == 0) {
            if (avPacket->stream_index == audio->streamIndex) {
                //解码操作
                count++;
                if (LOG_DEBUG) {
                    LOGE("解码第 %d 帧", count);
                }
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
    if (LOG_DEBUG) {
        LOGD("解码完成")
    }
}

MusicFFmpeg::MusicFFmpeg(MusicPlayStatus *playStatus, MusicCallJava *callJava, const char *url) {
    this->playStatus = playStatus;
    this->callJava = callJava;
    this->url = url;
}
