package com.ty.followboom.okhttp.requests;

import com.ty.followboom.okhttp.RequestBuilder;

public class GetCoinsHistoryRequest extends RequestBuilder {
    private static final String DEFAULT_COINS_STATE = "0";
    private String mSessionToken;
    private String mViUserId;

    public GetCoinsHistoryRequest(String viUserId, String sessionToken) {
        this.mViUserId = viUserId;
        this.mSessionToken = sessionToken;
    }

    public String getPath() {
        return String.format(RequstURL.GET_COINS_HISTORY, new Object[]{this.mViUserId, this.mSessionToken, "0"});
    }
}
