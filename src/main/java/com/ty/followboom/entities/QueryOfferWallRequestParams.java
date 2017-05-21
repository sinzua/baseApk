package com.ty.followboom.entities;

public class QueryOfferWallRequestParams {
    private Integer coins;
    private String deviceId;
    private String offerwallPlatform;
    private String platform;

    public QueryOfferWallRequestParams(int coins, String offerwallPlatform, String deviceId, String platform) {
        this.coins = Integer.valueOf(coins);
        this.offerwallPlatform = offerwallPlatform;
        this.deviceId = deviceId;
        this.platform = platform;
    }

    public Integer getCoins() {
        return this.coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public String getOfferwallPlatform() {
        return this.offerwallPlatform;
    }

    public void setOfferwallPlatform(String offerwallPlatform) {
        this.offerwallPlatform = offerwallPlatform;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPlatform() {
        return this.platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
