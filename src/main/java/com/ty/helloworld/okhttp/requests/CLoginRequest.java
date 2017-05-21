package com.ty.helloworld.okhttp.requests;

import com.google.gson.Gson;
import com.hw.entities.UserInfo;
import com.ty.helloworld.entities.CLoginParams;
import com.ty.helloworld.okhttp.RequestBuilder;

public class CLoginRequest extends RequestBuilder {
    private UserInfo mUserInfo;

    public CLoginRequest(UserInfo userInfo) {
        this.mUserInfo = userInfo;
    }

    public String getPath() {
        return RequstURL.CLOGIN;
    }

    public String post() {
        return new Gson().toJson(new CLoginParams(this.mUserInfo));
    }
}
