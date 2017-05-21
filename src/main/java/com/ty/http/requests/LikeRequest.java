package com.ty.http.requests;

import com.google.gson.Gson;
import com.lidroid.xutils.http.client.multipart.MIME;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;
import com.ty.entities.LikeParams;
import com.ty.entities.UserInfo;
import com.ty.helpers.SignedBodyHelper;
import com.ty.http.RequestBuilder;

public class LikeRequest extends RequestBuilder {
    private LikeParams mLikeParams;

    public LikeRequest(UserInfo userInfo, String postId, String targetUserId) {
        this.mLikeParams = new LikeParams(userInfo, postId, targetUserId);
    }

    public String getPath() {
        return String.format("https://i.instagram.com/api/v1/media/%s/like/?d=0&src=%s", new Object[]{this.mLikeParams.getMedia_id(), this.mLikeParams.getSrc()});
    }

    public RequestBody post() {
        RequestBody requestBody = null;
        try {
            requestBody = new MultipartBuilder().type(MultipartBuilder.FORM).addPart(Headers.of(MIME.CONTENT_DISPOSITION, "form-data; name=\"signed_body\""), RequestBody.create(null, SignedBodyHelper.getSignedBody(new Gson().toJson(this.mLikeParams)))).addPart(Headers.of(MIME.CONTENT_DISPOSITION, "form-data; name=\"ig_sig_key_version\""), RequestBody.create(null, "5")).build();
        } catch (Exception e) {
        }
        return requestBody;
    }
}
