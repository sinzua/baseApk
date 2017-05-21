package com.supersonicads.sdk.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.MutableContextWrapper;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.ConsoleMessage;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Toast;
import com.nativex.monetization.mraid.objects.ObjectNames.CalendarEntryData;
import com.supersonic.environment.DeviceStatus;
import com.supersonicads.sdk.data.AdUnitsReady;
import com.supersonicads.sdk.data.AdUnitsState;
import com.supersonicads.sdk.data.SSABCParameters;
import com.supersonicads.sdk.data.SSAEnums.ControllerState;
import com.supersonicads.sdk.data.SSAEnums.DebugMode;
import com.supersonicads.sdk.data.SSAEnums.ProductType;
import com.supersonicads.sdk.data.SSAFile;
import com.supersonicads.sdk.data.SSAObj;
import com.supersonicads.sdk.listeners.OnGenericFunctionListener;
import com.supersonicads.sdk.listeners.OnInterstitialListener;
import com.supersonicads.sdk.listeners.OnOfferWallListener;
import com.supersonicads.sdk.listeners.OnRewardedVideoListener;
import com.supersonicads.sdk.precache.DownloadManager;
import com.supersonicads.sdk.precache.DownloadManager.OnPreCacheCompletion;
import com.supersonicads.sdk.utils.Constants;
import com.supersonicads.sdk.utils.Constants.ErrorCodes;
import com.supersonicads.sdk.utils.Constants.ForceClosePosition;
import com.supersonicads.sdk.utils.Constants.JSMethods;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import com.supersonicads.sdk.utils.Constants.RequestParameters;
import com.supersonicads.sdk.utils.DeviceProperties;
import com.supersonicads.sdk.utils.LocationHelper;
import com.supersonicads.sdk.utils.Logger;
import com.supersonicads.sdk.utils.SDKUtils;
import com.supersonicads.sdk.utils.SupersonicAsyncHttpRequestTask;
import com.supersonicads.sdk.utils.SupersonicSharedPrefHelper;
import com.supersonicads.sdk.utils.SupersonicStorageUtils;
import com.ty.followboom.helpers.VLTools;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SupersonicWebView extends WebView implements OnPreCacheCompletion, DownloadListener {
    public static String APP_IDS = "appIds";
    public static int DISPLAY_WEB_VIEW_INTENT = 0;
    public static String EXTERNAL_URL = "external_url";
    public static String IS_INSTALLED = "isInstalled";
    public static String IS_STORE = "is_store";
    public static String IS_STORE_CLOSE = "is_store_close";
    private static String JSON_KEY_FAIL = "fail";
    private static String JSON_KEY_SUCCESS = "success";
    public static int OPEN_URL_INTENT = 1;
    public static String REQUEST_ID = "requestId";
    public static String RESULT = "result";
    public static String SECONDARY_WEB_VIEW = "secondary_web_view";
    public static String WEBVIEW_TYPE = "webview_type";
    public static int mDebugMode = 0;
    private final String GENERIC_MESSAGE = "We're sorry, some error occurred. we will investigate it";
    private String PUB_TAG = "Supersonic";
    private String TAG = SupersonicWebView.class.getSimpleName();
    private DownloadManager downloadManager;
    private Boolean isKitkatAndAbove = null;
    private boolean isRemoveCloseEventHandler;
    private String mBCAppKey;
    private Map<String, String> mBCExtraParameters;
    private String mBCUserId;
    private String mCacheDirectory;
    private OnWebViewControllerChangeListener mChangeListener;
    private CountDownTimer mCloseEventTimer;
    private BroadcastReceiver mConnectionReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean isWifiConnected = false;
            boolean isMobileConnected = false;
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService("connectivity");
            NetworkInfo networkInfo = connMgr.getNetworkInfo(1);
            if (networkInfo != null) {
                isWifiConnected = networkInfo.isConnected();
            }
            networkInfo = connMgr.getNetworkInfo(0);
            if (networkInfo != null) {
                isMobileConnected = networkInfo.isConnected();
            }
            if (SupersonicWebView.this.mControllerState == ControllerState.Ready) {
                SupersonicWebView.this.deviceStatusChanged(isWifiConnected, isMobileConnected);
            }
        }
    };
    private String mControllerKeyPressed = "interrupt";
    private ControllerState mControllerState = ControllerState.None;
    private View mCustomView;
    private CustomViewCallback mCustomViewCallback;
    private FrameLayout mCustomViewContainer;
    private boolean mGlobalControllerTimeFinish;
    private CountDownTimer mGlobalControllerTimer;
    private int mHiddenForceCloseHeight = 50;
    private String mHiddenForceCloseLocation = ForceClosePosition.TOP_RIGHT;
    private int mHiddenForceCloseWidth = 50;
    private String mISAppKey;
    private Map<String, String> mISExtraParameters;
    private String mISUserId;
    private boolean mISmiss;
    private Boolean mIsInterstitialAvailable = null;
    private FrameLayout mLayout;
    private CountDownTimer mLoadControllerTimer;
    private String mOWAppKey;
    private String mOWCreditsAppKey;
    private boolean mOWCreditsMiss;
    private String mOWCreditsUserId;
    private Map<String, String> mOWExtraParameters;
    private String mOWUserId;
    private boolean mOWmiss;
    private OnGenericFunctionListener mOnGenericFunctionListener;
    private OnInterstitialListener mOnInitInterstitialListener;
    private OnOfferWallListener mOnOfferWallListener;
    private OnRewardedVideoListener mOnRewardedVideoListener;
    private String mOrientationState;
    private boolean mRVmiss;
    private String mRequestParameters;
    private AdUnitsState mSavedState = new AdUnitsState();
    private Object mSavedStateLocker = new Object();
    private State mState;
    private Uri mUri;
    private VideoEventsListener mVideoEventsListener;
    private ChromeClient mWebChromeClient;

    private class ChromeClient extends WebChromeClient {
        private ChromeClient() {
        }

        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            WebView childView = new WebView(view.getContext());
            childView.setWebChromeClient(this);
            childView.setWebViewClient(new FrameBustWebViewClient());
            resultMsg.obj.setWebView(childView);
            resultMsg.sendToTarget();
            Logger.i("onCreateWindow", "onCreateWindow");
            return true;
        }

        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Logger.i("MyApplication", consoleMessage.message() + " -- From line " + consoleMessage.lineNumber() + " of " + consoleMessage.sourceId());
            return true;
        }

        public void onShowCustomView(View view, CustomViewCallback callback) {
            Logger.i("Test", "onShowCustomView");
            SupersonicWebView.this.setVisibility(8);
            if (SupersonicWebView.this.mCustomView != null) {
                Logger.i("Test", "mCustomView != null");
                callback.onCustomViewHidden();
                return;
            }
            Logger.i("Test", "mCustomView == null");
            SupersonicWebView.this.mCustomViewContainer.addView(view);
            SupersonicWebView.this.mCustomView = view;
            SupersonicWebView.this.mCustomViewCallback = callback;
            SupersonicWebView.this.mCustomViewContainer.setVisibility(0);
        }

        public View getVideoLoadingProgressView() {
            FrameLayout frameLayout = new FrameLayout(SupersonicWebView.this.getBaseContext());
            frameLayout.setLayoutParams(new LayoutParams(-1, -1));
            return frameLayout;
        }

        public void onHideCustomView() {
            Logger.i("Test", "onHideCustomView");
            if (SupersonicWebView.this.mCustomView != null) {
                SupersonicWebView.this.mCustomView.setVisibility(8);
                SupersonicWebView.this.mCustomViewContainer.removeView(SupersonicWebView.this.mCustomView);
                SupersonicWebView.this.mCustomView = null;
                SupersonicWebView.this.mCustomViewContainer.setVisibility(8);
                SupersonicWebView.this.mCustomViewCallback.onCustomViewHidden();
                SupersonicWebView.this.setVisibility(0);
            }
        }
    }

    private class FrameBustWebViewClient extends WebViewClient {
        private FrameBustWebViewClient() {
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Intent intent = new Intent(view.getContext(), OpenUrlActivity.class);
            intent.putExtra(SupersonicWebView.EXTERNAL_URL, url);
            intent.putExtra(SupersonicWebView.SECONDARY_WEB_VIEW, false);
            view.getContext().startActivity(intent);
            return true;
        }
    }

    public class JSInterface {
        volatile int udiaResults = 0;

        public JSInterface(Context context) {
        }

        @JavascriptInterface
        public void initController(String value) {
            Logger.i(SupersonicWebView.this.TAG, "initController(" + value + ")");
            SSAObj ssaObj = new SSAObj(value);
            if (ssaObj.containsKey(ParametersKeys.STAGE)) {
                String stage = ssaObj.getString(ParametersKeys.STAGE);
                if (ParametersKeys.READY.equalsIgnoreCase(stage)) {
                    SupersonicWebView.this.mControllerState = ControllerState.Ready;
                    SupersonicWebView.this.mGlobalControllerTimer.cancel();
                    SupersonicWebView.this.mLoadControllerTimer.cancel();
                    if (SupersonicWebView.this.mRVmiss) {
                        SupersonicWebView.this.initBrandConnect(SupersonicWebView.this.mBCAppKey, SupersonicWebView.this.mBCUserId, SupersonicWebView.this.mBCExtraParameters, SupersonicWebView.this.mOnRewardedVideoListener);
                    }
                    if (SupersonicWebView.this.mISmiss) {
                        SupersonicWebView.this.initInterstitial(SupersonicWebView.this.mISAppKey, SupersonicWebView.this.mISUserId, SupersonicWebView.this.mISExtraParameters, SupersonicWebView.this.mOnInitInterstitialListener);
                    }
                    if (SupersonicWebView.this.mOWmiss) {
                        SupersonicWebView.this.initOfferWall(SupersonicWebView.this.mOWAppKey, SupersonicWebView.this.mOWUserId, SupersonicWebView.this.mOWExtraParameters, SupersonicWebView.this.mOnOfferWallListener);
                    }
                    if (SupersonicWebView.this.mOWCreditsMiss) {
                        SupersonicWebView.this.getOfferWallCredits(SupersonicWebView.this.mOWCreditsAppKey, SupersonicWebView.this.mOWCreditsUserId, SupersonicWebView.this.mOnOfferWallListener);
                    }
                    SupersonicWebView.this.restoreState(SupersonicWebView.this.mSavedState);
                } else if (ParametersKeys.LOADED.equalsIgnoreCase(stage)) {
                    SupersonicWebView.this.mControllerState = ControllerState.Loaded;
                } else if ("failed".equalsIgnoreCase(stage)) {
                    SupersonicWebView.this.mControllerState = ControllerState.Failed;
                    if (SupersonicWebView.this.mRVmiss) {
                        SupersonicWebView.this.sendProductErrorMessage(ProductType.BrandConnect);
                    }
                    if (SupersonicWebView.this.mISmiss) {
                        SupersonicWebView.this.sendProductErrorMessage(ProductType.Interstitial);
                    }
                    if (SupersonicWebView.this.mOWmiss) {
                        SupersonicWebView.this.sendProductErrorMessage(ProductType.OfferWall);
                    }
                    if (SupersonicWebView.this.mOWCreditsMiss) {
                        SupersonicWebView.this.sendProductErrorMessage(ProductType.OfferWallCredits);
                    }
                } else {
                    Logger.i(SupersonicWebView.this.TAG, "No STAGE mentioned! Should not get here!");
                }
            }
        }

        @JavascriptInterface
        public void alert(String message) {
        }

        @JavascriptInterface
        public void getDeviceStatus(String value) {
            Logger.i(SupersonicWebView.this.TAG, "getDeviceStatus(" + value + ")");
            String successFunToCall = SupersonicWebView.this.extractSuccessFunctionToCall(value);
            String failFunToCall = SupersonicWebView.this.extractFailFunctionToCall(value);
            Object[] resultArr = new Object[2];
            resultArr = SupersonicWebView.this.getDeviceParams(SupersonicWebView.this.getBaseContext());
            String params = resultArr[0];
            String funToCall = null;
            if (((Boolean) resultArr[1]).booleanValue()) {
                if (!TextUtils.isEmpty(failFunToCall)) {
                    funToCall = failFunToCall;
                }
            } else if (!TextUtils.isEmpty(successFunToCall)) {
                funToCall = successFunToCall;
            }
            if (!TextUtils.isEmpty(funToCall)) {
                SupersonicWebView.this.injectJavascript(SupersonicWebView.this.generateJSToInject(funToCall, params, JSMethods.ON_GET_DEVICE_STATUS_SUCCESS, JSMethods.ON_GET_DEVICE_STATUS_FAIL));
            }
        }

        @JavascriptInterface
        public void getApplicationInfo(String value) {
            Logger.i(SupersonicWebView.this.TAG, "getApplicationInfo(" + value + ")");
            String successFunToCall = SupersonicWebView.this.extractSuccessFunctionToCall(value);
            String failFunToCall = SupersonicWebView.this.extractFailFunctionToCall(value);
            String funToCall = null;
            Object[] resultArr = new Object[2];
            resultArr = SupersonicWebView.this.getApplicationParams(new SSAObj(value).getString(ParametersKeys.PRODUCT_TYPE));
            String params = resultArr[0];
            if (((Boolean) resultArr[1]).booleanValue()) {
                if (!TextUtils.isEmpty(failFunToCall)) {
                    funToCall = failFunToCall;
                }
            } else if (!TextUtils.isEmpty(successFunToCall)) {
                funToCall = successFunToCall;
            }
            if (!TextUtils.isEmpty(funToCall)) {
                SupersonicWebView.this.injectJavascript(SupersonicWebView.this.generateJSToInject(funToCall, params, JSMethods.ON_GET_APPLICATION_INFO_SUCCESS, JSMethods.ON_GET_APPLICATION_INFO_FAIL));
            }
        }

        @JavascriptInterface
        public void checkInstalledApps(String value) {
            Logger.i(SupersonicWebView.this.TAG, "checkInstalledApps(" + value + ")");
            String successFunToCall = SupersonicWebView.this.extractSuccessFunctionToCall(value);
            String failFunToCall = SupersonicWebView.this.extractFailFunctionToCall(value);
            String funToCall = null;
            SSAObj ssaObj = new SSAObj(value);
            Object[] resultArr = SupersonicWebView.this.getAppsStatus(ssaObj.getString(SupersonicWebView.APP_IDS), ssaObj.getString(SupersonicWebView.REQUEST_ID));
            String params = resultArr[0];
            if (((Boolean) resultArr[1]).booleanValue()) {
                if (!TextUtils.isEmpty(failFunToCall)) {
                    funToCall = failFunToCall;
                }
            } else if (!TextUtils.isEmpty(successFunToCall)) {
                funToCall = successFunToCall;
            }
            if (!TextUtils.isEmpty(funToCall)) {
                SupersonicWebView.this.injectJavascript(SupersonicWebView.this.generateJSToInject(funToCall, params, JSMethods.ON_CHECK_INSTALLED_APPS_SUCCESS, JSMethods.ON_CHECK_INSTALLED_APPS_FAIL));
            }
        }

        @JavascriptInterface
        public void saveFile(String value) {
            Logger.i(SupersonicWebView.this.TAG, "saveFile(" + value + ")");
            SSAFile ssaFile = new SSAFile(value);
            if (SDKUtils.getAvailableSpaceInMB(SupersonicWebView.this.getBaseContext(), SupersonicWebView.this.mCacheDirectory) <= 0) {
                SupersonicWebView.this.responseBack(value, false, DownloadManager.NO_DISK_SPACE, null);
            } else if (!SupersonicStorageUtils.isExternalStorageAvailable()) {
                SupersonicWebView.this.responseBack(value, false, DownloadManager.SOTRAGE_UNAVAILABLE, null);
            } else if (SupersonicStorageUtils.isFileCached(SupersonicWebView.this.mCacheDirectory, ssaFile)) {
                SupersonicWebView.this.responseBack(value, false, DownloadManager.FILE_ALREADY_EXIST, null);
            } else if (SDKUtils.isOnline(SupersonicWebView.this.getBaseContext())) {
                SupersonicWebView.this.responseBack(value, true, null, null);
                String lastUpdateTimeObj = ssaFile.getLastUpdateTime();
                if (lastUpdateTimeObj != null) {
                    String lastUpdateTimeStr = String.valueOf(lastUpdateTimeObj);
                    if (!TextUtils.isEmpty(lastUpdateTimeStr)) {
                        String folder;
                        String path = ssaFile.getPath();
                        if (path.contains("/")) {
                            String[] splitArr = ssaFile.getPath().split("/");
                            folder = splitArr[splitArr.length - 1];
                        } else {
                            folder = path;
                        }
                        SupersonicSharedPrefHelper.getSupersonicPrefHelper().setCampaignLastUpdate(folder, lastUpdateTimeStr);
                    }
                }
                SupersonicWebView.this.downloadManager.downloadFile(ssaFile);
            } else {
                SupersonicWebView.this.responseBack(value, false, DownloadManager.NO_NETWORK_CONNECTION, null);
            }
        }

        @JavascriptInterface
        public void adUnitsReady(String value) {
            Logger.i(SupersonicWebView.this.TAG, "adUnitsReady(" + value + ")");
            final AdUnitsReady adUnitsReady = new AdUnitsReady(value);
            if (adUnitsReady.isNumOfAdUnitsExist()) {
                SupersonicWebView.this.responseBack(value, true, null, null);
                final String product = adUnitsReady.getProductType();
                if (SupersonicWebView.this.shouldNotifyDeveloper(product)) {
                    Context ctx = SupersonicWebView.this.getBaseContext();
                    if (ctx instanceof Activity) {
                        ((Activity) ctx).runOnUiThread(new Runnable() {
                            public void run() {
                                boolean fireSuccess;
                                if (Integer.parseInt(adUnitsReady.getNumOfAdUnits()) > 0) {
                                    fireSuccess = true;
                                } else {
                                    fireSuccess = false;
                                }
                                if (product.equalsIgnoreCase(ProductType.BrandConnect.toString())) {
                                    SupersonicWebView.this.mSavedState.setRewardedVideoInitSuccess(true);
                                    if (fireSuccess) {
                                        Log.d(SupersonicWebView.this.TAG, "onRVInitSuccess()");
                                        SupersonicWebView.this.mOnRewardedVideoListener.onRVInitSuccess(adUnitsReady);
                                        return;
                                    }
                                    SupersonicWebView.this.mOnRewardedVideoListener.onRVNoMoreOffers();
                                }
                            }
                        });
                        return;
                    }
                    return;
                }
                return;
            }
            SupersonicWebView.this.responseBack(value, false, ErrorCodes.NUM_OF_AD_UNITS_DO_NOT_EXIST, null);
        }

        @JavascriptInterface
        public void deleteFolder(String value) {
            Logger.i(SupersonicWebView.this.TAG, "deleteFolder(" + value + ")");
            SSAFile file = new SSAFile(value);
            if (SupersonicStorageUtils.isPathExist(SupersonicWebView.this.mCacheDirectory, file.getPath())) {
                SupersonicWebView.this.responseBack(value, SupersonicStorageUtils.deleteFolder(SupersonicWebView.this.mCacheDirectory, file.getPath()), null, null);
                return;
            }
            SupersonicWebView.this.responseBack(value, false, ErrorCodes.FOLDER_NOT_EXIST_MSG, "1");
        }

        @JavascriptInterface
        public void deleteFile(String value) {
            Logger.i(SupersonicWebView.this.TAG, "deleteFile(" + value + ")");
            SSAFile file = new SSAFile(value);
            if (SupersonicStorageUtils.isPathExist(SupersonicWebView.this.mCacheDirectory, file.getPath())) {
                SupersonicWebView.this.responseBack(value, SupersonicStorageUtils.deleteFile(SupersonicWebView.this.mCacheDirectory, file.getPath(), file.getFile()), null, null);
                return;
            }
            SupersonicWebView.this.responseBack(value, false, ErrorCodes.FILE_NOT_EXIST_MSG, "1");
        }

        @JavascriptInterface
        public void displayWebView(String value) {
            Logger.i(SupersonicWebView.this.TAG, "displayWebView(" + value + ")");
            SupersonicWebView.this.responseBack(value, true, null, null);
            SSAObj ssaObj = new SSAObj(value);
            boolean display = ((Boolean) ssaObj.get(ParametersKeys.DISPLAY)).booleanValue();
            String productType = ssaObj.getString(ParametersKeys.PRODUCT_TYPE);
            boolean isRewardedVideo = false;
            if (!display) {
                SupersonicWebView.this.setState(State.Gone);
                SupersonicWebView.this.closeWebView();
            } else if (SupersonicWebView.this.getState() != State.Display) {
                Intent intent;
                SupersonicWebView.this.setState(State.Display);
                Logger.i(SupersonicWebView.this.TAG, "State: " + SupersonicWebView.this.mState);
                Context mContext = SupersonicWebView.this.getBaseContext();
                String orientation = SupersonicWebView.this.getOrientationState();
                int rotation = SDKUtils.getApplicationRotation(mContext);
                if (productType.equalsIgnoreCase(ProductType.Interstitial.toString())) {
                    intent = new Intent(mContext, InterstitialActivity.class);
                } else {
                    intent = new Intent(mContext, ControllerActivity.class);
                    if (ProductType.BrandConnect.toString().equalsIgnoreCase(productType)) {
                        isRewardedVideo = true;
                        intent.putExtra(ParametersKeys.PRODUCT_TYPE, ProductType.BrandConnect.toString());
                        SupersonicWebView.this.mSavedState.adOpened(ProductType.BrandConnect.ordinal());
                    } else {
                        intent.putExtra(ParametersKeys.PRODUCT_TYPE, ProductType.OfferWall.toString());
                        SupersonicWebView.this.mSavedState.adOpened(ProductType.OfferWall.ordinal());
                    }
                }
                if (isRewardedVideo && SupersonicWebView.this.shouldNotifyDeveloper(ProductType.BrandConnect.toString())) {
                    SupersonicWebView.this.mOnRewardedVideoListener.onRVAdOpened();
                }
                intent.putExtra(ParametersKeys.ORIENTATION_SET_FLAG, orientation);
                intent.putExtra(ParametersKeys.ROTATION_SET_FLAG, rotation);
                mContext.startActivity(intent);
            } else {
                Logger.i(SupersonicWebView.this.TAG, "State: " + SupersonicWebView.this.mState);
            }
        }

        @JavascriptInterface
        public void getOrientation(String value) {
            String funToCall = SupersonicWebView.this.extractSuccessFunctionToCall(value);
            String params = SDKUtils.getOrientation(SupersonicWebView.this.getBaseContext()).toString();
            if (!TextUtils.isEmpty(funToCall)) {
                SupersonicWebView.this.injectJavascript(SupersonicWebView.this.generateJSToInject(funToCall, params, JSMethods.ON_GET_ORIENTATION_SUCCESS, JSMethods.ON_GET_ORIENTATION_FAIL));
            }
        }

        @JavascriptInterface
        public void setOrientation(String value) {
            Logger.i(SupersonicWebView.this.TAG, "setOrientation(" + value + ")");
            String orientation = new SSAObj(value).getString(ParametersKeys.ORIENTATION);
            SupersonicWebView.this.setOrientationState(orientation);
            int rotation = SDKUtils.getApplicationRotation(SupersonicWebView.this.getBaseContext());
            if (SupersonicWebView.this.mChangeListener != null) {
                SupersonicWebView.this.mChangeListener.onSetOrientationCalled(orientation, rotation);
            }
        }

        @JavascriptInterface
        public void getCachedFilesMap(String value) {
            Logger.i(SupersonicWebView.this.TAG, "getCachedFilesMap(" + value + ")");
            String funToCall = SupersonicWebView.this.extractSuccessFunctionToCall(value);
            if (!TextUtils.isEmpty(funToCall)) {
                SSAObj ssaObj = new SSAObj(value);
                if (ssaObj.containsKey("path")) {
                    String mapPath = (String) ssaObj.get("path");
                    if (SupersonicStorageUtils.isPathExist(SupersonicWebView.this.mCacheDirectory, mapPath)) {
                        SupersonicWebView.this.injectJavascript(SupersonicWebView.this.generateJSToInject(funToCall, SupersonicStorageUtils.getCachedFilesMap(SupersonicWebView.this.mCacheDirectory, mapPath), JSMethods.ON_GET_CACHED_FILES_MAP_SUCCESS, JSMethods.ON_GET_CACHED_FILES_MAP_FAIL));
                        return;
                    }
                    SupersonicWebView.this.responseBack(value, false, ErrorCodes.PATH_FILE_DOES_NOT_EXIST_ON_DISK, null);
                    return;
                }
                SupersonicWebView.this.responseBack(value, false, ErrorCodes.PATH_KEY_DOES_NOT_EXIST, null);
            }
        }

        @JavascriptInterface
        public void adCredited(String value) {
            String appKey;
            String userId;
            Log.d(SupersonicWebView.this.PUB_TAG, "adCredited(" + value + ")");
            SSAObj sSAObj = new SSAObj(value);
            String creditsStr = sSAObj.getString(ParametersKeys.CREDITS);
            final int credits = creditsStr != null ? Integer.parseInt(creditsStr) : 0;
            String totalCreditsStr = sSAObj.getString(ParametersKeys.TOTAL);
            final int totalCredits = totalCreditsStr != null ? Integer.parseInt(totalCreditsStr) : 0;
            final String product = sSAObj.getString(ParametersKeys.PRODUCT_TYPE);
            boolean totalCreditsFlag = false;
            String latestCompeltionsTime = null;
            boolean md5Signature = false;
            if (sSAObj.getBoolean("externalPoll")) {
                appKey = SupersonicWebView.this.mOWCreditsAppKey;
                userId = SupersonicWebView.this.mOWCreditsUserId;
            } else {
                appKey = SupersonicWebView.this.mOWAppKey;
                userId = SupersonicWebView.this.mOWUserId;
            }
            if (product.equalsIgnoreCase(ProductType.OfferWall.toString())) {
                if (!sSAObj.isNull("signature")) {
                    if (!sSAObj.isNull("timestamp")) {
                        if (!sSAObj.isNull("totalCreditsFlag")) {
                            if (sSAObj.getString("signature").equalsIgnoreCase(SDKUtils.getMD5(totalCreditsStr + appKey + userId))) {
                                md5Signature = true;
                            } else {
                                SupersonicWebView.this.responseBack(value, false, "Controller signature is not equal to SDK signature", null);
                            }
                            totalCreditsFlag = sSAObj.getBoolean("totalCreditsFlag");
                            latestCompeltionsTime = sSAObj.getString("timestamp");
                        }
                    }
                }
                SupersonicWebView.this.responseBack(value, false, "One of the keys are missing: signature/timestamp/totalCreditsFlag", null);
                return;
            }
            if (SupersonicWebView.this.shouldNotifyDeveloper(product)) {
                Context ctx = SupersonicWebView.this.getBaseContext();
                if (ctx instanceof Activity) {
                    final boolean mTotalCreditsFlag = totalCreditsFlag;
                    final String mlatestCompeltionsTime = latestCompeltionsTime;
                    final boolean mMd5Signature = md5Signature;
                    final String str = value;
                    ((Activity) ctx).runOnUiThread(new Runnable() {
                        public void run() {
                            if (product.equalsIgnoreCase(ProductType.BrandConnect.toString())) {
                                SupersonicWebView.this.mOnRewardedVideoListener.onRVAdCredited(credits);
                            } else if (!product.equalsIgnoreCase(ProductType.OfferWall.toString()) || !mMd5Signature || !SupersonicWebView.this.mOnOfferWallListener.onOWAdCredited(credits, totalCredits, mTotalCreditsFlag) || TextUtils.isEmpty(mlatestCompeltionsTime)) {
                            } else {
                                if (SupersonicSharedPrefHelper.getSupersonicPrefHelper().setLatestCompeltionsTime(mlatestCompeltionsTime, appKey, userId)) {
                                    SupersonicWebView.this.responseBack(str, true, null, null);
                                } else {
                                    SupersonicWebView.this.responseBack(str, false, "Time Stamp could not be stored", null);
                                }
                            }
                        }
                    });
                }
            }
        }

        @JavascriptInterface
        public void removeCloseEventHandler(String value) {
            Logger.i(SupersonicWebView.this.TAG, "removeCloseEventHandler(" + value + ")");
            if (SupersonicWebView.this.mCloseEventTimer != null) {
                SupersonicWebView.this.mCloseEventTimer.cancel();
            }
            SupersonicWebView.this.isRemoveCloseEventHandler = true;
        }

        @JavascriptInterface
        public void onGetDeviceStatusSuccess(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onGetDeviceStatusSuccess(" + value + ")");
            SupersonicWebView.this.responseBack(value, true, null, null);
            SupersonicWebView.this.toastingErrMsg(JSMethods.ON_GET_DEVICE_STATUS_SUCCESS, value);
        }

        @JavascriptInterface
        public void onGetDeviceStatusFail(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onGetDeviceStatusFail(" + value + ")");
            SupersonicWebView.this.responseBack(value, true, null, null);
            SupersonicWebView.this.toastingErrMsg(JSMethods.ON_GET_DEVICE_STATUS_FAIL, value);
        }

        @JavascriptInterface
        public void onInitBrandConnectSuccess(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onInitBrandConnectSuccess(" + value + ")");
            SupersonicSharedPrefHelper.getSupersonicPrefHelper().setSSABCParameters(new SSABCParameters(value));
            SupersonicWebView.this.responseBack(value, true, null, null);
            SupersonicWebView.this.toastingErrMsg(JSMethods.ON_INIT_BRAND_CONNECT_SUCCESS, value);
        }

        @JavascriptInterface
        public void onInitBrandConnectFail(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onInitBrandConnectFail(" + value + ")");
            final String message = new SSAObj(value).getString(ParametersKeys.ERR_MSG);
            SupersonicWebView.this.mSavedState.setRewardedVideoInitSuccess(false);
            if (SupersonicWebView.this.shouldNotifyDeveloper(ProductType.BrandConnect.toString())) {
                Context ctx = SupersonicWebView.this.getBaseContext();
                if (ctx instanceof Activity) {
                    ((Activity) ctx).runOnUiThread(new Runnable() {
                        public void run() {
                            String toSend = message;
                            if (toSend == null) {
                                toSend = "We're sorry, some error occurred. we will investigate it";
                            }
                            Log.d(SupersonicWebView.this.TAG, "onRVInitFail(message:" + message + ")");
                            SupersonicWebView.this.mOnRewardedVideoListener.onRVInitFail(toSend);
                        }
                    });
                }
            }
            SupersonicWebView.this.responseBack(value, true, null, null);
            SupersonicWebView.this.toastingErrMsg(JSMethods.ON_INIT_BRAND_CONNECT_FAIL, value);
        }

        @JavascriptInterface
        public void onGetApplicationInfoSuccess(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onGetApplicationInfoSuccess(" + value + ")");
            SupersonicWebView.this.responseBack(value, true, null, null);
            SupersonicWebView.this.toastingErrMsg(JSMethods.ON_GET_APPLICATION_INFO_SUCCESS, value);
        }

        @JavascriptInterface
        public void onGetApplicationInfoFail(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onGetApplicationInfoFail(" + value + ")");
            SupersonicWebView.this.responseBack(value, true, null, null);
            SupersonicWebView.this.toastingErrMsg(JSMethods.ON_GET_APPLICATION_INFO_FAIL, value);
        }

        @JavascriptInterface
        public void onShowBrandConnectSuccess(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onShowBrandConnectSuccess(" + value + ")");
            SupersonicWebView.this.responseBack(value, true, null, null);
            SupersonicWebView.this.toastingErrMsg(JSMethods.ON_SHOW_BRAND_CONNECT_SUCCESS, value);
        }

        @JavascriptInterface
        public void onShowBrandConnectFail(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onShowBrandConnectFail(" + value + ")");
            final String message = new SSAObj(value).getString(ParametersKeys.ERR_MSG);
            if (SupersonicWebView.this.shouldNotifyDeveloper(ProductType.BrandConnect.toString())) {
                Context ctx = SupersonicWebView.this.getBaseContext();
                if (ctx instanceof Activity) {
                    ((Activity) ctx).runOnUiThread(new Runnable() {
                        public void run() {
                            String toSend = message;
                            if (toSend == null) {
                                toSend = "We're sorry, some error occurred. we will investigate it";
                            }
                            Log.d(SupersonicWebView.this.TAG, "onRVShowFail(message:" + message + ")");
                            SupersonicWebView.this.mOnRewardedVideoListener.onRVShowFail(toSend);
                        }
                    });
                }
            }
            SupersonicWebView.this.responseBack(value, true, null, null);
            SupersonicWebView.this.toastingErrMsg(JSMethods.ON_SHOW_BRAND_CONNECT_FAIL, value);
        }

        @JavascriptInterface
        public void onGetCachedFilesMapSuccess(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onGetCachedFilesMapSuccess(" + value + ")");
            SupersonicWebView.this.responseBack(value, true, null, null);
            SupersonicWebView.this.toastingErrMsg(JSMethods.ON_GET_CACHED_FILES_MAP_SUCCESS, value);
        }

        @JavascriptInterface
        public void onGetCachedFilesMapFail(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onGetCachedFilesMapFail(" + value + ")");
            SupersonicWebView.this.responseBack(value, true, null, null);
            SupersonicWebView.this.toastingErrMsg(JSMethods.ON_GET_CACHED_FILES_MAP_FAIL, value);
        }

        @JavascriptInterface
        public void onShowOfferWallSuccess(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onShowOfferWallSuccess(" + value + ")");
            SupersonicWebView.this.mSavedState.adOpened(ProductType.OfferWall.ordinal());
            if (SupersonicWebView.this.shouldNotifyDeveloper(ProductType.OfferWall.toString())) {
                Context ctx = SupersonicWebView.this.getBaseContext();
                if (ctx instanceof Activity) {
                    ((Activity) ctx).runOnUiThread(new Runnable() {
                        public void run() {
                            SupersonicWebView.this.mOnOfferWallListener.onOWShowSuccess();
                        }
                    });
                }
            }
            SupersonicWebView.this.responseBack(value, true, null, null);
            SupersonicWebView.this.toastingErrMsg(JSMethods.ON_SHOW_OFFER_WALL_SUCCESS, value);
        }

        @JavascriptInterface
        public void onShowOfferWallFail(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onShowOfferWallFail(" + value + ")");
            final String message = new SSAObj(value).getString(ParametersKeys.ERR_MSG);
            if (SupersonicWebView.this.shouldNotifyDeveloper(ProductType.OfferWall.toString())) {
                Context ctx = SupersonicWebView.this.getBaseContext();
                if (ctx instanceof Activity) {
                    ((Activity) ctx).runOnUiThread(new Runnable() {
                        public void run() {
                            String toSend = message;
                            if (toSend == null) {
                                toSend = "We're sorry, some error occurred. we will investigate it";
                            }
                            SupersonicWebView.this.mOnOfferWallListener.onOWShowFail(toSend);
                        }
                    });
                }
            }
            SupersonicWebView.this.responseBack(value, true, null, null);
            SupersonicWebView.this.toastingErrMsg(JSMethods.ON_SHOW_OFFER_WALL_FAIL, value);
        }

        @JavascriptInterface
        public void onInitInterstitialSuccess(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onInitInterstitialSuccess()");
            SupersonicWebView.this.toastingErrMsg(JSMethods.ON_INIT_INTERSTITIAL_SUCCESS, "true");
            SupersonicWebView.this.mSavedState.setInterstitialInitSuccess(true);
            if (SupersonicWebView.this.mSavedState.reportInitInterstitial()) {
                SupersonicWebView.this.mSavedState.setReportInitInterstitial(false);
                if (SupersonicWebView.this.shouldNotifyDeveloper(ProductType.Interstitial.toString())) {
                    Context ctx = SupersonicWebView.this.getBaseContext();
                    if (ctx instanceof Activity) {
                        ((Activity) ctx).runOnUiThread(new Runnable() {
                            public void run() {
                                Log.d(SupersonicWebView.this.TAG, "onInterstitialInitSuccess()");
                                SupersonicWebView.this.mOnInitInterstitialListener.onInterstitialInitSuccess();
                            }
                        });
                    }
                }
            }
        }

        @JavascriptInterface
        public void onInitInterstitialFail(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onInitInterstitialFail(" + value + ")");
            SupersonicWebView.this.mSavedState.setInterstitialInitSuccess(false);
            final String message = new SSAObj(value).getString(ParametersKeys.ERR_MSG);
            if (SupersonicWebView.this.mSavedState.reportInitInterstitial()) {
                SupersonicWebView.this.mSavedState.setReportInitInterstitial(false);
                if (SupersonicWebView.this.shouldNotifyDeveloper(ProductType.Interstitial.toString())) {
                    Context ctx = SupersonicWebView.this.getBaseContext();
                    if (ctx instanceof Activity) {
                        ((Activity) ctx).runOnUiThread(new Runnable() {
                            public void run() {
                                String toSend = message;
                                if (toSend == null) {
                                    toSend = "We're sorry, some error occurred. we will investigate it";
                                }
                                Log.d(SupersonicWebView.this.TAG, "onInterstitialInitFail(message:" + toSend + ")");
                                SupersonicWebView.this.mOnInitInterstitialListener.onInterstitialInitFail(toSend);
                            }
                        });
                    }
                }
            }
            SupersonicWebView.this.responseBack(value, true, null, null);
            SupersonicWebView.this.toastingErrMsg(JSMethods.ON_INIT_INTERSTITIAL_FAIL, value);
        }

        @JavascriptInterface
        public void interstitialAvailability(String value) {
            Log.d(SupersonicWebView.this.TAG, "interstitialAvailability(" + value + ")");
            changeInterstitialState(Boolean.parseBoolean(new SSAObj(value).getString(ParametersKeys.AVAILABLE)), ProductType.Interstitial.toString());
        }

        protected synchronized void changeInterstitialState(boolean available, String product) {
            boolean notify = false;
            if (SupersonicWebView.this.mIsInterstitialAvailable == null) {
                SupersonicWebView.this.mIsInterstitialAvailable = Boolean.valueOf(available);
                notify = true;
            } else {
                if (available) {
                    if (!SupersonicWebView.this.mIsInterstitialAvailable.booleanValue()) {
                        SupersonicWebView.this.mIsInterstitialAvailable = Boolean.valueOf(true);
                        notify = true;
                    }
                }
                if (!available && SupersonicWebView.this.mIsInterstitialAvailable.booleanValue()) {
                    notify = true;
                    SupersonicWebView.this.mIsInterstitialAvailable = Boolean.valueOf(false);
                }
            }
            if (notify && SupersonicWebView.this.shouldNotifyDeveloper(product)) {
                SupersonicWebView.this.mOnInitInterstitialListener.onInterstitialAvailability(SupersonicWebView.this.mIsInterstitialAvailable.booleanValue());
                SupersonicWebView.this.toastingErrMsg(JSMethods.ON_INTERSTITIAL_AVAILABILITY, String.valueOf(available));
            }
        }

        @JavascriptInterface
        public void adClicked(String value) {
            Logger.i(SupersonicWebView.this.TAG, "adClicked(" + value + ")");
            if (SupersonicWebView.this.shouldNotifyDeveloper(ProductType.Interstitial.toString())) {
                Context ctx = SupersonicWebView.this.getBaseContext();
                if (ctx instanceof Activity) {
                    ((Activity) ctx).runOnUiThread(new Runnable() {
                        public void run() {
                            SupersonicWebView.this.mOnInitInterstitialListener.onInterstitialAdClicked();
                        }
                    });
                }
                SupersonicWebView.this.toastingErrMsg(JSMethods.ON_INTERSTITIAL_AD_CLICKED, value);
            }
        }

        @JavascriptInterface
        public void onShowInterstitialSuccess(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onShowInterstitialSuccess(" + value + ")");
            SupersonicWebView.this.mSavedState.adOpened(ProductType.Interstitial.ordinal());
            SupersonicWebView.this.responseBack(value, true, null, null);
            if (SupersonicWebView.this.shouldNotifyDeveloper(ProductType.Interstitial.toString())) {
                Context ctx = SupersonicWebView.this.getBaseContext();
                if (ctx instanceof Activity) {
                    ((Activity) ctx).runOnUiThread(new Runnable() {
                        public void run() {
                            SupersonicWebView.this.mOnInitInterstitialListener.onInterstitialShowSuccess();
                        }
                    });
                }
                SupersonicWebView.this.toastingErrMsg(JSMethods.ON_SHOW_INTERSTITIAL_SUCCESS, value);
            }
        }

        @JavascriptInterface
        public void onInitOfferWallSuccess(String value) {
            SupersonicWebView.this.toastingErrMsg(JSMethods.ON_INIT_OFFERWALL_SUCCESS, "true");
            SupersonicWebView.this.mSavedState.setOfferwallInitSuccess(true);
            if (SupersonicWebView.this.mSavedState.reportInitOfferwall()) {
                SupersonicWebView.this.mSavedState.setOfferwallReportInit(false);
                if (SupersonicWebView.this.shouldNotifyDeveloper(ProductType.OfferWall.toString())) {
                    Context ctx = SupersonicWebView.this.getBaseContext();
                    if (ctx instanceof Activity) {
                        ((Activity) ctx).runOnUiThread(new Runnable() {
                            public void run() {
                                Log.d(SupersonicWebView.this.TAG, "onOfferWallInitSuccess()");
                                SupersonicWebView.this.mOnOfferWallListener.onOfferwallInitSuccess();
                            }
                        });
                    }
                }
            }
        }

        @JavascriptInterface
        public void onInitOfferWallFail(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onInitOfferWallFail(" + value + ")");
            SupersonicWebView.this.mSavedState.setOfferwallInitSuccess(false);
            final String message = new SSAObj(value).getString(ParametersKeys.ERR_MSG);
            if (SupersonicWebView.this.mSavedState.reportInitOfferwall()) {
                SupersonicWebView.this.mSavedState.setOfferwallReportInit(false);
                if (SupersonicWebView.this.shouldNotifyDeveloper(ProductType.OfferWall.toString())) {
                    Context ctx = SupersonicWebView.this.getBaseContext();
                    if (ctx instanceof Activity) {
                        ((Activity) ctx).runOnUiThread(new Runnable() {
                            public void run() {
                                String toSend = message;
                                if (toSend == null) {
                                    toSend = "We're sorry, some error occurred. we will investigate it";
                                }
                                Log.d(SupersonicWebView.this.TAG, "onOfferWallInitFail(message:" + toSend + ")");
                                SupersonicWebView.this.mOnOfferWallListener.onOfferwallInitFail(toSend);
                            }
                        });
                    }
                }
            }
            SupersonicWebView.this.responseBack(value, true, null, null);
            SupersonicWebView.this.toastingErrMsg(JSMethods.ON_INIT_OFFERWALL_FAIL, value);
        }

        @JavascriptInterface
        public void onShowInterstitialFail(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onShowInterstitialFail(" + value + ")");
            final String message = new SSAObj(value).getString(ParametersKeys.ERR_MSG);
            SupersonicWebView.this.responseBack(value, true, null, null);
            if (SupersonicWebView.this.shouldNotifyDeveloper(ProductType.Interstitial.toString())) {
                Context ctx = SupersonicWebView.this.getBaseContext();
                if (ctx instanceof Activity) {
                    ((Activity) ctx).runOnUiThread(new Runnable() {
                        public void run() {
                            String toSend = message;
                            if (toSend == null) {
                                toSend = "We're sorry, some error occurred. we will investigate it";
                            }
                            SupersonicWebView.this.mOnInitInterstitialListener.onInterstitialShowFail(toSend);
                        }
                    });
                }
            }
            SupersonicWebView.this.toastingErrMsg(JSMethods.ON_SHOW_INTERSTITIAL_FAIL, value);
        }

        @JavascriptInterface
        public void onGenericFunctionSuccess(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onGenericFunctionSuccess(" + value + ")");
            if (SupersonicWebView.this.mOnGenericFunctionListener == null) {
                Logger.d(SupersonicWebView.this.TAG, "genericFunctionListener was not found");
                return;
            }
            Context ctx = SupersonicWebView.this.getBaseContext();
            if (ctx instanceof Activity) {
                ((Activity) ctx).runOnUiThread(new Runnable() {
                    public void run() {
                        SupersonicWebView.this.mOnGenericFunctionListener.onGFSuccess();
                    }
                });
            }
            SupersonicWebView.this.responseBack(value, true, null, null);
        }

        @JavascriptInterface
        public void onGenericFunctionFail(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onGenericFunctionFail(" + value + ")");
            if (SupersonicWebView.this.mOnGenericFunctionListener == null) {
                Logger.d(SupersonicWebView.this.TAG, "genericFunctionListener was not found");
                return;
            }
            final String message = new SSAObj(value).getString(ParametersKeys.ERR_MSG);
            Context ctx = SupersonicWebView.this.getBaseContext();
            if (ctx instanceof Activity) {
                ((Activity) ctx).runOnUiThread(new Runnable() {
                    public void run() {
                        SupersonicWebView.this.mOnGenericFunctionListener.onGFFail(message);
                    }
                });
            }
            SupersonicWebView.this.responseBack(value, true, null, null);
            SupersonicWebView.this.toastingErrMsg(JSMethods.ON_GENERIC_FUNCTION_FAIL, value);
        }

        @JavascriptInterface
        public void createCalendarEvent(String value) {
            Logger.i(SupersonicWebView.this.TAG, "createCalendarEvent(" + value + ")");
            try {
                JSONObject jsObj = new JSONObject();
                JSONObject jsRecurrence = new JSONObject();
                jsRecurrence.put("frequency", "weekly");
                jsObj.put(CalendarEntryData.ID, "testevent723GDf84");
                jsObj.put("description", "Watch this crazy show on cannel 5!");
                jsObj.put(CalendarEntryData.START, "2014-02-01T20:00:00-8:00");
                jsObj.put(CalendarEntryData.END, "2014-06-30T20:00:00-8:00");
                jsObj.put("status", "pending");
                jsObj.put(CalendarEntryData.RECURRENCE, jsRecurrence.toString());
                jsObj.put(CalendarEntryData.REMINDER, "2014-02-01T19:50:00-8:00");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @JavascriptInterface
        public void openUrl(String value) {
            Logger.i(SupersonicWebView.this.TAG, "openUrl(" + value + ")");
            SSAObj ssaObj = new SSAObj(value);
            String url = ssaObj.getString(ParametersKeys.URL);
            String method = ssaObj.getString(ParametersKeys.METHOD);
            Context mContext = SupersonicWebView.this.getBaseContext();
            if (method.equalsIgnoreCase(ParametersKeys.EXTERNAL_BROWSER)) {
                mContext.startActivity(new Intent("android.intent.action.VIEW").setData(Uri.parse(url)));
            } else if (method.equalsIgnoreCase(ParametersKeys.WEB_VIEW)) {
                intent = new Intent(mContext, OpenUrlActivity.class);
                intent.putExtra(SupersonicWebView.EXTERNAL_URL, url);
                intent.putExtra(SupersonicWebView.SECONDARY_WEB_VIEW, true);
                mContext.startActivity(intent);
            } else if (method.equalsIgnoreCase(ParametersKeys.STORE)) {
                intent = new Intent(mContext, OpenUrlActivity.class);
                intent.putExtra(SupersonicWebView.EXTERNAL_URL, url);
                intent.putExtra(SupersonicWebView.IS_STORE, true);
                intent.putExtra(SupersonicWebView.SECONDARY_WEB_VIEW, true);
                mContext.startActivity(intent);
            }
        }

        @JavascriptInterface
        public void setForceClose(String value) {
            Logger.i(SupersonicWebView.this.TAG, "setForceClose(" + value + ")");
            SSAObj ssaObj = new SSAObj(value);
            String width = ssaObj.getString("width");
            String hight = ssaObj.getString("height");
            SupersonicWebView.this.mHiddenForceCloseWidth = Integer.parseInt(width);
            SupersonicWebView.this.mHiddenForceCloseHeight = Integer.parseInt(hight);
            SupersonicWebView.this.mHiddenForceCloseLocation = ssaObj.getString(ParametersKeys.POSITION);
        }

        @JavascriptInterface
        public void setBackButtonState(String value) {
            Logger.i(SupersonicWebView.this.TAG, "setBackButtonState(" + value + ")");
            SupersonicSharedPrefHelper.getSupersonicPrefHelper().setBackButtonState(new SSAObj(value).getString("state"));
        }

        @JavascriptInterface
        public void setStoreSearchKeys(String value) {
            Logger.i(SupersonicWebView.this.TAG, "setStoreSearchKeys(" + value + ")");
            SupersonicSharedPrefHelper.getSupersonicPrefHelper().setSearchKeys(value);
        }

        @JavascriptInterface
        public void setWebviewBackgroundColor(String value) {
            Logger.i(SupersonicWebView.this.TAG, "setWebviewBackgroundColor(" + value + ")");
            SupersonicWebView.this.setWebviewBackground(value);
        }

        @JavascriptInterface
        public void toggleUDIA(String value) {
            Logger.i(SupersonicWebView.this.TAG, "toggleUDIA(" + value + ")");
            SSAObj ssaObj = new SSAObj(value);
            if (ssaObj.containsKey(ParametersKeys.TOGGLE)) {
                int toggle = Integer.parseInt(ssaObj.getString(ParametersKeys.TOGGLE));
                if (toggle != 0) {
                    String binaryToggle = Integer.toBinaryString(toggle);
                    if (TextUtils.isEmpty(binaryToggle)) {
                        SupersonicWebView.this.responseBack(value, false, ErrorCodes.FIALED_TO_CONVERT_TOGGLE, null);
                        return;
                    } else if (binaryToggle.toCharArray()[3] == '0') {
                        SupersonicSharedPrefHelper.getSupersonicPrefHelper().setShouldRegisterSessions(true);
                        return;
                    } else {
                        SupersonicSharedPrefHelper.getSupersonicPrefHelper().setShouldRegisterSessions(false);
                        return;
                    }
                }
                return;
            }
            SupersonicWebView.this.responseBack(value, false, ErrorCodes.TOGGLE_KEY_DOES_NOT_EXIST, null);
        }

        @JavascriptInterface
        public void getUDIA(String value) {
            this.udiaResults = 0;
            Logger.i(SupersonicWebView.this.TAG, "getUDIA(" + value + ")");
            String funToCall = SupersonicWebView.this.extractSuccessFunctionToCall(value);
            SSAObj ssaObj = new SSAObj(value);
            if (ssaObj.containsKey(ParametersKeys.GET_BY_FLAG)) {
                int getByFlag = Integer.parseInt(ssaObj.getString(ParametersKeys.GET_BY_FLAG));
                if (getByFlag != 0) {
                    String binaryToggle = Integer.toBinaryString(getByFlag);
                    if (TextUtils.isEmpty(binaryToggle)) {
                        SupersonicWebView.this.responseBack(value, false, ErrorCodes.FIALED_TO_CONVERT_GET_BY_FLAG, null);
                        return;
                    }
                    JSONObject jsObj;
                    char[] binaryToggleArr = new StringBuilder(binaryToggle).reverse().toString().toCharArray();
                    JSONArray jsArr = new JSONArray();
                    if (binaryToggleArr[3] == '0') {
                        jsObj = new JSONObject();
                        try {
                            jsObj.put("sessions", SupersonicSharedPrefHelper.getSupersonicPrefHelper().getSessions());
                            SupersonicSharedPrefHelper.getSupersonicPrefHelper().deleteSessions();
                            jsArr.put(jsObj);
                        } catch (JSONException e) {
                        }
                    }
                    if (binaryToggleArr[2] == '1') {
                        this.udiaResults++;
                        Location location = LocationHelper.getLastLocation(SupersonicWebView.this.getBaseContext());
                        if (location != null) {
                            jsObj = new JSONObject();
                            try {
                                jsObj.put("latitude", location.getLatitude());
                                jsObj.put("longitude", location.getLongitude());
                                jsArr.put(jsObj);
                                this.udiaResults--;
                                sendResults(funToCall, jsArr);
                                Logger.i(SupersonicWebView.this.TAG, "done location");
                                return;
                            } catch (JSONException e2) {
                                return;
                            }
                        }
                        this.udiaResults--;
                        return;
                    }
                    return;
                }
                return;
            }
            SupersonicWebView.this.responseBack(value, false, ErrorCodes.GET_BY_FLAG_KEY_DOES_NOT_EXIST, null);
        }

        private void sendResults(String funToCall, JSONArray jsArr) {
            Logger.i(SupersonicWebView.this.TAG, "sendResults: " + this.udiaResults);
            if (this.udiaResults <= 0) {
                injectGetUDIA(funToCall, jsArr);
            }
        }

        @JavascriptInterface
        public void onUDIASuccess(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onUDIASuccess(" + value + ")");
        }

        @JavascriptInterface
        public void onUDIAFail(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onUDIAFail(" + value + ")");
        }

        @JavascriptInterface
        public void onGetUDIASuccess(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onGetUDIASuccess(" + value + ")");
        }

        @JavascriptInterface
        public void onGetUDIAFail(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onGetUDIAFail(" + value + ")");
        }

        @JavascriptInterface
        public void setUserUniqueId(String value) {
            Logger.i(SupersonicWebView.this.TAG, "setUserUniqueId(" + value + ")");
            SSAObj ssaObj = new SSAObj(value);
            if (ssaObj.containsKey(ParametersKeys.USER_UNIQUE_ID) && ssaObj.containsKey(ParametersKeys.PRODUCT_TYPE)) {
                if (SupersonicSharedPrefHelper.getSupersonicPrefHelper().setUniqueId(ssaObj.getString(ParametersKeys.USER_UNIQUE_ID), ssaObj.getString(ParametersKeys.PRODUCT_TYPE))) {
                    SupersonicWebView.this.responseBack(value, true, null, null);
                    return;
                } else {
                    SupersonicWebView.this.responseBack(value, false, ErrorCodes.SET_USER_UNIQUE_ID_FAILED, null);
                    return;
                }
            }
            SupersonicWebView.this.responseBack(value, false, ErrorCodes.UNIQUE_ID_OR_PRODUCT_TYPE_DOES_NOT_EXIST, null);
        }

        @JavascriptInterface
        public void getUserUniqueId(String value) {
            Logger.i(SupersonicWebView.this.TAG, "getUserUniqueId(" + value + ")");
            SSAObj ssaObj = new SSAObj(value);
            if (ssaObj.containsKey(ParametersKeys.PRODUCT_TYPE)) {
                String funToCall = SupersonicWebView.this.extractSuccessFunctionToCall(value);
                if (!TextUtils.isEmpty(funToCall)) {
                    String productType = ssaObj.getString(ParametersKeys.PRODUCT_TYPE);
                    SupersonicWebView.this.injectJavascript(SupersonicWebView.this.generateJSToInject(funToCall, SupersonicWebView.this.parseToJson(ParametersKeys.USER_UNIQUE_ID, SupersonicSharedPrefHelper.getSupersonicPrefHelper().getUniqueId(productType), ParametersKeys.PRODUCT_TYPE, productType, null, null, null, null, null, false), JSMethods.ON_GET_USER_UNIQUE_ID_SUCCESS, JSMethods.ON_GET_USER_UNIQUE_ID_FAIL));
                    return;
                }
                return;
            }
            SupersonicWebView.this.responseBack(value, false, ErrorCodes.PRODUCT_TYPE_DOES_NOT_EXIST, null);
        }

        @JavascriptInterface
        public void onGetUserUniqueIdSuccess(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onGetUserUniqueIdSuccess(" + value + ")");
        }

        @JavascriptInterface
        public void onGetUserUniqueIdFail(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onGetUserUniqueIdFail(" + value + ")");
        }

        private void injectGetUDIA(String funToCall, JSONArray jsonArr) {
            if (!TextUtils.isEmpty(funToCall)) {
                SupersonicWebView.this.injectJavascript(SupersonicWebView.this.generateJSToInject(funToCall, jsonArr.toString(), JSMethods.ON_GET_UDIA_SUCCESS, JSMethods.ON_GET_UDIA_FAIL));
            }
        }

        @JavascriptInterface
        public void onRewardedVideoGeneric(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onRewardedVideoGeneric(" + value + ")");
            if (SupersonicWebView.this.shouldNotifyDeveloper(ProductType.BrandConnect.toString())) {
                SupersonicWebView.this.mOnRewardedVideoListener.onRVGeneric("", "");
            }
        }

        @JavascriptInterface
        public void onOfferWallGeneric(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onOfferWallGeneric(" + value + ")");
            if (SupersonicWebView.this.shouldNotifyDeveloper(ProductType.OfferWall.toString())) {
                SupersonicWebView.this.mOnOfferWallListener.onOWGeneric("", "");
            }
        }

        @JavascriptInterface
        public void setUserData(String value) {
            Logger.i(SupersonicWebView.this.TAG, "setUserData(" + value + ")");
            SSAObj ssaObj = new SSAObj(value);
            if (!ssaObj.containsKey(ParametersKeys.KEY)) {
                SupersonicWebView.this.responseBack(value, false, ErrorCodes.KEY_DOES_NOT_EXIST, null);
            } else if (ssaObj.containsKey(ParametersKeys.VALUE)) {
                String mKey = ssaObj.getString(ParametersKeys.KEY);
                String mValue = ssaObj.getString(ParametersKeys.VALUE);
                if (SupersonicSharedPrefHelper.getSupersonicPrefHelper().setUserData(mKey, mValue)) {
                    SupersonicWebView.this.injectJavascript(SupersonicWebView.this.generateJSToInject(SupersonicWebView.this.extractSuccessFunctionToCall(value), SupersonicWebView.this.parseToJson(mKey, mValue, null, null, null, null, null, null, null, false)));
                    return;
                }
                SupersonicWebView.this.responseBack(value, false, "SetUserData failed writing to shared preferences", null);
            } else {
                SupersonicWebView.this.responseBack(value, false, ErrorCodes.VALUE_DOES_NOT_EXIST, null);
            }
        }

        @JavascriptInterface
        public void getUserData(String value) {
            Logger.i(SupersonicWebView.this.TAG, "getUserData(" + value + ")");
            SSAObj ssaObj = new SSAObj(value);
            if (ssaObj.containsKey(ParametersKeys.KEY)) {
                String failFunToCall = SupersonicWebView.this.extractSuccessFunctionToCall(value);
                String mKey = ssaObj.getString(ParametersKeys.KEY);
                SupersonicWebView.this.injectJavascript(SupersonicWebView.this.generateJSToInject(failFunToCall, SupersonicWebView.this.parseToJson(mKey, SupersonicSharedPrefHelper.getSupersonicPrefHelper().getUserData(mKey), null, null, null, null, null, null, null, false)));
                return;
            }
            SupersonicWebView.this.responseBack(value, false, ErrorCodes.KEY_DOES_NOT_EXIST, null);
        }

        @JavascriptInterface
        public void onGetUserCreditsFail(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onGetUserCreditsFail(" + value + ")");
            final String message = new SSAObj(value).getString(ParametersKeys.ERR_MSG);
            if (SupersonicWebView.this.shouldNotifyDeveloper(ProductType.OfferWall.toString())) {
                Context ctx = SupersonicWebView.this.getBaseContext();
                if (ctx instanceof Activity) {
                    ((Activity) ctx).runOnUiThread(new Runnable() {
                        public void run() {
                            String toSend = message;
                            if (toSend == null) {
                                toSend = "We're sorry, some error occurred. we will investigate it";
                            }
                            SupersonicWebView.this.mOnOfferWallListener.onGetOWCreditsFailed(toSend);
                        }
                    });
                }
            }
            SupersonicWebView.this.responseBack(value, true, null, null);
            SupersonicWebView.this.toastingErrMsg(JSMethods.ON_GET_USER_CREDITS_FAILED, value);
        }

        @JavascriptInterface
        public void onAdWindowsClosed(String value) {
            Logger.i(SupersonicWebView.this.TAG, "onAdWindowsClosed(" + value + ")");
            SupersonicWebView.this.mSavedState.adClosed();
            final String product = new SSAObj(value).getString(ParametersKeys.PRODUCT_TYPE);
            if (product.equalsIgnoreCase(ProductType.BrandConnect.toString())) {
                Log.d(SupersonicWebView.this.PUB_TAG, "onRVAdClosed()");
            } else if (product.equalsIgnoreCase(ProductType.Interstitial.toString())) {
                Log.d(SupersonicWebView.this.PUB_TAG, "onISAdClosed()");
            } else if (product.equalsIgnoreCase(ProductType.OfferWall.toString())) {
                Log.d(SupersonicWebView.this.PUB_TAG, "onOWAdClosed()");
            }
            if (SupersonicWebView.this.shouldNotifyDeveloper(product) && product != null) {
                Context ctx = SupersonicWebView.this.getBaseContext();
                if (ctx instanceof Activity) {
                    ((Activity) ctx).runOnUiThread(new Runnable() {
                        public void run() {
                            if (product.equalsIgnoreCase(ProductType.BrandConnect.toString())) {
                                SupersonicWebView.this.mOnRewardedVideoListener.onRVAdClosed();
                            } else if (product.equalsIgnoreCase(ProductType.Interstitial.toString())) {
                                SupersonicWebView.this.mOnInitInterstitialListener.onInterstitialAdClosed();
                            } else if (product.equalsIgnoreCase(ProductType.OfferWall.toString())) {
                                SupersonicWebView.this.mOnOfferWallListener.onOWAdClosed();
                            }
                        }
                    });
                }
            }
        }

        @JavascriptInterface
        public void onVideoStatusChanged(String value) {
            Log.d(SupersonicWebView.this.TAG, "onVideoStatusChanged(" + value + ")");
            SSAObj ssaObj = new SSAObj(value);
            String product = ssaObj.getString(ParametersKeys.PRODUCT_TYPE);
            if (SupersonicWebView.this.mVideoEventsListener != null && !TextUtils.isEmpty(product) && ProductType.BrandConnect.toString().equalsIgnoreCase(product)) {
                String status = ssaObj.getString("status");
                if (ParametersKeys.VIDEO_STATUS_STARTED.equalsIgnoreCase(status)) {
                    SupersonicWebView.this.mVideoEventsListener.onVideoStarted();
                } else if (ParametersKeys.VIDEO_STATUS_PAUSED.equalsIgnoreCase(status)) {
                    SupersonicWebView.this.mVideoEventsListener.onVideoPaused();
                } else if (ParametersKeys.VIDEO_STATUS_PLAYING.equalsIgnoreCase(status)) {
                    SupersonicWebView.this.mVideoEventsListener.onVideoResumed();
                } else if (ParametersKeys.VIDEO_STATUS_ENDED.equalsIgnoreCase(status)) {
                    SupersonicWebView.this.mVideoEventsListener.onVideoEnded();
                } else if (ParametersKeys.VIDEO_STATUS_STOPPED.equalsIgnoreCase(status)) {
                    SupersonicWebView.this.mVideoEventsListener.onVideoStopped();
                } else {
                    Logger.i(SupersonicWebView.this.TAG, "onVideoStatusChanged: unknown status: " + status);
                }
            }
        }
    }

    public interface OnWebViewControllerChangeListener {
        void onHide();

        void onSetOrientationCalled(String str, int i);
    }

    public enum State {
        Display,
        Gone
    }

    private class SupersonicWebViewTouchListener implements OnTouchListener {
        private SupersonicWebViewTouchListener() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == 1) {
                float xTouch = event.getX();
                float yTouch = event.getY();
                Logger.i(SupersonicWebView.this.TAG, "X:" + ((int) xTouch) + " Y:" + ((int) yTouch));
                int width = SDKUtils.getDeviceWidth();
                int height = SDKUtils.getDeviceHeight();
                Logger.i(SupersonicWebView.this.TAG, "Width:" + width + " Height:" + height);
                int boundsTouchAreaX = SDKUtils.dpToPx((long) SupersonicWebView.this.mHiddenForceCloseWidth);
                int boundsTouchAreaY = SDKUtils.dpToPx((long) SupersonicWebView.this.mHiddenForceCloseHeight);
                int actualTouchX = 0;
                int actualTouchY = 0;
                if (ForceClosePosition.TOP_RIGHT.equalsIgnoreCase(SupersonicWebView.this.mHiddenForceCloseLocation)) {
                    actualTouchX = width - ((int) xTouch);
                    actualTouchY = (int) yTouch;
                } else if (ForceClosePosition.TOP_LEFT.equalsIgnoreCase(SupersonicWebView.this.mHiddenForceCloseLocation)) {
                    actualTouchX = (int) xTouch;
                    actualTouchY = (int) yTouch;
                } else if (ForceClosePosition.BOTTOM_RIGHT.equalsIgnoreCase(SupersonicWebView.this.mHiddenForceCloseLocation)) {
                    actualTouchX = width - ((int) xTouch);
                    actualTouchY = height - ((int) yTouch);
                } else if (ForceClosePosition.BOTTOM_LEFT.equalsIgnoreCase(SupersonicWebView.this.mHiddenForceCloseLocation)) {
                    actualTouchX = (int) xTouch;
                    actualTouchY = height - ((int) yTouch);
                }
                if (actualTouchX <= boundsTouchAreaX && actualTouchY <= boundsTouchAreaY) {
                    SupersonicWebView.this.isRemoveCloseEventHandler = false;
                    if (SupersonicWebView.this.mCloseEventTimer != null) {
                        SupersonicWebView.this.mCloseEventTimer.cancel();
                    }
                    SupersonicWebView.this.mCloseEventTimer = new CountDownTimer(2000, 500) {
                        public void onTick(long millisUntilFinished) {
                            Logger.i(SupersonicWebView.this.TAG, "Close Event Timer Tick " + millisUntilFinished);
                        }

                        public void onFinish() {
                            Logger.i(SupersonicWebView.this.TAG, "Close Event Timer Finish");
                            if (SupersonicWebView.this.isRemoveCloseEventHandler) {
                                SupersonicWebView.this.isRemoveCloseEventHandler = false;
                            } else {
                                SupersonicWebView.this.engageEnd(ParametersKeys.FORCE_CLOSE);
                            }
                        }
                    }.start();
                }
            }
            return false;
        }
    }

    private class ViewClient extends WebViewClient {
        private ViewClient() {
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Logger.i("onPageStarted", url);
            super.onPageStarted(view, url, favicon);
        }

        public void onPageFinished(WebView view, String url) {
            Logger.i("onPageFinished", url);
            if (url.contains("adUnit") || url.contains("index.html")) {
                SupersonicWebView.this.pageFinished();
            }
            super.onPageFinished(view, url);
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Logger.i("onReceivedError", failingUrl + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + description);
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Logger.i("shouldOverrideUrlLoading", url);
            if (!SupersonicWebView.this.handleSearchKeysURLs(url)) {
                return super.shouldOverrideUrlLoading(view, url);
            }
            SupersonicWebView.this.interceptedUrlToStore();
            return true;
        }

        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            Logger.i("shouldInterceptRequest", url);
            boolean mraidCall = false;
            try {
                if (new URL(url).getFile().contains("mraid.js")) {
                    mraidCall = true;
                }
            } catch (MalformedURLException e) {
            }
            if (mraidCall) {
                String filePath = "file://" + SupersonicWebView.this.mCacheDirectory + File.separator + "mraid.js";
                try {
                    FileInputStream fis = new FileInputStream(new File(filePath));
                    return new WebResourceResponse("text/javascript", DownloadManager.UTF8_CHARSET, getClass().getResourceAsStream(filePath));
                } catch (FileNotFoundException e2) {
                }
            }
            return super.shouldInterceptRequest(view, url);
        }
    }

    public SupersonicWebView(Context context) {
        super(context);
        Logger.i(this.TAG, "C'tor");
        initLayout(context);
        this.mCacheDirectory = SupersonicStorageUtils.initializeCacheDirectory(context);
        this.downloadManager = DownloadManager.getInstance(this.mCacheDirectory);
        this.downloadManager.setOnPreCacheCompletion(this);
        this.mWebChromeClient = new ChromeClient();
        setWebViewClient(new ViewClient());
        setWebChromeClient(this.mWebChromeClient);
        setWebViewSettings();
        addJavascriptInterface(new JSInterface(context), Constants.JAVASCRIPT_INTERFACE_NAME);
        setDownloadListener(this);
        setOnTouchListener(new SupersonicWebViewTouchListener());
    }

    private void initLayout(Context context) {
        LayoutParams coverScreenParams = new LayoutParams(-1, -1);
        this.mLayout = new FrameLayout(context);
        this.mCustomViewContainer = new FrameLayout(context);
        this.mCustomViewContainer.setLayoutParams(new LayoutParams(-1, -1));
        this.mCustomViewContainer.setVisibility(8);
        FrameLayout mContentView = new FrameLayout(context);
        mContentView.setLayoutParams(new LayoutParams(-1, -1));
        mContentView.addView(this);
        this.mLayout.addView(this.mCustomViewContainer, coverScreenParams);
        this.mLayout.addView(mContentView);
    }

    private void setWebViewSettings() {
        WebSettings s = getSettings();
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        s.setBuiltInZoomControls(false);
        s.setJavaScriptEnabled(true);
        s.setSupportMultipleWindows(true);
        s.setJavaScriptCanOpenWindowsAutomatically(true);
        s.setGeolocationEnabled(true);
        s.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");
        s.setDomStorageEnabled(true);
        try {
            setDisplayZoomControls(s);
            setMediaPlaybackJellyBean(s);
        } catch (Throwable e) {
            Logger.e(this.TAG, "setWebSettings - " + e.toString());
            new SupersonicAsyncHttpRequestTask().execute(new String[]{"https://www.supersonicads.com/mobile/sdk5/log?method=setWebViewSettings"});
        }
    }

    private void setDisplayZoomControls(WebSettings s) {
        if (VERSION.SDK_INT > 11) {
            s.setDisplayZoomControls(false);
        }
    }

    public WebBackForwardList saveState(Bundle outState) {
        return super.saveState(outState);
    }

    @SuppressLint({"NewApi"})
    private void setMediaPlaybackJellyBean(WebSettings s) {
        if (VERSION.SDK_INT >= 17) {
            s.setMediaPlaybackRequiresUserGesture(false);
        }
    }

    @SuppressLint({"NewApi"})
    private void setWebDebuggingEnabled() {
        if (VERSION.SDK_INT >= 19) {
            setWebContentsDebuggingEnabled(true);
        }
    }

    public void downloadController() {
        SupersonicStorageUtils.deleteFile(this.mCacheDirectory, "", Constants.MOBILE_CONTROLLER_HTML);
        String controllerUrl = SDKUtils.getControllerUrl();
        SSAFile indexHtml = new SSAFile(controllerUrl, "");
        this.mGlobalControllerTimer = new CountDownTimer(40000, 1000) {
            public void onTick(long millisUntilFinished) {
                Logger.i(SupersonicWebView.this.TAG, "Global Controller Timer Tick " + millisUntilFinished);
            }

            public void onFinish() {
                Logger.i(SupersonicWebView.this.TAG, "Global Controller Timer Finish");
                SupersonicWebView.this.mGlobalControllerTimeFinish = true;
            }
        }.start();
        if (this.downloadManager.isMobileControllerThreadLive()) {
            Logger.i(this.TAG, "Download Mobile Controller: already alive");
            return;
        }
        Logger.i(this.TAG, "Download Mobile Controller: " + controllerUrl);
        this.downloadManager.downloadMobileControllerFile(indexHtml);
    }

    public void setDebugMode(int debugMode) {
        mDebugMode = debugMode;
    }

    public int getDebugMode() {
        return mDebugMode;
    }

    private boolean shouldNotifyDeveloper(String product) {
        boolean shouldNotify = false;
        if (product == null) {
            Logger.d(this.TAG, "Trying to trigger a listener - no product was found");
            return false;
        }
        if (product.equalsIgnoreCase(ProductType.Interstitial.toString())) {
            shouldNotify = this.mOnInitInterstitialListener != null;
        } else if (product.equalsIgnoreCase(ProductType.BrandConnect.toString())) {
            shouldNotify = this.mOnRewardedVideoListener != null;
        } else if (product.equalsIgnoreCase(ProductType.OfferWall.toString()) || product.equalsIgnoreCase(ProductType.OfferWallCredits.toString())) {
            shouldNotify = this.mOnOfferWallListener != null;
        }
        if (!shouldNotify) {
            Logger.d(this.TAG, "Trying to trigger a listener - no listener was found for product " + product);
        }
        return shouldNotify;
    }

    public void setOrientationState(String orientaiton) {
        this.mOrientationState = orientaiton;
    }

    public String getOrientationState() {
        return this.mOrientationState;
    }

    public static void setEXTERNAL_URL(String EXTERNAL_URL) {
        EXTERNAL_URL = EXTERNAL_URL;
    }

    public void setVideoEventsListener(VideoEventsListener listener) {
        this.mVideoEventsListener = listener;
    }

    public void removeVideoEventsListener() {
        this.mVideoEventsListener = null;
    }

    private void setWebviewBackground(String value) {
        String keyColor = new SSAObj(value).getString(ParametersKeys.COLOR);
        int bgColor = 0;
        if (!ParametersKeys.TRANSPARENT.equalsIgnoreCase(keyColor)) {
            bgColor = Color.parseColor(keyColor);
        }
        setBackgroundColor(bgColor);
    }

    public void load(int loadAttemp) {
        try {
            loadUrl("about:blank");
        } catch (Throwable e) {
            Logger.e(this.TAG, "WebViewController:: load: " + e.toString());
            new SupersonicAsyncHttpRequestTask().execute(new String[]{"https://www.supersonicads.com/mobile/sdk5/log?method=webviewLoadBlank"});
        }
        String controllerPath = "file://" + this.mCacheDirectory + File.separator + Constants.MOBILE_CONTROLLER_HTML;
        if (new File(this.mCacheDirectory + File.separator + Constants.MOBILE_CONTROLLER_HTML).exists()) {
            this.mRequestParameters = getRequestParameters();
            String controllerPathWithParams = controllerPath + "?" + this.mRequestParameters;
            final int i = loadAttemp;
            this.mLoadControllerTimer = new CountDownTimer(VLTools.DEFAULT_RATE_US_THREHOLD, 1000) {
                public void onTick(long millisUntilFinished) {
                    Logger.i(SupersonicWebView.this.TAG, "Loading Controller Timer Tick " + millisUntilFinished);
                }

                public void onFinish() {
                    Logger.i(SupersonicWebView.this.TAG, "Loading Controller Timer Finish");
                    if (i == 2) {
                        SupersonicWebView.this.mGlobalControllerTimer.cancel();
                        if (SupersonicWebView.this.mRVmiss) {
                            SupersonicWebView.this.sendProductErrorMessage(ProductType.BrandConnect);
                        }
                        if (SupersonicWebView.this.mISmiss) {
                            SupersonicWebView.this.sendProductErrorMessage(ProductType.Interstitial);
                        }
                        if (SupersonicWebView.this.mOWmiss) {
                            SupersonicWebView.this.sendProductErrorMessage(ProductType.OfferWall);
                        }
                        if (SupersonicWebView.this.mOWCreditsMiss) {
                            SupersonicWebView.this.sendProductErrorMessage(ProductType.OfferWallCredits);
                            return;
                        }
                        return;
                    }
                    SupersonicWebView.this.load(2);
                }
            }.start();
            try {
                loadUrl(controllerPathWithParams);
            } catch (Throwable e2) {
                Logger.e(this.TAG, "WebViewController:: load: " + e2.toString());
                new SupersonicAsyncHttpRequestTask().execute(new String[]{"https://www.supersonicads.com/mobile/sdk5/log?method=webviewLoadWithPath"});
            }
            Logger.i(this.TAG, "load(): " + controllerPathWithParams);
            return;
        }
        Logger.i(this.TAG, "load(): Mobile Controller HTML Does not exist");
        new SupersonicAsyncHttpRequestTask().execute(new String[]{"https://www.supersonicads.com/mobile/sdk5/log?method=htmlControllerDoesNotExistOnFileSystem"});
    }

    private void initProduct(String applicationKey, String userId, ProductType type, String action) {
        if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(applicationKey)) {
            triggerOnControllerInitProductFail("User id or Application key are missing", type);
        } else if (this.mControllerState == ControllerState.Ready) {
            SupersonicSharedPrefHelper.getSupersonicPrefHelper().setApplicationKey(applicationKey, type);
            SupersonicSharedPrefHelper.getSupersonicPrefHelper().setUserID(userId, type);
            createInitProductJSMethod(type);
        } else {
            setMissProduct(type);
            if (this.mControllerState == ControllerState.Failed) {
                triggerOnControllerInitProductFail(SDKUtils.createErrorMessage(action, ErrorCodes.InitiatingController), type);
            } else if (this.mGlobalControllerTimeFinish) {
                downloadController();
            }
        }
    }

    public void initBrandConnect(String applicationKey, String userId, Map<String, String> extraParameters, OnRewardedVideoListener listener) {
        this.mBCAppKey = applicationKey;
        this.mBCUserId = userId;
        this.mBCExtraParameters = extraParameters;
        this.mOnRewardedVideoListener = listener;
        this.mSavedState.setRewardedVideoAppKey(this.mBCAppKey);
        this.mSavedState.setRewardedVideoUserId(this.mBCUserId);
        this.mSavedState.setRewardedVideoExtraParams(this.mBCExtraParameters);
        initProduct(this.mBCAppKey, this.mBCUserId, ProductType.BrandConnect, ErrorCodes.InitBC);
    }

    public void initInterstitial(String applicationKey, String userId, Map<String, String> extraParameters, OnInterstitialListener listener) {
        this.mISAppKey = applicationKey;
        this.mISUserId = userId;
        this.mISExtraParameters = extraParameters;
        this.mOnInitInterstitialListener = listener;
        this.mSavedState.setInterstitialAppKey(this.mISAppKey);
        this.mSavedState.setInterstitialUserId(this.mISUserId);
        this.mSavedState.setInterstitialExtraParams(this.mISExtraParameters);
        this.mSavedState.setReportInitInterstitial(true);
        initProduct(this.mISAppKey, this.mISUserId, ProductType.Interstitial, ErrorCodes.InitIS);
    }

    public boolean isInterstitialAdAvailable() {
        return this.mIsInterstitialAvailable == null ? false : this.mIsInterstitialAvailable.booleanValue();
    }

    public void showInterstitial() {
        injectJavascript(generateJSToInject(JSMethods.SHOW_INTERSTITIAL, JSMethods.ON_SHOW_INTERSTITIAL_SUCCESS, JSMethods.ON_SHOW_INTERSTITIAL_FAIL));
    }

    public void forceShowInterstitial() {
        injectJavascript(generateJSToInject(JSMethods.FORCE_SHOW_INTERSTITIAL, JSMethods.ON_SHOW_INTERSTITIAL_SUCCESS, JSMethods.ON_SHOW_INTERSTITIAL_FAIL));
    }

    public void initOfferWall(String applicationKey, String userId, Map<String, String> extraParameters, OnOfferWallListener listener) {
        this.mOWAppKey = applicationKey;
        this.mOWUserId = userId;
        this.mOWExtraParameters = extraParameters;
        this.mOnOfferWallListener = listener;
        this.mSavedState.setOfferWallExtraParams(this.mISExtraParameters);
        this.mSavedState.setOfferwallReportInit(true);
        initProduct(this.mOWAppKey, this.mOWUserId, ProductType.OfferWall, ErrorCodes.InitOW);
    }

    public void showOfferWall(Map<String, String> extraParameters) {
        this.mOWExtraParameters = extraParameters;
        injectJavascript(generateJSToInject(JSMethods.SHOW_OFFER_WALL, JSMethods.ON_SHOW_OFFER_WALL_SUCCESS, JSMethods.ON_SHOW_OFFER_WALL_FAIL));
    }

    public void getOfferWallCredits(String applicationKey, String userId, OnOfferWallListener listener) {
        this.mOWCreditsAppKey = applicationKey;
        this.mOWCreditsUserId = userId;
        this.mOnOfferWallListener = listener;
        initProduct(this.mOWCreditsAppKey, this.mOWCreditsUserId, ProductType.OfferWallCredits, ErrorCodes.ShowOWCredits);
    }

    private void createInitProductJSMethod(ProductType type) {
        String script = null;
        if (type == ProductType.BrandConnect) {
            script = generateJSToInject(JSMethods.INIT_BRAND_CONNECT, JSMethods.ON_INIT_BRAND_CONNECT_SUCCESS, JSMethods.ON_INIT_BRAND_CONNECT_FAIL);
        } else if (type == ProductType.Interstitial) {
            Map<String, String> interstitialParamsMap = new HashMap();
            interstitialParamsMap.put("applicationKey", this.mISAppKey);
            interstitialParamsMap.put("applicationUserId", this.mISUserId);
            if (this.mISExtraParameters != null) {
                interstitialParamsMap.putAll(this.mISExtraParameters);
            }
            script = generateJSToInject(JSMethods.INIT_INTERSTITIAL, flatMapToJsonAsString(interstitialParamsMap), JSMethods.ON_INIT_INTERSTITIAL_SUCCESS, JSMethods.ON_INIT_INTERSTITIAL_FAIL);
        } else if (type == ProductType.OfferWall) {
            Map<String, String> offerwallParamsMap = new HashMap();
            offerwallParamsMap.put("applicationKey", this.mOWAppKey);
            offerwallParamsMap.put("applicationUserId", this.mOWUserId);
            if (this.mOWExtraParameters != null) {
                offerwallParamsMap.putAll(this.mOWExtraParameters);
            }
            script = generateJSToInject(JSMethods.INIT_OFFERWALL, flatMapToJsonAsString(offerwallParamsMap), JSMethods.ON_INIT_OFFERWALL_SUCCESS, JSMethods.ON_INIT_OFFERWALL_FAIL);
        } else if (type == ProductType.OfferWallCredits) {
            script = generateJSToInject(JSMethods.GET_USER_CREDITS, parseToJson(ParametersKeys.PRODUCT_TYPE, ParametersKeys.OFFER_WALL, "applicationKey", this.mOWCreditsAppKey, "applicationUserId", this.mOWCreditsUserId, null, null, null, false), "null", JSMethods.ON_GET_USER_CREDITS_FAILED);
        }
        if (script != null) {
            injectJavascript(script);
        }
    }

    private String flatMapToJsonAsString(Map<String, String> params) {
        JSONObject jsObj = new JSONObject();
        if (params != null) {
            Iterator<Entry<String, String>> it = params.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, String> pairs = (Entry) it.next();
                try {
                    jsObj.putOpt((String) pairs.getKey(), SDKUtils.encodeString((String) pairs.getValue()));
                } catch (JSONException e) {
                    Logger.i(this.TAG, "flatMapToJsonAsStringfailed " + e.toString());
                }
                it.remove();
            }
        }
        return jsObj.toString();
    }

    private void setMissProduct(ProductType type) {
        if (type == ProductType.BrandConnect) {
            this.mRVmiss = true;
        } else if (type == ProductType.Interstitial) {
            this.mISmiss = true;
        } else if (type == ProductType.OfferWall) {
            this.mOWmiss = true;
        } else if (type == ProductType.OfferWallCredits) {
            this.mOWCreditsMiss = true;
        }
        Logger.i(this.TAG, "setMissProduct(" + type + ")");
    }

    private void resetMissProduct() {
        this.mRVmiss = false;
        this.mISmiss = false;
        this.mOWmiss = false;
        this.mOWCreditsMiss = false;
    }

    private void triggerOnControllerInitProductFail(final String message, final ProductType type) {
        if (shouldNotifyDeveloper(type.toString())) {
            Context ctx = getBaseContext();
            if (ctx instanceof Activity) {
                ((Activity) ctx).runOnUiThread(new Runnable() {
                    public void run() {
                        if (ProductType.BrandConnect == type) {
                            SupersonicWebView.this.mSavedState.setRewardedVideoInitSuccess(false);
                            Log.d(SupersonicWebView.this.TAG, "onRVInitFail(message:" + message + ")");
                            SupersonicWebView.this.mOnRewardedVideoListener.onRVInitFail(message);
                        } else if (ProductType.Interstitial == type) {
                            SupersonicWebView.this.mSavedState.setInterstitialInitSuccess(false);
                            if (SupersonicWebView.this.mSavedState.reportInitInterstitial()) {
                                Log.d(SupersonicWebView.this.TAG, "onInterstitialInitFail(message:" + message + ")");
                                SupersonicWebView.this.mOnInitInterstitialListener.onInterstitialInitFail(message);
                                SupersonicWebView.this.mSavedState.setReportInitInterstitial(false);
                            }
                        } else if (ProductType.OfferWall == type) {
                            SupersonicWebView.this.mOnOfferWallListener.onOfferwallInitFail(message);
                        } else if (ProductType.OfferWallCredits == type) {
                            SupersonicWebView.this.mOnOfferWallListener.onGetOWCreditsFailed(message);
                        }
                    }
                });
            }
        }
    }

    public void showBrandConnect() {
        injectJavascript(generateJSToInject(JSMethods.SHOW_BRAND_CONNECT, JSMethods.ON_SHOW_BRAND_CONNECT_SUCCESS, JSMethods.ON_SHOW_BRAND_CONNECT_FAIL));
    }

    public void assetCached(String file, String path) {
        injectJavascript(generateJSToInject(JSMethods.ASSET_CACHED, parseToJson(ParametersKeys.FILE, file, "path", path, null, null, null, null, null, false)));
    }

    public void assetCachedFailed(String file, String path, String errorMsg) {
        injectJavascript(generateJSToInject(JSMethods.ASSET_CACHED_FAILED, parseToJson(ParametersKeys.FILE, file, "path", path, ParametersKeys.ERR_MSG, errorMsg, null, null, null, false)));
    }

    public void enterBackground() {
        if (this.mControllerState == ControllerState.Ready) {
            injectJavascript(generateJSToInject(JSMethods.ENTER_BACKGROUND));
        }
    }

    public void enterForeground() {
        if (this.mControllerState == ControllerState.Ready) {
            injectJavascript(generateJSToInject(JSMethods.ENTER_FOREGROUND));
        }
    }

    public void viewableChange(boolean visibility, String webview) {
        injectJavascript(generateJSToInject(JSMethods.VIEWABLE_CHANGE, parseToJson(ParametersKeys.WEB_VIEW, webview, null, null, null, null, null, null, ParametersKeys.IS_VIEWABLE, visibility)));
    }

    public void nativeNavigationPressed(String action) {
        injectJavascript(generateJSToInject(JSMethods.NATIVE_NAVIGATION_PRESSED, parseToJson(ParametersKeys.ACTION, action, null, null, null, null, null, null, null, false)));
    }

    public void pageFinished() {
        injectJavascript(generateJSToInject(JSMethods.PAGE_FINISHED));
    }

    public void interceptedUrlToStore() {
        injectJavascript(generateJSToInject(JSMethods.INTERCEPTED_URL_TO_STORE));
    }

    private void injectJavascript(String script) {
        String catchClosure = "empty";
        if (getDebugMode() == DebugMode.MODE_0.getValue()) {
            catchClosure = "console.log(\"JS exeption: \" + JSON.stringify(e));";
        } else if (getDebugMode() >= DebugMode.MODE_1.getValue() && getDebugMode() <= DebugMode.MODE_3.getValue()) {
            catchClosure = "console.log(\"JS exeption: \" + JSON.stringify(e));";
        }
        final StringBuilder scriptBuilder = new StringBuilder();
        scriptBuilder.append("try{").append(script).append("}catch(e){").append(catchClosure).append("}");
        final String url = "javascript:" + scriptBuilder.toString();
        Context ctx = getBaseContext();
        if (ctx instanceof Activity) {
            ((Activity) ctx).runOnUiThread(new Runnable() {
                public void run() {
                    Logger.i(SupersonicWebView.this.TAG, url);
                    try {
                        if (SupersonicWebView.this.isKitkatAndAbove != null) {
                            if (SupersonicWebView.this.isKitkatAndAbove.booleanValue()) {
                                SupersonicWebView.this.evaluateJavascriptKitKat(scriptBuilder.toString());
                            } else {
                                SupersonicWebView.this.loadUrl(url);
                            }
                        } else if (VERSION.SDK_INT >= 19) {
                            SupersonicWebView.this.evaluateJavascriptKitKat(scriptBuilder.toString());
                            SupersonicWebView.this.isKitkatAndAbove = Boolean.valueOf(true);
                        } else {
                            SupersonicWebView.this.loadUrl(url);
                            SupersonicWebView.this.isKitkatAndAbove = Boolean.valueOf(false);
                        }
                    } catch (NoSuchMethodError e) {
                        Logger.e(SupersonicWebView.this.TAG, "evaluateJavascrip NoSuchMethodError: SDK version=" + VERSION.SDK_INT + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + e);
                        SupersonicWebView.this.loadUrl(url);
                        SupersonicWebView.this.isKitkatAndAbove = Boolean.valueOf(false);
                    } catch (Throwable t) {
                        Logger.e(SupersonicWebView.this.TAG, "injectJavascript: " + t.toString());
                        new SupersonicAsyncHttpRequestTask().execute(new String[]{"https://www.supersonicads.com/mobile/sdk5/log?method=injectJavaScript"});
                    }
                }
            });
            return;
        }
        new SupersonicAsyncHttpRequestTask().execute(new String[]{"https://www.supersonicads.com/mobile/sdk5/log?method=contextIsNotActivity"});
    }

    @SuppressLint({"NewApi"})
    private void evaluateJavascriptKitKat(String script) {
        evaluateJavascript(script, null);
    }

    private Context getBaseContext() {
        return ((MutableContextWrapper) getContext()).getBaseContext();
    }

    private String getRequestParameters() {
        DeviceProperties properties = DeviceProperties.getInstance(getBaseContext());
        StringBuilder builder = new StringBuilder();
        String sdkVer = properties.getSupersonicSdkVersion();
        if (!TextUtils.isEmpty(sdkVer)) {
            builder.append(RequestParameters.SDK_VERSION).append(RequestParameters.EQUAL).append(sdkVer).append(RequestParameters.AMPERSAND);
        }
        String osType = properties.getDeviceOsType();
        if (!TextUtils.isEmpty(osType)) {
            builder.append(RequestParameters.DEVICE_OS).append(RequestParameters.EQUAL).append(osType);
        }
        Uri downloadUri = Uri.parse(SDKUtils.getControllerUrl());
        if (downloadUri != null) {
            String scheme = downloadUri.getScheme() + ":";
            String host = downloadUri.getHost();
            int port = downloadUri.getPort();
            if (port != -1) {
                host = host + ":" + port;
            }
            builder.append(RequestParameters.AMPERSAND).append(RequestParameters.PROTOCOL).append(RequestParameters.EQUAL).append(scheme);
            builder.append(RequestParameters.AMPERSAND).append(RequestParameters.DOMAIN).append(RequestParameters.EQUAL).append(host);
            String config = SDKUtils.getControllerConfig();
            if (!TextUtils.isEmpty(config)) {
                builder.append(RequestParameters.AMPERSAND).append(RequestParameters.CONTROLLER_CONFIG).append(RequestParameters.EQUAL).append(config);
            }
            builder.append(RequestParameters.AMPERSAND).append(RequestParameters.DEBUG).append(RequestParameters.EQUAL).append(getDebugMode());
        }
        return builder.toString();
    }

    private void closeWebView() {
        if (this.mChangeListener != null) {
            this.mChangeListener.onHide();
        }
    }

    private void responseBack(String value, boolean result, String errorMessage, String errorCode) {
        SSAObj ssaObj = new SSAObj(value);
        String success = ssaObj.getString(JSON_KEY_SUCCESS);
        String fail = ssaObj.getString(JSON_KEY_FAIL);
        String funToCall = null;
        if (result) {
            if (!TextUtils.isEmpty(success)) {
                funToCall = success;
            }
        } else if (!TextUtils.isEmpty(fail)) {
            funToCall = fail;
        }
        if (!TextUtils.isEmpty(funToCall)) {
            if (!TextUtils.isEmpty(errorMessage)) {
                try {
                    value = new JSONObject(value).put(ParametersKeys.ERR_MSG, errorMessage).toString();
                } catch (JSONException e) {
                }
            }
            if (!TextUtils.isEmpty(errorCode)) {
                try {
                    value = new JSONObject(value).put(ParametersKeys.ERR_CODE, errorCode).toString();
                } catch (JSONException e2) {
                }
            }
            injectJavascript(generateJSToInject(funToCall, value));
        }
    }

    private String extractSuccessFunctionToCall(String jsonStr) {
        return new SSAObj(jsonStr).getString(JSON_KEY_SUCCESS);
    }

    private String extractFailFunctionToCall(String jsonStr) {
        return new SSAObj(jsonStr).getString(JSON_KEY_FAIL);
    }

    private String parseToJson(String key1, String value1, String key2, String value2, String key3, String value3, String key4, String value4, String key5, boolean value5) {
        JSONObject jsObj = new JSONObject();
        try {
            if (!(TextUtils.isEmpty(key1) || TextUtils.isEmpty(value1))) {
                jsObj.put(key1, SDKUtils.encodeString(value1));
            }
            if (!(TextUtils.isEmpty(key2) || TextUtils.isEmpty(value2))) {
                jsObj.put(key2, SDKUtils.encodeString(value2));
            }
            if (!(TextUtils.isEmpty(key3) || TextUtils.isEmpty(value3))) {
                jsObj.put(key3, SDKUtils.encodeString(value3));
            }
            if (!(TextUtils.isEmpty(key4) || TextUtils.isEmpty(value4))) {
                jsObj.put(key4, SDKUtils.encodeString(value4));
            }
            if (!TextUtils.isEmpty(key5)) {
                jsObj.put(key5, value5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            new SupersonicAsyncHttpRequestTask().execute(new String[]{Constants.NATIVE_EXCEPTION_BASE_URL + e.getStackTrace()[0].getMethodName()});
        }
        return jsObj.toString();
    }

    private String mapToJson(Map<String, String> map) {
        JSONObject jsObj = new JSONObject();
        if (!(map == null || map.isEmpty())) {
            for (String key : map.keySet()) {
                try {
                    jsObj.put(key, SDKUtils.encodeString((String) map.get(key)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsObj.toString();
    }

    private Object[] getDeviceParams(Context context) {
        boolean fail = false;
        DeviceProperties deviceProperties = DeviceProperties.getInstance(context);
        JSONObject jsObj = new JSONObject();
        try {
            StringBuilder key;
            jsObj.put(RequestParameters.APP_ORIENTATION, "none");
            String deviceOem = deviceProperties.getDeviceOem();
            if (deviceOem != null) {
                jsObj.put(SDKUtils.encodeString(RequestParameters.DEVICE_OEM), SDKUtils.encodeString(deviceOem));
            }
            String deviceModel = deviceProperties.getDeviceModel();
            if (deviceModel != null) {
                jsObj.put(SDKUtils.encodeString("deviceModel"), SDKUtils.encodeString(deviceModel));
            } else {
                fail = true;
            }
            SDKUtils.loadGoogleAdvertiserInfo(context);
            String advertiserId = SDKUtils.getAdvertiserId();
            Boolean isLAT = Boolean.valueOf(SDKUtils.isLimitAdTrackingEnabled());
            if (!TextUtils.isEmpty(advertiserId)) {
                Logger.i(this.TAG, "add AID and LAT");
                jsObj.put(RequestParameters.isLAT, isLAT);
                jsObj.put(RequestParameters.DEVICE_IDS + RequestParameters.LEFT_BRACKETS + RequestParameters.AID + RequestParameters.RIGHT_BRACKETS, SDKUtils.encodeString(advertiserId));
            }
            String deviceOSType = deviceProperties.getDeviceOsType();
            if (deviceOSType != null) {
                jsObj.put(SDKUtils.encodeString(RequestParameters.DEVICE_OS), SDKUtils.encodeString(deviceOSType));
            } else {
                fail = true;
            }
            String deviceOSVersion = Integer.toString(deviceProperties.getDeviceOsVersion());
            if (deviceOSVersion != null) {
                jsObj.put(SDKUtils.encodeString(RequestParameters.DEVICE_OS_VERSION), deviceOSVersion);
            } else {
                fail = true;
            }
            String ssaSDKVersion = deviceProperties.getSupersonicSdkVersion();
            if (ssaSDKVersion != null) {
                jsObj.put(SDKUtils.encodeString(RequestParameters.SDK_VERSION), SDKUtils.encodeString(ssaSDKVersion));
            }
            if (deviceProperties.getDeviceCarrier() != null && deviceProperties.getDeviceCarrier().length() > 0) {
                jsObj.put(SDKUtils.encodeString(RequestParameters.MOBILE_CARRIER), SDKUtils.encodeString(deviceProperties.getDeviceCarrier()));
            }
            String connectionType = SDKUtils.getConnectionType(context);
            if (TextUtils.isEmpty(connectionType)) {
                fail = true;
            } else {
                jsObj.put(SDKUtils.encodeString(RequestParameters.CONNECTION_TYPE), SDKUtils.encodeString(connectionType));
            }
            String deviceLanguage = context.getResources().getConfiguration().locale.getLanguage();
            if (!TextUtils.isEmpty(deviceLanguage)) {
                jsObj.put(SDKUtils.encodeString(RequestParameters.DEVICE_LANGUAGE), SDKUtils.encodeString(deviceLanguage.toUpperCase()));
            }
            if (SupersonicStorageUtils.isExternalStorageAvailable()) {
                long freeDiskSize = SDKUtils.getAvailableSpaceInMB(context, this.mCacheDirectory);
                jsObj.put(SDKUtils.encodeString(RequestParameters.DISK_FREE_SIZE), SDKUtils.encodeString(String.valueOf(freeDiskSize)));
            } else {
                fail = true;
            }
            String width = String.valueOf(SDKUtils.getDeviceWidth());
            if (width != null) {
                key = new StringBuilder();
                key.append(SDKUtils.encodeString(RequestParameters.DEVICE_SCREEN_SIZE)).append(RequestParameters.LEFT_BRACKETS).append(SDKUtils.encodeString("width")).append(RequestParameters.RIGHT_BRACKETS);
                jsObj.put(key.toString(), SDKUtils.encodeString(width));
            } else {
                fail = true;
            }
            String height = String.valueOf(SDKUtils.getDeviceHeight());
            if (height != null) {
                key = new StringBuilder();
                key.append(SDKUtils.encodeString(RequestParameters.DEVICE_SCREEN_SIZE)).append(RequestParameters.LEFT_BRACKETS).append(SDKUtils.encodeString("height")).append(RequestParameters.RIGHT_BRACKETS);
                jsObj.put(key.toString(), SDKUtils.encodeString(height));
            } else {
                fail = true;
            }
            String packageName = SDKUtils.getPackageName(getBaseContext());
            if (!TextUtils.isEmpty(packageName)) {
                jsObj.put(SDKUtils.encodeString(RequestParameters.PACKAGE_NAME), SDKUtils.encodeString(packageName));
            }
            String scaleStr = String.valueOf(SDKUtils.getDeviceScale());
            if (!TextUtils.isEmpty(scaleStr)) {
                jsObj.put(SDKUtils.encodeString(RequestParameters.DEVICE_SCREEN_SCALE), SDKUtils.encodeString(scaleStr));
            }
            String rootStr = String.valueOf(DeviceStatus.isRootedDevice());
            if (!TextUtils.isEmpty(rootStr)) {
                jsObj.put(SDKUtils.encodeString(RequestParameters.IS_ROOT_DEVICE), SDKUtils.encodeString(rootStr));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            new SupersonicAsyncHttpRequestTask().execute(new String[]{Constants.NATIVE_EXCEPTION_BASE_URL + e.getStackTrace()[0].getMethodName()});
        }
        return new Object[]{jsObj.toString(), Boolean.valueOf(fail)};
    }

    private Object[] getApplicationParams(String productType) {
        boolean fail = false;
        JSONObject jsObj = new JSONObject();
        String appKey = "";
        String userId = "";
        Map<String, String> productExtraParams = null;
        if (TextUtils.isEmpty(productType)) {
            fail = true;
        } else {
            if (productType.equalsIgnoreCase(ProductType.BrandConnect.toString())) {
                appKey = this.mBCAppKey;
                userId = this.mBCUserId;
                productExtraParams = this.mBCExtraParameters;
            } else if (productType.equalsIgnoreCase(ProductType.Interstitial.toString())) {
                appKey = this.mISAppKey;
                userId = this.mISUserId;
                productExtraParams = this.mISExtraParameters;
            } else if (productType.equalsIgnoreCase(ProductType.OfferWall.toString())) {
                appKey = this.mOWAppKey;
                userId = this.mOWUserId;
                productExtraParams = this.mOWExtraParameters;
            }
            try {
                jsObj.put(ParametersKeys.PRODUCT_TYPE, productType);
            } catch (JSONException e) {
                e.printStackTrace();
                new SupersonicAsyncHttpRequestTask().execute(new String[]{"https://www.supersonicads.com/mobile/sdk5/log?method=noProductType"});
            }
        }
        if (TextUtils.isEmpty(userId)) {
            fail = true;
        } else {
            try {
                jsObj.put(SDKUtils.encodeString("applicationUserId"), SDKUtils.encodeString(userId));
            } catch (JSONException e2) {
                e2.printStackTrace();
                new SupersonicAsyncHttpRequestTask().execute(new String[]{"https://www.supersonicads.com/mobile/sdk5/log?method=encodeAppUserId"});
            }
        }
        if (TextUtils.isEmpty(appKey)) {
            fail = true;
        } else {
            try {
                jsObj.put(SDKUtils.encodeString("applicationKey"), SDKUtils.encodeString(appKey));
            } catch (JSONException e22) {
                e22.printStackTrace();
                new SupersonicAsyncHttpRequestTask().execute(new String[]{"https://www.supersonicads.com/mobile/sdk5/log?method=encodeAppKey"});
            }
        }
        if (!(productExtraParams == null || productExtraParams.isEmpty())) {
            for (Entry<String, String> entry : productExtraParams.entrySet()) {
                if (((String) entry.getKey()).equalsIgnoreCase("sdkWebViewCache")) {
                    setWebviewCache((String) entry.getValue());
                }
                try {
                    jsObj.put(SDKUtils.encodeString((String) entry.getKey()), SDKUtils.encodeString((String) entry.getValue()));
                } catch (JSONException e222) {
                    e222.printStackTrace();
                    new SupersonicAsyncHttpRequestTask().execute(new String[]{"https://www.supersonicads.com/mobile/sdk5/log?method=extraParametersToJson"});
                }
            }
        }
        return new Object[]{jsObj.toString(), Boolean.valueOf(fail)};
    }

    private Object[] getAppsStatus(String appIds, String requestId) {
        boolean fail = false;
        JSONObject result = new JSONObject();
        try {
            if (TextUtils.isEmpty(appIds) || appIds.equalsIgnoreCase("null")) {
                fail = true;
                result.put("error", "appIds is null or empty");
                return new Object[]{result.toString(), Boolean.valueOf(fail)};
            } else if (TextUtils.isEmpty(requestId) || requestId.equalsIgnoreCase("null")) {
                fail = true;
                result.put("error", "requestId is null or empty");
                return new Object[]{result.toString(), Boolean.valueOf(fail)};
            } else {
                List<ApplicationInfo> packages = getBaseContext().getPackageManager().getInstalledApplications(0);
                JSONArray appIdsArray = new JSONArray(appIds);
                JSONObject bundleIds = new JSONObject();
                for (int i = 0; i < appIdsArray.length(); i++) {
                    String appId = appIdsArray.getString(i).trim();
                    if (!TextUtils.isEmpty(appId)) {
                        JSONObject isInstalled = new JSONObject();
                        boolean found = false;
                        for (ApplicationInfo packageInfo : packages) {
                            if (appId.equalsIgnoreCase(packageInfo.packageName)) {
                                isInstalled.put(IS_INSTALLED, true);
                                bundleIds.put(appId, isInstalled);
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            isInstalled.put(IS_INSTALLED, false);
                            bundleIds.put(appId, isInstalled);
                        }
                    }
                }
                result.put(RESULT, bundleIds);
                result.put(REQUEST_ID, requestId);
                return new Object[]{result.toString(), Boolean.valueOf(fail)};
            }
        } catch (Exception e) {
            fail = true;
        }
    }

    public void onFileDownloadSuccess(SSAFile file) {
        if (file.getFile().contains(Constants.MOBILE_CONTROLLER_HTML)) {
            load(1);
        } else {
            assetCached(file.getFile(), file.getPath());
        }
    }

    public void onFileDownloadFail(SSAFile file) {
        if (file.getFile().contains(Constants.MOBILE_CONTROLLER_HTML)) {
            this.mGlobalControllerTimer.cancel();
            if (this.mRVmiss) {
                sendProductErrorMessage(ProductType.BrandConnect);
            }
            if (this.mISmiss) {
                sendProductErrorMessage(ProductType.Interstitial);
            }
            if (this.mOWmiss) {
                sendProductErrorMessage(ProductType.OfferWall);
            }
            if (this.mOWCreditsMiss) {
                sendProductErrorMessage(ProductType.OfferWallCredits);
                return;
            }
            return;
        }
        assetCachedFailed(file.getFile(), file.getPath(), file.getErrMsg());
    }

    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        Logger.i(this.TAG, url + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + mimetype);
    }

    private void toastingErrMsg(final String methodName, String value) {
        final String message = new SSAObj(value).getString(ParametersKeys.ERR_MSG);
        if (!TextUtils.isEmpty(message)) {
            Context ctx = getBaseContext();
            if (ctx instanceof Activity) {
                ((Activity) ctx).runOnUiThread(new Runnable() {
                    public void run() {
                        if (SupersonicWebView.this.getDebugMode() == DebugMode.MODE_3.getValue()) {
                            Toast.makeText(SupersonicWebView.this.getBaseContext(), methodName + " : " + message, 1).show();
                        }
                    }
                });
            }
        }
    }

    public void setControllerKeyPressed(String value) {
        this.mControllerKeyPressed = value;
    }

    public String getControllerKeyPressed() {
        String keyPressed = this.mControllerKeyPressed;
        setControllerKeyPressed("interrupt");
        return keyPressed;
    }

    public void runGenericFunction(String method, Map<String, String> keyValPairs, OnGenericFunctionListener listener) {
        this.mOnGenericFunctionListener = listener;
        if (JSMethods.INIT_BRAND_CONNECT.equalsIgnoreCase(method)) {
            initBrandConnect((String) keyValPairs.get("applicationUserId"), (String) keyValPairs.get("applicationKey"), keyValPairs, this.mOnRewardedVideoListener);
        } else if (JSMethods.SHOW_BRAND_CONNECT.equalsIgnoreCase(method)) {
            showBrandConnect();
        } else {
            injectJavascript(generateJSToInject(method, mapToJson(keyValPairs), JSMethods.ON_GENERIC_FUNCTION_SUCCESS, JSMethods.ON_GENERIC_FUNCTION_FAIL));
        }
    }

    public void deviceStatusChanged(boolean isWifiConnected, boolean isMobileConnected) {
        String networkType = "none";
        if (isWifiConnected) {
            networkType = RequestParameters.NETWORK_TYPE_WIFI;
        } else if (isMobileConnected) {
            networkType = RequestParameters.NETWORK_TYPE_3G;
        }
        injectJavascript(generateJSToInject(JSMethods.DEVICE_STATUS_CHANGED, parseToJson(RequestParameters.CONNECTION_TYPE, networkType, null, null, null, null, null, null, null, false)));
    }

    public void engageEnd(String action) {
        if (action.equals(ParametersKeys.FORCE_CLOSE)) {
            closeWebView();
        }
        injectJavascript(generateJSToInject(JSMethods.ENGAGE_END, parseToJson(ParametersKeys.ACTION, action, null, null, null, null, null, null, null, false)));
    }

    public void registerConnectionReceiver(Context context) {
        context.registerReceiver(this.mConnectionReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    public void unregisterConnectionReceiver(Context context) {
        try {
            context.unregisterReceiver(this.mConnectionReceiver);
        } catch (IllegalArgumentException e) {
        } catch (Exception e1) {
            Log.e(this.TAG, "unregisterConnectionReceiver - " + e1);
            new SupersonicAsyncHttpRequestTask().execute(new String[]{Constants.NATIVE_EXCEPTION_BASE_URL + e1.getStackTrace()[0].getMethodName()});
        }
    }

    public void pause() {
        if (VERSION.SDK_INT > 10) {
            try {
                onPause();
            } catch (Throwable e) {
                Logger.i(this.TAG, "WebViewController: pause() - " + e);
                new SupersonicAsyncHttpRequestTask().execute(new String[]{"https://www.supersonicads.com/mobile/sdk5/log?method=webviewPause"});
            }
        }
    }

    public void resume() {
        if (VERSION.SDK_INT > 10) {
            try {
                onResume();
            } catch (Throwable e) {
                Logger.i(this.TAG, "WebViewController: onResume() - " + e);
                new SupersonicAsyncHttpRequestTask().execute(new String[]{"https://www.supersonicads.com/mobile/sdk5/log?method=webviewResume"});
            }
        }
    }

    public void setOnWebViewControllerChangeListener(OnWebViewControllerChangeListener listener) {
        this.mChangeListener = listener;
    }

    public FrameLayout getLayout() {
        return this.mLayout;
    }

    public boolean inCustomView() {
        return this.mCustomView != null;
    }

    public void hideCustomView() {
        this.mWebChromeClient.onHideCustomView();
    }

    private void setWebviewCache(String value) {
        if (value.equalsIgnoreCase("0")) {
            getSettings().setCacheMode(2);
        } else {
            getSettings().setCacheMode(-1);
        }
    }

    public boolean handleSearchKeysURLs(String url) {
        List<String> searchKeys = SupersonicSharedPrefHelper.getSupersonicPrefHelper().getSearchKeys();
        if (!(searchKeys == null || searchKeys.isEmpty())) {
            for (String key : searchKeys) {
                if (url.contains(key)) {
                    getBaseContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                    return true;
                }
            }
        }
        return false;
    }

    public void setState(State state) {
        this.mState = state;
    }

    public State getState() {
        return this.mState;
    }

    private void sendProductErrorMessage(ProductType type) {
        String action = "";
        switch (type) {
            case BrandConnect:
                action = ErrorCodes.InitBC;
                break;
            case Interstitial:
                action = ErrorCodes.InitIS;
                break;
            case OfferWall:
                action = ErrorCodes.InitOW;
                break;
            case OfferWallCredits:
                action = ErrorCodes.ShowOWCredits;
                break;
        }
        triggerOnControllerInitProductFail(SDKUtils.createErrorMessage(action, ErrorCodes.InitiatingController), type);
    }

    public void destroy() {
        super.destroy();
        if (this.downloadManager != null) {
            this.downloadManager.release();
        }
        if (this.mConnectionReceiver != null) {
            this.mConnectionReceiver = null;
        }
    }

    private String generateJSToInject(String funToCall) {
        StringBuilder script = new StringBuilder();
        script.append("SSA_CORE.SDKController.runFunction('").append(funToCall).append("');");
        return script.toString();
    }

    private String generateJSToInject(String funToCall, String parameters) {
        StringBuilder script = new StringBuilder();
        script.append("SSA_CORE.SDKController.runFunction('").append(funToCall).append("?parameters=").append(parameters).append("');");
        return script.toString();
    }

    private String generateJSToInject(String funToCall, String successFunc, String failFunc) {
        StringBuilder script = new StringBuilder();
        script.append("SSA_CORE.SDKController.runFunction('").append(funToCall).append("','").append(successFunc).append("','").append(failFunc).append("');");
        return script.toString();
    }

    private String generateJSToInject(String funToCall, String parameters, String successFunc, String failFunc) {
        StringBuilder script = new StringBuilder();
        script.append("SSA_CORE.SDKController.runFunction('").append(funToCall).append("?parameters=").append(parameters).append("','").append(successFunc).append("','").append(failFunc).append("');");
        return script.toString();
    }

    public AdUnitsState getSavedState() {
        return this.mSavedState;
    }

    public void restoreState(AdUnitsState state) {
        synchronized (this.mSavedStateLocker) {
            if (state.shouldRestore() && this.mControllerState.equals(ControllerState.Ready)) {
                String appKey;
                String userId;
                Map<String, String> extraParams;
                Log.d(this.TAG, "restoreState(state:" + state + ")");
                int lastAd = state.getDisplayedProduct();
                if (lastAd != -1) {
                    if (lastAd == ProductType.BrandConnect.ordinal()) {
                        Log.d(this.TAG, "onRVAdClosed()");
                        if (this.mOnRewardedVideoListener != null) {
                            this.mOnRewardedVideoListener.onRVAdClosed();
                        }
                    } else if (lastAd == ProductType.Interstitial.ordinal()) {
                        Log.d(this.TAG, "onInterstitialAdClosed()");
                        if (this.mOnInitInterstitialListener != null) {
                            this.mOnInitInterstitialListener.onInterstitialAdClosed();
                        }
                    } else if (lastAd == ProductType.OfferWall.ordinal()) {
                        Log.d(this.TAG, "onOWAdClosed()");
                        if (this.mOnOfferWallListener != null) {
                            this.mOnOfferWallListener.onOWAdClosed();
                        }
                    }
                    state.adOpened(-1);
                } else {
                    Log.d(this.TAG, "No ad was opened");
                }
                if (state.isInterstitialInitSuccess()) {
                    Log.d(this.TAG, "onInterstitialAvailability(false)");
                    if (this.mOnInitInterstitialListener != null) {
                        this.mOnInitInterstitialListener.onInterstitialAvailability(false);
                    }
                    appKey = state.getInterstitialAppKey();
                    userId = state.getInterstitialUserId();
                    extraParams = state.getInterstitialExtraParams();
                    Log.d(this.TAG, "initInterstitial(appKey:" + appKey + ", userId:" + userId + ", extraParam:" + extraParams + ")");
                    initInterstitial(appKey, userId, extraParams, this.mOnInitInterstitialListener);
                }
                if (state.isRewardedVideoInitSuccess()) {
                    Log.d(this.TAG, "onRVNoMoreOffers()");
                    if (this.mOnRewardedVideoListener != null) {
                        this.mOnRewardedVideoListener.onRVNoMoreOffers();
                    }
                    appKey = state.getRewardedVideoAppKey();
                    userId = state.getRewardedVideoUserId();
                    extraParams = state.getRewardedVideoExtraParams();
                    Log.d(this.TAG, "initRewardedVideo(appKey:" + appKey + ", userId:" + userId + ", extraParam:" + extraParams + ")");
                    initBrandConnect(appKey, userId, extraParams, this.mOnRewardedVideoListener);
                }
                state.setShouldRestore(false);
            }
            this.mSavedState = state;
        }
    }
}
