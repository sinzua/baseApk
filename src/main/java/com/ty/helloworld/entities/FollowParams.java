package com.ty.helloworld.entities;

import com.supersonicads.sdk.utils.Constants.RequestParameters;
import com.ty.instagramapi.InstagramService;

public class FollowParams {
    private String _csrftoken = getCsrftoken(InstagramService.getInstance().getUserInfo().getCookie());
    private long _uid;
    private String _uuid;
    private String user_id;

    public FollowParams(UserInfo userInfo, String targetUserId) {
        this.user_id = targetUserId;
        this._uid = userInfo.getUserid().longValue();
        this._uuid = userInfo.getUuid();
    }

    public String getCsrftoken(String cookies) {
        for (String cookie : cookies.split(";")) {
            String[] content = cookie.split(RequestParameters.EQUAL);
            if (content[0].contains("csrftoken")) {
                return content[1];
            }
        }
        return "";
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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
}
