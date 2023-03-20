package com.demo.webrtc;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//音视频通话客户端
public class SocketLive {

    private ManiuSocketServer maniuSocketServer;
    private int port = 7033;
    // 房间地址
    private String[] urls = {"ws://192.168.3.46:", "ws://192.168.3.45:", "ws://192.168.18.53:"};
    private static final String TAG = SocketLive.class.getSimpleName();
    List<MyWebSocketClient> socketClientList = new ArrayList<>();

    public SocketLive(IPeerConnection peerConnection) {
        maniuSocketServer = new ManiuSocketServer(peerConnection);
        maniuSocketServer.start();
    }

    Timer timer;

    public void start(final Context context) {
        for (String value : urls) {
            if (value.contains(getLocalIpAddress(context))) {
                continue;
            }
            try {
                URI url = new URI(value + port);
                MyWebSocketClient myWebSocketClient = new MyWebSocketClient(value, url);
                myWebSocketClient.connect();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        // 定时器模拟进入房间时，其他人主动需要与新加入的人产生连接
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (String value : urls) {
                    if (value.contains(getLocalIpAddress(context))) {
                        continue;
                    }
                    boolean isSame = false;
                    for (MyWebSocketClient myWebSocketClient : socketClientList) {
                        if (value.contains(myWebSocketClient.getUrl())) {
                            isSame = true;
                            break;
                        }
                    }
                    if (isSame) {
                        continue;
                    }
                    try {
                        URI url = new URI(value + port);
                        MyWebSocketClient myWebSocketClient = new MyWebSocketClient(value, url);
                        myWebSocketClient.connect();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 3 * 1000, 5 * 1000);
    }

    public static String getLocalIpAddress(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int i = wifiInfo.getIpAddress();
            String ip = int2ip(i);
            Log.i(TAG, "本机ip: " + ip);
            return ip;
        } catch (Exception ex) {
            return null;
        }
    }

    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    public void sendData(byte[] bytes) {
        for (MyWebSocketClient myWebSocketClient : socketClientList) {
            if (myWebSocketClient.isOpen()) {
                myWebSocketClient.send(bytes);
            }
        }
    }

    // 编码层发送
    private class MyWebSocketClient extends WebSocketClient {

        String url;

        public MyWebSocketClient(String url, URI serverURI) {
            super(serverURI);
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        @Override
        public void onOpen(ServerHandshake serverHandshake) {
            socketClientList.add(this);
        }

        @Override
        public void onMessage(String s) {
        }

        @Override
        public void onMessage(ByteBuffer bytes) {

        }

        @Override
        public void onClose(int i, String s, boolean b) {
            Log.i(TAG, "onClose: ");
        }

        @Override
        public void onError(Exception e) {
            Log.i(TAG, "onError: ");
            if (socketClientList.contains(this)) {
                socketClientList.remove(this);
            }
        }
    }

    // 接收别人发送过来的消息
    class ManiuSocketServer extends WebSocketServer {

        private static final String TAG = "ManiuSocketServer";
        private IPeerConnection peerConnection;

        public ManiuSocketServer(IPeerConnection peerConnection) {
            super(new InetSocketAddress(port));
            this.peerConnection = peerConnection;
        }

        @Override
        public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
            this.peerConnection.newConnection
                    (webSocket.getRemoteSocketAddress().getAddress().getHostName());
        }

        @Override
        public void onClose(WebSocket webSocket, int i, String s, boolean b) {
            Log.i(TAG, "onClose: 关闭 socket");
        }

        @Override
        public void onMessage(WebSocket webSocket, String s) {

        }

        // 客户端发来的消息
        @Override
        public void onMessage(WebSocket conn, ByteBuffer bytes) {
            byte[] buf = new byte[bytes.remaining()];
            bytes.get(buf);
            peerConnection.remoteReceiveData(conn.getRemoteSocketAddress().getAddress().getHostName(), buf);
        }

        @Override
        public void onError(WebSocket webSocket, Exception e) {
        }

        @Override
        public void onStart() {

        }
    }

}
