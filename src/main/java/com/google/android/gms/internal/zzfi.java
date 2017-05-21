package com.google.android.gms.internal;

import android.location.Location;
import android.support.annotation.Nullable;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAdOptions.Builder;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.mediation.NativeMediationAdRequest;
import java.util.Date;
import java.util.List;
import java.util.Set;

@zzhb
public final class zzfi implements NativeMediationAdRequest {
    private final int zzCH;
    private final Date zzbf;
    private final Set<String> zzbh;
    private final boolean zzbi;
    private final Location zzbj;
    private final NativeAdOptionsParcel zzpP;
    private final List<String> zzpQ;
    private final int zztT;
    private final boolean zzuf;

    public zzfi(@Nullable Date date, int i, @Nullable Set<String> set, @Nullable Location location, boolean z, int i2, NativeAdOptionsParcel nativeAdOptionsParcel, List<String> list, boolean z2) {
        this.zzbf = date;
        this.zztT = i;
        this.zzbh = set;
        this.zzbj = location;
        this.zzbi = z;
        this.zzCH = i2;
        this.zzpP = nativeAdOptionsParcel;
        this.zzpQ = list;
        this.zzuf = z2;
    }

    public Date getBirthday() {
        return this.zzbf;
    }

    public int getGender() {
        return this.zztT;
    }

    public Set<String> getKeywords() {
        return this.zzbh;
    }

    public Location getLocation() {
        return this.zzbj;
    }

    public NativeAdOptions getNativeAdOptions() {
        return this.zzpP == null ? null : new Builder().setReturnUrlsForImageAssets(this.zzpP.zzyA).setImageOrientation(this.zzpP.zzyB).setRequestMultipleImages(this.zzpP.zzyC).build();
    }

    public boolean isAppInstallAdRequested() {
        return this.zzpQ != null && this.zzpQ.contains("2");
    }

    public boolean isContentAdRequested() {
        return this.zzpQ != null && this.zzpQ.contains("1");
    }

    public boolean isDesignedForFamilies() {
        return this.zzuf;
    }

    public boolean isTesting() {
        return this.zzbi;
    }

    public int taggedForChildDirectedTreatment() {
        return this.zzCH;
    }
}
