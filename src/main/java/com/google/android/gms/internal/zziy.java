package com.google.android.gms.internal;

import android.content.Context;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@zzhb
public final class zziy extends zzim {
    private final Context mContext;
    private final String zzF;
    private final String zzsy;
    private String zzzN = null;

    public zziy(Context context, String str, String str2) {
        this.mContext = context;
        this.zzsy = str;
        this.zzF = str2;
    }

    public zziy(Context context, String str, String str2, String str3) {
        this.mContext = context;
        this.zzsy = str;
        this.zzF = str2;
        this.zzzN = str3;
    }

    public void onStop() {
    }

    public void zzbr() {
        HttpURLConnection httpURLConnection;
        try {
            zzin.v("Pinging URL: " + this.zzF);
            httpURLConnection = (HttpURLConnection) new URL(this.zzF).openConnection();
            if (TextUtils.isEmpty(this.zzzN)) {
                zzr.zzbC().zza(this.mContext, this.zzsy, true, httpURLConnection);
            } else {
                zzr.zzbC().zza(this.mContext, this.zzsy, true, httpURLConnection, this.zzzN);
            }
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode < 200 || responseCode >= 300) {
                zzb.zzaK("Received non-success response code " + responseCode + " from pinging URL: " + this.zzF);
            }
            httpURLConnection.disconnect();
        } catch (IndexOutOfBoundsException e) {
            zzb.zzaK("Error while parsing ping URL: " + this.zzF + ". " + e.getMessage());
        } catch (IOException e2) {
            zzb.zzaK("Error while pinging URL: " + this.zzF + ". " + e2.getMessage());
        } catch (RuntimeException e3) {
            zzb.zzaK("Error while pinging URL: " + this.zzF + ". " + e3.getMessage());
        } catch (Throwable th) {
            httpURLConnection.disconnect();
        }
    }
}
