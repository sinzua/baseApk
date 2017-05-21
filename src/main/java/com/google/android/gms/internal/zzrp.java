package com.google.android.gms.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.WorkSource;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.zzd;
import com.google.android.gms.common.internal.zzx;

public class zzrp {
    private static boolean DEBUG = false;
    private static String TAG = "WakeLock";
    private static String zzbhl = "*gcore*:";
    private final Context mContext;
    private final String zzanQ;
    private final WakeLock zzbhm;
    private WorkSource zzbhn;
    private final int zzbho;
    private final String zzbhp;
    private boolean zzbhq;
    private int zzbhr;
    private int zzbhs;

    public zzrp(Context context, int i, String str) {
        this(context, i, str, null, context == null ? null : context.getPackageName());
    }

    @SuppressLint({"UnwrappedWakeLock"})
    public zzrp(Context context, int i, String str, String str2, String str3) {
        this.zzbhq = true;
        zzx.zzh(str, "Wake lock name can NOT be empty");
        this.zzbho = i;
        this.zzbhp = str2;
        this.mContext = context.getApplicationContext();
        if (zzni.zzcV(str3) || "com.google.android.gms" == str3) {
            this.zzanQ = str;
        } else {
            this.zzanQ = zzbhl + str;
        }
        this.zzbhm = ((PowerManager) context.getSystemService("power")).newWakeLock(i, str);
        if (zznj.zzaA(this.mContext)) {
            if (zzni.zzcV(str3)) {
                if (zzd.zzakE && zzlz.isInitialized()) {
                    Log.e(TAG, "callingPackage is not supposed to be empty for wakelock " + this.zzanQ + "!", new IllegalArgumentException());
                    str3 = "com.google.android.gms";
                } else {
                    str3 = context.getPackageName();
                }
            }
            this.zzbhn = zznj.zzl(context, str3);
            zzc(this.zzbhn);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void zzfJ(java.lang.String r9) {
        /*
        r8 = this;
        r0 = r8.zzfK(r9);
        r5 = r8.zzn(r9, r0);
        r1 = DEBUG;
        if (r1 == 0) goto L_0x0068;
    L_0x000c:
        r1 = TAG;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Release:\n mWakeLockName: ";
        r2 = r2.append(r3);
        r3 = r8.zzanQ;
        r2 = r2.append(r3);
        r3 = "\n mSecondaryName: ";
        r2 = r2.append(r3);
        r3 = r8.zzbhp;
        r2 = r2.append(r3);
        r3 = "\nmReferenceCounted: ";
        r2 = r2.append(r3);
        r3 = r8.zzbhq;
        r2 = r2.append(r3);
        r3 = "\nreason: ";
        r2 = r2.append(r3);
        r2 = r2.append(r9);
        r3 = "\n mOpenEventCount";
        r2 = r2.append(r3);
        r3 = r8.zzbhs;
        r2 = r2.append(r3);
        r3 = "\nuseWithReason: ";
        r2 = r2.append(r3);
        r2 = r2.append(r0);
        r3 = "\ntrackingName: ";
        r2 = r2.append(r3);
        r2 = r2.append(r5);
        r2 = r2.toString();
        android.util.Log.d(r1, r2);
    L_0x0068:
        monitor-enter(r8);
        r1 = r8.zzbhq;	 Catch:{ all -> 0x00a3 }
        if (r1 == 0) goto L_0x0077;
    L_0x006d:
        r1 = r8.zzbhr;	 Catch:{ all -> 0x00a3 }
        r1 = r1 + -1;
        r8.zzbhr = r1;	 Catch:{ all -> 0x00a3 }
        if (r1 == 0) goto L_0x0080;
    L_0x0075:
        if (r0 != 0) goto L_0x0080;
    L_0x0077:
        r0 = r8.zzbhq;	 Catch:{ all -> 0x00a3 }
        if (r0 != 0) goto L_0x00a1;
    L_0x007b:
        r0 = r8.zzbhs;	 Catch:{ all -> 0x00a3 }
        r1 = 1;
        if (r0 != r1) goto L_0x00a1;
    L_0x0080:
        r0 = com.google.android.gms.common.stats.zzi.zzrZ();	 Catch:{ all -> 0x00a3 }
        r1 = r8.mContext;	 Catch:{ all -> 0x00a3 }
        r2 = r8.zzbhm;	 Catch:{ all -> 0x00a3 }
        r2 = com.google.android.gms.common.stats.zzg.zza(r2, r5);	 Catch:{ all -> 0x00a3 }
        r3 = 8;
        r4 = r8.zzanQ;	 Catch:{ all -> 0x00a3 }
        r6 = r8.zzbho;	 Catch:{ all -> 0x00a3 }
        r7 = r8.zzbhn;	 Catch:{ all -> 0x00a3 }
        r7 = com.google.android.gms.internal.zznj.zzb(r7);	 Catch:{ all -> 0x00a3 }
        r0.zza(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ all -> 0x00a3 }
        r0 = r8.zzbhs;	 Catch:{ all -> 0x00a3 }
        r0 = r0 + -1;
        r8.zzbhs = r0;	 Catch:{ all -> 0x00a3 }
    L_0x00a1:
        monitor-exit(r8);	 Catch:{ all -> 0x00a3 }
        return;
    L_0x00a3:
        r0 = move-exception;
        monitor-exit(r8);	 Catch:{ all -> 0x00a3 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzrp.zzfJ(java.lang.String):void");
    }

    private boolean zzfK(String str) {
        return (TextUtils.isEmpty(str) || str.equals(this.zzbhp)) ? false : true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void zzj(java.lang.String r11, long r12) {
        /*
        r10 = this;
        r0 = r10.zzfK(r11);
        r5 = r10.zzn(r11, r0);
        r1 = DEBUG;
        if (r1 == 0) goto L_0x0072;
    L_0x000c:
        r1 = TAG;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Acquire:\n mWakeLockName: ";
        r2 = r2.append(r3);
        r3 = r10.zzanQ;
        r2 = r2.append(r3);
        r3 = "\n mSecondaryName: ";
        r2 = r2.append(r3);
        r3 = r10.zzbhp;
        r2 = r2.append(r3);
        r3 = "\nmReferenceCounted: ";
        r2 = r2.append(r3);
        r3 = r10.zzbhq;
        r2 = r2.append(r3);
        r3 = "\nreason: ";
        r2 = r2.append(r3);
        r2 = r2.append(r11);
        r3 = "\nmOpenEventCount";
        r2 = r2.append(r3);
        r3 = r10.zzbhs;
        r2 = r2.append(r3);
        r3 = "\nuseWithReason: ";
        r2 = r2.append(r3);
        r2 = r2.append(r0);
        r3 = "\ntrackingName: ";
        r2 = r2.append(r3);
        r2 = r2.append(r5);
        r3 = "\ntimeout: ";
        r2 = r2.append(r3);
        r2 = r2.append(r12);
        r2 = r2.toString();
        android.util.Log.d(r1, r2);
    L_0x0072:
        monitor-enter(r10);
        r1 = r10.zzbhq;	 Catch:{ all -> 0x00ac }
        if (r1 == 0) goto L_0x0081;
    L_0x0077:
        r1 = r10.zzbhr;	 Catch:{ all -> 0x00ac }
        r2 = r1 + 1;
        r10.zzbhr = r2;	 Catch:{ all -> 0x00ac }
        if (r1 == 0) goto L_0x0089;
    L_0x007f:
        if (r0 != 0) goto L_0x0089;
    L_0x0081:
        r0 = r10.zzbhq;	 Catch:{ all -> 0x00ac }
        if (r0 != 0) goto L_0x00aa;
    L_0x0085:
        r0 = r10.zzbhs;	 Catch:{ all -> 0x00ac }
        if (r0 != 0) goto L_0x00aa;
    L_0x0089:
        r0 = com.google.android.gms.common.stats.zzi.zzrZ();	 Catch:{ all -> 0x00ac }
        r1 = r10.mContext;	 Catch:{ all -> 0x00ac }
        r2 = r10.zzbhm;	 Catch:{ all -> 0x00ac }
        r2 = com.google.android.gms.common.stats.zzg.zza(r2, r5);	 Catch:{ all -> 0x00ac }
        r3 = 7;
        r4 = r10.zzanQ;	 Catch:{ all -> 0x00ac }
        r6 = r10.zzbho;	 Catch:{ all -> 0x00ac }
        r7 = r10.zzbhn;	 Catch:{ all -> 0x00ac }
        r7 = com.google.android.gms.internal.zznj.zzb(r7);	 Catch:{ all -> 0x00ac }
        r8 = r12;
        r0.zza(r1, r2, r3, r4, r5, r6, r7, r8);	 Catch:{ all -> 0x00ac }
        r0 = r10.zzbhs;	 Catch:{ all -> 0x00ac }
        r0 = r0 + 1;
        r10.zzbhs = r0;	 Catch:{ all -> 0x00ac }
    L_0x00aa:
        monitor-exit(r10);	 Catch:{ all -> 0x00ac }
        return;
    L_0x00ac:
        r0 = move-exception;
        monitor-exit(r10);	 Catch:{ all -> 0x00ac }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzrp.zzj(java.lang.String, long):void");
    }

    private String zzn(String str, boolean z) {
        return this.zzbhq ? z ? str : this.zzbhp : this.zzbhp;
    }

    public void acquire(long timeout) {
        if (!zzne.zzsg() && this.zzbhq) {
            Log.wtf(TAG, "Do not acquire with timeout on reference counted WakeLocks before ICS. wakelock: " + this.zzanQ);
        }
        zzj(null, timeout);
        this.zzbhm.acquire(timeout);
    }

    public boolean isHeld() {
        return this.zzbhm.isHeld();
    }

    public void release() {
        zzfJ(null);
        this.zzbhm.release();
    }

    public void setReferenceCounted(boolean value) {
        this.zzbhm.setReferenceCounted(value);
        this.zzbhq = value;
    }

    public void zzc(WorkSource workSource) {
        if (zznj.zzaA(this.mContext) && workSource != null) {
            if (this.zzbhn != null) {
                this.zzbhn.add(workSource);
            } else {
                this.zzbhn = workSource;
            }
            this.zzbhm.setWorkSource(this.zzbhn);
        }
    }
}
