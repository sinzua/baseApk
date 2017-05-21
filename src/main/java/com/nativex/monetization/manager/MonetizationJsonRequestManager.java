package com.nativex.monetization.manager;

import android.content.res.Resources;
import com.google.gson.Gson;
import com.nativex.common.Device;
import com.nativex.common.DeviceManager;
import com.nativex.common.JsonRequestManager;
import com.nativex.common.Log;
import com.nativex.common.NetworkConnectionManager;
import com.nativex.common.Version;
import com.nativex.monetization.business.ActionTakenRequestData;
import com.nativex.monetization.business.CreateSessionRequestData;
import com.nativex.monetization.business.GetDeviceBalanceRequestData;
import com.nativex.monetization.business.RedeemDeviceBalanceRequestData;
import com.nativex.monetization.business.Session;
import java.util.Locale;

public class MonetizationJsonRequestManager extends JsonRequestManager {
    public MonetizationJsonRequestManager() {
        super(DeviceManager.getDeviceInstance(MonetizationSharedDataManager.getContext()));
    }

    public String getCreateSessionRequest() {
        return getCreateSessionRequest(MonetizationSharedDataManager.getAppId(), MonetizationSharedDataManager.getPublisherUserId());
    }

    String getCreateSessionRequest(String appId, String pubUserId) {
        try {
            NetworkConnectionManager ncm = NetworkConnectionManager.getInstance(MonetizationSharedDataManager.getContext());
            Device device = getDevice();
            Object createSessionRequest = new CreateSessionRequestData();
            createSessionRequest.setUdids(generateUdidJsonCollection());
            createSessionRequest.setDeviceGenerationInfo(device.getDeviceName());
            createSessionRequest.setOsVersion(device.getOsVersion());
            createSessionRequest.setOnCellular(Boolean.valueOf(ncm.isOnWiFi()));
            createSessionRequest.setHacked(Boolean.valueOf(device.isHacked()));
            createSessionRequest.setUsingSDK(Boolean.valueOf(device.isUsingSdk()));
            createSessionRequest.setSDKVersion(Version.MONETIZATION);
            createSessionRequest.setAppId(appId);
            createSessionRequest.setPublisherUserId(pubUserId);
            createSessionRequest.setOfferCacheAvailable(Boolean.valueOf(CacheManager.getInstance().isOfferCacheAvailable()));
            createSessionRequest.setAdvertiserTrackingEnabled(Boolean.valueOf(!MonetizationSharedDataManager.isLimitAdTracking()));
            createSessionRequest.setBuildType();
            createSessionRequest.setWebViewUserAgent(MonetizationSharedDataManager.getWebViewUserAgent());
            String deviceLanguageCode = Resources.getSystem().getConfiguration().locale.getLanguage();
            if (!deviceLanguageCode.isEmpty()) {
                createSessionRequest.setDeviceLanguageCode(deviceLanguageCode);
            }
            String appLanguageCode = Locale.getDefault().getLanguage();
            if (!(appLanguageCode.isEmpty() || appLanguageCode.equalsIgnoreCase(deviceLanguageCode))) {
                createSessionRequest.setAppLanguageCode(appLanguageCode);
            }
            if (SessionManager.hasPreviousSessionId()) {
                createSessionRequest.setPreviousSessionId(SessionManager.getPreviousSessionId());
            }
            return new Gson().toJson(createSessionRequest);
        } catch (Exception e) {
            Log.d("MonetizationJsonRequestManager: Unexpected exception caught in getCreateSessionRequest()");
            e.printStackTrace();
            return null;
        }
    }

    public String getAvailableDeviceBalanceRequest() {
        return getAvailableDeviceBalanceRequest(SessionManager.getSessionId());
    }

    String getAvailableDeviceBalanceRequest(String sessionId) {
        try {
            Object request = new GetDeviceBalanceRequestData();
            request.setSession(getSession(sessionId));
            return new Gson().toJson(request);
        } catch (Exception e) {
            Log.d("MonetizationJsonRequestManager: Unexpected exception caught in getAvailableDeviceBalanceRequest()");
            e.printStackTrace();
            return null;
        }
    }

    private Session getSession(String sessionId) {
        Session session = new Session();
        session.setSessionId(sessionId);
        return session;
    }

    public String getRedeemCurrencyRequest(String[] payoutIds, String sessionId) {
        try {
            Object request = new RedeemDeviceBalanceRequestData();
            request.setSession(getSession(sessionId));
            request.setPayoutIds(payoutIds);
            return new Gson().toJson(request);
        } catch (Exception e) {
            Log.d("MonetizationJsonRequestManager: Unexpected exception caught in getRedeemCurrencyRequest()");
            e.printStackTrace();
            return null;
        }
    }

    public String getActionTakenRequest(int appId) {
        try {
            Device device = DeviceManager.getDeviceInstance(MonetizationSharedDataManager.getContext());
            Object request = new ActionTakenRequestData();
            request.setUdids(generateUdidJsonCollection());
            request.setActionId(appId);
            request.setIsHacked(device.isHackedAsString());
            return new Gson().toJson(request);
        } catch (Exception e) {
            Log.d("AdvertiserJsonRequestManager: Unexpected exception caught in getActionTakenBody().");
            e.printStackTrace();
            return null;
        }
    }
}
