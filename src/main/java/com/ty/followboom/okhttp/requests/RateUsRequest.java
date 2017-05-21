package com.ty.followboom.okhttp.requests;

import com.ty.followboom.okhttp.RequestBuilder;

public class RateUsRequest extends RequestBuilder {
    private String mSessionToken;
    private int mType;
    private String mUserid;

    public RateUsRequest(String userid, int type, String sessionToken) {
        this.mUserid = userid;
        this.mType = type;
        this.mSessionToken = sessionToken;
    }

    public String getPath() {
        return String.format(RequstURL.GET_REWARDS, new Object[]{this.mUserid, Integer.valueOf(this.mType), this.mSessionToken});
    }
}
