package com.ty.http.requests;

import android.text.TextUtils;
import com.ty.http.RequestBuilder;

public class TaglineRequest extends RequestBuilder {
    private String maxId;
    private String tagName;

    public TaglineRequest(String tagName, String maxId) {
        this.tagName = tagName;
        this.maxId = maxId;
    }

    public String getPath() {
        if (TextUtils.isEmpty(this.maxId)) {
            return String.format("https://i.instagram.com/api/v1/feed/tag/%s/", new Object[]{this.tagName});
        }
        return String.format("https://i.instagram.com/api/v1/feed/tag/%s/", new Object[]{this.tagName}) + "?max_id=" + this.maxId;
    }
}
