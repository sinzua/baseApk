package com.ty.http.requests;

import com.google.gson.Gson;
import com.lidroid.xutils.http.client.multipart.MIME;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;
import com.ty.entities.FollowParams;
import com.ty.entities.UserInfo;
import com.ty.helpers.SignedBodyHelper;
import com.ty.http.RequestBuilder;

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
