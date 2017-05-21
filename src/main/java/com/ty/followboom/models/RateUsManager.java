package com.ty.followboom.models;

import android.content.Context;
import com.ty.followboom.helpers.AppConfigHelper;
import com.ty.followboom.helpers.VLTools;
import com.ty.followboom.okhttp.RequestCallback;
import com.ty.followboom.okhttp.responses.GetAccountInfoResponse;

public class RateUsManager {
    private static RateUsManager sRateUsManager;
    private long mRateTimeStamp;

    public static RateUsManager getSingleton() {
        if (sRateUsManager == null) {
            synchronized (RateUsManager.class) {
                if (sRateUsManager == null) {
                    sRateUsManager = new RateUsManager();
                }
            }
        }
        return sRateUsManager;
    }

    public void trigger() {
        this.mRateTimeStamp = System.currentTimeMillis();
    }

    public void tryRecord(Context context, RequestCallback<GetAccountInfoResponse> accountInfoCallback) {
        if (this.mRateTimeStamp > 0 && System.currentTimeMillis() - this.mRateTimeStamp > VLTools.DEFAULT_RATE_US_THREHOLD) {
            AppConfigHelper.saveRateVersion(context, VLTools.getAppInfo(context));
            LikeServerInstagram.getSingleton().rateUs(context, 0, accountInfoCallback);
        }
    }

    public void forceRecord(Context context) {
        AppConfigHelper.saveRateVersion(context, VLTools.getAppInfo(context));
    }

    public boolean needRateUs(Context context) {
        if (AppConfigHelper.isPro(context) || VLTools.getAppInfo(context).equals(AppConfigHelper.getLastRateVersion(context))) {
            return false;
        }
        return true;
    }
}
