package com.google.android.gms.internal;

import android.content.Intent;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.purchase.InAppPurchaseResult;

@zzhb
public class zzgk implements InAppPurchaseResult {
    private final zzgg zzGb;

    public zzgk(zzgg com_google_android_gms_internal_zzgg) {
        this.zzGb = com_google_android_gms_internal_zzgg;
    }

    public void finishPurchase() {
        try {
            this.zzGb.finishPurchase();
        } catch (Throwable e) {
            zzb.zzd("Could not forward finishPurchase to InAppPurchaseResult", e);
        }
    }

    public String getProductId() {
        try {
            return this.zzGb.getProductId();
        } catch (Throwable e) {
            zzb.zzd("Could not forward getProductId to InAppPurchaseResult", e);
            return null;
        }
    }

    public Intent getPurchaseData() {
        try {
            return this.zzGb.getPurchaseData();
        } catch (Throwable e) {
            zzb.zzd("Could not forward getPurchaseData to InAppPurchaseResult", e);
            return null;
        }
    }

    public int getResultCode() {
        try {
            return this.zzGb.getResultCode();
        } catch (Throwable e) {
            zzb.zzd("Could not forward getPurchaseData to InAppPurchaseResult", e);
            return 0;
        }
    }

    public boolean isVerified() {
        try {
            return this.zzGb.isVerified();
        } catch (Throwable e) {
            zzb.zzd("Could not forward isVerified to InAppPurchaseResult", e);
            return false;
        }
    }
}
