package com.google.android.gms.internal;

import com.google.android.gms.common.internal.zzw;
import java.util.ArrayList;
import java.util.List;

@zzhb
public class zziv {
    private final String[] zzMn;
    private final double[] zzMo;
    private final double[] zzMp;
    private final int[] zzMq;
    private int zzMr;

    public static class zza {
        public final int count;
        public final String name;
        public final double zzMs;
        public final double zzMt;
        public final double zzMu;

        public zza(String str, double d, double d2, double d3, int i) {
            this.name = str;
            this.zzMt = d;
            this.zzMs = d2;
            this.zzMu = d3;
            this.count = i;
        }

        public boolean equals(Object other) {
            if (!(other instanceof zza)) {
                return false;
            }
            zza com_google_android_gms_internal_zziv_zza = (zza) other;
            return zzw.equal(this.name, com_google_android_gms_internal_zziv_zza.name) && this.zzMs == com_google_android_gms_internal_zziv_zza.zzMs && this.zzMt == com_google_android_gms_internal_zziv_zza.zzMt && this.count == com_google_android_gms_internal_zziv_zza.count && Double.compare(this.zzMu, com_google_android_gms_internal_zziv_zza.zzMu) == 0;
        }

        public int hashCode() {
            return zzw.hashCode(this.name, Double.valueOf(this.zzMs), Double.valueOf(this.zzMt), Double.valueOf(this.zzMu), Integer.valueOf(this.count));
        }

        public String toString() {
            return zzw.zzy(this).zzg("name", this.name).zzg("minBound", Double.valueOf(this.zzMt)).zzg("maxBound", Double.valueOf(this.zzMs)).zzg("percent", Double.valueOf(this.zzMu)).zzg("count", Integer.valueOf(this.count)).toString();
        }
    }

    public static class zzb {
        private final List<String> zzMv = new ArrayList();
        private final List<Double> zzMw = new ArrayList();
        private final List<Double> zzMx = new ArrayList();

        public zzb zza(String str, double d, double d2) {
            int i = 0;
            while (i < this.zzMv.size()) {
                double doubleValue = ((Double) this.zzMx.get(i)).doubleValue();
                double doubleValue2 = ((Double) this.zzMw.get(i)).doubleValue();
                if (d < doubleValue || (doubleValue == d && d2 < doubleValue2)) {
                    break;
                }
                i++;
            }
            this.zzMv.add(i, str);
            this.zzMx.add(i, Double.valueOf(d));
            this.zzMw.add(i, Double.valueOf(d2));
            return this;
        }

        public zziv zzhA() {
            return new zziv();
        }
    }

    private zziv(zzb com_google_android_gms_internal_zziv_zzb) {
        int size = com_google_android_gms_internal_zziv_zzb.zzMw.size();
        this.zzMn = (String[]) com_google_android_gms_internal_zziv_zzb.zzMv.toArray(new String[size]);
        this.zzMo = zzk(com_google_android_gms_internal_zziv_zzb.zzMw);
        this.zzMp = zzk(com_google_android_gms_internal_zziv_zzb.zzMx);
        this.zzMq = new int[size];
        this.zzMr = 0;
    }

    private double[] zzk(List<Double> list) {
        double[] dArr = new double[list.size()];
        for (int i = 0; i < dArr.length; i++) {
            dArr[i] = ((Double) list.get(i)).doubleValue();
        }
        return dArr;
    }

    public List<zza> getBuckets() {
        List<zza> arrayList = new ArrayList(this.zzMn.length);
        for (int i = 0; i < this.zzMn.length; i++) {
            arrayList.add(new zza(this.zzMn[i], this.zzMp[i], this.zzMo[i], ((double) this.zzMq[i]) / ((double) this.zzMr), this.zzMq[i]));
        }
        return arrayList;
    }

    public void zza(double d) {
        this.zzMr++;
        int i = 0;
        while (i < this.zzMp.length) {
            if (this.zzMp[i] <= d && d < this.zzMo[i]) {
                int[] iArr = this.zzMq;
                iArr[i] = iArr[i] + 1;
            }
            if (d >= this.zzMp[i]) {
                i++;
            } else {
                return;
            }
        }
    }
}
