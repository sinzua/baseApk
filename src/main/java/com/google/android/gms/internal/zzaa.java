package com.google.android.gms.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class zzaa extends ByteArrayOutputStream {
    private final zzu zzar;

    public zzaa(zzu com_google_android_gms_internal_zzu, int i) {
        this.zzar = com_google_android_gms_internal_zzu;
        this.buf = this.zzar.zzb(Math.max(i, 256));
    }

    private void zzd(int i) {
        if (this.count + i > this.buf.length) {
            Object zzb = this.zzar.zzb((this.count + i) * 2);
            System.arraycopy(this.buf, 0, zzb, 0, this.count);
            this.zzar.zza(this.buf);
            this.buf = zzb;
        }
    }

    public void close() throws IOException {
        this.zzar.zza(this.buf);
        this.buf = null;
        super.close();
    }

    public void finalize() {
        this.zzar.zza(this.buf);
    }

    public synchronized void write(int oneByte) {
        zzd(1);
        super.write(oneByte);
    }

    public synchronized void write(byte[] buffer, int offset, int len) {
        zzd(len);
        super.write(buffer, offset, len);
    }
}
