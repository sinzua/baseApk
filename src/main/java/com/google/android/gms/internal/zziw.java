package com.google.android.gms.internal;

import android.content.Context;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

@zzhb
public class zziw {
    private static zzl zzMy;
    public static final zza<Void> zzMz = new zza() {
        public /* synthetic */ Object zzgp() {
            return zzhB();
        }

        public /* synthetic */ Object zzh(InputStream inputStream) {
            return zzi(inputStream);
        }

        public Void zzhB() {
            return null;
        }

        public Void zzi(InputStream inputStream) {
            return null;
        }
    };
    private static final Object zzqy = new Object();

    public interface zza<T> {
        T zzgp();

        T zzh(InputStream inputStream);
    }

    private static class zzb<T> extends zzk<InputStream> {
        private final zza<T> zzMD;
        private final com.google.android.gms.internal.zzm.zzb<T> zzaG;

        class AnonymousClass1 implements com.google.android.gms.internal.zzm.zza {
            final /* synthetic */ com.google.android.gms.internal.zzm.zzb zzME;
            final /* synthetic */ zza zzMF;

            AnonymousClass1(com.google.android.gms.internal.zzm.zzb com_google_android_gms_internal_zzm_zzb, zza com_google_android_gms_internal_zziw_zza) {
                this.zzME = com_google_android_gms_internal_zzm_zzb;
                this.zzMF = com_google_android_gms_internal_zziw_zza;
            }

            public void zze(zzr com_google_android_gms_internal_zzr) {
                this.zzME.zzb(this.zzMF.zzgp());
            }
        }

        public zzb(String str, zza<T> com_google_android_gms_internal_zziw_zza_T, com.google.android.gms.internal.zzm.zzb<T> com_google_android_gms_internal_zzm_zzb_T) {
            super(0, str, new AnonymousClass1(com_google_android_gms_internal_zzm_zzb_T, com_google_android_gms_internal_zziw_zza_T));
            this.zzMD = com_google_android_gms_internal_zziw_zza_T;
            this.zzaG = com_google_android_gms_internal_zzm_zzb_T;
        }

        protected zzm<InputStream> zza(zzi com_google_android_gms_internal_zzi) {
            return zzm.zza(new ByteArrayInputStream(com_google_android_gms_internal_zzi.data), zzx.zzb(com_google_android_gms_internal_zzi));
        }

        protected /* synthetic */ void zza(Object obj) {
            zzj((InputStream) obj);
        }

        protected void zzj(InputStream inputStream) {
            this.zzaG.zzb(this.zzMD.zzh(inputStream));
        }
    }

    private class zzc<T> extends zzjd<T> implements com.google.android.gms.internal.zzm.zzb<T> {
        final /* synthetic */ zziw zzMB;

        private zzc(zziw com_google_android_gms_internal_zziw) {
            this.zzMB = com_google_android_gms_internal_zziw;
        }

        public void zzb(T t) {
            super.zzg(t);
        }
    }

    public zziw(Context context) {
        zzMy = zzS(context);
    }

    private static zzl zzS(Context context) {
        zzl com_google_android_gms_internal_zzl;
        synchronized (zzqy) {
            if (zzMy == null) {
                zzMy = zzac.zza(context.getApplicationContext());
            }
            com_google_android_gms_internal_zzl = zzMy;
        }
        return com_google_android_gms_internal_zzl;
    }

    public <T> zzjg<T> zza(String str, zza<T> com_google_android_gms_internal_zziw_zza_T) {
        Object com_google_android_gms_internal_zziw_zzc = new zzc();
        zzMy.zze(new zzb(str, com_google_android_gms_internal_zziw_zza_T, com_google_android_gms_internal_zziw_zzc));
        return com_google_android_gms_internal_zziw_zzc;
    }

    public zzjg<String> zzb(final String str, Map<String, String> map) {
        final Object com_google_android_gms_internal_zziw_zzc = new zzc();
        final Map<String, String> map2 = map;
        zzMy.zze(new zzab(this, str, com_google_android_gms_internal_zziw_zzc, new com.google.android.gms.internal.zzm.zza(this) {
            final /* synthetic */ zziw zzMB;

            public void zze(zzr com_google_android_gms_internal_zzr) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaK("Failed to load URL: " + str + "\n" + com_google_android_gms_internal_zzr.toString());
                com_google_android_gms_internal_zziw_zzc.zzb(null);
            }
        }) {
            final /* synthetic */ zziw zzMB;

            public Map<String, String> getHeaders() throws zza {
                return map2 == null ? super.getHeaders() : map2;
            }
        });
        return com_google_android_gms_internal_zziw_zzc;
    }
}
