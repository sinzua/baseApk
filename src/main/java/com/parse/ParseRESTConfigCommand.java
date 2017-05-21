package com.parse;

import com.parse.ParseRequest.Method;
import java.util.HashMap;
import java.util.Map;

class ParseRESTConfigCommand extends ParseRESTCommand {
    public ParseRESTConfigCommand(String httpPath, Method httpMethod, Map<String, ?> parameters, String sessionToken) {
        super(httpPath, httpMethod, (Map) parameters, sessionToken);
    }

    public static ParseRESTConfigCommand fetchConfigCommand(String sessionToken) {
        return new ParseRESTConfigCommand("config", Method.GET, null, sessionToken);
    }

    public static ParseRESTConfigCommand updateConfigCommand(Map<String, ?> configParameters, String sessionToken) {
        Map<String, Map<String, ?>> commandParameters = null;
        if (configParameters != null) {
            commandParameters = new HashMap();
            commandParameters.put("params", configParameters);
        }
        return new ParseRESTConfigCommand("config", Method.PUT, commandParameters, sessionToken);
    }
}
