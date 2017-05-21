package com.google.android.gms.internal;

import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import com.google.android.gms.ads.internal.zzr;
import java.lang.ref.WeakReference;

@zzhb
class zzjl extends zzjn implements OnGlobalLayoutListener {
    private final WeakReference<OnGlobalLayoutListener> zzNv;

    public zzjl(View view, OnGlobalLayoutListener onGlobalLayoutListener) {
        super(view);
        this.zzNv = new WeakReference(onGlobalLayoutListener);
    }

    public void onGlobalLayout() {
        OnGlobalLayoutListener onGlobalLayoutListener = (OnGlobalLayoutListener) this.zzNv.get();
        if (onGlobalLayoutListener != null) {
            onGlobalLayoutListener.onGlobalLayout();
        } else {
            detach();
        }
    }

    protected void zza(ViewTreeObserver viewTreeObserver) {
        viewTreeObserver.addOnGlobalLayoutListener(this);
    }

    protected void zzb(ViewTreeObserver viewTreeObserver) {
        zzr.zzbE().zza(viewTreeObserver, (OnGlobalLayoutListener) this);
    }
}
