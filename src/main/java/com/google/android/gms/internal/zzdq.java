package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.zzr;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@zzhb
public class zzdq implements Iterable<zzdp> {
    private final List<zzdp> zzzM = new LinkedList();

    private zzdp zzf(zzjp com_google_android_gms_internal_zzjp) {
        Iterator it = zzr.zzbR().iterator();
        while (it.hasNext()) {
            zzdp com_google_android_gms_internal_zzdp = (zzdp) it.next();
            if (com_google_android_gms_internal_zzdp.zzpD == com_google_android_gms_internal_zzjp) {
                return com_google_android_gms_internal_zzdp;
            }
        }
        return null;
    }

    public Iterator<zzdp> iterator() {
        return this.zzzM.iterator();
    }

    public void zza(zzdp com_google_android_gms_internal_zzdp) {
        this.zzzM.add(com_google_android_gms_internal_zzdp);
    }

    public void zzb(zzdp com_google_android_gms_internal_zzdp) {
        this.zzzM.remove(com_google_android_gms_internal_zzdp);
    }

    public boolean zzd(zzjp com_google_android_gms_internal_zzjp) {
        zzdp zzf = zzf(com_google_android_gms_internal_zzjp);
        if (zzf == null) {
            return false;
        }
        zzf.zzzJ.abort();
        return true;
    }

    public boolean zze(zzjp com_google_android_gms_internal_zzjp) {
        return zzf(com_google_android_gms_internal_zzjp) != null;
    }
}
