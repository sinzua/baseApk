package com.parse;

import com.parse.ParseRequest.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.json.JSONObject;

class ParseRESTQueryCommand extends ParseRESTCommand {
    public static <T extends ParseObject> ParseRESTQueryCommand findCommand(State<T> state, String sessionToken) {
        return new ParseRESTQueryCommand(String.format("classes/%s", new Object[]{state.className()}), Method.GET, encode(state, false), sessionToken);
    }

    public static <T extends ParseObject> ParseRESTQueryCommand countCommand(State<T> state, String sessionToken) {
        return new ParseRESTQueryCommand(String.format("classes/%s", new Object[]{state.className()}), Method.GET, encode(state, true), sessionToken);
    }

    static <T extends ParseObject> Map<String, String> encode(State<T> state, boolean count) {
        ParseEncoder encoder = PointerEncoder.get();
        HashMap<String, String> parameters = new HashMap();
        List<String> order = state.order();
        if (!order.isEmpty()) {
            parameters.put("order", ParseTextUtils.join(",", order));
        }
        QueryConstraints conditions = state.constraints();
        if (!conditions.isEmpty()) {
            parameters.put("where", ((JSONObject) encoder.encode(conditions)).toString());
        }
        Set<String> selectedKeys = state.selectedKeys();
        if (selectedKeys != null) {
            parameters.put("keys", ParseTextUtils.join(",", selectedKeys));
        }
        Set<String> includeds = state.includes();
        if (!includeds.isEmpty()) {
            parameters.put("include", ParseTextUtils.join(",", includeds));
        }
        if (count) {
            parameters.put("count", Integer.toString(1));
        } else {
            int limit = state.limit();
            if (limit >= 0) {
                parameters.put("limit", Integer.toString(limit));
            }
            int skip = state.skip();
            if (skip > 0) {
                parameters.put("skip", Integer.toString(skip));
            }
        }
        for (Entry<String, Object> entry : state.extraOptions().entrySet()) {
            parameters.put(entry.getKey(), encoder.encode(entry.getValue()).toString());
        }
        if (state.isTracingEnabled()) {
            parameters.put("trace", Integer.toString(1));
        }
        return parameters;
    }

    private ParseRESTQueryCommand(String httpPath, Method httpMethod, Map<String, ?> parameters, String sessionToken) {
        super(httpPath, httpMethod, (Map) parameters, sessionToken);
    }
}
