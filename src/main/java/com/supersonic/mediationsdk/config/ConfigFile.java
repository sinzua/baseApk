package com.supersonic.mediationsdk.config;

import com.supersonic.mediationsdk.logger.SupersonicLogger.SupersonicTag;
import com.supersonic.mediationsdk.logger.SupersonicLoggerManager;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class ConfigFile {
    public static final String DEFAULT_AMOUNT_WARNING_MESSAGE = "Rewards amount doesn't exist in configuration, value was set to 1. Edit configurations file in order to change the value";
    public static final int DEFAULT_REWARD_AMOUNT = 1;
    private static ConfigFile mInstance;
    private final String CONFIG_ADAPTERS = "adapters";
    private final String CONFIG_FILE_DEFAULT_PATH = "/supersonic.config";
    private final String CONFIG_PROVIDER_NAME = "name";
    private final String CONFIG_PROVIDER_SETTINGS = "settings";
    private final String CONFIG_PROVIDER_VERSION = "version";
    private final String CONFIG_TEST_FILE = "supersonic.config";
    private final String CONFIG_TEST_FOLDER = "SupersonicTest";
    protected final String TAG = ConfigFile.class.getName();
    private String mPluginType;
    private String mPluginVersion;
    private Map<String, JSONObject> mProvidersConfigMap = new HashMap();

    private ConfigFile() {
    }

    public static synchronized ConfigFile getConfigFile() {
        ConfigFile configFile;
        synchronized (ConfigFile.class) {
            if (mInstance == null) {
                mInstance = new ConfigFile();
            }
            configFile = mInstance;
        }
        return configFile;
    }

    public JSONObject getProviderJsonSettings(String providerName) {
        JSONObject result = null;
        try {
            if (this.mProvidersConfigMap.containsKey(providerName)) {
                result = (JSONObject) this.mProvidersConfigMap.get(providerName);
            }
        } catch (Throwable e) {
            SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, "getProviderJSONSettings(providerName:" + providerName + ")", e);
        }
        if (result != null) {
            return result;
        }
        result = new JSONObject();
        this.mProvidersConfigMap.put(providerName, result);
        return result;
    }

    public String getProviderCoreSdkVersion(String providerName) {
        String result = "";
        try {
            JSONObject providerSection = (JSONObject) this.mProvidersConfigMap.get(providerName);
            if (providerSection == null || !providerSection.has("version")) {
                return result;
            }
            return (String) providerSection.get("version");
        } catch (Exception e) {
            SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, "getProviderCoreSdkVersion(providerName:" + providerName + ")", e);
            return result;
        }
    }

    public void setPluginData(String pluginType, String pluginVersion) {
        if (pluginType != null && ("Unity".equals(pluginType) || "AdobeAir".equals(pluginType) || "Xamarin".equals(pluginType))) {
            this.mPluginType = pluginType;
        }
        if (pluginVersion != null) {
            this.mPluginVersion = pluginVersion;
        }
    }

    public String getPluginType() {
        return this.mPluginType;
    }

    public String getPluginVersion() {
        return this.mPluginVersion;
    }
}
