package com.google.android.gms.internal;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.KeyguardManager;
import android.content.Context;
import android.graphics.Rect;
import android.os.PowerManager;
import android.os.Process;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.supersonicads.sdk.utils.Constants.ControllerParameters;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

@TargetApi(14)
@zzhb
public class zzbf extends Thread {
    private boolean mStarted = false;
    private boolean zzam = false;
    private final Object zzpV;
    private final int zzsK;
    private final int zzsM;
    private boolean zzsY = false;
    private final zzbe zzsZ;
    private final zzbd zzta;
    private final zzha zztb;
    private final int zztc;
    private final int zztd;
    private final int zzte;

    @zzhb
    class zza {
        final /* synthetic */ zzbf zztg;
        final int zztm;
        final int zztn;

        zza(zzbf com_google_android_gms_internal_zzbf, int i, int i2) {
            this.zztg = com_google_android_gms_internal_zzbf;
            this.zztm = i;
            this.zztn = i2;
        }
    }

    public zzbf(zzbe com_google_android_gms_internal_zzbe, zzbd com_google_android_gms_internal_zzbd, zzha com_google_android_gms_internal_zzha) {
        this.zzsZ = com_google_android_gms_internal_zzbe;
        this.zzta = com_google_android_gms_internal_zzbd;
        this.zztb = com_google_android_gms_internal_zzha;
        this.zzpV = new Object();
        this.zzsK = ((Integer) zzbt.zzwk.get()).intValue();
        this.zztd = ((Integer) zzbt.zzwl.get()).intValue();
        this.zzsM = ((Integer) zzbt.zzwm.get()).intValue();
        this.zzte = ((Integer) zzbt.zzwn.get()).intValue();
        this.zztc = ((Integer) zzbt.zzwo.get()).intValue();
        setName("ContentFetchTask");
    }

    public void run() {
        while (!this.zzam) {
            try {
                if (zzcH()) {
                    Activity activity = this.zzsZ.getActivity();
                    if (activity == null) {
                        zzb.zzaI("ContentFetchThread: no activity");
                    } else {
                        zza(activity);
                    }
                } else {
                    zzb.zzaI("ContentFetchTask: sleeping");
                    zzcJ();
                }
                Thread.sleep((long) (this.zztc * ControllerParameters.SECOND));
            } catch (Throwable th) {
                zzb.zzb("Error in ContentFetchTask", th);
                this.zztb.zza(th, true);
            }
            synchronized (this.zzpV) {
                while (this.zzsY) {
                    try {
                        zzb.zzaI("ContentFetchTask: waiting");
                        this.zzpV.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    public void wakeup() {
        synchronized (this.zzpV) {
            this.zzsY = false;
            this.zzpV.notifyAll();
            zzb.zzaI("ContentFetchThread: wakeup");
        }
    }

    zza zza(View view, zzbc com_google_android_gms_internal_zzbc) {
        int i = 0;
        if (view == null) {
            return new zza(this, 0, 0);
        }
        boolean globalVisibleRect = view.getGlobalVisibleRect(new Rect());
        if ((view instanceof TextView) && !(view instanceof EditText)) {
            CharSequence text = ((TextView) view).getText();
            if (TextUtils.isEmpty(text)) {
                return new zza(this, 0, 0);
            }
            com_google_android_gms_internal_zzbc.zzd(text.toString(), globalVisibleRect);
            return new zza(this, 1, 0);
        } else if ((view instanceof WebView) && !(view instanceof zzjp)) {
            com_google_android_gms_internal_zzbc.zzcC();
            return zza((WebView) view, com_google_android_gms_internal_zzbc, globalVisibleRect) ? new zza(this, 0, 1) : new zza(this, 0, 0);
        } else if (!(view instanceof ViewGroup)) {
            return new zza(this, 0, 0);
        } else {
            ViewGroup viewGroup = (ViewGroup) view;
            int i2 = 0;
            int i3 = 0;
            while (i < viewGroup.getChildCount()) {
                zza zza = zza(viewGroup.getChildAt(i), com_google_android_gms_internal_zzbc);
                i3 += zza.zztm;
                i2 += zza.zztn;
                i++;
            }
            return new zza(this, i3, i2);
        }
    }

    void zza(Activity activity) {
        if (activity != null) {
            View view = null;
            if (!(activity.getWindow() == null || activity.getWindow().getDecorView() == null)) {
                view = activity.getWindow().getDecorView().findViewById(16908290);
            }
            if (view != null) {
                zze(view);
            }
        }
    }

    void zza(zzbc com_google_android_gms_internal_zzbc, WebView webView, String str, boolean z) {
        com_google_android_gms_internal_zzbc.zzcB();
        try {
            if (!TextUtils.isEmpty(str)) {
                String optString = new JSONObject(str).optString("text");
                if (TextUtils.isEmpty(webView.getTitle())) {
                    com_google_android_gms_internal_zzbc.zzc(optString, z);
                } else {
                    com_google_android_gms_internal_zzbc.zzc(webView.getTitle() + "\n" + optString, z);
                }
            }
            if (com_google_android_gms_internal_zzbc.zzcx()) {
                this.zzta.zzb(com_google_android_gms_internal_zzbc);
            }
        } catch (JSONException e) {
            zzb.zzaI("Json string may be malformed.");
        } catch (Throwable th) {
            zzb.zza("Failed to get webview content.", th);
            this.zztb.zza(th, true);
        }
    }

    boolean zza(RunningAppProcessInfo runningAppProcessInfo) {
        return runningAppProcessInfo.importance == 100;
    }

    @TargetApi(19)
    boolean zza(final WebView webView, final zzbc com_google_android_gms_internal_zzbc, final boolean z) {
        if (!zzne.zzsk()) {
            return false;
        }
        com_google_android_gms_internal_zzbc.zzcC();
        webView.post(new Runnable(this) {
            final /* synthetic */ zzbf zztg;
            ValueCallback<String> zzth = new ValueCallback<String>(this) {
                final /* synthetic */ AnonymousClass2 zztl;

                {
                    this.zztl = r1;
                }

                public /* synthetic */ void onReceiveValue(Object obj) {
                    zzt((String) obj);
                }

                public void zzt(String str) {
                    this.zztl.zztg.zza(com_google_android_gms_internal_zzbc, webView, str, z);
                }
            };

            public void run() {
                if (webView.getSettings().getJavaScriptEnabled()) {
                    try {
                        webView.evaluateJavascript("(function() { return  {text:document.body.innerText}})();", this.zzth);
                    } catch (Throwable th) {
                        this.zzth.onReceiveValue("");
                    }
                }
            }
        });
        return true;
    }

    public void zzcG() {
        synchronized (this.zzpV) {
            if (this.mStarted) {
                zzb.zzaI("Content hash thread already started, quiting...");
                return;
            }
            this.mStarted = true;
            start();
        }
    }

    boolean zzcH() {
        try {
            Context context = this.zzsZ.getContext();
            if (context == null) {
                return false;
            }
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService("keyguard");
            if (activityManager == null || keyguardManager == null) {
                return false;
            }
            List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
            if (runningAppProcesses == null) {
                return false;
            }
            for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                if (Process.myPid() == runningAppProcessInfo.pid) {
                    if (zza(runningAppProcessInfo) && !keyguardManager.inKeyguardRestrictedInputMode() && zzs(context)) {
                        return true;
                    }
                    return false;
                }
            }
            return false;
        } catch (Throwable th) {
            return false;
        }
    }

    public zzbc zzcI() {
        return this.zzta.zzcF();
    }

    public void zzcJ() {
        synchronized (this.zzpV) {
            this.zzsY = true;
            zzb.zzaI("ContentFetchThread: paused, mPause = " + this.zzsY);
        }
    }

    public boolean zzcK() {
        return this.zzsY;
    }

    boolean zze(final View view) {
        if (view == null) {
            return false;
        }
        view.post(new Runnable(this) {
            final /* synthetic */ zzbf zztg;

            public void run() {
                this.zztg.zzf(view);
            }
        });
        return true;
    }

    void zzf(View view) {
        try {
            zzbc com_google_android_gms_internal_zzbc = new zzbc(this.zzsK, this.zztd, this.zzsM, this.zzte);
            zza zza = zza(view, com_google_android_gms_internal_zzbc);
            com_google_android_gms_internal_zzbc.zzcD();
            if (zza.zztm != 0 || zza.zztn != 0) {
                if (zza.zztn != 0 || com_google_android_gms_internal_zzbc.zzcE() != 0) {
                    if (zza.zztn != 0 || !this.zzta.zza(com_google_android_gms_internal_zzbc)) {
                        this.zzta.zzc(com_google_android_gms_internal_zzbc);
                    }
                }
            }
        } catch (Throwable e) {
            zzb.zzb("Exception in fetchContentOnUIThread", e);
            this.zztb.zza(e, true);
        }
    }

    boolean zzs(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService("power");
        return powerManager == null ? false : powerManager.isScreenOn();
    }
}
