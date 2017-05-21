package com.google.android.gms.internal;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.security.NetworkSecurityPolicy;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.common.zze;
import com.google.android.gms.internal.zzip.zzb;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Future;

@zzhb
public class zzih implements zzb {
    private Context mContext;
    private boolean zzJA = true;
    private boolean zzJz = true;
    private final LinkedList<Thread> zzLA = new LinkedList();
    private Boolean zzLB = null;
    private boolean zzLC = false;
    private boolean zzLD = false;
    private final String zzLq;
    private final zzii zzLr;
    private BigInteger zzLs = BigInteger.ONE;
    private final HashSet<zzig> zzLt = new HashSet();
    private final HashMap<String, zzik> zzLu = new HashMap();
    private boolean zzLv = false;
    private int zzLw = 0;
    private zzbv zzLx = null;
    private zzbf zzLy = null;
    private String zzLz;
    private VersionInfoParcel zzpT;
    private final Object zzpV = new Object();
    private zzax zzpl;
    private boolean zzqA = false;
    private zzbe zzsZ = null;
    private zzbd zzta = null;
    private final zzha zztb = null;
    private String zzzN;

    public zzih(zzir com_google_android_gms_internal_zzir) {
        this.zzLq = com_google_android_gms_internal_zzir.zzhs();
        this.zzLr = new zzii(this.zzLq);
    }

    public String getSessionId() {
        return this.zzLq;
    }

    public void zzB(boolean z) {
        synchronized (this.zzpV) {
            if (this.zzJA != z) {
                zzip.zzb(this.mContext, z);
            }
            this.zzJA = z;
            zzbf zzG = zzG(this.mContext);
            if (!(zzG == null || zzG.isAlive())) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaJ("start fetching content...");
                zzG.zzcG();
            }
        }
    }

    public void zzC(boolean z) {
        synchronized (this.zzpV) {
            this.zzLC = z;
        }
    }

    public zzbf zzG(Context context) {
        if (!((Boolean) zzbt.zzwj.get()).booleanValue() || !zzne.zzsg() || zzgY()) {
            return null;
        }
        synchronized (this.zzpV) {
            if (this.zzsZ == null) {
                if (context instanceof Activity) {
                    this.zzsZ = new zzbe((Application) context.getApplicationContext(), (Activity) context);
                } else {
                    return null;
                }
            }
            if (this.zzta == null) {
                this.zzta = new zzbd();
            }
            if (this.zzLy == null) {
                this.zzLy = new zzbf(this.zzsZ, this.zzta, new zzha(this.mContext, this.zzpT, null, null));
            }
            this.zzLy.zzcG();
            zzbf com_google_android_gms_internal_zzbf = this.zzLy;
            return com_google_android_gms_internal_zzbf;
        }
    }

    public Bundle zza(Context context, zzij com_google_android_gms_internal_zzij, String str) {
        Bundle bundle;
        synchronized (this.zzpV) {
            bundle = new Bundle();
            bundle.putBundle("app", this.zzLr.zzc(context, str));
            Bundle bundle2 = new Bundle();
            for (String str2 : this.zzLu.keySet()) {
                bundle2.putBundle(str2, ((zzik) this.zzLu.get(str2)).toBundle());
            }
            bundle.putBundle("slots", bundle2);
            ArrayList arrayList = new ArrayList();
            Iterator it = this.zzLt.iterator();
            while (it.hasNext()) {
                arrayList.add(((zzig) it.next()).toBundle());
            }
            bundle.putParcelableArrayList("ads", arrayList);
            com_google_android_gms_internal_zzij.zza(this.zzLt);
            this.zzLt.clear();
        }
        return bundle;
    }

    public Future zza(Context context, boolean z) {
        Future zza;
        synchronized (this.zzpV) {
            if (z != this.zzJz) {
                this.zzJz = z;
                zza = zzip.zza(context, z);
            } else {
                zza = null;
            }
        }
        return zza;
    }

    public void zza(zzig com_google_android_gms_internal_zzig) {
        synchronized (this.zzpV) {
            this.zzLt.add(com_google_android_gms_internal_zzig);
        }
    }

    public void zza(String str, zzik com_google_android_gms_internal_zzik) {
        synchronized (this.zzpV) {
            this.zzLu.put(str, com_google_android_gms_internal_zzik);
        }
    }

    public void zza(Thread thread) {
        zzha.zza(this.mContext, thread, this.zzpT);
    }

    public Future zzaA(String str) {
        Future zzd;
        synchronized (this.zzpV) {
            if (str != null) {
                if (!str.equals(this.zzLz)) {
                    this.zzLz = str;
                    zzd = zzip.zzd(this.mContext, str);
                }
            }
            zzd = null;
        }
        return zzd;
    }

    @TargetApi(23)
    public void zzb(Context context, VersionInfoParcel versionInfoParcel) {
        synchronized (this.zzpV) {
            if (!this.zzqA) {
                this.mContext = context.getApplicationContext();
                this.zzpT = versionInfoParcel;
                zzip.zza(context, (zzb) this);
                zzip.zzb(context, (zzb) this);
                zzip.zzc(context, this);
                zzip.zzd(context, (zzb) this);
                zza(Thread.currentThread());
                this.zzzN = zzr.zzbC().zze(context, versionInfoParcel.afmaVersion);
                if (zzne.zzsn() && !NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted()) {
                    this.zzLD = true;
                }
                this.zzpl = new zzax(context.getApplicationContext(), this.zzpT, new zzeg(context.getApplicationContext(), this.zzpT, (String) zzbt.zzvB.get()));
                zzhk();
                zzr.zzbM().zzz(this.mContext);
                this.zzqA = true;
            }
        }
    }

    public void zzb(Boolean bool) {
        synchronized (this.zzpV) {
            this.zzLB = bool;
        }
    }

    public void zzb(Throwable th, boolean z) {
        new zzha(this.mContext, this.zzpT, null, null).zza(th, z);
    }

    public void zzb(HashSet<zzig> hashSet) {
        synchronized (this.zzpV) {
            this.zzLt.addAll(hashSet);
        }
    }

    public String zzd(int i, String str) {
        Resources resources = this.zzpT.zzNb ? this.mContext.getResources() : zze.getRemoteResource(this.mContext);
        return resources == null ? str : resources.getString(i);
    }

    public void zze(Bundle bundle) {
        synchronized (this.zzpV) {
            this.zzJz = bundle.containsKey("use_https") ? bundle.getBoolean("use_https") : this.zzJz;
            this.zzLw = bundle.containsKey("webview_cache_version") ? bundle.getInt("webview_cache_version") : this.zzLw;
            if (bundle.containsKey("content_url_opted_out")) {
                zzB(bundle.getBoolean("content_url_opted_out"));
            }
            if (bundle.containsKey("content_url_hashes")) {
                this.zzLz = bundle.getString("content_url_hashes");
            }
        }
    }

    public boolean zzgY() {
        boolean z;
        synchronized (this.zzpV) {
            z = this.zzJA;
        }
        return z;
    }

    public String zzgZ() {
        String bigInteger;
        synchronized (this.zzpV) {
            bigInteger = this.zzLs.toString();
            this.zzLs = this.zzLs.add(BigInteger.ONE);
        }
        return bigInteger;
    }

    public zzii zzha() {
        zzii com_google_android_gms_internal_zzii;
        synchronized (this.zzpV) {
            com_google_android_gms_internal_zzii = this.zzLr;
        }
        return com_google_android_gms_internal_zzii;
    }

    public zzbv zzhb() {
        zzbv com_google_android_gms_internal_zzbv;
        synchronized (this.zzpV) {
            com_google_android_gms_internal_zzbv = this.zzLx;
        }
        return com_google_android_gms_internal_zzbv;
    }

    public boolean zzhc() {
        boolean z;
        synchronized (this.zzpV) {
            z = this.zzLv;
            this.zzLv = true;
        }
        return z;
    }

    public boolean zzhd() {
        boolean z;
        synchronized (this.zzpV) {
            z = this.zzJz || this.zzLD;
        }
        return z;
    }

    public String zzhe() {
        String str;
        synchronized (this.zzpV) {
            str = this.zzzN;
        }
        return str;
    }

    public String zzhf() {
        String str;
        synchronized (this.zzpV) {
            str = this.zzLz;
        }
        return str;
    }

    public Boolean zzhg() {
        Boolean bool;
        synchronized (this.zzpV) {
            bool = this.zzLB;
        }
        return bool;
    }

    public zzax zzhh() {
        return this.zzpl;
    }

    public boolean zzhi() {
        boolean z;
        synchronized (this.zzpV) {
            if (this.zzLw < ((Integer) zzbt.zzwA.get()).intValue()) {
                this.zzLw = ((Integer) zzbt.zzwA.get()).intValue();
                zzip.zza(this.mContext, this.zzLw);
                z = true;
            } else {
                z = false;
            }
        }
        return z;
    }

    public boolean zzhj() {
        boolean z;
        synchronized (this.zzpV) {
            z = this.zzLC;
        }
        return z;
    }

    void zzhk() {
        try {
            this.zzLx = zzr.zzbH().zza(new zzbu(this.mContext, this.zzpT.afmaVersion));
        } catch (Throwable e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Cannot initialize CSI reporter.", e);
        }
    }
}
