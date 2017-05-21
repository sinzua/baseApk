package com.ty.followboom.okhttp.requests;

import android.content.Context;
import com.google.gson.Gson;
import com.ty.followboom.entities.BuyCoinsConfirmParams;
import com.ty.followboom.okhttp.RequestBuilder;

public class BuyCoinsConfirmRequest extends RequestBuilder {
    private String mAndroidSignature;
    private String mAndroidSignedData;
    private Context mContext;
    private String mProductId;
    private String mSessionToken;
    private String mToken;
    private String mUserid;

    public BuyCoinsConfirmRequest(Context context, String userid, String sessionToken, String productId, String token, String androidSignedData, String androidSignature) {
        this.mContext = context;
        this.mUserid = userid;
        this.mSessionToken = sessionToken;
        this.mProductId = productId;
        this.mToken = token;
        this.mAndroidSignedData = androidSignedData;
        this.mAndroidSignature = androidSignature;
    }

    public String getPath() {
        return String.format(RequstURL.BUY_COINS_CONFIRM, new Object[]{this.mUserid, this.mSessionToken});
    }

    public String post() {
        return new Gson().toJson(new BuyCoinsConfirmParams(this.mContext.getPackageName(), this.mProductId, this.mToken, this.mAndroidSignedData, this.mAndroidSignature));
    }
}
