package com.google.android.gms.ads.internal.formats;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzan;
import com.google.android.gms.internal.zzfb;
import com.google.android.gms.internal.zzfc;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzjp;
import java.lang.ref.WeakReference;
import java.util.Map;
import org.json.JSONObject;

@zzhb
public class zzg extends zzi {
    private Object zzpV;
    private zzfb zzyl;
    private zzfc zzym;
    private final zzp zzyn;
    private zzh zzyo;
    private boolean zzyp;

    private zzg(Context context, zzp com_google_android_gms_ads_internal_zzp, zzan com_google_android_gms_internal_zzan) {
        super(context, com_google_android_gms_ads_internal_zzp, null, com_google_android_gms_internal_zzan, null, null, null);
        this.zzyp = false;
        this.zzpV = new Object();
        this.zzyn = com_google_android_gms_ads_internal_zzp;
    }

    public zzg(Context context, zzp com_google_android_gms_ads_internal_zzp, zzan com_google_android_gms_internal_zzan, zzfb com_google_android_gms_internal_zzfb) {
        this(context, com_google_android_gms_ads_internal_zzp, com_google_android_gms_internal_zzan);
        this.zzyl = com_google_android_gms_internal_zzfb;
    }

    public zzg(Context context, zzp com_google_android_gms_ads_internal_zzp, zzan com_google_android_gms_internal_zzan, zzfc com_google_android_gms_internal_zzfc) {
        this(context, com_google_android_gms_ads_internal_zzp, com_google_android_gms_internal_zzan);
        this.zzym = com_google_android_gms_internal_zzfc;
    }

    public void recordImpression() {
        zzx.zzcD("recordImpression must be called on the main UI thread.");
        synchronized (this.zzpV) {
            zzn(true);
            if (this.zzyo != null) {
                this.zzyo.recordImpression();
            } else {
                try {
                    if (this.zzyl != null && !this.zzyl.getOverrideImpressionRecording()) {
                        this.zzyl.recordImpression();
                    } else if (!(this.zzym == null || this.zzym.getOverrideImpressionRecording())) {
                        this.zzym.recordImpression();
                    }
                } catch (Throwable e) {
                    zzb.zzd("Failed to call recordImpression", e);
                }
            }
            this.zzyn.recordImpression();
        }
    }

    public zzb zza(OnClickListener onClickListener) {
        return null;
    }

    public void zza(View view, Map<String, WeakReference<View>> map, JSONObject jSONObject, JSONObject jSONObject2, JSONObject jSONObject3) {
        zzx.zzcD("performClick must be called on the main UI thread.");
        synchronized (this.zzpV) {
            if (this.zzyo != null) {
                this.zzyo.zza(view, map, jSONObject, jSONObject2, jSONObject3);
                this.zzyn.onAdClicked();
            } else {
                try {
                    if (!(this.zzyl == null || this.zzyl.getOverrideClickHandling())) {
                        this.zzyl.zzc(zze.zzC(view));
                        this.zzyn.onAdClicked();
                    }
                    if (!(this.zzym == null || this.zzym.getOverrideClickHandling())) {
                        this.zzym.zzc(zze.zzC(view));
                        this.zzyn.onAdClicked();
                    }
                } catch (Throwable e) {
                    zzb.zzd("Failed to call performClick", e);
                }
            }
        }
    }

    public void zzc(zzh com_google_android_gms_ads_internal_formats_zzh) {
        synchronized (this.zzpV) {
            this.zzyo = com_google_android_gms_ads_internal_formats_zzh;
        }
    }

    public boolean zzdP() {
        boolean z;
        synchronized (this.zzpV) {
            z = this.zzyp;
        }
        return z;
    }

    public zzh zzdQ() {
        zzh com_google_android_gms_ads_internal_formats_zzh;
        synchronized (this.zzpV) {
            com_google_android_gms_ads_internal_formats_zzh = this.zzyo;
        }
        return com_google_android_gms_ads_internal_formats_zzh;
    }

    public zzjp zzdR() {
        return null;
    }

    public void zzg(View view) {
        synchronized (this.zzpV) {
            this.zzyp = true;
            try {
                if (this.zzyl != null) {
                    this.zzyl.zzd(zze.zzC(view));
                } else if (this.zzym != null) {
                    this.zzym.zzd(zze.zzC(view));
                }
            } catch (Throwable e) {
                zzb.zzd("Failed to call prepareAd", e);
            }
            this.zzyp = false;
        }
    }
}
