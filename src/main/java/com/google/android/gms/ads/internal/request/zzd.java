package com.google.android.gms.ads.internal.request;

import android.content.Context;
import android.os.Binder;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.internal.zzbm;
import com.google.android.gms.internal.zzbt;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzhc;
import com.google.android.gms.internal.zzhd;
import com.google.android.gms.internal.zzit;
import com.google.android.gms.internal.zzji;
import com.google.android.gms.internal.zzji.zzc;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;

@zzhb
public abstract class zzd implements com.google.android.gms.ads.internal.request.zzc.zza, zzit<Void> {
    private final zzji<AdRequestInfoParcel> zzHl;
    private final com.google.android.gms.ads.internal.request.zzc.zza zzHm;
    private final Object zzpV = new Object();

    @zzhb
    public static final class zza extends zzd {
        private final Context mContext;

        public zza(Context context, zzji<AdRequestInfoParcel> com_google_android_gms_internal_zzji_com_google_android_gms_ads_internal_request_AdRequestInfoParcel, com.google.android.gms.ads.internal.request.zzc.zza com_google_android_gms_ads_internal_request_zzc_zza) {
            super(com_google_android_gms_internal_zzji_com_google_android_gms_ads_internal_request_AdRequestInfoParcel, com_google_android_gms_ads_internal_request_zzc_zza);
            this.mContext = context;
        }

        public /* synthetic */ Object zzgd() {
            return super.zzga();
        }

        public void zzgr() {
        }

        public zzj zzgs() {
            return zzhd.zza(this.mContext, new zzbm((String) zzbt.zzvB.get()), zzhc.zzgA());
        }
    }

    @zzhb
    public static class zzb extends zzd implements ConnectionCallbacks, OnConnectionFailedListener {
        private Context mContext;
        private zzji<AdRequestInfoParcel> zzHl;
        private final com.google.android.gms.ads.internal.request.zzc.zza zzHm;
        protected zze zzHp;
        private boolean zzHq;
        private VersionInfoParcel zzpT;
        private final Object zzpV = new Object();

        public zzb(Context context, VersionInfoParcel versionInfoParcel, zzji<AdRequestInfoParcel> com_google_android_gms_internal_zzji_com_google_android_gms_ads_internal_request_AdRequestInfoParcel, com.google.android.gms.ads.internal.request.zzc.zza com_google_android_gms_ads_internal_request_zzc_zza) {
            Looper zzhC;
            super(com_google_android_gms_internal_zzji_com_google_android_gms_ads_internal_request_AdRequestInfoParcel, com_google_android_gms_ads_internal_request_zzc_zza);
            this.mContext = context;
            this.zzpT = versionInfoParcel;
            this.zzHl = com_google_android_gms_internal_zzji_com_google_android_gms_ads_internal_request_AdRequestInfoParcel;
            this.zzHm = com_google_android_gms_ads_internal_request_zzc_zza;
            if (((Boolean) zzbt.zzwa.get()).booleanValue()) {
                this.zzHq = true;
                zzhC = zzr.zzbO().zzhC();
            } else {
                zzhC = context.getMainLooper();
            }
            this.zzHp = new zze(context, zzhC, this, this, this.zzpT.zzNa);
            connect();
        }

        protected void connect() {
            this.zzHp.zzqG();
        }

        public void onConnected(Bundle connectionHint) {
            zzga();
        }

        public void onConnectionFailed(@NonNull ConnectionResult result) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Cannot connect to remote service, fallback to local instance.");
            zzgt().zzgd();
            Bundle bundle = new Bundle();
            bundle.putString(ParametersKeys.ACTION, "gms_connection_failed_fallback_to_local");
            zzr.zzbC().zzb(this.mContext, this.zzpT.afmaVersion, "gmob-apps", bundle, true);
        }

        public void onConnectionSuspended(int cause) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Disconnected from remote ad request service.");
        }

        public /* synthetic */ Object zzgd() {
            return super.zzga();
        }

        public void zzgr() {
            synchronized (this.zzpV) {
                if (this.zzHp.isConnected() || this.zzHp.isConnecting()) {
                    this.zzHp.disconnect();
                }
                Binder.flushPendingCommands();
                if (this.zzHq) {
                    zzr.zzbO().zzhD();
                    this.zzHq = false;
                }
            }
        }

        public zzj zzgs() {
            zzj zzgw;
            synchronized (this.zzpV) {
                try {
                    zzgw = this.zzHp.zzgw();
                } catch (IllegalStateException e) {
                    zzgw = null;
                    return zzgw;
                } catch (DeadObjectException e2) {
                    zzgw = null;
                    return zzgw;
                }
            }
            return zzgw;
        }

        zzit zzgt() {
            return new zza(this.mContext, this.zzHl, this.zzHm);
        }
    }

    public zzd(zzji<AdRequestInfoParcel> com_google_android_gms_internal_zzji_com_google_android_gms_ads_internal_request_AdRequestInfoParcel, com.google.android.gms.ads.internal.request.zzc.zza com_google_android_gms_ads_internal_request_zzc_zza) {
        this.zzHl = com_google_android_gms_internal_zzji_com_google_android_gms_ads_internal_request_AdRequestInfoParcel;
        this.zzHm = com_google_android_gms_ads_internal_request_zzc_zza;
    }

    public void cancel() {
        zzgr();
    }

    boolean zza(zzj com_google_android_gms_ads_internal_request_zzj, AdRequestInfoParcel adRequestInfoParcel) {
        try {
            com_google_android_gms_ads_internal_request_zzj.zza(adRequestInfoParcel, new zzg(this));
            return true;
        } catch (Throwable e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not fetch ad response from ad request service.", e);
            zzr.zzbF().zzb(e, true);
        } catch (Throwable e2) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not fetch ad response from ad request service due to an Exception.", e2);
            zzr.zzbF().zzb(e2, true);
        } catch (Throwable e22) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not fetch ad response from ad request service due to an Exception.", e22);
            zzr.zzbF().zzb(e22, true);
        } catch (Throwable e222) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not fetch ad response from ad request service due to an Exception.", e222);
            zzr.zzbF().zzb(e222, true);
        }
        this.zzHm.zzb(new AdResponseParcel(0));
        return false;
    }

    public void zzb(AdResponseParcel adResponseParcel) {
        synchronized (this.zzpV) {
            this.zzHm.zzb(adResponseParcel);
            zzgr();
        }
    }

    public Void zzga() {
        final zzj zzgs = zzgs();
        if (zzgs == null) {
            this.zzHm.zzb(new AdResponseParcel(0));
            zzgr();
        } else {
            this.zzHl.zza(new zzc<AdRequestInfoParcel>(this) {
                final /* synthetic */ zzd zzHo;

                public void zzc(AdRequestInfoParcel adRequestInfoParcel) {
                    if (!this.zzHo.zza(zzgs, adRequestInfoParcel)) {
                        this.zzHo.zzgr();
                    }
                }

                public /* synthetic */ void zze(Object obj) {
                    zzc((AdRequestInfoParcel) obj);
                }
            }, new com.google.android.gms.internal.zzji.zza(this) {
                final /* synthetic */ zzd zzHo;

                {
                    this.zzHo = r1;
                }

                public void run() {
                    this.zzHo.zzgr();
                }
            });
        }
        return null;
    }

    public /* synthetic */ Object zzgd() {
        return zzga();
    }

    public abstract void zzgr();

    public abstract zzj zzgs();
}
