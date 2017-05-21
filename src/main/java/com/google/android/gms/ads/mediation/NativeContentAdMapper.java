package com.google.android.gms.ads.mediation;

import com.google.android.gms.ads.formats.NativeAd.Image;
import java.util.List;

public abstract class NativeContentAdMapper extends NativeAdMapper {
    private Image zzOp;
    private String zzxW;
    private List<Image> zzxX;
    private String zzxY;
    private String zzya;
    private String zzyh;

    public final String getAdvertiser() {
        return this.zzyh;
    }

    public final String getBody() {
        return this.zzxY;
    }

    public final String getCallToAction() {
        return this.zzya;
    }

    public final String getHeadline() {
        return this.zzxW;
    }

    public final List<Image> getImages() {
        return this.zzxX;
    }

    public final Image getLogo() {
        return this.zzOp;
    }

    public final void setAdvertiser(String advertiser) {
        this.zzyh = advertiser;
    }

    public final void setBody(String body) {
        this.zzxY = body;
    }

    public final void setCallToAction(String callToAction) {
        this.zzya = callToAction;
    }

    public final void setHeadline(String headline) {
        this.zzxW = headline;
    }

    public final void setImages(List<Image> images) {
        this.zzxX = images;
    }

    public final void setLogo(Image logo) {
        this.zzOp = logo;
    }
}
