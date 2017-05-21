package com.hw.http.requests;

import com.hw.helpers.ImageHelper;
import com.hw.http.RequestBuilder;
import com.hw.http.RequstURL;
import com.lidroid.xutils.http.client.multipart.MIME;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

public class UploadRequest extends RequestBuilder {
    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpeg");
    private long upload_id;

    public UploadRequest(long upload_id) {
        this.upload_id = upload_id;
    }

    public String getPath() {
        return RequstURL.UPLOAD;
    }

    public RequestBody post() {
        RequestBody requestBody = null;
        try {
            requestBody = new MultipartBuilder().type(MultipartBuilder.FORM).addPart(Headers.of(MIME.CONTENT_DISPOSITION, "form-data; name=\"upload_id\""), RequestBody.create(null, Long.toString(this.upload_id))).addPart(Headers.of(MIME.CONTENT_DISPOSITION, "form-data; name=\"image_compression\""), RequestBody.create(null, "{\"lib_version\":\"847.240000\", \"lib_name\":\"uikit\", \"quality\":45}")).addPart(Headers.of(MIME.CONTENT_DISPOSITION, "form-data; name=\"photo\"; filename=\"photo\""), RequestBody.create(MEDIA_TYPE_JPG, ImageHelper.getImage())).build();
        } catch (Exception e) {
        }
        return requestBody;
    }
}
