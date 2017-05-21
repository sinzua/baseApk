package com.nativex.common;

import com.google.gson.annotations.SerializedName;

public class LogItem {
    @SerializedName("DisplayName")
    private String displayName;
    @SerializedName("DisplayText")
    private String displayText;
    @SerializedName("ReferenceName")
    private String referenceName;
}
