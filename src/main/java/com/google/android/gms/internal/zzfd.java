package com.google.android.gms.internal;

import android.location.Location;
import android.support.annotation.Nullable;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import java.util.Date;
import java.util.Set;

@zzhb
public final class zzfd implements MediationAdRequest {
    private final int zzCH;
    private final Date zzbf;
    private final Set<String> zzbh;
    private final boolean zzbi;
    private final Location zzbj;
    private final int zztT;
    private final boolean zzuf;

    public zzfd(@Nullable Date date, int i, @Nullable Set<String> set, @Nullable Location location, boolean z, int i2, boolean z2) {
        this.zzbf = date;
        this.zztT = i;
        this.zzbh = set;
        this.zzbj = location;
        this.zzbi = z;
        this.zzCH = i2;
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
