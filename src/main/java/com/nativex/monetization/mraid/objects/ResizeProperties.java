package com.nativex.monetization.mraid.objects;

import android.graphics.Rect;
import com.google.gson.annotations.SerializedName;
import com.nativex.monetization.manager.DensityManager;
import com.nativex.monetization.mraid.MRAIDUtils.ClosePosition;
import com.nativex.monetization.mraid.MRAIDWebView;

public class ResizeProperties {
    @SerializedName("allowOffscreen")
    private Boolean allowOffscreen;
    @SerializedName("customClosePosition")
    private String customClosePosition;
    @SerializedName("height")
    private Integer height;
    @SerializedName("offsetX")
    private Integer offsetX;
    @SerializedName("offsetY")
    private Integer offsetY;
    @SerializedName("width")
    private Integer width;

    public Rect getCheckRect(MRAIDWebView webView) {
        int l = DensityManager.getDIP(webView.getContext(), (float) webView.getLeft()) + this.offsetX.intValue();
        int t = DensityManager.getDIP(webView.getContext(), (float) webView.getTop()) + this.offsetY.intValue();
        return new Rect(l, t, this.width.intValue() + l, this.height.intValue() + t);
    }

    public Rect getPositionRect(MRAIDWebView webView, MaxSize maxSize) {
        int r;
        int b;
        int l = webView.getLeft() + DensityManager.dipToPixels(webView.getContext(), (float) this.offsetX.intValue());
        int t = webView.getTop() + DensityManager.dipToPixels(webView.getContext(), (float) this.offsetY.intValue());
        if (this.allowOffscreen.booleanValue()) {
            r = DensityManager.dipToPixels(webView.getContext(), (float) this.width.intValue()) + l;
            b = DensityManager.dipToPixels(webView.getContext(), (float) this.height.intValue()) + t;
        } else {
            if (l < 0) {
                l = 0;
            }
            if (t < 0) {
                t = 0;
            }
            r = DensityManager.dipToPixels(webView.getContext(), (float) this.width.intValue()) + l;
            b = DensityManager.dipToPixels(webView.getContext(), (float) this.height.intValue()) + t;
            int maxSizeWidth = DensityManager.dipToPixels(webView.getContext(), (float) maxSize.getWidth());
            int maxSizeHeight = DensityManager.dipToPixels(webView.getContext(), (float) maxSize.getHeight());
            if (r > maxSizeWidth) {
                l = Math.max(l - (r - maxSizeWidth), 0);
                r = maxSizeWidth;
            }
            if (b > maxSizeHeight) {
                t = Math.max(t - (b - maxSizeHeight), 0);
                b = maxSizeHeight;
            }
        }
        return new Rect(l, t, r, b);
    }

    public Boolean allowOffscreen() {
        return Boolean.valueOf(this.allowOffscreen == null ? false : this.allowOffscreen.booleanValue());
    }

    public void setAllowOffscreen(Boolean allowOffscreen) {
        this.allowOffscreen = allowOffscreen;
    }

    public ClosePosition getCustomClosePosition() {
        return ClosePosition.getPosition(this.customClosePosition);
    }

    public void setCustomClosePosition(String position) {
        this.customClosePosition = position;
    }

    public void setCustomClosePosition(ClosePosition customClosePosition) {
        this.customClosePosition = customClosePosition.getName();
    }

    public int getHeight() {
        return this.height == null ? 0 : this.height.intValue();
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public int getOffsetX() {
        return this.offsetX == null ? 0 : this.offsetX.intValue();
    }

    public void setOffsetX(Integer offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return this.offsetY == null ? 0 : this.offsetY.intValue();
    }

    public void setOffsetY(Integer offsetY) {
        this.offsetY = offsetY;
    }

    public int getWidth() {
        return this.width == null ? 0 : this.width.intValue();
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
}
