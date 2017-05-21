package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.client.zza;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.overlay.zzg;
import com.google.android.gms.ads.internal.overlay.zzp;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.zze;
import com.google.android.gms.ads.internal.zzr;
import com.supersonicads.sdk.precache.DownloadManager;
import org.json.JSONObject;

@zzhb
public class zzef implements zzed {
    private final zzjp zzpD;

    public zzef(Context context, VersionInfoParcel versionInfoParcel, zzan com_google_android_gms_internal_zzan) {
        this.zzpD = zzr.zzbD().zza(context, new AdSizeParcel(), false, false, com_google_android_gms_internal_zzan, versionInfoParcel);
        this.zzpD.getWebView().setWillNotDraw(true);
    }

    private void runOnUiThread(Runnable runnable) {
        if (zzn.zzcS().zzhJ()) {
            runnable.run();
        } else {
            zzir.zzMc.post(runnable);
        }
    }

    public void destroy() {
        this.zzpD.destroy();
    }

    public void zzZ(String str) {
        final String format = String.format("<!DOCTYPE html><html><head><script src=\"%s\"></script></head><body></body></html>", new Object[]{str});
        runOnUiThread(new Runnable(this) {
            final /* synthetic */ zzef zzAU;

            public void run() {
                this.zzAU.zzpD.loadData(format, "text/html", DownloadManager.UTF8_CHARSET);
            }
        });
    }

    public void zza(zza com_google_android_gms_ads_internal_client_zza, zzg com_google_android_gms_ads_internal_overlay_zzg, zzdb com_google_android_gms_internal_zzdb, zzp com_google_android_gms_ads_internal_overlay_zzp, boolean z, zzdh com_google_android_gms_internal_zzdh, zzdj com_google_android_gms_internal_zzdj, zze com_google_android_gms_ads_internal_zze, zzft com_google_android_gms_internal_zzft) {
        this.zzpD.zzhU().zzb(com_google_android_gms_ads_internal_client_zza, com_google_android_gms_ads_internal_overlay_zzg, com_google_android_gms_internal_zzdb, com_google_android_gms_ads_internal_overlay_zzp, z, com_google_android_gms_internal_zzdh, com_google_android_gms_internal_zzdj, new zze(false), com_google_android_gms_internal_zzft);
    }

    public void zza(final zzed.zza com_google_android_gms_internal_zzed_zza) {
        this.zzpD.zzhU().zza(new zzjq.zza(this) {
            final /* synthetic */ zzef zzAU;

            public void zza(zzjp com_google_android_gms_internal_zzjp, boolean z) {
                com_google_android_gms_internal_zzed_zza.zzeo();
            }
        });
    }

    public void zza(String str, zzdf com_google_android_gms_internal_zzdf) {
        this.zzpD.zzhU().zza(str, com_google_android_gms_internal_zzdf);
    }

    public void zza(final String str, final JSONObject jSONObject) {
        runOnUiThread(new Runnable(this) {
            final /* synthetic */ zzef zzAU;

            public void run() {
                this.zzAU.zzpD.zza(str, jSONObject);
            }
        });
    }

    public void zzaa(final String str) {
        runOnUiThread(new Runnable(this) {
            final /* synthetic */ zzef zzAU;

            public void run() {
                this.zzAU.zzpD.loadUrl(str);
            }
        });
    }

    public void zzab(final String str) {
        runOnUiThread(new Runnable(this) {
            final /* synthetic */ zzef zzAU;

            public void run() {
                this.zzAU.zzpD.loadData(str, "text/html", DownloadManager.UTF8_CHARSET);
            }
        });
    }

    public void zzb(String str, zzdf com_google_android_gms_internal_zzdf) {
        this.zzpD.zzhU().zzb(str, com_google_android_gms_internal_zzdf);
    }

    public void zzb(String str, JSONObject jSONObject) {
        this.zzpD.zzb(str, jSONObject);
    }

    public void zze(final String str, final String str2) {
        runOnUiThread(new Runnable(this) {
            final /* synthetic */ zzef zzAU;

            public void run() {
                this.zzAU.zzpD.zze(str, str2);
            }
        });
    }

    public zzei zzen() {
        return new zzej(this);
    }
}
