package com.ty.followboom.entities;

public class TrackActionParams {
    private int action;
    private String actionToken;
    private long orderId;

    public TrackActionParams(long orderId, int action, String actionToken) {
        this.orderId = orderId;
        this.action = action;
        this.actionToken = actionToken;
    }

    public long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getAction() {
        return this.action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getActionToken() {
        return this.actionToken;
    }

    public void setActionToken(String actionToken) {
        this.actionToken = actionToken;
    }
}
