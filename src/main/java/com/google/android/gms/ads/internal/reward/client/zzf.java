package com.google.android.gms.ads.internal.reward.client;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.reward.client.zzb.zza;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.dynamic.zzg;
import com.google.android.gms.internal.zzew;
import com.google.android.gms.internal.zzhb;

@zzhb
public class zzf extends zzg<zzc> {
    public zzf() {
        super("com.google.android.gms.ads.reward.RewardedVideoAdCreatorImpl");
    }

    private zzb zzb(Context context, zzew com_google_android_gms_internal_zzew) {
        Throwable e;
        try {
            return zza.zzaa(((zzc) zzaB(context)).zza(zze.zzC(context), com_google_android_gms_internal_zzew, 8487000));
        } catch (RemoteException e2) {
            e = e2;
            zzb.zzd("Could not get remote RewardedVideoAd.", e);
            return null;
        } catch (zzg.zza e3) {
            e = e3;
            zzb.zzd("Could not get remote RewardedVideoAd.", e);
            return null;
        }
    }

    public zzb zza(Context context, zzew com_google_android_gms_internal_zzew) {
        if (zzn.zzcS().zzU(context)) {
            zzb zzb = zzb(context, com_google_android_gms_internal_zzew);
            if (zzb != null) {
                return zzb;
            }
        }
        zzb.zzaI("Using RewardedVideoAd from the client jar.");
        return zzn.zzcU().createRewardedVideoAd(context, com_google_android_gms_internal_zzew, new VersionInfoParcel(8487000, 8487000, true));
    }

    protected zzc zzad(IBinder iBinder) {
        return zzc.zza.zzab(iBinder);
    }

    protected /* synthetic */ Object zzd(IBinder iBinder) {
        return zzad(iBinder);
    }
}
