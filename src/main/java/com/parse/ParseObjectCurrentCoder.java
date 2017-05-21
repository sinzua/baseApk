package com.parse;

import java.util.Date;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class ParseObjectCurrentCoder extends ParseObjectCoder {
    private static final ParseObjectCurrentCoder INSTANCE = new ParseObjectCurrentCoder();
    private static final String KEY_CLASS_NAME = "classname";
    private static final String KEY_CREATED_AT = "createdAt";
    private static final String KEY_DATA = "data";
    private static final String KEY_OBJECT_ID = "objectId";
    private static final String KEY_OLD_CREATED_AT = "created_at";
    private static final String KEY_OLD_OBJECT_ID = "id";
    private static final String KEY_OLD_POINTERS = "pointers";
    private static final String KEY_OLD_UPDATED_AT = "updated_at";
    private static final String KEY_UPDATED_AT = "updatedAt";

    public static ParseObjectCurrentCoder get() {
        return INSTANCE;
    }

    ParseObjectCurrentCoder() {
    }

    public <T extends State> JSONObject encode(T state, ParseOperationSet operations, ParseEncoder encoder) {
        if (operations != null) {
            throw new IllegalArgumentException("Parameter ParseOperationSet is not null");
        }
        JSONObject objectJSON = new JSONObject();
        JSONObject dataJSON = new JSONObject();
        try {
            for (String key : state.keySet()) {
                dataJSON.put(key, encoder.encode(state.get(key)));
            }
            if (state.createdAt() > 0) {
                dataJSON.put(KEY_CREATED_AT, ParseDateFormat.getInstance().format(new Date(state.createdAt())));
            }
            if (state.updatedAt() > 0) {
                dataJSON.put(KEY_UPDATED_AT, ParseDateFormat.getInstance().format(new Date(state.updatedAt())));
            }
            if (state.objectId() != null) {
                dataJSON.put(KEY_OBJECT_ID, state.objectId());
            }
            objectJSON.put(KEY_DATA, dataJSON);
            objectJSON.put(KEY_CLASS_NAME, state.className());
            return objectJSON;
        } catch (JSONException e) {
            throw new RuntimeException("could not serialize object to JSON");
        }
    }

    public <T extends Init<?>> T decode(T builder, JSONObject json, ParseDecoder decoder) {
        try {
            Iterator<?> keys;
            String key;
            if (json.has("id")) {
                builder.objectId(json.getString("id"));
            }
            if (json.has(KEY_OLD_CREATED_AT)) {
                String createdAtString = json.getString(KEY_OLD_CREATED_AT);
                if (createdAtString != null) {
                    builder.createdAt(ParseImpreciseDateFormat.getInstance().parse(createdAtString));
                }
            }
            if (json.has(KEY_OLD_UPDATED_AT)) {
                String updatedAtString = json.getString(KEY_OLD_UPDATED_AT);
                if (updatedAtString != null) {
                    builder.updatedAt(ParseImpreciseDateFormat.getInstance().parse(updatedAtString));
                }
            }
            if (json.has(KEY_OLD_POINTERS)) {
                JSONObject newPointers = json.getJSONObject(KEY_OLD_POINTERS);
                keys = newPointers.keys();
                while (keys.hasNext()) {
                    key = (String) keys.next();
                    JSONArray pointerArray = newPointers.getJSONArray(key);
                    builder.put(key, ParseObject.createWithoutData(pointerArray.optString(0), pointerArray.optString(1)));
                }
            }
            JSONObject data = json.optJSONObject(KEY_DATA);
            if (data != null) {
                keys = data.keys();
                while (keys.hasNext()) {
                    key = (String) keys.next();
                    if (key.equals(KEY_OBJECT_ID)) {
                        builder.objectId(data.getString(key));
                    } else if (key.equals(KEY_CREATED_AT)) {
                        builder.createdAt(ParseDateFormat.getInstance().parse(data.getString(key)));
                    } else if (key.equals(KEY_UPDATED_AT)) {
                        builder.updatedAt(ParseDateFormat.getInstance().parse(data.getString(key)));
                    } else {
                        builder.put(key, decoder.decode(data.get(key)));
                    }
                }
            }
            return builder;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
