package com.parse;

import org.json.JSONObject;

class PointerEncoder extends PointerOrLocalIdEncoder {
    private static final PointerEncoder INSTANCE = new PointerEncoder();

    PointerEncoder() {
    }

    public static PointerEncoder get() {
        return INSTANCE;
    }

    public JSONObject encodeRelatedObject(ParseObject object) {
        if (object.getObjectId() != null) {
            return super.encodeRelatedObject(object);
        }
        throw new IllegalStateException("unable to encode an association with an unsaved ParseObject");
    }
}
