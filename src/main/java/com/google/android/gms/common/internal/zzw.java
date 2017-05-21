package com.google.android.gms.common.internal;

import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class zzw {

    public static final class zza {
        private final Object zzML;
        private final List<String> zzamp;

        private zza(Object obj) {
            this.zzML = zzx.zzz(obj);
            this.zzamp = new ArrayList();
        }

        public String toString() {
            StringBuilder append = new StringBuilder(100).append(this.zzML.getClass().getSimpleName()).append('{');
            int size = this.zzamp.size();
            for (int i = 0; i < size; i++) {
                append.append((String) this.zzamp.get(i));
                if (i < size - 1) {
                    append.append(", ");
                }
            }
            return append.append('}').toString();
        }

        public zza zzg(String str, Object obj) {
            this.zzamp.add(((String) zzx.zzz(str)) + RequestParameters.EQUAL + String.valueOf(obj));
            return this;
        }
    }

    public static boolean equal(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    public static int hashCode(Object... objects) {
        return Arrays.hashCode(objects);
    }

    public static zza zzy(Object obj) {
        return new zza(obj);
    }
}
