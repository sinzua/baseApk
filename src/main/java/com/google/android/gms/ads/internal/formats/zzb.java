package com.google.android.gms.ads.internal.formats;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzhb;
import java.util.List;

@zzhb
class zzb extends RelativeLayout {
    private static final float[] zzxR = new float[]{5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f};
    private final RelativeLayout zzxS;
    private AnimationDrawable zzxT;

    public zzb(Context context, zza com_google_android_gms_ads_internal_formats_zza) {
        super(context);
        zzx.zzz(com_google_android_gms_ads_internal_formats_zza);
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(10);
        layoutParams.addRule(11);
        Drawable shapeDrawable = new ShapeDrawable(new RoundRectShape(zzxR, null, null));
        shapeDrawable.getPaint().setColor(com_google_android_gms_ads_internal_formats_zza.getBackgroundColor());
        this.zzxS = new RelativeLayout(context);
        this.zzxS.setLayoutParams(layoutParams);
        zzr.zzbE().zza(this.zzxS, shapeDrawable);
        layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        if (!TextUtils.isEmpty(com_google_android_gms_ads_internal_formats_zza.getText())) {
            LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
            View textView = new TextView(context);
            textView.setLayoutParams(layoutParams2);
            textView.setId(1195835393);
            textView.setTypeface(Typeface.DEFAULT);
            textView.setText(com_google_android_gms_ads_internal_formats_zza.getText());
            textView.setTextColor(com_google_android_gms_ads_internal_formats_zza.getTextColor());
            textView.setTextSize((float) com_google_android_gms_ads_internal_formats_zza.getTextSize());
            textView.setPadding(zzn.zzcS().zzb(context, 4), 0, zzn.zzcS().zzb(context, 4), 0);
            this.zzxS.addView(textView);
            layoutParams.addRule(1, textView.getId());
        }
        View imageView = new ImageView(context);
        imageView.setLayoutParams(layoutParams);
        imageView.setId(1195835394);
        List<Drawable> zzdG = com_google_android_gms_ads_internal_formats_zza.zzdG();
        if (zzdG.size() > 1) {
            this.zzxT = new AnimationDrawable();
            for (Drawable addFrame : zzdG) {
                this.zzxT.addFrame(addFrame, com_google_android_gms_ads_internal_formats_zza.zzdH());
            }
            zzr.zzbE().zza(imageView, this.zzxT);
        } else if (zzdG.size() == 1) {
            imageView.setImageDrawable((Drawable) zzdG.get(0));
        }
        this.zzxS.addView(imageView);
        addView(this.zzxS);
    }

    public void onAttachedToWindow() {
        if (this.zzxT != null) {
            this.zzxT.start();
        }
        super.onAttachedToWindow();
    }

    public ViewGroup zzdI() {
        return this.zzxS;
    }
}
