package com.google.android.gms.internal;

import android.content.Context;
import android.support.annotation.Nullable;
import com.google.android.gms.internal.zzif.zza;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.Future;

@zzhb
public class zzhz extends zzim implements zzhy {
    private final Context mContext;
    private final zza zzGd;
    private final ArrayList<Future> zzKL = new ArrayList();
    private final ArrayList<String> zzKM = new ArrayList();
    private final HashSet<String> zzKN = new HashSet();
    private final zzht zzKO;
    private final Object zzpV = new Object();
    private final String zzrG;

    public zzhz(Context context, String str, zza com_google_android_gms_internal_zzif_zza, zzht com_google_android_gms_internal_zzht) {
        this.mContext = context;
        this.zzrG = str;
        this.zzGd = com_google_android_gms_internal_zzif_zza;
        this.zzKO = com_google_android_gms_internal_zzht;
    }

    private zzif zza(int i, @Nullable String str, @Nullable zzen com_google_android_gms_internal_zzen) {
        return new zzif(this.zzGd.zzLd.zzHt, null, this.zzGd.zzLe.zzBQ, i, this.zzGd.zzLe.zzBR, this.zzGd.zzLe.zzHV, this.zzGd.zzLe.orientation, this.zzGd.zzLe.zzBU, this.zzGd.zzLd.zzHw, this.zzGd.zzLe.zzHT, com_google_android_gms_internal_zzen, null, str, this.zzGd.zzKV, null, this.zzGd.zzLe.zzHU, this.zzGd.zzrp, this.zzGd.zzLe.zzHS, this.zzGd.zzKY, this.zzGd.zzLe.zzHX, this.zzGd.zzLe.zzHY, this.zzGd.zzKT, null, this.zzGd.zzLe.zzIj, this.zzGd.zzLe.zzIk, this.zzGd.zzLe.zzIl, this.zzGd.zzLe.zzIm);
    }

    private zzif zza(String str, zzen com_google_android_gms_internal_zzen) {
        return zza(-2, str, com_google_android_gms_internal_zzen);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void zzd(java.lang.String r11, java.lang.String r12, java.lang.String r13) {
        /*
        r10 = this;
        r9 = r10.zzpV;
        monitor-enter(r9);
        r0 = r10.zzKO;	 Catch:{ all -> 0x0038 }
        r7 = r0.zzaw(r11);	 Catch:{ all -> 0x0038 }
        if (r7 == 0) goto L_0x0017;
    L_0x000b:
        r0 = r7.zzgQ();	 Catch:{ all -> 0x0038 }
        if (r0 == 0) goto L_0x0017;
    L_0x0011:
        r0 = r7.zzgP();	 Catch:{ all -> 0x0038 }
        if (r0 != 0) goto L_0x0019;
    L_0x0017:
        monitor-exit(r9);	 Catch:{ all -> 0x0038 }
    L_0x0018:
        return;
    L_0x0019:
        r0 = new com.google.android.gms.internal.zzhu;	 Catch:{ all -> 0x0038 }
        r1 = r10.mContext;	 Catch:{ all -> 0x0038 }
        r3 = r10.zzrG;	 Catch:{ all -> 0x0038 }
        r6 = r10.zzGd;	 Catch:{ all -> 0x0038 }
        r2 = r11;
        r4 = r12;
        r5 = r13;
        r8 = r10;
        r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8);	 Catch:{ all -> 0x0038 }
        r1 = r10.zzKL;	 Catch:{ all -> 0x0038 }
        r0 = r0.zzhn();	 Catch:{ all -> 0x0038 }
        r1.add(r0);	 Catch:{ all -> 0x0038 }
        r0 = r10.zzKM;	 Catch:{ all -> 0x0038 }
        r0.add(r11);	 Catch:{ all -> 0x0038 }
        monitor-exit(r9);	 Catch:{ all -> 0x0038 }
        goto L_0x0018;
    L_0x0038:
        r0 = move-exception;
        monitor-exit(r9);	 Catch:{ all -> 0x0038 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzhz.zzd(java.lang.String, java.lang.String, java.lang.String):void");
    }

    private zzif zzgO() {
        return zza(3, null, null);
    }

    public void onStop() {
    }

    public void zza(String str, int i) {
    }

    public void zzax(String str) {
        synchronized (this.zzpV) {
            this.zzKN.add(str);
        }
    }

    public void zzbr() {
        final zzif zza;
        for (zzen com_google_android_gms_internal_zzen : this.zzGd.zzKV.zzBO) {
            String str = com_google_android_gms_internal_zzen.zzBG;
            for (String zzd : com_google_android_gms_internal_zzen.zzBB) {
                zzd(zzd, str, com_google_android_gms_internal_zzen.zzBz);
            }
        }
        int i = 0;
        while (i < this.zzKL.size()) {
            try {
                ((Future) this.zzKL.get(i)).get();
                synchronized (this.zzpV) {
                    if (this.zzKN.contains(this.zzKM.get(i))) {
                        zza = zza((String) this.zzKM.get(i), (zzen) this.zzGd.zzKV.zzBO.get(i));
                        com.google.android.gms.ads.internal.util.client.zza.zzMS.post(new Runnable(this) {
                            final /* synthetic */ zzhz zzKP;

                            public void run() {
                                this.zzKP.zzKO.zzb(zza);
                            }
                        });
                        return;
                    }
                }
            } catch (InterruptedException e) {
            } catch (Exception e2) {
            }
        }
        zza = zzgO();
        com.google.android.gms.ads.internal.util.client.zza.zzMS.post(new Runnable(this) {
            final /* synthetic */ zzhz zzKP;

            public void run() {
                this.zzKP.zzKO.zzb(zza);
            }
        });
        return;
        i++;
    }
}
