package com.ty.followboom.okhttp.requests;

import com.forwardwin.base.widgets.MD5Encryption;
import com.google.gson.Gson;
import com.ty.followboom.entities.TrackActionParams;
import com.ty.followboom.okhttp.RequestBuilder;

public class TrackActionRequest extends RequestBuilder {
    private int mActionType;
    private long mOrderId;
    private String mSessionToken;
    private String mUserid;

    public TrackActionRequest(String userid, String sessionToken, int actionType, long orderId) {
        this.mOrderId = orderId;
        this.mActionType = actionType;
        this.mUserid = userid;
        this.mSessionToken = sessionToken;
    }

    public String getPath() {
        return String.format(RequstURL.TRACK_ACTION, new Object[]{this.mUserid, this.mSessionToken});
    }

    public String post() {
        return new Gson().toJson(new TrackActionParams(this.mOrderId, this.mActionType, MD5Encryption.getMD5("" + this.mOrderId + System.currentTimeMillis())));
    }
}
