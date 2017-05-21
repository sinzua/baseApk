package com.nativex.monetization.enums;

public enum AdEvent {
    ALREADY_FETCHED(-1),
    DOWNLOADING(-1),
    ALREADY_SHOWN(-1),
    RESIZED(-1),
    EXPANDED(-1),
    COLLAPSED(-1),
    USER_TOUCH(-1),
    FETCHED(10),
    BEFORE_DISPLAY(15),
    DISPLAYED(20),
    EXPIRED(20),
    USER_NAVIGATES_OUT_OF_APP(90),
    DISMISSED(100),
    ERROR(100),
    NO_AD(100),
    VIDEO_75_PERCENT_COMPLETED(21),
    VIDEO_COMPLETED(-1);
    
    private final int level;

    private AdEvent(int i) {
        this.level = i;
    }

    public int getEventLevel() {
        return this.level;
    }
}
