package com.nativex.common;

public class Device {
    private String androidDeviceID;
    private String androidID;
    private String deviceId;
    private String deviceName;
    private String ipAddress;
    private boolean isHacked;
    private String macWlan;
    private String osVersion;
    private boolean usingSdk;

    public String getAndroidDeviceID() {
        return this.androidDeviceID;
    }

    public void setAndroidDeviceID(String androidDeviceID) {
        this.androidDeviceID = androidDeviceID;
    }

    public String getAndroidID() {
        return this.androidID;
    }

    public void setAndroidID(String androidID) {
        this.androidID = androidID;
    }

    public boolean isHacked() {
        return this.isHacked;
    }

    public String isHackedAsString() {
        return this.isHacked ? "True" : "False";
    }

    public void setHacked(boolean isHacked) {
        this.isHacked = isHacked;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getOsVersion() {
        return this.osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public boolean isUsingSdk() {
        return this.usingSdk;
    }

    public void setUsingSdk(boolean usingSdk) {
        this.usingSdk = usingSdk;
    }
}
