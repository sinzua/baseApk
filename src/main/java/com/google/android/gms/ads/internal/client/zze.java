package com.google.android.gms.ads.internal.client;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.ads.internal.client.zzu.zza;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.dynamic.zzg;
import com.google.android.gms.internal.zzew;
import com.google.android.gms.internal.zzhb;

@zzhb
public class zze extends zzg<zzv> {
    public zze() {
        super("com.google.android.gms.ads.AdManagerCreatorImpl");
    }

    private zzu zza(Context context, AdSizeParcel adSizeParcel, String str, zzew com_google_android_gms_internal_zzew, int i) {
        Throwable e;
        try {
            return zza.zzk(((zzv) zzaB(context)).zza(com.google.android.gms.dynamic.zze.zzC(context), adSizeParcel, str, com_google_android_gms_internal_zzew, 8487000, i));
        } catch (RemoteException e2) {
            e = e2;
            zzb.zza("Could not create remote AdManager.", e);
            return null;
        } catch (zzg.zza e3) {
            e = e3;
            zzb.zza("Could not create remote AdManager.", e);
            return null;
        }
    }

    public zzu zza(Context context, AdSizeParcel adSizeParcel, String str, zzew com_google_android_gms_internal_zzew) {
        if (zzn.zzcS().zzU(context)) {
            zzu zza = zza(context, adSizeParcel, str, com_google_android_gms_internal_zzew, 1);
            if (zza != null) {
                return zza;
            }
        }
        zzb.zzaI("Using BannerAdManager from the client jar.");
        return zzn.zzcU().createBannerAdManager(context, adSizeParcel, str, com_google_android_gms_internal_zzew, new VersionInfoParcel(8487000, 8487000, true));
    }

    public zzu zzb(Context context, AdSizeParcel adSizeParcel, String str, zzew com_google_android_gms_internal_zzew) {
        if (zzn.zzcS().zzU(context)) {
            zzu zza = zza(context, adSizeParcel, str, com_google_android_gms_internal_zzew, 2);
            if (zza != null) {
                return zza;
            }
        }
        zzb.zzaK("Using InterstitialAdManager from the client jar.");
        return zzn.zzcU().createInterstitialAdManager(context, adSizeParcel, str, com_google_android_gms_internal_zzew, new VersionInfoParcel(8487000, 8487000, true));
    }

    protected /* synthetic */ Object zzd(IBinder iBinder) {
        return zze(iBinder);
    }

    protected zzv zze(IBinder iBinder) {
        return zzv.zza.zzl(iBinder);
    }
}
