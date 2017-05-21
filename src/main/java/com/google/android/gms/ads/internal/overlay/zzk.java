package com.google.android.gms.ads.internal.overlay;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import com.google.android.gms.common.internal.zzb;
import com.google.android.gms.internal.zzbz;
import com.google.android.gms.internal.zzcb;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzjp;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import java.util.HashMap;
import java.util.Map;

@zzhb
public class zzk extends FrameLayout implements zzh {
    private final FrameLayout zzEN;
    private final zzu zzEO;
    @Nullable
    private zzi zzEP;
    private boolean zzEQ;
    private boolean zzER;
    private TextView zzES;
    private long zzET;
    private long zzEU;
    private String zzEV;
    private final zzjp zzpD;
    private String zzzK;

    public zzk(Context context, zzjp com_google_android_gms_internal_zzjp, int i, zzcb com_google_android_gms_internal_zzcb, zzbz com_google_android_gms_internal_zzbz) {
        super(context);
        this.zzpD = com_google_android_gms_internal_zzjp;
        this.zzEN = new FrameLayout(context);
        addView(this.zzEN, new LayoutParams(-1, -1));
        zzb.zzv(com_google_android_gms_internal_zzjp.zzhR());
        this.zzEP = com_google_android_gms_internal_zzjp.zzhR().zzpx.zza(context, com_google_android_gms_internal_zzjp, i, com_google_android_gms_internal_zzcb, com_google_android_gms_internal_zzbz);
        if (this.zzEP != null) {
            this.zzEN.addView(this.zzEP, new LayoutParams(-1, -1, 17));
        }
        this.zzES = new TextView(context);
        this.zzES.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        zzfG();
        this.zzEO = new zzu(this);
        this.zzEO.zzfQ();
        if (this.zzEP != null) {
            this.zzEP.zza((zzh) this);
        }
        if (this.zzEP == null) {
            zzg("AdVideoUnderlay Error", "Allocating player failed.");
        }
    }

    private void zza(String str, String... strArr) {
        Map hashMap = new HashMap();
        hashMap.put("event", str);
        int length = strArr.length;
        int i = 0;
        Object obj = null;
        while (i < length) {
            Object obj2 = strArr[i];
            if (obj != null) {
                hashMap.put(obj, obj2);
                obj2 = null;
            }
            i++;
            obj = obj2;
        }
        this.zzpD.zza("onVideoEvent", hashMap);
    }

    private void zzfG() {
        if (!zzfI()) {
            this.zzEN.addView(this.zzES, new LayoutParams(-1, -1));
            this.zzEN.bringChildToFront(this.zzES);
        }
    }

    private void zzfH() {
        if (zzfI()) {
            this.zzEN.removeView(this.zzES);
        }
    }

    private boolean zzfI() {
        return this.zzES.getParent() != null;
    }

    private void zzfJ() {
        if (this.zzpD.zzhP() != null && !this.zzEQ) {
            this.zzER = (this.zzpD.zzhP().getWindow().getAttributes().flags & 128) != 0;
            if (!this.zzER) {
                this.zzpD.zzhP().getWindow().addFlags(128);
                this.zzEQ = true;
            }
        }
    }

    private void zzfK() {
        if (this.zzpD.zzhP() != null && this.zzEQ && !this.zzER) {
            this.zzpD.zzhP().getWindow().clearFlags(128);
            this.zzEQ = false;
        }
    }

    public static void zzg(zzjp com_google_android_gms_internal_zzjp) {
        Map hashMap = new HashMap();
        hashMap.put("event", "no_video_view");
        com_google_android_gms_internal_zzjp.zza("onVideoEvent", hashMap);
    }

    public void destroy() {
        this.zzEO.cancel();
        if (this.zzEP != null) {
            this.zzEP.stop();
        }
        zzfK();
    }

    public void onPaused() {
        zza("pause", new String[0]);
        zzfK();
    }

    public void pause() {
        if (this.zzEP != null) {
            this.zzEP.pause();
        }
    }

    public void play() {
        if (this.zzEP != null) {
            this.zzEP.play();
        }
    }

    public void seekTo(int millis) {
        if (this.zzEP != null) {
            this.zzEP.seekTo(millis);
        }
    }

    public void setMimeType(String mimeType) {
        this.zzEV = mimeType;
    }

    public void zza(float f) {
        if (this.zzEP != null) {
            this.zzEP.zza(f);
        }
    }

    public void zzap(String str) {
        this.zzzK = str;
    }

    public void zzd(int i, int i2, int i3, int i4) {
        if (i3 != 0 && i4 != 0) {
            ViewGroup.LayoutParams layoutParams = new LayoutParams(i3 + 2, i4 + 2);
            layoutParams.setMargins(i - 1, i2 - 1, 0, 0);
            this.zzEN.setLayoutParams(layoutParams);
            requestLayout();
        }
    }

    public void zzd(MotionEvent motionEvent) {
        if (this.zzEP != null) {
            this.zzEP.dispatchTouchEvent(motionEvent);
        }
    }

    public void zzfA() {
        zzfJ();
    }

    public void zzfB() {
        zza(ParametersKeys.VIDEO_STATUS_ENDED, new String[0]);
        zzfK();
    }

    public void zzfC() {
        zzfG();
        this.zzEU = this.zzET;
    }

    public void zzfD() {
        if (this.zzEP != null) {
            if (TextUtils.isEmpty(this.zzzK)) {
                zza("no_src", new String[0]);
                return;
            }
            this.zzEP.setMimeType(this.zzEV);
            this.zzEP.setVideoPath(this.zzzK);
        }
    }

    public void zzfE() {
        if (this.zzEP != null) {
            View textView = new TextView(this.zzEP.getContext());
            textView.setText("AdMob - " + this.zzEP.zzeZ());
            textView.setTextColor(SupportMenu.CATEGORY_MASK);
            textView.setBackgroundColor(InputDeviceCompat.SOURCE_ANY);
            this.zzEN.addView(textView, new LayoutParams(-2, -2, 17));
            this.zzEN.bringChildToFront(textView);
        }
    }

    void zzfF() {
        if (this.zzEP != null) {
            long currentPosition = (long) this.zzEP.getCurrentPosition();
            if (this.zzET != currentPosition && currentPosition > 0) {
                zzfH();
                float f = ((float) currentPosition) / 1000.0f;
                zza("timeupdate", "time", String.valueOf(f));
                this.zzET = currentPosition;
            }
        }
    }

    public void zzff() {
        if (this.zzEP != null) {
            this.zzEP.zzff();
        }
    }

    public void zzfg() {
        if (this.zzEP != null) {
            this.zzEP.zzfg();
        }
    }

    public void zzfy() {
    }

    public void zzfz() {
        if (this.zzEP != null && this.zzEU == 0) {
            float duration = ((float) this.zzEP.getDuration()) / 1000.0f;
            int videoWidth = this.zzEP.getVideoWidth();
            int videoHeight = this.zzEP.getVideoHeight();
            zza("canplaythrough", "duration", String.valueOf(duration), "videoWidth", String.valueOf(videoWidth), "videoHeight", String.valueOf(videoHeight));
        }
    }

    public void zzg(String str, String str2) {
        zza("error", "what", str, "extra", str2);
    }
}
