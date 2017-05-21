package com.google.android.gms.internal;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.common.zze;
import java.util.Locale;

@zzhb
public final class zzhj {
    public final int zzHD;
    public final int zzHE;
    public final float zzHF;
    public final int zzJQ;
    public final boolean zzJR;
    public final boolean zzJS;
    public final String zzJT;
    public final String zzJU;
    public final boolean zzJV;
    public final boolean zzJW;
    public final boolean zzJX;
    public final boolean zzJY;
    public final String zzJZ;
    public final String zzKa;
    public final int zzKb;
    public final int zzKc;
    public final int zzKd;
    public final int zzKe;
    public final int zzKf;
    public final int zzKg;
    public final double zzKh;
    public final boolean zzKi;
    public final boolean zzKj;
    public final int zzKk;
    public final String zzKl;

    public static final class zza {
        private int zzHD;
        private int zzHE;
        private float zzHF;
        private int zzJQ;
        private boolean zzJR;
        private boolean zzJS;
        private String zzJT;
        private String zzJU;
        private boolean zzJV;
        private boolean zzJW;
        private boolean zzJX;
        private boolean zzJY;
        private String zzJZ;
        private String zzKa;
        private int zzKb;
        private int zzKc;
        private int zzKd;
        private int zzKe;
        private int zzKf;
        private int zzKg;
        private double zzKh;
        private boolean zzKi;
        private boolean zzKj;
        private int zzKk;
        private String zzKl;

        public zza(Context context) {
            boolean z = true;
            PackageManager packageManager = context.getPackageManager();
            zzB(context);
            zza(context, packageManager);
            zzC(context);
            Locale locale = Locale.getDefault();
            this.zzJR = zza(packageManager, "geo:0,0?q=donuts") != null;
            if (zza(packageManager, "http://www.google.com") == null) {
                z = false;
            }
            this.zzJS = z;
            this.zzJU = locale.getCountry();
            this.zzJV = zzn.zzcS().zzhI();
            this.zzJW = zze.zzap(context);
            this.zzJZ = locale.getLanguage();
            this.zzKa = zza(packageManager);
            Resources resources = context.getResources();
            if (resources != null) {
                DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                if (displayMetrics != null) {
                    this.zzHF = displayMetrics.density;
                    this.zzHD = displayMetrics.widthPixels;
                    this.zzHE = displayMetrics.heightPixels;
                }
            }
        }

        public zza(Context context, zzhj com_google_android_gms_internal_zzhj) {
            PackageManager packageManager = context.getPackageManager();
            zzB(context);
            zza(context, packageManager);
            zzC(context);
            zzD(context);
            this.zzJR = com_google_android_gms_internal_zzhj.zzJR;
            this.zzJS = com_google_android_gms_internal_zzhj.zzJS;
            this.zzJU = com_google_android_gms_internal_zzhj.zzJU;
            this.zzJV = com_google_android_gms_internal_zzhj.zzJV;
            this.zzJW = com_google_android_gms_internal_zzhj.zzJW;
            this.zzJZ = com_google_android_gms_internal_zzhj.zzJZ;
            this.zzKa = com_google_android_gms_internal_zzhj.zzKa;
            this.zzHF = com_google_android_gms_internal_zzhj.zzHF;
            this.zzHD = com_google_android_gms_internal_zzhj.zzHD;
            this.zzHE = com_google_android_gms_internal_zzhj.zzHE;
        }

        private void zzB(Context context) {
            AudioManager audioManager = (AudioManager) context.getSystemService("audio");
            this.zzJQ = audioManager.getMode();
            this.zzJX = audioManager.isMusicActive();
            this.zzJY = audioManager.isSpeakerphoneOn();
            this.zzKb = audioManager.getStreamVolume(3);
            this.zzKf = audioManager.getRingerMode();
            this.zzKg = audioManager.getStreamVolume(2);
        }

        private void zzC(Context context) {
            boolean z = false;
            Intent registerReceiver = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            if (registerReceiver != null) {
                int intExtra = registerReceiver.getIntExtra("status", -1);
                this.zzKh = (double) (((float) registerReceiver.getIntExtra("level", -1)) / ((float) registerReceiver.getIntExtra("scale", -1)));
                if (intExtra == 2 || intExtra == 5) {
                    z = true;
                }
                this.zzKi = z;
                return;
            }
            this.zzKh = -1.0d;
            this.zzKi = false;
        }

        private void zzD(Context context) {
            this.zzKl = Build.FINGERPRINT;
        }

        private static ResolveInfo zza(PackageManager packageManager, String str) {
            return packageManager.resolveActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)), 65536);
        }

        private static String zza(PackageManager packageManager) {
            String str = null;
            ResolveInfo zza = zza(packageManager, "market://details?id=com.google.android.gms.ads");
            if (zza != null) {
                ActivityInfo activityInfo = zza.activityInfo;
                if (activityInfo != null) {
                    try {
                        PackageInfo packageInfo = packageManager.getPackageInfo(activityInfo.packageName, 0);
                        if (packageInfo != null) {
                            str = packageInfo.versionCode + "." + activityInfo.packageName;
                        }
                    } catch (NameNotFoundException e) {
                    }
                }
            }
            return str;
        }

        @TargetApi(16)
        private void zza(Context context, PackageManager packageManager) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            this.zzJT = telephonyManager.getNetworkOperator();
            this.zzKd = telephonyManager.getNetworkType();
            this.zzKe = telephonyManager.getPhoneType();
            this.zzKc = -2;
            this.zzKj = false;
            this.zzKk = -1;
            if (zzr.zzbC().zza(packageManager, context.getPackageName(), "android.permission.ACCESS_NETWORK_STATE")) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null) {
                    this.zzKc = activeNetworkInfo.getType();
                    this.zzKk = activeNetworkInfo.getDetailedState().ordinal();
                } else {
                    this.zzKc = -1;
                }
                if (VERSION.SDK_INT >= 16) {
                    this.zzKj = connectivityManager.isActiveNetworkMetered();
                }
            }
        }

        public zzhj zzgI() {
            return new zzhj(this.zzJQ, this.zzJR, this.zzJS, this.zzJT, this.zzJU, this.zzJV, this.zzJW, this.zzJX, this.zzJY, this.zzJZ, this.zzKa, this.zzKb, this.zzKc, this.zzKd, this.zzKe, this.zzKf, this.zzKg, this.zzHF, this.zzHD, this.zzHE, this.zzKh, this.zzKi, this.zzKj, this.zzKk, this.zzKl);
        }
    }

    zzhj(int i, boolean z, boolean z2, String str, String str2, boolean z3, boolean z4, boolean z5, boolean z6, String str3, String str4, int i2, int i3, int i4, int i5, int i6, int i7, float f, int i8, int i9, double d, boolean z7, boolean z8, int i10, String str5) {
        this.zzJQ = i;
        this.zzJR = z;
        this.zzJS = z2;
        this.zzJT = str;
        this.zzJU = str2;
        this.zzJV = z3;
        this.zzJW = z4;
        this.zzJX = z5;
        this.zzJY = z6;
        this.zzJZ = str3;
        this.zzKa = str4;
        this.zzKb = i2;
        this.zzKc = i3;
        this.zzKd = i4;
        this.zzKe = i5;
        this.zzKf = i6;
        this.zzKg = i7;
        this.zzHF = f;
        this.zzHD = i8;
        this.zzHE = i9;
        this.zzKh = d;
        this.zzKi = z7;
        this.zzKj = z8;
        this.zzKk = i10;
        this.zzKl = str5;
    }
}
