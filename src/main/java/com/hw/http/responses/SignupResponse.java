package com.hw.http.responses;

import com.hw.entities.LoginUser;

public class SignupResponse extends BasicResponse {
    private boolean account_created;
    private LoginUser created_user;

    public LoginUser getCreated_user() {
        return this.created_user;
    }

    public void setCreated_user(LoginUser created_user) {
        this.created_user = created_user;
    }

    public boolean isAccount_created() {
        return this.account_created;
    }

    public void setAccount_created(boolean account_created) {
        this.account_created = account_created;
    }
}
