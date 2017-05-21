package com.google.android.gms.internal;

import android.os.Parcel;
import android.util.Base64;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.supersonicads.sdk.precache.DownloadManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@zzhb
class zzec {
    final int zzAC;
    final String zzpS;
    final AdRequestParcel zzqH;

    zzec(AdRequestParcel adRequestParcel, String str, int i) {
        this.zzqH = adRequestParcel;
        this.zzpS = str;
        this.zzAC = i;
    }

    zzec(zzea com_google_android_gms_internal_zzea) {
        this(com_google_android_gms_internal_zzea.zzei(), com_google_android_gms_internal_zzea.getAdUnitId(), com_google_android_gms_internal_zzea.getNetworkType());
    }

    zzec(String str) throws IOException {
        String[] split = str.split("\u0000");
        if (split.length != 3) {
            throw new IOException("Incorrect field count for QueueSeed.");
        }
        Parcel obtain = Parcel.obtain();
        try {
            this.zzpS = new String(Base64.decode(split[0], 0), DownloadManager.UTF8_CHARSET);
            this.zzAC = Integer.parseInt(split[1]);
            byte[] decode = Base64.decode(split[2], 0);
            obtain.unmarshall(decode, 0, decode.length);
            obtain.setDataPosition(0);
            this.zzqH = AdRequestParcel.CREATOR.zzb(obtain);
            obtain.recycle();
        } catch (IllegalArgumentException e) {
            throw new IOException("Malformed QueueSeed encoding.");
        } catch (Throwable th) {
            obtain.recycle();
        }
    }

    String zzem() {
        String encodeToString;
        Parcel obtain = Parcel.obtain();
        try {
            encodeToString = Base64.encodeToString(this.zzpS.getBytes(DownloadManager.UTF8_CHARSET), 0);
            String num = Integer.toString(this.zzAC);
            this.zzqH.writeToParcel(obtain, 0);
            encodeToString = encodeToString + "\u0000" + num + "\u0000" + Base64.encodeToString(obtain.marshall(), 0);
            return encodeToString;
        } catch (UnsupportedEncodingException e) {
            encodeToString = "QueueSeed encode failed because UTF-8 is not available.";
            zzb.e(encodeToString);
            return "";
        } finally {
            obtain.recycle();
        }
    }
}
