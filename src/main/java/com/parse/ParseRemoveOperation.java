package com.parse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class ParseRemoveOperation implements ParseFieldOperation {
    protected final HashSet<Object> objects = new HashSet();

    public ParseRemoveOperation(Collection<?> coll) {
        this.objects.addAll(coll);
    }

    public JSONObject encode(ParseEncoder objectEncoder) throws JSONException {
        JSONObject output = new JSONObject();
        output.put("__op", "Remove");
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
        } else if (previous instanceof ParseRemoveOperation) {
            HashSet<Object> result = new HashSet(((ParseRemoveOperation) previous).objects);
            result.addAll(this.objects);
            this(result);
            return this;
        } else {
            throw new IllegalArgumentException("Operation is invalid after previous operation.");
        }
    }

    public Object apply(Object oldValue, String key) {
        if (oldValue == null) {
            return new ArrayList();
        }
        if (oldValue instanceof JSONArray) {
            return new JSONArray((ArrayList) apply(ParseFieldOperations.jsonArrayAsArrayList((JSONArray) oldValue), key));
        }
        if (oldValue instanceof List) {
            Object obj;
            Object result = new ArrayList((List) oldValue);
            result.removeAll(this.objects);
            ArrayList<Object> objectsToBeRemoved = new ArrayList(this.objects);
            objectsToBeRemoved.removeAll(result);
            HashSet<String> objectIds = new HashSet();
            Iterator i$ = objectsToBeRemoved.iterator();
            while (i$.hasNext()) {
                obj = i$.next();
                if (obj instanceof ParseObject) {
                    objectIds.add(((ParseObject) obj).getObjectId());
                }
            }
            Iterator<Object> resultIterator = result.iterator();
            while (resultIterator.hasNext()) {
                obj = resultIterator.next();
                if ((obj instanceof ParseObject) && objectIds.contains(((ParseObject) obj).getObjectId())) {
                    resultIterator.remove();
                }
            }
            return result;
        }
        throw new IllegalArgumentException("Operation is invalid after previous operation.");
    }
}
