package com.hw.http.requests;

import com.hw.http.RequestBuilder;
import com.hw.http.RequstURL;

public class GetPostRequest extends RequestBuilder {
    private String postId;
    private String targetUserId;

    public GetPostRequest(String postId, String targetUserId) {
        this.postId = postId;
        this.targetUserId = targetUserId;
    }

    public String getPath() {
        return String.format(RequstURL.GET_POST, new Object[]{this.postId + "_" + this.targetUserId});
    }
}
