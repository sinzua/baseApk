package com.flurry.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.flurry.sdk.hm;
import com.flurry.sdk.kg;
import com.flurry.sdk.lt;
import com.supersonicads.sdk.utils.Constants.RequestParameters;

public final class FlurryInstallReceiver extends BroadcastReceiver {
    static final String a = FlurryInstallReceiver.class.getSimpleName();

    public void onReceive(Context context, Intent intent) {
        kg.a(4, a, "Received an Install nofication of " + intent.getAction());
        String string = intent.getExtras().getString("referrer");
        kg.a(4, a, "Received an Install referrer of " + string);
        if (string == null || !"com.android.vending.INSTALL_REFERRER".equals(intent.getAction())) {
            kg.a(5, a, "referrer is null");
            return;
        }
        if (!string.contains(RequestParameters.EQUAL)) {
            kg.a(4, a, "referrer is before decoding: " + string);
            string = lt.d(string);
            kg.a(4, a, "referrer is: " + string);
        }
        new hm(context).a(string);
    }
}
