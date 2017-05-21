package com.google.android.gms.auth.api.signin;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.R;
import com.google.android.gms.auth.api.credentials.IdentityProviders;

public enum zzd {
    GOOGLE("google.com", R.string.auth_google_play_services_client_google_display_name, IdentityProviders.GOOGLE),
    FACEBOOK("facebook.com", R.string.auth_google_play_services_client_facebook_display_name, IdentityProviders.FACEBOOK);
    
    private final String zzVY;
    private final String zzXj;
    private final int zzXk;

    private zzd(String str, int i, String str2) {
        this.zzXj = str;
        this.zzXk = i;
        this.zzVY = str2;
    }

    public static zzd zzbL(String str) {
        if (str != null) {
            for (zzd com_google_android_gms_auth_api_signin_zzd : values()) {
                if (com_google_android_gms_auth_api_signin_zzd.zzmT().equals(str)) {
                    return com_google_android_gms_auth_api_signin_zzd;
                }
            }
            Log.w("IdProvider", "Unrecognized providerId: " + str);
        }
        return null;
    }

    public String toString() {
        return this.zzXj;
    }

    public CharSequence zzae(Context context) {
        return context.getResources().getString(this.zzXk);
    }

    public String zzmT() {
        return this.zzXj;
    }
}
