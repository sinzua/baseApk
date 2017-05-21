package com.google.android.gms.internal;

import android.os.Bundle;
import android.view.View;
import com.google.android.gms.ads.formats.NativeAd.Image;
import com.google.android.gms.ads.internal.formats.zzc;
import com.google.android.gms.ads.mediation.NativeAppInstallAdMapper;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzfb.zza;
import java.util.ArrayList;
import java.util.List;

@zzhb
public class zzfg extends zza {
    private final NativeAppInstallAdMapper zzCM;

    public zzfg(NativeAppInstallAdMapper nativeAppInstallAdMapper) {
        this.zzCM = nativeAppInstallAdMapper;
    }

    public String getBody() {
        return this.zzCM.getBody();
    }

    public String getCallToAction() {
        return this.zzCM.getCallToAction();
    }

    public Bundle getExtras() {
        return this.zzCM.getExtras();
    }

    public String getHeadline() {
        return this.zzCM.getHeadline();
    }

    public List getImages() {
        List<Image> images = this.zzCM.getImages();
        if (images == null) {
            return null;
        }
        List arrayList = new ArrayList();
        for (Image image : images) {
            arrayList.add(new zzc(image.getDrawable(), image.getUri(), image.getScale()));
        }
        return arrayList;
    }

    public boolean getOverrideClickHandling() {
        return this.zzCM.getOverrideClickHandling();
    }

    public boolean getOverrideImpressionRecording() {
        return this.zzCM.getOverrideImpressionRecording();
    }

    public String getPrice() {
        return this.zzCM.getPrice();
    }

    public double getStarRating() {
        return this.zzCM.getStarRating();
    }

    public String getStore() {
        return this.zzCM.getStore();
    }

    public void recordImpression() {
        this.zzCM.recordImpression();
    }

    public void zzc(zzd com_google_android_gms_dynamic_zzd) {
        this.zzCM.handleClick((View) zze.zzp(com_google_android_gms_dynamic_zzd));
    }

    public void zzd(zzd com_google_android_gms_dynamic_zzd) {
        this.zzCM.trackView((View) zze.zzp(com_google_android_gms_dynamic_zzd));
    }

    public zzch zzdK() {
        Image icon = this.zzCM.getIcon();
        return icon != null ? new zzc(icon.getDrawable(), icon.getUri(), icon.getScale()) : null;
    }
}
