package com.google.android.gms.ads.formats;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public final class NativeContentAdView extends NativeAdView {
    public NativeContentAdView(Context context) {
        super(context);
    }

    public NativeContentAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NativeContentAdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NativeContentAdView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public final View getAdvertiserView() {
        return super.zzn("1004");
    }

    public final View getBodyView() {
        return super.zzn("1002");
    }

    public final View getCallToActionView() {
        return super.zzn("1003");
    }

    public final View getHeadlineView() {
        return super.zzn("1001");
    }

    public final View getImageView() {
        return super.zzn("1005");
    }

    public final View getLogoView() {
        return super.zzn("1006");
    }

    public final void setAdvertiserView(View view) {
        super.zza("1004", view);
    }

    public final void setBodyView(View view) {
        super.zza("1002", view);
    }

    public final void setCallToActionView(View view) {
        super.zza("1003", view);
    }

    public final void setHeadlineView(View view) {
        super.zza("1001", view);
    }

    public final void setImageView(View view) {
        super.zza("1005", view);
    }

    public final void setLogoView(View view) {
        super.zza("1006", view);
    }
}
