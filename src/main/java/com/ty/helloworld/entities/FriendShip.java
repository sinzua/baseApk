package com.ty.helloworld.entities;

public class FriendShip {
    private long followGot;
    private long followNum;
    private long likeGot;
    private long likeNum;
    private long orderId;
    private long orderKind;
    private long postId;
    private long state;
    private long targetUserId;
    private long userId;

    public long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getLikeNum() {
        return this.likeNum;
    }

    public void setLikeNum(long likeNum) {
        this.likeNum = likeNum;
    }

    public long getLikeGot() {
        return this.likeGot;
    }

    public void setLikeGot(long likeGot) {
        this.likeGot = likeGot;
    }

    public long getOrderKind() {
        return this.orderKind;
    }

    public void setOrderKind(long orderKind) {
        this.orderKind = orderKind;
    }

    public long getFollowNum() {
        return this.followNum;
    }

    public void setFollowNum(long followNum) {
        this.followNum = followNum;
    }

    public long getFollowGot() {
        return this.followGot;
    }

    public void setFollowGot(long followGot) {
        this.followGot = followGot;
    }

    public long getState() {
        return this.state;
    }

    public void setState(long state) {
        this.state = state;
    }

    public long getPostId() {
        return this.postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public long getTargetUserId() {
        return this.targetUserId;
    }

    public void setTargetUserId(long targetUserId) {
        this.targetUserId = targetUserId;
    }
}
