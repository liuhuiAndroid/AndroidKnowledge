#include <jni.h>
#include <string>
#include <android/log.h>
#include <android/native_window_jni.h>

extern "C" {
#include "libavcodec/avcodec.h"
#include "libavformat/avformat.h"
#include "libswscale/swscale.h"
#include "libavutil/imgutils.h"
#include "libswresample/swresample.h"
#include "libavutil/time.h"
}

#define LOGD(...) __android_log_print(ANDROID_LOG_INFO,"David",__VA_ARGS__)

extern "C" JNIEXPORT jstring JNICALL
Java_com_video_player_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    // 获取配置信息
    std::string hello = avcodec_configuration();
    return env->NewStringUTF(hello.c_str());
}

static AVFormatContext *avFormatContext;
static AVCodecContext *avCodecContext;
AVCodec *avCodec;
ANativeWindow *nativeWindow;
ANativeWindow_Buffer windowBuffer;
static AVPacket *avPacket;
static AVFrame *avFrame, *rgbFrame;
uint8_t *outbuffer;
struct SwsContext *swsContext;

extern "C"
JNIEXPORT jint JNICALL
Java_com_video_player_MainActivity_play(JNIEnv *env, jobject thiz, jstring url_, jobject surface) {
    // 把java路径变成cpp字符串，注意释放：env->ReleaseStringUTFChars(url_, url);
    const char *url = env->GetStringUTFChars(url_, 0);
    // 注册所有的组件
    avcodec_register_all();
    // 实例化解码上下文：注意释放：avformat_free_context(avFormatContext);
    avFormatContext = avformat_alloc_context();
    // 打开视频文件
    if (avformat_open_input(&avFormatContext, url, NULL, NULL) != 0) {
        LOGD("Couldn't open input stream.\n");
        return -1;
    }
    LOGD("打开视频成功.\n");
    // 获取视频文件信息
    if (avformat_find_stream_info(avFormatContext, NULL) < 0) {
        LOGD("Couldn't find stream information.\n");
        return -1;
    }
    // 解封装：找视频流索引
//    int videoindex = -1;
//    // nb_streams：Number of elements in AVFormatContext.streams.
//    for (int i = 0; i < avFormatContext->nb_streams; i++) {
//        if (avFormatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
//            videoindex = i;
//            break;
//        }
//    }
    // 解封装：找视频流索引 //// 方法二
    int videoindex = av_find_best_stream(avFormatContext, AVMediaType::AVMEDIA_TYPE_VIDEO, -1, -1,
                                         NULL, 0);
    // 视频流索引
    if (videoindex == -1) {
        LOGD("Couldn't find a video stream.\n");
        return -1;
    }
    LOGD("找到了视频流\n");
    // 解码器上下文
    avCodecContext = avFormatContext->streams[videoindex]->codec;
    // 解码器
    avCodec = avcodec_find_decoder(avCodecContext->codec_id);

    //打开解码器
    if (avcodec_open2(avCodecContext, avCodec, NULL) < 0) {
        LOGD("Couldn't open codec.\n");
        return -1;
    }
    LOGD("打开解码器成功\n");

    // 渲染
    if (nativeWindow) {
        ANativeWindow_release(nativeWindow);
        nativeWindow = 0;
    }
    // 输入surface，输出native window
    nativeWindow = ANativeWindow_fromSurface(env, surface);
    if (0 == nativeWindow) {
        LOGD("Couldn't get native window from surface.\n");
        return -1;
    }

    // 三个容器
    avPacket = av_packet_alloc();   //压缩数据
    avFrame = av_frame_alloc();     //原始数据
    rgbFrame = av_frame_alloc();    //转换数据
    int width = avCodecContext->width;
    int height = avCodecContext->height;

    int numBytes = av_image_get_buffer_size(AV_PIX_FMT_RGBA, width, height, 1);
    LOGD("计算解码后的rgb %d\n", numBytes);
    // 实例化一个输入缓冲区
    outbuffer = (uint8_t *) av_malloc(numBytes * sizeof(uint8_t));
    // 将缓冲区设置给rgbFrame
    av_image_fill_arrays(rgbFrame->data, rgbFrame->linesize, outbuffer, AV_PIX_FMT_RGBA,
                         width, height, 1);

    // 转换器上下文
    swsContext = sws_getContext(width, height, avCodecContext->pix_fmt,
                                width, height, AV_PIX_FMT_RGBA, SWS_BICUBIC, NULL, NULL, NULL);
    // 给window缓冲区设置大小：ANativeWindow_setBuffersGeometry
    if (0 >
        ANativeWindow_setBuffersGeometry(nativeWindow, width, height, WINDOW_FORMAT_RGBA_8888)) {
        LOGD("Couldn't set buffers geometry.\n");
        ANativeWindow_release(nativeWindow);
        return -1;
    }
    LOGD("ANativeWindow_setBuffersGeometry成功\n");

    while (av_read_frame(avFormatContext, avPacket) >= 0) {
        // 视频数据
        if (avPacket->stream_index == videoindex) {
            // 设置压缩数据：avcodec_send_packet with avcodec_receive_frame
            int ret = avcodec_send_packet(avCodecContext, avPacket);
            if (ret < 0 && ret != AVERROR(EAGAIN) && ret != AVERROR_EOF) {
                LOGD("解码出错");
                return -1;
            }
            // 解码得到原始数据
            ret = avcodec_receive_frame(avCodecContext, avFrame);
            if (ret == AVERROR(EAGAIN)) { // 继续解码
                continue;
            } else if (ret < 0) {
                break;
            }
            // 转换原始数据
            sws_scale(swsContext, avFrame->data, avFrame->linesize, 0, avCodecContext->height,
                      rgbFrame->data, rgbFrame->linesize);
            // 把window对象转换成缓冲区，设置数据，渲染是基于内存的操作
            if (ANativeWindow_lock(nativeWindow, &windowBuffer, NULL) < 0) {
                LOGD("cannot lock window");
            } else {
                // 将图像绘制到界面上，注意这里pFrameRGBA一行的像素和windowBuffer一行的像素长度可能不一致
                // 需要转换好，否则可能花屏
                // 待显示的缓冲区
                uint8_t *dstData = (uint8_t *) windowBuffer.bits;
                for (int h = 0; h < height; h++) {
                    // 内存拷贝，一个像素4个字节所以*4
                    memcpy(dstData + h * windowBuffer.stride * 4,
                           outbuffer + h * rgbFrame->linesize[0],
                           rgbFrame->linesize[0]);
                }
                switch (avFrame->pict_type) {
                    case AV_PICTURE_TYPE_I:
                        LOGD("I");
                        break;
                    case AV_PICTURE_TYPE_P:
                        LOGD("P");
                        break;
                    case AV_PICTURE_TYPE_B:
                        LOGD("B");
                        break;
                    default:;
                        break;
                }
            }
            av_usleep(1000 * 33);
            ANativeWindow_unlockAndPost(nativeWindow);
        }
    }

    avformat_free_context(avFormatContext);
    env->ReleaseStringUTFChars(url_, url);
    return -1;
}