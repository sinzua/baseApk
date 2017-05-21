package com.ty.helloworld.entities;

import com.google.android.gms.common.Scopes;
import com.supersonicads.sdk.utils.Constants.RequestParameters;
import com.ty.instagramapi.InstagramService;

public class LikeParams {
    private String _csrftoken;
    private long _uid;
    private String _uuid;
    private String media_id;
    private String src = Scopes.PROFILE;

    public LikeParams(UserInfo userInfo, String postId, String targetUserId) {
        this.media_id = postId + "_" + targetUserId;
        this._uid = userInfo.getUserid().longValue();
        this._uuid = userInfo.getUuid();
        this._csrftoken = getCsrftoken(InstagramService.getInstance().getUserInfo().getCookie());
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

    public String get_uuid() {
        return this._uuid;
    }

    public void set_uuid(String _uuid) {
        this._uuid = _uuid;
    }

    public String getMedia_id() {
        return this.media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public long get_uid() {
        return this._uid;
    }

    public void set_uid(long _uid) {
        this._uid = _uid;
    }

    public String getSrc() {
        return this.src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String get_csrftoken() {
        return this._csrftoken;
    }

    public void set_csrftoken(String _csrftoken) {
        this._csrftoken = _csrftoken;
    }
}
