package com.parse;

import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

class ParseObjectCoder {
    private static final ParseObjectCoder INSTANCE = new ParseObjectCoder();
    private static final String KEY_ACL = "ACL";
    private static final String KEY_CLASS_NAME = "className";
    private static final String KEY_CREATED_AT = "createdAt";
    private static final String KEY_OBJECT_ID = "objectId";
    private static final String KEY_UPDATED_AT = "updatedAt";

    public static ParseObjectCoder get() {
        return INSTANCE;
    }

    ParseObjectCoder() {
    }

    public <T extends State> JSONObject encode(T state, ParseOperationSet operations, ParseEncoder encoder) {
        JSONObject objectJSON = new JSONObject();
        try {
            for (String key : operations.keySet()) {
                objectJSON.put(key, encoder.encode((ParseFieldOperation) operations.get(key)));
            }
            if (state.objectId() != null) {
                objectJSON.put(KEY_OBJECT_ID, state.objectId());
            }
            return objectJSON;
        } catch (JSONException e) {
            throw new RuntimeException("could not serialize object to JSON");
        }
    }

    public <T extends Init<?>> T decode(T builder, JSONObject json, ParseDecoder decoder) {
        try {
            Iterator<?> keys = json.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (!(key.equals("__type") || key.equals(KEY_CLASS_NAME))) {
                    if (key.equals(KEY_OBJECT_ID)) {
                        builder.objectId(json.getString(key));
                    } else if (key.equals(KEY_CREATED_AT)) {
                        builder.createdAt(ParseDateFormat.getInstance().parse(json.getString(key)));
                    } else if (key.equals(KEY_UPDATED_AT)) {
                        builder.updatedAt(ParseDateFormat.getInstance().parse(json.getString(key)));
                    } else if (key.equals(KEY_ACL)) {
                        builder.put(KEY_ACL, ParseACL.createACLFromJSONObject(json.getJSONObject(key), decoder));
                    } else {
                        builder.put(key, decoder.decode(json.get(key)));
                    }
                }
            }
            return builder;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
