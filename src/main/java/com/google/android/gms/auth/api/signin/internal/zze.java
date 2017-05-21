package com.google.android.gms.auth.api.signin.internal;

public class zze {
    static int zzXy = 31;
    private int zzXz = 1;

    public zze zzP(boolean z) {
        this.zzXz = (z ? 1 : 0) + (this.zzXz * zzXy);
        return this;
    }

    public int zzne() {
        return this.zzXz;
    }

    public zze zzp(Object obj) {
        this.zzXz = (obj == null ? 0 : obj.hashCode()) + (this.zzXz * zzXy);
        return this;
    }
}
