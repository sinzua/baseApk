package com.google.android.gms.ads.search;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import com.google.android.gms.ads.internal.client.zzaa;
import com.google.android.gms.ads.internal.client.zzaa.zza;
import com.google.android.gms.ads.mediation.MediationAdapter;
import com.google.android.gms.ads.mediation.NetworkExtras;
import com.google.android.gms.ads.mediation.customevent.CustomEvent;

public final class SearchAdRequest {
    public static final int BORDER_TYPE_DASHED = 1;
    public static final int BORDER_TYPE_DOTTED = 2;
    public static final int BORDER_TYPE_NONE = 0;
    public static final int BORDER_TYPE_SOLID = 3;
    public static final int CALL_BUTTON_COLOR_DARK = 2;
    public static final int CALL_BUTTON_COLOR_LIGHT = 0;
    public static final int CALL_BUTTON_COLOR_MEDIUM = 1;
    public static final String DEVICE_ID_EMULATOR = zzaa.DEVICE_ID_EMULATOR;
    public static final int ERROR_CODE_INTERNAL_ERROR = 0;
    public static final int ERROR_CODE_INVALID_REQUEST = 1;
    public static final int ERROR_CODE_NETWORK_ERROR = 2;
    public static final int ERROR_CODE_NO_FILL = 3;
    private final int zzOA;
    private final int zzOB;
    private final int zzOC;
    private final int zzOD;
    private final String zzOE;
    private final int zzOF;
    private final String zzOG;
    private final int zzOH;
    private final int zzOI;
    private final String zzOJ;
    private final int zzOx;
    private final int zzOy;
    private final int zzOz;
    private final zzaa zzoE;
    private final int zzxO;

    public static final class Builder {
        private int zzOA;
        private int zzOB;
        private int zzOC = 0;
        private int zzOD;
        private String zzOE;
        private int zzOF;
        private String zzOG;
        private int zzOH;
        private int zzOI;
        private String zzOJ;
        private int zzOx;
        private int zzOy;
        private int zzOz;
        private final zza zzoF = new zza();
        private int zzxO;

        public Builder addCustomEventExtrasBundle(Class<? extends CustomEvent> adapterClass, Bundle customEventExtras) {
            this.zzoF.zzb(adapterClass, customEventExtras);
            return this;
        }

        public Builder addNetworkExtras(NetworkExtras networkExtras) {
            this.zzoF.zza(networkExtras);
            return this;
        }

        public Builder addNetworkExtrasBundle(Class<? extends MediationAdapter> adapterClass, Bundle networkExtras) {
            this.zzoF.zza((Class) adapterClass, networkExtras);
            return this;
        }

        public Builder addTestDevice(String deviceId) {
            this.zzoF.zzB(deviceId);
            return this;
        }

        public SearchAdRequest build() {
            return new SearchAdRequest();
        }

        public Builder setAnchorTextColor(int anchorTextColor) {
            this.zzOx = anchorTextColor;
            return this;
        }

        public Builder setBackgroundColor(int backgroundColor) {
            this.zzxO = backgroundColor;
            this.zzOy = Color.argb(0, 0, 0, 0);
            this.zzOz = Color.argb(0, 0, 0, 0);
            return this;
        }

        public Builder setBackgroundGradient(int top, int bottom) {
            this.zzxO = Color.argb(0, 0, 0, 0);
            this.zzOy = bottom;
            this.zzOz = top;
            return this;
        }

        public Builder setBorderColor(int borderColor) {
            this.zzOA = borderColor;
            return this;
        }

        public Builder setBorderThickness(int borderThickness) {
            this.zzOB = borderThickness;
            return this;
        }

        public Builder setBorderType(int borderType) {
            this.zzOC = borderType;
            return this;
        }

        public Builder setCallButtonColor(int callButtonColor) {
            this.zzOD = callButtonColor;
            return this;
        }

        public Builder setCustomChannels(String channelIds) {
            this.zzOE = channelIds;
            return this;
        }

        public Builder setDescriptionTextColor(int descriptionTextColor) {
            this.zzOF = descriptionTextColor;
            return this;
        }

        public Builder setFontFace(String fontFace) {
            this.zzOG = fontFace;
            return this;
        }

        public Builder setHeaderTextColor(int headerTextColor) {
            this.zzOH = headerTextColor;
            return this;
        }

        public Builder setHeaderTextSize(int headerTextSize) {
            this.zzOI = headerTextSize;
            return this;
        }

        public Builder setLocation(Location location) {
            this.zzoF.zzb(location);
            return this;
        }

        public Builder setQuery(String query) {
            this.zzOJ = query;
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

    private SearchAdRequest(Builder builder) {
        this.zzOx = builder.zzOx;
        this.zzxO = builder.zzxO;
        this.zzOy = builder.zzOy;
        this.zzOz = builder.zzOz;
        this.zzOA = builder.zzOA;
        this.zzOB = builder.zzOB;
        this.zzOC = builder.zzOC;
        this.zzOD = builder.zzOD;
        this.zzOE = builder.zzOE;
        this.zzOF = builder.zzOF;
        this.zzOG = builder.zzOG;
        this.zzOH = builder.zzOH;
        this.zzOI = builder.zzOI;
        this.zzOJ = builder.zzOJ;
        this.zzoE = new zzaa(builder.zzoF, this);
    }

    public int getAnchorTextColor() {
        return this.zzOx;
    }

    public int getBackgroundColor() {
        return this.zzxO;
    }

    public int getBackgroundGradientBottom() {
        return this.zzOy;
    }

    public int getBackgroundGradientTop() {
        return this.zzOz;
    }

    public int getBorderColor() {
        return this.zzOA;
    }

    public int getBorderThickness() {
        return this.zzOB;
    }

    public int getBorderType() {
        return this.zzOC;
    }

    public int getCallButtonColor() {
        return this.zzOD;
    }

    public String getCustomChannels() {
        return this.zzOE;
    }

    public <T extends CustomEvent> Bundle getCustomEventExtrasBundle(Class<T> adapterClass) {
        return this.zzoE.getCustomEventExtrasBundle(adapterClass);
    }

    public int getDescriptionTextColor() {
        return this.zzOF;
    }

    public String getFontFace() {
        return this.zzOG;
    }

    public int getHeaderTextColor() {
        return this.zzOH;
    }

    public int getHeaderTextSize() {
        return this.zzOI;
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

    public String getQuery() {
        return this.zzOJ;
    }

    public boolean isTestDevice(Context context) {
        return this.zzoE.isTestDevice(context);
    }

    zzaa zzaE() {
        return this.zzoE;
    }
}
