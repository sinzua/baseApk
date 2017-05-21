package com.ty.followboom.okhttp.requests;

import com.google.gson.Gson;
import com.ty.followboom.entities.GetFollowersParams;
import com.ty.followboom.okhttp.RequestBuilder;

public class GetFollowersRequest extends RequestBuilder {
    private String mAvatarUrl;
    private String mGoodsId;
    private String mSessionToken;
    private int mStartAt;
    private String mUserName;
    private String mUserid;

    public GetFollowersRequest(String userid, String sessionToken, String avatarUrl, String goodsId, String userName, int startAt) {
        this.mUserid = userid;
        this.mSessionToken = sessionToken;
        this.mAvatarUrl = avatarUrl;
        this.mGoodsId = goodsId;
        this.mUserName = userName;
        this.mStartAt = startAt;
    }

    public String getPath() {
        return String.format(RequstURL.GET_FOLLOWERS, new Object[]{this.mUserid, this.mSessionToken});
    }

    public String post() {
        return new Gson().toJson(new GetFollowersParams(this.mAvatarUrl, this.mGoodsId, this.mUserName, this.mStartAt));
    }
}
