package com.ty.http.requests;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.ty.entities.ShowManyParams;
import com.ty.http.RequestBuilder;
import java.util.ArrayList;

public class ShowManyRequest extends RequestBuilder {
    private ShowManyParams mShowManyParams;

    public ShowManyRequest(ArrayList<Long> userIds) {
        this.mShowManyParams = new ShowManyParams(userIds);
    }

    public String getPath() {
        return "https://i.instagram.com/api/v1/friendships/show_many/";
    }

    public RequestBody post() {
        try {
            return new FormEncodingBuilder().add("user_ids", this.mShowManyParams.getUser_ids()).build();
        } catch (Exception e) {
            return null;
        }
    }
}
