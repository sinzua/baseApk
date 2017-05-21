package com.supersonic.mediationsdk.utils;

import android.text.TextUtils;
import com.supersonic.mediationsdk.logger.ConsoleLogger;
import com.supersonic.mediationsdk.logger.PublisherLogger;
import com.supersonic.mediationsdk.logger.ServerLogger;
import com.supersonic.mediationsdk.logger.SupersonicError;
import com.supersonic.mediationsdk.logger.SupersonicLogger.SupersonicTag;
import com.supersonic.mediationsdk.logger.SupersonicLoggerManager;
import com.supersonic.mediationsdk.model.Placement;
import com.supersonic.mediationsdk.model.PlacementsHolder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerResponseWrapper {
    public static final String APP_KEY_FIELD = "appKey";
    public static final String SETTINGS_FIELD = "settings";
    public static final String USER_ID_FIELD = "userId";
    private final String ADAPTERS_LOAD_TIMEOUT = "adapterTimeOutInSeconds";
    private final String ADAPTER_ALGORITHM = "adapterAlgorithm";
    private final String CONFIG_KEY = "config";
    private final int DEFAULT_ADAPTERS_SMARTLOAD_AMOUNT = 2;
    private final int DEFAULT_ADAPTERS_SMARTLOAD_TIMEOUT = 60;
    private final String DEFAULT_ADAPTER_ALGORITHM = "KTO";
    private final String ERROR_KEY = "error";
    private final String NUM_OF_ADAPTERS_KEY = "maxNumOfAdaptersToLoadOnStart";
    private final String PROVIDERS_KEY = "providers";
    private final String PROVIDER_NAME_KEY = "provider";
    private String mAppKey;
    private Map<String, JSONObject> mConfigurationMap;
    private int mLoadPosition = -1;
    private int mMaxRewardedVideoAdapters;
    private PlacementsHolder mPlacementHolder;
    private ArrayList<JSONObject> mProviders;
    private JSONObject mSettings;
    private String mUserId;

    private ServerResponseWrapper() {
    }

    public ServerResponseWrapper(String appKey, String userId, String jsonData) {
        try {
            if (TextUtils.isEmpty(jsonData)) {
                this.mSettings = new JSONObject();
            } else {
                this.mSettings = new JSONObject(jsonData);
            }
            this.mProviders = new ArrayList();
            this.mConfigurationMap = new HashMap();
            initializeProvidersAndConfigurationsMap(this.mSettings, this.mProviders, this.mConfigurationMap);
            this.mMaxRewardedVideoAdapters = this.mProviders.size();
            if (TextUtils.isEmpty(appKey)) {
                appKey = "";
            }
            this.mAppKey = appKey;
            if (TextUtils.isEmpty(userId)) {
                userId = "";
            }
            this.mUserId = userId;
            this.mPlacementHolder = parsePlacementsSection(this.mSettings);
        } catch (JSONException e) {
            defaultInit();
        }
    }

    private void initializeProvidersAndConfigurationsMap(JSONObject settings, ArrayList<JSONObject> providers, Map<String, JSONObject> configMap) {
        JSONArray arr = settings.optJSONArray("providers");
        if (arr != null) {
            for (int i = 0; i < arr.length(); i++) {
                JSONObject currentAdapter = arr.optJSONObject(i);
                if (currentAdapter != null) {
                    providers.add(currentAdapter);
                    JSONObject currentAdapterConfig = currentAdapter.optJSONObject("config");
                    String currentAdapterName = currentAdapter.optString("provider");
                    if (!(TextUtils.isEmpty(currentAdapterName) || currentAdapterConfig == null)) {
                        configMap.put(currentAdapterName, currentAdapterConfig);
                    }
                }
            }
        }
    }

    public ServerResponseWrapper(ServerResponseWrapper srw) {
        try {
            this.mSettings = new JSONObject(srw.mSettings.toString());
            this.mProviders = new ArrayList();
            this.mProviders.addAll(srw.mProviders);
            this.mAppKey = srw.mAppKey;
            this.mUserId = srw.mUserId;
            this.mMaxRewardedVideoAdapters = srw.getMaxRewardedVideoAdapters();
            this.mPlacementHolder = srw.getPlacementHolder();
        } catch (Exception e) {
            defaultInit();
            e.printStackTrace();
        }
    }

    private void defaultInit() {
        this.mSettings = new JSONObject();
        this.mProviders = new ArrayList();
        this.mAppKey = "";
        this.mUserId = "";
        this.mMaxRewardedVideoAdapters = 0;
    }

    public String toString() {
        JSONObject resultObject = new JSONObject();
        try {
            resultObject.put(APP_KEY_FIELD, this.mAppKey);
            resultObject.put("userId", this.mUserId);
            resultObject.put("settings", this.mSettings);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultObject.toString();
    }

    public boolean isValidResponse() {
        boolean valid = true && this.mSettings != null;
        if (!valid || this.mSettings.has("error")) {
            valid = false;
        } else {
            valid = true;
        }
        if (!valid || getRVProvidersArray() == null) {
            valid = false;
        } else {
            valid = true;
        }
        if (!valid || getRVProvidersArray().length() == 0) {
            valid = false;
        } else {
            valid = true;
        }
        if (!valid || this.mPlacementHolder.getDefaultRewardedVideoPlacement() == null) {
            return false;
        }
        return true;
    }

    public SupersonicError getReponseError() {
        if (isValidResponse()) {
            return null;
        }
        String errorMsg = "";
        if (this.mSettings != null && this.mSettings.has("error")) {
            errorMsg = this.mSettings.optString("error");
        }
        if (errorMsg == null) {
            errorMsg = "";
        }
        return ErrorBuilder.buildAdapterInitFailedError(errorMsg);
    }

    private int getLoggerLogLevel(String loggerName) {
        int result = -1;
        if (loggerName != null) {
            JSONObject loggersSettings = getLoggersSettings();
            try {
                if (loggersSettings.has(loggerName)) {
                    result = loggersSettings.getInt(loggerName);
                }
            } catch (Exception e) {
            }
        }
        return result;
    }

    public int getServerLoggerLevel() {
        return getLoggerLogLevel(ServerLogger.NAME);
    }

    public int getPublisherLoggerLevel() {
        return getLoggerLogLevel(PublisherLogger.NAME);
    }

    public int getConsoleLoggerLevel() {
        return getLoggerLogLevel(ConsoleLogger.NAME);
    }

    public Map<String, JSONObject> getConfigurationMap() {
        return this.mConfigurationMap;
    }

    public JSONArray getRVProvidersArray() {
        JSONArray rvProviders = null;
        if (this.mSettings != null) {
            rvProviders = this.mSettings.optJSONArray("providers");
        }
        if (rvProviders == null) {
            return new JSONArray();
        }
        return rvProviders;
    }

    public int getMaxRewardedVideoAdapters() {
        return this.mMaxRewardedVideoAdapters;
    }

    public int decreaseMaxRewardedVideoAdapters() {
        if (this.mMaxRewardedVideoAdapters > 0) {
            this.mMaxRewardedVideoAdapters--;
        }
        return this.mMaxRewardedVideoAdapters;
    }

    public JSONArray getISProvidersArray() {
        return getJSONArrayWithSupersonicAdapter();
    }

    public JSONArray getOWProvidersArray() {
        return getJSONArrayWithSupersonicAdapter();
    }

    private JSONArray getJSONArrayWithSupersonicAdapter() {
        JSONArray result = new JSONArray();
        JSONArray providers = getRVProvidersArray();
        if (providers != null) {
            for (int i = 0; i < providers.length(); i++) {
                try {
                    JSONObject p = (JSONObject) providers.get(i);
                    if (p != null) {
                        if ("SupersonicAds".equals(p.optString("provider"))) {
                            result.put(p);
                            break;
                        }
                    } else {
                        continue;
                    }
                } catch (Exception e) {
                }
            }
        }
        return result;
    }

    private JSONObject getSection(JSONObject json, String sectionName) {
        JSONObject result = null;
        if (json != null) {
            result = json.optJSONObject(sectionName);
        }
        if (result == null) {
            return new JSONObject();
        }
        return result;
    }

    private JSONObject getSettingsSection() {
        return getSection(this.mSettings, "settings");
    }

    public String getAdapterAlgorithm() {
        String result = getSettingsSection().optString("adapterAlgorithm");
        if (TextUtils.isEmpty(result)) {
            return "KTO";
        }
        return result;
    }

    private JSONObject getLoggersSettings() {
        JSONObject loggers = getSettingsSection().optJSONObject("loggers");
        if (loggers == null) {
            return new JSONObject();
        }
        return loggers;
    }

    public int getAdaptersSmartLoadAmount() {
        int amount = getSmartLoadAmountFromResponse();
        if (amount == 0) {
            return 2;
        }
        return amount;
    }

    private int getSmartLoadAmountFromResponse() {
        return getSettingsSection().optInt("maxNumOfAdaptersToLoadOnStart");
    }

    public int getAdaptersSmartLoadTimeout() {
        int timeout = getSmartLoadTimeoutFromResponse();
        if (timeout == 0) {
            return 60;
        }
        return timeout;
    }

    private int getSmartLoadTimeoutFromResponse() {
        return getSettingsSection().optInt("adapterTimeOutInSeconds");
    }

    private JSONObject getProviderSettings(String providerName) {
        JSONArray providers = getRVProvidersArray();
        for (int i = 0; i < providers.length(); i++) {
            JSONObject providerSettigns = providers.optJSONObject(i);
            if (providerSettigns != null && providerSettigns.optString("provider").equals(providerName)) {
                return providerSettigns;
            }
        }
        return new JSONObject();
    }

    public JSONObject getNextProvider() {
        try {
            this.mLoadPosition++;
            if (this.mProviders.size() > this.mLoadPosition) {
                return (JSONObject) this.mProviders.get(this.mLoadPosition);
            }
            return null;
        } catch (Exception e) {
            SupersonicLoggerManager.getLogger().logException(SupersonicTag.INTERNAL, "getNextProvider(mLoadPosition: " + this.mLoadPosition + " mProviders.size(): " + this.mProviders.size() + ")", e);
            return null;
        }
    }

    public boolean hasMoreProvidersToLoad() {
        return this.mLoadPosition < this.mProviders.size() && this.mProviders.size() > 0;
    }

    public int getAdaptersLoadPosition() {
        return this.mLoadPosition;
    }

    private JSONObject getEventsSettingsSection() {
        return getSection(getSettingsSection(), EventEntry.TABLE_NAME);
    }

    private int getIntFromEventsSectionOrNegative(String key) {
        JSONObject eventsSection = getEventsSettingsSection();
        if (eventsSection == null || !eventsSection.has(key)) {
            return -1;
        }
        try {
            return eventsSection.getInt(key);
        } catch (JSONException e) {
            return -1;
        }
    }

    public String getEventsUrl() {
        return getEventsSettingsSection().optString("serverEventsURL");
    }

    public int getMaxNumberOfEvents() {
        return getIntFromEventsSectionOrNegative("maxNumberOfEvents");
    }

    public int getEventsBackupThreshold() {
        return getIntFromEventsSectionOrNegative("backupThreshold");
    }

    public boolean isEventsEnabled() {
        String eventsToggleKey = "sendEventsToggle";
        try {
            JSONObject eventsSettings = getEventsSettingsSection();
            if (eventsSettings == null || !eventsSettings.has(eventsToggleKey)) {
                return true;
            }
            return eventsSettings.getBoolean(eventsToggleKey);
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isUltraEventsEnabled() {
        String ultraEventsToggleKey = "sendUltraEvents";
        JSONObject eventsSettings = getEventsSettingsSection();
        if (eventsSettings.has(ultraEventsToggleKey)) {
            return eventsSettings.optBoolean(ultraEventsToggleKey);
        }
        return false;
    }

    private PlacementsHolder parsePlacementsSection(JSONObject response) {
        PlacementsHolder holder = new PlacementsHolder();
        try {
            JSONArray rvPlacements = getSection(getSettingsSection(), "placements").optJSONArray("rewardedVideo");
            if (rvPlacements != null) {
                for (int i = 0; i < rvPlacements.length(); i++) {
                    Placement placement = parseSinglePlacement(rvPlacements.optJSONObject(i));
                    if (placement != null) {
                        holder.addRewardedVideoPlacement(placement);
                    }
                }
            }
        } catch (Exception e) {
        }
        return holder;
    }

    private Placement parseSinglePlacement(JSONObject placementJson) {
        if (placementJson == null) {
            return null;
        }
        try {
            int id = placementJson.optInt("placementId", -1);
            String placementName = placementJson.optString("placementName");
            String rewardName = placementJson.optString("virtualItemName");
            int rewardAmount = placementJson.optInt("virtualItemCount", -1);
            if (id < 0 || TextUtils.isEmpty(placementName) || TextUtils.isEmpty(rewardName) || rewardAmount <= 0) {
                return null;
            }
            return new Placement(id, placementName, rewardName, rewardAmount);
        } catch (Exception e) {
            return null;
        }
    }

    public PlacementsHolder getPlacementHolder() {
        return this.mPlacementHolder;
    }
}
