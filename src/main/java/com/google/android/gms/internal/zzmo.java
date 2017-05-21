package com.google.android.gms.internal;

import android.util.Base64;

public final class zzmo {
    public static String zzj(byte[] bArr) {
        return bArr == null ? null : Base64.encodeToString(bArr, 0);
    }

    public static String zzk(byte[] bArr) {
        return bArr == null ? null : Base64.encodeToString(bArr, 10);
    }
}
