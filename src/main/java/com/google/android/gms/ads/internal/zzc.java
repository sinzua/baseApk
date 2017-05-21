package com.google.android.gms.ads.internal;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzcb;
import com.google.android.gms.internal.zzcc;
import com.google.android.gms.internal.zzce;
import com.google.android.gms.internal.zzcf;
import com.google.android.gms.internal.zzdf;
import com.google.android.gms.internal.zzeh;
import com.google.android.gms.internal.zzex;
import com.google.android.gms.internal.zzft;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzif;
import com.google.android.gms.internal.zzif.zza;
import com.google.android.gms.internal.zzir;
import com.google.android.gms.internal.zzjp;
import java.util.Map;

@zzhb
public abstract class zzc extends zzb implements zzg, zzft {
    public zzc(Context context, AdSizeParcel adSizeParcel, String str, zzex com_google_android_gms_internal_zzex, VersionInfoParcel versionInfoParcel, zzd com_google_android_gms_ads_internal_zzd) {
        super(context, adSizeParcel, str, com_google_android_gms_internal_zzex, versionInfoParcel, com_google_android_gms_ads_internal_zzd);
    }

    protected zzjp zza(zza com_google_android_gms_internal_zzif_zza, zze com_google_android_gms_ads_internal_zze) {
        zzeh com_google_android_gms_internal_zzeh;
        View nextView = this.zzpj.zzrm.getNextView();
        zzjp com_google_android_gms_internal_zzjp;
        if (nextView instanceof zzjp) {
            zzb.zzaI("Reusing webview...");
            com_google_android_gms_internal_zzjp = (zzjp) nextView;
            com_google_android_gms_internal_zzjp.zza(this.zzpj.context, this.zzpj.zzrp, this.zzpe);
            com_google_android_gms_internal_zzeh = com_google_android_gms_internal_zzjp;
        } else {
            if (nextView != null) {
                this.zzpj.zzrm.removeView(nextView);
            }
            com_google_android_gms_internal_zzjp = zzr.zzbD().zza(this.zzpj.context, this.zzpj.zzrp, false, false, this.zzpj.zzrk, this.zzpj.zzrl, this.zzpe, this.zzpm);
            if (this.zzpj.zzrp.zzuj == null) {
                zzb(com_google_android_gms_internal_zzjp.getView());
            }
            Object obj = com_google_android_gms_internal_zzjp;
        }
        com_google_android_gms_internal_zzeh.zzhU().zzb(this, this, this, this, false, this, null, com_google_android_gms_ads_internal_zze, this);
        zza(com_google_android_gms_internal_zzeh);
        com_google_android_gms_internal_zzeh.zzaM(com_google_android_gms_internal_zzif_zza.zzLd.zzHI);
        return com_google_android_gms_internal_zzeh;
    }

    public void zza(int i, int i2, int i3, int i4) {
        zzaS();
    }

    public void zza(zzcf com_google_android_gms_internal_zzcf) {
        zzx.zzcD("setOnCustomRenderedAdLoadedListener must be called on the main UI thread.");
        this.zzpj.zzrE = com_google_android_gms_internal_zzcf;
    }

    protected void zza(zzeh com_google_android_gms_internal_zzeh) {
        com_google_android_gms_internal_zzeh.zza("/trackActiveViewUnit", new zzdf(this) {
            final /* synthetic */ zzc zzpr;

            {
                this.zzpr = r1;
            }

            public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
                if (this.zzpr.zzpj.zzrq != null) {
                    this.zzpr.zzpl.zza(this.zzpr.zzpj.zzrp, this.zzpr.zzpj.zzrq, com_google_android_gms_internal_zzjp.getView(), (zzeh) com_google_android_gms_internal_zzjp);
                } else {
                    zzb.zzaK("Request to enable ActiveView before adState is available.");
                }
            }
        });
    }

    protected void zza(final zza com_google_android_gms_internal_zzif_zza, final zzcb com_google_android_gms_internal_zzcb) {
        if (com_google_android_gms_internal_zzif_zza.errorCode != -2) {
            zzir.zzMc.post(new Runnable(this) {
                final /* synthetic */ zzc zzpr;

                public void run() {
                    this.zzpr.zzb(new zzif(com_google_android_gms_internal_zzif_zza, null, null, null, null, null, null));
                }
            });
            return;
        }
        if (com_google_android_gms_internal_zzif_zza.zzrp != null) {
            this.zzpj.zzrp = com_google_android_gms_internal_zzif_zza.zzrp;
        }
        if (!com_google_android_gms_internal_zzif_zza.zzLe.zzHT || com_google_android_gms_internal_zzif_zza.zzLe.zzum) {
            zzir.zzMc.post(new Runnable(this) {
                final /* synthetic */ zzc zzpr;

                public void run() {
                    if (com_google_android_gms_internal_zzif_zza.zzLe.zzIc && this.zzpr.zzpj.zzrE != null) {
                        String str = null;
                        if (com_google_android_gms_internal_zzif_zza.zzLe.zzEF != null) {
                            str = zzr.zzbC().zzaC(com_google_android_gms_internal_zzif_zza.zzLe.zzEF);
                        }
                        zzce com_google_android_gms_internal_zzcc = new zzcc(this.zzpr, str, com_google_android_gms_internal_zzif_zza.zzLe.body);
                        this.zzpr.zzpj.zzrL = 1;
                        try {
                            this.zzpr.zzph = false;
                            this.zzpr.zzpj.zzrE.zza(com_google_android_gms_internal_zzcc);
                            return;
                        } catch (Throwable e) {
                            zzb.zzd("Could not call the onCustomRenderedAdLoadedListener.", e);
                            this.zzpr.zzph = true;
                        }
                    }
                    final zze com_google_android_gms_ads_internal_zze = new zze();
                    zzjp zza = this.zzpr.zza(com_google_android_gms_internal_zzif_zza, com_google_android_gms_ads_internal_zze);
                    com_google_android_gms_ads_internal_zze.zza(new zze.zzb(com_google_android_gms_internal_zzif_zza, zza));
                    zza.setOnTouchListener(new OnTouchListener(this) {
                        final /* synthetic */ AnonymousClass3 zzpv;

                        public boolean onTouch(View v, MotionEvent event) {
                            com_google_android_gms_ads_internal_zze.recordClick();
                            return false;
                        }
                    });
                    zza.setOnClickListener(new OnClickListener(this) {
                        final /* synthetic */ AnonymousClass3 zzpv;

                        public void onClick(View v) {
                            com_google_android_gms_ads_internal_zze.recordClick();
                        }
                    });
                    this.zzpr.zzpj.zzrL = 0;
                    this.zzpr.zzpj.zzro = zzr.zzbB().zza(this.zzpr.zzpj.context, this.zzpr, com_google_android_gms_internal_zzif_zza, this.zzpr.zzpj.zzrk, zza, this.zzpr.zzpn, this.zzpr, com_google_android_gms_internal_zzcb);
                }
            });
            return;
        }
        this.zzpj.zzrL = 0;
        this.zzpj.zzro = zzr.zzbB().zza(this.zzpj.context, this, com_google_android_gms_internal_zzif_zza, this.zzpj.zzrk, null, this.zzpn, this, com_google_android_gms_internal_zzcb);
    }

    protected boolean zza(zzif com_google_android_gms_internal_zzif, zzif com_google_android_gms_internal_zzif2) {
        if (this.zzpj.zzbW() && this.zzpj.zzrm != null) {
            this.zzpj.zzrm.zzcc().zzaF(com_google_android_gms_internal_zzif2.zzHY);
        }
        return super.zza(com_google_android_gms_internal_zzif, com_google_android_gms_internal_zzif2);
    }

    public void zzbd() {
        onAdClicked();
    }

    public void zzbe() {
        recordImpression();
        zzaP();
    }

    public void zzbf() {
        zzaQ();
    }

    public void zzc(View view) {
        this.zzpj.zzrK = view;
        zzb(new zzif(this.zzpj.zzrr, null, null, null, null, null, null));
    }
}
