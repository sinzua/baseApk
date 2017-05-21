package com.google.android.gms.internal;

import android.content.Context;
import android.view.View;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.formats.zzh;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.internal.zzau.zza;
import com.google.android.gms.internal.zzau.zzd;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.WeakHashMap;

@zzhb
public class zzax implements zzay {
    private final VersionInfoParcel zzpT;
    private final Object zzpV = new Object();
    private final WeakHashMap<zzif, zzau> zzsB = new WeakHashMap();
    private final ArrayList<zzau> zzsC = new ArrayList();
    private final zzeg zzsD;
    private final Context zzsa;

    public zzax(Context context, VersionInfoParcel versionInfoParcel, zzeg com_google_android_gms_internal_zzeg) {
        this.zzsa = context.getApplicationContext();
        this.zzpT = versionInfoParcel;
        this.zzsD = com_google_android_gms_internal_zzeg;
    }

    public zzau zza(AdSizeParcel adSizeParcel, zzif com_google_android_gms_internal_zzif) {
        return zza(adSizeParcel, com_google_android_gms_internal_zzif, com_google_android_gms_internal_zzif.zzED.getView());
    }

    public zzau zza(AdSizeParcel adSizeParcel, zzif com_google_android_gms_internal_zzif, View view) {
        return zza(adSizeParcel, com_google_android_gms_internal_zzif, new zzd(view, com_google_android_gms_internal_zzif), null);
    }

    public zzau zza(AdSizeParcel adSizeParcel, zzif com_google_android_gms_internal_zzif, View view, zzeh com_google_android_gms_internal_zzeh) {
        return zza(adSizeParcel, com_google_android_gms_internal_zzif, new zzd(view, com_google_android_gms_internal_zzif), com_google_android_gms_internal_zzeh);
    }

    public zzau zza(AdSizeParcel adSizeParcel, zzif com_google_android_gms_internal_zzif, zzh com_google_android_gms_ads_internal_formats_zzh) {
        return zza(adSizeParcel, com_google_android_gms_internal_zzif, new zza(com_google_android_gms_ads_internal_formats_zzh), null);
    }

    public zzau zza(AdSizeParcel adSizeParcel, zzif com_google_android_gms_internal_zzif, zzbb com_google_android_gms_internal_zzbb, zzeh com_google_android_gms_internal_zzeh) {
        zzau com_google_android_gms_internal_zzau;
        synchronized (this.zzpV) {
            if (zzh(com_google_android_gms_internal_zzif)) {
                com_google_android_gms_internal_zzau = (zzau) this.zzsB.get(com_google_android_gms_internal_zzif);
            } else {
                if (com_google_android_gms_internal_zzeh != null) {
                    com_google_android_gms_internal_zzau = new zzaz(this.zzsa, adSizeParcel, com_google_android_gms_internal_zzif, this.zzpT, com_google_android_gms_internal_zzbb, com_google_android_gms_internal_zzeh);
                } else {
                    com_google_android_gms_internal_zzau = new zzba(this.zzsa, adSizeParcel, com_google_android_gms_internal_zzif, this.zzpT, com_google_android_gms_internal_zzbb, this.zzsD);
                }
                com_google_android_gms_internal_zzau.zza((zzay) this);
                this.zzsB.put(com_google_android_gms_internal_zzif, com_google_android_gms_internal_zzau);
                this.zzsC.add(com_google_android_gms_internal_zzau);
            }
        }
        return com_google_android_gms_internal_zzau;
    }

    public void zza(zzau com_google_android_gms_internal_zzau) {
        synchronized (this.zzpV) {
            if (!com_google_android_gms_internal_zzau.zzch()) {
                this.zzsC.remove(com_google_android_gms_internal_zzau);
                Iterator it = this.zzsB.entrySet().iterator();
                while (it.hasNext()) {
                    if (((Entry) it.next()).getValue() == com_google_android_gms_internal_zzau) {
                        it.remove();
                    }
                }
            }
        }
    }

    public boolean zzh(zzif com_google_android_gms_internal_zzif) {
        boolean z;
        synchronized (this.zzpV) {
            zzau com_google_android_gms_internal_zzau = (zzau) this.zzsB.get(com_google_android_gms_internal_zzif);
            z = com_google_android_gms_internal_zzau != null && com_google_android_gms_internal_zzau.zzch();
        }
        return z;
    }

    public void zzi(zzif com_google_android_gms_internal_zzif) {
        synchronized (this.zzpV) {
            zzau com_google_android_gms_internal_zzau = (zzau) this.zzsB.get(com_google_android_gms_internal_zzif);
            if (com_google_android_gms_internal_zzau != null) {
                com_google_android_gms_internal_zzau.zzcf();
            }
        }
    }

    public void zzj(zzif com_google_android_gms_internal_zzif) {
        synchronized (this.zzpV) {
            zzau com_google_android_gms_internal_zzau = (zzau) this.zzsB.get(com_google_android_gms_internal_zzif);
            if (com_google_android_gms_internal_zzau != null) {
                com_google_android_gms_internal_zzau.stop();
            }
        }
    }

    public void zzk(zzif com_google_android_gms_internal_zzif) {
        synchronized (this.zzpV) {
            zzau com_google_android_gms_internal_zzau = (zzau) this.zzsB.get(com_google_android_gms_internal_zzif);
            if (com_google_android_gms_internal_zzau != null) {
                com_google_android_gms_internal_zzau.pause();
            }
        }
    }

    public void zzl(zzif com_google_android_gms_internal_zzif) {
        synchronized (this.zzpV) {
            zzau com_google_android_gms_internal_zzau = (zzau) this.zzsB.get(com_google_android_gms_internal_zzif);
            if (com_google_android_gms_internal_zzau != null) {
                com_google_android_gms_internal_zzau.resume();
            }
        }
    }
}
