package com.flurry.sdk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class lv {
    public static void a(JSONObject jSONObject, String str, Object obj) throws NullPointerException, JSONException {
        if (jSONObject == null) {
            throw new NullPointerException("Null Json object");
        } else if (obj == null) {
            jSONObject.put(str, JSONObject.NULL);
        } else {
            jSONObject.put(str, obj);
        }
    }

    public static void a(JSONObject jSONObject, String str, String str2) throws IOException, JSONException {
        if (jSONObject == null) {
            throw new IOException("Null Json object");
        } else if (str2 != null) {
            jSONObject.put(str, str2);
        } else {
            jSONObject.put(str, JSONObject.NULL);
        }
    }

    public static void a(JSONObject jSONObject, String str, boolean z) throws IOException, JSONException {
        if (jSONObject == null) {
            throw new IOException("Null Json object");
        }
        jSONObject.put(str, z);
    }

    public static void a(JSONObject jSONObject, String str, int i) throws IOException, JSONException {
        if (jSONObject == null) {
            throw new IOException("Null Json object");
        }
        jSONObject.put(str, i);
    }

    public static void a(JSONObject jSONObject, String str, float f) throws IOException, JSONException {
        if (jSONObject == null) {
            throw new IOException("Null Json object");
        }
        jSONObject.putOpt(str, Float.valueOf(f));
    }

    public static void a(JSONObject jSONObject, String str, long j) throws IOException, JSONException {
        if (jSONObject == null) {
            throw new IOException("Null Json object");
        }
        jSONObject.put(str, j);
    }

    public static Map<String, String> a(JSONObject jSONObject) throws JSONException {
        Map<String, String> hashMap = new HashMap();
        Iterator keys = jSONObject.keys();
        while (keys.hasNext()) {
            Object next = keys.next();
            if (next instanceof String) {
                String str = (String) next;
                Object obj = jSONObject.get(str);
                if (obj instanceof String) {
                    hashMap.put(str, (String) obj);
                } else {
                    throw new JSONException("JSONObject contains unsupported type for value in the map.");
                }
            }
            throw new JSONException("JSONObject contains unsupported type for key in the map.");
        }
        return hashMap;
    }

    public static List<JSONObject> a(JSONArray jSONArray) throws JSONException {
        List<JSONObject> arrayList = new ArrayList();
        int i = 0;
        while (i < jSONArray.length()) {
            Object obj = jSONArray.get(i);
            if (obj instanceof JSONObject) {
                arrayList.add((JSONObject) obj);
                i++;
            } else {
                throw new JSONException("Array contains unsupported objects. JSONArray param must contain JSON object.");
            }
        }
        return arrayList;
    }

    public static List<String> b(JSONArray jSONArray) throws JSONException {
        List<String> arrayList = new ArrayList();
        int i = 0;
        while (i < jSONArray.length()) {
            Object obj = jSONArray.get(i);
            if (obj instanceof String) {
                arrayList.add((String) obj);
                i++;
            } else {
                throw new JSONException("Array contains unsupported objects. JSONArray param must contain String object.");
            }
        }
        return arrayList;
    }
}
