package com.nativex.monetization.enums;

public enum VideoProgress {
    VIDEO_PROGRESS_STARTED(0.0f),
    VIDEO_PROGRESS_25_PERCENT(25.0f),
    VIDEO_PROGRESS_50_PERCENT(50.0f),
    VIDEO_PROGRESS_75_PERCENT(75.0f),
    VIDEO_PROGRESS_COMPLETED(100.0f);
    
    private final float mPercentage;

    private VideoProgress(float percentage) {
        this.mPercentage = percentage;
    }

    public String toString() {
        return Float.toString(this.mPercentage);
    }
}
