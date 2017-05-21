package com.google.android.gms.internal;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.MutableContextWrapper;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.overlay.zzd;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
class zzjt extends WebView implements OnGlobalLayoutListener, DownloadListener, zzjp {
    private AdSizeParcel zzCh;
    private int zzDC = -1;
    private int zzDD = -1;
    private int zzDF = -1;
    private int zzDG = -1;
    private String zzEY = "";
    private Boolean zzLB;
    private final zza zzNP;
    private zzjq zzNQ;
    private zzd zzNR;
    private boolean zzNS;
    private boolean zzNT;
    private boolean zzNU;
    private boolean zzNV;
    private int zzNW;
    private boolean zzNX = true;
    private zzbz zzNY;
    private zzbz zzNZ;
    private zzbz zzOa;
    private zzca zzOb;
    private WeakReference<OnClickListener> zzOc;
    private zzd zzOd;
    private Map<String, zzdr> zzOe;
    private final VersionInfoParcel zzpT;
    private final Object zzpV = new Object();
    private final com.google.android.gms.ads.internal.zzd zzpm;
    private zzjc zzrV;
    private final WindowManager zzsb;
    private final zzan zzyt;

    @zzhb
    public static class zza extends MutableContextWrapper {
        private Activity zzMM;
        private Context zzOg;
        private Context zzsa;

        public zza(Context context) {
            super(context);
            setBaseContext(context);
        }

        public Object getSystemService(String service) {
            return this.zzOg.getSystemService(service);
        }

        public void setBaseContext(Context base) {
            this.zzsa = base.getApplicationContext();
            this.zzMM = base instanceof Activity ? (Activity) base : null;
            this.zzOg = base;
            super.setBaseContext(this.zzsa);
        }

        public void startActivity(Intent intent) {
            if (this.zzMM == null || zzne.isAtLeastL()) {
                intent.setFlags(268435456);
                this.zzsa.startActivity(intent);
                return;
            }
            this.zzMM.startActivity(intent);
        }

        public Activity zzhP() {
            return this.zzMM;
        }

        public Context zzhQ() {
            return this.zzOg;
        }
    }

    protected zzjt(zza com_google_android_gms_internal_zzjt_zza, AdSizeParcel adSizeParcel, boolean z, boolean z2, zzan com_google_android_gms_internal_zzan, VersionInfoParcel versionInfoParcel, zzcb com_google_android_gms_internal_zzcb, com.google.android.gms.ads.internal.zzd com_google_android_gms_ads_internal_zzd) {
        super(com_google_android_gms_internal_zzjt_zza);
        this.zzNP = com_google_android_gms_internal_zzjt_zza;
        this.zzCh = adSizeParcel;
        this.zzNU = z;
        this.zzNW = -1;
        this.zzyt = com_google_android_gms_internal_zzan;
        this.zzpT = versionInfoParcel;
        this.zzpm = com_google_android_gms_ads_internal_zzd;
        this.zzsb = (WindowManager) getContext().getSystemService("window");
        setBackgroundColor(0);
        WebSettings settings = getSettings();
        settings.setAllowFileAccess(false);
        settings.setJavaScriptEnabled(true);
        settings.setSavePassword(false);
        settings.setSupportMultipleWindows(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        if (VERSION.SDK_INT >= 21) {
            settings.setMixedContentMode(0);
        }
        zzr.zzbC().zza((Context) com_google_android_gms_internal_zzjt_zza, versionInfoParcel.afmaVersion, settings);
        zzr.zzbE().zza(getContext(), settings);
        setDownloadListener(this);
        zziq();
        if (zzne.zzsi()) {
            addJavascriptInterface(new zzju(this), "googleAdsJsInterface");
        }
        this.zzrV = new zzjc(this.zzNP.zzhP(), this, null);
        zzd(com_google_android_gms_internal_zzcb);
    }

    static zzjt zzb(Context context, AdSizeParcel adSizeParcel, boolean z, boolean z2, zzan com_google_android_gms_internal_zzan, VersionInfoParcel versionInfoParcel, zzcb com_google_android_gms_internal_zzcb, com.google.android.gms.ads.internal.zzd com_google_android_gms_ads_internal_zzd) {
        return new zzjt(new zza(context), adSizeParcel, z, z2, com_google_android_gms_internal_zzan, versionInfoParcel, com_google_android_gms_internal_zzcb, com_google_android_gms_ads_internal_zzd);
    }

    private void zzd(zzcb com_google_android_gms_internal_zzcb) {
        zziu();
        this.zzOb = new zzca(new zzcb(true, "make_wv", this.zzCh.zzuh));
        this.zzOb.zzdA().zzc(com_google_android_gms_internal_zzcb);
        this.zzNZ = zzbx.zzb(this.zzOb.zzdA());
        this.zzOb.zza("native:view_create", this.zzNZ);
        this.zzOa = null;
        this.zzNY = null;
    }

    private void zzio() {
        synchronized (this.zzpV) {
            this.zzLB = zzr.zzbF().zzhg();
            if (this.zzLB == null) {
                try {
                    evaluateJavascript("(function(){})()", null);
                    zzb(Boolean.valueOf(true));
                } catch (IllegalStateException e) {
                    zzb(Boolean.valueOf(false));
                }
            }
        }
    }

    private void zzip() {
        zzbx.zza(this.zzOb.zzdA(), this.zzNY, "aeh");
    }

    private void zziq() {
        synchronized (this.zzpV) {
            if (this.zzNU || this.zzCh.zzui) {
                if (VERSION.SDK_INT < 14) {
                    zzb.zzaI("Disabling hardware acceleration on an overlay.");
                    zzir();
                } else {
                    zzb.zzaI("Enabling hardware acceleration on an overlay.");
                    zzis();
                }
            } else if (VERSION.SDK_INT < 18) {
                zzb.zzaI("Disabling hardware acceleration on an AdView.");
                zzir();
            } else {
                zzb.zzaI("Enabling hardware acceleration on an AdView.");
                zzis();
            }
        }
    }

    private void zzir() {
        synchronized (this.zzpV) {
            if (!this.zzNV) {
                zzr.zzbE().zzn(this);
            }
            this.zzNV = true;
        }
    }

    private void zzis() {
        synchronized (this.zzpV) {
            if (this.zzNV) {
                zzr.zzbE().zzm(this);
            }
            this.zzNV = false;
        }
    }

    private void zzit() {
        synchronized (this.zzpV) {
            if (this.zzOe != null) {
                for (zzdr release : this.zzOe.values()) {
                    release.release();
                }
            }
        }
    }

    private void zziu() {
        if (this.zzOb != null) {
            zzcb zzdA = this.zzOb.zzdA();
            if (zzdA != null && zzr.zzbF().zzhb() != null) {
                zzr.zzbF().zzhb().zza(zzdA);
            }
        }
    }

    public void destroy() {
        synchronized (this.zzpV) {
            zziu();
            this.zzrV.zzhF();
            if (this.zzNR != null) {
                this.zzNR.close();
                this.zzNR.onDestroy();
                this.zzNR = null;
            }
            this.zzNQ.reset();
            if (this.zzNT) {
                return;
            }
            zzr.zzbR().zzd(this);
            zzit();
            this.zzNT = true;
            zzin.v("Initiating WebView self destruct sequence in 3...");
            this.zzNQ.zzii();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @android.annotation.TargetApi(19)
    public void evaluateJavascript(java.lang.String r3, android.webkit.ValueCallback<java.lang.String> r4) {
        /*
        r2 = this;
        r1 = r2.zzpV;
        monitor-enter(r1);
        r0 = r2.isDestroyed();	 Catch:{ all -> 0x001b }
        if (r0 == 0) goto L_0x0016;
    L_0x0009:
        r0 = "The webview is destroyed. Ignoring action.";
        com.google.android.gms.ads.internal.util.client.zzb.zzaK(r0);	 Catch:{ all -> 0x001b }
        if (r4 == 0) goto L_0x0014;
    L_0x0010:
        r0 = 0;
        r4.onReceiveValue(r0);	 Catch:{ all -> 0x001b }
    L_0x0014:
        monitor-exit(r1);	 Catch:{ all -> 0x001b }
    L_0x0015:
        return;
    L_0x0016:
        super.evaluateJavascript(r3, r4);	 Catch:{ all -> 0x001b }
        monitor-exit(r1);	 Catch:{ all -> 0x001b }
        goto L_0x0015;
    L_0x001b:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x001b }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzjt.evaluateJavascript(java.lang.String, android.webkit.ValueCallback):void");
    }

    protected void finalize() throws Throwable {
        synchronized (this.zzpV) {
            if (!this.zzNT) {
                zzr.zzbR().zzd(this);
                zzit();
            }
        }
        super.finalize();
    }

    public String getRequestId() {
        String str;
        synchronized (this.zzpV) {
            str = this.zzEY;
        }
        return str;
    }

    public int getRequestedOrientation() {
        int i;
        synchronized (this.zzpV) {
            i = this.zzNW;
        }
        return i;
    }

    public View getView() {
        return this;
    }

    public WebView getWebView() {
        return this;
    }

    public boolean isDestroyed() {
        boolean z;
        synchronized (this.zzpV) {
            z = this.zzNT;
        }
        return z;
    }

    public void loadData(String data, String mimeType, String encoding) {
        synchronized (this.zzpV) {
            if (isDestroyed()) {
                zzb.zzaK("The webview is destroyed. Ignoring action.");
            } else {
                super.loadData(data, mimeType, encoding);
            }
        }
    }

    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        synchronized (this.zzpV) {
            if (isDestroyed()) {
                zzb.zzaK("The webview is destroyed. Ignoring action.");
            } else {
                super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
            }
        }
    }

    public void loadUrl(String uri) {
        synchronized (this.zzpV) {
            if (isDestroyed()) {
                zzb.zzaK("The webview is destroyed. Ignoring action.");
            } else {
                try {
                    super.loadUrl(uri);
                } catch (Throwable th) {
                    zzb.zzaK("Could not call loadUrl. " + th);
                }
            }
        }
    }

    protected void onAttachedToWindow() {
        synchronized (this.zzpV) {
            super.onAttachedToWindow();
            if (!isDestroyed()) {
                this.zzrV.onAttachedToWindow();
            }
        }
    }

    protected void onDetachedFromWindow() {
        synchronized (this.zzpV) {
            if (!isDestroyed()) {
                this.zzrV.onDetachedFromWindow();
            }
            super.onDetachedFromWindow();
        }
    }

    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long size) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(Uri.parse(url), mimeType);
            zzr.zzbC().zzb(getContext(), intent);
        } catch (ActivityNotFoundException e) {
            zzb.zzaI("Couldn't find an Activity to view url/mimetype: " + url + " / " + mimeType);
        }
    }

    @TargetApi(21)
    protected void onDraw(Canvas canvas) {
        if (!isDestroyed()) {
            if (VERSION.SDK_INT != 21 || !canvas.isHardwareAccelerated() || isAttachedToWindow()) {
                super.onDraw(canvas);
            }
        }
    }

    public void onGlobalLayout() {
        boolean zzin = zzin();
        zzd zzhS = zzhS();
        if (zzhS != null && zzin) {
            zzhS.zzfq();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void onMeasure(int r10, int r11) {
        /*
        r9 = this;
        r0 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r8 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r7 = 8;
        r6 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r4 = r9.zzpV;
        monitor-enter(r4);
        r1 = r9.isDestroyed();	 Catch:{ all -> 0x0034 }
        if (r1 == 0) goto L_0x0019;
    L_0x0012:
        r0 = 0;
        r1 = 0;
        r9.setMeasuredDimension(r0, r1);	 Catch:{ all -> 0x0034 }
        monitor-exit(r4);	 Catch:{ all -> 0x0034 }
    L_0x0018:
        return;
    L_0x0019:
        r1 = r9.isInEditMode();	 Catch:{ all -> 0x0034 }
        if (r1 != 0) goto L_0x002f;
    L_0x001f:
        r1 = r9.zzNU;	 Catch:{ all -> 0x0034 }
        if (r1 != 0) goto L_0x002f;
    L_0x0023:
        r1 = r9.zzCh;	 Catch:{ all -> 0x0034 }
        r1 = r1.zzuk;	 Catch:{ all -> 0x0034 }
        if (r1 != 0) goto L_0x002f;
    L_0x0029:
        r1 = r9.zzCh;	 Catch:{ all -> 0x0034 }
        r1 = r1.zzul;	 Catch:{ all -> 0x0034 }
        if (r1 == 0) goto L_0x0037;
    L_0x002f:
        super.onMeasure(r10, r11);	 Catch:{ all -> 0x0034 }
        monitor-exit(r4);	 Catch:{ all -> 0x0034 }
        goto L_0x0018;
    L_0x0034:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0034 }
        throw r0;
    L_0x0037:
        r1 = r9.zzCh;	 Catch:{ all -> 0x0034 }
        r1 = r1.zzui;	 Catch:{ all -> 0x0034 }
        if (r1 == 0) goto L_0x0054;
    L_0x003d:
        r0 = new android.util.DisplayMetrics;	 Catch:{ all -> 0x0034 }
        r0.<init>();	 Catch:{ all -> 0x0034 }
        r1 = r9.zzsb;	 Catch:{ all -> 0x0034 }
        r1 = r1.getDefaultDisplay();	 Catch:{ all -> 0x0034 }
        r1.getMetrics(r0);	 Catch:{ all -> 0x0034 }
        r1 = r0.widthPixels;	 Catch:{ all -> 0x0034 }
        r0 = r0.heightPixels;	 Catch:{ all -> 0x0034 }
        r9.setMeasuredDimension(r1, r0);	 Catch:{ all -> 0x0034 }
        monitor-exit(r4);	 Catch:{ all -> 0x0034 }
        goto L_0x0018;
    L_0x0054:
        r2 = android.view.View.MeasureSpec.getMode(r10);	 Catch:{ all -> 0x0034 }
        r3 = android.view.View.MeasureSpec.getSize(r10);	 Catch:{ all -> 0x0034 }
        r5 = android.view.View.MeasureSpec.getMode(r11);	 Catch:{ all -> 0x0034 }
        r1 = android.view.View.MeasureSpec.getSize(r11);	 Catch:{ all -> 0x0034 }
        if (r2 == r6) goto L_0x0068;
    L_0x0066:
        if (r2 != r8) goto L_0x00fd;
    L_0x0068:
        r2 = r3;
    L_0x0069:
        if (r5 == r6) goto L_0x006d;
    L_0x006b:
        if (r5 != r8) goto L_0x006e;
    L_0x006d:
        r0 = r1;
    L_0x006e:
        r5 = r9.zzCh;	 Catch:{ all -> 0x0034 }
        r5 = r5.widthPixels;	 Catch:{ all -> 0x0034 }
        if (r5 > r2) goto L_0x007a;
    L_0x0074:
        r2 = r9.zzCh;	 Catch:{ all -> 0x0034 }
        r2 = r2.heightPixels;	 Catch:{ all -> 0x0034 }
        if (r2 <= r0) goto L_0x00e7;
    L_0x007a:
        r0 = r9.zzNP;	 Catch:{ all -> 0x0034 }
        r0 = r0.getResources();	 Catch:{ all -> 0x0034 }
        r0 = r0.getDisplayMetrics();	 Catch:{ all -> 0x0034 }
        r0 = r0.density;	 Catch:{ all -> 0x0034 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0034 }
        r2.<init>();	 Catch:{ all -> 0x0034 }
        r5 = "Not enough space to show ad. Needs ";
        r2 = r2.append(r5);	 Catch:{ all -> 0x0034 }
        r5 = r9.zzCh;	 Catch:{ all -> 0x0034 }
        r5 = r5.widthPixels;	 Catch:{ all -> 0x0034 }
        r5 = (float) r5;	 Catch:{ all -> 0x0034 }
        r5 = r5 / r0;
        r5 = (int) r5;	 Catch:{ all -> 0x0034 }
        r2 = r2.append(r5);	 Catch:{ all -> 0x0034 }
        r5 = "x";
        r2 = r2.append(r5);	 Catch:{ all -> 0x0034 }
        r5 = r9.zzCh;	 Catch:{ all -> 0x0034 }
        r5 = r5.heightPixels;	 Catch:{ all -> 0x0034 }
        r5 = (float) r5;	 Catch:{ all -> 0x0034 }
        r5 = r5 / r0;
        r5 = (int) r5;	 Catch:{ all -> 0x0034 }
        r2 = r2.append(r5);	 Catch:{ all -> 0x0034 }
        r5 = " dp, but only has ";
        r2 = r2.append(r5);	 Catch:{ all -> 0x0034 }
        r3 = (float) r3;	 Catch:{ all -> 0x0034 }
        r3 = r3 / r0;
        r3 = (int) r3;	 Catch:{ all -> 0x0034 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0034 }
        r3 = "x";
        r2 = r2.append(r3);	 Catch:{ all -> 0x0034 }
        r1 = (float) r1;	 Catch:{ all -> 0x0034 }
        r0 = r1 / r0;
        r0 = (int) r0;	 Catch:{ all -> 0x0034 }
        r0 = r2.append(r0);	 Catch:{ all -> 0x0034 }
        r1 = " dp.";
        r0 = r0.append(r1);	 Catch:{ all -> 0x0034 }
        r0 = r0.toString();	 Catch:{ all -> 0x0034 }
        com.google.android.gms.ads.internal.util.client.zzb.zzaK(r0);	 Catch:{ all -> 0x0034 }
        r0 = r9.getVisibility();	 Catch:{ all -> 0x0034 }
        if (r0 == r7) goto L_0x00df;
    L_0x00db:
        r0 = 4;
        r9.setVisibility(r0);	 Catch:{ all -> 0x0034 }
    L_0x00df:
        r0 = 0;
        r1 = 0;
        r9.setMeasuredDimension(r0, r1);	 Catch:{ all -> 0x0034 }
    L_0x00e4:
        monitor-exit(r4);	 Catch:{ all -> 0x0034 }
        goto L_0x0018;
    L_0x00e7:
        r0 = r9.getVisibility();	 Catch:{ all -> 0x0034 }
        if (r0 == r7) goto L_0x00f1;
    L_0x00ed:
        r0 = 0;
        r9.setVisibility(r0);	 Catch:{ all -> 0x0034 }
    L_0x00f1:
        r0 = r9.zzCh;	 Catch:{ all -> 0x0034 }
        r0 = r0.widthPixels;	 Catch:{ all -> 0x0034 }
        r1 = r9.zzCh;	 Catch:{ all -> 0x0034 }
        r1 = r1.heightPixels;	 Catch:{ all -> 0x0034 }
        r9.setMeasuredDimension(r0, r1);	 Catch:{ all -> 0x0034 }
        goto L_0x00e4;
    L_0x00fd:
        r2 = r0;
        goto L_0x0069;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzjt.onMeasure(int, int):void");
    }

    public void onPause() {
        if (!isDestroyed()) {
            try {
                if (zzne.zzsd()) {
                    super.onPause();
                }
            } catch (Throwable e) {
                zzb.zzb("Could not pause webview.", e);
            }
        }
    }

    public void onResume() {
        if (!isDestroyed()) {
            try {
                if (zzne.zzsd()) {
                    super.onResume();
                }
            } catch (Throwable e) {
                zzb.zzb("Could not resume webview.", e);
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.zzyt != null) {
            this.zzyt.zza(event);
        }
        return isDestroyed() ? false : super.onTouchEvent(event);
    }

    public void setContext(Context context) {
        this.zzNP.setBaseContext(context);
        this.zzrV.zzi(this.zzNP.zzhP());
    }

    public void setOnClickListener(OnClickListener listener) {
        this.zzOc = new WeakReference(listener);
        super.setOnClickListener(listener);
    }

    public void setRequestedOrientation(int requestedOrientation) {
        synchronized (this.zzpV) {
            this.zzNW = requestedOrientation;
            if (this.zzNR != null) {
                this.zzNR.setRequestedOrientation(this.zzNW);
            }
        }
    }

    public void setWebViewClient(WebViewClient webViewClient) {
        super.setWebViewClient(webViewClient);
        if (webViewClient instanceof zzjq) {
            this.zzNQ = (zzjq) webViewClient;
        }
    }

    public void stopLoading() {
        if (!isDestroyed()) {
            try {
                super.stopLoading();
            } catch (Throwable e) {
                zzb.zzb("Could not stop loading webview.", e);
            }
        }
    }

    public void zzD(boolean z) {
        synchronized (this.zzpV) {
            this.zzNU = z;
            zziq();
        }
    }

    public void zzE(boolean z) {
        synchronized (this.zzpV) {
            if (this.zzNR != null) {
                this.zzNR.zza(this.zzNQ.zzcv(), z);
            } else {
                this.zzNS = z;
            }
        }
    }

    public void zzF(boolean z) {
        synchronized (this.zzpV) {
            this.zzNX = z;
        }
    }

    public void zza(Context context, AdSizeParcel adSizeParcel, zzcb com_google_android_gms_internal_zzcb) {
        synchronized (this.zzpV) {
            this.zzrV.zzhF();
            setContext(context);
            this.zzNR = null;
            this.zzCh = adSizeParcel;
            this.zzNU = false;
            this.zzNS = false;
            this.zzEY = "";
            this.zzNW = -1;
            zzr.zzbE().zzj(this);
            loadUrl("about:blank");
            this.zzNQ.reset();
            setOnTouchListener(null);
            setOnClickListener(null);
            this.zzNX = true;
            zzd(com_google_android_gms_internal_zzcb);
        }
    }

    public void zza(AdSizeParcel adSizeParcel) {
        synchronized (this.zzpV) {
            this.zzCh = adSizeParcel;
            requestLayout();
        }
    }

    public void zza(zzau com_google_android_gms_internal_zzau, boolean z) {
        Map hashMap = new HashMap();
        hashMap.put("isVisible", z ? "1" : "0");
        zza("onAdVisibilityChanged", hashMap);
    }

    @TargetApi(19)
    protected void zza(String str, ValueCallback<String> valueCallback) {
        synchronized (this.zzpV) {
            if (isDestroyed()) {
                zzb.zzaK("The webview is destroyed. Ignoring action.");
                if (valueCallback != null) {
                    valueCallback.onReceiveValue(null);
                }
            } else {
                evaluateJavascript(str, valueCallback);
            }
        }
    }

    public void zza(String str, zzdf com_google_android_gms_internal_zzdf) {
        if (this.zzNQ != null) {
            this.zzNQ.zza(str, com_google_android_gms_internal_zzdf);
        }
    }

    public void zza(String str, Map<String, ?> map) {
        try {
            zzb(str, zzr.zzbC().zzG(map));
        } catch (JSONException e) {
            zzb.zzaK("Could not convert parameters to JSON.");
        }
    }

    public void zza(String str, JSONObject jSONObject) {
        if (jSONObject == null) {
            jSONObject = new JSONObject();
        }
        zze(str, jSONObject.toString());
    }

    public void zzaL(String str) {
        synchronized (this.zzpV) {
            try {
                super.loadUrl(str);
            } catch (Throwable th) {
                zzb.zzaK("Could not call loadUrl. " + th);
            }
        }
    }

    public void zzaM(String str) {
        synchronized (this.zzpV) {
            if (str == null) {
                str = "";
            }
            this.zzEY = str;
        }
    }

    public AdSizeParcel zzaN() {
        AdSizeParcel adSizeParcel;
        synchronized (this.zzpV) {
            adSizeParcel = this.zzCh;
        }
        return adSizeParcel;
    }

    protected void zzaO(String str) {
        synchronized (this.zzpV) {
            if (isDestroyed()) {
                zzb.zzaK("The webview is destroyed. Ignoring action.");
            } else {
                loadUrl(str);
            }
        }
    }

    protected void zzaP(String str) {
        if (zzne.zzsk()) {
            if (zzhg() == null) {
                zzio();
            }
            if (zzhg().booleanValue()) {
                zza(str, null);
                return;
            } else {
                zzaO("javascript:" + str);
                return;
            }
        }
        zzaO("javascript:" + str);
    }

    public void zzb(zzd com_google_android_gms_ads_internal_overlay_zzd) {
        synchronized (this.zzpV) {
            this.zzNR = com_google_android_gms_ads_internal_overlay_zzd;
        }
    }

    void zzb(Boolean bool) {
        this.zzLB = bool;
        zzr.zzbF().zzb(bool);
    }

    public void zzb(String str, zzdf com_google_android_gms_internal_zzdf) {
        if (this.zzNQ != null) {
            this.zzNQ.zzb(str, com_google_android_gms_internal_zzdf);
        }
    }

    public void zzb(String str, JSONObject jSONObject) {
        if (jSONObject == null) {
            jSONObject = new JSONObject();
        }
        String jSONObject2 = jSONObject.toString();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AFMA_ReceiveMessage('");
        stringBuilder.append(str);
        stringBuilder.append("'");
        stringBuilder.append(",");
        stringBuilder.append(jSONObject2);
        stringBuilder.append(");");
        zzin.v("Dispatching AFMA event: " + stringBuilder.toString());
        zzaP(stringBuilder.toString());
    }

    public void zzc(zzd com_google_android_gms_ads_internal_overlay_zzd) {
        synchronized (this.zzpV) {
            this.zzOd = com_google_android_gms_ads_internal_overlay_zzd;
        }
    }

    public void zze(String str, String str2) {
        zzaP(str + "(" + str2 + ");");
    }

    public boolean zzfL() {
        boolean z;
        synchronized (this.zzpV) {
            zzbx.zza(this.zzOb.zzdA(), this.zzNY, "aebb");
            z = this.zzNX;
        }
        return z;
    }

    public void zzfr() {
        if (this.zzNY == null) {
            zzbx.zza(this.zzOb.zzdA(), this.zzOa, "aes");
            this.zzNY = zzbx.zzb(this.zzOb.zzdA());
            this.zzOb.zza("native:view_show", this.zzNY);
        }
        Map hashMap = new HashMap(1);
        hashMap.put("version", this.zzpT.afmaVersion);
        zza("onshow", hashMap);
    }

    public void zzhN() {
        zzip();
        Map hashMap = new HashMap(1);
        hashMap.put("version", this.zzpT.afmaVersion);
        zza("onhide", hashMap);
    }

    public void zzhO() {
        Map hashMap = new HashMap(2);
        hashMap.put("app_volume", String.valueOf(zzr.zzbC().zzbt()));
        hashMap.put("device_volume", String.valueOf(zzr.zzbC().zzQ(getContext())));
        zza("volume", hashMap);
    }

    public Activity zzhP() {
        return this.zzNP.zzhP();
    }

    public Context zzhQ() {
        return this.zzNP.zzhQ();
    }

    public com.google.android.gms.ads.internal.zzd zzhR() {
        return this.zzpm;
    }

    public zzd zzhS() {
        zzd com_google_android_gms_ads_internal_overlay_zzd;
        synchronized (this.zzpV) {
            com_google_android_gms_ads_internal_overlay_zzd = this.zzNR;
        }
        return com_google_android_gms_ads_internal_overlay_zzd;
    }

    public zzd zzhT() {
        zzd com_google_android_gms_ads_internal_overlay_zzd;
        synchronized (this.zzpV) {
            com_google_android_gms_ads_internal_overlay_zzd = this.zzOd;
        }
        return com_google_android_gms_ads_internal_overlay_zzd;
    }

    public zzjq zzhU() {
        return this.zzNQ;
    }

    public boolean zzhV() {
        return this.zzNS;
    }

    public zzan zzhW() {
        return this.zzyt;
    }

    public VersionInfoParcel zzhX() {
        return this.zzpT;
    }

    public boolean zzhY() {
        boolean z;
        synchronized (this.zzpV) {
            z = this.zzNU;
        }
        return z;
    }

    public void zzhZ() {
        synchronized (this.zzpV) {
            zzin.v("Destroying WebView!");
            zzir.zzMc.post(new Runnable(this) {
                final /* synthetic */ zzjt zzOf;

                {
                    this.zzOf = r1;
                }

                public void run() {
                    super.destroy();
                }
            });
        }
    }

    Boolean zzhg() {
        Boolean bool;
        synchronized (this.zzpV) {
            bool = this.zzLB;
        }
        return bool;
    }

    public zzjo zzia() {
        return null;
    }

    public zzbz zzib() {
        return this.zzOa;
    }

    public zzca zzic() {
        return this.zzOb;
    }

    public void zzid() {
        this.zzrV.zzhE();
    }

    public void zzie() {
        if (this.zzOa == null) {
            this.zzOa = zzbx.zzb(this.zzOb.zzdA());
            this.zzOb.zza("native:view_load", this.zzOa);
        }
    }

    public OnClickListener zzif() {
        return (OnClickListener) this.zzOc.get();
    }

    public boolean zzin() {
        if (!zzhU().zzcv()) {
            return false;
        }
        int i;
        int i2;
        DisplayMetrics zza = zzr.zzbC().zza(this.zzsb);
        int zzb = zzn.zzcS().zzb(zza, zza.widthPixels);
        int zzb2 = zzn.zzcS().zzb(zza, zza.heightPixels);
        Activity zzhP = zzhP();
        if (zzhP == null || zzhP.getWindow() == null) {
            i = zzb2;
            i2 = zzb;
        } else {
            int[] zze = zzr.zzbC().zze(zzhP);
            i2 = zzn.zzcS().zzb(zza, zze[0]);
            i = zzn.zzcS().zzb(zza, zze[1]);
        }
        if (this.zzDC == zzb && this.zzDD == zzb2 && this.zzDF == i2 && this.zzDG == i) {
            return false;
        }
        boolean z = (this.zzDC == zzb && this.zzDD == zzb2) ? false : true;
        this.zzDC = zzb;
        this.zzDD = zzb2;
        this.zzDF = i2;
        this.zzDG = i;
        new zzfs(this).zza(zzb, zzb2, i2, i, zza.density, this.zzsb.getDefaultDisplay().getRotation());
        return z;
    }

    public void zzy(int i) {
        zzip();
        Map hashMap = new HashMap(2);
        hashMap.put("closetype", String.valueOf(i));
        hashMap.put("version", this.zzpT.afmaVersion);
        zza("onhide", hashMap);
    }
}
