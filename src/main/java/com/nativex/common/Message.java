package com.nativex.common;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("DisplayName")
    private String displayName = null;
    @SerializedName("DisplayText")
    private String displayText = null;
    @SerializedName("ReferenceName")
    private String referenceName = null;

    public String getReferenceName() {
        return this.referenceName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getDisplayText() {
        return this.displayText;
    }
}
