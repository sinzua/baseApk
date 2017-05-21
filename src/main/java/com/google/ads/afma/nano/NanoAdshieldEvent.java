package com.google.ads.afma.nano;

import com.google.android.gms.internal.zzsm;
import com.google.android.gms.internal.zzsn;
import com.google.android.gms.internal.zzss;
import com.google.android.gms.internal.zzst;
import com.google.android.gms.internal.zzsu;
import com.google.android.gms.internal.zzsx;
import java.io.IOException;

public interface NanoAdshieldEvent {

    public static final class AdShieldEvent extends zzsu {
        private static volatile AdShieldEvent[] zzaK;
        public String appId;

        public AdShieldEvent() {
            clear();
        }

        public static AdShieldEvent[] emptyArray() {
            if (zzaK == null) {
                synchronized (zzss.zzbut) {
                    if (zzaK == null) {
                        zzaK = new AdShieldEvent[0];
                    }
                }
            }
            return zzaK;
        }

        public static AdShieldEvent parseFrom(zzsm input) throws IOException {
            return new AdShieldEvent().mergeFrom(input);
        }

        public static AdShieldEvent parseFrom(byte[] data) throws zzst {
            return (AdShieldEvent) zzsu.mergeFrom(new AdShieldEvent(), data);
        }

        public AdShieldEvent clear() {
            this.appId = "";
            this.zzbuu = -1;
            return this;
        }

        public AdShieldEvent mergeFrom(zzsm input) throws IOException {
            while (true) {
                int zzIX = input.zzIX();
                switch (zzIX) {
                    case 0:
                        break;
                    case 10:
                        this.appId = input.readString();
                        continue;
                    default:
                        if (!zzsx.zzb(input, zzIX)) {
                            break;
                        }
                        continue;
                }
                return this;
            }
        }

        public void writeTo(zzsn output) throws IOException {
            if (!this.appId.equals("")) {
                output.zzn(1, this.appId);
            }
            super.writeTo(output);
        }

        protected int zzz() {
            int zzz = super.zzz();
            return !this.appId.equals("") ? zzz + zzsn.zzo(1, this.appId) : zzz;
        }
    }
}
