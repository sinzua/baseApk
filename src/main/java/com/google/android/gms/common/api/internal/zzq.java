package com.google.android.gms.common.api.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.android.gms.common.internal.zzx;

public final class zzq<L> {
    private volatile L mListener;
    private final zza zzaiw;

    private final class zza extends Handler {
        final /* synthetic */ zzq zzaix;

        public zza(zzq com_google_android_gms_common_api_internal_zzq, Looper looper) {
            this.zzaix = com_google_android_gms_common_api_internal_zzq;
            super(looper);
        }

        public void handleMessage(Message msg) {
            boolean z = true;
            if (msg.what != 1) {
                z = false;
            }
            zzx.zzac(z);
            this.zzaix.zzb((zzb) msg.obj);
        }
    }

    public interface zzb<L> {
        void zzpr();

        void zzt(L l);
    }

    zzq(Looper looper, L l) {
        this.zzaiw = new zza(this, looper);
        this.mListener = zzx.zzb((Object) l, (Object) "Listener must not be null");
    }

    public void clear() {
        this.mListener = null;
    }

    public void zza(zzb<? super L> com_google_android_gms_common_api_internal_zzq_zzb__super_L) {
        zzx.zzb((Object) com_google_android_gms_common_api_internal_zzq_zzb__super_L, (Object) "Notifier must not be null");
        this.zzaiw.sendMessage(this.zzaiw.obtainMessage(1, com_google_android_gms_common_api_internal_zzq_zzb__super_L));
    }

    void zzb(zzb<? super L> com_google_android_gms_common_api_internal_zzq_zzb__super_L) {
        Object obj = this.mListener;
        if (obj == null) {
            com_google_android_gms_common_api_internal_zzq_zzb__super_L.zzpr();
            return;
        }
        try {
            com_google_android_gms_common_api_internal_zzq_zzb__super_L.zzt(obj);
        } catch (RuntimeException e) {
            com_google_android_gms_common_api_internal_zzq_zzb__super_L.zzpr();
            throw e;
        }
    }
}
