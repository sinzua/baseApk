package com.amazon.device.iap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.amazon.device.iap.internal.d;
import com.amazon.device.iap.internal.util.e;

public final class ResponseReceiver extends BroadcastReceiver {
    private static final String TAG = ResponseReceiver.class.getSimpleName();

    public void onReceive(Context context, Intent intent) {
        try {
            d.d().a(context, intent);
        } catch (Exception e) {
            e.b(TAG, "Error in onReceive: " + e);
        }
    }
}
