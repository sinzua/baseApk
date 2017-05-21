package com.parse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class ParseAddUniqueOperation implements ParseFieldOperation {
    protected final LinkedHashSet<Object> objects = new LinkedHashSet();

    public ParseAddUniqueOperation(Collection<?> col) {
        this.objects.addAll(col);
    }

    public JSONObject encode(ParseEncoder objectEncoder) throws JSONException {
        JSONObject output = new JSONObject();
        output.put("__op", "AddUnique");
        output.put("objects", objectEncoder.encode(new ArrayList(this.objects)));
        return output;
    }

    public ParseFieldOperation mergeWithPrevious(ParseFieldOperation previous) {
        if (previous == null) {
            return this;
        }
        if (previous instanceof ParseDeleteOperation) {
            return new ParseSetOperation(this.objects);
        }
        if (previous instanceof ParseSetOperation) {
            Object value = ((ParseSetOperation) previous).getValue();
            if ((value instanceof JSONArray) || (value instanceof List)) {
                return new ParseSetOperation(apply(value, null));
            }
            throw new IllegalArgumentException("You can only add an item to a List or JSONArray.");
        } else if (previous instanceof ParseAddUniqueOperation) {
            return new ParseAddUniqueOperation((List) apply(new ArrayList(((ParseAddUniqueOperation) previous).objects), null));
        } else {
            throw new IllegalArgumentException("Operation is invalid after previous operation.");
        }
    }

    public Object apply(Object oldValue, String key) {
        if (oldValue == null) {
            return new ArrayList(this.objects);
        }
        if (oldValue instanceof JSONArray) {
            return new JSONArray((ArrayList) apply(ParseFieldOperations.jsonArrayAsArrayList((JSONArray) oldValue), key));
        }
        if (oldValue instanceof List) {
            Object result = new ArrayList((List) oldValue);
            HashMap<String, Integer> existingObjectIds = new HashMap();
            for (int i = 0; i < result.size(); i++) {
                if (result.get(i) instanceof ParseObject) {
                    existingObjectIds.put(((ParseObject) result.get(i)).getObjectId(), Integer.valueOf(i));
                }
            }
            Iterator i$ = this.objects.iterator();
            while (i$.hasNext()) {
                Object obj = i$.next();
                if (obj instanceof ParseObject) {
                    String objectId = ((ParseObject) obj).getObjectId();
                    if (objectId != null && existingObjectIds.containsKey(objectId)) {
                        result.set(((Integer) existingObjectIds.get(objectId)).intValue(), obj);
                    } else if (!result.contains(obj)) {
                        result.add(obj);
                    }
                } else if (!result.contains(obj)) {
                    result.add(obj);
                }
            }
            return result;
        }
        throw new IllegalArgumentException("Operation is invalid after previous operation.");
    }
}
