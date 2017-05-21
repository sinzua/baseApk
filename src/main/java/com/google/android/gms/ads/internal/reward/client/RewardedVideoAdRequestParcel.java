package com.google.android.gms.ads.internal.reward.client;

import android.os.Parcel;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzhb;

@zzhb
public final class RewardedVideoAdRequestParcel implements SafeParcelable {
    public static final zzh CREATOR = new zzh();
    public final int versionCode;
    public final AdRequestParcel zzHt;
    public final String zzrj;

    public RewardedVideoAdRequestParcel(int versionCode, AdRequestParcel adRequest, String adUnitId) {
        this.versionCode = versionCode;
        this.zzHt = adRequest;
        this.zzrj = adUnitId;
    }

    public RewardedVideoAdRequestParcel(AdRequestParcel adRequest, String adUnitId) {
        this(1, adRequest, adUnitId);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzh.zza(this, out, flags);
    }
}
