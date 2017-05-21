package com.google.android.gms.internal;

import android.content.Context;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.zzr;
import java.math.BigInteger;
import java.util.Locale;

@zzhb
public final class zzil {
    private static String zzLL;
    private static final Object zzqy = new Object();

    public static String zza(Context context, String str, String str2) {
        String str3;
        synchronized (zzqy) {
            if (zzLL == null && !TextUtils.isEmpty(str)) {
                zzb(context, str, str2);
            }
            str3 = zzLL;
        }
        return str3;
    }

    private static void zzb(Context context, String str, String str2) {
        try {
            ClassLoader classLoader = context.createPackageContext(str2, 3).getClassLoader();
            Class cls = Class.forName("com.google.ads.mediation.MediationAdapter", false, classLoader);
            BigInteger bigInteger = new BigInteger(new byte[1]);
            String[] split = str.split(",");
            BigInteger bigInteger2 = bigInteger;
            for (int i = 0; i < split.length; i++) {
                if (zzr.zzbC().zza(classLoader, cls, split[i])) {
                    bigInteger2 = bigInteger2.setBit(i);
                }
            }
            zzLL = String.format(Locale.US, "%X", new Object[]{bigInteger2});
        } catch (Throwable th) {
            zzLL = "err";
        }
    }

    public static String zzhm() {
        String str;
        synchronized (zzqy) {
            str = zzLL;
        }
        return str;
    }
}
