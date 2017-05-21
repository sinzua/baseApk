package com.google.android.gms.common.api;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;

public final class Scope implements SafeParcelable {
    public static final Creator<Scope> CREATOR = new zzb();
    final int mVersionCode;
    private final String zzagB;

    Scope(int versionCode, String scopeUri) {
        zzx.zzh(scopeUri, "scopeUri must not be null or empty");
        this.mVersionCode = versionCode;
        this.zzagB = scopeUri;
    }

    public Scope(String scopeUri) {
        this(1, scopeUri);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return !(o instanceof Scope) ? false : this.zzagB.equals(((Scope) o).zzagB);
    }

    public int hashCode() {
        return this.zzagB.hashCode();
    }

    public String toString() {
        return this.zzagB;
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzb.zza(this, dest, flags);
    }

    public String zzpb() {
        return this.zzagB;
    }
}
