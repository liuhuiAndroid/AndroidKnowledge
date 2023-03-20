package com.demo.room;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.demo.room.utils.PermissionUtil;
import com.demo.room.utils.Utils;
import com.demo.room.webrtc.WebRTCManager;

import org.webrtc.EglBase;
import org.webrtc.MediaStream;
import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoTrack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRoomActivity extends Activity {

    private WebRTCManager webRTCManager;
    private EglBase rootEglBase;
    private Map<String, SurfaceViewRenderer> videoViews = new HashMap<>();
    //房间里所有的ID列表
    private List<String> persons = new ArrayList<>();
    private FrameLayout wrVideoLayout;

    public static void openActivity(Activity activity) {
        Intent intent = new Intent(activity, ChatRoomActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        initView();
    }

    private void initView() {
        wrVideoLayout = findViewById(R.id.wr_video_view);
        wrVideoLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        rootEglBase = EglBase.create();
        webRTCManager = WebRTCManager.getInstance();
        if (!PermissionUtil.isNeedRequestPermission(this)) {
            webRTCManager.joinRoom(this, rootEglBase);
        }
    }

    // 设置远端的流数据
    public void onAddRemoteStream(MediaStream stream, String userId) {
        runOnUiThread(() -> addView(userId, stream));
    }

    public void onSetLocalStream(MediaStream stream, String userId) {
        // 总流不为空
        if (stream.videoTracks.size() > 0) {
            VideoTrack localVideoTrack = stream.videoTracks.get(0);
        }
        runOnUiThread(() -> addView(userId, stream));
    }

    private void addView(String userId, MediaStream stream) {
        // 不用 SurfaceView
        // 采用 WebRTC 给我们提供的 SurfaceViewRenderer
        SurfaceViewRenderer surfaceViewRenderer = new SurfaceViewRenderer(this);
        // 初始化 SurfaceViewRenderer
        surfaceViewRenderer.init(rootEglBase.getEglBaseContext(), null);
        // 设置缩放模式
        surfaceViewRenderer.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
        surfaceViewRenderer.setMirror(true);
        // 关联数据源和View
        if (stream.videoTracks.size() > 0) {
            stream.videoTracks.get(0).addSink(surfaceViewRenderer);
        }
        videoViews.put(userId, surfaceViewRenderer);
        persons.add(userId);
        wrVideoLayout.addView(surfaceViewRenderer);
        int size = videoViews.size();
        for (int i = 0; i < size; i++) {
            String peerId = persons.get(i);
            SurfaceViewRenderer renderer1 = videoViews.get(peerId);
            if (renderer1 != null) {
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.height = Utils.getWidth(this, size);
                layoutParams.width = Utils.getWidth(this, size);
                layoutParams.leftMargin = Utils.getX(this, size, i);
                layoutParams.topMargin = Utils.getY(this, size, i);
                renderer1.setLayoutParams(layoutParams);
            }
        }
    }

}
