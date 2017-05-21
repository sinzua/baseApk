package com.supersonic.mediationsdk;

import android.app.Activity;
import android.text.TextUtils;
import com.supersonic.mediationsdk.logger.SupersonicError;
import com.supersonic.mediationsdk.logger.SupersonicLogger.SupersonicTag;
import com.supersonic.mediationsdk.logger.SupersonicLoggerManager;
import com.supersonic.mediationsdk.sdk.GeneralProperties;
import com.supersonic.mediationsdk.sdk.OfferwallApi;
import com.supersonic.mediationsdk.sdk.OfferwallListener;
import com.supersonic.mediationsdk.sdk.SupersonicFactory;
import com.supersonic.mediationsdk.utils.ErrorBuilder;
import com.supersonic.mediationsdk.utils.ServerResponseWrapper;
import com.supersonic.mediationsdk.utils.SupersonicConstants;
import com.supersonic.mediationsdk.utils.SupersonicUtils;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONArray;

class OfferwallManager implements OfferwallApi, OfferwallListener {
    private final String GENERAL_PROPERTIES_USER_ID = "userId";
    private final String TAG = getClass().getName();
    private OfferwallApi mAdapter;
    private AtomicBoolean mAtomicShouldPerformInit = new AtomicBoolean(true);
    private AtomicBoolean mIsOfferwallAvailable = new AtomicBoolean(false);
    private OfferwallListener mListenersWrapper;
    private SupersonicLoggerManager mLoggerManager = SupersonicLoggerManager.getLogger();

    public synchronized void initOfferwall(final Activity activity, String appKey, final String userId) {
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
                            OfferwallManager.this.reportInitFail(ErrorBuilder.buildNoConfigurationAvailableError());
                            return;
                        } else if (srw.isValidResponse()) {
                            ArrayList<AbstractAdapter> startedAdapters = OfferwallManager.this.startAdapters(activity, userId, srw);
                            if (startedAdapters == null || startedAdapters.isEmpty()) {
                                OfferwallManager.this.reportInitFail(ErrorBuilder.buildAdapterInitFailedError("Please check configurations for Offerwall adapters"));
                                return;
                            }
                            sso.addAll(startedAdapters);
                            return;
                        } else {
                            SupersonicError error = srw.getReponseError();
                            if (error == null) {
                                error = ErrorBuilder.buildNoConfigurationAvailableError();
                            }
                            OfferwallManager.this.reportInitFail(error);
                            return;
                        }
                    }
                    OfferwallManager.this.reportInitFail(ErrorBuilder.buildGenericError("No Internet Connection"));
                }
            }, "OfferwallInitiator");
        }
    }

    private synchronized void reportInitFail(SupersonicError error) {
        if (this.mIsOfferwallAvailable != null) {
            this.mIsOfferwallAvailable.set(false);
        }
        if (this.mAtomicShouldPerformInit != null) {
            this.mAtomicShouldPerformInit.set(true);
        }
        if (this.mListenersWrapper != null) {
            this.mListenersWrapper.onOfferwallInitFail(error);
        }
    }

    private ArrayList<AbstractAdapter> startAdapters(Activity activity, String userId, ServerResponseWrapper serverResponseWrapper) {
        ArrayList<AbstractAdapter> adapterList = new ArrayList();
        JSONArray providersList = serverResponseWrapper.getOWProvidersArray();
        for (int i = 0; i < providersList.length(); i++) {
            String providerName = providersList.optJSONObject(i).optString("provider");
            String requestUrl = providersList.optJSONObject(i).optString(SupersonicConstants.REQUEST_URL);
            if (!providerName.isEmpty()) {
                try {
                    Class<?> mAdapterClass = Class.forName("com.supersonic.adapters." + providerName.toLowerCase() + "." + providerName + "Adapter");
                    AbstractAdapter providerAdapter = (AbstractAdapter) mAdapterClass.getMethod(SupersonicConstants.GET_INSTANCE, new Class[]{String.class, String.class}).invoke(mAdapterClass, new Object[]{providerName, requestUrl});
                    providerAdapter.setLogListener(this.mLoggerManager);
                    ((OfferwallApi) providerAdapter).setOfferwallListener(this);
                    addOfferwallAdapter((OfferwallApi) providerAdapter);
                    ((OfferwallApi) providerAdapter).initOfferwall(activity, ((SupersonicObject) SupersonicFactory.getInstance()).getSupersonicAppKey(), userId);
                    adapterList.add(providerAdapter);
                } catch (Throwable e) {
                    this.mLoggerManager.log(SupersonicTag.API, providerName + " initialization failed - please verify that required dependencies are in you build path.", 2);
                    this.mLoggerManager.logException(SupersonicTag.API, this.TAG + ":startAdapter(providerList:" + providersList.toString() + ")", e);
                }
            }
        }
        return adapterList;
    }

    public void addOfferwallAdapter(OfferwallApi adapter) {
        this.mAdapter = adapter;
    }

    public boolean hasProviders() {
        return this.mAdapter != null;
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

    public void showOfferwall() {
        if (this.mIsOfferwallAvailable != null && this.mIsOfferwallAvailable.get() && this.mAdapter != null) {
            this.mAdapter.showOfferwall();
        }
    }

    public synchronized boolean isOfferwallAvailable() {
        boolean result;
        result = false;
        if (this.mIsOfferwallAvailable != null) {
            result = this.mIsOfferwallAvailable.get();
        }
        return result;
    }

    public void getOfferwallCredits() {
        if (this.mAdapter != null) {
            this.mAdapter.getOfferwallCredits();
        }
    }

    public void setOfferwallListener(OfferwallListener offerwallListener) {
        this.mListenersWrapper = offerwallListener;
    }

    public void onOfferwallInitSuccess() {
        if (this.mIsOfferwallAvailable != null) {
            this.mIsOfferwallAvailable.set(true);
        }
        this.mLoggerManager.log(SupersonicTag.ADAPTER_CALLBACK, "onOfferwallInitSuccess()", 1);
        this.mListenersWrapper.onOfferwallInitSuccess();
    }

    public void onOfferwallInitFail(SupersonicError supersonicError) {
        this.mLoggerManager.log(SupersonicTag.ADAPTER_CALLBACK, "onOfferwallInitFail(" + supersonicError + ")", 1);
        reportInitFail(supersonicError);
    }

    public void onOfferwallOpened() {
        this.mLoggerManager.log(SupersonicTag.ADAPTER_CALLBACK, "onOfferwallOpened()", 1);
        this.mListenersWrapper.onOfferwallOpened();
    }

    public void onOfferwallShowFail(SupersonicError supersonicError) {
        this.mLoggerManager.log(SupersonicTag.ADAPTER_CALLBACK, "onOfferwallShowFail(" + supersonicError + ")", 1);
        this.mListenersWrapper.onOfferwallShowFail(supersonicError);
    }

    public boolean onOfferwallAdCredited(int credits, int totalCredits, boolean totalCreditsFlag) {
        return this.mListenersWrapper.onOfferwallAdCredited(credits, totalCredits, totalCreditsFlag);
    }

    public void onGetOfferwallCreditsFail(SupersonicError supersonicError) {
        this.mLoggerManager.log(SupersonicTag.ADAPTER_CALLBACK, "onGetOfferwallCreditsFail(" + supersonicError + ")", 1);
        this.mListenersWrapper.onGetOfferwallCreditsFail(supersonicError);
    }

    public void onOfferwallClosed() {
        this.mLoggerManager.log(SupersonicTag.ADAPTER_CALLBACK, "onOfferwallClosed()", 1);
        this.mListenersWrapper.onOfferwallClosed();
    }
}
