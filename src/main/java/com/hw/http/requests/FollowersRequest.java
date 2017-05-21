package com.hw.http.requests;

import android.text.TextUtils;
import com.hw.http.RequestBuilder;

public class FollowersRequest extends RequestBuilder {
    private String maxId;
    private String userId;

    public FollowersRequest(String userId, String maxId) {
        this.userId = userId;
        this.maxId = maxId;
    }

    public String getPath() {
        if (TextUtils.isEmpty(this.maxId)) {
            return String.format("https://i.instagram.com/api/v1/friendships/%s/followers/", new Object[]{this.userId});
        }
        return String.format("https://i.instagram.com/api/v1/friendships/%s/followers/", new Object[]{this.userId}) + "?max_id=" + this.maxId;
    }
}
