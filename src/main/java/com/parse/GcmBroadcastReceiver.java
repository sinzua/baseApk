package com.parse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class GcmBroadcastReceiver extends BroadcastReceiver {
    public final void onReceive(Context context, Intent intent) {
        ServiceUtils.runWakefulIntentInService(context, intent, PushService.class);
    }
}
