package com.nativex.monetization.mraid.objects;

import com.google.gson.annotations.SerializedName;
import com.nativex.monetization.manager.DensityManager;
import com.nativex.monetization.mraid.MRAIDWebView;

public class DefaultPosition {
    @SerializedName("height")
    private Integer height;
    @SerializedName("width")
    private Integer width;
    @SerializedName("x")
    private Integer x;
    @SerializedName("y")
    private Integer y;

    public Integer getX() {
        return this.x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return this.y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getWidth() {
        return this.width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return this.height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setPosition(MRAIDWebView webView) {
        this.height = Integer.valueOf(DensityManager.getDIP(webView.getContext(), (float) webView.getHeight()));
        this.width = Integer.valueOf(DensityManager.getDIP(webView.getContext(), (float) webView.getWidth()));
        this.x = Integer.valueOf(DensityManager.getDIP(webView.getContext(), (float) webView.getLeft()));
        this.y = Integer.valueOf(DensityManager.getDIP(webView.getContext(), (float) webView.getTop()));
    }
}
