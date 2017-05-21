package com.hw.http.requests;

import com.google.gson.Gson;
import com.hw.entities.MediaConfigParams;
import com.hw.helpers.SignedBodyHelper;
import com.hw.http.RequestBuilder;
import com.hw.http.RequstURL;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

public class MediaConfigRequest extends RequestBuilder {
    private MediaConfigParams mMediaConfigParams;

    public MediaConfigRequest(long upload_id) {
        this.mMediaConfigParams = new MediaConfigParams(upload_id);
    }

    public String getPath() {
        return RequstURL.MEDIA_CONFIG;
    }

    public RequestBody post() {
        try {
            return new FormEncodingBuilder().add("signed_body", SignedBodyHelper.getSignedBody(new Gson().toJson(this.mMediaConfigParams))).add("ig_sig_key_version", "5").build();
        } catch (Exception e) {
            return null;
        }
    }
}
