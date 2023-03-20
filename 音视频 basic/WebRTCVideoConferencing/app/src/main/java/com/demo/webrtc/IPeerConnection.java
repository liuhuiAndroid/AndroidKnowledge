package com.demo.webrtc;

public interface IPeerConnection {

    // 加入房间
    void newConnection(String remoteIp);
    // 接收远端数据
    void remoteReceiveData(String remoteIp, byte[] data);

}
