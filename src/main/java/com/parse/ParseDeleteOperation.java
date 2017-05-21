package com.parse;

import org.json.JSONException;
import org.json.JSONObject;

class ParseDeleteOperation implements ParseFieldOperation {
    private static final ParseDeleteOperation defaultInstance = new ParseDeleteOperation();

    public static ParseDeleteOperation getInstance() {
        return defaultInstance;
    }

    private ParseDeleteOperation() {
    }

    public JSONObject encode(ParseEncoder objectEncoder) throws JSONException {
        JSONObject output = new JSONObject();
        output.put("__op", "Delete");
        return output;
    }

    public ParseFieldOperation mergeWithPrevious(ParseFieldOperation previous) {
        return this;
    }

    public Object apply(Object oldValue, String key) {
        return null;
    }
}
