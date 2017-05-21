package com.google.android.gms.auth.api.signin.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.android.gms.auth.api.signin.EmailSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;
import org.json.JSONObject;

public final class SignInConfiguration implements SafeParcelable {
    public static final Creator<SignInConfiguration> CREATOR = new zzp();
    final int versionCode;
    private final String zzXL;
    private EmailSignInOptions zzXM;
    private GoogleSignInOptions zzXN;
    private String zzXO;
    private String zzXd;

    SignInConfiguration(int versionCode, String consumerPkgName, String serverClientId, EmailSignInOptions emailConfig, GoogleSignInOptions googleConfig, String apiKey) {
        this.versionCode = versionCode;
        this.zzXL = zzx.zzcM(consumerPkgName);
        this.zzXd = serverClientId;
        this.zzXM = emailConfig;
        this.zzXN = googleConfig;
        this.zzXO = apiKey;
    }

    public SignInConfiguration(String consumerPkgName) {
        this(2, consumerPkgName, null, null, null, null);
    }

    private JSONObject zzmJ() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("consumerPackageName", this.zzXL);
            if (!TextUtils.isEmpty(this.zzXd)) {
                jSONObject.put("serverClientId", this.zzXd);
            }
            if (this.zzXM != null) {
                jSONObject.put("emailSignInOptions", this.zzXM.zzmI());
            }
            if (this.zzXN != null) {
                jSONObject.put("googleSignInOptions", this.zzXN.zzmI());
            }
            if (!TextUtils.isEmpty(this.zzXO)) {
                jSONObject.put("apiKey", this.zzXO);
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
            SignInConfiguration signInConfiguration = (SignInConfiguration) obj;
            if (!this.zzXL.equals(signInConfiguration.zznk())) {
                return false;
            }
            if (TextUtils.isEmpty(this.zzXd)) {
                if (!TextUtils.isEmpty(signInConfiguration.zzmR())) {
                    return false;
                }
            } else if (!this.zzXd.equals(signInConfiguration.zzmR())) {
                return false;
            }
            if (TextUtils.isEmpty(this.zzXO)) {
                if (!TextUtils.isEmpty(signInConfiguration.zznn())) {
                    return false;
                }
            } else if (!this.zzXO.equals(signInConfiguration.zznn())) {
                return false;
            }
            if (this.zzXM == null) {
                if (signInConfiguration.zznl() != null) {
                    return false;
                }
            } else if (!this.zzXM.equals(signInConfiguration.zznl())) {
                return false;
            }
            if (this.zzXN == null) {
                if (signInConfiguration.zznm() != null) {
                    return false;
                }
            } else if (!this.zzXN.equals(signInConfiguration.zznm())) {
                return false;
            }
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public int hashCode() {
        return new zze().zzp(this.zzXL).zzp(this.zzXd).zzp(this.zzXO).zzp(this.zzXM).zzp(this.zzXN).zzne();
    }

    public void writeToParcel(Parcel out, int flags) {
        zzp.zza(this, out, flags);
    }

    public SignInConfiguration zzj(GoogleSignInOptions googleSignInOptions) {
        this.zzXN = (GoogleSignInOptions) zzx.zzb((Object) googleSignInOptions, (Object) "GoogleSignInOptions cannot be null.");
        return this;
    }

    public String zzmI() {
        return zzmJ().toString();
    }

    public String zzmR() {
        return this.zzXd;
    }

    public String zznk() {
        return this.zzXL;
    }

    public EmailSignInOptions zznl() {
        return this.zzXM;
    }

    public GoogleSignInOptions zznm() {
        return this.zzXN;
    }

    public String zznn() {
        return this.zzXO;
    }
}
