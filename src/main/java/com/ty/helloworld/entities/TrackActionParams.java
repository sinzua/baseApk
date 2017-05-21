package com.ty.helloworld.entities;

public class TrackActionParams {
    private long orderId;
    private long orderKind;
    private long targetUserId;
    private long timeStamp = (System.currentTimeMillis() / 1000);
    private long userId;

    public TrackActionParams(long userId, long orderId, long targetUserId, long orderKind) {
        this.userId = userId;
        this.orderId = orderId;
        this.targetUserId = targetUserId;
        this.orderKind = orderKind;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getTargetUserId() {
        return this.targetUserId;
    }

    public void setTargetUserId(long targetUserId) {
        this.targetUserId = targetUserId;
    }

    public long getOrderKind() {
        return this.orderKind;
    }

    public void setOrderKind(long orderKind) {
        this.orderKind = orderKind;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
