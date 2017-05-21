package com.supersonic.adapters.supersonicads;

import android.text.TextUtils;
import com.nativex.common.JsonRequestConstants.UDIDs;
import com.parse.ParseException;
import com.supersonic.mediationsdk.config.AbstractAdapterConfig;
import com.supersonic.mediationsdk.config.ConfigValidationResult;
import com.supersonic.mediationsdk.logger.SupersonicLogger.SupersonicTag;
import com.supersonic.mediationsdk.logger.SupersonicLoggerManager;
import com.supersonic.mediationsdk.utils.ErrorBuilder;
import com.supersonic.mediationsdk.utils.SupersonicConstants.Gender;
import com.supersonic.mediationsdk.utils.SupersonicUtils;
import com.supersonicads.sdk.utils.Constants.ControllerParameters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

public class SupersonicConfig extends AbstractAdapterConfig {
    public static final String AGE = "age";
    public static final String APPLICATION_KEY = "applicationKey";
    public static final String APPLICATION_PRIVATE_KEY = "privateKey";
    public static final String APPLICATION_USER_AGE_GROUP = "applicationUserAgeGroup";
    public static final String APPLICATION_USER_GENDER = "applicationUserGender";
    public static final String CAMPAIGN_ID = "campaignId";
    public static final String CLIENT_SIDE_CALLBACKS = "useClientSideCallbacks";
    private static final String DYNAMIC_CONTROLLER_CONFIG = "controllerConfig";
    private static final String DYNAMIC_CONTROLLER_DEBUG_MODE = "debugMode";
    private static final String DYNAMIC_CONTROLLER_URL = "controllerUrl";
    public static final String GENDER = "gender";
    public static final String ITEM_COUNT = "itemCount";
    public static final String ITEM_NAME = "itemName";
    public static final String LANGUAGE = "language";
    public static final String MAX_VIDEO_LENGTH = "maxVideoLength";
    public static final String PROVIDER_NAME = "SupersonicAds";
    public static final String USER_ID = "userId";
    private static SupersonicConfig mInstance;
    private final String CUSTOM_PARAM_PREFIX = "custom_";
    private final String SDK_PLUGIN_TYPE = "SDKPluginType";
    private final String TAG = SupersonicConfig.class.getSimpleName();
    private Map<String, String> mOfferwallCustomParams;
    private Map<String, String> mRewardedVideoCustomParams;

    private SupersonicConfig() {
    }

    public static synchronized SupersonicConfig getConfigObj() {
        SupersonicConfig supersonicConfig;
        synchronized (SupersonicConfig.class) {
            if (mInstance == null) {
                mInstance = new SupersonicConfig();
            }
            supersonicConfig = mInstance;
        }
        return supersonicConfig;
    }

    String getUserAgeGroup() {
        return SupersonicUtils.get(this.mJsonSettings, APPLICATION_USER_AGE_GROUP);
    }

    public void setCustomControllerUrl(String url) {
        SupersonicUtils.put(this.mJsonSettings, DYNAMIC_CONTROLLER_URL, url);
    }

    String getDynamicControllerUrl() {
        return SupersonicUtils.get(this.mJsonSettings, DYNAMIC_CONTROLLER_URL);
    }

    public void setDebugMode(int debugMode) {
        SupersonicUtils.put(this.mJsonSettings, DYNAMIC_CONTROLLER_DEBUG_MODE, debugMode);
    }

    public int getDebugMode() {
        if (this.mJsonSettings == null || !this.mJsonSettings.has(DYNAMIC_CONTROLLER_DEBUG_MODE)) {
            return 0;
        }
        return this.mJsonSettings.optInt(DYNAMIC_CONTROLLER_DEBUG_MODE, 0);
    }

    public String getControllerConfig() {
        try {
            if (this.mJsonSettings != null && this.mJsonSettings.has("controllerConfig")) {
                return this.mJsonSettings.getJSONObject("controllerConfig").toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean getClientSideCallbacks() {
        boolean csc = false;
        try {
            if (this.mJsonSettings != null && this.mJsonSettings.has("useClientSideCallbacks")) {
                csc = this.mJsonSettings.optBoolean("useClientSideCallbacks", false);
            }
        } catch (Exception e) {
        }
        return csc;
    }

    public String getMaxVideoLength() {
        return SupersonicUtils.get(this.mJsonSettings, MAX_VIDEO_LENGTH);
    }

    public String getLanguage() {
        return SupersonicUtils.get(this.mJsonSettings, "language");
    }

    int getMaxVideos() {
        return getMaxVideosToPresent();
    }

    int getMaxVideosPerIteration() {
        return getMaxVideosPerIterationToPresent();
    }

    String getUserGender() {
        return SupersonicUtils.get(this.mJsonSettings, APPLICATION_USER_GENDER);
    }

    String getPluginType() {
        String pluginType = SupersonicUtils.get(this.mJsonSettings, "SDKPluginType");
        if (pluginType == null) {
            return "";
        }
        return pluginType;
    }

    public String getPrivateKey() {
        return SupersonicUtils.get(this.mJsonSettings, APPLICATION_PRIVATE_KEY);
    }

    public String getItemName() {
        return SupersonicUtils.get(this.mJsonSettings, ITEM_NAME);
    }

    public int getItemCount() {
        int itemCount = -1;
        try {
            String itemCountString = SupersonicUtils.get(this.mJsonSettings, ITEM_COUNT);
            if (!SupersonicUtils.isEmpty(itemCountString)) {
                itemCount = Integer.valueOf(itemCountString).intValue();
            }
        } catch (NumberFormatException e) {
            SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, this.TAG + ":getItemCount()", e);
        }
        return itemCount;
    }

    String getCampaignId() {
        return SupersonicUtils.get(this.mJsonSettings, CAMPAIGN_ID);
    }

    public void setCampaignId(String key, String id) {
        if (key.equals(CAMPAIGN_ID)) {
            SupersonicUtils.put(this.mJsonSettings, key, id);
        }
    }

    public Map<String, String> getOfferwallCustomParams() {
        return this.mOfferwallCustomParams;
    }

    public Map<String, String> getRewardedVideoCustomParams() {
        return this.mRewardedVideoCustomParams;
    }

    private Map<String, String> convertCustomParams(Map<String, String> customParams) {
        Map<String, String> result = new HashMap();
        if (customParams != null) {
            try {
                Set<String> keys = customParams.keySet();
                if (keys != null) {
                    for (String k : keys) {
                        if (!TextUtils.isEmpty(k)) {
                            String value = (String) customParams.get(k);
                            if (!TextUtils.isEmpty(value)) {
                                result.put("custom_" + k, value);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, this.TAG + ":convertCustomParams()", e);
            }
        }
        return result;
    }

    void setUserAgeGroup(int age) {
        String ageGroup = "0";
        if (age >= 13 && age <= 17) {
            ageGroup = "1";
        } else if (age >= 18 && age <= 20) {
            ageGroup = "2";
        } else if (age >= 21 && age <= 24) {
            ageGroup = UDIDs.ANDROID_DEVICE_ID;
        } else if (age >= 25 && age <= 34) {
            ageGroup = UDIDs.ANDROID_ID;
        } else if (age >= 35 && age <= 44) {
            ageGroup = "5";
        } else if (age >= 45 && age <= 54) {
            ageGroup = "6";
        } else if (age >= 55 && age <= 64) {
            ageGroup = "7";
        } else if (age > 65 && age <= ParseException.CACHE_MISS) {
            ageGroup = "8";
        }
        SupersonicUtils.put(this.mJsonSettings, APPLICATION_USER_AGE_GROUP, ageGroup);
    }

    public void setMaxVideoLength(int length) {
        SupersonicUtils.put(this.mJsonSettings, MAX_VIDEO_LENGTH, String.valueOf(length));
    }

    public void setLanguage(String language) {
        SupersonicUtils.put(this.mJsonSettings, "language", String.valueOf(language));
    }

    void setUserGender(String gender) {
        SupersonicUtils.put(this.mJsonSettings, APPLICATION_USER_GENDER, gender);
    }

    public void setClientSideCallbacks(boolean status) {
        SupersonicUtils.put(this.mJsonSettings, "useClientSideCallbacks", String.valueOf(status));
    }

    public void setPrivateKey(String key) {
        SupersonicUtils.put(this.mJsonSettings, APPLICATION_PRIVATE_KEY, key);
    }

    public void setItemName(String name) {
        SupersonicUtils.put(this.mJsonSettings, ITEM_NAME, name);
    }

    public void setItemCount(int count) {
        SupersonicUtils.put(this.mJsonSettings, ITEM_COUNT, String.valueOf(count));
    }

    public void setPluginType(String pluginType) {
        if (pluginType == null) {
            return;
        }
        if ("Unity".equals(pluginType) || "AdobeAir".equals(pluginType) || "Xamarin".equals(pluginType)) {
            SupersonicUtils.put(this.mJsonSettings, "SDKPluginType", pluginType);
        }
    }

    public void setRewardedVideoCustomParams(Map<String, String> rvCustomParams) {
        this.mRewardedVideoCustomParams = convertCustomParams(rvCustomParams);
    }

    public void setOfferwallCustomParams(Map<String, String> owCustomParams) {
        this.mOfferwallCustomParams = convertCustomParams(owCustomParams);
    }

    protected String getProviderName() {
        return "SupersonicAds";
    }

    protected ArrayList<String> initializeMandatoryFields() {
        ArrayList<String> result = new ArrayList();
        result.add(DYNAMIC_CONTROLLER_URL);
        return result;
    }

    protected ArrayList<String> initializeOptionalFields() {
        ArrayList<String> result = new ArrayList();
        result.add("useClientSideCallbacks");
        result.add(APPLICATION_USER_GENDER);
        result.add(APPLICATION_USER_AGE_GROUP);
        result.add("language");
        result.add("maxVideosPerSession");
        result.add("maxVideosPerIteration");
        result.add(APPLICATION_PRIVATE_KEY);
        result.add(MAX_VIDEO_LENGTH);
        result.add(ITEM_NAME);
        result.add(ITEM_COUNT);
        result.add("SDKPluginType");
        result.add("controllerConfig");
        result.add(DYNAMIC_CONTROLLER_DEBUG_MODE);
        return result;
    }

    protected void validateOptionalField(JSONObject config, String key, ConfigValidationResult result) {
        try {
            if ("maxVideosPerSession".equals(key)) {
                validateMaxVideos(config.getInt(key), result);
            } else if (!"maxVideosPerIteration".equals(key) && !DYNAMIC_CONTROLLER_DEBUG_MODE.equals(key) && !"controllerConfig".equals(key)) {
                String value = (String) config.get(key);
                if ("useClientSideCallbacks".equals(key)) {
                    validateClientSideCallbacks(value, result);
                } else if (APPLICATION_USER_GENDER.equals(key)) {
                    validateGender(value, result);
                } else if (APPLICATION_USER_AGE_GROUP.equals(key)) {
                    validateAgeGroup(value, result);
                } else if ("language".equals(key)) {
                    validateLanguage(value, result);
                } else if (MAX_VIDEO_LENGTH.equals(key)) {
                    validateMaxVideoLength(value, result);
                } else if (APPLICATION_PRIVATE_KEY.equals(key)) {
                    validatePrivateKey(value, result);
                } else if (ITEM_NAME.equals(key)) {
                    validateItemName(value, result);
                } else if (ITEM_COUNT.equals(key)) {
                    validateItemCount(value, result);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError(key, getProviderName(), null));
        } catch (Throwable th) {
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError(key, getProviderName(), null));
        }
    }

    private void validateItemCount(String value, ConfigValidationResult result) {
        try {
            int itemCount = Integer.parseInt(value.trim());
            if (itemCount < 1 || itemCount > 100000) {
                result.setInvalid(ErrorBuilder.buildInvalidKeyValueError(ITEM_COUNT, getProviderName(), "itemCount value should be between 1-100000"));
            }
        } catch (NumberFormatException e) {
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError(ITEM_COUNT, getProviderName(), "itemCount value should be between 1-100000"));
        }
    }

    private void validateItemName(String value, ConfigValidationResult result) {
        if (value == null) {
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError(ITEM_NAME, getProviderName(), "itemNamelength should be between 1-50 characters"));
        } else if (value.length() < 1 || value.length() > 50) {
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError(ITEM_NAME, getProviderName(), "itemNamelength should be between 1-50 characters"));
        }
    }

    private void validatePrivateKey(String value, ConfigValidationResult result) {
        if (value == null) {
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError(APPLICATION_PRIVATE_KEY, getProviderName(), "privateKey length should be between 5-30 characters"));
        } else if (value.length() < 5 || value.length() > 30) {
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError(APPLICATION_PRIVATE_KEY, getProviderName(), "privateKey length should be between 5-30 characters"));
        } else if (!value.matches("^[a-zA-Z0-9]*$")) {
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError(APPLICATION_PRIVATE_KEY, getProviderName(), "privateKey should contains only characters and numbers"));
        }
    }

    private void validateMaxVideoLength(String value, ConfigValidationResult result) {
        try {
            int age = Integer.parseInt(value.trim());
            if (age < 1 || age > ControllerParameters.SECOND) {
                result.setInvalid(ErrorBuilder.buildInvalidKeyValueError(MAX_VIDEO_LENGTH, getProviderName(), "maxVideoLength value should be between 1-1000"));
            }
        } catch (NumberFormatException e) {
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError(MAX_VIDEO_LENGTH, getProviderName(), "maxVideoLength value should be between 1-1000"));
        }
    }

    private void validateLanguage(String value, ConfigValidationResult result) {
        if (value != null) {
            value = value.trim();
            if (!value.matches("^[a-zA-Z]*$") || value.length() != 2) {
                result.setInvalid(ErrorBuilder.buildInvalidKeyValueError("language", getProviderName(), "language value should be two letters format."));
                return;
            }
            return;
        }
        result.setInvalid(ErrorBuilder.buildInvalidKeyValueError("language", getProviderName(), "language value should be two letters format."));
    }

    private void validateAgeGroup(String value, ConfigValidationResult result) {
        try {
            int age = Integer.parseInt(value.trim());
            if (age < 0 || age > 8) {
                result.setInvalid(ErrorBuilder.buildInvalidKeyValueError(APPLICATION_USER_AGE_GROUP, getProviderName(), "applicationUserAgeGroup value should be between 0-8"));
            }
        } catch (NumberFormatException e) {
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError(APPLICATION_USER_AGE_GROUP, getProviderName(), "applicationUserAgeGroup value should be between 0-8"));
        }
    }

    private void validateGender(String gender, ConfigValidationResult result) {
        if (gender != null) {
            try {
                gender = gender.toLowerCase().trim();
                if (!Gender.MALE.equals(gender) && !Gender.FEMALE.equals(gender) && !"unknown".equals(gender)) {
                    result.setInvalid(ErrorBuilder.buildInvalidKeyValueError(GENDER, getProviderName(), "gender value should be one of male/female/unknown."));
                }
            } catch (Exception e) {
                result.setInvalid(ErrorBuilder.buildInvalidKeyValueError(GENDER, getProviderName(), "gender value should be one of male/female/unknown."));
            }
        }
    }

    private void validateClientSideCallbacks(String value, ConfigValidationResult result) {
        validateBoolean("useClientSideCallbacks", value, result);
    }

    protected void validateMandatoryField(JSONObject config, String key, ConfigValidationResult result) {
        try {
            String value = (String) config.get(key);
            if ("applicationKey".equals(key)) {
                validateApplicationKey(value, result);
            } else if ("userId".equals(key)) {
                validateUserId(value, result);
            } else if (DYNAMIC_CONTROLLER_URL.equals(key)) {
                validateDynamicUrl(value, result);
            }
        } catch (JSONException e) {
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError(key, getProviderName(), null));
            e.printStackTrace();
        } catch (Throwable th) {
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError(key, getProviderName(), null));
        }
    }

    protected void adapterPostValidation(JSONObject config, ConfigValidationResult result) {
        try {
            validatePrivateKeyItemNameCountCombination(config, result);
        } catch (Exception e) {
            result.setInvalid(ErrorBuilder.buildGenericError(""));
        }
    }

    private void validatePrivateKeyItemNameCountCombination(JSONObject config, ConfigValidationResult result) {
        if (!config.has(APPLICATION_PRIVATE_KEY) && !config.has(ITEM_NAME) && !config.has(ITEM_COUNT)) {
            return;
        }
        if (!config.has(APPLICATION_PRIVATE_KEY) || !config.has(ITEM_NAME) || !config.has(ITEM_COUNT)) {
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError("itemName, itemCount or privateKey", getProviderName(), "configure itemName/itemCount requires the following configurations: itemName, itemCount and privateKey"));
        }
    }

    private void validateUserId(String value, ConfigValidationResult result) {
        if (value == null) {
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError("userId", getProviderName(), "userId is missing"));
        } else if (value.length() < 1 || value.length() > 64) {
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError("userId", getProviderName(), "userId value should be between 1-64 characters"));
        }
    }

    private void validateDynamicUrl(String value, ConfigValidationResult result) {
        if (TextUtils.isEmpty(value)) {
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError(DYNAMIC_CONTROLLER_URL, getProviderName(), "controllerUrl is missing"));
        }
    }

    private void validateApplicationKey(String value, ConfigValidationResult result) {
        value = value.trim();
        if (value == null) {
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError("applicationKey", getProviderName(), "applicationKey value is missing"));
        } else if (value.length() < 5 || value.length() > 10) {
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError("applicationKey", getProviderName(), "applicationKey length should be between 5-10 characters"));
        } else if (!value.matches("^[a-zA-Z0-9]*$")) {
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError("applicationKey", getProviderName(), "applicationKey value should contains only english characters and numbers"));
        }
    }
}
