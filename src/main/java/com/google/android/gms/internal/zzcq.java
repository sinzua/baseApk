package com.google.android.gms.internal;

import com.google.android.gms.ads.formats.NativeAd.Image;
import com.google.android.gms.ads.formats.NativeCustomTemplateAd;
import com.google.android.gms.ads.internal.util.client.zzb;
import java.util.List;

@zzhb
public class zzcq implements NativeCustomTemplateAd {
    private final zzcp zzyR;

    public zzcq(zzcp com_google_android_gms_internal_zzcp) {
        this.zzyR = com_google_android_gms_internal_zzcp;
    }

    public List<String> getAvailableAssetNames() {
        try {
            return this.zzyR.getAvailableAssetNames();
        } catch (Throwable e) {
            zzb.zzb("Failed to get available asset names.", e);
            return null;
        }
    }

    public String getCustomTemplateId() {
        try {
            return this.zzyR.getCustomTemplateId();
        } catch (Throwable e) {
            zzb.zzb("Failed to get custom template id.", e);
            return null;
        }
    }

    public Image getImage(String assetName) {
        try {
            zzch zzP = this.zzyR.zzP(assetName);
            if (zzP != null) {
                return new zzci(zzP);
            }
        } catch (Throwable e) {
            zzb.zzb("Failed to get image.", e);
        }
        return null;
    }

    public CharSequence getText(String assetName) {
        try {
            return this.zzyR.zzO(assetName);
        } catch (Throwable e) {
            zzb.zzb("Failed to get string.", e);
            return null;
        }
    }

    public void performClick(String assetName) {
        try {
            this.zzyR.performClick(assetName);
        } catch (Throwable e) {
            zzb.zzb("Failed to perform click.", e);
        }
    }

    public void recordImpression() {
        try {
            this.zzyR.recordImpression();
        } catch (Throwable e) {
            zzb.zzb("Failed to record impression.", e);
        }
    }
}
