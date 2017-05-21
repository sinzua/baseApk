package org.codehaus.jackson.map.util;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class LRUMap<K, V> extends LinkedHashMap<K, V> {
    protected final int _maxEntries;

    public LRUMap(int initialEntries, int maxEntries) {
        super(initialEntries, 0.8f, true);
        this._maxEntries = maxEntries;
    }

    protected boolean removeEldestEntry(Entry<K, V> entry) {
        return size() > this._maxEntries;
    }
}
