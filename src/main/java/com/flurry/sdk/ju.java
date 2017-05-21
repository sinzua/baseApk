package com.flurry.sdk;

import java.util.HashMap;
import java.util.Map;

public class ju {
    private static ju a;
    private static final String b = ju.class.getSimpleName();
    private static final HashMap<String, Map<String, String>> c = new HashMap();

    public static synchronized ju a() {
        ju juVar;
        synchronized (ju.class) {
            if (a == null) {
                a = new ju();
            }
            juVar = a;
        }
        return juVar;
    }

    public static synchronized void b() {
        synchronized (ju.class) {
            a = null;
        }
    }

    public synchronized void a(String str, String str2, Map<String, String> map) {
        if (map == null) {
            map = new HashMap();
        }
        if (map.size() >= 10) {
            kg.e(b, "MaxOriginParams exceeded: " + map.size());
        } else {
            map.put("flurryOriginVersion", str2);
            synchronized (c) {
                if (c.size() < 10 || c.containsKey(str)) {
                    c.put(str, map);
                } else {
                    kg.e(b, "MaxOrigins exceeded: " + c.size());
                }
            }
        }
    }

    public synchronized HashMap<String, Map<String, String>> c() {
        HashMap<String, Map<String, String>> hashMap;
        synchronized (c) {
            hashMap = new HashMap(c);
        }
        return hashMap;
    }
}
