package com.google.android.gms.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@zzhb
public class zzbq {
    private final Collection<zzbp> zzvu = new ArrayList();
    private final Collection<zzbp<String>> zzvv = new ArrayList();
    private final Collection<zzbp<String>> zzvw = new ArrayList();

    public void zza(zzbp com_google_android_gms_internal_zzbp) {
        this.zzvu.add(com_google_android_gms_internal_zzbp);
    }

    public void zzb(zzbp<String> com_google_android_gms_internal_zzbp_java_lang_String) {
        this.zzvv.add(com_google_android_gms_internal_zzbp_java_lang_String);
    }

    public void zzc(zzbp<String> com_google_android_gms_internal_zzbp_java_lang_String) {
        this.zzvw.add(com_google_android_gms_internal_zzbp_java_lang_String);
    }

    public List<String> zzdr() {
        List<String> arrayList = new ArrayList();
        for (zzbp com_google_android_gms_internal_zzbp : this.zzvv) {
            String str = (String) com_google_android_gms_internal_zzbp.get();
            if (str != null) {
                arrayList.add(str);
            }
        }
        return arrayList;
    }

    public List<String> zzds() {
        List<String> zzdr = zzdr();
        for (zzbp com_google_android_gms_internal_zzbp : this.zzvw) {
            String str = (String) com_google_android_gms_internal_zzbp.get();
            if (str != null) {
                zzdr.add(str);
            }
        }
        return zzdr;
    }
}
