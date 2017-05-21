package com.ty.followboom.models;

import android.app.Activity;
import android.util.Log;
import com.forwardwin.base.widgets.ToastHelper;
import com.nativex.monetization.MonetizationManager;
import com.nativex.monetization.enums.AdEvent;
import com.nativex.monetization.enums.NativeXAdPlacement;
import com.nativex.monetization.listeners.OnAdEvent;
import com.nativex.monetization.listeners.SessionListener;
import com.ty.followboom.helpers.AppConfigHelper;

public class NativeXManager {
    private static final String TAG = "NativeXManager";
    private static NativeXManager sNativeXManager;
    private Activity mActivity;
    private OnAdEvent mAdEventListener = new OnAdEvent() {
        public void onEvent(AdEvent adEvent, String s) {
            Log.d(NativeXManager.TAG, s);
        }
    };
    private SessionListener sessionListener = new SessionListener() {
        public void createSessionCompleted(boolean success, boolean isOfferWallEnabled, String sessionId) {
            if (success) {
                Log.d(NativeXManager.TAG, "Get ad succeed!");
                MonetizationManager.fetchAd(NativeXManager.this.mActivity, NativeXAdPlacement.Store_Open, NativeXManager.this.mAdEventListener);
                return;
            }
            Log.d(NativeXManager.TAG, "NativeX set up uncorrectly!");
        }
    };

    public static NativeXManager getSingleton() {
        if (sNativeXManager == null) {
            synchronized (NativeXManager.class) {
                if (sNativeXManager == null) {
                    sNativeXManager = new NativeXManager();
                }
            }
        }
        return sNativeXManager;
    }

    public void init(Activity activity) {
        this.mActivity = activity;
        if (!MonetizationManager.isInitialized()) {
            MonetizationManager.createSession(activity, AppConfigHelper.NATIVEX_APPID, UserInfoManager.getSingleton().getUserInfo(activity).getUserId(), this.sessionListener);
        } else if (!MonetizationManager.isAdReady(NativeXAdPlacement.Store_Open)) {
            MonetizationManager.fetchAd(this.mActivity, NativeXAdPlacement.Store_Open, this.mAdEventListener);
        }
    }

    public void show() {
        if (MonetizationManager.isAdReady(NativeXAdPlacement.Store_Open)) {
            MonetizationManager.showReadyAd(this.mActivity, NativeXAdPlacement.Store_Open, null);
        } else {
            ToastHelper.showToast(this.mActivity, "NativeX not initialized, wait a moment!");
        }
    }
}
