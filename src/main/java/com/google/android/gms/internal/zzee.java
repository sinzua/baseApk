package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import java.util.concurrent.Future;

@zzhb
public class zzee {

    private static class zza<JavascriptEngine> extends zzjd<JavascriptEngine> {
        JavascriptEngine zzAR;

        private zza() {
        }
    }

    private zzed zza(Context context, VersionInfoParcel versionInfoParcel, final zza<zzed> com_google_android_gms_internal_zzee_zza_com_google_android_gms_internal_zzed, zzan com_google_android_gms_internal_zzan) {
        zzed com_google_android_gms_internal_zzef = new zzef(context, versionInfoParcel, com_google_android_gms_internal_zzan);
        com_google_android_gms_internal_zzee_zza_com_google_android_gms_internal_zzed.zzAR = com_google_android_gms_internal_zzef;
        com_google_android_gms_internal_zzef.zza(new com.google.android.gms.internal.zzed.zza(this) {
            final /* synthetic */ zzee zzAQ;

            public void zzeo() {
                com_google_android_gms_internal_zzee_zza_com_google_android_gms_internal_zzed.zzg(com_google_android_gms_internal_zzee_zza_com_google_android_gms_internal_zzed.zzAR);
            }
        });
        return com_google_android_gms_internal_zzef;
    }

    public Future<zzed> zza(Context context, VersionInfoParcel versionInfoParcel, String str, zzan com_google_android_gms_internal_zzan) {
        final Future com_google_android_gms_internal_zzee_zza = new zza();
        final Context context2 = context;
        final VersionInfoParcel versionInfoParcel2 = versionInfoParcel;
        final zzan com_google_android_gms_internal_zzan2 = com_google_android_gms_internal_zzan;
        final String str2 = str;
        zzir.zzMc.post(new Runnable(this) {
            final /* synthetic */ zzee zzAQ;

            public void run() {
                this.zzAQ.zza(context2, versionInfoParcel2, com_google_android_gms_internal_zzee_zza, com_google_android_gms_internal_zzan2).zzaa(str2);
            }
        });
        return com_google_android_gms_internal_zzee_zza;
    }
}
