package com.ty.http.requests;

import android.text.TextUtils;
import com.ty.http.RequestBuilder;

public class FollowingRequest extends RequestBuilder {
    private String maxId;
    private String userId;

    public FollowingRequest(String userId, String maxId) {
        this.userId = userId;
        this.maxId = maxId;
    }

    public String getPath() {
        if (TextUtils.isEmpty(this.maxId)) {
            return String.format("https://i.instagram.com/api/v1/friendships/%s/following/", new Object[]{this.userId});
        }
        return String.format("https://i.instagram.com/api/v1/friendships/%s/following/", new Object[]{this.userId}) + "?max_id=" + this.maxId;
    }
}
