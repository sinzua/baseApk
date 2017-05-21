package com.ty.followboom.entities;

public class IGUserData {
    private String access_token;
    private IGUser user;

    public String getAccess_token() {
        return this.access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public IGUser getUser() {
        return this.user;
    }

    public void setUser(IGUser user) {
        this.user = user;
    }
}
