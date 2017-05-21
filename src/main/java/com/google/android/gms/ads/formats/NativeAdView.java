package com.google.android.gms.ads.formats;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzcj;

public abstract class NativeAdView extends FrameLayout {
    private final FrameLayout zzoQ;
    private final zzcj zzoR = zzaI();

    public NativeAdView(Context context) {
        super(context);
        this.zzoQ = zzn(context);
    }

    public NativeAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.zzoQ = zzn(context);
    }

    public NativeAdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.zzoQ = zzn(context);
    }

    public NativeAdView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.zzoQ = zzn(context);
    }

    private zzcj zzaI() {
        zzx.zzb(this.zzoQ, (Object) "createDelegate must be called after mOverlayFrame has been created");
        return zzn.zzcW().zza(this.zzoQ.getContext(), this, this.zzoQ);
    }

    private FrameLayout zzn(Context context) {
        View zzo = zzo(context);
        zzo.setLayoutParams(new LayoutParams(-1, -1));
        addView(zzo);
        return zzo;
    }

    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        super.bringChildToFront(this.zzoQ);
    }

    public void bringChildToFront(View child) {
        super.bringChildToFront(child);
        if (this.zzoQ != child) {
            super.bringChildToFront(this.zzoQ);
        }
    }

    public void destroy() {
        try {
            this.zzoR.destroy();
        } catch (Throwable e) {
            zzb.zzb("Unable to destroy native ad view", e);
        }
    }

    public void removeAllViews() {
        super.removeAllViews();
        super.addView(this.zzoQ);
    }

    public void removeView(View child) {
        if (this.zzoQ != child) {
            super.removeView(child);
        }
    }

    public void setNativeAd(NativeAd ad) {
        try {
            this.zzoR.zza((zzd) ad.zzaH());
        } catch (Throwable e) {
            zzb.zzb("Unable to call setNativeAd on delegate", e);
        }
    }

    protected void zza(String str, View view) {
        try {
            this.zzoR.zza(str, zze.zzC(view));
        } catch (Throwable e) {
            zzb.zzb("Unable to call setAssetView on delegate", e);
        }
    }

    protected View zzn(String str) {
        try {
            zzd zzK = this.zzoR.zzK(str);
            if (zzK != null) {
                return (View) zze.zzp(zzK);
            }
        } catch (Throwable e) {
            zzb.zzb("Unable to call getAssetView on delegate", e);
        }
        return null;
    }

    FrameLayout zzo(Context context) {
        return new FrameLayout(context);
    }
}
