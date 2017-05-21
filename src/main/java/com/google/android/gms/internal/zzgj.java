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
public final class zzgj extends zzg<zzgf> {
    private static final zzgj zzGa = new zzgj();

    private static final class zza extends Exception {
        public zza(String str) {
            super(str);
        }
    }

    private zzgj() {
        super("com.google.android.gms.ads.InAppPurchaseManagerCreatorImpl");
    }

    @Nullable
    public static zzge createInAppPurchaseManager(Activity activity) {
        try {
            if (!zzb(activity)) {
                zzge zzd = zzGa.zzd(activity);
                if (zzd != null) {
                    return zzd;
                }
            }
            zzb.zzaI("Using AdOverlay from the client jar.");
            return zzn.zzcU().createInAppPurchaseManager(activity);
        } catch (zza e) {
            zzb.zzaK(e.getMessage());
            return null;
        }
    }

    private static boolean zzb(Activity activity) throws zza {
        Intent intent = activity.getIntent();
        if (intent.hasExtra("com.google.android.gms.ads.internal.purchase.useClientJar")) {
            return intent.getBooleanExtra("com.google.android.gms.ads.internal.purchase.useClientJar", false);
        }
        throw new zza("InAppPurchaseManager requires the useClientJar flag in intent extras.");
    }

    private zzge zzd(Activity activity) {
        try {
            return com.google.android.gms.internal.zzge.zza.zzQ(((zzgf) zzaB(activity)).zzf(zze.zzC(activity)));
        } catch (Throwable e) {
            zzb.zzd("Could not create remote InAppPurchaseManager.", e);
            return null;
        } catch (Throwable e2) {
            zzb.zzd("Could not create remote InAppPurchaseManager.", e2);
            return null;
        }
    }

    protected zzgf zzU(IBinder iBinder) {
        return com.google.android.gms.internal.zzgf.zza.zzR(iBinder);
    }

    protected /* synthetic */ Object zzd(IBinder iBinder) {
        return zzU(iBinder);
    }
}
