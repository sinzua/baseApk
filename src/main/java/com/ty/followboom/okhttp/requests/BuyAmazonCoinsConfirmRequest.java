package com.ty.followboom.okhttp.requests;

import android.content.Context;
import com.google.gson.Gson;
import com.ty.followboom.entities.BuyCoinsConfirmParams;
import com.ty.followboom.okhttp.RequestBuilder;

public class BuyAmazonCoinsConfirmRequest extends RequestBuilder {
    private String mAmazonUserId;
    private Context mContext;
    private String mOriginalReceipt;
    private String mProductId;
    private String mSessionToken;
    private String mUserid;

    public BuyAmazonCoinsConfirmRequest(Context context, String userid, String sessionToken, String productId, String originalReceipt, String amazonUserId) {
        this.mContext = context;
        this.mUserid = userid;
        this.mSessionToken = sessionToken;
        this.mProductId = productId;
        this.mOriginalReceipt = originalReceipt;
        this.mAmazonUserId = amazonUserId;
    }

    public String getPath() {
        return String.format(RequstURL.BUY_COINS_CONFIRM, new Object[]{this.mUserid, this.mSessionToken});
    }

    public String post() {
        return new Gson().toJson(new BuyCoinsConfirmParams(this.mContext.getPackageName(), this.mProductId, this.mOriginalReceipt, this.mAmazonUserId));
    }
}
