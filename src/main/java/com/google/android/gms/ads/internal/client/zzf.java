package com.google.android.gms.ads.internal.client;

import android.location.Location;
import android.os.Bundle;
import com.google.android.gms.internal.zzhb;
import java.util.ArrayList;
import java.util.List;

@zzhb
public final class zzf {
    private Bundle mExtras;
    private Location zzbj;
    private boolean zzpE;
    private long zztS;
    private int zztT;
    private List<String> zztU;
    private boolean zztV;
    private int zztW;
    private String zztX;
    private SearchAdRequestParcel zztY;
    private String zztZ;
    private Bundle zzua;
    private Bundle zzub;
    private List<String> zzuc;
    private String zzud;
    private String zzue;
    private boolean zzuf;

    public zzf() {
        this.zztS = -1;
        this.mExtras = new Bundle();
        this.zztT = -1;
        this.zztU = new ArrayList();
        this.zztV = false;
        this.zztW = -1;
        this.zzpE = false;
        this.zztX = null;
        this.zztY = null;
        this.zzbj = null;
        this.zztZ = null;
        this.zzua = new Bundle();
        this.zzub = new Bundle();
        this.zzuc = new ArrayList();
        this.zzud = null;
        this.zzue = null;
        this.zzuf = false;
    }

    public zzf(AdRequestParcel adRequestParcel) {
        this.zztS = adRequestParcel.zztC;
        this.mExtras = adRequestParcel.extras;
        this.zztT = adRequestParcel.zztD;
        this.zztU = adRequestParcel.zztE;
        this.zztV = adRequestParcel.zztF;
        this.zztW = adRequestParcel.zztG;
        this.zzpE = adRequestParcel.zztH;
        this.zztX = adRequestParcel.zztI;
        this.zztY = adRequestParcel.zztJ;
        this.zzbj = adRequestParcel.zztK;
        this.zztZ = adRequestParcel.zztL;
        this.zzua = adRequestParcel.zztM;
        this.zzub = adRequestParcel.zztN;
        this.zzuc = adRequestParcel.zztO;
        this.zzud = adRequestParcel.zztP;
        this.zzue = adRequestParcel.zztQ;
    }

    public zzf zza(Location location) {
        this.zzbj = location;
        return this;
    }

    public AdRequestParcel zzcN() {
        return new AdRequestParcel(7, this.zztS, this.mExtras, this.zztT, this.zztU, this.zztV, this.zztW, this.zzpE, this.zztX, this.zztY, this.zzbj, this.zztZ, this.zzua, this.zzub, this.zzuc, this.zzud, this.zzue, this.zzuf);
    }
}
