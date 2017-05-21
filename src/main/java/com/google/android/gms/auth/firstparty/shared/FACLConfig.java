package com.google.android.gms.auth.firstparty.shared;

import android.os.Parcel;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;

public class FACLConfig implements SafeParcelable {
    public static final zza CREATOR = new zza();
    final int version;
    boolean zzYm;
    String zzYn;
    boolean zzYo;
    boolean zzYp;
    boolean zzYq;
    boolean zzYr;

    FACLConfig(int version, boolean isAllCirclesVisible, String visibleEdges, boolean isAllContactsVisible, boolean showCircles, boolean showContacts, boolean hasShowCircles) {
        this.version = version;
        this.zzYm = isAllCirclesVisible;
        this.zzYn = visibleEdges;
        this.zzYo = isAllContactsVisible;
        this.zzYp = showCircles;
        this.zzYq = showContacts;
        this.zzYr = hasShowCircles;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (!(o instanceof FACLConfig)) {
            return false;
        }
        FACLConfig fACLConfig = (FACLConfig) o;
        return this.zzYm == fACLConfig.zzYm && TextUtils.equals(this.zzYn, fACLConfig.zzYn) && this.zzYo == fACLConfig.zzYo && this.zzYp == fACLConfig.zzYp && this.zzYq == fACLConfig.zzYq && this.zzYr == fACLConfig.zzYr;
    }

    public int hashCode() {
        return zzw.hashCode(Boolean.valueOf(this.zzYm), this.zzYn, Boolean.valueOf(this.zzYo), Boolean.valueOf(this.zzYp), Boolean.valueOf(this.zzYq), Boolean.valueOf(this.zzYr));
    }

    public void writeToParcel(Parcel dest, int flags) {
        zza.zza(this, dest, flags);
    }
}
