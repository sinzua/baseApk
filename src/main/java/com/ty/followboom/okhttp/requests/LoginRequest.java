package com.ty.followboom.okhttp.requests;

import com.google.gson.Gson;
import com.ty.followboom.entities.LoginPostParams;
import com.ty.followboom.okhttp.RequestBuilder;

public class LoginRequest extends RequestBuilder {
    private String mDeviceId;
    private String mSessionToken;
    private String mUserid;
    private String mUsername;

    public LoginRequest(String userid, String sessionToken, String deviceId, String username) {
        this.mUserid = userid;
        this.mSessionToken = sessionToken;
        this.mDeviceId = deviceId;
        this.mUsername = username;
    }

    public String getPath() {
        return RequstURL.LOGIN;
    }

    public String post() {
        return new Gson().toJson(new LoginPostParams(this.mUserid, this.mUsername, "", this.mSessionToken, this.mDeviceId, ""));
    }
}
