package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import java.util.ArrayList;
import java.util.Iterator;

@zzhb
public class zzbc {
    private final Object zzpV = new Object();
    private final int zzsK;
    private final int zzsL;
    private final int zzsM;
    private final zzbh zzsN;
    private ArrayList<String> zzsO = new ArrayList();
    private ArrayList<String> zzsP = new ArrayList();
    private int zzsQ = 0;
    private int zzsR = 0;
    private int zzsS = 0;
    private int zzsT;
    private String zzsU = "";
    private String zzsV = "";

    public zzbc(int i, int i2, int i3, int i4) {
        this.zzsK = i;
        this.zzsL = i2;
        this.zzsM = i3;
        this.zzsN = new zzbh(i4);
    }

    private String zza(ArrayList<String> arrayList, int i) {
        if (arrayList.isEmpty()) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            stringBuffer.append((String) it.next());
            stringBuffer.append(' ');
            if (stringBuffer.length() > i) {
                break;
            }
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        String stringBuffer2 = stringBuffer.toString();
        return stringBuffer2.length() >= i ? stringBuffer2.substring(0, i) : stringBuffer2;
    }

    private void zze(String str, boolean z) {
        if (str != null && str.length() >= this.zzsM) {
            synchronized (this.zzpV) {
                this.zzsO.add(str);
                this.zzsQ += str.length();
                if (z) {
                    this.zzsP.add(str);
                }
            }
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof zzbc)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        zzbc com_google_android_gms_internal_zzbc = (zzbc) obj;
        return com_google_android_gms_internal_zzbc.zzcy() != null && com_google_android_gms_internal_zzbc.zzcy().equals(zzcy());
    }

    public int getScore() {
        return this.zzsT;
    }

    public int hashCode() {
        return zzcy().hashCode();
    }

    public String toString() {
        return "ActivityContent fetchId: " + this.zzsR + " score:" + this.zzsT + " total_length:" + this.zzsQ + "\n text: " + zza(this.zzsO, 100) + "\n viewableText" + zza(this.zzsP, 100) + "\n signture: " + this.zzsU + "\n viewableSignture: " + this.zzsV;
    }

    int zzb(int i, int i2) {
        return (this.zzsK * i) + (this.zzsL * i2);
    }

    public void zzc(String str, boolean z) {
        zze(str, z);
        synchronized (this.zzpV) {
            if (this.zzsS < 0) {
                zzb.zzaI("ActivityContent: negative number of WebViews.");
            }
            zzcD();
        }
    }

    public void zzcA() {
        synchronized (this.zzpV) {
            this.zzsT -= 100;
        }
    }

    public void zzcB() {
        synchronized (this.zzpV) {
            this.zzsS--;
        }
    }

    public void zzcC() {
        synchronized (this.zzpV) {
            this.zzsS++;
        }
    }

    public void zzcD() {
        synchronized (this.zzpV) {
            int zzb = zzb(this.zzsQ, this.zzsR);
            if (zzb > this.zzsT) {
                this.zzsT = zzb;
                this.zzsU = this.zzsN.zza(this.zzsO);
                this.zzsV = this.zzsN.zza(this.zzsP);
            }
        }
    }

    int zzcE() {
        return this.zzsQ;
    }

    public boolean zzcx() {
        boolean z;
        synchronized (this.zzpV) {
            z = this.zzsS == 0;
        }
        return z;
    }

    public String zzcy() {
        return this.zzsU;
    }

    public String zzcz() {
        return this.zzsV;
    }

    public void zzd(String str, boolean z) {
        zze(str, z);
    }

    public void zzh(int i) {
        this.zzsR = i;
    }
}
