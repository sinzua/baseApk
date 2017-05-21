package com.google.android.gms.internal;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.WindowManager;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.formats.zzh;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.zzr;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
public abstract class zzau implements OnGlobalLayoutListener, OnScrollChangedListener {
    protected final Object zzpV = new Object();
    private boolean zzqJ = false;
    private zziz zzrQ;
    private final WeakReference<zzif> zzrW;
    private WeakReference<ViewTreeObserver> zzrX;
    private final zzbb zzrY;
    protected final zzaw zzrZ;
    private final Context zzsa;
    private final WindowManager zzsb;
    private final PowerManager zzsc;
    private final KeyguardManager zzsd;
    private zzay zzse;
    private boolean zzsf;
    private boolean zzsg = false;
    private boolean zzsh;
    private boolean zzsi;
    private boolean zzsj;
    BroadcastReceiver zzsk;
    private final HashSet<zzav> zzsl = new HashSet();
    private final zzdf zzsm = new zzdf(this) {
        final /* synthetic */ zzau zzsp;

        {
            this.zzsp = r1;
        }

        public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
            if (this.zzsp.zzb((Map) map)) {
                this.zzsp.zza(com_google_android_gms_internal_zzjp.getView(), (Map) map);
            }
        }
    };
    private final zzdf zzsn = new zzdf(this) {
        final /* synthetic */ zzau zzsp;

        {
            this.zzsp = r1;
        }

        public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
            if (this.zzsp.zzb((Map) map)) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaI("Received request to untrack: " + this.zzsp.zzrZ.zzcu());
                this.zzsp.destroy();
            }
        }
    };
    private final zzdf zzso = new zzdf(this) {
        final /* synthetic */ zzau zzsp;

        {
            this.zzsp = r1;
        }

        public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
            if (this.zzsp.zzb((Map) map) && map.containsKey("isVisible")) {
                boolean z = "1".equals(map.get("isVisible")) || "true".equals(map.get("isVisible"));
                this.zzsp.zzg(Boolean.valueOf(z).booleanValue());
            }
        }
    };

    public static class zza implements zzbb {
        private WeakReference<zzh> zzsq;

        public zza(zzh com_google_android_gms_ads_internal_formats_zzh) {
            this.zzsq = new WeakReference(com_google_android_gms_ads_internal_formats_zzh);
        }

        public View zzco() {
            zzh com_google_android_gms_ads_internal_formats_zzh = (zzh) this.zzsq.get();
            return com_google_android_gms_ads_internal_formats_zzh != null ? com_google_android_gms_ads_internal_formats_zzh.zzdS() : null;
        }

        public boolean zzcp() {
            return this.zzsq.get() == null;
        }

        public zzbb zzcq() {
            return new zzb((zzh) this.zzsq.get());
        }
    }

    public static class zzb implements zzbb {
        private zzh zzsr;

        public zzb(zzh com_google_android_gms_ads_internal_formats_zzh) {
            this.zzsr = com_google_android_gms_ads_internal_formats_zzh;
        }

        public View zzco() {
            return this.zzsr.zzdS();
        }

        public boolean zzcp() {
            return this.zzsr == null;
        }

        public zzbb zzcq() {
            return this;
        }
    }

    public static class zzc implements zzbb {
        private final View mView;
        private final zzif zzss;

        public zzc(View view, zzif com_google_android_gms_internal_zzif) {
            this.mView = view;
            this.zzss = com_google_android_gms_internal_zzif;
        }

        public View zzco() {
            return this.mView;
        }

        public boolean zzcp() {
            return this.zzss == null || this.mView == null;
        }

        public zzbb zzcq() {
            return this;
        }
    }

    public static class zzd implements zzbb {
        private final WeakReference<View> zzst;
        private final WeakReference<zzif> zzsu;

        public zzd(View view, zzif com_google_android_gms_internal_zzif) {
            this.zzst = new WeakReference(view);
            this.zzsu = new WeakReference(com_google_android_gms_internal_zzif);
        }

        public View zzco() {
            return (View) this.zzst.get();
        }

        public boolean zzcp() {
            return this.zzst.get() == null || this.zzsu.get() == null;
        }

        public zzbb zzcq() {
            return new zzc((View) this.zzst.get(), (zzif) this.zzsu.get());
        }
    }

    public zzau(Context context, AdSizeParcel adSizeParcel, zzif com_google_android_gms_internal_zzif, VersionInfoParcel versionInfoParcel, zzbb com_google_android_gms_internal_zzbb) {
        this.zzrW = new WeakReference(com_google_android_gms_internal_zzif);
        this.zzrY = com_google_android_gms_internal_zzbb;
        this.zzrX = new WeakReference(null);
        this.zzsh = true;
        this.zzsj = false;
        this.zzrQ = new zziz(200);
        this.zzrZ = new zzaw(UUID.randomUUID().toString(), versionInfoParcel, adSizeParcel.zzuh, com_google_android_gms_internal_zzif.zzKT, com_google_android_gms_internal_zzif.zzcv(), adSizeParcel.zzuk);
        this.zzsb = (WindowManager) context.getSystemService("window");
        this.zzsc = (PowerManager) context.getApplicationContext().getSystemService("power");
        this.zzsd = (KeyguardManager) context.getSystemService("keyguard");
        this.zzsa = context;
    }

    protected void destroy() {
        synchronized (this.zzpV) {
            zzcj();
            zzce();
            this.zzsh = false;
            zzcg();
        }
    }

    boolean isScreenOn() {
        return this.zzsc.isScreenOn();
    }

    public void onGlobalLayout() {
        zzh(false);
    }

    public void onScrollChanged() {
        zzh(true);
    }

    public void pause() {
        synchronized (this.zzpV) {
            this.zzqJ = true;
            zzh(false);
        }
    }

    public void resume() {
        synchronized (this.zzpV) {
            this.zzqJ = false;
            zzh(false);
        }
    }

    public void stop() {
        synchronized (this.zzpV) {
            this.zzsg = true;
            zzh(false);
        }
    }

    protected int zza(int i, DisplayMetrics displayMetrics) {
        return (int) (((float) i) / displayMetrics.density);
    }

    protected void zza(View view, Map<String, String> map) {
        zzh(false);
    }

    public void zza(zzav com_google_android_gms_internal_zzav) {
        this.zzsl.add(com_google_android_gms_internal_zzav);
    }

    public void zza(zzay com_google_android_gms_internal_zzay) {
        synchronized (this.zzpV) {
            this.zzse = com_google_android_gms_internal_zzay;
        }
    }

    protected void zza(JSONObject jSONObject) {
        try {
            JSONArray jSONArray = new JSONArray();
            JSONObject jSONObject2 = new JSONObject();
            jSONArray.put(jSONObject);
            jSONObject2.put("units", jSONArray);
            zzb(jSONObject2);
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Skipping active view message.", th);
        }
    }

    protected void zzb(zzeh com_google_android_gms_internal_zzeh) {
        com_google_android_gms_internal_zzeh.zza("/updateActiveView", this.zzsm);
        com_google_android_gms_internal_zzeh.zza("/untrackActiveViewUnit", this.zzsn);
        com_google_android_gms_internal_zzeh.zza("/visibilityChanged", this.zzso);
    }

    protected abstract void zzb(JSONObject jSONObject);

    protected boolean zzb(Map<String, String> map) {
        if (map == null) {
            return false;
        }
        String str = (String) map.get("hashCode");
        boolean z = !TextUtils.isEmpty(str) && str.equals(this.zzrZ.zzcu());
        return z;
    }

    protected void zzc(zzeh com_google_android_gms_internal_zzeh) {
        com_google_android_gms_internal_zzeh.zzb("/visibilityChanged", this.zzso);
        com_google_android_gms_internal_zzeh.zzb("/untrackActiveViewUnit", this.zzsn);
        com_google_android_gms_internal_zzeh.zzb("/updateActiveView", this.zzsm);
    }

    protected void zzcd() {
        synchronized (this.zzpV) {
            if (this.zzsk != null) {
                return;
            }
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            this.zzsk = new BroadcastReceiver(this) {
                final /* synthetic */ zzau zzsp;

                {
                    this.zzsp = r1;
                }

                public void onReceive(Context context, Intent intent) {
                    this.zzsp.zzh(false);
                }
            };
            this.zzsa.registerReceiver(this.zzsk, intentFilter);
        }
    }

    protected void zzce() {
        synchronized (this.zzpV) {
            if (this.zzsk != null) {
                try {
                    this.zzsa.unregisterReceiver(this.zzsk);
                } catch (Throwable e) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzb("Failed trying to unregister the receiver", e);
                } catch (Throwable e2) {
                    zzr.zzbF().zzb(e2, true);
                }
                this.zzsk = null;
            }
        }
    }

    public void zzcf() {
        synchronized (this.zzpV) {
            if (this.zzsh) {
                this.zzsi = true;
                try {
                    zza(zzcn());
                } catch (Throwable e) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzb("JSON failure while processing active view data.", e);
                } catch (Throwable e2) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzb("Failure while processing active view data.", e2);
                }
                com.google.android.gms.ads.internal.util.client.zzb.zzaI("Untracking ad unit: " + this.zzrZ.zzcu());
            }
        }
    }

    protected void zzcg() {
        if (this.zzse != null) {
            this.zzse.zza(this);
        }
    }

    public boolean zzch() {
        boolean z;
        synchronized (this.zzpV) {
            z = this.zzsh;
        }
        return z;
    }

    protected void zzci() {
        View zzco = this.zzrY.zzcq().zzco();
        if (zzco != null) {
            ViewTreeObserver viewTreeObserver = (ViewTreeObserver) this.zzrX.get();
            ViewTreeObserver viewTreeObserver2 = zzco.getViewTreeObserver();
            if (viewTreeObserver2 != viewTreeObserver) {
                zzcj();
                if (!this.zzsf || (viewTreeObserver != null && viewTreeObserver.isAlive())) {
                    this.zzsf = true;
                    viewTreeObserver2.addOnScrollChangedListener(this);
                    viewTreeObserver2.addOnGlobalLayoutListener(this);
                }
                this.zzrX = new WeakReference(viewTreeObserver2);
            }
        }
    }

    protected void zzcj() {
        ViewTreeObserver viewTreeObserver = (ViewTreeObserver) this.zzrX.get();
        if (viewTreeObserver != null && viewTreeObserver.isAlive()) {
            viewTreeObserver.removeOnScrollChangedListener(this);
            viewTreeObserver.removeGlobalOnLayoutListener(this);
        }
    }

    protected JSONObject zzck() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("afmaVersion", this.zzrZ.zzcs()).put("activeViewJSON", this.zzrZ.zzct()).put("timestamp", zzr.zzbG().elapsedRealtime()).put("adFormat", this.zzrZ.zzcr()).put("hashCode", this.zzrZ.zzcu()).put("isMraid", this.zzrZ.zzcv()).put("isStopped", this.zzsg).put("isPaused", this.zzqJ).put("isScreenOn", isScreenOn()).put("isNative", this.zzrZ.zzcw());
        return jSONObject;
    }

    protected abstract boolean zzcl();

    protected JSONObject zzcm() throws JSONException {
        return zzck().put("isAttachedToWindow", false).put("isScreenOn", isScreenOn()).put("isVisible", false);
    }

    protected JSONObject zzcn() throws JSONException {
        JSONObject zzck = zzck();
        zzck.put("doneReasonCode", "u");
        return zzck;
    }

    protected JSONObject zzd(View view) throws JSONException {
        if (view == null) {
            return zzcm();
        }
        boolean isAttachedToWindow = zzr.zzbE().isAttachedToWindow(view);
        int[] iArr = new int[2];
        int[] iArr2 = new int[2];
        try {
            view.getLocationOnScreen(iArr);
            view.getLocationInWindow(iArr2);
        } catch (Throwable e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Failure getting view location.", e);
        }
        DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
        Rect rect = new Rect();
        rect.left = iArr[0];
        rect.top = iArr[1];
        rect.right = rect.left + view.getWidth();
        rect.bottom = rect.top + view.getHeight();
        Rect rect2 = new Rect();
        rect2.right = this.zzsb.getDefaultDisplay().getWidth();
        rect2.bottom = this.zzsb.getDefaultDisplay().getHeight();
        Rect rect3 = new Rect();
        boolean globalVisibleRect = view.getGlobalVisibleRect(rect3, null);
        Rect rect4 = new Rect();
        boolean localVisibleRect = view.getLocalVisibleRect(rect4);
        Rect rect5 = new Rect();
        view.getHitRect(rect5);
        JSONObject zzck = zzck();
        zzck.put("windowVisibility", view.getWindowVisibility()).put("isAttachedToWindow", isAttachedToWindow).put("viewBox", new JSONObject().put("top", zza(rect2.top, displayMetrics)).put("bottom", zza(rect2.bottom, displayMetrics)).put("left", zza(rect2.left, displayMetrics)).put("right", zza(rect2.right, displayMetrics))).put("adBox", new JSONObject().put("top", zza(rect.top, displayMetrics)).put("bottom", zza(rect.bottom, displayMetrics)).put("left", zza(rect.left, displayMetrics)).put("right", zza(rect.right, displayMetrics))).put("globalVisibleBox", new JSONObject().put("top", zza(rect3.top, displayMetrics)).put("bottom", zza(rect3.bottom, displayMetrics)).put("left", zza(rect3.left, displayMetrics)).put("right", zza(rect3.right, displayMetrics))).put("globalVisibleBoxVisible", globalVisibleRect).put("localVisibleBox", new JSONObject().put("top", zza(rect4.top, displayMetrics)).put("bottom", zza(rect4.bottom, displayMetrics)).put("left", zza(rect4.left, displayMetrics)).put("right", zza(rect4.right, displayMetrics))).put("localVisibleBoxVisible", localVisibleRect).put("hitBox", new JSONObject().put("top", zza(rect5.top, displayMetrics)).put("bottom", zza(rect5.bottom, displayMetrics)).put("left", zza(rect5.left, displayMetrics)).put("right", zza(rect5.right, displayMetrics))).put("screenDensity", (double) displayMetrics.density).put("isVisible", zzr.zzbC().zza(view, this.zzsc, this.zzsd));
        return zzck;
    }

    protected void zzg(boolean z) {
        Iterator it = this.zzsl.iterator();
        while (it.hasNext()) {
            ((zzav) it.next()).zza(this, z);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void zzh(boolean r6) {
        /*
        r5 = this;
        r1 = r5.zzpV;
        monitor-enter(r1);
        r0 = r5.zzcl();	 Catch:{ all -> 0x0042 }
        if (r0 == 0) goto L_0x000d;
    L_0x0009:
        r0 = r5.zzsh;	 Catch:{ all -> 0x0042 }
        if (r0 != 0) goto L_0x000f;
    L_0x000d:
        monitor-exit(r1);	 Catch:{ all -> 0x0042 }
    L_0x000e:
        return;
    L_0x000f:
        r0 = r5.zzrY;	 Catch:{ all -> 0x0042 }
        r2 = r0.zzco();	 Catch:{ all -> 0x0042 }
        if (r2 == 0) goto L_0x0045;
    L_0x0017:
        r0 = com.google.android.gms.ads.internal.zzr.zzbC();	 Catch:{ all -> 0x0042 }
        r3 = r5.zzsc;	 Catch:{ all -> 0x0042 }
        r4 = r5.zzsd;	 Catch:{ all -> 0x0042 }
        r0 = r0.zza(r2, r3, r4);	 Catch:{ all -> 0x0042 }
        if (r0 == 0) goto L_0x0045;
    L_0x0025:
        r0 = new android.graphics.Rect;	 Catch:{ all -> 0x0042 }
        r0.<init>();	 Catch:{ all -> 0x0042 }
        r3 = 0;
        r0 = r2.getGlobalVisibleRect(r0, r3);	 Catch:{ all -> 0x0042 }
        if (r0 == 0) goto L_0x0045;
    L_0x0031:
        r0 = 1;
    L_0x0032:
        if (r6 == 0) goto L_0x0047;
    L_0x0034:
        r3 = r5.zzrQ;	 Catch:{ all -> 0x0042 }
        r3 = r3.tryAcquire();	 Catch:{ all -> 0x0042 }
        if (r3 != 0) goto L_0x0047;
    L_0x003c:
        r3 = r5.zzsj;	 Catch:{ all -> 0x0042 }
        if (r0 != r3) goto L_0x0047;
    L_0x0040:
        monitor-exit(r1);	 Catch:{ all -> 0x0042 }
        goto L_0x000e;
    L_0x0042:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0042 }
        throw r0;
    L_0x0045:
        r0 = 0;
        goto L_0x0032;
    L_0x0047:
        r5.zzsj = r0;	 Catch:{ all -> 0x0042 }
        r0 = r5.zzrY;	 Catch:{ all -> 0x0042 }
        r0 = r0.zzcp();	 Catch:{ all -> 0x0042 }
        if (r0 == 0) goto L_0x0056;
    L_0x0051:
        r5.zzcf();	 Catch:{ all -> 0x0042 }
        monitor-exit(r1);	 Catch:{ all -> 0x0042 }
        goto L_0x000e;
    L_0x0056:
        r0 = r5.zzd(r2);	 Catch:{ JSONException -> 0x006c, RuntimeException -> 0x0065 }
        r5.zza(r0);	 Catch:{ JSONException -> 0x006c, RuntimeException -> 0x0065 }
    L_0x005d:
        r5.zzci();	 Catch:{ all -> 0x0042 }
        r5.zzcg();	 Catch:{ all -> 0x0042 }
        monitor-exit(r1);	 Catch:{ all -> 0x0042 }
        goto L_0x000e;
    L_0x0065:
        r0 = move-exception;
    L_0x0066:
        r2 = "Active view update failed.";
        com.google.android.gms.ads.internal.util.client.zzb.zza(r2, r0);	 Catch:{ all -> 0x0042 }
        goto L_0x005d;
    L_0x006c:
        r0 = move-exception;
        goto L_0x0066;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzau.zzh(boolean):void");
    }
}
