package com.google.android.gms.clearcut;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.playlog.internal.PlayLoggerContext;

public class zzd implements Creator<LogEventParcelable> {
    static void zza(LogEventParcelable logEventParcelable, Parcel parcel, int i) {
        int zzav = zzb.zzav(parcel);
        zzb.zzc(parcel, 1, logEventParcelable.versionCode);
        zzb.zza(parcel, 2, logEventParcelable.zzafh, i, false);
        zzb.zza(parcel, 3, logEventParcelable.zzafi, false);
        zzb.zza(parcel, 4, logEventParcelable.zzafj, false);
        zzb.zzI(parcel, zzav);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzaf(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzbs(i);
    }

    public LogEventParcelable zzaf(Parcel parcel) {
        int[] iArr = null;
        int zzau = zza.zzau(parcel);
        int i = 0;
        byte[] bArr = null;
        PlayLoggerContext playLoggerContext = null;
        while (parcel.dataPosition() < zzau) {
            byte[] bArr2;
            PlayLoggerContext playLoggerContext2;
            int zzg;
            int[] iArr2;
            int zzat = zza.zzat(parcel);
            int[] iArr3;
            switch (zza.zzca(zzat)) {
                case 1:
                    iArr3 = iArr;
                    bArr2 = bArr;
                    playLoggerContext2 = playLoggerContext;
                    zzg = zza.zzg(parcel, zzat);
                    iArr2 = iArr3;
                    break;
                case 2:
                    zzg = i;
                    byte[] bArr3 = bArr;
                    playLoggerContext2 = (PlayLoggerContext) zza.zza(parcel, zzat, PlayLoggerContext.CREATOR);
                    iArr2 = iArr;
                    bArr2 = bArr3;
                    break;
                case 3:
                    playLoggerContext2 = playLoggerContext;
                    zzg = i;
                    iArr3 = iArr;
                    bArr2 = zza.zzs(parcel, zzat);
                    iArr2 = iArr3;
                    break;
                case 4:
                    iArr2 = zza.zzv(parcel, zzat);
                    bArr2 = bArr;
                    playLoggerContext2 = playLoggerContext;
                    zzg = i;
                    break;
                default:
                    zza.zzb(parcel, zzat);
                    iArr2 = iArr;
                    bArr2 = bArr;
                    playLoggerContext2 = playLoggerContext;
                    zzg = i;
                    break;
            }
            i = zzg;
            playLoggerContext = playLoggerContext2;
            bArr = bArr2;
            iArr = iArr2;
        }
        if (parcel.dataPosition() == zzau) {
            return new LogEventParcelable(i, playLoggerContext, bArr, iArr);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public LogEventParcelable[] zzbs(int i) {
        return new LogEventParcelable[i];
    }
}
