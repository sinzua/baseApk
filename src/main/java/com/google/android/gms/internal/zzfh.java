package com.google.android.gms.internal;

import android.os.Bundle;
import android.view.View;
import com.google.android.gms.ads.formats.NativeAd.Image;
import com.google.android.gms.ads.internal.formats.zzc;
import com.google.android.gms.ads.mediation.NativeContentAdMapper;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzfc.zza;
import java.util.ArrayList;
import java.util.List;

@zzhb
public class zzfh extends zza {
    private final NativeContentAdMapper zzCN;

    public zzfh(NativeContentAdMapper nativeContentAdMapper) {
        this.zzCN = nativeContentAdMapper;
    }

    public String getAdvertiser() {
        return this.zzCN.getAdvertiser();
    }

    public String getBody() {
        return this.zzCN.getBody();
    }

    public String getCallToAction() {
        return this.zzCN.getCallToAction();
    }

    public Bundle getExtras() {
        return this.zzCN.getExtras();
    }

    public String getHeadline() {
        return this.zzCN.getHeadline();
    }

    public List getImages() {
        List<Image> images = this.zzCN.getImages();
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
        return this.zzCN.getOverrideClickHandling();
    }

    public boolean getOverrideImpressionRecording() {
        return this.zzCN.getOverrideImpressionRecording();
    }

    public void recordImpression() {
        this.zzCN.recordImpression();
    }

    public void zzc(zzd com_google_android_gms_dynamic_zzd) {
        this.zzCN.handleClick((View) zze.zzp(com_google_android_gms_dynamic_zzd));
    }

    public void zzd(zzd com_google_android_gms_dynamic_zzd) {
        this.zzCN.trackView((View) zze.zzp(com_google_android_gms_dynamic_zzd));
    }

    public zzch zzdO() {
        Image logo = this.zzCN.getLogo();
        return logo != null ? new zzc(logo.getDrawable(), logo.getUri(), logo.getScale()) : null;
    }
}
