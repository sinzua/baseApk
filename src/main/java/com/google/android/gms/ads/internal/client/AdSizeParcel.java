package com.google.android.gms.ads.internal.client;

import android.content.Context;
import android.os.Parcel;
import android.util.DisplayMetrics;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.zza;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzhb;

@zzhb
public class AdSizeParcel implements SafeParcelable {
    public static final zzi CREATOR = new zzi();
    public final int height;
    public final int heightPixels;
    public final int versionCode;
    public final int width;
    public final int widthPixels;
    public final String zzuh;
    public final boolean zzui;
    public final AdSizeParcel[] zzuj;
    public final boolean zzuk;
    public final boolean zzul;
    public boolean zzum;

    public AdSizeParcel() {
        this(5, "interstitial_mb", 0, 0, true, 0, 0, null, false, false, false);
    }

    AdSizeParcel(int versionCode, String formatString, int height, int heightPixels, boolean isInterstitial, int width, int widthPixels, AdSizeParcel[] supportedAdSizes, boolean isNative, boolean isFluid, boolean isNativeExpress) {
        this.versionCode = versionCode;
        this.zzuh = formatString;
        this.height = height;
        this.heightPixels = heightPixels;
        this.zzui = isInterstitial;
        this.width = width;
        this.widthPixels = widthPixels;
        this.zzuj = supportedAdSizes;
        this.zzuk = isNative;
        this.zzul = isFluid;
        this.zzum = isNativeExpress;
    }

    public AdSizeParcel(Context context, AdSize adSize) {
        this(context, new AdSize[]{adSize});
    }

    public AdSizeParcel(Context context, AdSize[] adSizes) {
        int i;
        int i2;
        AdSize adSize = adSizes[0];
        this.versionCode = 5;
        this.zzui = false;
        this.zzul = adSize.isFluid();
        if (this.zzul) {
            this.width = AdSize.BANNER.getWidth();
            this.height = AdSize.BANNER.getHeight();
        } else {
            this.width = adSize.getWidth();
            this.height = adSize.getHeight();
        }
        boolean z = this.width == -1;
        boolean z2 = this.height == -2;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        if (z) {
            if (zzn.zzcS().zzV(context) && zzn.zzcS().zzW(context)) {
                this.widthPixels = zza(displayMetrics) - zzn.zzcS().zzX(context);
            } else {
                this.widthPixels = zza(displayMetrics);
            }
            double d = (double) (((float) this.widthPixels) / displayMetrics.density);
            i = (int) d;
            if (d - ((double) ((int) d)) >= 0.01d) {
                i++;
            }
            i2 = i;
        } else {
            i = this.width;
            this.widthPixels = zzn.zzcS().zza(displayMetrics, this.width);
            i2 = i;
        }
        i = z2 ? zzc(displayMetrics) : this.height;
        this.heightPixels = zzn.zzcS().zza(displayMetrics, i);
        if (z || z2) {
            this.zzuh = i2 + "x" + i + "_as";
        } else if (this.zzul) {
            this.zzuh = "320x50_mb";
        } else {
            this.zzuh = adSize.toString();
        }
        if (adSizes.length > 1) {
            this.zzuj = new AdSizeParcel[adSizes.length];
            for (int i3 = 0; i3 < adSizes.length; i3++) {
                this.zzuj[i3] = new AdSizeParcel(context, adSizes[i3]);
            }
        } else {
            this.zzuj = null;
        }
        this.zzuk = false;
        this.zzum = false;
    }

    public AdSizeParcel(AdSizeParcel adSize, AdSizeParcel[] supportedAdSizes) {
        this(5, adSize.zzuh, adSize.height, adSize.heightPixels, adSize.zzui, adSize.width, adSize.widthPixels, supportedAdSizes, adSize.zzuk, adSize.zzul, adSize.zzum);
    }

    public static int zza(DisplayMetrics displayMetrics) {
        return displayMetrics.widthPixels;
    }

    public static int zzb(DisplayMetrics displayMetrics) {
        return (int) (((float) zzc(displayMetrics)) * displayMetrics.density);
    }

    private static int zzc(DisplayMetrics displayMetrics) {
        int i = (int) (((float) displayMetrics.heightPixels) / displayMetrics.density);
        return i <= 400 ? 32 : i <= 720 ? 50 : 90;
    }

    public static AdSizeParcel zzcP() {
        return new AdSizeParcel(5, "reward_mb", 0, 0, true, 0, 0, null, false, false, false);
    }

    public static AdSizeParcel zzt(Context context) {
        return new AdSizeParcel(5, "320x50_mb", 0, 0, false, 0, 0, null, true, false, false);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzi.zza(this, out, flags);
    }

    public AdSize zzcQ() {
        return zza.zza(this.width, this.height, this.zzuh);
    }

    public void zzi(boolean z) {
        this.zzum = z;
    }
}
