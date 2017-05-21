package com.ty.followboom.okhttp.requests;

import com.ty.followboom.okhttp.RequestBuilder;

public class GetAccountInfoRequest extends RequestBuilder {
    private String mSessionToken;
    private String mUserid;

    public GetAccountInfoRequest(String userid, String sessionToken) {
        this.mUserid = userid;
        this.mSessionToken = sessionToken;
    }

    public String getPath() {
        return String.format(RequstURL.GET_ACCOUNT_INFO, new Object[]{this.mUserid, this.mSessionToken});
    }
}
