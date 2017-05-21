package com.parse;

import android.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

abstract class ParseEncoder {
    protected abstract JSONObject encodeRelatedObject(ParseObject parseObject);

    ParseEncoder() {
    }

    static boolean isValidType(Object value) {
        return (value instanceof JSONObject) || (value instanceof JSONArray) || (value instanceof String) || (value instanceof Number) || (value instanceof Boolean) || value == JSONObject.NULL || (value instanceof ParseObject) || (value instanceof ParseACL) || (value instanceof ParseFile) || (value instanceof ParseGeoPoint) || (value instanceof Date) || (value instanceof byte[]) || (value instanceof List) || (value instanceof Map) || (value instanceof ParseRelation);
    }

    public Object encode(Object object) {
        try {
            if (object instanceof ParseObject) {
                return encodeRelatedObject((ParseObject) object);
            }
            if (object instanceof Builder) {
                return encode(((Builder) object).build());
            }
            if (object instanceof State) {
                return ((State) object).toJSON(this);
            }
            if (object instanceof Date) {
                return encodeDate((Date) object);
            }
            Object json;
            if (object instanceof byte[]) {
                json = new JSONObject();
                json.put("__type", "Bytes");
                json.put("base64", Base64.encodeToString((byte[]) object, 2));
                return json;
            } else if (object instanceof ParseFile) {
                return ((ParseFile) object).encode();
            } else {
                if (object instanceof ParseGeoPoint) {
                    ParseGeoPoint point = (ParseGeoPoint) object;
                    json = new JSONObject();
                    json.put("__type", "GeoPoint");
                    json.put("latitude", point.getLatitude());
                    json.put("longitude", point.getLongitude());
                    return json;
                } else if (object instanceof ParseACL) {
                    return ((ParseACL) object).toJSONObject(this);
                } else {
                    if (object instanceof Map) {
                        Map<String, Object> map = (Map) object;
                        json = new JSONObject();
                        for (Entry<String, Object> pair : map.entrySet()) {
                            json.put((String) pair.getKey(), encode(pair.getValue()));
                        }
                        return json;
                    } else if (object instanceof JSONObject) {
                        JSONObject map2 = (JSONObject) object;
                        json = new JSONObject();
                        Iterator<?> keys = map2.keys();
                        while (keys.hasNext()) {
                            String key = (String) keys.next();
                            json.put(key, encode(map2.opt(key)));
                        }
                        return json;
                    } else if (object instanceof Collection) {
                        array = new JSONArray();
                        for (Object item : (Collection) object) {
                            array.put(encode(item));
                        }
                        return array;
                    } else if (object instanceof JSONArray) {
                        array = (JSONArray) object;
                        json = new JSONArray();
                        for (int i = 0; i < array.length(); i++) {
                            json.put(encode(array.opt(i)));
                        }
                        return json;
                    } else if (object instanceof ParseRelation) {
                        return ((ParseRelation) object).encodeToJSON(this);
                    } else {
                        if (object instanceof ParseFieldOperation) {
                            return ((ParseFieldOperation) object).encode(this);
                        }
                        if (object instanceof RelationConstraint) {
                            return ((RelationConstraint) object).encode(this);
                        }
                        if (object == null) {
                            return JSONObject.NULL;
                        }
                        if (isValidType(object)) {
                            return object;
                        }
                        throw new IllegalArgumentException("invalid type for ParseObject: " + object.getClass().toString());
                    }
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    protected JSONObject encodeDate(Date date) {
        JSONObject object = new JSONObject();
        String iso = ParseDateFormat.getInstance().format(date);
        try {
            object.put("__type", "Date");
            object.put("iso", iso);
            return object;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
