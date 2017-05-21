package com.hw.http.requests;

import com.hw.http.RequestBuilder;

public class UserSearchRequest extends RequestBuilder {
    private String username;

    public UserSearchRequest(String username) {
        this.username = username;
    }

    public String getPath() {
        return String.format("https://i.instagram.com/api/v1/users/search/?query=%s", new Object[]{this.username});
    }
}
