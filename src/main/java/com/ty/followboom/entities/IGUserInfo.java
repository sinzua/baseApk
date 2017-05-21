package com.ty.followboom.entities;

public class IGUserInfo {
    private IGUserData data;
    private int status;

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public IGUserData getData() {
        return this.data;
    }

    public void setData(IGUserData data) {
        this.data = data;
    }
}
