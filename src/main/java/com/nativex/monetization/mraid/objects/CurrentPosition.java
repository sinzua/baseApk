package com.nativex.monetization.mraid.objects;

import com.google.gson.annotations.SerializedName;
import com.nativex.monetization.manager.DensityManager;
import com.nativex.monetization.mraid.MRAIDWebView;

public class CurrentPosition {
    @SerializedName("height")
    private Integer height = Integer.valueOf(0);
    @SerializedName("width")
    private Integer width = Integer.valueOf(0);
    @SerializedName("x")
    private Integer x = Integer.valueOf(0);
    @SerializedName("y")
    private Integer y = Integer.valueOf(0);

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

    public boolean setCurrentPosition(MRAIDWebView webView) {
        int height = DensityManager.getMRAIDDip((float) webView.getHeight());
        int width = DensityManager.getMRAIDDip((float) webView.getWidth());
        int x = DensityManager.getMRAIDDip((float) webView.getLeft());
        int y = DensityManager.getMRAIDDip((float) webView.getTop());
        boolean changed = false;
        if (!(this.height.intValue() == height && this.width.intValue() == width && this.x.intValue() == x && this.y.intValue() == y)) {
            changed = true;
        }
        this.x = Integer.valueOf(x);
        this.y = Integer.valueOf(y);
        this.height = Integer.valueOf(height);
        this.width = Integer.valueOf(width);
        return changed;
    }
}
