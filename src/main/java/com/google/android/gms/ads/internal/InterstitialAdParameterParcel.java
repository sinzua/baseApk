package com.google.android.gms.ads.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzhb;

@zzhb
public final class InterstitialAdParameterParcel implements SafeParcelable {
    public static final zzl CREATOR = new zzl();
    public final int versionCode;
    public final boolean zzql;
    public final boolean zzqm;
    public final String zzqn;
    public final boolean zzqo;
    public final float zzqp;

    InterstitialAdParameterParcel(int versionCode, boolean transparentBackground, boolean hideStatusBar, String backgroundImage, boolean blur, float blurRadius) {
        this.versionCode = versionCode;
        this.zzql = transparentBackground;
        this.zzqm = hideStatusBar;
        this.zzqn = backgroundImage;
        this.zzqo = blur;
        this.zzqp = blurRadius;
    }

    public InterstitialAdParameterParcel(boolean transparentBackground, boolean hideStatusBar, String backgroundImage, boolean blur, float blurRadius) {
        this(2, transparentBackground, hideStatusBar, backgroundImage, blur, blurRadius);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzl.zza(this, out, flags);
    }
}
