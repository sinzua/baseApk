package com.parse;

import org.json.JSONException;
import org.json.JSONObject;

class PointerOrLocalIdEncoder extends ParseEncoder {
    private static final PointerOrLocalIdEncoder INSTANCE = new PointerOrLocalIdEncoder();

    PointerOrLocalIdEncoder() {
    }

    public static PointerOrLocalIdEncoder get() {
        return INSTANCE;
    }

    public JSONObject encodeRelatedObject(ParseObject object) {
        JSONObject json = new JSONObject();
        try {
            if (object.getObjectId() != null) {
                json.put("__type", "Pointer");
                json.put("className", object.getClassName());
                json.put("objectId", object.getObjectId());
            } else {
                json.put("__type", "Pointer");
                json.put("className", object.getClassName());
                json.put("localId", object.getOrCreateLocalId());
            }
            return json;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
