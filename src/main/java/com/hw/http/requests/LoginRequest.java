package com.hw.http.requests;

import com.google.gson.Gson;
import com.hw.entities.LoginParams;
import com.hw.entities.UserInfo;
import com.hw.helpers.SignedBodyHelper;
import com.hw.http.RequestBuilder;
import com.lidroid.xutils.http.client.multipart.MIME;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

public class LoginRequest extends RequestBuilder {
    private LoginParams mLoginParams;

    public LoginRequest(UserInfo userInfo) {
        this.mLoginParams = new LoginParams(userInfo);
    }

    public String getPath() {
        return "https://i.instagram.com/api/v1/accounts/login/";
    }

    public RequestBody post() {
        RequestBody requestBody = null;
        try {
            requestBody = new MultipartBuilder().type(MultipartBuilder.FORM).addPart(Headers.of(MIME.CONTENT_DISPOSITION, "form-data; name=\"signed_body\""), RequestBody.create(null, SignedBodyHelper.getSignedBody(new Gson().toJson(this.mLoginParams)))).addPart(Headers.of(MIME.CONTENT_DISPOSITION, "form-data; name=\"ig_sig_key_version\""), RequestBody.create(null, "5")).build();
        } catch (Exception e) {
        }
        return requestBody;
    }
}
