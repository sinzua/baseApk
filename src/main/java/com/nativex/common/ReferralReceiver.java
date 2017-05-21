package com.nativex.common;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import com.nativex.common.JsonRequestConstants.AndroidMarketInputs;
import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class ReferralReceiver extends BroadcastReceiver {
    private static final String[] EXPECTED_PARAMETERS = new String[]{AndroidMarketInputs.SOURCE, AndroidMarketInputs.MEDIUM, AndroidMarketInputs.TERM, AndroidMarketInputs.CONTENT, AndroidMarketInputs.CAMPAIGN};
    private static final String PREFS_FILE_NAME = "ReferralParamsFile";

    public void onReceive(Context context, Intent intent) {
        try {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                extras.containsKey(null);
            }
            Map<String, String> referralParams = new HashMap();
            if (intent.getAction().equals("com.android.vending.INSTALL_REFERRER")) {
                String referrer = intent.getStringExtra("referrer");
                if (referrer != null && referrer.length() != 0) {
                    Log.d("Referral Information Encoded: " + referrer);
                    referrer = URLDecoder.decode(referrer);
                    Log.d("Referral Information Decoded: " + referrer);
                    for (String param : referrer.split(RequestParameters.AMPERSAND)) {
                        String[] pair = param.split(RequestParameters.EQUAL);
                        if (pair.length == 2) {
                            referralParams.put(pair[0], pair[1]);
                        }
                    }
                    String packageName = context.getPackageName();
                    Log.d("Package Name: " + packageName + "; Referral Package Name: " + ((String) referralParams.get(AndroidMarketInputs.CAMPAIGN)));
                    if (referralParams.get(AndroidMarketInputs.CAMPAIGN) != null && packageName.equalsIgnoreCase((String) referralParams.get(AndroidMarketInputs.CAMPAIGN))) {
                        storeReferralParams(context, referralParams);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("ReferralReceiver: Exception caught", e);
        }
    }

    @SuppressLint({"CommitPrefEdits"})
    private static void storeReferralParams(Context context, Map<String, String> params) {
        int i = 0;
        Editor editor = context.getSharedPreferences(PREFS_FILE_NAME, 0).edit();
        String[] strArr = EXPECTED_PARAMETERS;
        int length = strArr.length;
        while (i < length) {
            String key = strArr[i];
            editor.putString(key, (String) params.get(key));
            i++;
        }
        editor.commit();
    }
}
