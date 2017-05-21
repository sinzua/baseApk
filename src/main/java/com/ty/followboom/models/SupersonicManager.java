package com.ty.followboom.models;

import android.app.Activity;
import android.util.Log;
import com.forwardwin.base.widgets.ToastHelper;
import com.supersonic.adapters.supersonicads.SupersonicConfig;
import com.supersonic.mediationsdk.logger.SupersonicError;
import com.supersonic.mediationsdk.sdk.OfferwallListener;
import com.supersonic.mediationsdk.sdk.Supersonic;
import com.supersonic.mediationsdk.sdk.SupersonicFactory;
import com.supersonicads.sdk.agent.SupersonicAdsAdvertiserAgent;
import com.ty.followboom.helpers.AppConfigHelper;

public class SupersonicManager {
    private static final String TAG = "SupersonicManager";
    private static SupersonicManager sSupersonicManager;
    private Activity mActivity;
    private OfferwallListener mOfferwallListener = new OfferwallListener() {
        public void onOfferwallInitSuccess() {
            Log.d(SupersonicManager.TAG, "onOfferwallInitSuccess");
        }

        public void onOfferwallInitFail(SupersonicError supersonicError) {
            Log.d(SupersonicManager.TAG, "onOfferwallInitFail");
        }

        public void onOfferwallOpened() {
            Log.d(SupersonicManager.TAG, "onOfferwallOpened");
        }

        public void onOfferwallShowFail(SupersonicError supersonicError) {
            Log.d(SupersonicManager.TAG, "onOfferwallShowFail");
        }

        public boolean onOfferwallAdCredited(int i, int i1, boolean b) {
            ToastHelper.showToast(SupersonicManager.this.mActivity, "onOfferwallAdCredited i:" + i + " i1:" + i1 + " b:" + b);
            return false;
        }

        public void onGetOfferwallCreditsFail(SupersonicError supersonicError) {
            Log.d(SupersonicManager.TAG, "onGetOfferwallCreditsFail");
        }

        public void onOfferwallClosed() {
            Log.d(SupersonicManager.TAG, "onOfferwallClosed");
        }
    };
    private Supersonic mSupersonicInstance;

    public static SupersonicManager getSingleton() {
        if (sSupersonicManager == null) {
            synchronized (SupersonicManager.class) {
                if (sSupersonicManager == null) {
                    sSupersonicManager = new SupersonicManager();
                }
            }
        }
        return sSupersonicManager;
    }

    public void init(Activity activity) {
        this.mActivity = activity;
        this.mSupersonicInstance = SupersonicFactory.getInstance();
        SupersonicAdsAdvertiserAgent.getInstance().reportAppStarted(activity);
        this.mSupersonicInstance.setOfferwallListener(this.mOfferwallListener);
        SupersonicConfig.getConfigObj().setClientSideCallbacks(true);
        Log.e(TAG, "InstaId: " + UserInfoManager.getSingleton().getUserInfo(activity).getUserId());
        this.mSupersonicInstance.initOfferwall(activity, AppConfigHelper.SUPERSONIC_APP_KEY, UserInfoManager.getSingleton().getUserInfo(activity).getUserId());
    }

    public void show() {
        if (this.mSupersonicInstance.isOfferwallAvailable()) {
            this.mSupersonicInstance.showOfferwall();
        } else {
            ToastHelper.showToast(this.mActivity, "SuperSonic not initialized, wait a moment!");
        }
    }
}
