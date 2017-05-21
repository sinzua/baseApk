package com.ty.http.requests;

import android.text.TextUtils;
import com.ty.http.RequestBuilder;

public class UserLikedRequest extends RequestBuilder {
    private String maxId;

    public UserLikedRequest(String maxId) {
        this.maxId = maxId;
    }

    public String getPath() {
        if (TextUtils.isEmpty(this.maxId)) {
            return "https://i.instagram.com/api/v1/feed/liked/";
        }
        return "https://i.instagram.com/api/v1/feed/liked/?max_id=" + this.maxId;
    }
}
