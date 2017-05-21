package com.google.android.gms.ads.internal.formats;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import java.lang.ref.WeakReference;
import java.util.Map;
import org.json.JSONObject;

public interface zzh {

    public interface zza {
        String getCustomTemplateId();

        void zzb(zzh com_google_android_gms_ads_internal_formats_zzh);

        String zzdM();

        zza zzdN();
    }

    Context getContext();

    void recordImpression();

    void zza(View view, Map<String, WeakReference<View>> map, JSONObject jSONObject, JSONObject jSONObject2, JSONObject jSONObject3);

    void zza(String str, JSONObject jSONObject, JSONObject jSONObject2, JSONObject jSONObject3);

    void zzb(MotionEvent motionEvent);

    View zzdS();

    void zzh(View view);

    void zzi(View view);
}
