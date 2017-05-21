package com.ty.followboom.okhttp.requests;

import com.google.gson.Gson;
import com.ty.followboom.entities.GetViewsParams;
import com.ty.followboom.okhttp.RequestBuilder;

public class GetViewsRequest extends RequestBuilder {
    private String mGoodsId;
    private int mGoodsType;
    private String mMediaId;
    private String mPostCode;
    private String mSessionToken;
    private int mStartAt;
    private Long mTargetUserId;
    private String mThumbnailUrl;
    private Long mTimestamp;
    private String mTrackingToken;
    private String mUserName;
    private String mUserid;
    private String mVideoLowURL;
    private String mVideoUrl;

    public GetViewsRequest(String userid, String sessionToken, String mediaId, String videoUrl, String videoLowURL, String thumbnailUrl, String goodsId, int goodsType, String userName, String postCode, String trackingToken, Long targetUserId, Long timestamp, int startAt) {
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
        this.mTrackingToken = trackingToken;
        this.mTargetUserId = targetUserId;
        this.mTimestamp = timestamp;
        this.mStartAt = startAt;
    }

    public String getPath() {
        return String.format(RequstURL.GET_VIEWS, new Object[]{this.mUserid, this.mSessionToken});
    }

    public String post() {
        return new Gson().toJson(new GetViewsParams(this.mMediaId, this.mVideoUrl, this.mVideoLowURL, this.mThumbnailUrl, this.mGoodsId, this.mGoodsType, this.mUserName, this.mPostCode, this.mTrackingToken, this.mTargetUserId, this.mTimestamp, this.mStartAt));
    }
}
