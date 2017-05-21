package com.google.android.gms.ads.internal.overlay;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.Window;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.internal.zzbt;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzim;
import com.google.android.gms.internal.zzir;
import com.google.android.gms.internal.zziu;
import com.google.android.gms.internal.zzjp;
import com.supersonicads.sdk.precache.DownloadManager;
import com.supersonicads.sdk.utils.Constants.ControllerParameters;
import java.util.Collections;
import java.util.Map;

@zzhb
public class zzd extends com.google.android.gms.internal.zzfv.zza implements zzs {
    static final int zzEg = Color.argb(0, 0, 0, 0);
    private final Activity mActivity;
    RelativeLayout zzDm;
    AdOverlayInfoParcel zzEh;
    zzc zzEi;
    zzo zzEj;
    boolean zzEk = false;
    FrameLayout zzEl;
    CustomViewCallback zzEm;
    boolean zzEn = false;
    boolean zzEo = false;
    boolean zzEp = false;
    int zzEq = 0;
    zzl zzEr;
    private boolean zzEs;
    private boolean zzEt = false;
    private boolean zzEu = true;
    zzjp zzpD;

    @zzhb
    private static final class zza extends Exception {
        public zza(String str) {
            super(str);
        }
    }

    @zzhb
    static final class zzb extends RelativeLayout {
        zziu zzrU;

        public zzb(Context context, String str) {
            super(context);
            this.zzrU = new zziu(context, str);
        }

        public boolean onInterceptTouchEvent(MotionEvent event) {
            this.zzrU.zze(event);
            return false;
        }
    }

    @zzhb
    public static class zzc {
        public final Context context;
        public final int index;
        public final LayoutParams zzEw;
        public final ViewGroup zzEx;

        public zzc(zzjp com_google_android_gms_internal_zzjp) throws zza {
            this.zzEw = com_google_android_gms_internal_zzjp.getLayoutParams();
            ViewParent parent = com_google_android_gms_internal_zzjp.getParent();
            this.context = com_google_android_gms_internal_zzjp.zzhQ();
            if (parent == null || !(parent instanceof ViewGroup)) {
                throw new zza("Could not get the parent of the WebView for an overlay.");
            }
            this.zzEx = (ViewGroup) parent;
            this.index = this.zzEx.indexOfChild(com_google_android_gms_internal_zzjp.getView());
            this.zzEx.removeView(com_google_android_gms_internal_zzjp.getView());
            com_google_android_gms_internal_zzjp.zzD(true);
        }
    }

    @zzhb
    private class zzd extends zzim {
        final /* synthetic */ zzd zzEv;

        private zzd(zzd com_google_android_gms_ads_internal_overlay_zzd) {
            this.zzEv = com_google_android_gms_ads_internal_overlay_zzd;
        }

        public void onStop() {
        }

        public void zzbr() {
            Bitmap zzf = zzr.zzbC().zzf(this.zzEv.mActivity, this.zzEv.zzEh.zzEM.zzqn);
            if (zzf != null) {
                final Drawable zza = zzr.zzbE().zza(this.zzEv.mActivity, zzf, this.zzEv.zzEh.zzEM.zzqo, this.zzEv.zzEh.zzEM.zzqp);
                zzir.zzMc.post(new Runnable(this) {
                    final /* synthetic */ zzd zzEz;

                    public void run() {
                        this.zzEz.zzEv.mActivity.getWindow().setBackgroundDrawable(zza);
                    }
                });
            }
        }
    }

    public zzd(Activity activity) {
        this.mActivity = activity;
        this.zzEr = new zzq();
    }

    public void close() {
        this.zzEq = 2;
        this.mActivity.finish();
    }

    public void onBackPressed() {
        this.zzEq = 0;
    }

    public void onCreate(Bundle savedInstanceState) {
        boolean z = false;
        if (savedInstanceState != null) {
            z = savedInstanceState.getBoolean("com.google.android.gms.ads.internal.overlay.hasResumed", false);
        }
        this.zzEn = z;
        try {
            this.zzEh = AdOverlayInfoParcel.zzb(this.mActivity.getIntent());
            if (this.zzEh == null) {
                throw new zza("Could not get info for ad overlay.");
            }
            if (this.zzEh.zzrl.zzNa > 7500000) {
                this.zzEq = 3;
            }
            if (this.mActivity.getIntent() != null) {
                this.zzEu = this.mActivity.getIntent().getBooleanExtra("shouldCallOnOverlayOpened", true);
            }
            if (this.zzEh.zzEM != null) {
                this.zzEo = this.zzEh.zzEM.zzql;
            } else {
                this.zzEo = false;
            }
            if (((Boolean) zzbt.zzxe.get()).booleanValue() && this.zzEo && this.zzEh.zzEM.zzqn != null) {
                new zzd().zzhn();
            }
            if (savedInstanceState == null) {
                if (this.zzEh.zzEC != null && this.zzEu) {
                    this.zzEh.zzEC.zzaX();
                }
                if (!(this.zzEh.zzEJ == 1 || this.zzEh.zzEB == null)) {
                    this.zzEh.zzEB.onAdClicked();
                }
            }
            this.zzDm = new zzb(this.mActivity, this.zzEh.zzEL);
            this.zzDm.setId(ControllerParameters.SECOND);
            switch (this.zzEh.zzEJ) {
                case 1:
                    zzx(false);
                    return;
                case 2:
                    this.zzEi = new zzc(this.zzEh.zzED);
                    zzx(false);
                    return;
                case 3:
                    zzx(true);
                    return;
                case 4:
                    if (this.zzEn) {
                        this.zzEq = 3;
                        this.mActivity.finish();
                        return;
                    } else if (!zzr.zzbz().zza(this.mActivity, this.zzEh.zzEA, this.zzEh.zzEI)) {
                        this.zzEq = 3;
                        this.mActivity.finish();
                        return;
                    } else {
                        return;
                    }
                default:
                    throw new zza("Could not determine ad overlay type.");
            }
        } catch (zza e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaK(e.getMessage());
            this.zzEq = 3;
            this.mActivity.finish();
        }
    }

    public void onDestroy() {
        if (this.zzpD != null) {
            this.zzDm.removeView(this.zzpD.getView());
        }
        zzfp();
    }

    public void onPause() {
        this.zzEr.pause();
        zzfl();
        if (this.zzEh.zzEC != null) {
            this.zzEh.zzEC.onPause();
        }
        if (this.zzpD != null && (!this.mActivity.isFinishing() || this.zzEi == null)) {
            zzr.zzbE().zzi(this.zzpD);
        }
        zzfp();
    }

    public void onRestart() {
    }

    public void onResume() {
        if (this.zzEh != null && this.zzEh.zzEJ == 4) {
            if (this.zzEn) {
                this.zzEq = 3;
                this.mActivity.finish();
            } else {
                this.zzEn = true;
            }
        }
        if (this.zzEh.zzEC != null) {
            this.zzEh.zzEC.onResume();
        }
        if (this.zzpD == null || this.zzpD.isDestroyed()) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaK("The webview does not exit. Ignoring action.");
        } else {
            zzr.zzbE().zzj(this.zzpD);
        }
        this.zzEr.resume();
    }

    public void onSaveInstanceState(Bundle outBundle) {
        outBundle.putBoolean("com.google.android.gms.ads.internal.overlay.hasResumed", this.zzEn);
    }

    public void onStart() {
    }

    public void onStop() {
        zzfp();
    }

    public void setRequestedOrientation(int requestedOrientation) {
        this.mActivity.setRequestedOrientation(requestedOrientation);
    }

    public void zza(View view, CustomViewCallback customViewCallback) {
        this.zzEl = new FrameLayout(this.mActivity);
        this.zzEl.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        this.zzEl.addView(view, -1, -1);
        this.mActivity.setContentView(this.zzEl);
        zzaD();
        this.zzEm = customViewCallback;
        this.zzEk = true;
    }

    public void zza(boolean z, boolean z2) {
        if (this.zzEj != null) {
            this.zzEj.zza(z, z2);
        }
    }

    public void zzaD() {
        this.zzEs = true;
    }

    public void zzfl() {
        if (this.zzEh != null && this.zzEk) {
            setRequestedOrientation(this.zzEh.orientation);
        }
        if (this.zzEl != null) {
            this.mActivity.setContentView(this.zzDm);
            zzaD();
            this.zzEl.removeAllViews();
            this.zzEl = null;
        }
        if (this.zzEm != null) {
            this.zzEm.onCustomViewHidden();
            this.zzEm = null;
        }
        this.zzEk = false;
    }

    public void zzfm() {
        this.zzEq = 1;
        this.mActivity.finish();
    }

    public boolean zzfn() {
        boolean z = true;
        this.zzEq = 0;
        if (this.zzpD != null) {
            if (!(this.zzpD.zzfL() && this.zzEr.zzfL())) {
                z = false;
            }
            if (!z) {
                this.zzpD.zza("onbackblocked", Collections.emptyMap());
            }
        }
        return z;
    }

    public void zzfo() {
        this.zzDm.removeView(this.zzEj);
        zzw(true);
    }

    protected void zzfp() {
        if (this.mActivity.isFinishing() && !this.zzEt) {
            this.zzEt = true;
            if (this.zzpD != null) {
                zzy(this.zzEq);
                this.zzDm.removeView(this.zzpD.getView());
                if (this.zzEi != null) {
                    this.zzpD.setContext(this.zzEi.context);
                    this.zzpD.zzD(false);
                    this.zzEi.zzEx.addView(this.zzpD.getView(), this.zzEi.index, this.zzEi.zzEw);
                    this.zzEi = null;
                } else if (this.mActivity.getApplicationContext() != null) {
                    this.zzpD.setContext(this.mActivity.getApplicationContext());
                }
                this.zzpD = null;
            }
            if (!(this.zzEh == null || this.zzEh.zzEC == null)) {
                this.zzEh.zzEC.zzaW();
            }
            this.zzEr.destroy();
        }
    }

    public void zzfq() {
        if (this.zzEp) {
            this.zzEp = false;
            zzfr();
        }
    }

    protected void zzfr() {
        this.zzpD.zzfr();
    }

    public void zzg(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
        this.zzEr.zzg(com_google_android_gms_internal_zzjp, map);
    }

    public void zzw(boolean z) {
        this.zzEj = new zzo(this.mActivity, z ? 50 : 32, this);
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(10);
        layoutParams.addRule(z ? 11 : 9);
        this.zzEj.zza(z, this.zzEh.zzEG);
        this.zzDm.addView(this.zzEj, layoutParams);
    }

    protected void zzx(boolean z) throws zza {
        if (!this.zzEs) {
            this.mActivity.requestWindowFeature(1);
        }
        Window window = this.mActivity.getWindow();
        if (window == null) {
            throw new zza("Invalid activity, no window available.");
        }
        if (!this.zzEo || (this.zzEh.zzEM != null && this.zzEh.zzEM.zzqm)) {
            window.setFlags(1024, 1024);
        }
        boolean zzcv = this.zzEh.zzED.zzhU().zzcv();
        this.zzEp = false;
        if (zzcv) {
            if (this.zzEh.orientation == zzr.zzbE().zzhv()) {
                this.zzEp = this.mActivity.getResources().getConfiguration().orientation == 1;
            } else if (this.zzEh.orientation == zzr.zzbE().zzhw()) {
                this.zzEp = this.mActivity.getResources().getConfiguration().orientation == 2;
            }
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaI("Delay onShow to next orientation change: " + this.zzEp);
        setRequestedOrientation(this.zzEh.orientation);
        if (zzr.zzbE().zza(window)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaI("Hardware acceleration on the AdActivity window enabled.");
        }
        if (this.zzEo) {
            this.zzDm.setBackgroundColor(zzEg);
        } else {
            this.zzDm.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        }
        this.mActivity.setContentView(this.zzDm);
        zzaD();
        if (z) {
            this.zzpD = zzr.zzbD().zza(this.mActivity, this.zzEh.zzED.zzaN(), true, zzcv, null, this.zzEh.zzrl, null, this.zzEh.zzED.zzhR());
            this.zzpD.zzhU().zzb(null, null, this.zzEh.zzEE, this.zzEh.zzEI, true, this.zzEh.zzEK, null, this.zzEh.zzED.zzhU().zzig(), null);
            this.zzpD.zzhU().zza(new com.google.android.gms.internal.zzjq.zza(this) {
                final /* synthetic */ zzd zzEv;

                {
                    this.zzEv = r1;
                }

                public void zza(zzjp com_google_android_gms_internal_zzjp, boolean z) {
                    com_google_android_gms_internal_zzjp.zzfr();
                }
            });
            if (this.zzEh.url != null) {
                this.zzpD.loadUrl(this.zzEh.url);
            } else if (this.zzEh.zzEH != null) {
                this.zzpD.loadDataWithBaseURL(this.zzEh.zzEF, this.zzEh.zzEH, "text/html", DownloadManager.UTF8_CHARSET, null);
            } else {
                throw new zza("No URL or HTML to display in ad overlay.");
            }
            if (this.zzEh.zzED != null) {
                this.zzEh.zzED.zzc(this);
            }
        } else {
            this.zzpD = this.zzEh.zzED;
            this.zzpD.setContext(this.mActivity);
        }
        this.zzpD.zzb(this);
        ViewParent parent = this.zzpD.getParent();
        if (parent != null && (parent instanceof ViewGroup)) {
            ((ViewGroup) parent).removeView(this.zzpD.getView());
        }
        if (this.zzEo) {
            this.zzpD.setBackgroundColor(zzEg);
        }
        this.zzDm.addView(this.zzpD.getView(), -1, -1);
        if (!(z || this.zzEp)) {
            zzfr();
        }
        zzw(zzcv);
        if (this.zzpD.zzhV()) {
            zza(zzcv, true);
        }
        com.google.android.gms.ads.internal.zzd zzhR = this.zzpD.zzhR();
        zzm com_google_android_gms_ads_internal_overlay_zzm = zzhR != null ? zzhR.zzpy : null;
        if (com_google_android_gms_ads_internal_overlay_zzm != null) {
            this.zzEr = com_google_android_gms_ads_internal_overlay_zzm.zza(this.mActivity, this.zzpD, this.zzDm);
        } else {
            com.google.android.gms.ads.internal.util.client.zzb.zzaK("Appstreaming controller is null.");
        }
    }

    protected void zzy(int i) {
        this.zzpD.zzy(i);
    }
}
