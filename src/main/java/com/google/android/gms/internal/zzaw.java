package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import org.json.JSONObject;

@zzhb
public final class zzaw {
    private final boolean zzsA;
    private final String zzsv;
    private final JSONObject zzsw;
    private final String zzsx;
    private final String zzsy;
    private final boolean zzsz;

    public zzaw(String str, VersionInfoParcel versionInfoParcel, String str2, JSONObject jSONObject, boolean z, boolean z2) {
        this.zzsy = versionInfoParcel.afmaVersion;
        this.zzsw = jSONObject;
        this.zzsx = str;
        this.zzsv = str2;
        this.zzsz = z;
        this.zzsA = z2;
    }

    public String zzcr() {
        return this.zzsv;
    }

    public String zzcs() {
        return this.zzsy;
    }

    public JSONObject zzct() {
        return this.zzsw;
    }

    public String zzcu() {
        return this.zzsx;
    }

    public boolean zzcv() {
        return this.zzsz;
    }

    public boolean zzcw() {
        return this.zzsA;
    }
}
