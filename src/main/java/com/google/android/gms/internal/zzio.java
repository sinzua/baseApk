package com.google.android.gms.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.android.gms.ads.internal.zzr;

@zzhb
public class zzio extends Handler {
    public zzio(Looper looper) {
        super(looper);
    }

    public void handleMessage(Message msg) {
        try {
            super.handleMessage(msg);
        } catch (Throwable e) {
            zzr.zzbF().zzb(e, false);
            throw e;
        }
    }
}
