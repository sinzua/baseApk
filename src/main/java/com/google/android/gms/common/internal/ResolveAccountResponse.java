package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzp.zza;

public class ResolveAccountResponse implements SafeParcelable {
    public static final Creator<ResolveAccountResponse> CREATOR = new zzz();
    final int mVersionCode;
    private boolean zzahx;
    IBinder zzakA;
    private ConnectionResult zzams;
    private boolean zzamt;

    ResolveAccountResponse(int versionCode, IBinder accountAccessorBinder, ConnectionResult connectionResult, boolean saveDefaultAccount, boolean isFromCrossClientAuth) {
        this.mVersionCode = versionCode;
        this.zzakA = accountAccessorBinder;
        this.zzams = connectionResult;
        this.zzahx = saveDefaultAccount;
        this.zzamt = isFromCrossClientAuth;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResolveAccountResponse)) {
            return false;
        }
        ResolveAccountResponse resolveAccountResponse = (ResolveAccountResponse) o;
        return this.zzams.equals(resolveAccountResponse.zzams) && zzqX().equals(resolveAccountResponse.zzqX());
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzz.zza(this, dest, flags);
    }

    public zzp zzqX() {
        return zza.zzaP(this.zzakA);
    }

    public ConnectionResult zzqY() {
        return this.zzams;
    }

    public boolean zzqZ() {
        return this.zzahx;
    }

    public boolean zzra() {
        return this.zzamt;
    }
}
