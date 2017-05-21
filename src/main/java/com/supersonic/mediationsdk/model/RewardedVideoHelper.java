package com.supersonic.mediationsdk.model;

public class RewardedVideoHelper {
    private int mCurrentVideosPresented;
    private int mMaxVideosPerSession;
    private String mPlacementName;
    private Boolean mVideoAvailability;

    public RewardedVideoHelper() {
        initState();
    }

    private void initState() {
        this.mVideoAvailability = null;
        this.mCurrentVideosPresented = 0;
        this.mMaxVideosPerSession = 0;
        this.mPlacementName = "";
    }

    public String getPlacementName() {
        return this.mPlacementName;
    }

    public void setPlacementName(String placementName) {
        this.mPlacementName = placementName;
    }

    public void reset() {
        initState();
    }

    public void setMaxVideo(int maxVideo) {
        this.mMaxVideosPerSession = maxVideo;
    }

    public synchronized boolean increaseCurrentVideo() {
        this.mCurrentVideosPresented++;
        return setVideoAvailability(canShowVideoInCurrentSession());
    }

    public synchronized boolean isVideoAvailable() {
        boolean result;
        result = false;
        if (this.mVideoAvailability != null) {
            result = this.mVideoAvailability.booleanValue();
        }
        return result;
    }

    public synchronized boolean setVideoAvailability(boolean availability) {
        boolean shouldNotify;
        shouldNotify = false;
        if (availability) {
            if (canShowVideoInCurrentSession()) {
                availability = true;
                if (this.mVideoAvailability == null || this.mVideoAvailability.booleanValue() != availability) {
                    this.mVideoAvailability = Boolean.valueOf(availability);
                    shouldNotify = true;
                }
            }
        }
        availability = false;
        this.mVideoAvailability = Boolean.valueOf(availability);
        shouldNotify = true;
        return shouldNotify;
    }

    public boolean canShowVideoInCurrentSession() {
        return this.mCurrentVideosPresented < this.mMaxVideosPerSession;
    }
}
