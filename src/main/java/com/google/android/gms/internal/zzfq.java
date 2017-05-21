package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import org.json.JSONObject;

@zzhb
public class zzfq {
    private final boolean zzDu;
    private final boolean zzDv;
    private final boolean zzDw;
    private final boolean zzDx;
    private final boolean zzDy;

    public static final class zza {
        private boolean zzDu;
        private boolean zzDv;
        private boolean zzDw;
        private boolean zzDx;
        private boolean zzDy;

        public zzfq zzeP() {
            return new zzfq();
        }

        public zza zzq(boolean z) {
            this.zzDu = z;
            return this;
        }

        public zza zzr(boolean z) {
            this.zzDv = z;
            return this;
        }

        public zza zzs(boolean z) {
            this.zzDw = z;
            return this;
        }

        public zza zzt(boolean z) {
            this.zzDx = z;
            return this;
        }

        public zza zzu(boolean z) {
            this.zzDy = z;
            return this;
        }
    }

    private zzfq(zza com_google_android_gms_internal_zzfq_zza) {
        this.zzDu = com_google_android_gms_internal_zzfq_zza.zzDu;
        this.zzDv = com_google_android_gms_internal_zzfq_zza.zzDv;
        this.zzDw = com_google_android_gms_internal_zzfq_zza.zzDw;
        this.zzDx = com_google_android_gms_internal_zzfq_zza.zzDx;
        this.zzDy = com_google_android_gms_internal_zzfq_zza.zzDy;
    }

    public JSONObject toJson() {
        try {
            return new JSONObject().put("sms", this.zzDu).put("tel", this.zzDv).put("calendar", this.zzDw).put("storePicture", this.zzDx).put("inlineVideo", this.zzDy);
        } catch (Throwable e) {
            zzb.zzb("Error occured while obtaining the MRAID capabilities.", e);
            return null;
        }
    }
}
