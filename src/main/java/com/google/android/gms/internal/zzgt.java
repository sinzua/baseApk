package com.google.android.gms.internal;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzif.zza;

@TargetApi(19)
@zzhb
public class zzgt extends zzgs {
    private Object zzGv = new Object();
    private PopupWindow zzGw;
    private boolean zzGx = false;

    zzgt(Context context, zza com_google_android_gms_internal_zzif_zza, zzjp com_google_android_gms_internal_zzjp, zzgr.zza com_google_android_gms_internal_zzgr_zza) {
        super(context, com_google_android_gms_internal_zzif_zza, com_google_android_gms_internal_zzjp, com_google_android_gms_internal_zzgr_zza);
    }

    private void zzgj() {
        synchronized (this.zzGv) {
            this.zzGx = true;
            if ((this.mContext instanceof Activity) && ((Activity) this.mContext).isDestroyed()) {
                this.zzGw = null;
            }
            if (this.zzGw != null) {
                if (this.zzGw.isShowing()) {
                    this.zzGw.dismiss();
                }
                this.zzGw = null;
            }
        }
    }

    public void cancel() {
        zzgj();
        super.cancel();
    }

    protected void zzC(int i) {
        zzgj();
        super.zzC(i);
    }

    protected void zzgi() {
        Window window = this.mContext instanceof Activity ? ((Activity) this.mContext).getWindow() : null;
        if (window != null && window.getDecorView() != null && !((Activity) this.mContext).isDestroyed()) {
            View frameLayout = new FrameLayout(this.mContext);
            frameLayout.setLayoutParams(new LayoutParams(-1, -1));
            frameLayout.addView(this.zzpD.getView(), -1, -1);
            synchronized (this.zzGv) {
                if (this.zzGx) {
                    return;
                }
                this.zzGw = new PopupWindow(frameLayout, 1, 1, false);
                this.zzGw.setOutsideTouchable(true);
                this.zzGw.setClippingEnabled(false);
                zzb.zzaI("Displaying the 1x1 popup off the screen.");
                try {
                    this.zzGw.showAtLocation(window.getDecorView(), 0, -1, -1);
                } catch (Exception e) {
                    this.zzGw = null;
                }
            }
        }
    }
}
