package com.lidroid.xutils.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DoubleKeyValueMap<K1, K2, V> {
    private ConcurrentHashMap<K1, ConcurrentHashMap<K2, V>> k1_k2V_map = new ConcurrentHashMap();

    public void put(K1 key1, K2 key2, V value) {
        if (key1 != null && key2 != null && value != null) {
            ConcurrentHashMap<K2, V> k2V_map;
            if (this.k1_k2V_map.containsKey(key1)) {
                k2V_map = (ConcurrentHashMap) this.k1_k2V_map.get(key1);
                if (k2V_map != null) {
                    k2V_map.put(key2, value);
                    return;
                }
                k2V_map = new ConcurrentHashMap();
                k2V_map.put(key2, value);
                this.k1_k2V_map.put(key1, k2V_map);
                return;
            }
            k2V_map = new ConcurrentHashMap();
            k2V_map.put(key2, value);
            this.k1_k2V_map.put(key1, k2V_map);
        }
    }

    public Set<K1> getFirstKeys() {
        return this.k1_k2V_map.keySet();
    }

    public ConcurrentHashMap<K2, V> get(K1 key1) {
        return (ConcurrentHashMap) this.k1_k2V_map.get(key1);
    }

    public V get(K1 key1, K2 key2) {
        ConcurrentHashMap<K2, V> k2_v = (ConcurrentHashMap) this.k1_k2V_map.get(key1);
        return k2_v == null ? null : k2_v.get(key2);
    }

    public Collection<V> getAllValues(K1 key1) {
        ConcurrentHashMap<K2, V> k2_v = (ConcurrentHashMap) this.k1_k2V_map.get(key1);
        return k2_v == null ? null : k2_v.values();
    }

    public Collection<V> getAllValues() {
        Collection<V> result = null;
        Set<K1> k1Set = this.k1_k2V_map.keySet();
        if (k1Set != null) {
            result = new ArrayList();
            for (K1 k1 : k1Set) {
                Collection<V> values = ((ConcurrentHashMap) this.k1_k2V_map.get(k1)).values();
                if (values != null) {
                    result.addAll(values);
                }
            }
        }
        return result;
    }

    public boolean containsKey(K1 key1, K2 key2) {
        if (this.k1_k2V_map.containsKey(key1)) {
            return ((ConcurrentHashMap) this.k1_k2V_map.get(key1)).containsKey(key2);
        }
        return false;
    }

    public boolean containsKey(K1 key1) {
        return this.k1_k2V_map.containsKey(key1);
    }

    public int size() {
        if (this.k1_k2V_map.size() == 0) {
            return 0;
        }
        int result = 0;
        for (ConcurrentHashMap<K2, V> k2V_map : this.k1_k2V_map.values()) {
            result += k2V_map.size();
        }
        return result;
    }

    public void remove(K1 key1) {
        this.k1_k2V_map.remove(key1);
    }

    public void remove(K1 key1, K2 key2) {
        ConcurrentHashMap<K2, V> k2_v = (ConcurrentHashMap) this.k1_k2V_map.get(key1);
        if (k2_v != null) {
            k2_v.remove(key2);
        }
    }

    public void clear() {
        if (this.k1_k2V_map.size() > 0) {
            for (ConcurrentHashMap<K2, V> k2V_map : this.k1_k2V_map.values()) {
                k2V_map.clear();
            }
            this.k1_k2V_map.clear();
        }
    }
}
