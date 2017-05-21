package com.google.android.gms.internal;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View.MeasureSpec;
import android.webkit.WebView;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import com.supersonicads.sdk.precache.DownloadManager;

@zzhb
public class zzgo implements Runnable {
    private final Handler zzGj;
    private final long zzGk;
    private long zzGl;
    private com.google.android.gms.internal.zzjq.zza zzGm;
    protected boolean zzGn;
    protected boolean zzGo;
    private final int zzoG;
    private final int zzoH;
    protected final zzjp zzpD;

    protected final class zza extends AsyncTask<Void, Void, Boolean> {
        private final WebView zzGp;
        private Bitmap zzGq;
        final /* synthetic */ zzgo zzGr;

        public zza(zzgo com_google_android_gms_internal_zzgo, WebView webView) {
            this.zzGr = com_google_android_gms_internal_zzgo;
            this.zzGp = webView;
        }

        protected /* synthetic */ Object doInBackground(Object[] objArr) {
            return zza((Void[]) objArr);
        }

        protected /* synthetic */ void onPostExecute(Object obj) {
            zza((Boolean) obj);
        }

        protected synchronized void onPreExecute() {
            this.zzGq = Bitmap.createBitmap(this.zzGr.zzoG, this.zzGr.zzoH, Config.ARGB_8888);
            this.zzGp.setVisibility(0);
            this.zzGp.measure(MeasureSpec.makeMeasureSpec(this.zzGr.zzoG, 0), MeasureSpec.makeMeasureSpec(this.zzGr.zzoH, 0));
            this.zzGp.layout(0, 0, this.zzGr.zzoG, this.zzGr.zzoH);
            this.zzGp.draw(new Canvas(this.zzGq));
            this.zzGp.invalidate();
        }

        protected synchronized Boolean zza(Void... voidArr) {
            Boolean valueOf;
            int width = this.zzGq.getWidth();
            int height = this.zzGq.getHeight();
            if (width == 0 || height == 0) {
                valueOf = Boolean.valueOf(false);
            } else {
                int i = 0;
                for (int i2 = 0; i2 < width; i2 += 10) {
                    for (int i3 = 0; i3 < height; i3 += 10) {
                        if (this.zzGq.getPixel(i2, i3) != 0) {
                            i++;
                        }
                    }
                }
                valueOf = Boolean.valueOf(((double) i) / (((double) (width * height)) / 100.0d) > 0.1d);
            }
            return valueOf;
        }

        protected void zza(Boolean bool) {
            zzgo.zzc(this.zzGr);
            if (bool.booleanValue() || this.zzGr.zzgg() || this.zzGr.zzGl <= 0) {
                this.zzGr.zzGo = bool.booleanValue();
                this.zzGr.zzGm.zza(this.zzGr.zzpD, true);
            } else if (this.zzGr.zzGl > 0) {
                if (zzb.zzQ(2)) {
                    zzb.zzaI("Ad not detected, scheduling another run.");
                }
                this.zzGr.zzGj.postDelayed(this.zzGr, this.zzGr.zzGk);
            }
        }
    }

    public zzgo(com.google.android.gms.internal.zzjq.zza com_google_android_gms_internal_zzjq_zza, zzjp com_google_android_gms_internal_zzjp, int i, int i2) {
        this(com_google_android_gms_internal_zzjq_zza, com_google_android_gms_internal_zzjp, i, i2, 200, 50);
    }

    public zzgo(com.google.android.gms.internal.zzjq.zza com_google_android_gms_internal_zzjq_zza, zzjp com_google_android_gms_internal_zzjp, int i, int i2, long j, long j2) {
        this.zzGk = j;
        this.zzGl = j2;
        this.zzGj = new Handler(Looper.getMainLooper());
        this.zzpD = com_google_android_gms_internal_zzjp;
        this.zzGm = com_google_android_gms_internal_zzjq_zza;
        this.zzGn = false;
        this.zzGo = false;
        this.zzoH = i2;
        this.zzoG = i;
    }

    static /* synthetic */ long zzc(zzgo com_google_android_gms_internal_zzgo) {
        long j = com_google_android_gms_internal_zzgo.zzGl - 1;
        com_google_android_gms_internal_zzgo.zzGl = j;
        return j;
    }

    public void run() {
        if (this.zzpD == null || zzgg()) {
            this.zzGm.zza(this.zzpD, true);
        } else {
            new zza(this, this.zzpD.getWebView()).execute(new Void[0]);
        }
    }

    public void zza(AdResponseParcel adResponseParcel) {
        zza(adResponseParcel, new zzjy(this, this.zzpD, adResponseParcel.zzIa));
    }

    public void zza(AdResponseParcel adResponseParcel, zzjy com_google_android_gms_internal_zzjy) {
        this.zzpD.setWebViewClient(com_google_android_gms_internal_zzjy);
        this.zzpD.loadDataWithBaseURL(TextUtils.isEmpty(adResponseParcel.zzEF) ? null : zzr.zzbC().zzaC(adResponseParcel.zzEF), adResponseParcel.body, "text/html", DownloadManager.UTF8_CHARSET, null);
    }

    public void zzge() {
        this.zzGj.postDelayed(this, this.zzGk);
    }

    public synchronized void zzgf() {
        this.zzGn = true;
    }

    public synchronized boolean zzgg() {
        return this.zzGn;
    }

    public boolean zzgh() {
        return this.zzGo;
    }
}
