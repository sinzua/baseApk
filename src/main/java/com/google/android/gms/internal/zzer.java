package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import com.google.ads.mediation.AdUrlAdapter;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAdOptions.Builder;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzes.zza;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import com.ty.followboom.helpers.VLTools;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
public final class zzer implements zza {
    private final Context mContext;
    private final String zzCd;
    private final long zzCe;
    private final zzeo zzCf;
    private final zzen zzCg;
    private final AdSizeParcel zzCh;
    private zzey zzCi;
    private int zzCj = -2;
    private zzfa zzCk;
    private final NativeAdOptionsParcel zzpP;
    private final List<String> zzpQ;
    private final VersionInfoParcel zzpT;
    private final Object zzpV = new Object();
    private final zzex zzpn;
    private final AdRequestParcel zzqH;
    private final boolean zzsA;
    private final boolean zzuS;

    public zzer(Context context, String str, zzex com_google_android_gms_internal_zzex, zzeo com_google_android_gms_internal_zzeo, zzen com_google_android_gms_internal_zzen, AdRequestParcel adRequestParcel, AdSizeParcel adSizeParcel, VersionInfoParcel versionInfoParcel, boolean z, boolean z2, NativeAdOptionsParcel nativeAdOptionsParcel, List<String> list) {
        this.mContext = context;
        this.zzpn = com_google_android_gms_internal_zzex;
        this.zzCg = com_google_android_gms_internal_zzen;
        if ("com.google.ads.mediation.customevent.CustomEventAdapter".equals(str)) {
            this.zzCd = zzey();
        } else {
            this.zzCd = str;
        }
        this.zzCf = com_google_android_gms_internal_zzeo;
        this.zzCe = com_google_android_gms_internal_zzeo.zzBP != -1 ? com_google_android_gms_internal_zzeo.zzBP : VLTools.DEFAULT_RATE_US_THREHOLD;
        this.zzqH = adRequestParcel;
        this.zzCh = adSizeParcel;
        this.zzpT = versionInfoParcel;
        this.zzsA = z;
        this.zzuS = z2;
        this.zzpP = nativeAdOptionsParcel;
        this.zzpQ = list;
    }

    private void zza(long j, long j2, long j3, long j4) {
        while (this.zzCj == -2) {
            zzb(j, j2, j3, j4);
        }
    }

    private void zza(zzeq com_google_android_gms_internal_zzeq) {
        if ("com.google.ads.mediation.AdUrlAdapter".equals(this.zzCd)) {
            Bundle bundle = this.zzqH.zztM.getBundle(this.zzCd);
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putString("sdk_less_network_id", this.zzCg.zzBA);
            this.zzqH.zztM.putBundle(this.zzCd, bundle);
        }
        String zzac = zzac(this.zzCg.zzBG);
        try {
            if (this.zzpT.zzNa < 4100000) {
                if (this.zzCh.zzui) {
                    this.zzCi.zza(zze.zzC(this.mContext), this.zzqH, zzac, com_google_android_gms_internal_zzeq);
                } else {
                    this.zzCi.zza(zze.zzC(this.mContext), this.zzCh, this.zzqH, zzac, (zzez) com_google_android_gms_internal_zzeq);
                }
            } else if (this.zzsA) {
                this.zzCi.zza(zze.zzC(this.mContext), this.zzqH, zzac, this.zzCg.zzBz, com_google_android_gms_internal_zzeq, this.zzpP, this.zzpQ);
            } else if (this.zzCh.zzui) {
                this.zzCi.zza(zze.zzC(this.mContext), this.zzqH, zzac, this.zzCg.zzBz, (zzez) com_google_android_gms_internal_zzeq);
            } else if (!this.zzuS) {
                this.zzCi.zza(zze.zzC(this.mContext), this.zzCh, this.zzqH, zzac, this.zzCg.zzBz, com_google_android_gms_internal_zzeq);
            } else if (this.zzCg.zzBJ != null) {
                this.zzCi.zza(zze.zzC(this.mContext), this.zzqH, zzac, this.zzCg.zzBz, com_google_android_gms_internal_zzeq, new NativeAdOptionsParcel(zzad(this.zzCg.zzBN)), this.zzCg.zzBM);
            } else {
                this.zzCi.zza(zze.zzC(this.mContext), this.zzCh, this.zzqH, zzac, this.zzCg.zzBz, com_google_android_gms_internal_zzeq);
            }
        } catch (Throwable e) {
            zzb.zzd("Could not request ad from mediation adapter.", e);
            zzr(5);
        }
    }

    private String zzac(String str) {
        if (!(str == null || !zzeB() || zzs(2))) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                jSONObject.remove("cpm_floor_cents");
                str = jSONObject.toString();
            } catch (JSONException e) {
                zzb.zzaK("Could not remove field. Returning the original value");
            }
        }
        return str;
    }

    private static NativeAdOptions zzad(String str) {
        Builder builder = new Builder();
        if (str == null) {
            return builder.build();
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            builder.setRequestMultipleImages(jSONObject.optBoolean("multiple_images", false));
            builder.setReturnUrlsForImageAssets(jSONObject.optBoolean("only_urls", false));
            builder.setImageOrientation(zzae(jSONObject.optString("native_image_orientation", "any")));
        } catch (Throwable e) {
            zzb.zzd("Exception occurred when creating native ad options", e);
        }
        return builder.build();
    }

    private static int zzae(String str) {
        return ParametersKeys.ORIENTATION_LANDSCAPE.equals(str) ? 2 : ParametersKeys.ORIENTATION_PORTRAIT.equals(str) ? 1 : 0;
    }

    private void zzb(long j, long j2, long j3, long j4) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j5 = j2 - (elapsedRealtime - j);
        elapsedRealtime = j4 - (elapsedRealtime - j3);
        if (j5 <= 0 || elapsedRealtime <= 0) {
            zzb.zzaJ("Timed out waiting for adapter.");
            this.zzCj = 3;
            return;
        }
        try {
            this.zzpV.wait(Math.min(j5, elapsedRealtime));
        } catch (InterruptedException e) {
            this.zzCj = -1;
        }
    }

    private zzey zzeA() {
        zzb.zzaJ("Instantiating mediation adapter: " + this.zzCd);
        if (((Boolean) zzbt.zzwV.get()).booleanValue() && "com.google.ads.mediation.admob.AdMobAdapter".equals(this.zzCd)) {
            return new zzfe(new AdMobAdapter());
        }
        if (((Boolean) zzbt.zzwW.get()).booleanValue() && "com.google.ads.mediation.AdUrlAdapter".equals(this.zzCd)) {
            return new zzfe(new AdUrlAdapter());
        }
        try {
            return this.zzpn.zzaf(this.zzCd);
        } catch (Throwable e) {
            zzb.zza("Could not instantiate mediation adapter: " + this.zzCd, e);
            return null;
        }
    }

    private boolean zzeB() {
        return this.zzCf.zzBX != -1;
    }

    private int zzeC() {
        if (this.zzCg.zzBG == null) {
            return 0;
        }
        try {
            JSONObject jSONObject = new JSONObject(this.zzCg.zzBG);
            if ("com.google.ads.mediation.admob.AdMobAdapter".equals(this.zzCd)) {
                return jSONObject.optInt("cpm_cents", 0);
            }
            int optInt = zzs(2) ? jSONObject.optInt("cpm_floor_cents", 0) : 0;
            return optInt == 0 ? jSONObject.optInt("penalized_average_cpm_cents", 0) : optInt;
        } catch (JSONException e) {
            zzb.zzaK("Could not convert to json. Returning 0");
            return 0;
        }
    }

    private String zzey() {
        try {
            if (!TextUtils.isEmpty(this.zzCg.zzBD)) {
                return this.zzpn.zzag(this.zzCg.zzBD) ? "com.google.android.gms.ads.mediation.customevent.CustomEventAdapter" : "com.google.ads.mediation.customevent.CustomEventAdapter";
            }
        } catch (RemoteException e) {
            zzb.zzaK("Fail to determine the custom event's version, assuming the old one.");
        }
        return "com.google.ads.mediation.customevent.CustomEventAdapter";
    }

    private zzfa zzez() {
        if (this.zzCj != 0 || !zzeB()) {
            return null;
        }
        try {
            if (!(!zzs(4) || this.zzCk == null || this.zzCk.zzeD() == 0)) {
                return this.zzCk;
            }
        } catch (RemoteException e) {
            zzb.zzaK("Could not get cpm value from MediationResponseMetadata");
        }
        return zzt(zzeC());
    }

    private boolean zzs(int i) {
        try {
            Bundle zzeI = this.zzsA ? this.zzCi.zzeI() : this.zzCh.zzui ? this.zzCi.getInterstitialAdapterInfo() : this.zzCi.zzeH();
            if (zzeI == null) {
                return false;
            }
            return (zzeI.getInt("capabilities", 0) & i) == i;
        } catch (RemoteException e) {
            zzb.zzaK("Could not get adapter info. Returning false");
            return false;
        }
    }

    private static zzfa zzt(final int i) {
        return new zzfa.zza() {
            public int zzeD() throws RemoteException {
                return i;
            }
        };
    }

    public void cancel() {
        synchronized (this.zzpV) {
            try {
                if (this.zzCi != null) {
                    this.zzCi.destroy();
                }
            } catch (Throwable e) {
                zzb.zzd("Could not destroy mediation adapter.", e);
            }
            this.zzCj = -1;
            this.zzpV.notify();
        }
    }

    public zzes zza(long j, long j2) {
        zzes com_google_android_gms_internal_zzes;
        synchronized (this.zzpV) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            final zzeq com_google_android_gms_internal_zzeq = new zzeq();
            zzir.zzMc.post(new Runnable(this) {
                final /* synthetic */ zzer zzCm;

                public void run() {
                    synchronized (this.zzCm.zzpV) {
                        if (this.zzCm.zzCj != -2) {
                            return;
                        }
                        this.zzCm.zzCi = this.zzCm.zzeA();
                        if (this.zzCm.zzCi == null) {
                            this.zzCm.zzr(4);
                        } else if (!this.zzCm.zzeB() || this.zzCm.zzs(1)) {
                            com_google_android_gms_internal_zzeq.zza(this.zzCm);
                            this.zzCm.zza(com_google_android_gms_internal_zzeq);
                        } else {
                            zzb.zzaK("Ignoring adapter " + this.zzCm.zzCd + " as delayed" + " impression is not supported");
                            this.zzCm.zzr(2);
                        }
                    }
                }
            });
            zza(elapsedRealtime, this.zzCe, j, j2);
            com_google_android_gms_internal_zzes = new zzes(this.zzCg, this.zzCi, this.zzCd, com_google_android_gms_internal_zzeq, this.zzCj, zzez());
        }
        return com_google_android_gms_internal_zzes;
    }

    public void zza(int i, zzfa com_google_android_gms_internal_zzfa) {
        synchronized (this.zzpV) {
            this.zzCj = i;
            this.zzCk = com_google_android_gms_internal_zzfa;
            this.zzpV.notify();
        }
    }

    public void zzr(int i) {
        synchronized (this.zzpV) {
            this.zzCj = i;
            this.zzpV.notify();
        }
    }
}
