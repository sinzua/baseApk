package com.google.android.gms.ads.mediation;

import com.google.android.gms.ads.formats.NativeAd.Image;
import java.util.List;

public abstract class NativeAppInstallAdMapper extends NativeAdMapper {
    private Image zzOo;
    private String zzxW;
    private List<Image> zzxX;
    private String zzxY;
    private String zzya;
    private double zzyb;
    private String zzyc;
    private String zzyd;

    public final String getBody() {
        return this.zzxY;
    }

    public final String getCallToAction() {
        return this.zzya;
    }

    public final String getHeadline() {
        return this.zzxW;
    }

    public final Image getIcon() {
        return this.zzOo;
    }

    public final List<Image> getImages() {
        return this.zzxX;
    }

    public final String getPrice() {
        return this.zzyd;
    }

    public final double getStarRating() {
        return this.zzyb;
    }

    public final String getStore() {
        return this.zzyc;
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

    public final void setIcon(Image icon) {
        this.zzOo = icon;
    }

    public final void setImages(List<Image> images) {
        this.zzxX = images;
    }

    public final void setPrice(String price) {
        this.zzyd = price;
    }

    public final void setStarRating(double starRating) {
        this.zzyb = starRating;
    }

    public final void setStore(String store) {
        this.zzyc = store;
    }
}
