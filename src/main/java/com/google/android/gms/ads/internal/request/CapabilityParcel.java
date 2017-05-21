package com.google.android.gms.ads.internal.request;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzhb;

@zzhb
public class CapabilityParcel implements SafeParcelable {
    public static final Creator<CapabilityParcel> CREATOR = new zzi();
    public final int versionCode;
    public final boolean zzIn;
    public final boolean zzIo;
    public final boolean zzIp;

    CapabilityParcel(int versionCode, boolean inAppPurchaseSupported, boolean defaultInAppPurchaseSupported, boolean appStreamingSupported) {
        this.versionCode = versionCode;
        this.zzIn = inAppPurchaseSupported;
        this.zzIo = defaultInAppPurchaseSupported;
        this.zzIp = appStreamingSupported;
    }

    public CapabilityParcel(boolean inAppPurchaseSupported, boolean defaultInAppPurchaseSupported, boolean appStreamingSupported) {
        this(2, inAppPurchaseSupported, defaultInAppPurchaseSupported, appStreamingSupported);
    }

    public int describeContents() {
        return 0;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("iap_supported", this.zzIn);
        bundle.putBoolean("default_iap_supported", this.zzIo);
        bundle.putBoolean("app_streaming_supported", this.zzIp);
        return bundle;
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzi.zza(this, dest, flags);
    }
}
