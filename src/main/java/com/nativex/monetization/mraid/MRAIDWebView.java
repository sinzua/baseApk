package com.nativex.monetization.mraid;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.gson.Gson;
import com.nativex.common.FileConstants;
import com.nativex.common.JsonRequestConstants.RichMedia;
import com.nativex.common.Log;
import com.nativex.common.Utilities;
import com.nativex.monetization.business.CacheFile;
import com.nativex.monetization.business.RichMediaResponseData;
import com.nativex.monetization.communication.ServerRequestManager;
import com.nativex.monetization.enums.AdEvent;
import com.nativex.monetization.enums.FileStatus;
import com.nativex.monetization.listeners.onRichMediaDownloadedListener;
import com.nativex.monetization.manager.CacheDBManager;
import com.nativex.monetization.manager.CacheFileManager;
import com.nativex.monetization.manager.CacheManager;
import com.nativex.monetization.manager.SessionManager;
import com.nativex.monetization.mraid.MRAIDUtils.JSCommands;
import com.nativex.monetization.mraid.MRAIDUtils.JSDialogAction;
import com.supersonicads.sdk.precache.DownloadManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.json.JSONObject;

public class MRAIDWebView extends WebView {
    static final int MODAL_BACKGROUND = Color.argb(128, 10, 10, 10);
    private static final boolean dumpHtmlToSDCard = false;
    public static Boolean forceHardware = null;
    private static String mraidController;
    private static final String sdCardFileName = null;
    private static String sizeScript;
    private boolean adIsMRAID = false;
    private boolean adLoaded = false;
    private boolean adReleased = false;
    private List<String> callsHandled;
    private MRAIDContainer container;
    private StringBuilder downloadedData;
    private int initialLayerType;
    private boolean mraidLoaded = false;
    private MRAIDSchemeHandler schemeHandler;
    private String url;

    private class MRAIDWebChromeClient extends WebChromeClient {
        private MRAIDWebChromeClient() {
        }

        public void onShowCustomView(View view, CustomViewCallback callback) {
            MRAIDLogger.d("WebView is showing custom view - " + view.getClass().getSimpleName());
            super.onShowCustomView(view, callback);
        }

        public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
            onShowCustomView(view, callback);
        }

        public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
            MRAIDLogger.d("WebView is creating a new window");
            if (!dialog) {
                return super.onCreateWindow(view, dialog, userGesture, resultMsg);
            }
            MRAIDLogger.d("Window is dialog");
            return true;
        }

        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            MRAIDWebView.this.container.showJSDialog(url, message, result, JSDialogAction.ALERT);
            return true;
        }

        public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            MRAIDWebView.this.container.showJSDialog(url, message, result, JSDialogAction.BEFORE_UNLOAD);
            return true;
        }

        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            MRAIDWebView.this.container.showJSDialog(url, message, result, JSDialogAction.CONFIRM);
            return true;
        }

        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            MRAIDWebView.this.container.showJSDialog(url, message, result, JSDialogAction.PROMPT);
            return true;
        }

        public boolean onConsoleMessage(@NonNull ConsoleMessage consoleMessage) {
            MRAIDLogger.d("JSMessage: " + consoleMessage.message());
            return true;
        }
    }

    private class MRAIDWebViewClient extends WebViewClient {
        private MRAIDWebViewClient() {
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                if (MRAIDWebView.this.schemeHandler != null) {
                    if (MRAIDWebView.this.schemeHandler.handleScheme(UrlScheme.checkForScheme(url), url, MRAIDWebView.this.callsHandled)) {
                        MRAIDLogger.d(MRAIDWebView.this.container, "Loading custom url scheme");
                        return true;
                    }
                }
                if (MRAIDWebView.this.adIsMRAID) {
                    MRAIDLogger.d(MRAIDWebView.this.container, "Url blocked " + url);
                } else {
                    MRAIDLogger.i(MRAIDWebView.this.container, "Not MRAID AD. Opening url in android browser - " + url);
                    MRAIDUtils.startMRAIDBrowser(MRAIDWebView.this.getContext(), url);
                    MRAIDWebView.this.container.fireListener(AdEvent.USER_NAVIGATES_OUT_OF_APP, "The user clicked on a link in the ad and is navigating out of the app");
                    if (MRAIDWebView.this.container.getWillCloseAdOnRedirect()) {
                        MRAIDManager.releaseAd(MRAIDWebView.this.container);
                    }
                }
            } catch (Exception e) {
                MRAIDLogger.e(MRAIDWebView.this.container, "Error loading url " + url, e);
            }
            return true;
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            MRAIDWebView.this.container.onPageFinished();
            view.clearCache(true);
        }
    }

    public MRAIDWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MRAIDWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MRAIDWebView(Context context) {
        super(context);
        init();
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private void init() {
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setPluginState(PluginState.ON);
        settings.setCacheMode(2);
        settings.setAppCacheEnabled(false);
        if (SessionManager.shouldReplaceWebViewUserAgent()) {
            Log.d("replacing webView user agent..");
            settings.setUserAgentString("Apache-HttpClient/UNAVAILABLE (java 1.4)");
        }
        if (VERSION.SDK_INT >= 17) {
            setVideoAutoPlayEnabled(settings);
        }
        setWebViewClient(new MRAIDWebViewClient());
        setWebChromeClient(new MRAIDWebChromeClient());
        setScrollBarStyle(0);
        this.initialLayerType = getInitialLayerType();
        this.callsHandled = new ArrayList();
        makeTransparent();
    }

    @TargetApi(17)
    private void setVideoAutoPlayEnabled(WebSettings settings) {
        settings.setMediaPlaybackRequiresUserGesture(false);
    }

    int getInitialLayerType() {
        try {
            Method method = getClass().getMethod("getLayerType", new Class[0]);
            method.setAccessible(true);
            return ((Integer) method.invoke(this, new Object[0])).intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    void setSchemeHandler(MRAIDSchemeHandler schemeHandler) {
        this.schemeHandler = schemeHandler;
    }

    void setLayerType(int layerType) {
        if (forceHardware != null) {
            if (forceHardware.booleanValue()) {
                layerType = 2;
            } else {
                layerType = 1;
            }
        }
        try {
            Method method = getClass().getMethod("setLayerType", new Class[]{Integer.TYPE, Paint.class});
            method.setAccessible(true);
            method.invoke(this, new Object[]{Integer.valueOf(layerType), null});
        } catch (Exception e) {
            MRAIDLogger.d("Cannot access setLayerType method in WebView. Must be older API than 11.");
        }
    }

    void setSoftwareLayerType() {
        setLayerType(1);
    }

    private void makeTransparent() {
        setBackgroundColor(0);
        setBackgroundDrawable(new ColorDrawable(0));
    }

    public void setContainer(MRAIDContainer container) {
        this.container = container;
    }

    public void setAdLoaded() {
        this.adLoaded = true;
    }

    boolean isAdLoaded() {
        return this.adLoaded;
    }

    void resetMRAID() {
        setLayerType(this.initialLayerType);
        this.mraidLoaded = false;
        this.adLoaded = false;
        this.adIsMRAID = false;
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        this.container.setIsViewable(visibility == 0);
        if (visibility == 0) {
            setKeepScreenOn(true);
        }
    }

    void runJSCommand(JSCommand command) {
        if (command == null) {
            MRAIDLogger.e("Unable to run JS command");
            return;
        }
        try {
            String jsCommand;
            if (this.mraidLoaded && !this.adReleased) {
                jsCommand = command.getJSCall();
                loadUrl(jsCommand);
                MRAIDLogger.d(jsCommand);
            } else if (command.getCommand() == JSCommands.GET_PAGE_SIZE) {
                jsCommand = command.getJSCall();
                loadUrl(jsCommand);
                Log.d(jsCommand);
            }
        } catch (Exception e) {
            MRAIDLogger.e("Unable to run JS command", e);
        }
    }

    MRAIDContainer getContainer() {
        return this.container;
    }

    void release() {
        try {
            setKeepScreenOn(false);
            destroyDrawingCache();
            if (getParent() != null) {
                ((ViewGroup) getParent()).removeView(this);
                destroy();
            }
            this.adReleased = true;
        } catch (Exception e) {
            MRAIDLogger.e("Failed to release the WebView", e);
        }
    }

    void loadAd(String url) {
        this.url = url;
        if (sdCardFileName != null) {
            loadAdFromSDCard();
        } else {
            loadAdFromServer(url);
        }
    }

    void loadAdFromSDCard() {
        File sdCardFile = new File(new File(Environment.getExternalStorageDirectory().getPath(), "/nativex/mraid/"), sdCardFileName);
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(sdCardFile));
            while (true) {
                String line = br.readLine();
                if (line != null) {
                    builder.append(line);
                    builder.append('\n');
                } else {
                    MRAIDLogger.d("Loading Ad from SD card " + sdCardFile.getName());
                    loadAdData(builder);
                    return;
                }
            }
        } catch (IOException e) {
            MRAIDLogger.e(" ERROR IOException in reading SD Card file");
        }
    }

    void loadAdFromServer(String url) {
        this.url = url;
        ServerRequestManager.getInstance().getRichMedia(url, new onRichMediaDownloadedListener() {
            public void downloadComplete(String httpErrorMsg, String responseData) {
                try {
                    if (MRAIDWebView.this.adReleased) {
                        MRAIDWebView.this.container.fireListener(AdEvent.ERROR, "Ad was released before the content was loaded.");
                        MRAIDLogger.d("Downloading ad finished, but ad container was released.");
                    } else if (httpErrorMsg != null) {
                        if (httpErrorMsg.equals("NO AD")) {
                            MRAIDLogger.d("Downloading ad failed - No ads to show.");
                            MRAIDWebView.this.loadAdFailed();
                            MRAIDWebView.this.container.fireListener(AdEvent.NO_AD, "There was no ad to show.");
                            return;
                        }
                        MRAIDWebView.this.handleLoadAdFailure(Utilities.stringIsEmpty(httpErrorMsg) ? "" : " - " + httpErrorMsg);
                    } else if (responseData != null) {
                        RichMediaResponseData richMediaResponseData = null;
                        boolean responseIsJSON = true;
                        try {
                            richMediaResponseData = (RichMediaResponseData) new Gson().fromJson(responseData, RichMediaResponseData.class);
                        } catch (Exception e) {
                            responseIsJSON = false;
                        }
                        if (!responseIsJSON) {
                            MRAIDLogger.d("Downloading ad finished successfully");
                            MRAIDWebView.this.loadAdData(new StringBuilder(responseData));
                        } else if (richMediaResponseData == null) {
                            MRAIDWebView.this.handleLoadAdFailure("Improper RichMedia response format obtained");
                        } else {
                            long viewTimeOut = richMediaResponseData.getViewTimeOut() * 1000;
                            if (viewTimeOut > 0) {
                                MRAIDManager.setViewTimeOut(MRAIDWebView.this.container, viewTimeOut);
                            }
                            String decodedHtml = null;
                            if (richMediaResponseData.getBase64HtmlContent() != null) {
                                try {
                                    decodedHtml = new String(Base64.decode(richMediaResponseData.getBase64HtmlContent(), 0), DownloadManager.UTF8_CHARSET);
                                } catch (UnsupportedEncodingException e2) {
                                    decodedHtml = null;
                                    MRAIDLogger.e("UnSupportedEncodingException obtained while encoding RichMedia content.");
                                }
                            }
                            if (decodedHtml == null) {
                                MRAIDWebView.this.handleLoadAdFailure("Error in decoding Richmedia Content");
                                return;
                            }
                            MRAIDWebView.this.downloadedData = new StringBuilder(decodedHtml);
                            MRAIDWebView.this.loadAdInfo(responseData);
                            List<String> offerIdList = richMediaResponseData.getOfferIds();
                            if (offerIdList.size() > 0) {
                                Set<String> md5UsedList = new HashSet();
                                synchronized (CacheManager.getExtendedOperationLock()) {
                                    for (String offerIdString : offerIdList) {
                                        for (CacheFile cacheFile : CacheDBManager.getInstance().getReadyCacheFilesForOffer(Long.valueOf(Long.parseLong(offerIdString)).longValue())) {
                                            String urlFromCache = cacheFile.getCDN() + cacheFile.getRelativeUrl();
                                            String cacheFileName = cacheFile.getFileName();
                                            String cachedFilePath = CacheFileManager.getNativeXCacheDirectoryPath() + cacheFileName;
                                            if (CacheFileManager.doesFileExistForCacheFile(cacheFileName)) {
                                                String cachedFilePathForHTML = "file://" + cachedFilePath;
                                                if (MRAIDWebView.this.replaceCachedFilePath(MRAIDWebView.this.downloadedData, cacheFile.getRelativeUrl(), cachedFilePathForHTML)) {
                                                    md5UsedList.add(cacheFile.getMD5());
                                                    CacheDBManager.getInstance().updateFileStatusWithMD5(cacheFile.getMD5(), FileStatus.STATUS_INUSE);
                                                    MRAIDLogger.d("URL match found with local cached file. " + urlFromCache);
                                                }
                                            } else {
                                                MRAIDLogger.e("Cached File is not present in local path. " + cacheFile.getFileName());
                                            }
                                        }
                                    }
                                }
                                if (md5UsedList.size() > 0) {
                                    MRAIDWebView.this.container.setMD5ListUsed(md5UsedList);
                                }
                                MRAIDLogger.d("Downloading ad finished successfully.");
                            } else {
                                MRAIDLogger.d("No offers found in response offers list.");
                            }
                            MRAIDWebView.this.loadAdData(MRAIDWebView.this.downloadedData);
                        }
                    } else {
                        MRAIDWebView.this.handleLoadAdFailure("Either empty or Improper RichMedia response format obtained");
                    }
                } catch (Exception e3) {
                    String str;
                    MRAIDWebView.this.container.fireListener(AdEvent.ERROR, "Error while downloading the ad");
                    StringBuilder append = new StringBuilder().append("Downloading ad failed");
                    if (Utilities.stringIsEmpty(httpErrorMsg)) {
                        str = ".";
                    } else {
                        str = " - " + httpErrorMsg;
                    }
                    MRAIDLogger.e(append.append(str).toString(), e3);
                }
            }
        });
    }

    private void handleLoadAdFailure(String message) {
        MRAIDLogger.d("Downloading ad failed." + message);
        loadAdFailed();
        this.container.fireListener(AdEvent.ERROR, "Error while downloading the ad" + message);
    }

    private void loadAdFailed() {
        if (this.container == null) {
            release();
        } else if (this.container.isSecondPartWebView(this)) {
            resetMRAID();
            this.container.close();
            this.container.fireErrorEvent("Expand failed.", null, JSCommands.EXPAND);
        } else {
            MRAIDManager.releaseAd(this.container);
        }
    }

    private void loadMraidController() {
        try {
            mraidController = new String(Base64.decode(FileConstants.NATIVEX_MRAID_CONTROLLER, 0), DownloadManager.UTF8_CHARSET);
        } catch (IOException e) {
            MRAIDLogger.e("MRAIDContainer: Exception while loading mraid controller from assets", e);
        }
    }

    private void loadSizeScript() {
        try {
            sizeScript = new String(Base64.decode(FileConstants.NATIVEX_SIZE_SCRIPT, 0), DownloadManager.UTF8_CHARSET);
        } catch (IOException e) {
            MRAIDLogger.e("MRAIDContainer: Exception while loading mraid controller from assets", e);
        }
    }

    private void loadAdData(StringBuilder data) {
        if (mraidController == null) {
            loadMraidController();
        }
        if (sizeScript == null) {
            loadSizeScript();
        }
        this.downloadedData = data;
        this.container.sendLoadDataMessage(this);
    }

    synchronized void loadDownloadedData() {
        try {
            if (this.downloadedData == null) {
                if (!this.container.isSecondPartWebView(this)) {
                    this.container.fireListener(AdEvent.ERROR, "Error while downloading the ad");
                }
            } else if (this.adReleased) {
                this.container.fireListener(AdEvent.ERROR, "Ad was released before the content was loaded.");
            } else {
                StringBuilder mraidLoadedData = this.downloadedData;
                if (mraidLoadedData.indexOf("<video") < 0) {
                    MRAIDLogger.i("MRAID has no video. Turning hardware acceleration off");
                    setSoftwareLayerType();
                }
                String searchString = "mraid.js";
                for (int mraidPosition = mraidLoadedData.indexOf("mraid.js"); mraidPosition > -1; mraidPosition = mraidLoadedData.indexOf("mraid.js", mraidPosition + 1)) {
                    int closeBracketPosition = mraidLoadedData.lastIndexOf(">", mraidPosition);
                    int scriptTagPosition = mraidLoadedData.lastIndexOf("<script ", mraidPosition);
                    if (closeBracketPosition < scriptTagPosition) {
                        mraidLoadedData.delete(scriptTagPosition, mraidLoadedData.indexOf("</script>", mraidPosition));
                        mraidLoadedData.insert(scriptTagPosition, "<script>" + mraidController + "\n" + sizeScript);
                        this.adIsMRAID = true;
                        break;
                    }
                }
                if (!this.adIsMRAID) {
                    if (this.container.isSecondPartWebView(this)) {
                        this.container.trackClick(this.url);
                    }
                    loadSizeScriptData(mraidLoadedData);
                    MRAIDLogger.d("Not a MRAID ad.");
                    loadAdFailed();
                    this.container.fireListener(AdEvent.NO_AD, "There was no ad to show.");
                } else if (this.url.startsWith("file:///")) {
                    loadDataWithBaseURL(null, mraidLoadedData.toString(), "text/html", DownloadManager.UTF8_CHARSET, null);
                } else {
                    loadDataWithBaseURL(this.url, mraidLoadedData.toString(), "text/html", DownloadManager.UTF8_CHARSET, null);
                }
            }
        } catch (Exception e) {
            MRAIDLogger.e("Failed to load ad", e);
            loadAdFailed();
            this.container.fireListener(AdEvent.ERROR, "Error while downloading the ad");
        }
    }

    private void loadSizeScriptData(StringBuilder htmlData) {
        if (htmlData != null) {
            int indexOfHtml = htmlData.indexOf("<html");
            if (indexOfHtml > -1) {
                int indexOfHead = htmlData.indexOf("<head");
                if (indexOfHead > -1) {
                    htmlData.insert(htmlData.indexOf("</head>", indexOfHead), "<script>" + sizeScript + "</script>");
                    return;
                } else {
                    htmlData.insert(htmlData.indexOf(">", indexOfHtml) + 1, "<head><script>" + sizeScript + "</script></head>");
                    return;
                }
            }
            htmlData.insert(0, "<html><head><script>" + sizeScript + "</script></head>");
            htmlData.append("</html>");
        }
    }

    public void setMraidLoaded() {
        this.mraidLoaded = true;
    }

    boolean replaceCachedFilePath(StringBuilder html, String relativeUrl, String localFilePath) {
        Pattern pattern = Pattern.compile("(http|https):\\/\\/[^\"\\s']*" + Pattern.quote(relativeUrl), 2);
        if (!pattern.matcher(html).find()) {
            return false;
        }
        String replacedHtml = pattern.matcher(html).replaceAll(localFilePath);
        html.setLength(0);
        html.append(replacedHtml);
        return true;
    }

    private void loadAdInfo(String responseData) {
        try {
            JSONObject responseJSON = new JSONObject(responseData);
            if (responseJSON.has(RichMedia.AD_INFO)) {
                AdInfo adInfo = new AdInfo(responseJSON.getJSONObject(RichMedia.AD_INFO).toString());
                adInfo.setPlacement(this.container.getAdName());
                this.container.setAdInfo(adInfo);
            }
        } catch (Exception e) {
        }
    }

    private void copyHtmlToSDCard(String html) {
        Throwable th;
        File subDirectoryFile = new File(Environment.getExternalStorageDirectory().getPath() + "/nativex/mraid");
        if (!subDirectoryFile.exists()) {
            subDirectoryFile.mkdir();
        }
        FileWriter fileWriter = null;
        try {
            FileWriter fileWriter2 = new FileWriter(new File(Environment.getExternalStorageDirectory().getPath() + "/nativex/mraid/" + getContainer().getAdName().replace(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, "") + ".html"));
            try {
                fileWriter2.write(html);
                fileWriter2.close();
                if (fileWriter2 != null) {
                    try {
                        fileWriter2.close();
                    } catch (Exception e) {
                        fileWriter = fileWriter2;
                        return;
                    }
                }
                fileWriter = fileWriter2;
            } catch (Exception e2) {
                fileWriter = fileWriter2;
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (Exception e3) {
                    }
                }
            } catch (Throwable th2) {
                th = th2;
                fileWriter = fileWriter2;
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (Exception e4) {
                    }
                }
                throw th;
            }
        } catch (Exception e5) {
            if (fileWriter != null) {
                fileWriter.close();
            }
        } catch (Throwable th3) {
            th = th3;
            if (fileWriter != null) {
                fileWriter.close();
            }
            throw th;
        }
    }
}
