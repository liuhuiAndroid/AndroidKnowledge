package com.example.chat;

import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;

//音视频通话客户端
public class SocketLive {
    private static final String TAG = "SocketLive";
    private SocketCallback socketCallback;
    MyWebSocketClient myWebSocketClient;
    public SocketLive(SocketCallback socketCallback ) {
        this.socketCallback = socketCallback;
    }
    public void start() {
        try {
            URI url = new URI("ws://192.168.18.52:40002");
            myWebSocketClient = new MyWebSocketClient(url);
            myWebSocketClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendData(byte[] bytes) {
        if (myWebSocketClient!=null&&(myWebSocketClient.isOpen())) {
            myWebSocketClient.send(bytes);
        }
    }


    private class MyWebSocketClient extends WebSocketClient {

        public MyWebSocketClient(URI serverURI) {
            super(serverURI);
        }

        @Override
        public void onOpen(ServerHandshake serverHandshake) {
            Log.i(TAG, "打开 socket  onOpen: ");
        }

        @Override
        public void onMessage(String s) {
        }
        @Override
        public void onMessage(ByteBuffer bytes) {
            Log.i(TAG, "消息长度  : " + bytes.remaining());
            byte[] buf = new byte[bytes.remaining()];
            bytes.get(buf);
            socketCallback.callBack(buf);
        }

        @Override
        public void onClose(int i, String s, boolean b) {
            Log.i(TAG, "onClose: ");
        }

        @Override
        public void onError(Exception e) {
            Log.i(TAG, "onError: ");
        }
    }

    public interface SocketCallback {
        void callBack(byte[] data);
    }
}
