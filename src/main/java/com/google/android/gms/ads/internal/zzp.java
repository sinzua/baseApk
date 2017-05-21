package com.google.android.gms.ads.internal;

import android.content.Context;
import android.os.RemoteException;
import android.support.v4.util.SimpleArrayMap;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.formats.zzd;
import com.google.android.gms.ads.internal.formats.zze;
import com.google.android.gms.ads.internal.formats.zzf;
import com.google.android.gms.ads.internal.formats.zzg;
import com.google.android.gms.ads.internal.formats.zzh;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzcb;
import com.google.android.gms.internal.zzcf;
import com.google.android.gms.internal.zzcr;
import com.google.android.gms.internal.zzcs;
import com.google.android.gms.internal.zzct;
import com.google.android.gms.internal.zzcu;
import com.google.android.gms.internal.zzex;
import com.google.android.gms.internal.zzfb;
import com.google.android.gms.internal.zzfc;
import com.google.android.gms.internal.zzgd;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzif;
import com.google.android.gms.internal.zzif.zza;
import com.google.android.gms.internal.zzir;
import java.util.List;

@zzhb
public class zzp extends zzb {
    public zzp(Context context, zzd com_google_android_gms_ads_internal_zzd, AdSizeParcel adSizeParcel, String str, zzex com_google_android_gms_internal_zzex, VersionInfoParcel versionInfoParcel) {
        super(context, adSizeParcel, str, com_google_android_gms_internal_zzex, versionInfoParcel, com_google_android_gms_ads_internal_zzd);
    }

    private static zzd zza(zzfb com_google_android_gms_internal_zzfb) throws RemoteException {
        return new zzd(com_google_android_gms_internal_zzfb.getHeadline(), com_google_android_gms_internal_zzfb.getImages(), com_google_android_gms_internal_zzfb.getBody(), com_google_android_gms_internal_zzfb.zzdK() != null ? com_google_android_gms_internal_zzfb.zzdK() : null, com_google_android_gms_internal_zzfb.getCallToAction(), com_google_android_gms_internal_zzfb.getStarRating(), com_google_android_gms_internal_zzfb.getStore(), com_google_android_gms_internal_zzfb.getPrice(), null, com_google_android_gms_internal_zzfb.getExtras());
    }

    private static zze zza(zzfc com_google_android_gms_internal_zzfc) throws RemoteException {
        return new zze(com_google_android_gms_internal_zzfc.getHeadline(), com_google_android_gms_internal_zzfc.getImages(), com_google_android_gms_internal_zzfc.getBody(), com_google_android_gms_internal_zzfc.zzdO() != null ? com_google_android_gms_internal_zzfc.zzdO() : null, com_google_android_gms_internal_zzfc.getCallToAction(), com_google_android_gms_internal_zzfc.getAdvertiser(), null, com_google_android_gms_internal_zzfc.getExtras());
    }

    private void zza(final zzd com_google_android_gms_ads_internal_formats_zzd) {
        zzir.zzMc.post(new Runnable(this) {
            final /* synthetic */ zzp zzqC;

            public void run() {
                try {
                    this.zzqC.zzpj.zzrz.zza(com_google_android_gms_ads_internal_formats_zzd);
                } catch (Throwable e) {
                    zzb.zzd("Could not call OnAppInstallAdLoadedListener.onAppInstallAdLoaded().", e);
                }
            }
        });
    }

    private void zza(final zze com_google_android_gms_ads_internal_formats_zze) {
        zzir.zzMc.post(new Runnable(this) {
            final /* synthetic */ zzp zzqC;

            public void run() {
                try {
                    this.zzqC.zzpj.zzrA.zza(com_google_android_gms_ads_internal_formats_zze);
                } catch (Throwable e) {
                    zzb.zzd("Could not call OnContentAdLoadedListener.onContentAdLoaded().", e);
                }
            }
        });
    }

    private void zza(final zzif com_google_android_gms_internal_zzif, final String str) {
        zzir.zzMc.post(new Runnable(this) {
            final /* synthetic */ zzp zzqC;

            public void run() {
                try {
                    ((zzcu) this.zzqC.zzpj.zzrC.get(str)).zza((zzf) com_google_android_gms_internal_zzif.zzLa);
                } catch (Throwable e) {
                    zzb.zzd("Could not call onCustomTemplateAdLoadedListener.onCustomTemplateAdLoaded().", e);
                }
            }
        });
    }

    public void pause() {
        throw new IllegalStateException("Native Ad DOES NOT support pause().");
    }

    public void resume() {
        throw new IllegalStateException("Native Ad DOES NOT support resume().");
    }

    public void showInterstitial() {
        throw new IllegalStateException("Interstitial is NOT supported by NativeAdManager.");
    }

    public void zza(SimpleArrayMap<String, zzcu> simpleArrayMap) {
        zzx.zzcD("setOnCustomTemplateAdLoadedListeners must be called on the main UI thread.");
        this.zzpj.zzrC = simpleArrayMap;
    }

    public void zza(zzh com_google_android_gms_ads_internal_formats_zzh) {
        if (this.zzpj.zzrq.zzKT != null) {
            zzr.zzbF().zzhh().zza(this.zzpj.zzrp, this.zzpj.zzrq, com_google_android_gms_ads_internal_formats_zzh);
        }
    }

    public void zza(zzcf com_google_android_gms_internal_zzcf) {
        throw new IllegalStateException("CustomRendering is NOT supported by NativeAdManager.");
    }

    public void zza(zzgd com_google_android_gms_internal_zzgd) {
        throw new IllegalStateException("In App Purchase is NOT supported by NativeAdManager.");
    }

    public void zza(final zza com_google_android_gms_internal_zzif_zza, zzcb com_google_android_gms_internal_zzcb) {
        if (com_google_android_gms_internal_zzif_zza.zzrp != null) {
            this.zzpj.zzrp = com_google_android_gms_internal_zzif_zza.zzrp;
        }
        if (com_google_android_gms_internal_zzif_zza.errorCode != -2) {
            zzir.zzMc.post(new Runnable(this) {
                final /* synthetic */ zzp zzqC;

                public void run() {
                    this.zzqC.zzb(new zzif(com_google_android_gms_internal_zzif_zza, null, null, null, null, null, null));
                }
            });
            return;
        }
        this.zzpj.zzrL = 0;
        this.zzpj.zzro = zzr.zzbB().zza(this.zzpj.context, this, com_google_android_gms_internal_zzif_zza, this.zzpj.zzrk, null, this.zzpn, this, com_google_android_gms_internal_zzcb);
        zzb.zzaI("AdRenderer: " + this.zzpj.zzro.getClass().getName());
    }

    public void zza(List<String> list) {
        zzx.zzcD("setNativeTemplates must be called on the main UI thread.");
        this.zzpj.zzrH = list;
    }

    protected boolean zza(AdRequestParcel adRequestParcel, zzif com_google_android_gms_internal_zzif, boolean z) {
        return this.zzpi.zzbw();
    }

    protected boolean zza(zzif com_google_android_gms_internal_zzif, zzif com_google_android_gms_internal_zzif2) {
        zza(null);
        if (this.zzpj.zzbW()) {
            if (com_google_android_gms_internal_zzif2.zzHT) {
                try {
                    zzfb zzeF = com_google_android_gms_internal_zzif2.zzCq.zzeF();
                    zzfc zzeG = com_google_android_gms_internal_zzif2.zzCq.zzeG();
                    if (zzeF != null) {
                        zzd zza = zza(zzeF);
                        zza.zzb(new zzg(this.zzpj.context, this, this.zzpj.zzrk, zzeF));
                        zza(zza);
                    } else if (zzeG != null) {
                        zze zza2 = zza(zzeG);
                        zza2.zzb(new zzg(this.zzpj.context, this, this.zzpj.zzrk, zzeG));
                        zza(zza2);
                    } else {
                        zzb.zzaK("No matching mapper for retrieved native ad template.");
                        zzf(0);
                        return false;
                    }
                } catch (Throwable e) {
                    zzb.zzd("Failed to get native ad mapper", e);
                }
            } else {
                zzh.zza com_google_android_gms_ads_internal_formats_zzh_zza = com_google_android_gms_internal_zzif2.zzLa;
                if ((com_google_android_gms_ads_internal_formats_zzh_zza instanceof zze) && this.zzpj.zzrA != null) {
                    zza((zze) com_google_android_gms_internal_zzif2.zzLa);
                } else if ((com_google_android_gms_ads_internal_formats_zzh_zza instanceof zzd) && this.zzpj.zzrz != null) {
                    zza((zzd) com_google_android_gms_internal_zzif2.zzLa);
                } else if (!(com_google_android_gms_ads_internal_formats_zzh_zza instanceof zzf) || this.zzpj.zzrC == null || this.zzpj.zzrC.get(((zzf) com_google_android_gms_ads_internal_formats_zzh_zza).getCustomTemplateId()) == null) {
                    zzb.zzaK("No matching listener for retrieved native ad template.");
                    zzf(0);
                    return false;
                } else {
                    zza(com_google_android_gms_internal_zzif2, ((zzf) com_google_android_gms_ads_internal_formats_zzh_zza).getCustomTemplateId());
                }
            }
            return super.zza(com_google_android_gms_internal_zzif, com_google_android_gms_internal_zzif2);
        }
        throw new IllegalStateException("Native ad DOES NOT have custom rendering mode.");
    }

    public void zzb(SimpleArrayMap<String, zzct> simpleArrayMap) {
        zzx.zzcD("setOnCustomClickListener must be called on the main UI thread.");
        this.zzpj.zzrB = simpleArrayMap;
    }

    public void zzb(NativeAdOptionsParcel nativeAdOptionsParcel) {
        zzx.zzcD("setNativeAdOptions must be called on the main UI thread.");
        this.zzpj.zzrD = nativeAdOptionsParcel;
    }

    public void zzb(zzcr com_google_android_gms_internal_zzcr) {
        zzx.zzcD("setOnAppInstallAdLoadedListener must be called on the main UI thread.");
        this.zzpj.zzrz = com_google_android_gms_internal_zzcr;
    }

    public void zzb(zzcs com_google_android_gms_internal_zzcs) {
        zzx.zzcD("setOnContentAdLoadedListener must be called on the main UI thread.");
        this.zzpj.zzrA = com_google_android_gms_internal_zzcs;
    }

    public SimpleArrayMap<String, zzcu> zzbv() {
        zzx.zzcD("getOnCustomTemplateAdLoadedListeners must be called on the main UI thread.");
        return this.zzpj.zzrC;
    }

    public zzct zzs(String str) {
        zzx.zzcD("getOnCustomClickListener must be called on the main UI thread.");
        return (zzct) this.zzpj.zzrB.get(str);
    }
}
