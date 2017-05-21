package com.google.android.gms.common.internal;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public abstract class DowngradeableSafeParcel implements SafeParcelable {
    private static final Object zzalh = new Object();
    private static ClassLoader zzali = null;
    private static Integer zzalj = null;
    private boolean zzalk = false;

    private static boolean zza(Class<?> cls) {
        boolean z = false;
        try {
            z = SafeParcelable.NULL.equals(cls.getField("NULL").get(null));
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e2) {
        }
        return z;
    }

    protected static boolean zzcF(String str) {
        ClassLoader zzqA = zzqA();
        if (zzqA == null) {
            return true;
        }
        try {
            return zza(zzqA.loadClass(str));
        } catch (Exception e) {
            return false;
        }
    }

    protected static ClassLoader zzqA() {
        ClassLoader classLoader;
        synchronized (zzalh) {
            classLoader = zzali;
        }
        return classLoader;
    }

    protected static Integer zzqB() {
        Integer num;
        synchronized (zzalh) {
            num = zzalj;
        }
        return num;
    }

    protected boolean zzqC() {
        return this.zzalk;
    }
}
