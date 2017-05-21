package com.google.android.gms.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import java.util.concurrent.Future;

@zzhb
public final class zzip {

    public interface zzb {
        void zze(Bundle bundle);
    }

    private static abstract class zza extends zzim {
        private zza() {
        }

        public void onStop() {
        }
    }

    public static Future zza(final Context context, final int i) {
        return new zza() {
            public void zzbr() {
                Editor edit = zzip.zzw(context).edit();
                edit.putInt("webview_cache_version", i);
                edit.apply();
            }
        }.zzhn();
    }

    public static Future zza(final Context context, final zzb com_google_android_gms_internal_zzip_zzb) {
        return new zza() {
            public void zzbr() {
                SharedPreferences zzw = zzip.zzw(context);
                Bundle bundle = new Bundle();
                bundle.putBoolean("use_https", zzw.getBoolean("use_https", true));
                if (com_google_android_gms_internal_zzip_zzb != null) {
                    com_google_android_gms_internal_zzip_zzb.zze(bundle);
                }
            }
        }.zzhn();
    }

    public static Future zza(final Context context, final boolean z) {
        return new zza() {
            public void zzbr() {
                Editor edit = zzip.zzw(context).edit();
                edit.putBoolean("use_https", z);
                edit.apply();
            }
        }.zzhn();
    }

    public static Future zzb(final Context context, final zzb com_google_android_gms_internal_zzip_zzb) {
        return new zza() {
            public void zzbr() {
                SharedPreferences zzw = zzip.zzw(context);
                Bundle bundle = new Bundle();
                bundle.putInt("webview_cache_version", zzw.getInt("webview_cache_version", 0));
                if (com_google_android_gms_internal_zzip_zzb != null) {
                    com_google_android_gms_internal_zzip_zzb.zze(bundle);
                }
            }
        }.zzhn();
    }

    public static Future zzb(final Context context, final boolean z) {
        return new zza() {
            public void zzbr() {
                Editor edit = zzip.zzw(context).edit();
                edit.putBoolean("content_url_opted_out", z);
                edit.apply();
            }
        }.zzhn();
    }

    public static Future zzc(final Context context, final zzb com_google_android_gms_internal_zzip_zzb) {
        return new zza() {
            public void zzbr() {
                SharedPreferences zzw = zzip.zzw(context);
                Bundle bundle = new Bundle();
                bundle.putBoolean("content_url_opted_out", zzw.getBoolean("content_url_opted_out", true));
                if (com_google_android_gms_internal_zzip_zzb != null) {
                    com_google_android_gms_internal_zzip_zzb.zze(bundle);
                }
            }
        }.zzhn();
    }

    public static Future zzd(final Context context, final zzb com_google_android_gms_internal_zzip_zzb) {
        return new zza() {
            public void zzbr() {
                SharedPreferences zzw = zzip.zzw(context);
                Bundle bundle = new Bundle();
                bundle.putString("content_url_hashes", zzw.getString("content_url_hashes", ""));
                if (com_google_android_gms_internal_zzip_zzb != null) {
                    com_google_android_gms_internal_zzip_zzb.zze(bundle);
                }
            }
        }.zzhn();
    }

    public static Future zzd(final Context context, final String str) {
        return new zza() {
            public void zzbr() {
                Editor edit = zzip.zzw(context).edit();
                edit.putString("content_url_hashes", str);
                edit.apply();
            }
        }.zzhn();
    }

    public static SharedPreferences zzw(Context context) {
        return context.getSharedPreferences("admob", 0);
    }
}
