package com.google.android.gms.internal;

import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.supersonicads.sdk.precache.DownloadManager;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@zzhb
public class zzgb implements zzfz {
    private final Context mContext;
    final Set<WebView> zzFr = Collections.synchronizedSet(new HashSet());

    public zzgb(Context context) {
        this.mContext = context;
    }

    public void zza(String str, final String str2, final String str3) {
        zzb.zzaI("Fetching assets for the given html");
        zzir.zzMc.post(new Runnable(this) {
            final /* synthetic */ zzgb zzFu;

            public void run() {
                final WebView zzfR = this.zzFu.zzfR();
                zzfR.setWebViewClient(new WebViewClient(this) {
                    final /* synthetic */ AnonymousClass1 zzFv;

                    public void onPageFinished(WebView view, String url) {
                        zzb.zzaI("Loading assets have finished");
                        this.zzFv.zzFu.zzFr.remove(zzfR);
                    }

                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        zzb.zzaK("Loading assets have failed.");
                        this.zzFv.zzFu.zzFr.remove(zzfR);
                    }
                });
                this.zzFu.zzFr.add(zzfR);
                zzfR.loadDataWithBaseURL(str2, str3, "text/html", DownloadManager.UTF8_CHARSET, null);
                zzb.zzaI("Fetching assets finished.");
            }
        });
    }

    public WebView zzfR() {
        WebView webView = new WebView(this.mContext);
        webView.getSettings().setJavaScriptEnabled(true);
        return webView;
    }
}
