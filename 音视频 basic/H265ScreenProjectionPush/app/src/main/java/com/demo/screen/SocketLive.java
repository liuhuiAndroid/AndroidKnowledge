package com.demo.screen;

import android.media.projection.MediaProjection;
import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class SocketLive {

    private static final String TAG = "SocketLive";
    private WebSocket webSocket;
    int port;
    CodecLiveH265 codecLiveH265;

    public SocketLive(int port) {
        this.port = port;
    }

    public void start(MediaProjection mediaProjection) {
        webSocketServer.start();
        codecLiveH265 = new CodecLiveH265(this, mediaProjection);
        codecLiveH265.startLive();
    }

    public void close() {
        try {
            webSocket.close();
            webSocketServer.stop();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private WebSocketServer webSocketServer = new WebSocketServer(new InetSocketAddress(12001)) {
        @Override
        public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
            SocketLive.this.webSocket = webSocket;
        }

        @Override
        public void onClose(WebSocket webSocket, int i, String s, boolean b) {
            Log.i(TAG, "onClose: 关闭 socket");
        }

        @Override
        public void onMessage(WebSocket webSocket, String s) {
        }

        @Override
        public void onError(WebSocket webSocket, Exception e) {
            Log.i(TAG, "onError: " + e.toString());
        }

        @Override
        public void onStart() {

        }
    };

    public void sendData(byte[] bytes) {
        if (webSocket != null && webSocket.isOpen()) {
            webSocket.send(bytes);
        }
    }
}
