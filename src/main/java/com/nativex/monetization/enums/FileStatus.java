package com.nativex.monetization.enums;

import com.nativex.common.JsonRequestConstants.UDIDs;

public enum FileStatus {
    STATUS_PENDING("1"),
    STATUS_DOWNLOADING("2"),
    STATUS_INUSE(UDIDs.ANDROID_ID),
    STATUS_FAILED("5"),
    STATUS_READY("6"),
    STATUS_DELETED("7");
    
    private final String id;

    private FileStatus(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }
}
