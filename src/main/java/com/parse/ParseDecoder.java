package com.parse;

import android.util.Base64;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class ParseDecoder {
    private static final ParseDecoder INSTANCE = new ParseDecoder();

    public static ParseDecoder get() {
        return INSTANCE;
    }

    protected ParseDecoder() {
    }

    List<Object> convertJSONArrayToList(JSONArray array) {
        List<Object> list = new ArrayList();
        for (int i = 0; i < array.length(); i++) {
            list.add(decode(array.opt(i)));
        }
        return list;
    }

    Map<String, Object> convertJSONObjectToMap(JSONObject object) {
        Map<String, Object> outputMap = new HashMap();
        Iterator<String> it = object.keys();
        while (it.hasNext()) {
            String key = (String) it.next();
            outputMap.put(key, decode(object.opt(key)));
        }
        return outputMap;
    }

    protected ParseObject decodePointer(String className, String objectId) {
        return ParseObject.createWithoutData(className, objectId);
    }

    public Object decode(Object object) {
        if (object instanceof JSONArray) {
            return convertJSONArrayToList((JSONArray) object);
        }
        if (!(object instanceof JSONObject)) {
            return object;
        }
        JSONObject jsonObject = (JSONObject) object;
        if (jsonObject.optString("__op", null) != null) {
            try {
                return ParseFieldOperations.decode(jsonObject, this);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        String typeString = jsonObject.optString("__type", null);
        if (typeString == null) {
            return convertJSONObjectToMap(jsonObject);
        }
        if (typeString.equals("Date")) {
            return ParseDateFormat.getInstance().parse(jsonObject.optString("iso"));
        } else if (typeString.equals("Bytes")) {
            return Base64.decode(jsonObject.optString("base64"), 2);
        } else {
            if (typeString.equals("Pointer")) {
                return decodePointer(jsonObject.optString("className"), jsonObject.optString("objectId"));
            }
            if (typeString.equals("File")) {
                return new ParseFile(jsonObject, this);
            }
            if (typeString.equals("GeoPoint")) {
                try {
                    return new ParseGeoPoint(jsonObject.getDouble("latitude"), jsonObject.getDouble("longitude"));
                } catch (JSONException e2) {
                    throw new RuntimeException(e2);
                }
            } else if (typeString.equals("Object")) {
                return ParseObject.fromJSON(jsonObject, null, true, this);
            } else {
                if (typeString.equals("Relation")) {
                    return new ParseRelation(jsonObject, this);
                }
                if (!typeString.equals("OfflineObject")) {
                    return null;
                }
                throw new RuntimeException("An unexpected offline pointer was encountered.");
            }
        }
    }
}
