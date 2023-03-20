#ifndef BILIRTMP_AUDIOCHANNEL_H
#define BILIRTMP_AUDIOCHANNEL_H

#include <faac.h>
#include "librtmp/rtmp.h"

typedef void (*Callback)(RTMPPacket *);
class AudioChannel {
public:
    AudioChannel();
    ~AudioChannel();
    void openCodec(int sampleRate, int channels);
    // 编码函数
    void encode(int32_t *data, int len);
    // 头帧
    RTMPPacket *getAudioConfig();
    void setCallback(Callback callback) {
        this->callback = callback;
    }
    int getInputByteNum() {
        return inputByteNum;
    }

public:
    Callback callback;
    faacEncHandle codec = 0;
    /**
     * 音频压缩成aac后最大数据量
     */
    unsigned long maxOutputBytes;
    /**
     * 输出的数据
     */
    unsigned char *outputBuffer = 0;
    // 输入容器的大小
    unsigned long inputByteNum;
};

#endif //BILIRTMP_AUDIOCHANNEL_H
