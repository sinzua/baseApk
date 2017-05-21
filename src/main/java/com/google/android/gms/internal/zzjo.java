package com.google.android.gms.internal;

import android.content.Context;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import com.google.android.gms.ads.internal.overlay.zzk;
import com.google.android.gms.common.internal.zzx;

@zzhb
public class zzjo {
    private final Context mContext;
    private zzk zzFo;
    private final ViewGroup zzNx;
    private final zzjp zzpD;

    public zzjo(Context context, ViewGroup viewGroup, zzjp com_google_android_gms_internal_zzjp) {
        this(context, viewGroup, com_google_android_gms_internal_zzjp, null);
    }

    zzjo(Context context, ViewGroup viewGroup, zzjp com_google_android_gms_internal_zzjp, zzk com_google_android_gms_ads_internal_overlay_zzk) {
        this.mContext = context;
        this.zzNx = viewGroup;
        this.zzpD = com_google_android_gms_internal_zzjp;
        this.zzFo = com_google_android_gms_ads_internal_overlay_zzk;
    }

    public void onDestroy() {
        zzx.zzcD("onDestroy must be called from the UI thread.");
        if (this.zzFo != null) {
            this.zzFo.destroy();
        }
    }

    public void onPause() {
        zzx.zzcD("onPause must be called from the UI thread.");
        if (this.zzFo != null) {
            this.zzFo.pause();
        }
    }

    public void zza(int i, int i2, int i3, int i4, int i5) {
        if (this.zzFo == null) {
            zzbx.zza(this.zzpD.zzic().zzdA(), this.zzpD.zzib(), "vpr");
            zzbz zzb = zzbx.zzb(this.zzpD.zzic().zzdA());
            this.zzFo = new zzk(this.mContext, this.zzpD, i5, this.zzpD.zzic().zzdA(), zzb);
            this.zzNx.addView(this.zzFo, 0, new LayoutParams(-1, -1));
            this.zzFo.zzd(i, i2, i3, i4);
            this.zzpD.zzhU().zzG(false);
        }
    }

    public void zze(int i, int i2, int i3, int i4) {
        zzx.zzcD("The underlay may only be modified from the UI thread.");
        if (this.zzFo != null) {
            this.zzFo.zzd(i, i2, i3, i4);
        }
    }

    public zzk zzhM() {
        zzx.zzcD("getAdVideoUnderlay must be called from the UI thread.");
        return this.zzFo;
    }
}
