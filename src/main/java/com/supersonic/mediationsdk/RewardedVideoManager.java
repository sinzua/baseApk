package com.supersonic.mediationsdk;

import android.app.Activity;
import android.content.IntentFilter;
import android.text.TextUtils;
import com.supersonic.environment.NetworkStateReceiver;
import com.supersonic.environment.NetworkStateReceiver.NetworkStateReceiverListener;
import com.supersonic.mediationsdk.events.EventsHelper;
import com.supersonic.mediationsdk.logger.SupersonicError;
import com.supersonic.mediationsdk.logger.SupersonicLogger.SupersonicTag;
import com.supersonic.mediationsdk.logger.SupersonicLoggerManager;
import com.supersonic.mediationsdk.model.Placement;
import com.supersonic.mediationsdk.sdk.GeneralProperties;
import com.supersonic.mediationsdk.sdk.RewardedVideoAdapterApi;
import com.supersonic.mediationsdk.sdk.RewardedVideoApi;
import com.supersonic.mediationsdk.sdk.RewardedVideoListener;
import com.supersonic.mediationsdk.sdk.RewardedVideoManagerListener;
import com.supersonic.mediationsdk.sdk.SupersonicFactory;
import com.supersonic.mediationsdk.server.Server;
import com.supersonic.mediationsdk.utils.ErrorBuilder;
import com.supersonic.mediationsdk.utils.ServerResponseWrapper;
import com.supersonic.mediationsdk.utils.SupersonicConstants;
import com.supersonic.mediationsdk.utils.SupersonicUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONArray;
import org.json.JSONObject;

class RewardedVideoManager implements RewardedVideoApi, RewardedVideoManagerListener, NetworkStateReceiverListener {
    private final String GENERAL_PROPERTIES_USER_ID = "userId";
    private final String KTO_ALGORITHM = "KTO";
    private final String TAG = getClass().getSimpleName();
    private Activity mActivity;
    private String mAppKey;
    private AtomicBoolean mAtomicShouldPerformInit = new AtomicBoolean(true);
    private ArrayList<RewardedVideoAdapterApi> mAvailableAdapters;
    private boolean mDidReportInit;
    private ArrayList<RewardedVideoAdapterApi> mExhaustedAdapters;
    private ArrayList<RewardedVideoAdapterApi> mInitiatedAdapters;
    private boolean mIsAdAvailable;
    private RewardedVideoListener mListenersWrapper;
    private SupersonicLoggerManager mLoggerManager = SupersonicLoggerManager.getLogger();
    NetworkStateReceiver mNetworkStateReceiver;
    private ArrayList<RewardedVideoAdapterApi> mNotAvailableAdapters;
    private boolean mPauseSmartLoadDueToNetworkUnavailability = false;
    private ServerResponseWrapper mServerResponseWrapper;
    private boolean mShouldTrackNetworkState = false;
    private String mUserId;

    public RewardedVideoManager() {
        prepareStateForInit();
    }

    private void prepareStateForInit() {
        this.mIsAdAvailable = false;
        this.mDidReportInit = false;
        this.mAvailableAdapters = new ArrayList();
        this.mInitiatedAdapters = new ArrayList();
        this.mNotAvailableAdapters = new ArrayList();
        this.mExhaustedAdapters = new ArrayList();
    }

    private synchronized void reportImpression(String adapterUrl, boolean hit, int placementId) {
        String url;
        try {
            url = ("" + adapterUrl) + "&sdkVersion=" + SupersonicUtils.getSDKVersion();
            Server.callAsyncRequestURL(url, hit, placementId);
        } catch (Throwable e) {
            this.mLoggerManager.logException(SupersonicTag.NETWORK, "reportImpression:(providerURL:" + url + ", " + "hit:" + hit + ")", e);
        }
    }

    private void reportFalseImpressionsOnHigherPriority(int priority, int placementId) {
        JSONArray providers = this.mServerResponseWrapper.getRVProvidersArray();
        for (int i = 0; i < priority; i++) {
            reportImpression(providers.optJSONObject(i).optString(SupersonicConstants.REQUEST_URL), false, placementId);
        }
    }

    public synchronized void initRewardedVideo(final Activity activity, String appKey, final String userId) {
        if (this.mAtomicShouldPerformInit == null || !this.mAtomicShouldPerformInit.compareAndSet(true, false)) {
            this.mLoggerManager.log(SupersonicTag.API, this.TAG + ": Multiple calls to init are not allowed", 2);
        } else {
            if (this.mShouldTrackNetworkState) {
                if (this.mNetworkStateReceiver == null) {
                    this.mNetworkStateReceiver = new NetworkStateReceiver(activity, this);
                }
                activity.registerReceiver(this.mNetworkStateReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            }
            this.mActivity = activity;
            this.mUserId = userId;
            this.mAppKey = appKey;
            prepareStateForInit();
            if (!TextUtils.isEmpty(userId)) {
                GeneralProperties.getProperties().putKey("userId", userId);
            }
            SupersonicUtils.createAndStartWorker(new Runnable() {
                public void run() {
                    SupersonicObject sso = (SupersonicObject) SupersonicFactory.getInstance();
                    if (SupersonicUtils.isNetworkConnected(activity)) {
                        RewardedVideoManager.this.mServerResponseWrapper = sso.getServerResponse(RewardedVideoManager.this.mActivity, userId);
                        if (RewardedVideoManager.this.mServerResponseWrapper == null) {
                            RewardedVideoManager.this.reportInitFail(ErrorBuilder.buildNoConfigurationAvailableError());
                            return;
                        } else if (RewardedVideoManager.this.mServerResponseWrapper.isValidResponse()) {
                            int numOfAdaptersToLoad = RewardedVideoManager.this.mServerResponseWrapper.getAdaptersSmartLoadAmount();
                            for (int i = 0; i < numOfAdaptersToLoad && RewardedVideoManager.this.loadNextAdapter() != null; i++) {
                            }
                            return;
                        } else {
                            SupersonicError error = RewardedVideoManager.this.mServerResponseWrapper.getReponseError();
                            if (error == null) {
                                error = ErrorBuilder.buildNoConfigurationAvailableError();
                            }
                            RewardedVideoManager.this.reportInitFail(error);
                            return;
                        }
                    }
                    RewardedVideoManager.this.reportInitFail(ErrorBuilder.buildGenericError("No Internet Connection"));
                }
            }, "RewardedVideoInitiator");
        }
    }

    private synchronized void reportInitFail(SupersonicError error) {
        this.mAtomicShouldPerformInit.set(true);
        this.mListenersWrapper.onRewardedVideoInitFail(error);
        this.mListenersWrapper.onVideoAvailabilityChanged(false);
    }

    private synchronized void reportShowFail(SupersonicError error) {
        this.mListenersWrapper.onRewardedVideoShowFail(error);
    }

    private synchronized AbstractAdapter startAdapter(ServerResponseWrapper srw) {
        AbstractAdapter abstractAdapter;
        JSONObject adapterObject = srw.getNextProvider();
        if (adapterObject == null) {
            abstractAdapter = null;
        } else {
            String providerName = adapterObject.optString("provider");
            String requestUrl = adapterObject.optString(SupersonicConstants.REQUEST_URL);
            this.mLoggerManager.log(SupersonicTag.NATIVE, this.TAG + ":startAdapter(" + providerName + ")", 1);
            if (providerName.isEmpty()) {
                abstractAdapter = null;
            } else {
                try {
                    Class<?> mAdapterClass = Class.forName("com.supersonic.adapters." + providerName.toLowerCase() + "." + providerName + "Adapter");
                    abstractAdapter = (AbstractAdapter) mAdapterClass.getMethod(SupersonicConstants.GET_INSTANCE, new Class[]{String.class, String.class}).invoke(mAdapterClass, new Object[]{providerName, requestUrl});
                    if (abstractAdapter.getMaxVideosPerIteration() < 1) {
                        abstractAdapter = null;
                    } else {
                        abstractAdapter.setLogListener(this.mLoggerManager);
                        abstractAdapter.setRewardedVideoTimeout(srw.getAdaptersSmartLoadTimeout());
                        abstractAdapter.setRewardedVideoPriority(srw.getAdaptersLoadPosition());
                        abstractAdapter.setPlacementsHolder(srw.getPlacementHolder());
                        ((RewardedVideoAdapterApi) abstractAdapter).setRewardedVideoListener(this);
                        this.mLoggerManager.log(SupersonicTag.NATIVE, this.TAG + ": startAdapter(" + providerName + ") moved to 'Initiated' list", 0);
                        addInitiatedRewardedVideoAdapter((RewardedVideoAdapterApi) abstractAdapter);
                        ((RewardedVideoAdapterApi) abstractAdapter).initRewardedVideo(this.mActivity, ((SupersonicObject) SupersonicFactory.getInstance()).getSupersonicAppKey(), this.mUserId);
                    }
                } catch (Throwable e) {
                    this.mLoggerManager.logException(SupersonicTag.API, this.TAG + ":startAdapter(JSONObject:" + adapterObject.toString() + ")", e);
                    this.mServerResponseWrapper.decreaseMaxRewardedVideoAdapters();
                    error = ErrorBuilder.buildAdapterInitFailedError(providerName + " initialization failed - please verify that required dependencies are in you build path.");
                    if (shouldCallInitFail()) {
                        SupersonicError error;
                        reportInitFail(error);
                    }
                    this.mLoggerManager.log(SupersonicTag.API, error.toString(), 2);
                    abstractAdapter = null;
                }
            }
        }
        return abstractAdapter;
    }

    public void release(Activity activity) {
    }

    public void onResume(Activity activity) {
        if (activity != null) {
            this.mActivity = activity;
        }
    }

    public void onPause(Activity activity) {
    }

    public void setAge(int age) {
    }

    public void setGender(String gender) {
    }

    public void showRewardedVideo() {
    }

    public synchronized void showRewardedVideo(String placementName) {
        ArrayList<RewardedVideoAdapterApi> unavailableAdapters = new ArrayList();
        if (SupersonicUtils.isNetworkConnected(this.mActivity)) {
            Iterator i$ = this.mAvailableAdapters.iterator();
            while (i$.hasNext()) {
                RewardedVideoAdapterApi adapter = (RewardedVideoAdapterApi) i$.next();
                if (adapter.isRewardedVideoAvailable()) {
                    if (this.mServerResponseWrapper.isUltraEventsEnabled()) {
                        Placement placement = this.mServerResponseWrapper.getPlacementHolder().getRewardedVideoPlacement(placementName);
                        reportImpression(((AbstractAdapter) adapter).getUrl(), true, placement.getId());
                        reportFalseImpressionsOnHigherPriority(((AbstractAdapter) adapter).getRewardedVideoPriority(), placement.getId());
                    }
                    adapter.showRewardedVideo(placementName);
                    ((AbstractAdapter) adapter).increaseNumberOfVideosPlayed();
                    this.mLoggerManager.log(SupersonicTag.INTERNAL, ((AbstractAdapter) adapter).getProviderName() + ": " + ((AbstractAdapter) adapter).getNumberOfVideosPlayed() + "/" + ((AbstractAdapter) adapter).getMaxVideosPerIteration() + " videos played", 0);
                    if (((AbstractAdapter) adapter).getNumberOfVideosPlayed() == ((AbstractAdapter) adapter).getMaxVideosPerIteration()) {
                        completeAdapterIteration((AbstractAdapter) adapter);
                    }
                    completeIterationRound();
                } else {
                    onVideoAvailabilityChanged(false, (AbstractAdapter) adapter);
                    this.mLoggerManager.logException(SupersonicTag.INTERNAL, ((AbstractAdapter) adapter).getProviderName() + " Failed to show video", new Exception("FailedToShowVideoException"));
                }
            }
        } else {
            reportShowFail(ErrorBuilder.buildShowVideoFailedError("No Internet Connection"));
        }
    }

    public synchronized boolean isRewardedVideoAvailable() {
        boolean z = false;
        synchronized (this) {
            Iterator i$ = this.mAvailableAdapters.iterator();
            while (i$.hasNext()) {
                RewardedVideoAdapterApi adapter = (RewardedVideoAdapterApi) i$.next();
                if (adapter.isRewardedVideoAvailable()) {
                    z = true;
                    break;
                }
                onVideoAvailabilityChanged(false, (AbstractAdapter) adapter);
            }
        }
        return z;
    }

    public void setRewardedVideoListener(RewardedVideoListener rewardedVideoListener) {
        this.mListenersWrapper = rewardedVideoListener;
    }

    public synchronized void onRewardedVideoInitSuccess(AbstractAdapter adapter) {
        this.mLoggerManager.log(SupersonicTag.ADAPTER_CALLBACK, adapter.getProviderName() + ":onRewardedVideoInitSuccess()", 1);
        EventsHelper.getInstance().notifyInitRewardedVideoResultEvent(adapter.getProviderName(), true);
        if (!this.mDidReportInit) {
            this.mDidReportInit = true;
            this.mListenersWrapper.onRewardedVideoInitSuccess();
        }
    }

    public synchronized void onRewardedVideoInitFail(SupersonicError error, AbstractAdapter adapter) {
        boolean shouldCallFail = false;
        if (!this.mPauseSmartLoadDueToNetworkUnavailability) {
            try {
                this.mLoggerManager.log(SupersonicTag.ADAPTER_CALLBACK, adapter.getProviderName() + ":onRewardedVideoInitFail(" + error + ")", 1);
                EventsHelper.getInstance().notifyInitRewardedVideoResultEvent(adapter.getProviderName(), false);
                this.mLoggerManager.log(SupersonicTag.NATIVE, "Smart Loading - " + adapter.getProviderName() + " moved to 'Not Available' list", 0);
                addUnavailableRewardedVideoAdapter((RewardedVideoAdapterApi) adapter);
                if (shouldCallInitFail()) {
                    shouldCallFail = true;
                } else {
                    loadNextAdapter();
                }
            } catch (Exception e) {
                this.mLoggerManager.logException(SupersonicTag.ADAPTER_CALLBACK, "onRewardedVideoInitFail(error:" + error + ", " + "provider:" + adapter.getProviderName() + ")", e);
            }
        }
        if (shouldCallFail) {
            reportInitFail(error);
        }
    }

    public void onRewardedVideoShowFail(SupersonicError error, AbstractAdapter adapter) {
        this.mLoggerManager.log(SupersonicTag.ADAPTER_CALLBACK, adapter.getProviderName() + ":onRewardedVideoShowFail(" + error + ")", 1);
        this.mListenersWrapper.onRewardedVideoShowFail(error);
    }

    private AbstractAdapter loadNextAdapter() {
        AbstractAdapter initiatedAdapter = null;
        if (this.mAvailableAdapters.size() + this.mInitiatedAdapters.size() < this.mServerResponseWrapper.getAdaptersSmartLoadAmount()) {
            while (this.mServerResponseWrapper.hasMoreProvidersToLoad() && initiatedAdapter == null) {
                initiatedAdapter = startAdapter(this.mServerResponseWrapper);
                if (initiatedAdapter != null) {
                    ((SupersonicObject) SupersonicFactory.getInstance()).addToAdaptersList(initiatedAdapter);
                }
            }
        }
        return initiatedAdapter;
    }

    public void onRewardedVideoAdOpened(AbstractAdapter adapter) {
        this.mLoggerManager.log(SupersonicTag.ADAPTER_CALLBACK, adapter.getProviderName() + ":onRewardedVideoAdOpened()", 1);
        EventsHelper.getInstance().notifyVideoAdOpenedEvent(adapter.getProviderName());
        this.mListenersWrapper.onRewardedVideoAdOpened();
    }

    public void onRewardedVideoAdClosed(AbstractAdapter adapter) {
        this.mLoggerManager.log(SupersonicTag.ADAPTER_CALLBACK, adapter.getProviderName() + ":onRewardedVideoAdClosed()", 1);
        EventsHelper.getInstance().notifyVideoAdClosedEvent(adapter.getProviderName());
        this.mListenersWrapper.onRewardedVideoAdClosed();
        notifyIsAdAvailableForStatistics();
    }

    public synchronized void onVideoAvailabilityChanged(boolean available, AbstractAdapter adapter) {
        boolean shouldCallPublisher = false;
        if (!this.mPauseSmartLoadDueToNetworkUnavailability) {
            try {
                this.mLoggerManager.log(SupersonicTag.ADAPTER_CALLBACK, adapter.getProviderName() + ":onVideoAvailabilityChanged(available:" + available + ")", 1);
                EventsHelper.getInstance().notifyVideoAvailabilityChangedEvent(adapter.getProviderName(), available);
                if (!this.mExhaustedAdapters.contains(adapter)) {
                    if (available) {
                        this.mLoggerManager.log(SupersonicTag.NATIVE, "Smart Loading - " + adapter.getProviderName() + " moved to 'Available' list", 0);
                        addAvailableRewardedVideoAdapter((RewardedVideoAdapterApi) adapter, false);
                    } else {
                        this.mLoggerManager.log(SupersonicTag.NATIVE, "Smart Loading - " + adapter.getProviderName() + " moved to 'Not Available' list", 0);
                        addUnavailableRewardedVideoAdapter((RewardedVideoAdapterApi) adapter);
                        loadNextAdapter();
                        completeIterationRound();
                    }
                    shouldCallPublisher = shouldNotifyAvailabilityChanged(available);
                }
            } catch (Throwable e) {
                this.mLoggerManager.logException(SupersonicTag.ADAPTER_CALLBACK, "onVideoAvailabilityChanged(available:" + available + ", " + "provider:" + adapter.getProviderName() + ")", e);
            }
        }
        if (shouldCallPublisher) {
            this.mListenersWrapper.onVideoAvailabilityChanged(this.mIsAdAvailable);
        }
    }

    private void completeAdapterIteration(AbstractAdapter adapter) {
        try {
            this.mLoggerManager.log(SupersonicTag.ADAPTER_CALLBACK, adapter.getProviderName() + ":completeIteration", 1);
            this.mLoggerManager.log(SupersonicTag.NATIVE, "Smart Loading - " + adapter.getProviderName() + " moved to 'Exhausted' list", 0);
            addExhaustedRewardedVideoAdapter((RewardedVideoAdapterApi) adapter);
            loadNextAdapter();
            adapter.resetNumberOfVideosPlayed();
        } catch (Throwable e) {
            this.mLoggerManager.logException(SupersonicTag.ADAPTER_CALLBACK, "completeIteration(provider:" + adapter.getProviderName() + ")", e);
        }
    }

    private boolean isIterationRoundComplete() {
        if (this.mInitiatedAdapters.size() == 0 && this.mAvailableAdapters.size() == 0) {
            return true;
        }
        return false;
    }

    private void completeIterationRound() {
        if (isIterationRoundComplete()) {
            this.mLoggerManager.log(SupersonicTag.INTERNAL, "Reset Iteration", 0);
            Iterator i$ = ((ArrayList) this.mExhaustedAdapters.clone()).iterator();
            while (i$.hasNext()) {
                RewardedVideoAdapterApi exhaustedAdapter = (RewardedVideoAdapterApi) i$.next();
                if (exhaustedAdapter.isRewardedVideoAvailable()) {
                    this.mLoggerManager.log(SupersonicTag.INTERNAL, ((AbstractAdapter) exhaustedAdapter).getProviderName() + ": " + "moved to 'Available'", 0);
                    addAvailableRewardedVideoAdapter(exhaustedAdapter, true);
                } else {
                    this.mLoggerManager.log(SupersonicTag.INTERNAL, ((AbstractAdapter) exhaustedAdapter).getProviderName() + ": " + "moved to 'Not Available'", 0);
                    addUnavailableRewardedVideoAdapter(exhaustedAdapter);
                }
            }
            this.mLoggerManager.log(SupersonicTag.INTERNAL, "End of Reset Iteration", 0);
        }
    }

    private boolean shouldNotifyAvailabilityChanged(boolean adapterAvailability) {
        if (!this.mIsAdAvailable && adapterAvailability && this.mAvailableAdapters.size() > 0) {
            this.mIsAdAvailable = true;
            return true;
        } else if (this.mIsAdAvailable && !adapterAvailability && this.mAvailableAdapters.size() <= 0) {
            this.mIsAdAvailable = false;
            return true;
        } else if (adapterAvailability || this.mNotAvailableAdapters.size() < this.mServerResponseWrapper.getMaxRewardedVideoAdapters()) {
            return false;
        } else {
            this.mIsAdAvailable = false;
            return true;
        }
    }

    private boolean shouldCallInitFail() {
        if (this.mNotAvailableAdapters.size() < this.mServerResponseWrapper.getMaxRewardedVideoAdapters() || this.mDidReportInit) {
            return false;
        }
        this.mDidReportInit = true;
        return true;
    }

    private synchronized void addToAvailable(RewardedVideoAdapterApi adapter, boolean forceOrder) {
        String adapterAlgorithm = this.mServerResponseWrapper.getAdapterAlgorithm();
        int priorityLocation = this.mAvailableAdapters.size();
        if (!this.mAvailableAdapters.contains(adapter)) {
            if ("KTO".equalsIgnoreCase(adapterAlgorithm) || forceOrder) {
                Iterator i$ = this.mAvailableAdapters.iterator();
                while (i$.hasNext()) {
                    RewardedVideoAdapterApi rwa = (RewardedVideoAdapterApi) i$.next();
                    if (((AbstractAdapter) adapter).getRewardedVideoPriority() <= ((AbstractAdapter) rwa).getRewardedVideoPriority()) {
                        priorityLocation = this.mAvailableAdapters.indexOf(rwa);
                        break;
                    }
                }
            }
            this.mAvailableAdapters.add(priorityLocation, adapter);
        }
    }

    private synchronized void removeFromAvailable(RewardedVideoAdapterApi adapter) {
        if (this.mAvailableAdapters.contains(adapter)) {
            this.mAvailableAdapters.remove(adapter);
        }
    }

    private synchronized void addToNotAvailable(RewardedVideoAdapterApi adapter) {
        if (!this.mNotAvailableAdapters.contains(adapter)) {
            this.mNotAvailableAdapters.add(adapter);
        }
    }

    private synchronized void removeFromUnavailable(RewardedVideoAdapterApi adapter) {
        if (this.mNotAvailableAdapters.contains(adapter)) {
            this.mNotAvailableAdapters.remove(adapter);
        }
    }

    private synchronized void addToInitiated(RewardedVideoAdapterApi adapter) {
        if (!this.mInitiatedAdapters.contains(adapter)) {
            this.mInitiatedAdapters.add(adapter);
        }
    }

    private synchronized void removeFromInitiated(RewardedVideoAdapterApi adapter) {
        if (this.mInitiatedAdapters.contains(adapter)) {
            this.mInitiatedAdapters.remove(adapter);
        }
    }

    private synchronized void addToExhausted(RewardedVideoAdapterApi adapter) {
        if (!this.mExhaustedAdapters.contains(adapter)) {
            this.mExhaustedAdapters.add(adapter);
        }
    }

    private synchronized void removeFromExhausted(RewardedVideoAdapterApi adapter) {
        if (this.mExhaustedAdapters.contains(adapter)) {
            this.mExhaustedAdapters.remove(adapter);
        }
    }

    public synchronized void addAvailableRewardedVideoAdapter(RewardedVideoAdapterApi adapter, boolean forceOrder) {
        addToAvailable(adapter, forceOrder);
        removeFromInitiated(adapter);
        removeFromUnavailable(adapter);
        removeFromExhausted(adapter);
    }

    public synchronized void addInitiatedRewardedVideoAdapter(RewardedVideoAdapterApi adapter) {
        addToInitiated(adapter);
        removeFromUnavailable(adapter);
        removeFromAvailable(adapter);
        removeFromExhausted(adapter);
    }

    public synchronized void addUnavailableRewardedVideoAdapter(RewardedVideoAdapterApi adapter) {
        addToNotAvailable(adapter);
        removeFromAvailable(adapter);
        removeFromInitiated(adapter);
        removeFromExhausted(adapter);
    }

    public synchronized void addExhaustedRewardedVideoAdapter(RewardedVideoAdapterApi adapter) {
        addToExhausted(adapter);
        removeFromAvailable(adapter);
        removeFromInitiated(adapter);
        removeFromUnavailable(adapter);
    }

    public void onVideoStart(AbstractAdapter adapter) {
        this.mLoggerManager.log(SupersonicTag.ADAPTER_CALLBACK, adapter.getProviderName() + ":onVideoStart()", 1);
        EventsHelper.getInstance().notifyVideoStartEvent(adapter.getProviderName());
        this.mListenersWrapper.onVideoStart();
    }

    public void onVideoEnd(AbstractAdapter adapter) {
        this.mLoggerManager.log(SupersonicTag.ADAPTER_CALLBACK, adapter.getProviderName() + ":onVideoEnd()", 1);
        EventsHelper.getInstance().notifyVideoEndEvent(adapter.getProviderName());
        this.mListenersWrapper.onVideoEnd();
    }

    public void onRewardedVideoAdRewarded(Placement placement, AbstractAdapter adapter) {
        this.mLoggerManager.log(SupersonicTag.ADAPTER_CALLBACK, adapter.getProviderName() + ":onRewardedVideoAdRewarded(" + placement + ")", 1);
        if (placement == null) {
            placement = this.mServerResponseWrapper.getPlacementHolder().getDefaultRewardedVideoPlacement();
        }
        EventsHelper.getInstance().notifyProviderVideoAdRewardedEvent(adapter.getProviderName(), placement.getPlacementName(), placement.getRewardName(), placement.getRewardAmount(), this.mAppKey);
        this.mListenersWrapper.onRewardedVideoAdRewarded(placement);
    }

    private synchronized void notifyIsAdAvailableForStatistics() {
        boolean mediationStatus = false;
        if (this.mAvailableAdapters != null && this.mAvailableAdapters.size() > 0) {
            mediationStatus = true;
        }
        EventsHelper.getInstance().notifyIsRewardedVideoAvailableEvent(SupersonicConstants.MEDIATION_PROVIDER_NAME, mediationStatus);
        Iterator i$ = this.mAvailableAdapters.iterator();
        while (i$.hasNext()) {
            EventsHelper.getInstance().notifyIsRewardedVideoAvailableEvent(((AbstractAdapter) ((RewardedVideoAdapterApi) i$.next())).getProviderName(), true);
        }
        i$ = this.mNotAvailableAdapters.iterator();
        while (i$.hasNext()) {
            EventsHelper.getInstance().notifyIsRewardedVideoAvailableEvent(((AbstractAdapter) ((RewardedVideoAdapterApi) i$.next())).getProviderName(), false);
        }
        i$ = this.mInitiatedAdapters.iterator();
        while (i$.hasNext()) {
            EventsHelper.getInstance().notifyIsRewardedVideoAvailableEvent(((AbstractAdapter) ((RewardedVideoAdapterApi) i$.next())).getProviderName(), false);
        }
    }

    void shouldTrackNetworkState(boolean track) {
        this.mLoggerManager.log(SupersonicTag.INTERNAL, "Should Track Network State: " + track, 0);
        this.mShouldTrackNetworkState = track;
        if (this.mShouldTrackNetworkState) {
            if (this.mNetworkStateReceiver == null) {
                this.mNetworkStateReceiver = new NetworkStateReceiver(this.mActivity, this);
            }
            this.mActivity.registerReceiver(this.mNetworkStateReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        } else if (this.mNetworkStateReceiver != null) {
            this.mActivity.unregisterReceiver(this.mNetworkStateReceiver);
        }
    }

    public void onNetworkAvailabilityChanged(boolean connected) {
        if (this.mShouldTrackNetworkState) {
            this.mLoggerManager.log(SupersonicTag.INTERNAL, "Network Availability Changed To: " + connected, 0);
            if (shouldNotifyNetworkAvailabilityChanged(connected)) {
                if (connected) {
                    this.mPauseSmartLoadDueToNetworkUnavailability = false;
                } else {
                    this.mPauseSmartLoadDueToNetworkUnavailability = true;
                }
                this.mListenersWrapper.onVideoAvailabilityChanged(connected);
            }
        }
    }

    private boolean shouldNotifyNetworkAvailabilityChanged(boolean networkState) {
        if (!this.mIsAdAvailable && networkState && this.mAvailableAdapters.size() > 0) {
            this.mIsAdAvailable = true;
            return true;
        } else if (!this.mIsAdAvailable || networkState) {
            return false;
        } else {
            this.mIsAdAvailable = false;
            return true;
        }
    }
}
