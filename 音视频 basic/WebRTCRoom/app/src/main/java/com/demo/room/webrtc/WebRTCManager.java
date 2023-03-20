package com.demo.room.webrtc;

import com.demo.room.ChatRoomActivity;
import com.demo.room.MainActivity;
import com.demo.room.webrtc.peersonnction.PeerConnectionManager;
import com.demo.room.webrtc.socket.WebSocketManager;

import org.webrtc.EglBase;

/**
 * 管理 WebSocketManager 和 PeerConnectionManager
 * WebSocketManager：管理房间服务器
 * PeerConnectionManager：管理打洞服务器
 */
public class WebRTCManager {

    private WebSocketManager webSocketManager;
    private PeerConnectionManager peerConnectionManager;
    private String roomId = "";
    private static final WebRTCManager ourInstance = new WebRTCManager();

    public static WebRTCManager getInstance() {
        return ourInstance;
    }

    private WebRTCManager() {

    }

    public void connect(MainActivity activity, String roomId) {
        this.roomId = roomId;
        peerConnectionManager = new PeerConnectionManager();
        webSocketManager = new WebSocketManager(activity, peerConnectionManager);
        // socket ws = http,  wss = https
        webSocketManager.connect("wss://8.210.234.39/wss");
    }

    public void joinRoom(ChatRoomActivity chatRoomActivity, EglBase eglBase) {
        peerConnectionManager.initContext(chatRoomActivity, eglBase);
        webSocketManager.joinRoom(roomId);
    }

}
