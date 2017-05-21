package com.google.android.gms.internal;

import java.io.IOException;

public final class zzsx {
    public static final boolean[] zzbuA = new boolean[0];
    public static final String[] zzbuB = new String[0];
    public static final byte[][] zzbuC = new byte[0][];
    public static final byte[] zzbuD = new byte[0];
    public static final int[] zzbuw = new int[0];
    public static final long[] zzbux = new long[0];
    public static final float[] zzbuy = new float[0];
    public static final double[] zzbuz = new double[0];

    static int zzF(int i, int i2) {
        return (i << 3) | i2;
    }

    public static boolean zzb(zzsm com_google_android_gms_internal_zzsm, int i) throws IOException {
        return com_google_android_gms_internal_zzsm.zzmo(i);
    }

    public static final int zzc(zzsm com_google_android_gms_internal_zzsm, int i) throws IOException {
        int i2 = 1;
        int position = com_google_android_gms_internal_zzsm.getPosition();
        com_google_android_gms_internal_zzsm.zzmo(i);
        while (com_google_android_gms_internal_zzsm.zzIX() == i) {
            com_google_android_gms_internal_zzsm.zzmo(i);
            i2++;
        }
        com_google_android_gms_internal_zzsm.zzms(position);
        return i2;
    }

    static int zzmI(int i) {
        return i & 7;
    }

    public static int zzmJ(int i) {
        return i >>> 3;
    }
}
