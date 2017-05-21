package com.ty.followboom.entities;

public class LoginPostParams {
    private String deviceId;
    private String imei;
    private String platform = "0";
    private String sessionToken;
    private String uuid;
    private String viPassword;
    private String viUserId;
    private String viUserName;

    public LoginPostParams(String viUserId, String viUserName, String viPassword, String sessionToken, String deviceId, String imei) {
        this.viUserId = viUserId;
        this.viUserName = viUserName;
        this.viPassword = viPassword;
        this.sessionToken = sessionToken;
        this.deviceId = deviceId;
        this.imei = imei;
    }

    public String getViUserId() {
        return this.viUserId;
    }

    public void setViUserId(String viUserId) {
        this.viUserId = viUserId;
    }

    public String getViUserName() {
        return this.viUserName;
    }

    public void setViUserName(String viUserName) {
        this.viUserName = viUserName;
    }

    public String getViPassword() {
        return this.viPassword;
    }

    public void setViPassword(String viPassword) {
        this.viPassword = viPassword;
    }

    public String getSessionToken() {
        return this.sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getPlatform() {
        return this.platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getImei() {
        return this.imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
