#include <jni.h>
#include <string>
#include <android/log.h>

extern "C" {
#include "libavcodec/avcodec.h"
#include "libavformat/avformat.h"
#include "libswscale/swscale.h"
#include "libavutil/imgutils.h"
#include "libswresample/swresample.h"
}

#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, "Seckill", __VA_ARGS__)

extern "C" JNIEXPORT jstring JNICALL
Java_com_music_player_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = avcodec_configuration();
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_com_music_player_MainActivity_playSound(JNIEnv *env, jobject clazz, jstring input_) {
    const char *input = env->GetStringUTFChars(input_, 0);
    // 注册所有的组件
    av_register_all();
    // 初始化网络模块
    avformat_network_init();
    // 初始化总上下文
    AVFormatContext *pFormatCtx = avformat_alloc_context();
    if (avformat_open_input(&pFormatCtx, input, NULL, NULL) != 0) {
        LOGE("%s", "打开输入视频文件失败");
        return;
    }
    if (avformat_find_stream_info(pFormatCtx, NULL) < 0) {
        LOGE("%s", "获取视频信息失败");
        return;
    }
    // 找音频索引
    int audio_stream_idx = av_find_best_stream(pFormatCtx, AVMEDIA_TYPE_AUDIO, -1, -1, NULL, 0);
//    int audio_stream_idx = -1;
//    for (int i = 0; i < pFormatCtx->nb_streams; ++i) {
//        if (pFormatCtx->streams[i]->codec->codec_type == AVMEDIA_TYPE_AUDIO) {
//            LOGE("  找到音频id %d", pFormatCtx->streams[i]->codec->codec_type);
//            audio_stream_idx = i;
//            break;
//        }
//    }
    // 获取解码器上下文
    AVCodecContext *pCodecCtx = pFormatCtx->streams[audio_stream_idx]->codec;
    // 获取音频解码器
    AVCodec *pCodex = avcodec_find_decoder(pCodecCtx->codec_id);
    // 打开解码器
    if (avcodec_open2(pCodecCtx, pCodex, NULL) < 0) {
        return;
    }
    // 压缩数据aac
    AVPacket *packet = (AVPacket *) av_malloc(sizeof(AVPacket));
    // 申请AVFrame，装解码后的pcm数据
    AVFrame *frame = av_frame_alloc();
    // 获取音频通道数
    int out_channer_nb = av_get_channel_layout_nb_channels(AV_CH_LAYOUT_STEREO);

    // 获取音频重采样上下文
    SwrContext *swrContext = swr_alloc();
    uint64_t out_ch_layout = AV_CH_LAYOUT_STEREO;
    enum AVSampleFormat out_format = AV_SAMPLE_FMT_S16;
    int out_sample_rate = pCodecCtx->sample_rate;
    // struct SwrContext *s, 重采样上下文
    // int64_t out_ch_layout, 输出的layout, 如：5.1声道…
    // enum AVSampleFormat out_sample_fmt,输出的样本格式。Float, S16, S24
    // int out_sample_rate, 输出的样本率。可以不变。
    // int64_t  in_ch_layout, 输入的layout。
    // enum AVSampleFormat  in_sample_fmt, 输入的样本格式。
    // int  in_sample_rate, 输入的样本率。
    // int log_offset, 日志，不用管，可直接传0
    // 输出的都是写死的，输入的是从数据中获取的
    swr_alloc_set_opts(swrContext, out_ch_layout, out_format, out_sample_rate,
                       pCodecCtx->channel_layout, pCodecCtx->sample_fmt, pCodecCtx->sample_rate,
                       0, NULL);
    // 初始化重采样上下文
    swr_init(swrContext);
    // 1s的pcm个数
    uint8_t *out_buffer = (uint8_t *) av_malloc(44100 * 2);
    // 反射的方式获取Java方法
    jclass mainActivity = env->GetObjectClass(clazz);
    jmethodID createTrack = env->GetMethodID(mainActivity, "createTrack", "(II)V");
    env->CallVoidMethod(clazz, createTrack, 44100, out_channer_nb);
    jmethodID audioWrite = env->GetMethodID(mainActivity, "playTrack", "([BI)V");
    int got_frame;
    // 读数据
    while (av_read_frame(pFormatCtx, packet) >= 0) {
        // 判断是音频数据
        if (packet->stream_index == audio_stream_idx) {
            // 子线程解码，不建议这样使用，过时
            // AVCodecContext *avctx,
            // AVFrame *frame, 解压数据
            // int *got_frame_ptr, 解码后的长度
            // const AVPacket *avpkt 压缩数据
            avcodec_decode_audio4(pCodecCtx, frame, &got_frame, packet);
            if (got_frame >= 0) {
                // 音频重采样
                // struct SwrContext *s, 重采样上下文
                // uint8_t **out,       输出数据
                // int out_count,       输出数据长度
                // const uint8_t **in , 输入数据
                // int in_count         输入数据长度
                swr_convert(swrContext, &out_buffer, 44100 * 2,
                            (const uint8_t **) (frame->data), frame->nb_samples);
                // 获取AVFrame数据的实际大小
                // int *linesize,
                // int nb_channels, 通道数
                // int nb_samples, 采样位数
                // enum AVSampleFormat sample_fmt, 采样率 16位 TODO: AV_SAMPLE_FMT_S16 = pCodecCtx->sample_fmt?
                // int align 字节对齐
                int size = av_samples_get_buffer_size(NULL, out_channer_nb, frame->nb_samples,
                                                      AV_SAMPLE_FMT_S16, 1);
                // 创建java的字节数组
                jbyteArray audio_sample_array = env->NewByteArray(size);
                // 将C里面的字节数组转换成java的字节数组
                env->SetByteArrayRegion(audio_sample_array, 0, size,
                                        reinterpret_cast<const jbyte *>(out_buffer));
                // 回调应用层
                env->CallVoidMethod(clazz, audioWrite, audio_sample_array, size);
                // 弊端：内存抖动
                env->DeleteLocalRef(audio_sample_array);
            }
        }
    }
    // 释放
    av_frame_free(&frame);
    av_free(packet);
    swr_free(&swrContext);
    avformat_network_deinit();
    avcodec_close(pCodecCtx);
    avformat_close_input(&pFormatCtx);
    env->ReleaseStringUTFChars(input_, input);
}