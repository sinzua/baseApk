package com.ty.followboom.okhttp.requests;

import com.google.gson.Gson;
import com.ty.followboom.entities.GetLikesParams;
import com.ty.followboom.okhttp.RequestBuilder;

public class GetLikesRequest extends RequestBuilder {
    public static int GOODS_TYPE_LIKE = 2;
    public static int GOODS_TYPE_LOOPS = 5;
    public static int GOODS_TYPE_REVINE = 4;
    private String mGoodsId;
    private int mGoodsType;
    private String mMediaId;
    private String mPostCode;
    private String mSessionToken;
    private int mStartAt;
    private String mThumbnailUrl;
    private String mUserName;
    private String mUserid;
    private String mVideoLowURL;
    private String mVideoUrl;

    public GetLikesRequest(String userid, String sessionToken, String mediaId, String videoUrl, String videoLowURL, String thumbnailUrl, String goodsId, int goodsType, String userName, String postCode, int startAt) {
        this.mUserid = userid;
        this.mSessionToken = sessionToken;
        this.mMediaId = mediaId;
        this.mVideoUrl = videoUrl;
        this.mVideoLowURL = videoLowURL;
        this.mThumbnailUrl = thumbnailUrl;
        this.mGoodsId = goodsId;
        this.mGoodsType = goodsType;
        this.mUserName = userName;
        this.mPostCode = postCode;
        this.mStartAt = startAt;
    }

    public String getPath() {
        return String.format(RequstURL.GET_LIKES, new Object[]{this.mUserid, this.mSessionToken});
    }

    public String post() {
        return new Gson().toJson(new GetLikesParams(this.mMediaId, this.mVideoUrl, this.mVideoLowURL, this.mThumbnailUrl, this.mGoodsId, this.mGoodsType, this.mUserName, this.mPostCode, this.mStartAt));
    }
}
