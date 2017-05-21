package com.google.android.gms.ads.internal.overlay;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzin;

@zzhb
public class zza {
    public boolean zza(Context context, Intent intent, zzp com_google_android_gms_ads_internal_overlay_zzp) {
        try {
            zzin.v("Launching an intent: " + intent.toURI());
            zzr.zzbC().zzb(context, intent);
            if (com_google_android_gms_ads_internal_overlay_zzp != null) {
                com_google_android_gms_ads_internal_overlay_zzp.zzaO();
            }
            return true;
        } catch (ActivityNotFoundException e) {
            zzb.zzaK(e.getMessage());
            return false;
        }
    }

    public boolean zza(Context context, AdLauncherIntentInfoParcel adLauncherIntentInfoParcel, zzp com_google_android_gms_ads_internal_overlay_zzp) {
        int i = 0;
        if (adLauncherIntentInfoParcel == null) {
            zzb.zzaK("No intent data for launcher overlay.");
            return i;
        } else if (adLauncherIntentInfoParcel.intent != null) {
            return zza(context, adLauncherIntentInfoParcel.intent, com_google_android_gms_ads_internal_overlay_zzp);
        } else {
            Intent intent = new Intent();
            if (TextUtils.isEmpty(adLauncherIntentInfoParcel.url)) {
                zzb.zzaK("Open GMSG did not contain a URL.");
                return i;
            }
            if (TextUtils.isEmpty(adLauncherIntentInfoParcel.mimeType)) {
                intent.setData(Uri.parse(adLauncherIntentInfoParcel.url));
            } else {
                intent.setDataAndType(Uri.parse(adLauncherIntentInfoParcel.url), adLauncherIntentInfoParcel.mimeType);
            }
            intent.setAction("android.intent.action.VIEW");
            if (!TextUtils.isEmpty(adLauncherIntentInfoParcel.packageName)) {
                intent.setPackage(adLauncherIntentInfoParcel.packageName);
            }
            if (!TextUtils.isEmpty(adLauncherIntentInfoParcel.zzDK)) {
                String[] split = adLauncherIntentInfoParcel.zzDK.split("/", 2);
                if (split.length < 2) {
                    zzb.zzaK("Could not parse component name from open GMSG: " + adLauncherIntentInfoParcel.zzDK);
                    return i;
                }
                intent.setClassName(split[i], split[1]);
            }
            Object obj = adLauncherIntentInfoParcel.zzDL;
            if (!TextUtils.isEmpty(obj)) {
                try {
                    i = Integer.parseInt(obj);
                } catch (NumberFormatException e) {
                    zzb.zzaK("Could not parse intent flags.");
                }
                intent.addFlags(i);
            }
            return zza(context, intent, com_google_android_gms_ads_internal_overlay_zzp);
        }
    }
}
