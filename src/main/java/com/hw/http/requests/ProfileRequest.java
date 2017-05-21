package com.hw.http.requests;

import com.google.gson.Gson;
import com.hw.entities.ProfileParams;
import com.hw.helpers.ImageHelper;
import com.hw.helpers.SignedBodyHelper;
import com.hw.http.RequestBuilder;
import com.hw.http.RequstURL;
import com.lidroid.xutils.http.client.multipart.MIME;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

public class ProfileRequest extends RequestBuilder {
    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpeg");
    private ProfileParams mProfileParams = new ProfileParams();

    public String getPath() {
        return RequstURL.SET_PROFILE;
    }

    public RequestBody post() {
        RequestBody requestBody = null;
        try {
            requestBody = new MultipartBuilder().type(MultipartBuilder.FORM).addPart(Headers.of(MIME.CONTENT_DISPOSITION, "form-data; name=\"signed_body\""), RequestBody.create(null, SignedBodyHelper.getSignedBody(new Gson().toJson(this.mProfileParams)))).addPart(Headers.of(MIME.CONTENT_DISPOSITION, "form-data; name=\"ig_sig_key_version\""), RequestBody.create(null, "5")).addPart(Headers.of(MIME.CONTENT_DISPOSITION, "form-data; name=\"profile_pic\"; filename=\"profile_pic\""), RequestBody.create(MEDIA_TYPE_JPG, ImageHelper.getImage())).build();
        } catch (Exception e) {
        }
        return requestBody;
    }
}
