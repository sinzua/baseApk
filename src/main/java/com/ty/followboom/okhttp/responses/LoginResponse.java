package com.ty.followboom.okhttp.responses;

import com.ty.followboom.entities.LoginData;

public class LoginResponse extends BasicResponse {
    private LoginData data;

    public LoginData getData() {
        return this.data;
    }

    public void setData(LoginData data) {
        this.data = data;
    }
}
