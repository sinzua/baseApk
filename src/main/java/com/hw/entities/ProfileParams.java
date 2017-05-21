package com.hw.entities;

import com.hw.hwapi.HewService;
import com.supersonicads.sdk.utils.Constants.RequestParameters;

public class ProfileParams {
    private String _csrftoken = getCsrftoken(HewService.getInstance().getUserInfo().getCookie());
    private long _uid = HewService.getInstance().getUserInfo().getUserid().longValue();
    private String _uuid = HewService.getInstance().getUserInfo().getUuid();
    private String biography = "";
    private String email = HewService.getInstance().getUserInfo().getEmail();
    private String external_url = "";
    private String first_name = "";
    private int gender = 2;
    private String phone_number = "";
    private String username = "";

    public String getCsrftoken(String cookies) {
        for (String cookie : cookies.split(";")) {
            String[] content = cookie.split(RequestParameters.EQUAL);
            if (content[0].contains("csrftoken")) {
                return content[1];
            }
        }
        return "";
    }

    public int getGender() {
        return this.gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String get_uuid() {
        return this._uuid;
    }

    public void set_uuid(String _uuid) {
        this._uuid = _uuid;
    }

    public long get_uid() {
        return this._uid;
    }

    public void set_uid(long _uid) {
        this._uid = _uid;
    }

    public String get_csrftoken() {
        return this._csrftoken;
    }

    public void set_csrftoken(String _csrftoken) {
        this._csrftoken = _csrftoken;
    }

    public String getExternal_url() {
        return this.external_url;
    }

    public void setExternal_url(String external_url) {
        this.external_url = external_url;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return this.phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getBiography() {
        return this.biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getFirst_name() {
        return this.first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
}
