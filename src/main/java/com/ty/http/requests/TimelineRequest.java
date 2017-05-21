package com.ty.http.requests;

import android.text.TextUtils;
import com.ty.http.RequestBuilder;

public class TimelineRequest extends RequestBuilder {
    private String maxId;

    public TimelineRequest(String maxId) {
        this.maxId = maxId;
    }

    public String getPath() {
        if (TextUtils.isEmpty(this.maxId)) {
            return "https://i.instagram.com/api/v1/feed/timeline/";
        }
        return "https://i.instagram.com/api/v1/feed/timeline/?max_id=" + this.maxId;
    }
}
