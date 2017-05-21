package com.ty.helloworld.okhttp.requests;

import com.google.gson.Gson;
import com.ty.helloworld.entities.GetFriendShipParams;
import com.ty.helloworld.okhttp.RequestBuilder;

public class GetFriendShipRequest extends RequestBuilder {
    private long mUserId;

    public GetFriendShipRequest(long userid) {
        this.mUserId = userid;
    }

    public String getPath() {
        return RequstURL.FRIENDS;
    }

    public String post() {
        return new Gson().toJson(new GetFriendShipParams(this.mUserId));
    }
}
