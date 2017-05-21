package com.google.android.gms.ads.search;

import android.content.Context;
import android.support.annotation.RequiresPermission;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.internal.client.zzab;

public final class SearchAdView extends ViewGroup {
    private final zzab zzoJ;

    public SearchAdView(Context context) {
        super(context);
        this.zzoJ = new zzab(this);
    }

    public SearchAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.zzoJ = new zzab(this, attrs, false);
    }

    public SearchAdView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.zzoJ = new zzab(this, attrs, false);
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

    @RequiresPermission("android.permission.INTERNET")
    public void loadAd(SearchAdRequest searchAdRequest) {
        this.zzoJ.zza(searchAdRequest.zzaE());
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
    }

    public void setAdSize(AdSize adSize) {
        this.zzoJ.setAdSizes(adSize);
    }

    public void setAdUnitId(String adUnitId) {
        this.zzoJ.setAdUnitId(adUnitId);
    }
}
