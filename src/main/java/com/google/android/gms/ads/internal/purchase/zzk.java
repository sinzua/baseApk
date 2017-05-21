package com.google.android.gms.ads.internal.purchase;

import android.content.Intent;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.internal.zzhb;

@zzhb
public class zzk {
    private final String zzuM;

    public zzk(String str) {
        this.zzuM = str;
    }

    public boolean zza(String str, int i, Intent intent) {
        if (str == null || intent == null) {
            return false;
        }
        String zze = zzr.zzbM().zze(intent);
        String zzf = zzr.zzbM().zzf(intent);
        if (zze == null || zzf == null) {
            return false;
        }
        if (!str.equals(zzr.zzbM().zzaq(zze))) {
            zzb.zzaK("Developer payload not match.");
            return false;
        } else if (this.zzuM == null || zzl.zzc(this.zzuM, zze, zzf)) {
            return true;
        } else {
            zzb.zzaK("Fail to verify signature.");
            return false;
        }
    }

    public String zzfZ() {
        return zzr.zzbC().zzhs();
    }
}
