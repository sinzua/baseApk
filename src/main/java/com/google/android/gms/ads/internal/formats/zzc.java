package com.google.android.gms.ads.internal.formats;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.RemoteException;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzch.zza;
import com.google.android.gms.internal.zzhb;

@zzhb
public class zzc extends zza {
    private final Uri mUri;
    private final Drawable zzxU;
    private final double zzxV;

    public zzc(Drawable drawable, Uri uri, double d) {
        this.zzxU = drawable;
        this.mUri = uri;
        this.zzxV = d;
    }

    public double getScale() {
        return this.zzxV;
    }

    public Uri getUri() throws RemoteException {
        return this.mUri;
    }

    public zzd zzdJ() throws RemoteException {
        return zze.zzC(this.zzxU);
    }
}
