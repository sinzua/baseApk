package com.google.android.gms.ads.internal.client;

import android.content.Context;
import android.os.IBinder;
import com.google.android.gms.ads.internal.client.zzs.zza;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.dynamic.zzg;
import com.google.android.gms.internal.zzew;
import com.google.android.gms.internal.zzhb;

@zzhb
public final class zzd extends zzg<zzt> {
    private static final zzd zztB = new zzd();

    private zzd() {
        super("com.google.android.gms.ads.AdLoaderBuilderCreatorImpl");
    }

    public static zzs zza(Context context, String str, zzew com_google_android_gms_internal_zzew) {
        if (zzn.zzcS().zzU(context)) {
            zzs zzb = zztB.zzb(context, str, com_google_android_gms_internal_zzew);
            if (zzb != null) {
                return zzb;
            }
        }
        zzb.zzaI("Using AdLoader from the client jar.");
        return zzn.zzcU().createAdLoaderBuilder(context, str, com_google_android_gms_internal_zzew, new VersionInfoParcel(8487000, 8487000, true));
    }

    private zzs zzb(Context context, String str, zzew com_google_android_gms_internal_zzew) {
        try {
            return zza.zzi(((zzt) zzaB(context)).zza(zze.zzC(context), str, com_google_android_gms_internal_zzew, 8487000));
        } catch (Throwable e) {
            zzb.zzd("Could not create remote builder for AdLoader.", e);
            return null;
        } catch (Throwable e2) {
            zzb.zzd("Could not create remote builder for AdLoader.", e2);
            return null;
        }
    }

    protected zzt zzc(IBinder iBinder) {
        return zzt.zza.zzj(iBinder);
    }

    protected /* synthetic */ Object zzd(IBinder iBinder) {
        return zzc(iBinder);
    }
}
