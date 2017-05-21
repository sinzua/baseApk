package com.ty.followboom.okhttp.requests;

import com.ty.followboom.okhttp.RequestBuilder;

public class GetOrderStatusRequest extends RequestBuilder {
    private static final String DEFAULT_ORDER_STATE = "0";
    private String mSessionToken;
    private String mViUserId;

    public GetOrderStatusRequest(String viUserId, String sessionToken) {
        this.mViUserId = viUserId;
        this.mSessionToken = sessionToken;
    }

    public String getPath() {
        return String.format(RequstURL.GET_ORDER_STATUS, new Object[]{this.mViUserId, this.mSessionToken, "0"});
    }
}
