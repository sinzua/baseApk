package com.parse;

enum PushType {
    NONE("none"),
    PPNS("ppns"),
    GCM("gcm");
    
    private final String pushType;

    private PushType(String pushType) {
        this.pushType = pushType;
    }

    static PushType fromString(String pushType) {
        if ("none".equals(pushType)) {
            return NONE;
        }
        if ("ppns".equals(pushType)) {
            return PPNS;
        }
        if ("gcm".equals(pushType)) {
            return GCM;
        }
        return null;
    }

    public String toString() {
        return this.pushType;
    }
}
