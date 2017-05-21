package com.google.android.gms.ads.internal.client;

import com.google.android.gms.ads.doubleclick.AppEventListener;
import com.google.android.gms.ads.internal.client.zzw.zza;
import com.google.android.gms.internal.zzhb;

@zzhb
public final class zzj extends zza {
    private final AppEventListener zzun;

    public zzj(AppEventListener appEventListener) {
        this.zzun = appEventListener;
    }

    public void onAppEvent(String name, String info) {
        this.zzun.onAppEvent(name, info);
    }
}
