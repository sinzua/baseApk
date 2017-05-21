package com.parse;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

abstract class ParseTraverser {
    private boolean traverseParseObjects = false;
    private boolean yieldRoot = false;

    protected abstract boolean visit(Object obj);

    private void traverseInternal(Object root, boolean yieldRoot, IdentityHashMap<Object, Object> seen) {
        if (root != null && !seen.containsKey(root)) {
            if (!yieldRoot || visit(root)) {
                seen.put(root, root);
                if (root instanceof JSONObject) {
                    JSONObject json = (JSONObject) root;
                    Iterator<String> keys = json.keys();
                    while (keys.hasNext()) {
                        try {
                            traverseInternal(json.get((String) keys.next()), true, seen);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else if (root instanceof JSONArray) {
                    JSONArray array = (JSONArray) root;
                    int i = 0;
                    while (i < array.length()) {
                        try {
                            traverseInternal(array.get(i), true, seen);
                            i++;
                        } catch (JSONException e2) {
                            throw new RuntimeException(e2);
                        }
                    }
                } else if (root instanceof Map) {
                    for (Object value : ((Map) root).values()) {
                        traverseInternal(value, true, seen);
                    }
                } else if (root instanceof List) {
                    for (Object value2 : (List) root) {
                        traverseInternal(value2, true, seen);
                    }
                } else if (root instanceof ParseObject) {
                    if (this.traverseParseObjects) {
                        ParseObject object = (ParseObject) root;
                        for (String key : object.keySet()) {
                            traverseInternal(object.get(key), true, seen);
                        }
                    }
                } else if (root instanceof ParseACL) {
                    ParseUser user = ((ParseACL) root).getUnresolvedUser();
                    if (user != null && user.isCurrentUser()) {
                        traverseInternal(user, true, seen);
                    }
                }
            }
        }
    }

    public ParseTraverser setTraverseParseObjects(boolean newValue) {
        this.traverseParseObjects = newValue;
        return this;
    }

    public ParseTraverser setYieldRoot(boolean newValue) {
        this.yieldRoot = newValue;
        return this;
    }

    public void traverse(Object root) {
        traverseInternal(root, this.yieldRoot, new IdentityHashMap());
    }
}
