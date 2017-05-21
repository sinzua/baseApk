package com.google.android.gms.ads.internal.overlay;

import android.content.Intent;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzhb;

@zzhb
public final class AdLauncherIntentInfoParcel implements SafeParcelable {
    public static final zzb CREATOR = new zzb();
    public final Intent intent;
    public final String intentAction;
    public final String mimeType;
    public final String packageName;
    public final String url;
    public final int versionCode;
    public final String zzDK;
    public final String zzDL;
    public final String zzDM;

    public AdLauncherIntentInfoParcel(int versionCode, String intentAction, String url, String mimeType, String packageName, String componentName, String intentFlagsString, String intentExtrasString, Intent intent) {
        this.versionCode = versionCode;
        this.intentAction = intentAction;
        this.url = url;
        this.mimeType = mimeType;
        this.packageName = packageName;
        this.zzDK = componentName;
        this.zzDL = intentFlagsString;
        this.zzDM = intentExtrasString;
        this.intent = intent;
    }

    public AdLauncherIntentInfoParcel(Intent intent) {
        this(2, null, null, null, null, null, null, null, intent);
    }

    public AdLauncherIntentInfoParcel(String intentAction, String url, String mimeType, String packageName, String componentName, String intentFlagsString, String intentExtrasString) {
        this(2, intentAction, url, mimeType, packageName, componentName, intentFlagsString, intentExtrasString, null);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzb.zza(this, out, flags);
    }
}
