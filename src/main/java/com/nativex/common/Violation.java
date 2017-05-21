package com.nativex.common;

import com.google.gson.annotations.SerializedName;

public class Violation {
    @SerializedName("Entity")
    private String entity = null;
    @SerializedName("Message")
    private String message = null;
    @SerializedName("Type")
    private String type = null;

    public String getEntity() {
        return this.entity;
    }

    public String getMessage() {
        return this.message;
    }
}
