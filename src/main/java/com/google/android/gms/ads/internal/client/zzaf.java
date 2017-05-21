package com.google.android.gms.ads.internal.client;

import android.content.Context;
import android.os.IBinder;
import com.google.android.gms.ads.internal.client.zzy.zza;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.dynamic.zzg;
import com.google.android.gms.internal.zzhb;

@zzhb
public class zzaf extends zzg<zzz> {
    public zzaf() {
        super("com.google.android.gms.ads.MobileAdsSettingManagerCreatorImpl");
    }

    private zzy zzv(Context context) {
        try {
            return zza.zzo(((zzz) zzaB(context)).zza(zze.zzC(context), 8487000));
        } catch (Throwable e) {
            zzb.zzd("Could not get remote MobileAdsSettingManager.", e);
            return null;
        } catch (Throwable e2) {
            zzb.zzd("Could not get remote MobileAdsSettingManager.", e2);
            return null;
        }
    }

    protected /* synthetic */ Object zzd(IBinder iBinder) {
        return zzq(iBinder);
    }

    protected zzz zzq(IBinder iBinder) {
        return zzz.zza.zzp(iBinder);
    }

    public zzy zzu(Context context) {
        if (zzn.zzcS().zzU(context)) {
            zzy zzv = zzv(context);
            if (zzv != null) {
                return zzv;
            }
        }
        zzb.zzaI("Using MobileAdsSettingManager from the client jar.");
        VersionInfoParcel versionInfoParcel = new VersionInfoParcel(8487000, 8487000, true);
        return zzn.zzcU().getMobileAdsSettingsManager(context);
    }
}
