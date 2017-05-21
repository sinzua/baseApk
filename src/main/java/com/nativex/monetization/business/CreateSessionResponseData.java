package com.nativex.monetization.business;

import com.google.gson.annotations.SerializedName;
import com.nativex.common.LogItem;
import com.nativex.common.Message;
import com.nativex.common.Violation;
import java.util.List;

public class CreateSessionResponseData {
    @SerializedName("CachingFrequency")
    private Integer cachingFrequency;
    @SerializedName("IsCurrencyEnabled")
    private Boolean currencyEnabled;
    @SerializedName("ApiDeviceId")
    private String deviceId;
    @SerializedName("IsFirstRun")
    private Boolean firstRun;
    @SerializedName("FreeSpaceMin")
    private Integer freeSpaceMinMegabytes;
    @SerializedName("IsBackupAdsEnabled")
    private Boolean isBackupAdsAvailable = Boolean.valueOf(true);
    @SerializedName("Log")
    private List<LogItem> log = null;
    @SerializedName("Messages")
    private List<Message> messages = null;
    @SerializedName("IsAfppOfferwallEnabled")
    private Boolean offerwallEnabled;
    @SerializedName("ReplaceWebViewUserAgent")
    private Boolean replaceWebViewUserAgent = Boolean.valueOf(true);
    @SerializedName("Session")
    private Session session;
    @SerializedName("Violations")
    private List<Violation> violations = null;

    public boolean isOfferwallEnabled() {
        return this.offerwallEnabled.booleanValue();
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public String getSessionId() {
        if (this.session == null) {
            return null;
        }
        return this.session.getSessionId();
    }

    public Session getSession() {
        return this.session;
    }

    public Boolean isCurrencyEnabled() {
        return Boolean.valueOf(this.currencyEnabled == null ? true : this.currencyEnabled.booleanValue());
    }

    public int getCachingFrequency() {
        if (this.cachingFrequency == null) {
            return 0;
        }
        return this.cachingFrequency.intValue();
    }

    public Integer getFreeSpaceMinMegabytes() {
        return this.freeSpaceMinMegabytes;
    }

    public boolean isBackupAdsEnabled() {
        return this.isBackupAdsAvailable.booleanValue();
    }

    public boolean shouldReplaceWebViewUserAgent() {
        return this.replaceWebViewUserAgent.booleanValue();
    }
}
