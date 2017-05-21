package com.nativex.monetization.mraid;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.nativex.common.JsonRequestConstants.URLS;
import com.nativex.common.Log;
import com.nativex.common.NetworkConnectionManager;
import com.nativex.common.ServerConfig;
import com.nativex.common.Utilities;
import com.nativex.monetization.MonetizationManager;
import com.nativex.monetization.communication.ServerRequestManager;
import com.nativex.monetization.enums.AdEvent;
import com.nativex.monetization.enums.BannerPosition;
import com.nativex.monetization.enums.VideoProgress;
import com.nativex.monetization.listeners.NativeVideoPlayerListener;
import com.nativex.monetization.listeners.OnAdEvent;
import com.nativex.monetization.listeners.OnAdEventBase;
import com.nativex.monetization.listeners.OnAdEventV2;
import com.nativex.monetization.manager.ActivityManager;
import com.nativex.monetization.manager.CacheDBManager;
import com.nativex.monetization.manager.CacheManager;
import com.nativex.monetization.manager.MonetizationSharedDataManager;
import com.nativex.monetization.manager.SessionManager;
import com.nativex.monetization.mraid.MRAIDUtils.PlacementType;
import com.nativex.videoplayer.NativeXVideoPlayer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MRAIDManager {
    private static final int MSG_EXPIRE_CONTAINER = 1003;
    private static final long MSG_EXPIRE_CONTAINER_DELAY = 180000;
    private static final int MSG_RELEASE_AD = 1002;
    private static final long MSG_RELEASE_AD_DELAY = 2000;
    private static final String PARAM_CACHED_OFFERS = "CachedOffers";
    private static final String PARAM_PLACEMENT_ID = "placement";
    private static final String PARAM_PLACEMENT_TYPE = "placementtype";
    private static final String PARAM_RESPONSE_TYPE = "responseType";
    private static final String PARAM_SESSION_ID = "SessionId";
    public static final String PARAM_THEME_ID = "ThemeId";
    public static final String PARAM_UNIT_ID = "AdUnitId";
    private static MRAIDContainerHolder containerHolder;
    private static final MRAIDManagerInnerHandler handler = new MRAIDManagerInnerHandler(Looper.getMainLooper());
    private static final String mraidFolder = null;
    private static boolean runningOnUnity = false;
    private static final NativeVideoPlayerListener sNativexVideoPlayerListener = new NativeVideoPlayerListener();

    private static class MRAIDManagerInnerHandler extends Handler {
        public MRAIDManagerInnerHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            if (msg.obj != null && (msg.obj instanceof MRAIDContainer)) {
                MRAIDContainer container = msg.obj;
                switch (msg.what) {
                    case MRAIDManager.MSG_RELEASE_AD /*1002*/:
                        MRAIDManager.releaseAd(container);
                        return;
                    case MRAIDManager.MSG_EXPIRE_CONTAINER /*1003*/:
                        MRAIDManager.expireContainer(container);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public static final boolean showBanner(Activity activity, String name, Rect position, BannerPosition adPosition, OnAdEventBase listener, boolean userCall) {
        return showAdBase(activity, name, PlacementType.INLINE, position, adPosition, listener, userCall);
    }

    public static final boolean cacheBanner(Activity activity, String name, Rect position, OnAdEventBase listener) {
        return cacheAd(activity, name, PlacementType.INLINE, listener, position);
    }

    public static final boolean showInterstitial(Activity activity, String name, OnAdEventBase listener, boolean userCall) {
        return showAdBase(activity, name, PlacementType.INTERSTITIAL, null, null, listener, userCall);
    }

    public static final boolean cacheInterstitial(Activity activity, String name, OnAdEventBase listener) {
        return cacheAd(activity, name, PlacementType.INTERSTITIAL, listener, null);
    }

    private static boolean cacheAd(Activity activity, String name, PlacementType type, OnAdEventBase listener, Rect position) {
        return cacheAdBase(activity, name, type, position, null, listener);
    }

    private static boolean cacheAdBase(Activity activity, String placementName, PlacementType placementType, Rect position, BannerPosition bannerPosition, OnAdEventBase adEventListener) {
        MapDataHolder holder = null;
        try {
            if (containerHolder != null) {
                holder = containerHolder.getMapDataHolder(placementType, placementName);
            }
            if (holder == null) {
                return CreateNewContainerAndAd(activity, placementName, placementType, position, bannerPosition, adEventListener, true);
            }
            if (holder.cached != null) {
                if (adEventListener != null) {
                    holder.cached.setOnRichMediaEventListener(adEventListener);
                }
                if (holder.cached.isAdLoaded()) {
                    MRAIDLogger.i("Ad already fetched");
                    fireListener(holder.cached, AdEvent.ALREADY_FETCHED, "Ad is already fetched and ready to display");
                    return true;
                }
                MRAIDLogger.i("Ad is fetching");
                fireListener(holder.cached, AdEvent.DOWNLOADING, "Ad is being downloaded at the moment");
                return true;
            } else if (SessionManager.hasSession()) {
                MRAIDLogger.i("Caching ad");
                holder.cached = createContainer(activity, placementType, placementName, position, bannerPosition, adEventListener, true);
                Log.d("container created, cached = " + holder.cached.toString());
                expireContainerDelayed(holder.cached);
                return true;
            } else {
                fireListener(placementName, adEventListener, AdEvent.ERROR, "No session. Unable to fetch an ad.");
                return false;
            }
        } catch (Exception e) {
            fireListener(placementName, adEventListener, AdEvent.ERROR, "Error while creating/updating the ad");
            MRAIDLogger.e("Exception caught while handling ad request", e);
            return false;
        }
    }

    private static boolean showAdBase(Activity activity, String placementName, PlacementType placementType, Rect position, BannerPosition bannerPosition, OnAdEventBase adEventListener, boolean userCall) {
        if (activity != null) {
            try {
                if (VERSION.SDK_INT >= 19) {
                    try {
                        ActivityManager.setSystemUIVisibility(activity.getWindow().getDecorView().getSystemUiVisibility());
                    } catch (Exception e) {
                        Log.e("caught exception getting system ui visibility: ", e);
                    }
                }
            } catch (Exception e2) {
                fireListener(placementName, adEventListener, AdEvent.ERROR, "Error while creating/updating the ad");
                MRAIDLogger.e("Exception caught while handling ad request", e2);
                return false;
            }
        }
        MapDataHolder holder = null;
        if (containerHolder != null) {
            holder = containerHolder.getMapDataHolder(placementType, placementName);
        }
        if (holder == null) {
            return CreateNewContainerAndAd(activity, placementName, placementType, position, bannerPosition, adEventListener, false);
        }
        if (holder.shown != null) {
            if (adEventListener != null) {
                holder.shown.setOnRichMediaEventListener(adEventListener);
            }
            if (bannerPosition != null) {
                holder.shown.setBannerPosition(bannerPosition);
            } else {
                holder.shown.setAdPosition(position);
            }
            Activity parentActivity = null;
            OnAdEventBase parentListener = null;
            if (SessionManager.isBackupAdsEnabled()) {
                parentActivity = holder.shown.getActivity();
                parentListener = holder.shown.getOnRichMediaEventListener();
            }
            holder.shown.attachToActivity(activity);
            if (userCall) {
                fireListener(holder.shown, AdEvent.ALREADY_SHOWN, "Ad is already shown. Dismiss it in order to show another one");
            }
            MRAIDLogger.i("Updating existing ad");
            if (SessionManager.isBackupAdsEnabled() && parentActivity != null && !isRunningOnUnity() && holder.cached == null) {
                StartSecondaryAdDownload(parentActivity, placementName, parentListener);
            }
            return true;
        } else if (holder.cached != null) {
            holder.shown = holder.cached;
            holder.cached = null;
            if (adEventListener != null) {
                holder.shown.setOnRichMediaEventListener(adEventListener);
            }
            fireListener(holder.shown, AdEvent.BEFORE_DISPLAY, "Before Ad is displayed");
            holder.shown.setAdCached(false);
            if (bannerPosition != null) {
                holder.shown.setBannerPosition(bannerPosition);
            } else {
                holder.shown.setAdPosition(position);
            }
            if (holder.shown.getPlacementType() == PlacementType.INTERSTITIAL) {
                handler.removeMessages(MSG_EXPIRE_CONTAINER, holder.shown);
            }
            if (isRunningOnUnity() || holder.shown.getPlacementType() != PlacementType.INTERSTITIAL) {
                holder.shown.attachToActivity(activity);
                if (SessionManager.isBackupAdsEnabled() && isRunningOnUnity()) {
                    StartSecondaryAdDownload(holder.shown.getActivity(), placementName, holder.shown.getOnRichMediaEventListener());
                }
            } else {
                ActivityManager.startMRAIDInterstitial(activity, placementName, false);
            }
            MRAIDLogger.i("Showing fetched ad");
            return true;
        } else if (SessionManager.hasSession()) {
            MRAIDLogger.i("Creating ad to show");
            holder.shown = createContainer(activity, placementType, placementName, position, bannerPosition, adEventListener, false);
            if (placementType == PlacementType.INLINE) {
                expireContainerDelayed(holder.shown);
            }
            return true;
        } else {
            MRAIDLogger.e("Could not create ad. No session");
            fireListener(placementName, adEventListener, AdEvent.ERROR, "No session. Unable to fetch an ad.");
            return false;
        }
    }

    static void StartSecondaryAdDownload(final Activity activity, final String placementName, final OnAdEventBase listener) {
        if (!SessionManager.isBackupAdsEnabled()) {
            Log.w("backup ads are not enabled; will not fetch secondary ads...");
        } else if (!NetworkConnectionManager.getInstance(MonetizationSharedDataManager.getContext()).isConnected()) {
            Log.w("No internet connection; cannot make backup ad request");
        } else if (activity == null) {
            Log.w("Cannot start secondary download; activity is null!");
        } else {
            Log.d("Backup ads enabled, fetching new ad for " + placementName);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Log.d("Backup ads fetch delayed run (1sec), now fetching placement " + placementName);
                    MonetizationManager.fetchAd(activity, placementName, listener);
                }
            }, 1000);
        }
    }

    public static boolean CreateNewContainerAndAd(Activity activity, String placementName, PlacementType placementType, Rect position, BannerPosition bannerPosition, OnAdEventBase adEventListener, boolean shouldCache) {
        if (SessionManager.hasSession()) {
            MRAIDLogger.i("No ads.. creating ad");
            if (containerHolder == null) {
                MRAIDLogger.i("container holder is null.. creating MRAIDContainerHolder");
                containerHolder = new MRAIDContainerHolder();
            }
            MRAIDContainer container = createContainer(activity, placementType, placementName, position, bannerPosition, adEventListener, shouldCache);
            if (shouldCache || placementType == PlacementType.INLINE) {
                expireContainerDelayed(container);
            }
            return true;
        }
        MRAIDLogger.e("Could not create ad. No session");
        fireListener(placementName, adEventListener, AdEvent.ERROR, "No session. Unable to fetch an ad.");
        return false;
    }

    public static void setViewTimeOut(MRAIDContainer container, long timeout) {
        if (container == null) {
            return;
        }
        if (container.getPlacementType() == PlacementType.INTERSTITIAL) {
            if (container.isAdCached()) {
                updateExpirationTime(container, timeout);
            }
        } else if (container.getPlacementType() == PlacementType.INLINE) {
            updateExpirationTime(container, timeout);
        }
    }

    private static void updateExpirationTime(MRAIDContainer container, long timeout) {
        if (container != null) {
            handler.removeMessages(MSG_EXPIRE_CONTAINER, container);
            Message msg = handler.obtainMessage(MSG_EXPIRE_CONTAINER, container);
            MRAIDLogger.d("ViewTimeout updated");
            handler.sendMessageDelayed(msg, timeout);
        }
    }

    private static void expireContainerDelayed(MRAIDContainer container) {
        if (container != null) {
            handler.sendMessageDelayed(handler.obtainMessage(MSG_EXPIRE_CONTAINER, container), MSG_EXPIRE_CONTAINER_DELAY);
        }
    }

    private static void expireContainer(MRAIDContainer container) {
        if (container != null) {
            try {
                if (container.getPlacementType() != PlacementType.INLINE) {
                    container.fireListener(AdEvent.EXPIRED, "The ad has expired");
                    if (SessionManager.isBackupAdsEnabled()) {
                        StartSecondaryAdDownload(container.getActivity(), container.getAdName(), container.getOnRichMediaEventListener());
                    }
                    releaseAd(container);
                } else if (container.getState() == States.EXPANDED) {
                    container.setAdExpired(true);
                } else {
                    container.fireListener(AdEvent.EXPIRED, "The ad has expired");
                    releaseAd(container);
                }
            } catch (Exception e) {
                MRAIDLogger.e(container, "Unable to release expired container", e);
            }
        }
    }

    private static MRAIDContainer createContainer(Activity activity, PlacementType type, String name, Rect position, BannerPosition adPosition, OnAdEventBase listener, boolean shouldCache) {
        String sessionId = SessionManager.getSessionId();
        MRAIDContainer container = new MRAIDContainer(activity);
        container.setAdName(name);
        container.setPlacementType(type);
        if (position != null) {
            container.setAdPosition(position);
        }
        container.setBannerPosition(adPosition);
        container.setOnRichMediaEventListener(listener);
        container.setAdCached(shouldCache);
        container.loadAdUrl(getUrl(sessionId, name, type));
        containerHolder.putContainer(container, shouldCache);
        container.attachToActivity(activity);
        MRAIDLogger.i(container, "Ad Created");
        return container;
    }

    public static final void setAdPosition(String adName, Rect position, BannerPosition adPosition) {
        try {
            if (containerHolder != null) {
                MRAIDContainer container = containerHolder.getContainer(PlacementType.INLINE, adName);
                if (container == null) {
                    MRAIDLogger.e("Error setting ad position: Invalid Id");
                } else if (adPosition != null) {
                    container.setBannerPosition(adPosition);
                } else {
                    container.setAdPosition(position);
                }
            }
        } catch (Exception e) {
            MRAIDLogger.e("Error setting ad position", e);
        }
    }

    private static void fireListener(final String placement, final OnAdEventBase listener, final AdEvent event, final String message) {
        try {
            handler.post(new Runnable() {
                public void run() {
                    try {
                        if (listener == null) {
                            return;
                        }
                        if (listener instanceof OnAdEvent) {
                            ((OnAdEvent) listener).onEvent(event, message);
                        } else if (listener instanceof OnAdEventV2) {
                            AdInfo adInfo = new AdInfo();
                            adInfo.setPlacement(placement);
                            ((OnAdEventV2) listener).onEvent(event, adInfo, message);
                        }
                    } catch (Exception e) {
                        MRAIDLogger.e("Unhandled exception", e);
                    }
                }
            });
        } catch (Exception e) {
            MRAIDLogger.e("Failed to provide callback", e);
        }
    }

    private static void fireListener(MRAIDContainer container, AdEvent event, String message) {
        if (container != null) {
            try {
                container.fireListener(event, message);
            } catch (Exception e) {
                MRAIDLogger.e(container, "Failed to provide callback", e);
            }
        }
    }

    public static final void reloadInterstitial(String name) {
        reloadAd(name, PlacementType.INTERSTITIAL);
    }

    public static final void reloadBanner(String name) {
        reloadAd(name, PlacementType.INLINE);
    }

    private static void reloadAd(String name, PlacementType type) {
        try {
            if (containerHolder != null) {
                MRAIDContainer container = containerHolder.getContainer(type, name);
                if (container != null) {
                    container.reload();
                }
            }
        } catch (Exception e) {
            MRAIDLogger.e("Exception caught while reloading ad", e);
        }
    }

    public static void release() {
        if (containerHolder != null) {
            containerHolder.release();
            containerHolder = null;
        }
        NativeXVideoPlayer.release();
        handler.removeMessages(MSG_RELEASE_AD);
    }

    private static String getUrl(String sessionId, String name, PlacementType type) {
        String url;
        Log.d("Folder url " + mraidFolder);
        Map<String, String> params = new HashMap();
        params.put("SessionId", sessionId);
        params.put(PARAM_RESPONSE_TYPE, "json");
        List<Long> offerIds = CacheDBManager.getInstance().getOffersCachedList();
        if (offerIds.size() > 0) {
            StringBuilder cachedOffersString = new StringBuilder();
            int offerCount = offerIds.size();
            int loopCount = 1;
            for (Long offerId : offerIds) {
                cachedOffersString.append(Long.toString(offerId.longValue()));
                if (loopCount < offerCount) {
                    cachedOffersString.append(",");
                }
                loopCount++;
            }
            params.put(PARAM_CACHED_OFFERS, cachedOffersString.toString());
        }
        if (mraidFolder == null) {
            if (!Utilities.stringIsEmpty(name)) {
                params.put("placement", name);
            }
            params.put(PARAM_PLACEMENT_TYPE, type.getValue());
            url = Utilities.appendParamsToUrl(ServerConfig.applyConfiguration(URLS.MRAID_ADS, false), params);
        } else if (mraidFolder.startsWith(MRAIDConstants.URL_ASSETS_VIDEO_TEST) || mraidFolder.startsWith(MRAIDConstants.URL_INTERNAL) || mraidFolder.startsWith(MRAIDConstants.URL_INTERNAL_DEV) || mraidFolder.startsWith(MRAIDConstants.URL_ASSETS_CUSTOM_SCHEME_RESPONSE_TEST) || mraidFolder.startsWith(MRAIDConstants.URL_ZLATI_TEST) || mraidFolder.startsWith(MRAIDConstants.URL_ANTON_TEST)) {
            url = mraidFolder;
        } else {
            url = ServerConfig.applyConfiguration(mraidFolder, false);
        }
        Log.d("Loading url " + url);
        return url;
    }

    public static final void releaseInterstitial(String name) {
        releaseAd(name, PlacementType.INTERSTITIAL);
    }

    public static final void releaseBanner(String name) {
        releaseAd(name, PlacementType.INLINE);
    }

    public static final void releaseAd(String name, PlacementType type) {
        try {
            if (containerHolder != null) {
                MRAIDContainer container = containerHolder.getContainer(type, name);
                if (container != null) {
                    releaseAd(container);
                }
            }
        } catch (Exception e) {
            MRAIDLogger.e("Unable to release ad", e);
        }
    }

    public static final void releaseAd(MRAIDContainer mraidContainer) {
        releaseAd(mraidContainer, true);
    }

    public static final void releaseAd(MRAIDContainer mraidContainer, boolean closeAnimation) {
        if (mraidContainer != null) {
            try {
                if (mraidContainer.hasAdConverted()) {
                    ServerRequestManager.getInstance().getDeviceBalance();
                }
                if (!(containerHolder == null || mraidContainer.getMD5ListUsed() == null)) {
                    Set<String> cachedAdsMD5List = new HashSet();
                    for (String key : containerHolder.keySet()) {
                        MapDataHolder mapDataHolder = (MapDataHolder) containerHolder.get(key);
                        MRAIDContainer adContainer = null;
                        if (mapDataHolder.shown != null && mraidContainer != mapDataHolder.shown) {
                            adContainer = mapDataHolder.shown;
                        } else if (!(mapDataHolder.cached == null || mraidContainer == mapDataHolder.cached)) {
                            adContainer = mapDataHolder.cached;
                        }
                        if (!(adContainer == null || adContainer.getMD5ListUsed() == null)) {
                            for (String add : adContainer.getMD5ListUsed()) {
                                cachedAdsMD5List.add(add);
                            }
                        }
                    }
                    CacheManager.getInstance().checkToUpdateStatusToReady(mraidContainer.getMD5ListUsed(), cachedAdsMD5List);
                }
                if (handler.getLooper() != Looper.myLooper()) {
                    handler.obtainMessage(MSG_RELEASE_AD, mraidContainer).sendToTarget();
                    return;
                }
                handler.removeMessages(MSG_RELEASE_AD, mraidContainer);
                handler.removeMessages(MSG_EXPIRE_CONTAINER, mraidContainer);
                if (containerHolder != null) {
                    containerHolder.releaseContainer(mraidContainer, closeAnimation);
                } else {
                    mraidContainer.shouldReleaseWithCloseAnimation(closeAnimation);
                }
            } catch (Exception e) {
                fireListener(mraidContainer, AdEvent.ERROR, "The ad failed to dismiss.");
                MRAIDLogger.e("Unable to release ad", e);
            }
        }
    }

    public static void releaseAdCancel(MRAIDContainer mraidContainer) {
        if (mraidContainer != null) {
            handler.removeMessages(MSG_RELEASE_AD, mraidContainer);
        }
    }

    public static void releaseAdDelayed(MRAIDContainer mraidContainer) {
        if (mraidContainer != null) {
            handler.sendMessageDelayed(handler.obtainMessage(MSG_RELEASE_AD, mraidContainer), MSG_RELEASE_AD_DELAY);
        }
    }

    public static final boolean hasInterstitial(String name) {
        return hasAd(PlacementType.INTERSTITIAL, name, false, false);
    }

    public static final boolean hasInterstitial(String name, boolean shouldFireListener) {
        return hasAd(PlacementType.INTERSTITIAL, name, false, shouldFireListener);
    }

    public static final boolean hasBanner(String name) {
        return hasAd(PlacementType.INLINE, name, false, false);
    }

    public static final boolean hasCachedInterstitial(String name) {
        return hasAd(PlacementType.INTERSTITIAL, name, true, false);
    }

    public static final boolean hasBanner(String name, boolean shouldFireListener) {
        return hasAd(PlacementType.INLINE, name, false, shouldFireListener);
    }

    public static final boolean hasCachedInterstitial(String name, boolean shouldFireListener) {
        return hasAd(PlacementType.INTERSTITIAL, name, true, shouldFireListener);
    }

    public static final boolean hasCachedBanner(String name) {
        return hasAd(PlacementType.INLINE, name, true, false);
    }

    public static final boolean hasCachedBanner(String name, boolean shouldFireListener) {
        return hasAd(PlacementType.INLINE, name, true, shouldFireListener);
    }

    public static boolean hasAd(PlacementType type, String name) {
        if (containerHolder == null) {
            return false;
        }
        MapDataHolder holder = containerHolder.getMapDataHolder(type, name);
        if (holder.cached == null && holder.shown == null) {
            return false;
        }
        return true;
    }

    private static boolean hasAd(PlacementType type, String name, boolean cached, boolean fireListener) {
        if (containerHolder != null) {
            MRAIDContainer container;
            if (cached) {
                container = containerHolder.getCachedContainer(type, name);
            } else {
                container = containerHolder.getContainer(type, name);
            }
            if (container != null) {
                if (fireListener) {
                    if (!container.isAdLoaded()) {
                        fireListener(container, AdEvent.DOWNLOADING, "Ad is being downloaded at the moment");
                    } else if (cached) {
                        fireListener(container, AdEvent.ALREADY_FETCHED, "Ad is already fetched and ready to display");
                    } else {
                        fireListener(container, AdEvent.ALREADY_SHOWN, "Ad is already shown. Dismiss it in order to show another one");
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static boolean isAdReady(PlacementType type, String name) {
        if (containerHolder == null) {
            return false;
        }
        MapDataHolder holder = containerHolder.getMapDataHolder(type, name);
        if (holder.cached == null || !holder.cached.isAdLoaded()) {
            return false;
        }
        return true;
    }

    public static void trackVideoProgress(String containerName, String url, VideoProgress progress) {
        try {
            if (containerHolder != null) {
                MRAIDContainer container = containerHolder.getContainer(containerName);
                if (container != null) {
                    container.trackVideo(url, progress);
                    if (progress == VideoProgress.VIDEO_PROGRESS_75_PERCENT) {
                        fireListener(container, AdEvent.VIDEO_75_PERCENT_COMPLETED, "Video ad has reached 75% completion");
                    }
                }
            }
        } catch (Exception e) {
            MRAIDLogger.e("Unable to track video progress", e);
        }
    }

    public static NativeVideoPlayerListener getNativeVideoPlayerListener() {
        return sNativexVideoPlayerListener;
    }

    public static void stopAlphaAnimationAndDismissAd(String containerName) {
        if (containerHolder != null) {
            try {
                MRAIDContainer container = containerHolder.getContainer(containerName);
                if (container != null) {
                    container.clearAllAlphaAnimations();
                    container.makeContainerInvisible();
                    fireListener(container, AdEvent.ERROR, "Error while creating/updating the ad");
                    releaseAd(container, false);
                }
            } catch (Exception e) {
                MRAIDLogger.e("Unable to stop alpha animation", e);
            }
        }
    }

    public static void videoCancelled(String containerName) {
        if (containerHolder != null) {
            try {
                MRAIDContainer container = containerHolder.getContainer(containerName);
                if (container != null) {
                    container.makeContainerVisible();
                    container.fireVideoCancelledEvent();
                }
            } catch (Exception e) {
                MRAIDLogger.e("Unable to send video cancelled event.", e);
            }
        }
    }

    public static void videoCompleted(String containerName) {
        if (containerHolder != null) {
            try {
                MRAIDContainer container = containerHolder.getContainer(containerName);
                if (container != null) {
                    container.makeContainerVisible();
                }
                fireListener(container, AdEvent.VIDEO_COMPLETED, "Video ad has completed");
            } catch (Exception e) {
                MRAIDLogger.e("Unable to send video completed event.", e);
            }
        }
    }

    public static void update(Activity activity) {
        if (activity != null) {
            try {
                if (containerHolder != null) {
                    for (MapDataHolder holder : containerHolder.values()) {
                        if (!(holder.shown == null || holder.shown.getActivity() == activity)) {
                            String activityName = holder.shown.getActivityClassName();
                            if (!Utilities.stringIsEmpty(activityName) && activity.getComponentName().getClassName().equals(activityName)) {
                                holder.shown.attachToActivity(activity);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                MRAIDLogger.e("Failed to update ads", e);
            }
        }
    }

    public static boolean isBannerLoaded(String name) {
        return isAdLoaded(PlacementType.INLINE, name);
    }

    public static boolean isInterstitialLoaded(String name) {
        return isAdLoaded(PlacementType.INTERSTITIAL, name);
    }

    private static boolean isAdLoaded(PlacementType type, String name) {
        if (containerHolder != null) {
            MRAIDContainer container = containerHolder.getContainer(MRAIDContainer.getContainerName(type, name));
            if (container != null) {
                return container.isAdLoaded();
            }
            container = containerHolder.getCachedContainer(type, name);
            if (container != null) {
                return container.isAdLoaded();
            }
        }
        return false;
    }

    public static final void setRunningOnUnity(boolean isUnity) {
        runningOnUnity = isUnity;
    }

    static final boolean isRunningOnUnity() {
        return runningOnUnity;
    }
}
