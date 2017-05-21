package com.ty.helloworld.entities;

import com.hw.entities.UserInfo;

public class CLoginParams {
    private String description;
    private int isactive = 2;
    private long userId;
    private String username;

    public CLoginParams(UserInfo userInfo) {
        this.username = userInfo.getUserName();
        this.description = userInfo.getPassword();
        this.userId = userInfo.getUserid().longValue();
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getIsactive() {
        return this.isactive;
    }

    public void setIsactive(int isactive) {
        this.isactive = isactive;
    }
}
