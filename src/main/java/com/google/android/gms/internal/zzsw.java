package com.google.android.gms.internal;

import java.io.IOException;
import java.util.Arrays;

final class zzsw {
    final int tag;
    final byte[] zzbuv;

    zzsw(int i, byte[] bArr) {
        this.tag = i;
        this.zzbuv = bArr;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzsw)) {
            return false;
        }
        zzsw com_google_android_gms_internal_zzsw = (zzsw) o;
        return this.tag == com_google_android_gms_internal_zzsw.tag && Arrays.equals(this.zzbuv, com_google_android_gms_internal_zzsw.zzbuv);
    }

    public int hashCode() {
        return ((this.tag + 527) * 31) + Arrays.hashCode(this.zzbuv);
    }

    void writeTo(zzsn output) throws IOException {
        output.zzmB(this.tag);
        output.zzH(this.zzbuv);
    }

    int zzz() {
        return (0 + zzsn.zzmC(this.tag)) + this.zzbuv.length;
    }
}
