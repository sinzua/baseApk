package com.google.android.gms.ads.internal;

import android.net.Uri.Builder;
import android.text.TextUtils;
import com.google.android.gms.internal.zzbt;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzjp;
import com.nativex.monetization.mraid.objects.ObjectNames.CalendarEntryData;

@zzhb
public class zze {
    private boolean zzpA;
    private boolean zzpB;
    private zza zzpz;

    public interface zza {
        void zzr(String str);
    }

    @zzhb
    public static class zzb implements zza {
        private final com.google.android.gms.internal.zzif.zza zzpC;
        private final zzjp zzpD;

        public zzb(com.google.android.gms.internal.zzif.zza com_google_android_gms_internal_zzif_zza, zzjp com_google_android_gms_internal_zzjp) {
            this.zzpC = com_google_android_gms_internal_zzif_zza;
            this.zzpD = com_google_android_gms_internal_zzjp;
        }

        public void zzr(String str) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("An auto-clicking creative is blocked");
            Builder builder = new Builder();
            builder.scheme("https");
            builder.path("//pagead2.googlesyndication.com/pagead/gen_204");
            builder.appendQueryParameter(CalendarEntryData.ID, "gmob-apps-blocked-navigation");
            if (!TextUtils.isEmpty(str)) {
                builder.appendQueryParameter("navigationURL", str);
            }
            if (!(this.zzpC == null || this.zzpC.zzLe == null || TextUtils.isEmpty(this.zzpC.zzLe.zzHY))) {
                builder.appendQueryParameter("debugDialog", this.zzpC.zzLe.zzHY);
            }
            zzr.zzbC().zzc(this.zzpD.getContext(), this.zzpD.zzhX().afmaVersion, builder.toString());
        }
    }

    public zze() {
        this.zzpB = ((Boolean) zzbt.zzvI.get()).booleanValue();
    }

    public zze(boolean z) {
        this.zzpB = z;
    }

    public void recordClick() {
        this.zzpA = true;
    }

    public void zza(zza com_google_android_gms_ads_internal_zze_zza) {
        this.zzpz = com_google_android_gms_ads_internal_zze_zza;
    }

    public boolean zzbh() {
        return !this.zzpB || this.zzpA;
    }

    public void zzq(String str) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaI("Action was blocked because no click was detected.");
        if (this.zzpz != null) {
            this.zzpz.zzr(str);
        }
    }
}
