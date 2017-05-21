package com.google.android.gms.internal;

import com.google.android.gms.internal.zzez.zza;

@zzhb
public final class zzeq extends zza {
    private zzes.zza zzCb;
    private zzep zzCc;
    private final Object zzpV = new Object();

    public void onAdClicked() {
        synchronized (this.zzpV) {
            if (this.zzCc != null) {
                this.zzCc.zzaY();
            }
        }
    }

    public void onAdClosed() {
        synchronized (this.zzpV) {
            if (this.zzCc != null) {
                this.zzCc.zzaZ();
            }
        }
    }

    public void onAdFailedToLoad(int error) {
        synchronized (this.zzpV) {
            if (this.zzCb != null) {
                this.zzCb.zzr(error == 3 ? 1 : 2);
                this.zzCb = null;
            }
        }
    }

    public void onAdLeftApplication() {
        synchronized (this.zzpV) {
            if (this.zzCc != null) {
                this.zzCc.zzba();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onAdLoaded() {
        /*
        r3 = this;
        r1 = r3.zzpV;
        monitor-enter(r1);
        r0 = r3.zzCb;	 Catch:{ all -> 0x001d }
        if (r0 == 0) goto L_0x0012;
    L_0x0007:
        r0 = r3.zzCb;	 Catch:{ all -> 0x001d }
        r2 = 0;
        r0.zzr(r2);	 Catch:{ all -> 0x001d }
        r0 = 0;
        r3.zzCb = r0;	 Catch:{ all -> 0x001d }
        monitor-exit(r1);	 Catch:{ all -> 0x001d }
    L_0x0011:
        return;
    L_0x0012:
        r0 = r3.zzCc;	 Catch:{ all -> 0x001d }
        if (r0 == 0) goto L_0x001b;
    L_0x0016:
        r0 = r3.zzCc;	 Catch:{ all -> 0x001d }
        r0.zzbc();	 Catch:{ all -> 0x001d }
    L_0x001b:
        monitor-exit(r1);	 Catch:{ all -> 0x001d }
        goto L_0x0011;
    L_0x001d:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x001d }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzeq.onAdLoaded():void");
    }

    public void onAdOpened() {
        synchronized (this.zzpV) {
            if (this.zzCc != null) {
                this.zzCc.zzbb();
            }
        }
    }

    public void zza(zzep com_google_android_gms_internal_zzep) {
        synchronized (this.zzpV) {
            this.zzCc = com_google_android_gms_internal_zzep;
        }
    }

    public void zza(zzes.zza com_google_android_gms_internal_zzes_zza) {
        synchronized (this.zzpV) {
            this.zzCb = com_google_android_gms_internal_zzes_zza;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void zza(com.google.android.gms.internal.zzfa r4) {
        /*
        r3 = this;
        r1 = r3.zzpV;
        monitor-enter(r1);
        r0 = r3.zzCb;	 Catch:{ all -> 0x001d }
        if (r0 == 0) goto L_0x0012;
    L_0x0007:
        r0 = r3.zzCb;	 Catch:{ all -> 0x001d }
        r2 = 0;
        r0.zza(r2, r4);	 Catch:{ all -> 0x001d }
        r0 = 0;
        r3.zzCb = r0;	 Catch:{ all -> 0x001d }
        monitor-exit(r1);	 Catch:{ all -> 0x001d }
    L_0x0011:
        return;
    L_0x0012:
        r0 = r3.zzCc;	 Catch:{ all -> 0x001d }
        if (r0 == 0) goto L_0x001b;
    L_0x0016:
        r0 = r3.zzCc;	 Catch:{ all -> 0x001d }
        r0.zzbc();	 Catch:{ all -> 0x001d }
    L_0x001b:
        monitor-exit(r1);	 Catch:{ all -> 0x001d }
        goto L_0x0011;
    L_0x001d:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x001d }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzeq.zza(com.google.android.gms.internal.zzfa):void");
    }
}
