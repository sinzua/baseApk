package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.IBinder;
import com.google.android.gms.ads.formats.NativeAd.Image;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.internal.zzch.zza;
import java.util.ArrayList;
import java.util.List;

@zzhb
public class zzcm extends NativeAppInstallAd {
    private final zzcl zzyM;
    private final List<Image> zzyN = new ArrayList();
    private final zzci zzyO;

    public zzcm(zzcl com_google_android_gms_internal_zzcl) {
        zzci com_google_android_gms_internal_zzci;
        this.zzyM = com_google_android_gms_internal_zzcl;
        try {
            List<Object> images = this.zzyM.getImages();
            if (images != null) {
                for (Object zzc : images) {
                    zzch zzc2 = zzc(zzc);
                    if (zzc2 != null) {
                        this.zzyN.add(new zzci(zzc2));
                    }
                }
            }
        } catch (Throwable e) {
            zzb.zzb("Failed to get image.", e);
        }
        try {
            zzch zzdK = this.zzyM.zzdK();
            if (zzdK != null) {
                com_google_android_gms_internal_zzci = new zzci(zzdK);
                this.zzyO = com_google_android_gms_internal_zzci;
            }
        } catch (Throwable e2) {
            zzb.zzb("Failed to get icon.", e2);
        }
        com_google_android_gms_internal_zzci = null;
        this.zzyO = com_google_android_gms_internal_zzci;
    }

    public void destroy() {
        try {
            this.zzyM.destroy();
        } catch (Throwable e) {
            zzb.zzb("Failed to destroy", e);
        }
    }

    public CharSequence getBody() {
        try {
            return this.zzyM.getBody();
        } catch (Throwable e) {
            zzb.zzb("Failed to get body.", e);
            return null;
        }
    }

    public CharSequence getCallToAction() {
        try {
            return this.zzyM.getCallToAction();
        } catch (Throwable e) {
            zzb.zzb("Failed to get call to action.", e);
            return null;
        }
    }

    public Bundle getExtras() {
        try {
            return this.zzyM.getExtras();
        } catch (Throwable e) {
            zzb.zzb("Failed to get extras", e);
            return null;
        }
    }

    public CharSequence getHeadline() {
        try {
            return this.zzyM.getHeadline();
        } catch (Throwable e) {
            zzb.zzb("Failed to get headline.", e);
            return null;
        }
    }

    public Image getIcon() {
        return this.zzyO;
    }

    public List<Image> getImages() {
        return this.zzyN;
    }

    public CharSequence getPrice() {
        try {
            return this.zzyM.getPrice();
        } catch (Throwable e) {
            zzb.zzb("Failed to get price.", e);
            return null;
        }
    }

    public Double getStarRating() {
        Double d = null;
        try {
            double starRating = this.zzyM.getStarRating();
            if (starRating != -1.0d) {
                d = Double.valueOf(starRating);
            }
        } catch (Throwable e) {
            zzb.zzb("Failed to get star rating.", e);
        }
        return d;
    }

    public CharSequence getStore() {
        try {
            return this.zzyM.getStore();
        } catch (Throwable e) {
            zzb.zzb("Failed to get store", e);
            return null;
        }
    }

    protected /* synthetic */ Object zzaH() {
        return zzdL();
    }

    zzch zzc(Object obj) {
        return obj instanceof IBinder ? zza.zzt((IBinder) obj) : null;
    }

    protected zzd zzdL() {
        try {
            return this.zzyM.zzdL();
        } catch (Throwable e) {
            zzb.zzb("Failed to retrieve native ad engine.", e);
            return null;
        }
    }
}
