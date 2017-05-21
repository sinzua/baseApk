package com.google.android.gms.internal;

import android.content.Context;
import com.google.ads.afma.nano.NanoAfmaSignals.AFMASignals;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class zzam extends zzal {
    private static AdvertisingIdClient zzok = null;
    private static CountDownLatch zzol = new CountDownLatch(1);
    private boolean zzom;

    class zza {
        private String zzon;
        private boolean zzoo;
        final /* synthetic */ zzam zzop;

        public zza(zzam com_google_android_gms_internal_zzam, String str, boolean z) {
            this.zzop = com_google_android_gms_internal_zzam;
            this.zzon = str;
            this.zzoo = z;
        }

        public String getId() {
            return this.zzon;
        }

        public boolean isLimitAdTrackingEnabled() {
            return this.zzoo;
        }
    }

    private static final class zzb implements Runnable {
        private Context zzoq;

        public zzb(Context context) {
            this.zzoq = context.getApplicationContext();
            if (this.zzoq == null) {
                this.zzoq = context;
            }
        }

        public void run() {
            synchronized (zzam.class) {
                try {
                    if (zzam.zzok == null) {
                        AdvertisingIdClient.setShouldSkipGmsCoreVersionCheck(true);
                        AdvertisingIdClient advertisingIdClient = new AdvertisingIdClient(this.zzoq);
                        advertisingIdClient.start();
                        zzam.zzok = advertisingIdClient;
                    }
                    zzam.zzol.countDown();
                } catch (GooglePlayServicesNotAvailableException e) {
                    try {
                        zzam.zzok = null;
                    } finally {
                        zzam.zzol.countDown();
                    }
                } catch (IOException e2) {
                    zzam.zzok = null;
                } catch (GooglePlayServicesRepairableException e3) {
                    zzam.zzok = null;
                }
            }
        }
    }

    protected zzam(Context context, zzap com_google_android_gms_internal_zzap, boolean z) {
        super(context, com_google_android_gms_internal_zzap);
        this.zzom = z;
    }

    public static zzam zza(String str, Context context, boolean z) {
        zzap com_google_android_gms_internal_zzah = new zzah();
        zzal.zza(str, context, com_google_android_gms_internal_zzah);
        if (z) {
            synchronized (zzam.class) {
                if (zzok == null) {
                    new Thread(new zzb(context)).start();
                }
            }
        }
        return new zzam(context, com_google_android_gms_internal_zzah, z);
    }

    private void zza(Context context, AFMASignals aFMASignals) {
        if (this.zzom) {
            try {
                if (zzS()) {
                    zza zzY = zzY();
                    String id = zzY.getId();
                    if (id != null) {
                        aFMASignals.didOptOut = Boolean.valueOf(zzY.isLimitAdTrackingEnabled());
                        aFMASignals.didSignalType = Integer.valueOf(5);
                        aFMASignals.didSignal = id;
                        zzal.zza(28, zzob);
                        return;
                    }
                    return;
                }
                aFMASignals.didSignal = zzal.zzf(context);
                zzal.zza(24, zzob);
            } catch (IOException e) {
            } catch (zza e2) {
            }
        }
    }

    zza zzY() throws IOException {
        try {
            if (!zzol.await(2, TimeUnit.SECONDS)) {
                return new zza(this, null, false);
            }
            synchronized (zzam.class) {
                if (zzok == null) {
                    zza com_google_android_gms_internal_zzam_zza = new zza(this, null, false);
                    return com_google_android_gms_internal_zzam_zza;
                }
                Info info = zzok.getInfo();
                return new zza(this, zzk(info.getId()), info.isLimitAdTrackingEnabled());
            }
        } catch (InterruptedException e) {
            return new zza(this, null, false);
        }
    }

    protected AFMASignals zzc(Context context) {
        AFMASignals zzc = super.zzc(context);
        zza(context, zzc);
        return zzc;
    }
}
