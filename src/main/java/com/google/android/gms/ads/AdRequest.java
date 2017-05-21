package com.google.android.gms.ads;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.internal.client.zzaa;
import com.google.android.gms.ads.internal.client.zzaa.zza;
import com.google.android.gms.ads.mediation.MediationAdapter;
import com.google.android.gms.ads.mediation.NetworkExtras;
import com.google.android.gms.ads.mediation.customevent.CustomEvent;
import com.google.android.gms.common.internal.zzx;
import java.util.Date;
import java.util.Set;

public final class AdRequest {
    public static final String DEVICE_ID_EMULATOR = zzaa.DEVICE_ID_EMULATOR;
    public static final int ERROR_CODE_INTERNAL_ERROR = 0;
    public static final int ERROR_CODE_INVALID_REQUEST = 1;
    public static final int ERROR_CODE_NETWORK_ERROR = 2;
    public static final int ERROR_CODE_NO_FILL = 3;
    public static final int GENDER_FEMALE = 2;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_UNKNOWN = 0;
    public static final int MAX_CONTENT_URL_LENGTH = 512;
    private final zzaa zzoE;

    public static final class Builder {
        private final zza zzoF = new zza();

        public Builder() {
            this.zzoF.zzB(AdRequest.DEVICE_ID_EMULATOR);
        }

        public Builder addCustomEventExtrasBundle(Class<? extends CustomEvent> adapterClass, Bundle customEventExtras) {
            this.zzoF.zzb(adapterClass, customEventExtras);
            return this;
        }

        public Builder addKeyword(String keyword) {
            this.zzoF.zzA(keyword);
            return this;
        }

        public Builder addNetworkExtras(NetworkExtras networkExtras) {
            this.zzoF.zza(networkExtras);
            return this;
        }

        public Builder addNetworkExtrasBundle(Class<? extends MediationAdapter> adapterClass, Bundle networkExtras) {
            this.zzoF.zza((Class) adapterClass, networkExtras);
            if (adapterClass.equals(AdMobAdapter.class) && networkExtras.getBoolean("_emulatorLiveAds")) {
                this.zzoF.zzC(AdRequest.DEVICE_ID_EMULATOR);
            }
            return this;
        }

        public Builder addTestDevice(String deviceId) {
            this.zzoF.zzB(deviceId);
            return this;
        }

        public AdRequest build() {
            return new AdRequest();
        }

        public Builder setBirthday(Date birthday) {
            this.zzoF.zza(birthday);
            return this;
        }

        public Builder setContentUrl(String contentUrl) {
            zzx.zzb((Object) contentUrl, (Object) "Content URL must be non-null.");
            zzx.zzh(contentUrl, "Content URL must be non-empty.");
            zzx.zzb(contentUrl.length() <= 512, "Content URL must not exceed %d in length.  Provided length was %d.", Integer.valueOf(512), Integer.valueOf(contentUrl.length()));
            this.zzoF.zzD(contentUrl);
            return this;
        }

        public Builder setGender(int gender) {
            this.zzoF.zzn(gender);
            return this;
        }

        public Builder setIsDesignedForFamilies(boolean isDesignedForFamilies) {
            this.zzoF.zzl(isDesignedForFamilies);
            return this;
        }

        public Builder setLocation(Location location) {
            this.zzoF.zzb(location);
            return this;
        }

        public Builder setRequestAgent(String requestAgent) {
            this.zzoF.zzF(requestAgent);
            return this;
        }

        public Builder tagForChildDirectedTreatment(boolean tagForChildDirectedTreatment) {
            this.zzoF.zzk(tagForChildDirectedTreatment);
            return this;
        }
    }

    private AdRequest(Builder builder) {
        this.zzoE = new zzaa(builder.zzoF);
    }

    public Date getBirthday() {
        return this.zzoE.getBirthday();
    }

    public String getContentUrl() {
        return this.zzoE.getContentUrl();
    }

    public <T extends CustomEvent> Bundle getCustomEventExtrasBundle(Class<T> adapterClass) {
        return this.zzoE.getCustomEventExtrasBundle(adapterClass);
    }

    public int getGender() {
        return this.zzoE.getGender();
    }

    public Set<String> getKeywords() {
        return this.zzoE.getKeywords();
    }

    public Location getLocation() {
        return this.zzoE.getLocation();
    }

    @Deprecated
    public <T extends NetworkExtras> T getNetworkExtras(Class<T> networkExtrasClass) {
        return this.zzoE.getNetworkExtras(networkExtrasClass);
    }

    public <T extends MediationAdapter> Bundle getNetworkExtrasBundle(Class<T> adapterClass) {
        return this.zzoE.getNetworkExtrasBundle(adapterClass);
    }

    public boolean isTestDevice(Context context) {
        return this.zzoE.isTestDevice(context);
    }

    public zzaa zzaE() {
        return this.zzoE;
    }
}
