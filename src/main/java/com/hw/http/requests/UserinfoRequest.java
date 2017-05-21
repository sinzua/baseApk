package com.hw.http.requests;

import com.hw.http.RequestBuilder;

public class UserinfoRequest extends RequestBuilder {
    private String userId;

    public UserinfoRequest(String userId) {
        this.userId = userId;
    }

    public String getPath() {
        return String.format("https://i.instagram.com/api/v1/users/%s/info/", new Object[]{this.userId});
    }
}
