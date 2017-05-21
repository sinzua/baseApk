package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzeg.zzd;
import java.util.Map;
import java.util.concurrent.Future;

@zzhb
public final class zzhf {
    private String zzEY;
    private String zzJh;
    private zzjd<zzhi> zzJi = new zzjd();
    zzd zzJj;
    public final zzdf zzJk = new zzdf(this) {
        final /* synthetic */ zzhf zzJm;

        {
            this.zzJm = r1;
        }

        public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
            synchronized (this.zzJm.zzpV) {
                if (this.zzJm.zzJi.isDone()) {
                } else if (this.zzJm.zzEY.equals(map.get("request_id"))) {
                    zzhi com_google_android_gms_internal_zzhi = new zzhi(1, map);
                    zzb.zzaK("Invalid " + com_google_android_gms_internal_zzhi.getType() + " request error: " + com_google_android_gms_internal_zzhi.zzgE());
                    this.zzJm.zzJi.zzg(com_google_android_gms_internal_zzhi);
                }
            }
        }
    };
    public final zzdf zzJl = new zzdf(this) {
        final /* synthetic */ zzhf zzJm;

        {
            this.zzJm = r1;
        }

        public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
            synchronized (this.zzJm.zzpV) {
                if (this.zzJm.zzJi.isDone()) {
                    return;
                }
                zzhi com_google_android_gms_internal_zzhi = new zzhi(-2, map);
                if (this.zzJm.zzEY.equals(com_google_android_gms_internal_zzhi.getRequestId())) {
                    String url = com_google_android_gms_internal_zzhi.getUrl();
                    if (url == null) {
                        zzb.zzaK("URL missing in loadAdUrl GMSG.");
                        return;
                    }
                    if (url.contains("%40mediation_adapters%40")) {
                        String replaceAll = url.replaceAll("%40mediation_adapters%40", zzil.zza(com_google_android_gms_internal_zzjp.getContext(), (String) map.get("check_adapters"), this.zzJm.zzJh));
                        com_google_android_gms_internal_zzhi.setUrl(replaceAll);
                        zzin.v("Ad request URL modified to " + replaceAll);
                    }
                    this.zzJm.zzJi.zzg(com_google_android_gms_internal_zzhi);
                    return;
                }
                zzb.zzaK(com_google_android_gms_internal_zzhi.getRequestId() + " ==== " + this.zzJm.zzEY);
            }
        }
    };
    zzjp zzpD;
    private final Object zzpV = new Object();

    public zzhf(String str, String str2) {
        this.zzJh = str2;
        this.zzEY = str;
    }

    public void zzb(zzd com_google_android_gms_internal_zzeg_zzd) {
        this.zzJj = com_google_android_gms_internal_zzeg_zzd;
    }

    public zzd zzgB() {
        return this.zzJj;
    }

    public Future<zzhi> zzgC() {
        return this.zzJi;
    }

    public void zzgD() {
        if (this.zzpD != null) {
            this.zzpD.destroy();
            this.zzpD = null;
        }
    }

    public void zzh(zzjp com_google_android_gms_internal_zzjp) {
        this.zzpD = com_google_android_gms_internal_zzjp;
    }
}
