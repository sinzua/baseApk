package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzeg.zzd;
import com.google.android.gms.internal.zzji.zza;
import com.google.android.gms.internal.zzji.zzc;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
public class zzba extends zzau {
    private zzd zzsF;
    private boolean zzsG;

    public zzba(Context context, AdSizeParcel adSizeParcel, zzif com_google_android_gms_internal_zzif, VersionInfoParcel versionInfoParcel, zzbb com_google_android_gms_internal_zzbb, zzeg com_google_android_gms_internal_zzeg) {
        super(context, adSizeParcel, com_google_android_gms_internal_zzif, versionInfoParcel, com_google_android_gms_internal_zzbb);
        this.zzsF = com_google_android_gms_internal_zzeg.zzer();
        try {
            final JSONObject zzd = zzd(com_google_android_gms_internal_zzbb.zzcq().zzco());
            this.zzsF.zza(new zzc<zzeh>(this) {
                final /* synthetic */ zzba zzsI;

                public void zzd(zzeh com_google_android_gms_internal_zzeh) {
                    this.zzsI.zza(zzd);
                }

                public /* synthetic */ void zze(Object obj) {
                    zzd((zzeh) obj);
                }
            }, new zza(this) {
                final /* synthetic */ zzba zzsI;

                {
                    this.zzsI = r1;
                }

                public void run() {
                }
            });
        } catch (JSONException e) {
        } catch (Throwable e2) {
            zzb.zzb("Failure while processing active view data.", e2);
        }
        this.zzsF.zza(new zzc<zzeh>(this) {
            final /* synthetic */ zzba zzsI;

            {
                this.zzsI = r1;
            }

            public void zzd(zzeh com_google_android_gms_internal_zzeh) {
                this.zzsI.zzsG = true;
                this.zzsI.zzb(com_google_android_gms_internal_zzeh);
                this.zzsI.zzcd();
                this.zzsI.zzh(false);
            }

            public /* synthetic */ void zze(Object obj) {
                zzd((zzeh) obj);
            }
        }, new zza(this) {
            final /* synthetic */ zzba zzsI;

            {
                this.zzsI = r1;
            }

            public void run() {
                this.zzsI.destroy();
            }
        });
        zzb.zzaI("Tracking ad unit: " + this.zzrZ.zzcu());
    }

    protected void destroy() {
        synchronized (this.zzpV) {
            super.destroy();
            this.zzsF.zza(new zzc<zzeh>(this) {
                final /* synthetic */ zzba zzsI;

                {
                    this.zzsI = r1;
                }

                public void zzd(zzeh com_google_android_gms_internal_zzeh) {
                    this.zzsI.zzc(com_google_android_gms_internal_zzeh);
                }

                public /* synthetic */ void zze(Object obj) {
                    zzd((zzeh) obj);
                }
            }, new zzji.zzb());
            this.zzsF.release();
        }
    }

    protected void zzb(final JSONObject jSONObject) {
        this.zzsF.zza(new zzc<zzeh>(this) {
            final /* synthetic */ zzba zzsI;

            public void zzd(zzeh com_google_android_gms_internal_zzeh) {
                com_google_android_gms_internal_zzeh.zza("AFMA_updateActiveView", jSONObject);
            }

            public /* synthetic */ void zze(Object obj) {
                zzd((zzeh) obj);
            }
        }, new zzji.zzb());
    }

    protected boolean zzcl() {
        return this.zzsG;
    }
}
