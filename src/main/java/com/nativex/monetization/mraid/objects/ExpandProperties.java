package com.nativex.monetization.mraid.objects;

import android.graphics.Rect;
import com.google.gson.annotations.SerializedName;

public class ExpandProperties {
    @SerializedName("height")
    private Integer height;
    @SerializedName("isModal")
    private Boolean modal;
    @SerializedName("useCustomClose")
    private Boolean useCustomClose;
    @SerializedName("width")
    private Integer width;

    public Rect getRect() {
        return new Rect(0, 0, this.width.intValue(), this.height.intValue());
    }

    public Integer getHeight() {
        return this.height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Boolean isModal() {
        return this.modal;
    }

    public void setModal(Boolean modal) {
        this.modal = modal;
    }

    public Boolean getUseCustomClose() {
        return this.useCustomClose;
    }

    public void setUseCustomClose(Boolean useCustomClose) {
        this.useCustomClose = useCustomClose;
    }

    public Integer getWidth() {
        return this.width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
}
