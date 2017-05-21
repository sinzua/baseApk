package com.nativex.network.volley.toolbox;

import com.nativex.network.volley.Cache;
import com.nativex.network.volley.Cache.Entry;

public class NoCache implements Cache {
    public void clear() {
    }

    public Entry get(String key) {
        return null;
    }

    public void put(String key, Entry entry) {
    }

    public void invalidate(String key, boolean fullExpire) {
    }

    public void remove(String key) {
    }

    public void initialize() {
    }
}
