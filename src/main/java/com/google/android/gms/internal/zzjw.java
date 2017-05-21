package com.google.android.gms.internal;

import android.annotation.TargetApi;
import android.content.Context;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import com.supersonicads.sdk.precache.DownloadManager;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@TargetApi(11)
@zzhb
public class zzjw extends zzjq {
    public zzjw(zzjp com_google_android_gms_internal_zzjp, boolean z) {
        super(com_google_android_gms_internal_zzjp, z);
    }

    public WebResourceResponse shouldInterceptRequest(WebView webView, String url) {
        Exception e;
        try {
            if (!"mraid.js".equalsIgnoreCase(new File(url).getName())) {
                return super.shouldInterceptRequest(webView, url);
            }
            if (webView instanceof zzjp) {
                zzjp com_google_android_gms_internal_zzjp = (zzjp) webView;
                com_google_android_gms_internal_zzjp.zzhU().zzfo();
                String str = com_google_android_gms_internal_zzjp.zzaN().zzui ? (String) zzbt.zzwf.get() : com_google_android_gms_internal_zzjp.zzhY() ? (String) zzbt.zzwe.get() : (String) zzbt.zzwd.get();
                zzin.v("shouldInterceptRequest(" + str + ")");
                return zzd(com_google_android_gms_internal_zzjp.getContext(), this.zzpD.zzhX().afmaVersion, str);
            }
            zzb.zzaK("Tried to intercept request from a WebView that wasn't an AdWebView.");
            return super.shouldInterceptRequest(webView, url);
        } catch (IOException e2) {
            e = e2;
            zzb.zzaK("Could not fetch MRAID JS. " + e.getMessage());
            return super.shouldInterceptRequest(webView, url);
        } catch (ExecutionException e3) {
            e = e3;
            zzb.zzaK("Could not fetch MRAID JS. " + e.getMessage());
            return super.shouldInterceptRequest(webView, url);
        } catch (InterruptedException e4) {
            e = e4;
            zzb.zzaK("Could not fetch MRAID JS. " + e.getMessage());
            return super.shouldInterceptRequest(webView, url);
        } catch (TimeoutException e5) {
            e = e5;
            zzb.zzaK("Could not fetch MRAID JS. " + e.getMessage());
            return super.shouldInterceptRequest(webView, url);
        }
    }

    protected WebResourceResponse zzd(Context context, String str, String str2) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        Map hashMap = new HashMap();
        hashMap.put("User-Agent", zzr.zzbC().zze(context, str));
        hashMap.put("Cache-Control", "max-stale=3600");
        String str3 = (String) new zziw(context).zzb(str2, hashMap).get(60, TimeUnit.SECONDS);
        return str3 == null ? null : new WebResourceResponse("application/javascript", DownloadManager.UTF8_CHARSET, new ByteArrayInputStream(str3.getBytes(DownloadManager.UTF8_CHARSET)));
    }
}
