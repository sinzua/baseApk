package com.nativex.monetization.mraid.objects;

import com.google.gson.annotations.SerializedName;

public class ScreenSize {
    @SerializedName("height")
    private Integer height;
    @SerializedName("width")
    private Integer width;

    public Integer getHeight() {
        return this.height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return this.width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
}
