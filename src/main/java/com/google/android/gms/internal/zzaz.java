package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import org.json.JSONObject;

@zzhb
public class zzaz extends zzau {
    private final zzeh zzsE;

    public zzaz(Context context, AdSizeParcel adSizeParcel, zzif com_google_android_gms_internal_zzif, VersionInfoParcel versionInfoParcel, zzbb com_google_android_gms_internal_zzbb, zzeh com_google_android_gms_internal_zzeh) {
        super(context, adSizeParcel, com_google_android_gms_internal_zzif, versionInfoParcel, com_google_android_gms_internal_zzbb);
        this.zzsE = com_google_android_gms_internal_zzeh;
        zzb(this.zzsE);
        zzcd();
        zzh(false);
        zzb.zzaI("Tracking ad unit: " + this.zzrZ.zzcu());
    }

    protected void destroy() {
        synchronized (this.zzpV) {
            super.destroy();
            zzc(this.zzsE);
        }
    }

    protected void zzb(JSONObject jSONObject) {
        this.zzsE.zza("AFMA_updateActiveView", jSONObject);
    }

    public void zzcf() {
        destroy();
    }

    protected boolean zzcl() {
        return true;
    }
}
