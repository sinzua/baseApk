package com.nativex.monetization.mraid.objects;

import com.google.gson.annotations.SerializedName;

public class MaxSize {
    @SerializedName("height")
    private Integer height;
    @SerializedName("width")
    private Integer width;

    public int getHeight() {
        return this.height == null ? 0 : this.height.intValue();
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public int getWidth() {
        return this.width == null ? 0 : this.width.intValue();
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
}
