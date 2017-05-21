package com.ty.helloworld.entities;

public class GetFriendShipParams {
    private long timeStamp = (System.currentTimeMillis() / 1000);
    private long userId;

    public GetFriendShipParams(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
