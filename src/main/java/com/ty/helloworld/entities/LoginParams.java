package com.ty.helloworld.entities;

public class LoginParams {
    private String _csrftoken = "2435840e7eb34784eac33b2b391818eb";
    private String _uuid;
    private String device_id;
    private boolean from_reg;
    private String password;
    private String username;

    public LoginParams(UserInfo userInfo) {
        this.username = userInfo.getUserName();
        this.password = userInfo.getPassword();
        String uuid = userInfo.getUuid();
        this.device_id = uuid;
        this._uuid = uuid;
    }

    public String get_uuid() {
        return this._uuid;
    }

    public void set_uuid(String _uuid) {
        this._uuid = _uuid;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDevice_id() {
        return this.device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public boolean isFrom_reg() {
        return this.from_reg;
    }

    public void setFrom_reg(boolean from_reg) {
        this.from_reg = from_reg;
    }

    public String get_csrftoken() {
        return this._csrftoken;
    }

    public void set_csrftoken(String _csrftoken) {
        this._csrftoken = _csrftoken;
    }
}
