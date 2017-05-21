package com.nativex.monetization;

public class ApplicationInputs {
    private String appId;
    private String applicationName = "";
    private String packageName = "";
    private String publisherUserId = "";

    public String getApplicationName() {
        return this.applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(int appId) {
        this.appId = Integer.toString(appId);
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPublisherUserId() {
        return this.publisherUserId;
    }

    public void setPublisherUserId(String publisherUserId) {
        this.publisherUserId = publisherUserId;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
