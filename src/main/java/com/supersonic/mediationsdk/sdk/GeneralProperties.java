package com.supersonic.mediationsdk.sdk;

import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class GeneralProperties {
    private static GeneralProperties mInstance;
    private JSONObject mProperties = new JSONObject();

    private GeneralProperties() {
    }

    public static synchronized GeneralProperties getProperties() {
        GeneralProperties generalProperties;
        synchronized (GeneralProperties.class) {
            if (mInstance == null) {
                mInstance = new GeneralProperties();
            }
            generalProperties = mInstance;
        }
        return generalProperties;
    }

    public synchronized void putKeys(Map<String, Object> properties) {
        if (properties != null) {
            for (String key : properties.keySet()) {
                putKey(key, properties.get(key));
            }
        }
    }

    public synchronized void putKey(String key, Object value) {
        try {
            this.mProperties.put(key, value);
        } catch (JSONException e) {
        } catch (Exception e2) {
        }
    }

    public synchronized String get(String key) {
        return this.mProperties.optString(key);
    }

    public synchronized JSONObject toJSON() {
        return this.mProperties;
    }
}
