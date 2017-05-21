package com.parse;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

class ParseJSONUtils {
    ParseJSONUtils() {
    }

    public static JSONObject create(JSONObject copyFrom, Collection<String> excludes) {
        JSONObject json = new JSONObject();
        Iterator<String> iterator = copyFrom.keys();
        while (iterator.hasNext()) {
            String name = (String) iterator.next();
            if (!excludes.contains(name)) {
                try {
                    json.put(name, copyFrom.opt(name));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return json;
    }

    public static Iterable<String> keys(JSONObject object) {
        final JSONObject finalObject = object;
        return new Iterable<String>() {
            public Iterator<String> iterator() {
                return finalObject.keys();
            }
        };
    }

    public static int getInt(JSONObject object, List<String> keys) throws JSONException {
        for (String key : keys) {
            try {
                return object.getInt(key);
            } catch (JSONException e) {
            }
        }
        throw new JSONException("No value for " + keys);
    }
}
