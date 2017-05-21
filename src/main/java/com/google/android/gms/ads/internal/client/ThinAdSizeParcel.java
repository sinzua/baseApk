package com.google.android.gms.ads.internal.client;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.internal.zzhb;

@zzhb
public class ThinAdSizeParcel extends AdSizeParcel {
    public ThinAdSizeParcel(AdSizeParcel originalAdSize) {
        super(originalAdSize.versionCode, originalAdSize.zzuh, originalAdSize.height, originalAdSize.heightPixels, originalAdSize.zzui, originalAdSize.width, originalAdSize.widthPixels, originalAdSize.zzuj, originalAdSize.zzuk, originalAdSize.zzul, originalAdSize.zzum);
    }

    public void writeToParcel(Parcel parcel, int flags) {
        int zzav = zzb.zzav(parcel);
        zzb.zzc(parcel, 1, this.versionCode);
        zzb.zza(parcel, 2, this.zzuh, false);
        zzb.zzc(parcel, 3, this.height);
        zzb.zzc(parcel, 6, this.width);
        zzb.zzI(parcel, zzav);
    }
}
