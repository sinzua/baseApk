package com.supersonicads.sdk.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.supersonicads.sdk.agent.SupersonicAdsPublisherAgent;
import com.supersonicads.sdk.utils.Constants;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import com.supersonicads.sdk.utils.Logger;
import com.supersonicads.sdk.utils.SupersonicAsyncHttpRequestTask;
import com.supersonicads.sdk.utils.SupersonicSharedPrefHelper;
import java.util.List;

public class OpenUrlActivity extends Activity {
    private static final String TAG = "OpenUrlActivity";
    private static final int WEB_VIEW_VIEW_ID = 1;
    boolean isSecondaryWebview;
    private ProgressBar mProgressBar;
    private String mUrl;
    private SupersonicWebView mWebViewController;
    private RelativeLayout mainLayout;
    private WebView webView = null;

    private class Client extends WebViewClient {
        private Client() {
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            OpenUrlActivity.this.mProgressBar.setVisibility(0);
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            OpenUrlActivity.this.mProgressBar.setVisibility(4);
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            List<String> searchKeys = SupersonicSharedPrefHelper.getSupersonicPrefHelper().getSearchKeys();
            if (!(searchKeys == null || searchKeys.isEmpty())) {
                for (String key : searchKeys) {
                    if (url.contains(key)) {
                        OpenUrlActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                        OpenUrlActivity.this.mWebViewController.interceptedUrlToStore();
                        OpenUrlActivity.this.finish();
                        return true;
                    }
                }
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i(TAG, "onCreate()");
        this.mWebViewController = SupersonicAdsPublisherAgent.getInstance(this).getWebViewController();
        hideActivityTitle();
        hideActivtiyStatusBar();
        Bundle bundle = getIntent().getExtras();
        this.mUrl = bundle.getString(SupersonicWebView.EXTERNAL_URL);
        this.isSecondaryWebview = bundle.getBoolean(SupersonicWebView.SECONDARY_WEB_VIEW);
        this.mainLayout = new RelativeLayout(this);
        setContentView(this.mainLayout, new LayoutParams(-1, -1));
    }

    protected void onResume() {
        super.onResume();
        initializeWebView();
    }

    private void initializeWebView() {
        RelativeLayout.LayoutParams webViewLayoutParams = new RelativeLayout.LayoutParams(-1, -1);
        this.webView = new WebView(this);
        this.webView.setId(1);
        this.webView.setWebViewClient(new WebViewClient());
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.setWebViewClient(new Client());
        this.mainLayout.addView(this.webView, webViewLayoutParams);
        if (VERSION.SDK_INT >= 11) {
            this.mProgressBar = new ProgressBar(new ContextThemeWrapper(this, 16973939));
        } else {
            this.mProgressBar = new ProgressBar(this);
        }
        RelativeLayout.LayoutParams progressBarLayoutParams = new RelativeLayout.LayoutParams(-2, -2);
        progressBarLayoutParams.addRule(13);
        this.mProgressBar.setLayoutParams(progressBarLayoutParams);
        this.mProgressBar.setVisibility(4);
        this.mainLayout.addView(this.mProgressBar);
        loadUrl(this.mUrl);
        if (this.mWebViewController != null) {
            this.mWebViewController.viewableChange(true, ParametersKeys.SECONDARY);
        }
    }

    protected void onPause() {
        super.onPause();
        if (this.mWebViewController != null) {
            this.mWebViewController.viewableChange(false, ParametersKeys.SECONDARY);
            if (this.mainLayout != null) {
                ViewGroup parent = (ViewGroup) this.webView.getParent();
                if (parent.findViewById(1) != null) {
                    parent.removeView(this.webView);
                    this.webView.destroy();
                }
            }
        }
    }

    public void loadUrl(String url) {
        this.webView.stopLoading();
        this.webView.clearHistory();
        try {
            this.webView.loadUrl(url);
        } catch (Throwable e) {
            Logger.e(TAG, "OpenUrlActivity:: loadUrl: " + e.toString());
            new SupersonicAsyncHttpRequestTask().execute(new String[]{Constants.NATIVE_EXCEPTION_BASE_URL + e.getStackTrace()[0].getMethodName()});
        }
    }

    private void hideActivityTitle() {
        requestWindowFeature(1);
    }

    private void hideActivtiyStatusBar() {
        getWindow().setFlags(1024, 1024);
    }

    private void disableTouch() {
        getWindow().addFlags(16);
    }

    public void onBackPressed() {
        if (this.webView.canGoBack()) {
            this.webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public void finish() {
        if (this.isSecondaryWebview) {
            this.mWebViewController.engageEnd(ParametersKeys.SECONDARY_CLOSE);
        }
        super.finish();
    }
}
