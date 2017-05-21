package com.ty.followboom.okhttp.requests;

import com.ty.followboom.okhttp.RequestBuilder;

public class GetBoardRequest extends RequestBuilder {
    private int mPage;
    private String mSessionToken;
    private int mType;
    private String mUserid;

    public GetBoardRequest(String userid, int type, int page, String sessionToken) {
        this.mUserid = userid;
        this.mType = type;
        this.mPage = page;
        this.mSessionToken = sessionToken;
    }

    public String getPath() {
        return String.format(RequstURL.GET_BOARD, new Object[]{this.mUserid, Integer.valueOf(this.mType), this.mSessionToken});
    }
}
