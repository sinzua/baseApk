package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.internal.zzfq.zza;
import java.util.Map;

@zzhb
public class zzfr extends zzfs implements zzdf {
    private final Context mContext;
    DisplayMetrics zzDA;
    private float zzDB;
    int zzDC = -1;
    int zzDD = -1;
    private int zzDE;
    int zzDF = -1;
    int zzDG = -1;
    int zzDH = -1;
    int zzDI = -1;
    private final zzbl zzDz;
    private final zzjp zzpD;
    private final WindowManager zzsb;

    public zzfr(zzjp com_google_android_gms_internal_zzjp, Context context, zzbl com_google_android_gms_internal_zzbl) {
        super(com_google_android_gms_internal_zzjp);
        this.zzpD = com_google_android_gms_internal_zzjp;
        this.mContext = context;
        this.zzDz = com_google_android_gms_internal_zzbl;
        this.zzsb = (WindowManager) context.getSystemService("window");
    }

    private void zzeQ() {
        this.zzDA = new DisplayMetrics();
        Display defaultDisplay = this.zzsb.getDefaultDisplay();
        defaultDisplay.getMetrics(this.zzDA);
        this.zzDB = this.zzDA.density;
        this.zzDE = defaultDisplay.getRotation();
    }

    private void zzeV() {
        int[] iArr = new int[2];
        this.zzpD.getLocationOnScreen(iArr);
        zzf(zzn.zzcS().zzc(this.mContext, iArr[0]), zzn.zzcS().zzc(this.mContext, iArr[1]));
    }

    private zzfq zzeY() {
        return new zza().zzr(this.zzDz.zzdj()).zzq(this.zzDz.zzdk()).zzs(this.zzDz.zzdo()).zzt(this.zzDz.zzdl()).zzu(this.zzDz.zzdm()).zzeP();
    }

    public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
        zzeT();
    }

    void zzeR() {
        this.zzDC = zzn.zzcS().zzb(this.zzDA, this.zzDA.widthPixels);
        this.zzDD = zzn.zzcS().zzb(this.zzDA, this.zzDA.heightPixels);
        Activity zzhP = this.zzpD.zzhP();
        if (zzhP == null || zzhP.getWindow() == null) {
            this.zzDF = this.zzDC;
            this.zzDG = this.zzDD;
            return;
        }
        int[] zze = zzr.zzbC().zze(zzhP);
        this.zzDF = zzn.zzcS().zzb(this.zzDA, zze[0]);
        this.zzDG = zzn.zzcS().zzb(this.zzDA, zze[1]);
    }

    void zzeS() {
        if (this.zzpD.zzaN().zzui) {
            this.zzDH = this.zzDC;
            this.zzDI = this.zzDD;
            return;
        }
        this.zzpD.measure(0, 0);
        this.zzDH = zzn.zzcS().zzc(this.mContext, this.zzpD.getMeasuredWidth());
        this.zzDI = zzn.zzcS().zzc(this.mContext, this.zzpD.getMeasuredHeight());
    }

    public void zzeT() {
        zzeQ();
        zzeR();
        zzeS();
        zzeW();
        zzeX();
        zzeV();
        zzeU();
    }

    void zzeU() {
        if (zzb.zzQ(2)) {
            zzb.zzaJ("Dispatching Ready Event.");
        }
        zzan(this.zzpD.zzhX().afmaVersion);
    }

    void zzeW() {
        zza(this.zzDC, this.zzDD, this.zzDF, this.zzDG, this.zzDB, this.zzDE);
    }

    void zzeX() {
        this.zzpD.zzb("onDeviceFeaturesReceived", zzeY().toJson());
    }

    public void zzf(int i, int i2) {
        zzc(i, i2 - (this.mContext instanceof Activity ? zzr.zzbC().zzh((Activity) this.mContext)[0] : 0), this.zzDH, this.zzDI);
        this.zzpD.zzhU().zze(i, i2);
    }
}
