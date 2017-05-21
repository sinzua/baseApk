package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.FrameLayout;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.dynamic.zzg;
import com.google.android.gms.internal.zzcj.zza;

@zzhb
public class zzcv extends zzg<zzck> {
    public zzcv() {
        super("com.google.android.gms.ads.NativeAdViewDelegateCreatorImpl");
    }

    private zzcj zzb(Context context, FrameLayout frameLayout, FrameLayout frameLayout2) {
        Throwable e;
        try {
            return zza.zzu(((zzck) zzaB(context)).zza(zze.zzC(context), zze.zzC(frameLayout), zze.zzC(frameLayout2), 8487000));
        } catch (RemoteException e2) {
            e = e2;
            zzb.zzd("Could not create remote NativeAdViewDelegate.", e);
            return null;
        } catch (zzg.zza e3) {
            e = e3;
            zzb.zzd("Could not create remote NativeAdViewDelegate.", e);
            return null;
        }
    }

    protected zzck zzD(IBinder iBinder) {
        return zzck.zza.zzv(iBinder);
    }

    public zzcj zza(Context context, FrameLayout frameLayout, FrameLayout frameLayout2) {
        if (zzn.zzcS().zzU(context)) {
            zzcj zzb = zzb(context, frameLayout, frameLayout2);
            if (zzb != null) {
                return zzb;
            }
        }
        zzb.zzaI("Using NativeAdViewDelegate from the client jar.");
        return zzn.zzcU().createNativeAdViewDelegate(frameLayout, frameLayout2);
    }

    protected /* synthetic */ Object zzd(IBinder iBinder) {
        return zzD(iBinder);
    }
}
