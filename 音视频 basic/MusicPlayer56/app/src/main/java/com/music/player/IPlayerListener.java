package com.music.player;

public interface IPlayerListener {

    void onLoad(boolean load);

    void onCurrentTime(int currentTime, int totalTime);

    void onError(int code, String msg);

    void onPause(boolean pause);

    void onDbValue(int db);

    void onComplete();

    String onNext();
}
