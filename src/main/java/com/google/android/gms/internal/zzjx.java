package com.google.android.gms.internal;

import android.annotation.TargetApi;
import android.view.View;
import android.webkit.WebChromeClient.CustomViewCallback;

@TargetApi(14)
@zzhb
public final class zzjx extends zzjv {
    public zzjx(zzjp com_google_android_gms_internal_zzjp) {
        super(com_google_android_gms_internal_zzjp);
    }

    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback customViewCallback) {
        zza(view, requestedOrientation, customViewCallback);
    }
}
