package com.ty.followboom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import com.ty.followboom.helpers.AppConfigHelper;
import com.ty.instaview.R;

public class FAQActivity extends Activity implements OnClickListener {
    private static final String TAG = "FAQActivity";
    private ImageView mBackButton;
    private ImageView mDownloadButton;
    private TextView mTitle;
    private WebView mWebView;
    private WebViewClient mWebViewClient = new WebViewClient() {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(FAQActivity.TAG, "url : " + url);
            view.loadUrl(url);
            return false;
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_faq);
        onActivate();
    }

    private void onActivate() {
        this.mBackButton = (ImageView) findViewById(R.id.sidebar_button);
        this.mTitle = (TextView) findViewById(R.id.fragment_title);
        this.mDownloadButton = (ImageView) findViewById(R.id.download_button);
        this.mBackButton.setImageResource(R.drawable.ic_back);
        this.mBackButton.setOnClickListener(this);
        this.mDownloadButton.setVisibility(8);
        this.mTitle.setText(R.string.activity_faq);
        this.mWebView = (WebView) findViewById(R.id.web_view);
        initWebView(this.mWebView);
        this.mWebView.loadUrl(AppConfigHelper.getFAQUrl(this));
    }

    private void initWebView(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        setJavaScriptEnabled(webView, true);
        webSettings.setCacheMode(-1);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webView.setScrollBarStyle(33554432);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(this.mWebViewClient);
        webView.setWebChromeClient(new WebChromeClient());
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private void setJavaScriptEnabled(WebView webView, boolean flag) {
        try {
            webView.getSettings().setJavaScriptEnabled(flag);
        } catch (Exception e) {
            Log.d(TAG, "Exception: " + e.toString());
        }
    }

    public void onClick(View view) {
        if (R.id.sidebar_button == view.getId()) {
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }
    }
}
