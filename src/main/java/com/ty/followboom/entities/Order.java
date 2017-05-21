package com.ty.followboom.entities;

import java.util.ArrayList;

public class Order {
    public static final String TPYE_COLLAGE = "collage";
    public static final String TPYE_FOLLOW = "follow";
    public static final int TPYE_FOLLOW_INDEX = 1;
    public static final String TPYE_LIKE = "like";
    public static final int TPYE_LIKE_INDEX = 0;
    public static final int TPYE_MIX_INDEX = 2;
    private int actionCoolDown;
    private String avatarUrl;
    private long coinsReward;
    private ArrayList<Order> collage;
    private String kind;
    private long orderId;
    private String postId;
    private int skipCoolDown;
    private String username;
    private String viUserId;
    private String videoLowUrl;
    private String videoThumbnailUrl;

    public String getViUserId() {
        return this.viUserId;
    }

    public void setViUserId(String viUserId) {
        this.viUserId = viUserId;
    }

    public long getCoinsReward() {
        return this.coinsReward;
    }

    public void setCoinsReward(long coinsReward) {
        this.coinsReward = coinsReward;
    }

    public String getVideoLowUrl() {
        return this.videoLowUrl;
    }

    public void setVideoLowUrl(String videoLowUrl) {
        this.videoLowUrl = videoLowUrl;
    }

    public String getVideoThumbnailUrl() {
        return this.videoThumbnailUrl;
    }

    public void setVideoThumbnailUrl(String videoThumbnailUrl) {
        this.videoThumbnailUrl = videoThumbnailUrl;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPostId() {
        return this.postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public int getSkipCoolDown() {
        return this.skipCoolDown;
    }

    public void setSkipCoolDown(int skipCoolDown) {
        this.skipCoolDown = skipCoolDown;
    }

    public int getActionCoolDown() {
        return this.actionCoolDown;
    }

    public void setActionCoolDown(int actionCoolDown) {
        this.actionCoolDown = actionCoolDown;
    }

    public long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getKind() {
        return this.kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public ArrayList<Order> getCollage() {
        return this.collage;
    }

    public void setCollage(ArrayList<Order> collage) {
        this.collage = collage;
    }

    public boolean isLikeOrder() {
        return "like".equals(this.kind);
    }

    public boolean isFollowOrder() {
        return "follow".equals(this.kind);
    }

    public boolean isCollageOrder() {
        return TPYE_COLLAGE.equals(this.kind);
    }
}
