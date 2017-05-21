package com.hw.http.responses;

import com.hw.entities.UserDetail;

public class UserinfoResponse extends BasicResponse {
    private UserDetail user;

    public UserDetail getUser() {
        return this.user;
    }

    public void setUser(UserDetail user) {
        this.user = user;
    }
}
