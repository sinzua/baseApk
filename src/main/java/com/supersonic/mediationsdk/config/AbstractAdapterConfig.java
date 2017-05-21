package com.supersonic.mediationsdk.config;

import com.supersonic.mediationsdk.logger.SupersonicError;
import com.supersonic.mediationsdk.logger.SupersonicLogger.SupersonicTag;
import com.supersonic.mediationsdk.logger.SupersonicLoggerManager;
import com.supersonic.mediationsdk.sdk.ConfigValidator;
import com.supersonic.mediationsdk.utils.ErrorBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONObject;

public abstract class AbstractAdapterConfig implements ConfigValidator {
    protected final String MAX_VIDEOS_KEY = "maxVideosPerSession";
    protected final String MAX_VIDEOS_PER_ITERATION_KEY = "maxVideosPerIteration";
    protected JSONObject mJsonSettings = ConfigFile.getConfigFile().getProviderJsonSettings(getProviderName());
    protected ArrayList<String> mMandatoryKeys = initializeMandatoryFields();
    protected ArrayList<String> mOptionalKeys;

    protected abstract void adapterPostValidation(JSONObject jSONObject, ConfigValidationResult configValidationResult);

    protected abstract String getProviderName();

    protected abstract ArrayList<String> initializeMandatoryFields();

    protected abstract ArrayList<String> initializeOptionalFields();

    protected abstract void validateMandatoryField(JSONObject jSONObject, String str, ConfigValidationResult configValidationResult);

    protected abstract void validateOptionalField(JSONObject jSONObject, String str, ConfigValidationResult configValidationResult);

    public AbstractAdapterConfig() {
        if (this.mMandatoryKeys == null) {
            this.mMandatoryKeys = new ArrayList();
        }
        this.mOptionalKeys = initializeOptionalFields();
        if (this.mOptionalKeys == null) {
            this.mOptionalKeys = new ArrayList();
        }
    }

    protected int getMaxVideosPerIterationToPresent() {
        try {
            if (this.mJsonSettings.has("maxVideosPerIteration")) {
                return this.mJsonSettings.getInt("maxVideosPerIteration");
            }
            return Integer.MAX_VALUE;
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }

    protected int getMaxVideosToPresent() {
        try {
            if (this.mJsonSettings.has("maxVideosPerSession")) {
                return this.mJsonSettings.getInt("maxVideosPerSession");
            }
            return Integer.MAX_VALUE;
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }

    public ConfigValidationResult isConfigValid() {
        ConfigValidationResult result = new ConfigValidationResult();
        checkForAllMandatoryFields(this.mJsonSettings, this.mMandatoryKeys, result);
        if (result.isValid()) {
            validateAllFields(this.mJsonSettings, result);
        }
        if (result.isValid()) {
            adapterPostValidation(this.mJsonSettings, result);
            if (!result.isValid()) {
                logConfigWarningMessage(result.getSupersonicError());
                result.setValid();
            }
        }
        SupersonicLoggerManager.getLogger().log(SupersonicTag.NATIVE, getProviderName() + ":isConfigValid(config: " + this.mJsonSettings.toString() + "):result(valid:" + result.isValid() + ")", 0);
        return result;
    }

    public void validateOptionalKeys(ArrayList<String> keys) {
        SupersonicLoggerManager.getLogger().log(SupersonicTag.NATIVE, getProviderName() + ":validateOptionalKeys(config: " + this.mJsonSettings + ")", 1);
        Iterator<String> iterator = keys.iterator();
        ConfigValidationResult result = new ConfigValidationResult();
        Iterator i$ = keys.iterator();
        while (i$.hasNext()) {
            String key = (String) i$.next();
            if (isOptionalField(key)) {
                validateOptionalField(this.mJsonSettings, key, result);
                if (!result.isValid()) {
                    logConfigWarningMessage(result.getSupersonicError());
                    result.setValid();
                }
            } else {
                SupersonicLoggerManager.getLogger().log(SupersonicTag.NATIVE, getProviderName() + ":validateOptionalKeys(" + key + ")", 0);
            }
        }
    }

    private void checkForAllMandatoryFields(JSONObject config, ArrayList<String> mandatoryKeys, ConfigValidationResult result) {
        if (mandatoryKeys == null || config == null) {
            result.setInvalid(ErrorBuilder.buildGenericError(getProviderName() + " - Wrong configuration"));
            return;
        }
        Iterator i$ = mandatoryKeys.iterator();
        while (i$.hasNext()) {
            String mandatory = (String) i$.next();
            if (config.has(mandatory)) {
                try {
                    if (isEmpty(config.getString(mandatory))) {
                        result.setInvalid(ErrorBuilder.buildInvalidKeyValueError(mandatory, getProviderName(), null));
                        return;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    result.setInvalid(ErrorBuilder.buildInvalidKeyValueError(mandatory, getProviderName(), null));
                    return;
                }
            }
            result.setInvalid(ErrorBuilder.buildKeyNotSetForProviderError(mandatory, getProviderName()));
            return;
        }
    }

    private void validateAllFields(JSONObject config, ConfigValidationResult result) {
        try {
            Iterator<String> keysIterator = config.keys();
            while (result.isValid() && keysIterator.hasNext()) {
                String key = (String) keysIterator.next();
                if (isMandatoryField(key)) {
                    validateMandatoryField(config, key, result);
                } else if (isOptionalField(key)) {
                    validateOptionalField(config, key, result);
                    if (!result.isValid()) {
                        logConfigWarningMessage(result.getSupersonicError());
                        keysIterator.remove();
                        result.setValid();
                    }
                } else {
                    SupersonicLoggerManager.getLogger().log(SupersonicTag.ADAPTER_API, getProviderName() + ":Unknown key in configuration - " + key, 2);
                }
            }
        } catch (Throwable th) {
            result.setInvalid(ErrorBuilder.buildGenericError(getProviderName() + " - Invalid configuration"));
        }
    }

    private boolean isOptionalField(String key) {
        return this.mOptionalKeys.contains(key);
    }

    private boolean isMandatoryField(String key) {
        return this.mMandatoryKeys.contains(key);
    }

    protected void validateMaxVideos(int maxVideos, ConfigValidationResult result) {
        if (maxVideos < 0) {
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError("maxVideos", getProviderName(), "maxVideos value should be any integer >= 0, your value is:" + maxVideos));
        }
    }

    protected void validateRewards(int rewards, ConfigValidationResult result) {
        if (rewards <= 0) {
            try {
                result.setInvalid(ErrorBuilder.buildInvalidKeyValueError("rewards", getProviderName(), "rewards value should be any positive value."));
            } catch (Throwable th) {
                result.setInvalid(ErrorBuilder.buildInvalidKeyValueError("rewards", getProviderName(), "rewards value should be any positive value."));
            }
        }
    }

    protected void validateNonEmptyString(String key, String value, ConfigValidationResult result) {
        if (isEmpty(value)) {
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError(key, getProviderName(), "value is empty"));
        }
    }

    private boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    protected void logConfigWarningMessage(SupersonicError error) {
        SupersonicLoggerManager.getLogger().log(SupersonicTag.ADAPTER_API, error.toString(), 2);
    }

    protected void validateBoolean(String key, String value, ConfigValidationResult result) {
        value = value.trim();
        if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
            result.setInvalid(ErrorBuilder.buildInvalidKeyValueError(key, getProviderName(), "value should be 'true'/'false'"));
        }
    }
}
