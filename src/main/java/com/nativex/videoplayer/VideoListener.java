package com.nativex.videoplayer;

public interface VideoListener {
    void on25PercentCompleted();

    void on50PercentCompleted();

    void on75PercentCompleted();

    void onClosedEarly();

    void onCompleted();

    void onStarted();
}
