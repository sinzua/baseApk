package com.nativex.monetization.listeners;

import com.nativex.common.Log;
import com.nativex.common.Utilities;
import com.nativex.monetization.enums.VideoProgress;
import com.nativex.monetization.mraid.MRAIDManager;
import com.nativex.videoplayer.VideoListener;

public class NativeVideoPlayerListener implements VideoListener {
    private String mMRAIDContainerName;
    private String mVideoUrl;

    public void setVideoResource(String url) {
        this.mVideoUrl = url;
    }

    public void setMRAIDContainerName(String containerName) {
        this.mMRAIDContainerName = containerName;
    }

    public void onStarted() {
        trackVideoProgress(VideoProgress.VIDEO_PROGRESS_STARTED);
    }

    public void on25PercentCompleted() {
        trackVideoProgress(VideoProgress.VIDEO_PROGRESS_25_PERCENT);
    }

    public void on50PercentCompleted() {
        trackVideoProgress(VideoProgress.VIDEO_PROGRESS_50_PERCENT);
    }

    public void on75PercentCompleted() {
        trackVideoProgress(VideoProgress.VIDEO_PROGRESS_75_PERCENT);
    }

    public void onCompleted() {
        trackVideoProgress(VideoProgress.VIDEO_PROGRESS_COMPLETED);
        Log.d("VideoPlayer Listener onCompleted() container name " + this.mMRAIDContainerName);
        if (!Utilities.stringIsEmpty(this.mMRAIDContainerName)) {
            MRAIDManager.videoCompleted(this.mMRAIDContainerName);
        }
    }

    public void onClosedEarly() {
        Log.d("VideoPlayer Listener onClosedEarly() container name " + this.mMRAIDContainerName);
        if (!Utilities.stringIsEmpty(this.mMRAIDContainerName)) {
            MRAIDManager.videoCancelled(this.mMRAIDContainerName);
        }
    }

    private void trackVideoProgress(VideoProgress videoProgress) {
        Log.d("Track Video - video progress " + videoProgress.toString() + " container name " + this.mMRAIDContainerName);
        if (!Utilities.stringIsEmpty(this.mMRAIDContainerName)) {
            MRAIDManager.trackVideoProgress(this.mMRAIDContainerName, this.mVideoUrl, videoProgress);
        }
    }
}
