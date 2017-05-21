package com.google.android.gms.ads.internal.client;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzam implements Creator<SearchAdRequestParcel> {
    static void zza(SearchAdRequestParcel searchAdRequestParcel, Parcel parcel, int i) {
        int zzav = zzb.zzav(parcel);
        zzb.zzc(parcel, 1, searchAdRequestParcel.versionCode);
        zzb.zzc(parcel, 2, searchAdRequestParcel.zzvd);
        zzb.zzc(parcel, 3, searchAdRequestParcel.backgroundColor);
        zzb.zzc(parcel, 4, searchAdRequestParcel.zzve);
        zzb.zzc(parcel, 5, searchAdRequestParcel.zzvf);
        zzb.zzc(parcel, 6, searchAdRequestParcel.zzvg);
        zzb.zzc(parcel, 7, searchAdRequestParcel.zzvh);
        zzb.zzc(parcel, 8, searchAdRequestParcel.zzvi);
        zzb.zzc(parcel, 9, searchAdRequestParcel.zzvj);
        zzb.zza(parcel, 10, searchAdRequestParcel.zzvk, false);
        zzb.zzc(parcel, 11, searchAdRequestParcel.zzvl);
        zzb.zza(parcel, 12, searchAdRequestParcel.zzvm, false);
        zzb.zzc(parcel, 13, searchAdRequestParcel.zzvn);
        zzb.zzc(parcel, 14, searchAdRequestParcel.zzvo);
        zzb.zza(parcel, 15, searchAdRequestParcel.zzvp, false);
        zzb.zzI(parcel, zzav);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzd(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzo(i);
    }

    public SearchAdRequestParcel zzd(Parcel parcel) {
        int zzau = zza.zzau(parcel);
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        String str = null;
        int i10 = 0;
        String str2 = null;
        int i11 = 0;
        int i12 = 0;
        String str3 = null;
        while (parcel.dataPosition() < zzau) {
            int zzat = zza.zzat(parcel);
            switch (zza.zzca(zzat)) {
                case 1:
                    i = zza.zzg(parcel, zzat);
                    break;
                case 2:
                    i2 = zza.zzg(parcel, zzat);
                    break;
                case 3:
                    i3 = zza.zzg(parcel, zzat);
                    break;
                case 4:
                    i4 = zza.zzg(parcel, zzat);
                    break;
                case 5:
                    i5 = zza.zzg(parcel, zzat);
                    break;
                case 6:
                    i6 = zza.zzg(parcel, zzat);
                    break;
                case 7:
                    i7 = zza.zzg(parcel, zzat);
                    break;
                case 8:
                    i8 = zza.zzg(parcel, zzat);
                    break;
                case 9:
                    i9 = zza.zzg(parcel, zzat);
                    break;
                case 10:
                    str = zza.zzp(parcel, zzat);
                    break;
                case 11:
                    i10 = zza.zzg(parcel, zzat);
                    break;
                case 12:
                    str2 = zza.zzp(parcel, zzat);
                    break;
                case 13:
                    i11 = zza.zzg(parcel, zzat);
                    break;
                case 14:
                    i12 = zza.zzg(parcel, zzat);
                    break;
                case 15:
                    str3 = zza.zzp(parcel, zzat);
                    break;
                default:
                    zza.zzb(parcel, zzat);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new SearchAdRequestParcel(i, i2, i3, i4, i5, i6, i7, i8, i9, str, i10, str2, i11, i12, str3);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public SearchAdRequestParcel[] zzo(int i) {
        return new SearchAdRequestParcel[i];
    }
}
