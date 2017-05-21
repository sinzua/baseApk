package com.google.android.gms.internal;

import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.common.internal.zzw;
import java.net.URI;
import java.net.URISyntaxException;

@zzhb
public class zzjy extends WebViewClient {
    private final zzgo zzGs;
    private final String zzOl;
    private boolean zzOm = false;
    private final zzjp zzpD;

    public zzjy(zzgo com_google_android_gms_internal_zzgo, zzjp com_google_android_gms_internal_zzjp, String str) {
        this.zzOl = zzaR(str);
        this.zzpD = com_google_android_gms_internal_zzjp;
        this.zzGs = com_google_android_gms_internal_zzgo;
    }

    private String zzaR(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                if (str.endsWith("/")) {
                    str = str.substring(0, str.length() - 1);
                }
            } catch (IndexOutOfBoundsException e) {
                zzb.e(e.getMessage());
            }
        }
        return str;
    }

    public void onLoadResource(WebView view, String url) {
        zzb.zzaI("JavascriptAdWebViewClient::onLoadResource: " + url);
        if (!zzaQ(url)) {
            this.zzpD.zzhU().onLoadResource(this.zzpD.getWebView(), url);
        }
    }

    public void onPageFinished(WebView view, String url) {
        zzb.zzaI("JavascriptAdWebViewClient::onPageFinished: " + url);
        if (!this.zzOm) {
            this.zzGs.zzge();
            this.zzOm = true;
        }
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        zzb.zzaI("JavascriptAdWebViewClient::shouldOverrideUrlLoading: " + url);
        if (!zzaQ(url)) {
            return this.zzpD.zzhU().shouldOverrideUrlLoading(this.zzpD.getWebView(), url);
        }
        zzb.zzaI("shouldOverrideUrlLoading: received passback url");
        return true;
    }

    protected boolean zzaQ(String str) {
        Object zzaR = zzaR(str);
        if (TextUtils.isEmpty(zzaR)) {
            return false;
        }
        try {
            URI uri = new URI(zzaR);
            if ("passback".equals(uri.getScheme())) {
                zzb.zzaI("Passback received");
                this.zzGs.zzgf();
                return true;
            } else if (TextUtils.isEmpty(this.zzOl)) {
                return false;
            } else {
                URI uri2 = new URI(this.zzOl);
                String host = uri2.getHost();
                String host2 = uri.getHost();
                String path = uri2.getPath();
                String path2 = uri.getPath();
                if (!zzw.equal(host, host2) || !zzw.equal(path, path2)) {
                    return false;
                }
                zzb.zzaI("Passback received");
                this.zzGs.zzgf();
                return true;
            }
        } catch (URISyntaxException e) {
            zzb.e(e.getMessage());
            return false;
        }
    }
}
