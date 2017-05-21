package com.google.android.gms.internal;

import java.io.IOException;

public abstract class zzso<M extends zzso<M>> extends zzsu {
    protected zzsq zzbuj;

    public /* synthetic */ zzsu clone() throws CloneNotSupportedException {
        return zzJp();
    }

    public /* synthetic */ Object m0clone() throws CloneNotSupportedException {
        return zzJp();
    }

    public void writeTo(zzsn output) throws IOException {
        if (this.zzbuj != null) {
            for (int i = 0; i < this.zzbuj.size(); i++) {
                this.zzbuj.zzmG(i).writeTo(output);
            }
        }
    }

    public M zzJp() throws CloneNotSupportedException {
        zzso com_google_android_gms_internal_zzso = (zzso) super.clone();
        zzss.zza(this, com_google_android_gms_internal_zzso);
        return com_google_android_gms_internal_zzso;
    }

    public final <T> T zza(zzsp<M, T> com_google_android_gms_internal_zzsp_M__T) {
        if (this.zzbuj == null) {
            return null;
        }
        zzsr zzmF = this.zzbuj.zzmF(zzsx.zzmJ(com_google_android_gms_internal_zzsp_M__T.tag));
        return zzmF != null ? zzmF.zzb(com_google_android_gms_internal_zzsp_M__T) : null;
    }

    protected final boolean zza(zzsm com_google_android_gms_internal_zzsm, int i) throws IOException {
        int position = com_google_android_gms_internal_zzsm.getPosition();
        if (!com_google_android_gms_internal_zzsm.zzmo(i)) {
            return false;
        }
        int zzmJ = zzsx.zzmJ(i);
        zzsw com_google_android_gms_internal_zzsw = new zzsw(i, com_google_android_gms_internal_zzsm.zzz(position, com_google_android_gms_internal_zzsm.getPosition() - position));
        zzsr com_google_android_gms_internal_zzsr = null;
        if (this.zzbuj == null) {
            this.zzbuj = new zzsq();
        } else {
            com_google_android_gms_internal_zzsr = this.zzbuj.zzmF(zzmJ);
        }
        if (com_google_android_gms_internal_zzsr == null) {
            com_google_android_gms_internal_zzsr = new zzsr();
            this.zzbuj.zza(zzmJ, com_google_android_gms_internal_zzsr);
        }
        com_google_android_gms_internal_zzsr.zza(com_google_android_gms_internal_zzsw);
        return true;
    }

    protected int zzz() {
        int i = 0;
        if (this.zzbuj == null) {
            return 0;
        }
        int i2 = 0;
        while (i < this.zzbuj.size()) {
            i2 += this.zzbuj.zzmG(i).zzz();
            i++;
        }
        return i2;
    }
}
