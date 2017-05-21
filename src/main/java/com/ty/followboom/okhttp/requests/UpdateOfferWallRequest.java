package com.ty.followboom.okhttp.requests;

import android.content.Context;
import com.google.gson.Gson;
import com.ty.followboom.entities.QueryOfferWallRequestParams;
import com.ty.followboom.entities.QueryOfferWallResult;
import com.ty.followboom.helpers.AppConfigHelper;
import com.ty.followboom.helpers.VLTools;
import com.ty.followboom.okhttp.RequestBuilder;

public class UpdateOfferWallRequest extends RequestBuilder {
    private Context mContext;
    private String mOfferwallPlatform;
    private String mSessionToken;
    private String mUserid;

    public UpdateOfferWallRequest(Context context, String userid, String sessionToken, String offerwallPlatform) {
        this.mContext = context;
        this.mUserid = userid;
        this.mSessionToken = sessionToken;
        this.mOfferwallPlatform = offerwallPlatform;
    }

    public String getPath() {
        return String.format(RequstURL.UPDATE_OFFERWALL, new Object[]{this.mUserid, this.mSessionToken});
    }

    public String post() {
        Object queryOfferWallRequestParams;
        QueryOfferWallResult queryOfferWallResult = AppConfigHelper.getOfferWallItemByName(this.mContext, this.mOfferwallPlatform);
        if (queryOfferWallResult == null) {
            try {
                queryOfferWallRequestParams = new QueryOfferWallRequestParams(0, this.mOfferwallPlatform, VLTools.getDeviceId(this.mContext), "android");
            } catch (Exception e) {
                queryOfferWallRequestParams = new QueryOfferWallRequestParams(0, this.mOfferwallPlatform, "", "android");
            }
        } else {
            queryOfferWallRequestParams = new QueryOfferWallRequestParams(queryOfferWallResult.getCoinsOfOfferwall().intValue(), this.mOfferwallPlatform, VLTools.getDeviceId(this.mContext), "android");
        }
        return new Gson().toJson(queryOfferWallRequestParams);
    }
}
