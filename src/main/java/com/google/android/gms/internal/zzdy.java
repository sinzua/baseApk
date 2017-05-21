package com.google.android.gms.internal;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.internal.zzhj.zza;
import com.supersonicads.sdk.precache.DownloadManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

@zzhb
public class zzdy {
    private final Map<zzdz, zzea> zzAx = new HashMap();
    private final LinkedList<zzdz> zzAy = new LinkedList();
    private zzdv zzAz;

    private String[] zzY(String str) {
        try {
            String[] split = str.split("\u0000");
            for (int i = 0; i < split.length; i++) {
                split[i] = new String(Base64.decode(split[i], 0), DownloadManager.UTF8_CHARSET);
            }
            return split;
        } catch (UnsupportedEncodingException e) {
            return new String[0];
        }
    }

    private static void zza(String str, zzdz com_google_android_gms_internal_zzdz) {
        if (zzb.zzQ(2)) {
            zzin.v(String.format(str, new Object[]{com_google_android_gms_internal_zzdz}));
        }
    }

    private String zzef() {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            Iterator it = this.zzAy.iterator();
            while (it.hasNext()) {
                stringBuilder.append(Base64.encodeToString(((zzdz) it.next()).toString().getBytes(DownloadManager.UTF8_CHARSET), 0));
                if (it.hasNext()) {
                    stringBuilder.append("\u0000");
                }
            }
            return stringBuilder.toString();
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    void flush() {
        while (this.zzAy.size() > 0) {
            zzdz com_google_android_gms_internal_zzdz = (zzdz) this.zzAy.remove();
            zzea com_google_android_gms_internal_zzea = (zzea) this.zzAx.get(com_google_android_gms_internal_zzdz);
            zza("Flushing interstitial queue for %s.", com_google_android_gms_internal_zzdz);
            while (com_google_android_gms_internal_zzea.size() > 0) {
                com_google_android_gms_internal_zzea.zzej().zzAD.zzbp();
            }
            this.zzAx.remove(com_google_android_gms_internal_zzdz);
        }
    }

    void restore() {
        Throwable e;
        if (this.zzAz != null) {
            zzdz com_google_android_gms_internal_zzdz;
            SharedPreferences sharedPreferences = this.zzAz.zzed().getSharedPreferences("com.google.android.gms.ads.internal.interstitial.InterstitialAdPool", 0);
            flush();
            Map hashMap = new HashMap();
            for (Entry entry : sharedPreferences.getAll().entrySet()) {
                try {
                    if (!((String) entry.getKey()).equals("PoolKeys")) {
                        zzec com_google_android_gms_internal_zzec = new zzec((String) entry.getValue());
                        com_google_android_gms_internal_zzdz = new zzdz(com_google_android_gms_internal_zzec.zzqH, com_google_android_gms_internal_zzec.zzpS, com_google_android_gms_internal_zzec.zzAC);
                        if (!this.zzAx.containsKey(com_google_android_gms_internal_zzdz)) {
                            this.zzAx.put(com_google_android_gms_internal_zzdz, new zzea(com_google_android_gms_internal_zzec.zzqH, com_google_android_gms_internal_zzec.zzpS, com_google_android_gms_internal_zzec.zzAC));
                            hashMap.put(com_google_android_gms_internal_zzdz.toString(), com_google_android_gms_internal_zzdz);
                            zza("Restored interstitial queue for %s.", com_google_android_gms_internal_zzdz);
                        }
                    }
                } catch (IOException e2) {
                    e = e2;
                    zzb.zzd("Malformed preferences value for InterstitialAdPool.", e);
                } catch (ClassCastException e3) {
                    e = e3;
                    zzb.zzd("Malformed preferences value for InterstitialAdPool.", e);
                }
            }
            for (Object obj : zzY(sharedPreferences.getString("PoolKeys", ""))) {
                com_google_android_gms_internal_zzdz = (zzdz) hashMap.get(obj);
                if (this.zzAx.containsKey(com_google_android_gms_internal_zzdz)) {
                    this.zzAy.add(com_google_android_gms_internal_zzdz);
                }
            }
        }
    }

    void save() {
        if (this.zzAz != null) {
            Editor edit = this.zzAz.zzed().getSharedPreferences("com.google.android.gms.ads.internal.interstitial.InterstitialAdPool", 0).edit();
            edit.clear();
            for (Entry entry : this.zzAx.entrySet()) {
                zzdz com_google_android_gms_internal_zzdz = (zzdz) entry.getKey();
                if (com_google_android_gms_internal_zzdz.zzeh()) {
                    edit.putString(com_google_android_gms_internal_zzdz.toString(), new zzec((zzea) entry.getValue()).zzem());
                    zza("Saved interstitial queue for %s.", com_google_android_gms_internal_zzdz);
                }
            }
            edit.putString("PoolKeys", zzef());
            edit.commit();
        }
    }

    zza zza(AdRequestParcel adRequestParcel, String str) {
        zzea com_google_android_gms_internal_zzea;
        int i = new zza(this.zzAz.zzed()).zzgI().zzKc;
        zzdz com_google_android_gms_internal_zzdz = new zzdz(adRequestParcel, str, i);
        zzea com_google_android_gms_internal_zzea2 = (zzea) this.zzAx.get(com_google_android_gms_internal_zzdz);
        if (com_google_android_gms_internal_zzea2 == null) {
            zza("Interstitial pool created at %s.", com_google_android_gms_internal_zzdz);
            com_google_android_gms_internal_zzea2 = new zzea(adRequestParcel, str, i);
            this.zzAx.put(com_google_android_gms_internal_zzdz, com_google_android_gms_internal_zzea2);
            com_google_android_gms_internal_zzea = com_google_android_gms_internal_zzea2;
        } else {
            com_google_android_gms_internal_zzea = com_google_android_gms_internal_zzea2;
        }
        this.zzAy.remove(com_google_android_gms_internal_zzdz);
        this.zzAy.add(com_google_android_gms_internal_zzdz);
        com_google_android_gms_internal_zzdz.zzeg();
        while (this.zzAy.size() > ((Integer) zzbt.zzwG.get()).intValue()) {
            zzdz com_google_android_gms_internal_zzdz2 = (zzdz) this.zzAy.remove();
            zzea com_google_android_gms_internal_zzea3 = (zzea) this.zzAx.get(com_google_android_gms_internal_zzdz2);
            zza("Evicting interstitial queue for %s.", com_google_android_gms_internal_zzdz2);
            while (com_google_android_gms_internal_zzea3.size() > 0) {
                com_google_android_gms_internal_zzea3.zzej().zzAD.zzbp();
            }
            this.zzAx.remove(com_google_android_gms_internal_zzdz2);
        }
        while (com_google_android_gms_internal_zzea.size() > 0) {
            zza zzej = com_google_android_gms_internal_zzea.zzej();
            if (!zzej.zzAG || zzr.zzbG().currentTimeMillis() - zzej.zzAF <= 1000 * ((long) ((Integer) zzbt.zzwI.get()).intValue())) {
                zza("Pooled interstitial returned at %s.", com_google_android_gms_internal_zzdz);
                return zzej;
            }
            zza("Expired interstitial at %s.", com_google_android_gms_internal_zzdz);
        }
        return null;
    }

    void zza(zzdv com_google_android_gms_internal_zzdv) {
        if (this.zzAz == null) {
            this.zzAz = com_google_android_gms_internal_zzdv;
            restore();
        }
    }

    void zzee() {
        if (this.zzAz != null) {
            for (Entry entry : this.zzAx.entrySet()) {
                zzdz com_google_android_gms_internal_zzdz = (zzdz) entry.getKey();
                zzea com_google_android_gms_internal_zzea = (zzea) entry.getValue();
                while (com_google_android_gms_internal_zzea.size() < ((Integer) zzbt.zzwH.get()).intValue()) {
                    zza("Pooling one interstitial for %s.", com_google_android_gms_internal_zzdz);
                    com_google_android_gms_internal_zzea.zzb(this.zzAz);
                }
            }
            save();
        }
    }
}
