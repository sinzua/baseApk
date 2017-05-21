package com.nativex.monetization.interfaces;

import android.view.View.OnClickListener;

public interface ICustomVideoView {
    int getCurrentPosition();

    int getDuration();

    boolean isPlaying();

    void release();

    void seekTo(int i);

    void setOnCloseListener(OnClickListener onClickListener);

    void setVideoSource(String str);

    void start();

    void stopPlayback();
}
