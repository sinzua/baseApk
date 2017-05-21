package com.google.android.gms.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import com.google.android.gms.ads.AdActivity;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.util.client.zzb;

@zzhb
public class zzii {
    long zzLE = -1;
    long zzLF = -1;
    int zzLG = -1;
    int zzLH = 0;
    int zzLI = 0;
    final String zzLq;
    private final Object zzpV = new Object();

    public zzii(String str) {
        this.zzLq = str;
    }

    public static boolean zzH(Context context) {
        int identifier = context.getResources().getIdentifier("Theme.Translucent", "style", "android");
        if (identifier == 0) {
            zzb.zzaJ("Please set theme of AdActivity to @android:style/Theme.Translucent to enable transparent background interstitial ad.");
            return false;
        }
        try {
            if (identifier == context.getPackageManager().getActivityInfo(new ComponentName(context.getPackageName(), AdActivity.CLASS_NAME), 0).theme) {
                return true;
            }
            zzb.zzaJ("Please set theme of AdActivity to @android:style/Theme.Translucent to enable transparent background interstitial ad.");
            return false;
        } catch (NameNotFoundException e) {
            zzb.zzaK("Fail to fetch AdActivity theme");
            zzb.zzaJ("Please set theme of AdActivity to @android:style/Theme.Translucent to enable transparent background interstitial ad.");
            return false;
        }
    }

    public void zzb(AdRequestParcel adRequestParcel, long j) {
        synchronized (this.zzpV) {
            if (this.zzLF == -1) {
                this.zzLF = j;
                this.zzLE = this.zzLF;
            } else {
                this.zzLE = j;
            }
            if (adRequestParcel.extras == null || adRequestParcel.extras.getInt("gw", 2) != 1) {
                this.zzLG++;
                return;
            }
        }
    }

    public Bundle zzc(Context context, String str) {
        Bundle bundle;
        synchronized (this.zzpV) {
            bundle = new Bundle();
            bundle.putString("session_id", this.zzLq);
            bundle.putLong("basets", this.zzLF);
            bundle.putLong("currts", this.zzLE);
            bundle.putString("seq_num", str);
            bundle.putInt("preqs", this.zzLG);
            bundle.putInt("pclick", this.zzLH);
            bundle.putInt("pimp", this.zzLI);
            bundle.putBoolean("support_transparent_background", zzH(context));
        }
        return bundle;
    }

    public void zzgS() {
        synchronized (this.zzpV) {
            this.zzLI++;
        }
    }

    public void zzgT() {
        synchronized (this.zzpV) {
            this.zzLH++;
        }
    }

    public long zzhl() {
        return this.zzLF;
    }
}
