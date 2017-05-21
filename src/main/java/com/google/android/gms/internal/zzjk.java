package com.google.android.gms.internal;

import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;

@zzhb
public class zzjk {
    public static void zza(View view, OnGlobalLayoutListener onGlobalLayoutListener) {
        new zzjl(view, onGlobalLayoutListener).zzhL();
    }

    public static void zza(View view, OnScrollChangedListener onScrollChangedListener) {
        new zzjm(view, onScrollChangedListener).zzhL();
    }
}
