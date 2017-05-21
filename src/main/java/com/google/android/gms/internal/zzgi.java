package com.google.android.gms.internal;

import com.google.android.gms.ads.purchase.InAppPurchaseListener;
import com.google.android.gms.internal.zzgd.zza;

@zzhb
public final class zzgi extends zza {
    private final InAppPurchaseListener zzuO;

    public zzgi(InAppPurchaseListener inAppPurchaseListener) {
        this.zzuO = inAppPurchaseListener;
    }

    public void zza(zzgc com_google_android_gms_internal_zzgc) {
        this.zzuO.onInAppPurchaseRequested(new zzgl(com_google_android_gms_internal_zzgc));
    }
}
