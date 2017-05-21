package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@zzhb
public class zzbd {
    private final Object zzpV = new Object();
    private int zzsW;
    private List<zzbc> zzsX = new LinkedList();

    public boolean zza(zzbc com_google_android_gms_internal_zzbc) {
        boolean z;
        synchronized (this.zzpV) {
            if (this.zzsX.contains(com_google_android_gms_internal_zzbc)) {
                z = true;
            } else {
                z = false;
            }
        }
        return z;
    }

    public boolean zzb(zzbc com_google_android_gms_internal_zzbc) {
        boolean z;
        synchronized (this.zzpV) {
            Iterator it = this.zzsX.iterator();
            while (it.hasNext()) {
                zzbc com_google_android_gms_internal_zzbc2 = (zzbc) it.next();
                if (com_google_android_gms_internal_zzbc != com_google_android_gms_internal_zzbc2 && com_google_android_gms_internal_zzbc2.zzcy().equals(com_google_android_gms_internal_zzbc.zzcy())) {
                    it.remove();
                    z = true;
                    break;
                }
            }
            z = false;
        }
        return z;
    }

    public void zzc(zzbc com_google_android_gms_internal_zzbc) {
        synchronized (this.zzpV) {
            if (this.zzsX.size() >= 10) {
                zzb.zzaI("Queue is full, current size = " + this.zzsX.size());
                this.zzsX.remove(0);
            }
            int i = this.zzsW;
            this.zzsW = i + 1;
            com_google_android_gms_internal_zzbc.zzh(i);
            this.zzsX.add(com_google_android_gms_internal_zzbc);
        }
    }

    public zzbc zzcF() {
        zzbc com_google_android_gms_internal_zzbc = null;
        synchronized (this.zzpV) {
            if (this.zzsX.size() == 0) {
                zzb.zzaI("Queue empty");
                return null;
            } else if (this.zzsX.size() >= 2) {
                int i = Integer.MIN_VALUE;
                for (zzbc com_google_android_gms_internal_zzbc2 : this.zzsX) {
                    zzbc com_google_android_gms_internal_zzbc3;
                    int i2;
                    int score = com_google_android_gms_internal_zzbc2.getScore();
                    if (score > i) {
                        int i3 = score;
                        com_google_android_gms_internal_zzbc3 = com_google_android_gms_internal_zzbc2;
                        i2 = i3;
                    } else {
                        i2 = i;
                        com_google_android_gms_internal_zzbc3 = com_google_android_gms_internal_zzbc;
                    }
                    i = i2;
                    com_google_android_gms_internal_zzbc = com_google_android_gms_internal_zzbc3;
                }
                this.zzsX.remove(com_google_android_gms_internal_zzbc);
                return com_google_android_gms_internal_zzbc;
            } else {
                com_google_android_gms_internal_zzbc2 = (zzbc) this.zzsX.get(0);
                com_google_android_gms_internal_zzbc2.zzcA();
                return com_google_android_gms_internal_zzbc2;
            }
        }
    }
}
