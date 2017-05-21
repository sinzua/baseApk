package com.parse;

import org.json.JSONObject;

class NoObjectsEncoder extends ParseEncoder {
    private static final NoObjectsEncoder INSTANCE = new NoObjectsEncoder();

    NoObjectsEncoder() {
    }

    public static NoObjectsEncoder get() {
        return INSTANCE;
    }

    public JSONObject encodeRelatedObject(ParseObject object) {
        throw new IllegalArgumentException("ParseObjects not allowed here");
    }
}
