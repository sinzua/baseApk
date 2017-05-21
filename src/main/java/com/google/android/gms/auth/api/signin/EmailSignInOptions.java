package com.google.android.gms.auth.api.signin;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Patterns;
import com.google.android.gms.auth.api.signin.internal.zze;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;
import org.json.JSONObject;

public class EmailSignInOptions implements SafeParcelable {
    public static final Creator<EmailSignInOptions> CREATOR = new zza();
    final int versionCode;
    private final Uri zzWL;
    private String zzWM;
    private Uri zzWN;

    EmailSignInOptions(int versionCode, Uri serverWidgetUrl, String modeQueryName, Uri tosUrl) {
        zzx.zzb((Object) serverWidgetUrl, (Object) "Server widget url cannot be null in order to use email/password sign in.");
        zzx.zzh(serverWidgetUrl.toString(), "Server widget url cannot be null in order to use email/password sign in.");
        zzx.zzb(Patterns.WEB_URL.matcher(serverWidgetUrl.toString()).matches(), (Object) "Invalid server widget url");
        this.versionCode = versionCode;
        this.zzWL = serverWidgetUrl;
        this.zzWM = modeQueryName;
        this.zzWN = tosUrl;
    }

    private JSONObject zzmJ() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("serverWidgetUrl", this.zzWL.toString());
            if (!TextUtils.isEmpty(this.zzWM)) {
                jSONObject.put("modeQueryName", this.zzWM);
            }
            if (this.zzWN != null) {
                jSONObject.put("tosUrl", this.zzWN.toString());
            }
            return jSONObject;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            EmailSignInOptions emailSignInOptions = (EmailSignInOptions) obj;
            if (!this.zzWL.equals(emailSignInOptions.zzmF())) {
                return false;
            }
            if (this.zzWN == null) {
                if (emailSignInOptions.zzmG() != null) {
                    return false;
                }
            } else if (!this.zzWN.equals(emailSignInOptions.zzmG())) {
                return false;
            }
            if (TextUtils.isEmpty(this.zzWM)) {
                if (!TextUtils.isEmpty(emailSignInOptions.zzmH())) {
                    return false;
                }
            } else if (!this.zzWM.equals(emailSignInOptions.zzmH())) {
                return false;
            }
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public int hashCode() {
        return new zze().zzp(this.zzWL).zzp(this.zzWN).zzp(this.zzWM).zzne();
    }

    public void writeToParcel(Parcel out, int flags) {
        zza.zza(this, out, flags);
    }

    public Uri zzmF() {
        return this.zzWL;
    }

    public Uri zzmG() {
        return this.zzWN;
    }

    public String zzmH() {
        return this.zzWM;
    }

    public String zzmI() {
        return zzmJ().toString();
    }
}
