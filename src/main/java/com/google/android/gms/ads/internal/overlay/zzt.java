package com.google.android.gms.ads.internal.overlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.TextureView;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.internal.zzbt;
import com.google.android.gms.internal.zzbx;
import com.google.android.gms.internal.zzbz;
import com.google.android.gms.internal.zzcb;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zziv;
import com.google.android.gms.internal.zziv.zza;
import com.google.android.gms.internal.zziv.zzb;
import java.util.concurrent.TimeUnit;

@zzhb
public class zzt {
    private final Context mContext;
    private final String zzEY;
    private final VersionInfoParcel zzEZ;
    @Nullable
    private final zzbz zzFa;
    @Nullable
    private final zzcb zzFb;
    private final zziv zzFc = new zzb().zza("min_1", Double.MIN_VALUE, 1.0d).zza("1_5", 1.0d, 5.0d).zza("5_10", 5.0d, 10.0d).zza("10_20", 10.0d, 20.0d).zza("20_30", 20.0d, 30.0d).zza("30_max", 30.0d, Double.MAX_VALUE).zzhA();
    private final long[] zzFd;
    private final String[] zzFe;
    @Nullable
    private zzbz zzFf;
    @Nullable
    private zzbz zzFg;
    @Nullable
    private zzbz zzFh;
    @Nullable
    private zzbz zzFi;
    private boolean zzFj;
    private zzi zzFk;
    private boolean zzFl;
    private boolean zzFm;
    private long zzFn = -1;

    public zzt(Context context, VersionInfoParcel versionInfoParcel, String str, @Nullable zzcb com_google_android_gms_internal_zzcb, @Nullable zzbz com_google_android_gms_internal_zzbz) {
        this.mContext = context;
        this.zzEZ = versionInfoParcel;
        this.zzEY = str;
        this.zzFb = com_google_android_gms_internal_zzcb;
        this.zzFa = com_google_android_gms_internal_zzbz;
        String str2 = (String) zzbt.zzvV.get();
        if (str2 == null) {
            this.zzFe = new String[0];
            this.zzFd = new long[0];
            return;
        }
        String[] split = TextUtils.split(str2, ",");
        this.zzFe = new String[split.length];
        this.zzFd = new long[split.length];
        for (int i = 0; i < split.length; i++) {
            try {
                this.zzFd[i] = Long.parseLong(split[i]);
            } catch (Throwable e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Unable to parse frame hash target time number.", e);
                this.zzFd[i] = -1;
            }
        }
    }

    private void zzc(zzi com_google_android_gms_ads_internal_overlay_zzi) {
        long longValue = ((Long) zzbt.zzvW.get()).longValue();
        long currentPosition = (long) com_google_android_gms_ads_internal_overlay_zzi.getCurrentPosition();
        int i = 0;
        while (i < this.zzFe.length) {
            if (this.zzFe[i] == null && longValue > Math.abs(currentPosition - this.zzFd[i])) {
                this.zzFe[i] = zza((TextureView) com_google_android_gms_ads_internal_overlay_zzi);
                return;
            }
            i++;
        }
    }

    private void zzfN() {
        if (this.zzFh != null && this.zzFi == null) {
            zzbx.zza(this.zzFb, this.zzFh, "vff");
            zzbx.zza(this.zzFb, this.zzFa, "vtt");
            this.zzFi = zzbx.zzb(this.zzFb);
        }
        long nanoTime = zzr.zzbG().nanoTime();
        if (this.zzFj && this.zzFm && this.zzFn != -1) {
            this.zzFc.zza(((double) TimeUnit.SECONDS.toNanos(1)) / ((double) (nanoTime - this.zzFn)));
        }
        this.zzFm = this.zzFj;
        this.zzFn = nanoTime;
    }

    public void onStop() {
        if (((Boolean) zzbt.zzvU.get()).booleanValue() && !this.zzFl) {
            Bundle bundle = new Bundle();
            bundle.putString("type", "native-player-metrics");
            bundle.putString("request", this.zzEY);
            bundle.putString("player", this.zzFk.zzeZ());
            for (zza com_google_android_gms_internal_zziv_zza : this.zzFc.getBuckets()) {
                bundle.putString("fps_c_" + com_google_android_gms_internal_zziv_zza.name, Integer.toString(com_google_android_gms_internal_zziv_zza.count));
                bundle.putString("fps_p_" + com_google_android_gms_internal_zziv_zza.name, Double.toString(com_google_android_gms_internal_zziv_zza.zzMu));
            }
            for (int i = 0; i < this.zzFd.length; i++) {
                String str = this.zzFe[i];
                if (str != null) {
                    bundle.putString("fh_" + Long.valueOf(this.zzFd[i]), str);
                }
            }
            zzr.zzbC().zza(this.mContext, this.zzEZ.afmaVersion, "gmob-apps", bundle, true);
            this.zzFl = true;
        }
    }

    String zza(TextureView textureView) {
        Bitmap bitmap = textureView.getBitmap(8, 8);
        long j = 0;
        long j2 = 63;
        int i = 0;
        while (i < 8) {
            long j3 = j;
            j = j2;
            for (int i2 = 0; i2 < 8; i2++) {
                int pixel = bitmap.getPixel(i2, i);
                j3 |= (Color.green(pixel) + (Color.blue(pixel) + Color.red(pixel)) > 128 ? 1 : 0) << ((int) j);
                j--;
            }
            i++;
            j2 = j;
            j = j3;
        }
        return String.format("%016X", new Object[]{Long.valueOf(j)});
    }

    public void zza(zzi com_google_android_gms_ads_internal_overlay_zzi) {
        zzbx.zza(this.zzFb, this.zzFa, "vpc");
        this.zzFf = zzbx.zzb(this.zzFb);
        this.zzFk = com_google_android_gms_ads_internal_overlay_zzi;
    }

    public void zzb(zzi com_google_android_gms_ads_internal_overlay_zzi) {
        zzfN();
        zzc(com_google_android_gms_ads_internal_overlay_zzi);
    }

    public void zzfO() {
        this.zzFj = true;
        if (this.zzFg != null && this.zzFh == null) {
            zzbx.zza(this.zzFb, this.zzFg, "vfp");
            this.zzFh = zzbx.zzb(this.zzFb);
        }
    }

    public void zzfP() {
        this.zzFj = false;
    }

    public void zzfz() {
        if (this.zzFf != null && this.zzFg == null) {
            zzbx.zza(this.zzFb, this.zzFf, "vfr");
            this.zzFg = zzbx.zzb(this.zzFb);
        }
    }
}
