package com.hw.http.responses;

import com.hw.entities.LoginUser;

public class LoginResponse extends BasicResponse {
    private LoginUser logged_in_user;

    public LoginUser getLogged_in_user() {
        return this.logged_in_user;
    }

    public void setLogged_in_user(LoginUser logged_in_user) {
        this.logged_in_user = logged_in_user;
    }
}
