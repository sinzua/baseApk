package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.dynamic.zzg;

@zzhb
public final class zzfu extends zzg<zzfw> {
    private static final zzfu zzFp = new zzfu();

    private static final class zza extends Exception {
        public zza(String str) {
            super(str);
        }
    }

    private zzfu() {
        super("com.google.android.gms.ads.AdOverlayCreatorImpl");
    }

    @Nullable
    public static zzfv createAdOverlay(Activity activity) {
        try {
            if (!zzb(activity)) {
                zzfv zzc = zzFp.zzc(activity);
                if (zzc != null) {
                    return zzc;
                }
            }
            zzb.zzaI("Using AdOverlay from the client jar.");
            return zzn.zzcU().createAdOverlay(activity);
        } catch (zza e) {
            zzb.zzaK(e.getMessage());
            return null;
        }
    }

    private static boolean zzb(Activity activity) throws zza {
        Intent intent = activity.getIntent();
        if (intent.hasExtra("com.google.android.gms.ads.internal.overlay.useClientJar")) {
            return intent.getBooleanExtra("com.google.android.gms.ads.internal.overlay.useClientJar", false);
        }
        throw new zza("Ad overlay requires the useClientJar flag in intent extras.");
    }

    private zzfv zzc(Activity activity) {
        try {
            return com.google.android.gms.internal.zzfv.zza.zzL(((zzfw) zzaB(activity)).zze(zze.zzC(activity)));
        } catch (Throwable e) {
            zzb.zzd("Could not create remote AdOverlay.", e);
            return null;
        } catch (Throwable e2) {
            zzb.zzd("Could not create remote AdOverlay.", e2);
            return null;
        }
    }

    protected zzfw zzK(IBinder iBinder) {
        return com.google.android.gms.internal.zzfw.zza.zzM(iBinder);
    }

    protected /* synthetic */ Object zzd(IBinder iBinder) {
        return zzK(iBinder);
    }
}
