package com.google.android.gms.common.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.common.api.Api.zzd;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

public class zzad<T extends IInterface> extends zzj<T> {
    private final zzd<T> zzamx;

    public zzad(Context context, Looper looper, int i, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, zzf com_google_android_gms_common_internal_zzf, zzd com_google_android_gms_common_api_Api_zzd) {
        super(context, looper, i, com_google_android_gms_common_internal_zzf, connectionCallbacks, onConnectionFailedListener);
        this.zzamx = com_google_android_gms_common_api_Api_zzd;
    }

    protected T zzW(IBinder iBinder) {
        return this.zzamx.zzW(iBinder);
    }

    protected void zzc(int i, T t) {
        this.zzamx.zza(i, t);
    }

    protected String zzgu() {
        return this.zzamx.zzgu();
    }

    protected String zzgv() {
        return this.zzamx.zzgv();
    }
}
