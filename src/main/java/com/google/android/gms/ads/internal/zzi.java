package com.google.android.gms.ads.internal;

import android.content.Context;
import android.support.v4.util.SimpleArrayMap;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.client.zzq;
import com.google.android.gms.ads.internal.client.zzr.zza;
import com.google.android.gms.ads.internal.client.zzx;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.internal.zzcr;
import com.google.android.gms.internal.zzcs;
import com.google.android.gms.internal.zzct;
import com.google.android.gms.internal.zzcu;
import com.google.android.gms.internal.zzex;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzir;
import com.nativex.common.JsonRequestConstants.UDIDs;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

@zzhb
public class zzi extends zza {
    private final Context mContext;
    private final zzq zzpK;
    private final zzcr zzpL;
    private final zzcs zzpM;
    private final SimpleArrayMap<String, zzcu> zzpN;
    private final SimpleArrayMap<String, zzct> zzpO;
    private final NativeAdOptionsParcel zzpP;
    private final List<String> zzpQ;
    private final zzx zzpR;
    private final String zzpS;
    private final VersionInfoParcel zzpT;
    private WeakReference<zzp> zzpU;
    private final Object zzpV = new Object();
    private final zzd zzpm;
    private final zzex zzpn;

    zzi(Context context, String str, zzex com_google_android_gms_internal_zzex, VersionInfoParcel versionInfoParcel, zzq com_google_android_gms_ads_internal_client_zzq, zzcr com_google_android_gms_internal_zzcr, zzcs com_google_android_gms_internal_zzcs, SimpleArrayMap<String, zzcu> simpleArrayMap, SimpleArrayMap<String, zzct> simpleArrayMap2, NativeAdOptionsParcel nativeAdOptionsParcel, zzx com_google_android_gms_ads_internal_client_zzx, zzd com_google_android_gms_ads_internal_zzd) {
        this.mContext = context;
        this.zzpS = str;
        this.zzpn = com_google_android_gms_internal_zzex;
        this.zzpT = versionInfoParcel;
        this.zzpK = com_google_android_gms_ads_internal_client_zzq;
        this.zzpM = com_google_android_gms_internal_zzcs;
        this.zzpL = com_google_android_gms_internal_zzcr;
        this.zzpN = simpleArrayMap;
        this.zzpO = simpleArrayMap2;
        this.zzpP = nativeAdOptionsParcel;
        this.zzpQ = zzbl();
        this.zzpR = com_google_android_gms_ads_internal_client_zzx;
        this.zzpm = com_google_android_gms_ads_internal_zzd;
    }

    private List<String> zzbl() {
        List<String> arrayList = new ArrayList();
        if (this.zzpM != null) {
            arrayList.add("1");
        }
        if (this.zzpL != null) {
            arrayList.add("2");
        }
        if (this.zzpN.size() > 0) {
            arrayList.add(UDIDs.ANDROID_DEVICE_ID);
        }
        return arrayList;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getMediationAdapterClassName() {
        /*
        r3 = this;
        r1 = 0;
        r2 = r3.zzpV;
        monitor-enter(r2);
        r0 = r3.zzpU;	 Catch:{ all -> 0x001d }
        if (r0 == 0) goto L_0x001a;
    L_0x0008:
        r0 = r3.zzpU;	 Catch:{ all -> 0x001d }
        r0 = r0.get();	 Catch:{ all -> 0x001d }
        r0 = (com.google.android.gms.ads.internal.zzp) r0;	 Catch:{ all -> 0x001d }
        if (r0 == 0) goto L_0x0018;
    L_0x0012:
        r0 = r0.getMediationAdapterClassName();	 Catch:{ all -> 0x001d }
    L_0x0016:
        monitor-exit(r2);	 Catch:{ all -> 0x001d }
    L_0x0017:
        return r0;
    L_0x0018:
        r0 = r1;
        goto L_0x0016;
    L_0x001a:
        monitor-exit(r2);	 Catch:{ all -> 0x001d }
        r0 = r1;
        goto L_0x0017;
    L_0x001d:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x001d }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.ads.internal.zzi.getMediationAdapterClassName():java.lang.String");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isLoading() {
        /*
        r3 = this;
        r1 = 0;
        r2 = r3.zzpV;
        monitor-enter(r2);
        r0 = r3.zzpU;	 Catch:{ all -> 0x001d }
        if (r0 == 0) goto L_0x001a;
    L_0x0008:
        r0 = r3.zzpU;	 Catch:{ all -> 0x001d }
        r0 = r0.get();	 Catch:{ all -> 0x001d }
        r0 = (com.google.android.gms.ads.internal.zzp) r0;	 Catch:{ all -> 0x001d }
        if (r0 == 0) goto L_0x0018;
    L_0x0012:
        r0 = r0.isLoading();	 Catch:{ all -> 0x001d }
    L_0x0016:
        monitor-exit(r2);	 Catch:{ all -> 0x001d }
    L_0x0017:
        return r0;
    L_0x0018:
        r0 = r1;
        goto L_0x0016;
    L_0x001a:
        monitor-exit(r2);	 Catch:{ all -> 0x001d }
        r0 = r1;
        goto L_0x0017;
    L_0x001d:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x001d }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.ads.internal.zzi.isLoading():boolean");
    }

    protected void runOnUiThread(Runnable runnable) {
        zzir.zzMc.post(runnable);
    }

    protected zzp zzbm() {
        return new zzp(this.mContext, this.zzpm, AdSizeParcel.zzt(this.mContext), this.zzpS, this.zzpn, this.zzpT);
    }

    public void zzf(final AdRequestParcel adRequestParcel) {
        runOnUiThread(new Runnable(this) {
            final /* synthetic */ zzi zzpX;

            public void run() {
                synchronized (this.zzpX.zzpV) {
                    zzp zzbm = this.zzpX.zzbm();
                    this.zzpX.zzpU = new WeakReference(zzbm);
                    zzbm.zzb(this.zzpX.zzpL);
                    zzbm.zzb(this.zzpX.zzpM);
                    zzbm.zza(this.zzpX.zzpN);
                    zzbm.zza(this.zzpX.zzpK);
                    zzbm.zzb(this.zzpX.zzpO);
                    zzbm.zza(this.zzpX.zzbl());
                    zzbm.zzb(this.zzpX.zzpP);
                    zzbm.zza(this.zzpX.zzpR);
                    zzbm.zzb(adRequestParcel);
                }
            }
        });
    }
}
