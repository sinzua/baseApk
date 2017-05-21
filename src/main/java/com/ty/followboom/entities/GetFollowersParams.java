package com.ty.followboom.entities;

public class GetFollowersParams {
    private String avatarUrl;
    private String goodsId;
    private int startAt;
    private String userName;

    public GetFollowersParams(String avatarUrl, String goodsId, String userName, int startAt) {
        this.avatarUrl = avatarUrl;
        this.goodsId = goodsId;
        this.avatarUrl = avatarUrl;
        this.goodsId = goodsId;
        this.userName = userName;
        this.startAt = startAt;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getGoodsId() {
        return this.goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getStartAt() {
        return this.startAt;
    }

    public void setStartAt(int startAt) {
        this.startAt = startAt;
    }
}
