package com.google.android.gms.ads.internal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.util.SimpleArrayMap;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.ViewSwitcher;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.client.zzp;
import com.google.android.gms.ads.internal.client.zzq;
import com.google.android.gms.ads.internal.client.zzw;
import com.google.android.gms.ads.internal.client.zzx;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.purchase.zzk;
import com.google.android.gms.ads.internal.reward.client.zzd;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzan;
import com.google.android.gms.internal.zzbt;
import com.google.android.gms.internal.zzcf;
import com.google.android.gms.internal.zzcr;
import com.google.android.gms.internal.zzcs;
import com.google.android.gms.internal.zzct;
import com.google.android.gms.internal.zzcu;
import com.google.android.gms.internal.zzgd;
import com.google.android.gms.internal.zzgh;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzif;
import com.google.android.gms.internal.zzig;
import com.google.android.gms.internal.zzik;
import com.google.android.gms.internal.zzim;
import com.google.android.gms.internal.zzin;
import com.google.android.gms.internal.zzit;
import com.google.android.gms.internal.zziu;
import com.google.android.gms.internal.zziz;
import com.google.android.gms.internal.zzjc;
import com.google.android.gms.internal.zzjp;
import com.google.android.gms.internal.zzjq;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@zzhb
public final class zzs implements OnGlobalLayoutListener, OnScrollChangedListener {
    public final Context context;
    boolean zzql;
    zzcs zzrA;
    SimpleArrayMap<String, zzct> zzrB;
    SimpleArrayMap<String, zzcu> zzrC;
    NativeAdOptionsParcel zzrD;
    zzcf zzrE;
    @Nullable
    zzd zzrF;
    @Nullable
    private String zzrG;
    List<String> zzrH;
    zzk zzrI;
    public zzik zzrJ;
    View zzrK;
    public int zzrL;
    boolean zzrM;
    private HashSet<zzig> zzrN;
    private int zzrO;
    private int zzrP;
    private zziz zzrQ;
    private boolean zzrR;
    private boolean zzrS;
    private boolean zzrT;
    final String zzri;
    public String zzrj;
    final zzan zzrk;
    public final VersionInfoParcel zzrl;
    zza zzrm;
    public zzim zzrn;
    public zzit zzro;
    public AdSizeParcel zzrp;
    public zzif zzrq;
    public com.google.android.gms.internal.zzif.zza zzrr;
    public zzig zzrs;
    zzp zzrt;
    zzq zzru;
    zzw zzrv;
    zzx zzrw;
    zzgd zzrx;
    zzgh zzry;
    zzcr zzrz;

    public static class zza extends ViewSwitcher {
        private final zziu zzrU;
        private final zzjc zzrV;

        public zza(Context context, OnGlobalLayoutListener onGlobalLayoutListener, OnScrollChangedListener onScrollChangedListener) {
            super(context);
            this.zzrU = new zziu(context);
            if (context instanceof Activity) {
                this.zzrV = new zzjc((Activity) context, onGlobalLayoutListener, onScrollChangedListener);
                this.zzrV.zzhE();
                return;
            }
            this.zzrV = null;
        }

        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (this.zzrV != null) {
                this.zzrV.onAttachedToWindow();
            }
        }

        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            if (this.zzrV != null) {
                this.zzrV.onDetachedFromWindow();
            }
        }

        public boolean onInterceptTouchEvent(MotionEvent event) {
            this.zzrU.zze(event);
            return false;
        }

        public void removeAllViews() {
            List<zzjp> arrayList = new ArrayList();
            for (int i = 0; i < getChildCount(); i++) {
                View childAt = getChildAt(i);
                if (childAt != null && (childAt instanceof zzjp)) {
                    arrayList.add((zzjp) childAt);
                }
            }
            super.removeAllViews();
            for (zzjp destroy : arrayList) {
                destroy.destroy();
            }
        }

        public void zzbY() {
            zzin.v("Disable position monitoring on adFrame.");
            if (this.zzrV != null) {
                this.zzrV.zzhF();
            }
        }

        public zziu zzcc() {
            return this.zzrU;
        }
    }

    public zzs(Context context, AdSizeParcel adSizeParcel, String str, VersionInfoParcel versionInfoParcel) {
        this(context, adSizeParcel, str, versionInfoParcel, null);
    }

    zzs(Context context, AdSizeParcel adSizeParcel, String str, VersionInfoParcel versionInfoParcel, zzan com_google_android_gms_internal_zzan) {
        this.zzrJ = null;
        this.zzrK = null;
        this.zzrL = 0;
        this.zzrM = false;
        this.zzql = false;
        this.zzrN = null;
        this.zzrO = -1;
        this.zzrP = -1;
        this.zzrR = true;
        this.zzrS = true;
        this.zzrT = false;
        zzbt.initialize(context);
        if (zzr.zzbF().zzhb() != null) {
            List zzds = zzbt.zzds();
            if (versionInfoParcel.zzMZ != 0) {
                zzds.add(Integer.toString(versionInfoParcel.zzMZ));
            }
            zzr.zzbF().zzhb().zzb(zzds);
        }
        this.zzri = UUID.randomUUID().toString();
        if (adSizeParcel.zzui || adSizeParcel.zzuk) {
            this.zzrm = null;
        } else {
            this.zzrm = new zza(context, this, this);
            this.zzrm.setMinimumWidth(adSizeParcel.widthPixels);
            this.zzrm.setMinimumHeight(adSizeParcel.heightPixels);
            this.zzrm.setVisibility(4);
        }
        this.zzrp = adSizeParcel;
        this.zzrj = str;
        this.context = context;
        this.zzrl = versionInfoParcel;
        if (com_google_android_gms_internal_zzan == null) {
            com_google_android_gms_internal_zzan = new zzan(new zzh(this));
        }
        this.zzrk = com_google_android_gms_internal_zzan;
        this.zzrQ = new zziz(200);
        this.zzrC = new SimpleArrayMap();
    }

    private void zzbZ() {
        View findViewById = this.zzrm.getRootView().findViewById(16908290);
        if (findViewById != null) {
            Rect rect = new Rect();
            Rect rect2 = new Rect();
            this.zzrm.getGlobalVisibleRect(rect);
            findViewById.getGlobalVisibleRect(rect2);
            if (rect.top != rect2.top) {
                this.zzrR = false;
            }
            if (rect.bottom != rect2.bottom) {
                this.zzrS = false;
            }
        }
    }

    private void zze(boolean z) {
        boolean z2 = true;
        if (this.zzrm != null && this.zzrq != null && this.zzrq.zzED != null) {
            if (!z || this.zzrQ.tryAcquire()) {
                if (this.zzrq.zzED.zzhU().zzcv()) {
                    int[] iArr = new int[2];
                    this.zzrm.getLocationOnScreen(iArr);
                    int zzc = zzn.zzcS().zzc(this.context, iArr[0]);
                    int zzc2 = zzn.zzcS().zzc(this.context, iArr[1]);
                    if (!(zzc == this.zzrO && zzc2 == this.zzrP)) {
                        this.zzrO = zzc;
                        this.zzrP = zzc2;
                        zzjq zzhU = this.zzrq.zzED.zzhU();
                        zzc = this.zzrO;
                        int i = this.zzrP;
                        if (z) {
                            z2 = false;
                        }
                        zzhU.zza(zzc, i, z2);
                    }
                }
                zzbZ();
            }
        }
    }

    public void destroy() {
        zzbY();
        this.zzru = null;
        this.zzrv = null;
        this.zzry = null;
        this.zzrx = null;
        this.zzrE = null;
        this.zzrw = null;
        zzf(false);
        if (this.zzrm != null) {
            this.zzrm.removeAllViews();
        }
        zzbT();
        zzbV();
        this.zzrq = null;
    }

    public String getUserId() {
        return this.zzrG;
    }

    public void onGlobalLayout() {
        zze(false);
    }

    public void onScrollChanged() {
        zze(true);
        this.zzrT = true;
    }

    void setUserId(String userId) {
        this.zzrG = userId;
    }

    public void zza(HashSet<zzig> hashSet) {
        this.zzrN = hashSet;
    }

    public HashSet<zzig> zzbS() {
        return this.zzrN;
    }

    public void zzbT() {
        if (this.zzrq != null && this.zzrq.zzED != null) {
            this.zzrq.zzED.destroy();
        }
    }

    public void zzbU() {
        if (this.zzrq != null && this.zzrq.zzED != null) {
            this.zzrq.zzED.stopLoading();
        }
    }

    public void zzbV() {
        if (this.zzrq != null && this.zzrq.zzCq != null) {
            try {
                this.zzrq.zzCq.destroy();
            } catch (RemoteException e) {
                zzb.zzaK("Could not destroy mediation adapter.");
            }
        }
    }

    public boolean zzbW() {
        return this.zzrL == 0;
    }

    public boolean zzbX() {
        return this.zzrL == 1;
    }

    public void zzbY() {
        if (this.zzrm != null) {
            this.zzrm.zzbY();
        }
    }

    public String zzca() {
        return (this.zzrR && this.zzrS) ? "" : this.zzrR ? this.zzrT ? "top-scrollable" : "top-locked" : this.zzrS ? this.zzrT ? "bottom-scrollable" : "bottom-locked" : "";
    }

    public void zzcb() {
        this.zzrs.zzl(this.zzrq.zzKY);
        this.zzrs.zzm(this.zzrq.zzKZ);
        this.zzrs.zzz(this.zzrp.zzui);
        this.zzrs.zzA(this.zzrq.zzHT);
    }

    public void zzf(boolean z) {
        if (this.zzrL == 0) {
            zzbU();
        }
        if (this.zzrn != null) {
            this.zzrn.cancel();
        }
        if (this.zzro != null) {
            this.zzro.cancel();
        }
        if (z) {
            this.zzrq = null;
        }
    }
}
