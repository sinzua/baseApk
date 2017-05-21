package com.supersonic.mediationsdk;

import android.app.Activity;
import android.text.TextUtils;
import com.supersonic.mediationsdk.logger.SupersonicError;
import com.supersonic.mediationsdk.logger.SupersonicLogger.SupersonicTag;
import com.supersonic.mediationsdk.logger.SupersonicLoggerManager;
import com.supersonic.mediationsdk.sdk.GeneralProperties;
import com.supersonic.mediationsdk.sdk.InterstitialApi;
import com.supersonic.mediationsdk.sdk.InterstitialListener;
import com.supersonic.mediationsdk.sdk.SupersonicFactory;
import com.supersonic.mediationsdk.utils.ErrorBuilder;
import com.supersonic.mediationsdk.utils.ServerResponseWrapper;
import com.supersonic.mediationsdk.utils.SupersonicConstants;
import com.supersonic.mediationsdk.utils.SupersonicUtils;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONArray;

class InterstitialManager implements InterstitialApi, InterstitialListener {
    private final String GENERAL_PROPERTIES_USER_ID = "userId";
    private final String TAG = getClass().getName();
    private InterstitialApi mAdapter;
    private AtomicBoolean mAtomicShouldPerformInit = new AtomicBoolean(true);
    private InterstitialListener mListenersWrapper;
    private SupersonicLoggerManager mLoggerManager = SupersonicLoggerManager.getLogger();

    public void addInterstitialAdapter(InterstitialApi adapter) {
        this.mAdapter = adapter;
    }

    public boolean hasProviders() {
        return this.mAdapter != null;
    }

    public boolean isInterstitialAdAvailable() {
        if (this.mAdapter != null) {
            return this.mAdapter.isInterstitialAdAvailable();
        }
        return false;
    }

    public synchronized void initInterstitial(final Activity activity, String appKey, final String userId) {
        if (this.mAtomicShouldPerformInit == null || !this.mAtomicShouldPerformInit.compareAndSet(true, false)) {
            this.mLoggerManager.log(SupersonicTag.API, this.TAG + ": Multiple calls to init are not allowed", 2);
        } else {
            if (!TextUtils.isEmpty(userId)) {
                GeneralProperties.getProperties().putKey("userId", userId);
            }
            SupersonicUtils.createAndStartWorker(new Runnable() {
                public void run() {
                    SupersonicObject sso = (SupersonicObject) SupersonicFactory.getInstance();
                    if (SupersonicUtils.isNetworkConnected(activity)) {
                        ServerResponseWrapper srw = sso.getServerResponse(activity, userId);
                        if (srw == null) {
                            InterstitialManager.this.reportInitFail(ErrorBuilder.buildNoConfigurationAvailableError());
                            return;
                        } else if (srw.isValidResponse()) {
                            ArrayList<AbstractAdapter> startedAdapters = InterstitialManager.this.startAdapters(activity, userId, srw);
                            if (startedAdapters == null || startedAdapters.isEmpty()) {
                                InterstitialManager.this.reportInitFail(ErrorBuilder.buildAdapterInitFailedError("Please check configurations for Interstitial adapters"));
                                return;
                            }
                            sso.addAll(startedAdapters);
                            return;
                        } else {
                            SupersonicError error = srw.getReponseError();
                            if (error == null) {
                                error = ErrorBuilder.buildNoConfigurationAvailableError();
                            }
                            InterstitialManager.this.reportInitFail(error);
                            return;
                        }
                    }
                    InterstitialManager.this.reportInitFail(ErrorBuilder.buildGenericError("No Internet Connection"));
                }
            }, "InterstitialInitiator");
        }
    }

    private synchronized void reportInitFail(SupersonicError error) {
        this.mAtomicShouldPerformInit.set(true);
        this.mListenersWrapper.onInterstitialInitFail(error);
    }

    private ArrayList<AbstractAdapter> startAdapters(Activity activity, String userId, ServerResponseWrapper serverResponseWrapper) {
        ArrayList<AbstractAdapter> adapterList = new ArrayList();
        JSONArray providersList = serverResponseWrapper.getISProvidersArray();
        for (int i = 0; i < providersList.length(); i++) {
            String providerName = providersList.optJSONObject(i).optString("provider");
            String requestUrl = providersList.optJSONObject(i).optString(SupersonicConstants.REQUEST_URL);
            if (!providerName.isEmpty()) {
                try {
                    Class<?> mAdapterClass = Class.forName("com.supersonic.adapters." + providerName.toLowerCase() + "." + providerName + "Adapter");
                    AbstractAdapter providerAdapter = (AbstractAdapter) mAdapterClass.getMethod(SupersonicConstants.GET_INSTANCE, new Class[]{String.class, String.class}).invoke(mAdapterClass, new Object[]{providerName, requestUrl});
                    providerAdapter.setLogListener(this.mLoggerManager);
                    ((InterstitialApi) providerAdapter).setInterstitialListener(this);
                    addInterstitialAdapter((InterstitialApi) providerAdapter);
                    ((InterstitialApi) providerAdapter).initInterstitial(activity, ((SupersonicObject) SupersonicFactory.getInstance()).getSupersonicAppKey(), userId);
                    adapterList.add(providerAdapter);
                } catch (Throwable e) {
                    this.mLoggerManager.log(SupersonicTag.API, providerName + " initialization failed - please verify that required dependencies are in you build path.", 2);
                    this.mLoggerManager.logException(SupersonicTag.API, this.TAG + ":startAdapter(providerList:" + providersList.toString() + ")", e);
                }
            }
        }
        return adapterList;
    }

    public void showInterstitial() {
        if (this.mAdapter != null) {
            this.mAdapter.showInterstitial();
        }
    }

    public void release(Activity activity) {
    }

    public void onResume(Activity activity) {
    }

    public void onPause(Activity activity) {
    }

    public void setAge(int age) {
    }

    public void setGender(String gender) {
    }

    public void setInterstitialListener(InterstitialListener isListener) {
        this.mListenersWrapper = isListener;
    }

    public void onInterstitialInitSuccess() {
        this.mLoggerManager.log(SupersonicTag.ADAPTER_CALLBACK, "onInterstitialInitSuccess()", 1);
        this.mListenersWrapper.onInterstitialInitSuccess();
    }

    public void onInterstitialInitFail(SupersonicError supersonicError) {
        this.mLoggerManager.log(SupersonicTag.ADAPTER_CALLBACK, "onInterstitialInitFail(" + supersonicError + ")", 1);
        reportInitFail(supersonicError);
    }

    public void onInterstitialAvailability(boolean available) {
        this.mLoggerManager.log(SupersonicTag.ADAPTER_CALLBACK, "onInterstitialAvailability(available:" + available + ")", 1);
        this.mListenersWrapper.onInterstitialAvailability(available);
    }

    public void onInterstitialShowSuccess() {
        this.mLoggerManager.log(SupersonicTag.ADAPTER_CALLBACK, "onInterstitialShowSuccess()", 1);
        this.mListenersWrapper.onInterstitialShowSuccess();
    }

    public void onInterstitialShowFail(SupersonicError supersonicError) {
        this.mLoggerManager.log(SupersonicTag.ADAPTER_CALLBACK, "onInterstitialShowFail(" + supersonicError + ")", 1);
        this.mListenersWrapper.onInterstitialShowFail(supersonicError);
    }

    public void onInterstitialAdClicked() {
        this.mLoggerManager.log(SupersonicTag.ADAPTER_CALLBACK, "onInterstitialAdClicked()", 1);
        this.mListenersWrapper.onInterstitialAdClicked();
    }

    public void onInterstitialAdClosed() {
        this.mLoggerManager.log(SupersonicTag.ADAPTER_CALLBACK, "onInterstitialAdClosed()", 1);
        this.mListenersWrapper.onInterstitialAdClosed();
    }
}
