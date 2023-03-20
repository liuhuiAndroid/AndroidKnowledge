package com.demo.room;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.room.webrtc.WebRTCManager;

public class MainActivity extends AppCompatActivity {

    private EditText et_room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_room = findViewById(R.id.et_room);
    }

    public void JoinRoom(View view) {
        WebRTCManager.getInstance().connect(this, et_room.getText().toString());
    }

    public void JoinRoomSingleVideo(View view) {

    }
}