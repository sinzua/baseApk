package com.parse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class ParseAddOperation implements ParseFieldOperation {
    protected final ArrayList<Object> objects = new ArrayList();

    public ParseAddOperation(Collection<?> coll) {
        this.objects.addAll(coll);
    }

    public JSONObject encode(ParseEncoder objectEncoder) throws JSONException {
        JSONObject output = new JSONObject();
        output.put("__op", "Add");
        output.put("objects", objectEncoder.encode(this.objects));
        return output;
    }

    public ParseFieldOperation mergeWithPrevious(ParseFieldOperation previous) {
        if (previous == null) {
            return this;
        }
        if (previous instanceof ParseDeleteOperation) {
            return new ParseSetOperation(this.objects);
        }
        ArrayList<Object> result;
        if (previous instanceof ParseSetOperation) {
            Object value = ((ParseSetOperation) previous).getValue();
            if (value instanceof JSONArray) {
                result = ParseFieldOperations.jsonArrayAsArrayList((JSONArray) value);
                result.addAll(this.objects);
                super(new JSONArray(result));
                return this;
            } else if (value instanceof List) {
                result = new ArrayList((List) value);
                result.addAll(this.objects);
                super(result);
                return this;
            } else {
                throw new IllegalArgumentException("You can only add an item to a List or JSONArray.");
            }
        } else if (previous instanceof ParseAddOperation) {
            result = new ArrayList(((ParseAddOperation) previous).objects);
            result.addAll(this.objects);
            this(result);
            return this;
        } else {
            throw new IllegalArgumentException("Operation is invalid after previous operation.");
        }
    }

    public Object apply(Object oldValue, String key) {
        if (oldValue == null) {
            return this.objects;
        }
        if (oldValue instanceof JSONArray) {
            return new JSONArray((ArrayList) apply(ParseFieldOperations.jsonArrayAsArrayList((JSONArray) oldValue), key));
        }
        if (oldValue instanceof List) {
            Object result = new ArrayList((List) oldValue);
            result.addAll(this.objects);
            return result;
        }
        throw new IllegalArgumentException("Operation is invalid after previous operation.");
    }
}
