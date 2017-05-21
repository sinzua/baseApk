package com.hw.http.requests;

import com.google.gson.Gson;
import com.hw.entities.FollowParams;
import com.hw.entities.UserInfo;
import com.hw.helpers.SignedBodyHelper;
import com.hw.http.RequestBuilder;
import com.lidroid.xutils.http.client.multipart.MIME;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

public class FollowRequest extends RequestBuilder {
    private FollowParams mFollowParams;

    public FollowRequest(UserInfo userInfo, String targetUserId) {
        this.mFollowParams = new FollowParams(userInfo, targetUserId);
    }

    public String getPath() {
        return String.format("https://i.instagram.com/api/v1/friendships/create/%s/", new Object[]{this.mFollowParams.getUser_id()});
    }

    public RequestBody post() {
        RequestBody requestBody = null;
        try {
            requestBody = new MultipartBuilder().type(MultipartBuilder.FORM).addPart(Headers.of(MIME.CONTENT_DISPOSITION, "form-data; name=\"signed_body\""), RequestBody.create(null, SignedBodyHelper.getSignedBody(new Gson().toJson(this.mFollowParams)))).addPart(Headers.of(MIME.CONTENT_DISPOSITION, "form-data; name=\"ig_sig_key_version\""), RequestBody.create(null, "5")).build();
        } catch (Exception e) {
        }
        return requestBody;
    }
}
