package com.google.android.gms.ads;

import android.content.Context;
import android.support.annotation.RequiresPermission;
import com.google.android.gms.ads.internal.client.zzad;
import com.google.android.gms.ads.internal.client.zzae;
import com.google.android.gms.ads.reward.RewardedVideoAd;

public class MobileAds {

    public static final class Settings {
        private final zzae zzoM = new zzae();

        @Deprecated
        public String getTrackingId() {
            return this.zzoM.getTrackingId();
        }

        @Deprecated
        public boolean isGoogleAnalyticsEnabled() {
            return this.zzoM.isGoogleAnalyticsEnabled();
        }

        @Deprecated
        public Settings setGoogleAnalyticsEnabled(boolean enable) {
            this.zzoM.zzm(enable);
            return this;
        }

        @Deprecated
        public Settings setTrackingId(String trackingId) {
            this.zzoM.zzJ(trackingId);
            return this;
        }

        zzae zzaG() {
            return this.zzoM;
        }
    }

    private MobileAds() {
    }

    public static RewardedVideoAd getRewardedVideoAdInstance(Context context) {
        return zzad.zzdi().getRewardedVideoAdInstance(context);
    }

    public static void initialize(Context context) {
        zzad.zzdi().initialize(context);
    }

    @Deprecated
    @RequiresPermission("android.permission.INTERNET")
    public static void initialize(Context context, String applicationCode) {
        initialize(context, applicationCode, null);
    }

    @Deprecated
    @RequiresPermission("android.permission.INTERNET")
    public static void initialize(Context context, String applicationCode, Settings settings) {
        zzad.zzdi().zza(context, applicationCode, settings == null ? null : settings.zzaG());
    }

    public static void setAppVolume(float volume) {
        zzad.zzdi().setAppVolume(volume);
    }
}
