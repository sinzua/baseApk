package com.parse;

import java.util.List;

@ParseClassName("_Pin")
class ParsePin extends ParseObject {
    static final String KEY_NAME = "_name";
    private static final String KEY_OBJECTS = "_objects";

    boolean needsDefaultACL() {
        return false;
    }

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public List<ParseObject> getObjects() {
        return getList(KEY_OBJECTS);
    }

    public void setObjects(List<ParseObject> objects) {
        put(KEY_OBJECTS, objects);
    }
}
