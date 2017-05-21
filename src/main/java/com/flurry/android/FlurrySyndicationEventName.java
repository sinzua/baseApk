package com.flurry.android;

public enum FlurrySyndicationEventName {
    REBLOG("Reblog"),
    FAST_REBLOG("FastReblog"),
    SOURCE_LINK("SourceClick"),
    LIKE("Like");
    
    private String a;

    private FlurrySyndicationEventName(String str) {
        this.a = str;
    }

    public String toString() {
        return this.a;
    }
}
