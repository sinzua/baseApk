package com.nativex.monetization.activities;

import android.app.Activity;
import android.os.Build.VERSION;
import android.os.Bundle;
import com.nativex.common.Log;
import com.nativex.monetization.mraid.MRAIDManager;
import com.nativex.monetization.mraid.MRAIDUtils.PlacementType;

public class InterstitialActivity extends Activity {
    public static final String INTENT_EXTRA_AD_NAME = "mraidAdName";
    public static final String INTENT_EXTRA_SYSTEM_UI_VISIBILITY = "mraidSystemUIVisibility";
    public static final String INTENT_EXTRA_USER_CALL = "mraidUserCall";
    private String adName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateMRAID();
    }

    public void onBackPressed() {
        super.onBackPressed();
        onBackPressedMRAID();
    }

    public void onLowMemory() {
    }

    private void onCreateMRAID() {
        this.adName = getIntent().getStringExtra(INTENT_EXTRA_AD_NAME);
        boolean userCall = getIntent().getBooleanExtra(INTENT_EXTRA_USER_CALL, false);
        int visibility = getIntent().getIntExtra(INTENT_EXTRA_SYSTEM_UI_VISIBILITY, 0);
        if (VERSION.SDK_INT >= 19) {
            try {
                getWindow().getDecorView().setSystemUiVisibility(visibility);
            } catch (Exception e) {
                Log.e("Caught exception setting System UI Visibility: ", e);
            }
        }
        getIntent().removeExtra(INTENT_EXTRA_USER_CALL);
        Log.e("onCreateMRAID():Showing interstitial ad");
        if (!MRAIDManager.showInterstitial(this, this.adName, null, userCall)) {
            finish();
        }
    }

    private void onBackPressedMRAID() {
        MRAIDManager.releaseAd(this.adName, PlacementType.INTERSTITIAL);
    }
}
