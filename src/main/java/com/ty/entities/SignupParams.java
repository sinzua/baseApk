package com.ty.entities;

import java.util.UUID;

public class SignupParams {
    private String _csrftoken = "EIMzxUmBkrjb8sv72q0bkMVfnXyMAszo";
    private String _uuid;
    private String device_id;
    private String email;
    private String password;
    private String username;
    private String waterfall_id;

    public SignupParams(UserInfo userInfo) {
        this.username = userInfo.getUserName();
        this.password = userInfo.getPassword();
        this.email = userInfo.getEmail();
        String uuid = userInfo.getUuid();
        this.device_id = uuid;
        this._uuid = uuid;
        this.waterfall_id = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
    }

    public String getWaterfall_id() {
        return this.waterfall_id;
    }

    public void setWaterfall_id(String waterfall_id) {
        this.waterfall_id = waterfall_id;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String get_uuid() {
        return this._uuid;
    }

    public void set_uuid(String _uuid) {
        this._uuid = _uuid;
    }

    public String getDevice_id() {
        return this.device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String get_csrftoken() {
        return this._csrftoken;
    }

    public void set_csrftoken(String _csrftoken) {
        this._csrftoken = _csrftoken;
    }
}
