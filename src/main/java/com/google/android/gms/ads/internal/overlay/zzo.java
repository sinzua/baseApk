package com.google.android.gms.ads.internal.overlay;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.internal.zzhb;

@zzhb
public class zzo extends FrameLayout implements OnClickListener {
    private final ImageButton zzEW;
    private final zzs zzEX;

    public zzo(Context context, int i, zzs com_google_android_gms_ads_internal_overlay_zzs) {
        super(context);
        this.zzEX = com_google_android_gms_ads_internal_overlay_zzs;
        setOnClickListener(this);
        this.zzEW = new ImageButton(context);
        this.zzEW.setImageResource(17301527);
        this.zzEW.setBackgroundColor(0);
        this.zzEW.setOnClickListener(this);
        this.zzEW.setPadding(0, 0, 0, 0);
        this.zzEW.setContentDescription("Interstitial close button");
        int zzb = zzn.zzcS().zzb(context, i);
        addView(this.zzEW, new LayoutParams(zzb, zzb, 17));
    }

    public void onClick(View view) {
        if (this.zzEX != null) {
            this.zzEX.zzfm();
        }
    }

    public void zza(boolean z, boolean z2) {
        if (!z2) {
            this.zzEW.setVisibility(0);
        } else if (z) {
            this.zzEW.setVisibility(4);
        } else {
            this.zzEW.setVisibility(8);
        }
    }
}
