package com.parse;

import com.parse.ParseRequest.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class ParseRESTPushCommand extends ParseRESTCommand {
    static final String KEY_CHANNELS = "channels";
    static final String KEY_DATA = "data";
    static final String KEY_DEVICE_TYPE = "deviceType";
    static final String KEY_EXPIRATION_INTERVAL = "expiration_interval";
    static final String KEY_EXPIRATION_TIME = "expiration_time";
    static final String KEY_WHERE = "where";

    public ParseRESTPushCommand(String httpPath, Method httpMethod, Map<String, ?> parameters, String sessionToken) {
        super(httpPath, httpMethod, (Map) parameters, sessionToken);
    }

    public static ParseRESTPushCommand sendPushCommand(State<ParseInstallation> query, Set<String> targetChannels, String targetDeviceType, Long expirationTime, Long expirationInterval, JSONObject payload, String sessionToken) {
        Map<String, Object> parameters = new HashMap();
        if (targetChannels != null) {
            parameters.put(KEY_CHANNELS, new JSONArray(targetChannels));
        } else {
            if (query != null) {
                parameters.put(KEY_WHERE, (JSONObject) PointerEncoder.get().encode(query.constraints()));
            }
            if (targetDeviceType != null) {
                JSONObject deviceTypeCondition = new JSONObject();
                try {
                    deviceTypeCondition.put(KEY_DEVICE_TYPE, targetDeviceType);
                    parameters.put(KEY_WHERE, deviceTypeCondition);
                } catch (JSONException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
            if (parameters.size() == 0) {
                parameters.put(KEY_WHERE, new JSONObject());
            }
        }
        if (expirationTime != null) {
            parameters.put(KEY_EXPIRATION_TIME, expirationTime);
        } else if (expirationInterval != null) {
            parameters.put(KEY_EXPIRATION_INTERVAL, expirationInterval);
        }
        if (payload != null) {
            parameters.put(KEY_DATA, payload);
        }
        return new ParseRESTPushCommand("push", Method.POST, parameters, sessionToken);
    }
}
