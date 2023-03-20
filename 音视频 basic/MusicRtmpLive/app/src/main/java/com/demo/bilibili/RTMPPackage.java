package com.demo.bilibili;

public class RTMPPackage {

    // 帧数据
    private byte[] buffer;
    // 时间戳
    private long tms;
    // 视频包 音频包
    private int type;
    public static final int RTMP_PACKET_TYPE_AUDIO_DATA = 2;
    public static final int RTMP_PACKET_TYPE_AUDIO_HEAD = 1;
    public static final int RTMP_PACKET_TYPE_VIDEO = 0;

    public RTMPPackage() {
    }

    public RTMPPackage(byte[] buffer, long tms) {
        this.buffer = buffer;
        this.tms = tms;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public long getTms() {
        return tms;
    }

    public void setTms(long tms) {
        this.tms = tms;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
