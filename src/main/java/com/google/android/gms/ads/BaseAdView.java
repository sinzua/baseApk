package com.google.android.gms.ads;

import android.content.Context;
import android.support.annotation.RequiresPermission;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.ads.internal.client.zza;
import com.google.android.gms.ads.internal.client.zzab;
import com.google.android.gms.ads.purchase.InAppPurchaseListener;
import com.google.android.gms.ads.purchase.PlayStorePurchaseListener;

class BaseAdView extends ViewGroup {
    private final zzab zzoJ;

    public BaseAdView(Context context, int adViewType) {
        super(context);
        this.zzoJ = new zzab(this, zze(adViewType));
    }

    public BaseAdView(Context context, AttributeSet attrs, int adViewType) {
        super(context, attrs);
        this.zzoJ = new zzab(this, attrs, false, zze(adViewType));
    }

    public BaseAdView(Context context, AttributeSet attrs, int defStyle, int adViewType) {
        super(context, attrs, defStyle);
        this.zzoJ = new zzab(this, attrs, false, zze(adViewType));
    }

    private static boolean zze(int i) {
        return i == 2;
    }

    public void destroy() {
        this.zzoJ.destroy();
    }

    public AdListener getAdListener() {
        return this.zzoJ.getAdListener();
    }

    public AdSize getAdSize() {
        return this.zzoJ.getAdSize();
    }

    public String getAdUnitId() {
        return this.zzoJ.getAdUnitId();
    }

    public InAppPurchaseListener getInAppPurchaseListener() {
        return this.zzoJ.getInAppPurchaseListener();
    }

    public String getMediationAdapterClassName() {
        return this.zzoJ.getMediationAdapterClassName();
    }

    public boolean isLoading() {
        return this.zzoJ.isLoading();
    }

    @RequiresPermission("android.permission.INTERNET")
    public void loadAd(AdRequest adRequest) {
        this.zzoJ.zza(adRequest.zzaE());
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        View childAt = getChildAt(0);
        if (childAt != null && childAt.getVisibility() != 8) {
            int measuredWidth = childAt.getMeasuredWidth();
            int measuredHeight = childAt.getMeasuredHeight();
            int i = ((right - left) - measuredWidth) / 2;
            int i2 = ((bottom - top) - measuredHeight) / 2;
            childAt.layout(i, i2, measuredWidth + i, measuredHeight + i2);
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthInPixels;
        int i = 0;
        View childAt = getChildAt(0);
        if (childAt == null || childAt.getVisibility() == 8) {
            AdSize adSize = getAdSize();
            if (adSize != null) {
                Context context = getContext();
                widthInPixels = adSize.getWidthInPixels(context);
                i = adSize.getHeightInPixels(context);
            } else {
                widthInPixels = 0;
            }
        } else {
            measureChild(childAt, widthMeasureSpec, heightMeasureSpec);
            widthInPixels = childAt.getMeasuredWidth();
            i = childAt.getMeasuredHeight();
        }
        setMeasuredDimension(View.resolveSize(Math.max(widthInPixels, getSuggestedMinimumWidth()), widthMeasureSpec), View.resolveSize(Math.max(i, getSuggestedMinimumHeight()), heightMeasureSpec));
    }

    public void pause() {
        this.zzoJ.pause();
    }

    public void resume() {
        this.zzoJ.resume();
    }

    public void setAdListener(AdListener adListener) {
        this.zzoJ.setAdListener(adListener);
        if (adListener != null && (adListener instanceof zza)) {
            this.zzoJ.zza((zza) adListener);
        } else if (adListener == null) {
            this.zzoJ.zza(null);
        }
    }

    public void setAdSize(AdSize adSize) {
        this.zzoJ.setAdSizes(adSize);
    }

    public void setAdUnitId(String adUnitId) {
        this.zzoJ.setAdUnitId(adUnitId);
    }

    public void setInAppPurchaseListener(InAppPurchaseListener inAppPurchaseListener) {
        this.zzoJ.setInAppPurchaseListener(inAppPurchaseListener);
    }

    public void setPlayStorePurchaseParams(PlayStorePurchaseListener playStorePurchaseListener, String publicKey) {
        this.zzoJ.setPlayStorePurchaseParams(playStorePurchaseListener, publicKey);
    }
}
