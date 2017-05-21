package com.nativex.monetization.business;

import com.google.gson.annotations.SerializedName;
import com.nativex.common.SharedPreferenceManager;
import com.nativex.common.UDIDs;

public class CreateSessionRequestData {
    @SerializedName("IsAdvertiserTrackingEnabled")
    private Boolean advertiserTrackingEnabled = Boolean.valueOf(true);
    @SerializedName("AppId")
    private String appId;
    @SerializedName("AppLanguageCode")
    private String appLanguageCode;
    @SerializedName("BuildType")
    private String buildType;
    @SerializedName("DeviceGenerationInfo")
    private String deviceGenerationInfo;
    @SerializedName("DeviceLanguageCode")
    private String deviceLanguageCode;
    @SerializedName("IsHacked")
    private Boolean isHacked;
    @SerializedName("IsOnWifi")
    private Boolean isOnWifi;
    @SerializedName("IsUsingSdk")
    private Boolean isUsingSDK;
    @SerializedName("IsOfferCacheAvailable")
    private Boolean offerCacheAvailable;
    @SerializedName("OSVersion")
    private String osVersion;
    @SerializedName("PreviousSessionId")
    private String previousSessionId;
    @SerializedName("PublisherUserId")
    private String publisherUserId;
    @SerializedName("PublisherSDKVersion")
    private String sdkVersion;
    @SerializedName("UDIDs")
    private UDIDs udids;
    @SerializedName("WebViewUserAgent")
    private String webViewUserAgent;

    public void setAdvertiserTrackingEnabled(Boolean advertiserTrackingEnabled) {
        this.advertiserTrackingEnabled = advertiserTrackingEnabled;
    }

    public void setOfferCacheAvailable(Boolean offerCacheAvailable) {
        this.offerCacheAvailable = offerCacheAvailable;
    }

    public void setUdids(UDIDs udids) {
        if (udids == null) {
            throw new NullPointerException("UDIDs must not be null");
        }
        this.udids = udids;
    }

    public void setDeviceGenerationInfo(String deviceGenerationInfo) {
        this.deviceGenerationInfo = deviceGenerationInfo;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public void setPublisherUserId(String publisherUserId) {
        this.publisherUserId = publisherUserId;
    }

    public void setSDKVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public void setBuildType() {
        this.buildType = SharedPreferenceManager.getBuildType();
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setHacked(Boolean isHacked) {
        this.isHacked = isHacked;
    }

    public void setOnCellular(Boolean isOnCellular) {
        this.isOnWifi = isOnCellular;
    }

    public void setUsingSDK(Boolean isUsingSDK) {
        this.isUsingSDK = isUsingSDK;
    }

    public void setPreviousSessionId(String previousSessionId) {
        this.previousSessionId = previousSessionId;
    }

    public void setWebViewUserAgent(String webViewUserAgent) {
        this.webViewUserAgent = webViewUserAgent;
    }

    public void setDeviceLanguageCode(String deviceLanguageCode) {
        this.deviceLanguageCode = deviceLanguageCode;
    }

    public void setAppLanguageCode(String appLanguageCode) {
        this.appLanguageCode = appLanguageCode;
    }
}
