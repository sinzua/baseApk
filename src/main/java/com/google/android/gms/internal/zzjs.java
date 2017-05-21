package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.overlay.zzd;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import java.util.Map;
import org.json.JSONObject;

@zzhb
class zzjs extends FrameLayout implements zzjp {
    private final zzjp zzNN;
    private final zzjo zzNO;

    public zzjs(zzjp com_google_android_gms_internal_zzjp) {
        super(com_google_android_gms_internal_zzjp.getContext());
        this.zzNN = com_google_android_gms_internal_zzjp;
        this.zzNO = new zzjo(com_google_android_gms_internal_zzjp.zzhQ(), this, this);
        zzjq zzhU = this.zzNN.zzhU();
        if (zzhU != null) {
            zzhU.zzh((zzjp) this);
        }
        addView(this.zzNN.getView());
    }

    public void clearCache(boolean includeDiskFiles) {
        this.zzNN.clearCache(includeDiskFiles);
    }

    public void destroy() {
        this.zzNN.destroy();
    }

    public String getRequestId() {
        return this.zzNN.getRequestId();
    }

    public int getRequestedOrientation() {
        return this.zzNN.getRequestedOrientation();
    }

    public View getView() {
        return this;
    }

    public WebView getWebView() {
        return this.zzNN.getWebView();
    }

    public boolean isDestroyed() {
        return this.zzNN.isDestroyed();
    }

    public void loadData(String data, String mimeType, String encoding) {
        this.zzNN.loadData(data, mimeType, encoding);
    }

    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        this.zzNN.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    public void loadUrl(String url) {
        this.zzNN.loadUrl(url);
    }

    public void onPause() {
        this.zzNO.onPause();
        this.zzNN.onPause();
    }

    public void onResume() {
        this.zzNN.onResume();
    }

    public void setBackgroundColor(int color) {
        this.zzNN.setBackgroundColor(color);
    }

    public void setContext(Context context) {
        this.zzNN.setContext(context);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.zzNN.setOnClickListener(listener);
    }

    public void setOnTouchListener(OnTouchListener listener) {
        this.zzNN.setOnTouchListener(listener);
    }

    public void setRequestedOrientation(int requestedOrientation) {
        this.zzNN.setRequestedOrientation(requestedOrientation);
    }

    public void setWebChromeClient(WebChromeClient client) {
        this.zzNN.setWebChromeClient(client);
    }

    public void setWebViewClient(WebViewClient client) {
        this.zzNN.setWebViewClient(client);
    }

    public void stopLoading() {
        this.zzNN.stopLoading();
    }

    public void zzD(boolean z) {
        this.zzNN.zzD(z);
    }

    public void zzE(boolean z) {
        this.zzNN.zzE(z);
    }

    public void zzF(boolean z) {
        this.zzNN.zzF(z);
    }

    public void zza(Context context, AdSizeParcel adSizeParcel, zzcb com_google_android_gms_internal_zzcb) {
        this.zzNN.zza(context, adSizeParcel, com_google_android_gms_internal_zzcb);
    }

    public void zza(AdSizeParcel adSizeParcel) {
        this.zzNN.zza(adSizeParcel);
    }

    public void zza(zzau com_google_android_gms_internal_zzau, boolean z) {
        this.zzNN.zza(com_google_android_gms_internal_zzau, z);
    }

    public void zza(String str, zzdf com_google_android_gms_internal_zzdf) {
        this.zzNN.zza(str, com_google_android_gms_internal_zzdf);
    }

    public void zza(String str, Map<String, ?> map) {
        this.zzNN.zza(str, (Map) map);
    }

    public void zza(String str, JSONObject jSONObject) {
        this.zzNN.zza(str, jSONObject);
    }

    public void zzaL(String str) {
        this.zzNN.zzaL(str);
    }

    public void zzaM(String str) {
        this.zzNN.zzaM(str);
    }

    public AdSizeParcel zzaN() {
        return this.zzNN.zzaN();
    }

    public void zzb(zzd com_google_android_gms_ads_internal_overlay_zzd) {
        this.zzNN.zzb(com_google_android_gms_ads_internal_overlay_zzd);
    }

    public void zzb(String str, zzdf com_google_android_gms_internal_zzdf) {
        this.zzNN.zzb(str, com_google_android_gms_internal_zzdf);
    }

    public void zzb(String str, JSONObject jSONObject) {
        this.zzNN.zzb(str, jSONObject);
    }

    public void zzc(zzd com_google_android_gms_ads_internal_overlay_zzd) {
        this.zzNN.zzc(com_google_android_gms_ads_internal_overlay_zzd);
    }

    public void zze(String str, String str2) {
        this.zzNN.zze(str, str2);
    }

    public boolean zzfL() {
        return this.zzNN.zzfL();
    }

    public void zzfr() {
        this.zzNN.zzfr();
    }

    public void zzhN() {
        this.zzNN.zzhN();
    }

    public void zzhO() {
        this.zzNN.zzhO();
    }

    public Activity zzhP() {
        return this.zzNN.zzhP();
    }

    public Context zzhQ() {
        return this.zzNN.zzhQ();
    }

    public com.google.android.gms.ads.internal.zzd zzhR() {
        return this.zzNN.zzhR();
    }

    public zzd zzhS() {
        return this.zzNN.zzhS();
    }

    public zzd zzhT() {
        return this.zzNN.zzhT();
    }

    public zzjq zzhU() {
        return this.zzNN.zzhU();
    }

    public boolean zzhV() {
        return this.zzNN.zzhV();
    }

    public zzan zzhW() {
        return this.zzNN.zzhW();
    }

    public VersionInfoParcel zzhX() {
        return this.zzNN.zzhX();
    }

    public boolean zzhY() {
        return this.zzNN.zzhY();
    }

    public void zzhZ() {
        this.zzNO.onDestroy();
        this.zzNN.zzhZ();
    }

    public zzjo zzia() {
        return this.zzNO;
    }

    public zzbz zzib() {
        return this.zzNN.zzib();
    }

    public zzca zzic() {
        return this.zzNN.zzic();
    }

    public void zzid() {
        this.zzNN.zzid();
    }

    public void zzie() {
        this.zzNN.zzie();
    }

    public OnClickListener zzif() {
        return this.zzNN.zzif();
    }

    public void zzy(int i) {
        this.zzNN.zzy(i);
    }
}
