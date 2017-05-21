package com.nativex.monetization.mraid.objects;

import com.google.gson.annotations.SerializedName;

public class VideoPlayerOptions {
    @SerializedName("allowMute")
    private boolean allowMute;
    @SerializedName("allowSkipAfterMilliseconds")
    private int allowSkipAfterMilliseconds = Integer.MIN_VALUE;
    @SerializedName("allowSkipAfterVideoStuckForMilliseconds")
    private int allowSkipAfterVideoStuckForMilliseconds = Integer.MIN_VALUE;
    @SerializedName("autoPlay")
    private boolean autoPlay;
    @SerializedName("controlsAlpha")
    private int controlsAlpha = Integer.MIN_VALUE;
    @SerializedName("countdownAfterMilliseconds")
    private int countdownAfterMilliseconds = Integer.MIN_VALUE;
    @SerializedName("countdownMessageFormat")
    private String countdownMessageFormat = null;
    @SerializedName("countdownMessageTextColor")
    private String countdownMessageTextColor = null;
    @SerializedName("errorMessageToast")
    private String errorMessageToast = null;
    @SerializedName("controlIconMaxDimensionInDensityIndependentPixels")
    private int iconMaximumDimensionInDIP = Integer.MIN_VALUE;
    @SerializedName("controlsDistanceFromScreenEdgeInDensityIndependentPixels")
    private int iconsDistanceFromScreenEdgeInDIP = Integer.MIN_VALUE;
    @SerializedName("specialSkipCountdownMessageFormat")
    private String specialSkipCountdownMessageFormat = null;
    @SerializedName("startMuted")
    private boolean startMuted;

    public boolean isAllowMute() {
        return this.allowMute;
    }

    public boolean isStartMuted() {
        return this.startMuted;
    }

    public int getAllowSkipAfterMilliseconds() {
        return this.allowSkipAfterMilliseconds;
    }

    public int getCountdownAfterMilliseconds() {
        return this.countdownAfterMilliseconds;
    }

    public String getCountdownMessageTextColor() {
        return this.countdownMessageTextColor;
    }

    public String getCountdownMessageFormat() {
        return this.countdownMessageFormat;
    }

    public String getSpecialSkipCountdownMessageFormat() {
        return this.specialSkipCountdownMessageFormat;
    }

    public int getAllowSkipAfterVideoStuckForMilliseconds() {
        return this.allowSkipAfterVideoStuckForMilliseconds;
    }

    public String getErrorMessageToast() {
        return this.errorMessageToast;
    }

    public int getIconMaximumDimensionInDIP() {
        return this.iconMaximumDimensionInDIP;
    }

    public int getIconsDistanceFromScreenEdgeInDIP() {
        return this.iconsDistanceFromScreenEdgeInDIP;
    }

    public int getControlsAlpha() {
        return this.controlsAlpha;
    }
}
