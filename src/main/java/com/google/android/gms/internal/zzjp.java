package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.overlay.zzd;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import java.util.Map;
import org.json.JSONObject;

@zzhb
public interface zzjp extends zzav, zzeh {
    void clearCache(boolean z);

    void destroy();

    Context getContext();

    LayoutParams getLayoutParams();

    void getLocationOnScreen(int[] iArr);

    int getMeasuredHeight();

    int getMeasuredWidth();

    ViewParent getParent();

    String getRequestId();

    int getRequestedOrientation();

    View getView();

    WebView getWebView();

    boolean isDestroyed();

    void loadData(String str, String str2, String str3);

    void loadDataWithBaseURL(String str, String str2, String str3, String str4, String str5);

    void loadUrl(String str);

    void measure(int i, int i2);

    void onPause();

    void onResume();

    void setBackgroundColor(int i);

    void setContext(Context context);

    void setOnClickListener(OnClickListener onClickListener);

    void setOnTouchListener(OnTouchListener onTouchListener);

    void setRequestedOrientation(int i);

    void setWebChromeClient(WebChromeClient webChromeClient);

    void setWebViewClient(WebViewClient webViewClient);

    void stopLoading();

    void zzD(boolean z);

    void zzE(boolean z);

    void zzF(boolean z);

    void zza(Context context, AdSizeParcel adSizeParcel, zzcb com_google_android_gms_internal_zzcb);

    void zza(AdSizeParcel adSizeParcel);

    void zza(String str, Map<String, ?> map);

    void zza(String str, JSONObject jSONObject);

    void zzaL(String str);

    void zzaM(String str);

    AdSizeParcel zzaN();

    void zzb(zzd com_google_android_gms_ads_internal_overlay_zzd);

    void zzc(zzd com_google_android_gms_ads_internal_overlay_zzd);

    void zze(String str, String str2);

    boolean zzfL();

    void zzfr();

    void zzhN();

    void zzhO();

    Activity zzhP();

    Context zzhQ();

    com.google.android.gms.ads.internal.zzd zzhR();

    zzd zzhS();

    zzd zzhT();

    zzjq zzhU();

    boolean zzhV();

    zzan zzhW();

    VersionInfoParcel zzhX();

    boolean zzhY();

    void zzhZ();

    @Nullable
    zzjo zzia();

    @Nullable
    zzbz zzib();

    zzca zzic();

    void zzid();

    void zzie();

    OnClickListener zzif();

    void zzy(int i);
}
