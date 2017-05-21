package com.ty.followboom.entities;

public class GetViewsParams {
    private String goodsId;
    private int goodsType;
    private String mediaId;
    private String postCode;
    private int startAt;
    private Long targetUserId;
    private String thumbnailUrl;
    private Long timestamp;
    private String trackingToken;
    private String userName;
    private String videoLowURL;
    private String videoUrl;

    public GetViewsParams(String mediaId, String videoUrl, String videoLowURL, String thumbnailUrl, String goodsId, int goodsType, String userName, String postCode, String trackingToken, Long targetUserId, Long timestamp, int startAt) {
        this.mediaId = mediaId;
        this.videoUrl = videoUrl;
        this.videoLowURL = videoLowURL;
        this.thumbnailUrl = thumbnailUrl;
        this.goodsId = goodsId;
        this.goodsType = goodsType;
        this.userName = userName;
        this.postCode = postCode;
        this.trackingToken = trackingToken;
        this.targetUserId = targetUserId;
        this.timestamp = timestamp;
        this.startAt = startAt;
    }

    public String getMediaId() {
        return this.mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoLowURL() {
        return this.videoLowURL;
    }

    public void setVideoLowURL(String videoLowURL) {
        this.videoLowURL = videoLowURL;
    }

    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getGoodsId() {
        return this.goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public int getGoodsType() {
        return this.goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPostCode() {
        return this.postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getTrackingToken() {
        return this.trackingToken;
    }

    public void setTrackingToken(String trackingToken) {
        this.trackingToken = trackingToken;
    }

    public Long getTargetUserId() {
        return this.targetUserId;
    }

    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public int getStartAt() {
        return this.startAt;
    }

    public void setStartAt(int startAt) {
        this.startAt = startAt;
    }
}
