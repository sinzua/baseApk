package com.parse;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseRelation<T extends ParseObject> {
    private String key;
    private Set<ParseObject> knownObjects;
    private final Object mutex;
    private WeakReference<ParseObject> parent;
    private String parentClassName;
    private String parentObjectId;
    private String targetClass;

    ParseRelation(ParseObject parent, String key) {
        this.mutex = new Object();
        this.knownObjects = new HashSet();
        this.parent = new WeakReference(parent);
        this.parentObjectId = parent.getObjectId();
        this.parentClassName = parent.getClassName();
        this.key = key;
        this.targetClass = null;
    }

    ParseRelation(String targetClass) {
        this.mutex = new Object();
        this.knownObjects = new HashSet();
        this.parent = null;
        this.parentObjectId = null;
        this.parentClassName = null;
        this.key = null;
        this.targetClass = targetClass;
    }

    ParseRelation(JSONObject jsonObject, ParseDecoder decoder) {
        this.mutex = new Object();
        this.knownObjects = new HashSet();
        this.parent = null;
        this.parentObjectId = null;
        this.parentClassName = null;
        this.key = null;
        this.targetClass = jsonObject.optString("className", null);
        JSONArray objectsArray = jsonObject.optJSONArray("objects");
        if (objectsArray != null) {
            for (int i = 0; i < objectsArray.length(); i++) {
                this.knownObjects.add((ParseObject) decoder.decode(objectsArray.optJSONObject(i)));
            }
        }
    }

    void ensureParentAndKey(ParseObject someParent, String someKey) {
        synchronized (this.mutex) {
            if (this.parent == null) {
                this.parent = new WeakReference(someParent);
                this.parentObjectId = someParent.getObjectId();
                this.parentClassName = someParent.getClassName();
            }
            if (this.key == null) {
                this.key = someKey;
            }
            if (this.parent.get() != someParent) {
                throw new IllegalStateException("Internal error. One ParseRelation retrieved from two different ParseObjects.");
            } else if (this.key.equals(someKey)) {
            } else {
                throw new IllegalStateException("Internal error. One ParseRelation retrieved from two different keys.");
            }
        }
    }

    public void add(T object) {
        synchronized (this.mutex) {
            ParseRelationOperation<T> operation = new ParseRelationOperation(Collections.singleton(object), null);
            this.targetClass = operation.getTargetClass();
            getParent().performOperation(this.key, operation);
            this.knownObjects.add(object);
        }
    }

    public void remove(T object) {
        synchronized (this.mutex) {
            ParseRelationOperation<T> operation = new ParseRelationOperation(null, Collections.singleton(object));
            this.targetClass = operation.getTargetClass();
            getParent().performOperation(this.key, operation);
            this.knownObjects.remove(object);
        }
    }

    public ParseQuery<T> getQuery() {
        ParseQuery<T> parseQuery;
        synchronized (this.mutex) {
            Builder builder;
            if (this.targetClass == null) {
                builder = new Builder(this.parentClassName).redirectClassNameForKey(this.key);
            } else {
                builder = new Builder(this.targetClass);
            }
            builder.whereRelatedTo(getParent(), this.key);
            parseQuery = new ParseQuery(builder);
        }
        return parseQuery;
    }

    JSONObject encodeToJSON(ParseEncoder objectEncoder) throws JSONException {
        JSONObject relation;
        synchronized (this.mutex) {
            relation = new JSONObject();
            relation.put("__type", "Relation");
            relation.put("className", this.targetClass);
            JSONArray knownObjectsArray = new JSONArray();
            for (ParseObject knownObject : this.knownObjects) {
                try {
                    knownObjectsArray.put(objectEncoder.encodeRelatedObject(knownObject));
                } catch (Exception e) {
                }
            }
            relation.put("objects", knownObjectsArray);
        }
        return relation;
    }

    String getTargetClass() {
        String str;
        synchronized (this.mutex) {
            str = this.targetClass;
        }
        return str;
    }

    void setTargetClass(String className) {
        synchronized (this.mutex) {
            this.targetClass = className;
        }
    }

    void addKnownObject(ParseObject object) {
        synchronized (this.mutex) {
            this.knownObjects.add(object);
        }
    }

    void removeKnownObject(ParseObject object) {
        synchronized (this.mutex) {
            this.knownObjects.remove(object);
        }
    }

    boolean hasKnownObject(ParseObject object) {
        boolean contains;
        synchronized (this.mutex) {
            contains = this.knownObjects.contains(object);
        }
        return contains;
    }

    ParseObject getParent() {
        if (this.parent == null) {
            return null;
        }
        if (this.parent.get() == null) {
            return ParseObject.createWithoutData(this.parentClassName, this.parentObjectId);
        }
        return (ParseObject) this.parent.get();
    }

    String getKey() {
        return this.key;
    }

    Set<ParseObject> getKnownObjects() {
        return this.knownObjects;
    }
}
