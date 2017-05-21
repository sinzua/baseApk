package com.ty.helloworld.okhttp.requests;

import com.google.gson.Gson;
import com.ty.helloworld.entities.TrackActionParams;
import com.ty.helloworld.okhttp.RequestBuilder;

public class TrackActionRequest extends RequestBuilder {
    private long mOrderId;
    private long mOrderKind;
    private long mTargetUserId;
    private long mUserId;

    public TrackActionRequest(long userId, long orderId, long targetUserId, long orderKind) {
        this.mUserId = userId;
        this.mOrderId = orderId;
        this.mTargetUserId = targetUserId;
        this.mOrderKind = orderKind;
    }

    public String getPath() {
        return RequstURL.TRACK_ACTION;
    }

    public String post() {
        return new Gson().toJson(new TrackActionParams(this.mUserId, this.mOrderId, this.mTargetUserId, this.mOrderKind));
    }
}
