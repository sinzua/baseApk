package com.ty.http.requests;

import com.ty.http.RequestBuilder;

public class TagSearchRequest extends RequestBuilder {
    private String tag;

    public TagSearchRequest(String tag) {
        this.tag = tag;
    }

    public String getPath() {
        return String.format("https://i.instagram.com/api/v1/tags/search/?q=%s", new Object[]{this.tag});
    }
}
