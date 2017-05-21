package com.parse;

import android.net.Uri;
import com.parse.ParseRequest.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

class ParseRESTAnalyticsCommand extends ParseRESTCommand {
    static final String EVENT_APP_OPENED = "AppOpened";

    public ParseRESTAnalyticsCommand(String httpPath, Method httpMethod, Map<String, ?> parameters, String sessionToken) {
        super(httpPath, httpMethod, (Map) parameters, sessionToken);
    }

    public static ParseRESTAnalyticsCommand trackAppOpenedCommand(String pushHash, String sessionToken) {
        Map parameters = null;
        if (pushHash != null) {
            parameters = new HashMap();
            parameters.put("push_hash", pushHash);
        }
        return trackEventCommand(EVENT_APP_OPENED, parameters, sessionToken);
    }

    public static ParseRESTAnalyticsCommand trackEventCommand(String eventName, JSONObject dimensions, String sessionToken) {
        Map parameters = null;
        if (dimensions != null) {
            parameters = new HashMap();
            parameters.put("dimensions", dimensions);
        }
        return trackEventCommand(eventName, parameters, sessionToken);
    }

    static ParseRESTAnalyticsCommand trackEventCommand(String eventName, Map<String, ?> parameters, String sessionToken) {
        String httpPath = String.format("events/%s", new Object[]{Uri.encode(eventName)});
        Map<String, Object> commandParameters = new HashMap();
        if (parameters != null) {
            commandParameters.putAll(parameters);
        }
        commandParameters.put("at", NoObjectsEncoder.get().encode(new Date()));
        return new ParseRESTAnalyticsCommand(httpPath, Method.POST, commandParameters, sessionToken);
    }
}
