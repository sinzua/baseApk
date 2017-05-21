package com.parse;

import bolts.Continuation;
import bolts.Task;
import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class ParseConfig {
    static final TaskQueue taskQueue = new TaskQueue();
    final Map<String, Object> params;

    static ParseConfigController getConfigController() {
        return ParseCorePlugins.getInstance().getConfigController();
    }

    public static ParseConfig getCurrentConfig() {
        try {
            return (ParseConfig) ParseTaskUtils.wait(getConfigController().getCurrentConfigController().getCurrentConfigAsync());
        } catch (ParseException e) {
            return new ParseConfig();
        }
    }

    public static ParseConfig get() throws ParseException {
        return (ParseConfig) ParseTaskUtils.wait(getInBackground());
    }

    public static void getInBackground(ConfigCallback callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(getInBackground(), (ParseCallback2) callback);
    }

    public static Task<ParseConfig> getInBackground() {
        return taskQueue.enqueue(new Continuation<Void, Task<ParseConfig>>() {
            public Task<ParseConfig> then(Task<Void> toAwait) throws Exception {
                return ParseConfig.getAsync(toAwait);
            }
        });
    }

    private static Task<ParseConfig> getAsync(final Task<Void> toAwait) {
        return ParseUser.getCurrentSessionTokenAsync().onSuccessTask(new Continuation<String, Task<ParseConfig>>() {
            public Task<ParseConfig> then(Task<String> task) throws Exception {
                final String sessionToken = (String) task.getResult();
                return toAwait.continueWithTask(new Continuation<Void, Task<ParseConfig>>() {
                    public Task<ParseConfig> then(Task<Void> task) throws Exception {
                        return ParseConfig.getConfigController().getAsync(sessionToken);
                    }
                });
            }
        });
    }

    ParseConfig(JSONObject object, ParseDecoder decoder) {
        Map<String, Object> decodedParams = (Map) ((Map) decoder.decode(object)).get("params");
        if (decodedParams == null) {
            throw new RuntimeException("Object did not contain the 'params' key.");
        }
        this.params = Collections.unmodifiableMap(decodedParams);
    }

    ParseConfig() {
        this.params = Collections.unmodifiableMap(new HashMap());
    }

    Map<String, Object> getParams() {
        return Collections.unmodifiableMap(new HashMap(this.params));
    }

    public Object get(String key) {
        return get(key, null);
    }

    public Object get(String key, Object defaultValue) {
        if (!this.params.containsKey(key)) {
            return defaultValue;
        }
        if (this.params.get(key) == JSONObject.NULL) {
            return null;
        }
        return this.params.get(key);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        if (!this.params.containsKey(key)) {
            return defaultValue;
        }
        Object value = this.params.get(key);
        return value instanceof Boolean ? ((Boolean) value).booleanValue() : defaultValue;
    }

    public Date getDate(String key) {
        return getDate(key, null);
    }

    public Date getDate(String key, Date defaultValue) {
        if (!this.params.containsKey(key)) {
            return defaultValue;
        }
        Object value = this.params.get(key);
        if (value == null || value == JSONObject.NULL) {
            return null;
        }
        return value instanceof Date ? (Date) value : defaultValue;
    }

    public double getDouble(String key) {
        return getDouble(key, 0.0d);
    }

    public double getDouble(String key, double defaultValue) {
        Number number = getNumber(key);
        return number != null ? number.doubleValue() : defaultValue;
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        Number number = getNumber(key);
        return number != null ? number.intValue() : defaultValue;
    }

    public JSONArray getJSONArray(String key) {
        return getJSONArray(key, null);
    }

    public JSONArray getJSONArray(String key, JSONArray defaultValue) {
        List<Object> list = getList(key);
        Object encoded = list != null ? PointerEncoder.get().encode(list) : null;
        return (encoded == null || (encoded instanceof JSONArray)) ? (JSONArray) encoded : defaultValue;
    }

    public JSONObject getJSONObject(String key) {
        return getJSONObject(key, null);
    }

    public JSONObject getJSONObject(String key, JSONObject defaultValue) {
        Map<String, Object> map = getMap(key);
        Object encoded = map != null ? PointerEncoder.get().encode(map) : null;
        return (encoded == null || (encoded instanceof JSONObject)) ? (JSONObject) encoded : defaultValue;
    }

    public <T> List<T> getList(String key) {
        return getList(key, null);
    }

    public <T> List<T> getList(String key, List<T> defaultValue) {
        if (!this.params.containsKey(key)) {
            return defaultValue;
        }
        Object value = this.params.get(key);
        if (value == null || value == JSONObject.NULL) {
            return null;
        }
        List<T> returnValue;
        if (value instanceof List) {
            returnValue = (List) value;
        } else {
            returnValue = defaultValue;
        }
        return returnValue;
    }

    public long getLong(String key) {
        return getLong(key, 0);
    }

    public long getLong(String key, long defaultValue) {
        Number number = getNumber(key);
        return number != null ? number.longValue() : defaultValue;
    }

    public <V> Map<String, V> getMap(String key) {
        return getMap(key, null);
    }

    public <V> Map<String, V> getMap(String key, Map<String, V> defaultValue) {
        if (!this.params.containsKey(key)) {
            return defaultValue;
        }
        Object value = this.params.get(key);
        if (value == null || value == JSONObject.NULL) {
            return null;
        }
        Map<String, V> returnValue;
        if (value instanceof Map) {
            returnValue = (Map) value;
        } else {
            returnValue = defaultValue;
        }
        return returnValue;
    }

    public Number getNumber(String key) {
        return getNumber(key, null);
    }

    public Number getNumber(String key, Number defaultValue) {
        if (!this.params.containsKey(key)) {
            return defaultValue;
        }
        Object value = this.params.get(key);
        if (value == null || value == JSONObject.NULL) {
            return null;
        }
        return value instanceof Number ? (Number) value : defaultValue;
    }

    public ParseFile getParseFile(String key) {
        return getParseFile(key, null);
    }

    public ParseFile getParseFile(String key, ParseFile defaultValue) {
        if (!this.params.containsKey(key)) {
            return defaultValue;
        }
        Object value = this.params.get(key);
        if (value == null || value == JSONObject.NULL) {
            return null;
        }
        return value instanceof ParseFile ? (ParseFile) value : defaultValue;
    }

    public ParseGeoPoint getParseGeoPoint(String key) {
        return getParseGeoPoint(key, null);
    }

    public ParseGeoPoint getParseGeoPoint(String key, ParseGeoPoint defaultValue) {
        if (!this.params.containsKey(key)) {
            return defaultValue;
        }
        Object value = this.params.get(key);
        if (value == null || value == JSONObject.NULL) {
            return null;
        }
        return value instanceof ParseGeoPoint ? (ParseGeoPoint) value : defaultValue;
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String defaultValue) {
        if (!this.params.containsKey(key)) {
            return defaultValue;
        }
        Object value = this.params.get(key);
        if (value == null || value == JSONObject.NULL) {
            return null;
        }
        return value instanceof String ? (String) value : defaultValue;
    }

    public String toString() {
        return "ParseConfig[" + this.params.toString() + RequestParameters.RIGHT_BRACKETS;
    }
}
