package com.demo.room.webrtc.peersonnction;

import android.util.Log;

import com.demo.room.ChatRoomActivity;
import com.demo.room.webrtc.socket.WebSocketManager;

import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.DataChannel;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RtpReceiver;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoDecoderFactory;
import org.webrtc.VideoEncoderFactory;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;
import org.webrtc.audio.JavaAudioDeviceModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PeerConnectionManager {

    private static final String TAG = PeerConnectionManager.class.getSimpleName();
    //    webrtc中
//    摄像头的数据获取 Camera2
//    音频源的获取   AudioTrak
    //视频源
    private String myId;
    //视频  true    false 音视频
    private boolean videoEnable;
    // 线程池
    private ExecutorService executor;

    private PeerConnectionFactory factory;

    private ChatRoomActivity context;
    private EglBase rootEglBase;

    private MediaStream mediaStream;
    WebSocketManager webSocketManager;
    private ArrayList<String> connectionIdArray;
    private Map<String, Peer> connectionPeerDic;
    private ArrayList<PeerConnection.IceServer> ICEServers;

    // 后进Caller，先进Receiver
    enum Role {Caller, Receiver,}

    private Role role;

    public PeerConnectionManager() {
        executor = Executors.newSingleThreadExecutor();
        connectionIdArray = new ArrayList<>();
        connectionPeerDic = new HashMap<>();
        // 参考服务器搭建
        ICEServers = new ArrayList<>();
        PeerConnection.IceServer iceServer1 = PeerConnection.IceServer
                .builder("turn:8.210.234.39:3478?transport=udp")
                .setUsername("seckill44")
                .setPassword("sec123456")
                .createIceServer();
        ICEServers.add(iceServer1);
    }

    public void initContext(ChatRoomActivity context, EglBase rootEglBase) {
        this.context = context;
        this.rootEglBase = rootEglBase;
    }

    public void joinToRoom(WebSocketManager javaWebSocket, ArrayList<String> connections,
                           boolean isVideoEnable, String myId) {
        this.myId = myId;
        this.videoEnable = isVideoEnable;
        this.webSocketManager = javaWebSocket;
        connectionIdArray.addAll(connections);
        // 建立本地预览
        executor.execute(() -> {
            if (factory == null) {
                factory = createConnectionFactory();
            }
            if (mediaStream == null) {
                createLocalStream();
            }
            // 建立没有初始化的空链接
            // 参考之前的 PeerConnection 逻辑
            createPeerConnections();
            // 把当前的视频流集合到 mediaStream
            addStreams();
            // 给房间服务器的其他人发送一个offer
            createOffers();
        });
    }

    private void addStreams() {
        for (Map.Entry<String, Peer> entry : connectionPeerDic.entrySet()) {
            if (mediaStream == null) {
                createLocalStream();
            }
            entry.getValue().pc.addStream(mediaStream);
        }
    }

    private void createOffers() {
        for (Map.Entry<String, Peer> entry : connectionPeerDic.entrySet()) {
            role = Role.Caller;
            Peer mPeer = entry.getValue();
            // 请求服务器
            mPeer.pc.createOffer(mPeer, offerOrAnswerConstraint());
        }
    }

    private MediaConstraints offerOrAnswerConstraint() {
        MediaConstraints mediaConstraints = new MediaConstraints();
        ArrayList<MediaConstraints.KeyValuePair> keyValuePairs = new ArrayList<>();
        keyValuePairs.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        keyValuePairs.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", String.valueOf(videoEnable)));
        mediaConstraints.mandatory.addAll(keyValuePairs);
        return mediaConstraints;
    }

    private void createPeerConnections() {
        for (String str : connectionIdArray) {
            Peer peer = new Peer(str);
            connectionPeerDic.put(str, peer);
        }
    }

    public void onReceiverAnswer(String socketId, String sdp) {
        executor.execute(() -> {
            // 拿到链接对象
            Peer mPeer = connectionPeerDic.get(socketId);
            if (mPeer != null) {
                SessionDescription sessionDescription = new SessionDescription(SessionDescription.Type.ANSWER, sdp);
                // sdp 上传给打洞服务器
                mPeer.pc.setRemoteDescription(mPeer, sessionDescription);
            }
        });
    }

    public void onRemoteIceCandidate(String socketId, IceCandidate iceCandidate) {
        executor.execute(() -> {
            Peer peer = connectionPeerDic.get(socketId);
            if (peer != null) {
                // addIceCandidate 在内部会请求
                peer.pc.addIceCandidate(iceCandidate);
            }
        });
    }

    public void onRemoteJoinToRoom(String socketId) {
        executor.execute(() -> {
            if (mediaStream == null) {
                createLocalStream();
            }
            Peer mPeer = new Peer(socketId);
            mPeer.pc.addStream(mediaStream);
            connectionIdArray.add(socketId);
            connectionPeerDic.put(socketId, mPeer);
        });
    }

    public void onReceiveOffer(String socketId, String description) {
        executor.execute(() -> {
            role = Role.Receiver;
            Peer mPeer = connectionPeerDic.get(socketId);
            SessionDescription sdp = new SessionDescription(SessionDescription.Type.OFFER, description);
            if (mPeer != null) {
                mPeer.pc.setRemoteDescription(mPeer, sdp);
            }
        });
    }

    private class Peer implements SdpObserver, PeerConnection.Observer {

        private PeerConnection pc;
        private String userId;

        public Peer(String socketId) {
            this.userId = socketId;
            pc = createPeerConnection();
        }

        @Override
        public void onCreateSuccess(SessionDescription origSdp) {
            Log.v(TAG, "sdp回写成功：" + origSdp.description);
            // 设置本地的sdp，耗时操作，回调：onSetSuccess 或者 onSetFailure
            pc.setLocalDescription(Peer.this, origSdp);
            // 设置远端的sdp
            // pc.setRemoteDescription();
        }

        @Override
        public void onSetSuccess() {
            // 1 你只是设置了本地没有设值远端本地sdp
            // 2 你设值了远端sdp不能 进行通话
            // 3 ice 交换
            if (pc.signalingState() == PeerConnection.SignalingState.HAVE_LOCAL_OFFER) {
                if (role == Role.Caller) {
                    // 主叫发送sdp
                    webSocketManager.sendOffer(userId, pc.getLocalDescription().description);
                } else if (role == Role.Receiver) {
                    // 被叫
                    webSocketManager.sendAnswer(userId, pc.getLocalDescription().description);
                }
            } else if (pc.signalingState() == PeerConnection.SignalingState.HAVE_REMOTE_OFFER) {
                pc.createAnswer(Peer.this, offerOrAnswerConstraint());
            } else if (pc.signalingState() == PeerConnection.SignalingState.STABLE) {
                if (role == Role.Receiver) {
                    Log.i(TAG, "onSetSuccess: 最后一步测试");
                    webSocketManager.sendAnswer(userId, pc.getLocalDescription().description);
                }
            }
        }

        @Override
        public void onSetFailure(String s) {

        }


        @Override
        public void onCreateFailure(String s) {

        }

        // 对方同意或拒绝
        @Override
        public void onSignalingChange(PeerConnection.SignalingState signalingState) {

        }

        // 网络切换
        @Override
        public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {

        }

        @Override
        public void onIceConnectionReceivingChange(boolean b) {

        }

        @Override
        public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {

        }

        // 指挥客户端A请求对方的服务器
        @Override
        public void onIceCandidate(IceCandidate iceCandidate) {
            Log.i(TAG, "onIceCandidate: " + iceCandidate.toString());
            webSocketManager.sendIceCandidate(userId, iceCandidate);
        }

        @Override
        public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {

        }

        // ice交换完了回调这个
        @Override
        public void onAddStream(MediaStream mediaStream) {
            context.onAddRemoteStream(mediaStream, userId);
        }

        @Override
        public void onRemoveStream(MediaStream mediaStream) {

        }

        @Override
        public void onDataChannel(DataChannel dataChannel) {

        }

        @Override
        public void onRenegotiationNeeded() {

        }

        @Override
        public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {

        }

        private PeerConnection createPeerConnection() {
            if (factory == null) {
                factory = createConnectionFactory();
            }
            // PeerConnection 打洞
            // 光靠客户端是不能打洞的，需要添加服务器地址
            PeerConnection.RTCConfiguration rtcConfiguration = new PeerConnection.RTCConfiguration(ICEServers);
            // 设置监听
            return factory.createPeerConnection(rtcConfiguration, this);
        }
    }

    private void createLocalStream() {
        mediaStream = factory.createLocalMediaStream("ARDAMS");
        // 添加一个总流
        AudioSource audioSource = factory.createAudioSource(createAudioConstraints());
        // 创建一个音频轨道，id前面是固定的，第0个轨道就是0
        AudioTrack audioTrack = factory.createAudioTrack("ARDAMSa0", audioSource);
        mediaStream.addTrack(audioTrack);
        // 音频轨道创建成功------------------------------------------------------------
        // 视频可选
        if (videoEnable) {
            // 摄像头的捕获设备
            VideoCapturer videoCapturer = createVideoCapture();
            // 视频源，参数：是否是录屏
            VideoSource videoSource = factory.createVideoSource(videoCapturer.isScreencast());
            SurfaceTextureHelper surfaceTextureHelper =
                    SurfaceTextureHelper.create("CaptureThread", rootEglBase.getEglBaseContext());
            // 绑定
            videoCapturer.initialize(surfaceTextureHelper, context, videoSource.getCapturerObserver());
            videoCapturer.startCapture(320, 240, 10);
            // 创建一个视频轨道，id前面是固定的，第0个轨道就是0
            VideoTrack videoTrack = factory.createVideoTrack("ARDAMSv0", videoSource);
            mediaStream.addTrack(videoTrack);
            if (context != null) {
                // 回调预览
                context.onSetLocalStream(mediaStream, myId);
            }
        }
    }

    private VideoCapturer createVideoCapture() {
        VideoCapturer videoCapturer;
        // 支持Camera2，使用Camera2
        if (Camera2Enumerator.isSupported(context)) {
            // camera2
            Camera2Enumerator enumerator = new Camera2Enumerator(context);
            // 前置摄像头的捕获
            videoCapturer = createCameraCapture(enumerator);
        } else {
            Camera1Enumerator enumerator = new Camera1Enumerator(true);
            videoCapturer = createCameraCapture(enumerator);
        }
        return videoCapturer;
    }

    // 获取前置前置摄像头/后置摄像头
    private VideoCapturer createCameraCapture(CameraEnumerator enumerator) {
        String[] deviceNames = enumerator.getDeviceNames();
        for (String deviceName : deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }
        for (String deviceName : deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) {
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }
        return null;
    }

    //  回音消除
    private static final String AUDIO_ECHO_CANCELLATION_CONSTRAINT = "googEchoCancellation";
    // 噪声抑制
    private static final String AUDIO_NOISE_SUPPRESSION_CONSTRAINT = "googNoiseSuppression";
    // 自动增益控制
    private static final String AUDIO_AUTO_GAIN_CONTROL_CONSTRAINT = "googAutoGainControl";
    //  高通滤波器
    private static final String AUDIO_HIGH_PASS_FILTER_CONSTRAINT = "googHighpassFilter";

    private MediaConstraints createAudioConstraints() {
        // HashMap
        MediaConstraints audioConstraints = new MediaConstraints();
        // 回音消除
        audioConstraints.mandatory.add(
                new MediaConstraints.KeyValuePair(AUDIO_ECHO_CANCELLATION_CONSTRAINT, "true"));
        // 噪声抑制
        audioConstraints.mandatory.add(
                new MediaConstraints.KeyValuePair(AUDIO_NOISE_SUPPRESSION_CONSTRAINT, "true"));
        // 自动增益控制
        audioConstraints.mandatory.add(
                new MediaConstraints.KeyValuePair(AUDIO_AUTO_GAIN_CONTROL_CONSTRAINT, "false"));
        // 高通滤波器
        audioConstraints.mandatory.add(
                new MediaConstraints.KeyValuePair(AUDIO_HIGH_PASS_FILTER_CONSTRAINT, "true"));
        return audioConstraints;
    }

    private PeerConnectionFactory createConnectionFactory() {
        PeerConnectionFactory.initialize(
                PeerConnectionFactory.InitializationOptions.builder(context).createInitializationOptions()
        );
        VideoEncoderFactory encoderFactory = new DefaultVideoEncoderFactory(rootEglBase.getEglBaseContext(), true, true);
        VideoDecoderFactory decoderFactory = new DefaultVideoDecoderFactory(rootEglBase.getEglBaseContext());
        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
        PeerConnectionFactory peerConnectionFactory =
                PeerConnectionFactory.builder()
                        .setOptions(options)
                        // 支持音频
                        .setAudioDeviceModule(JavaAudioDeviceModule.builder(context).createAudioDeviceModule())
                        // 支持视频
                        .setVideoDecoderFactory(decoderFactory)
                        .setVideoEncoderFactory(encoderFactory)
                        .createPeerConnectionFactory();
        return peerConnectionFactory;
    }

}
