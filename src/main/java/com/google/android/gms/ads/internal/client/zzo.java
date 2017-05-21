package com.google.android.gms.ads.internal.client;

import com.google.android.gms.ads.internal.client.zzx.zza;
import com.google.android.gms.internal.zzhb;
import java.util.Random;

@zzhb
public class zzo extends zza {
    private Object zzpV = new Object();
    private final Random zzuy = new Random();
    private long zzuz;

    public zzo() {
        zzcY();
    }

    public long getValue() {
        return this.zzuz;
    }

    public void zzcY() {
        synchronized (this.zzpV) {
            int i = 3;
            long j = 0;
            while (true) {
                i--;
                if (i <= 0) {
                    break;
                }
                j = ((long) this.zzuy.nextInt()) + 2147483648L;
                if (j != this.zzuz && j != 0) {
                    break;
                }
            }
            this.zzuz = j;
        }
    }
}
