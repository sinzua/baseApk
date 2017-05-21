package com.parse;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class ParseRelationOperation<T extends ParseObject> implements ParseFieldOperation {
    private final Set<ParseObject> relationsToAdd;
    private final Set<ParseObject> relationsToRemove;
    private final String targetClass;

    ParseRelationOperation(Set<T> newRelationsToAdd, Set<T> newRelationsToRemove) {
        String targetClass = null;
        this.relationsToAdd = new HashSet();
        this.relationsToRemove = new HashSet();
        if (newRelationsToAdd != null) {
            for (T object : newRelationsToAdd) {
                addParseObjectToSet(object, this.relationsToAdd);
                if (targetClass == null) {
                    targetClass = object.getClassName();
                } else if (!targetClass.equals(object.getClassName())) {
                    throw new IllegalArgumentException("All objects in a relation must be of the same class.");
                }
            }
        }
        if (newRelationsToRemove != null) {
            for (T object2 : newRelationsToRemove) {
                addParseObjectToSet(object2, this.relationsToRemove);
                if (targetClass == null) {
                    targetClass = object2.getClassName();
                } else if (!targetClass.equals(object2.getClassName())) {
                    throw new IllegalArgumentException("All objects in a relation must be of the same class.");
                }
            }
        }
        if (targetClass == null) {
            throw new IllegalArgumentException("Cannot create a ParseRelationOperation with no objects.");
        }
        this.targetClass = targetClass;
    }

    private ParseRelationOperation(String newTargetClass, Set<ParseObject> newRelationsToAdd, Set<ParseObject> newRelationsToRemove) {
        this.targetClass = newTargetClass;
        this.relationsToAdd = new HashSet(newRelationsToAdd);
        this.relationsToRemove = new HashSet(newRelationsToRemove);
    }

    private void addParseObjectToSet(ParseObject obj, Set<ParseObject> set) {
        if (Parse.getLocalDatastore() != null || obj.getObjectId() == null) {
            set.add(obj);
            return;
        }
        for (ParseObject existingObject : set) {
            if (obj.getObjectId().equals(existingObject.getObjectId())) {
                set.remove(existingObject);
            }
        }
        set.add(obj);
    }

    private void addAllParseObjectsToSet(Collection<ParseObject> list, Set<ParseObject> set) {
        for (ParseObject obj : list) {
            addParseObjectToSet(obj, set);
        }
    }

    private void removeParseObjectFromSet(ParseObject obj, Set<ParseObject> set) {
        if (Parse.getLocalDatastore() != null || obj.getObjectId() == null) {
            set.remove(obj);
            return;
        }
        for (ParseObject existingObject : set) {
            if (obj.getObjectId().equals(existingObject.getObjectId())) {
                set.remove(existingObject);
            }
        }
    }

    private void removeAllParseObjectsFromSet(Collection<ParseObject> list, Set<ParseObject> set) {
        for (ParseObject obj : list) {
            removeParseObjectFromSet(obj, set);
        }
    }

    String getTargetClass() {
        return this.targetClass;
    }

    JSONArray convertSetToArray(Set<ParseObject> set, ParseEncoder objectEncoder) throws JSONException {
        JSONArray array = new JSONArray();
        for (ParseObject obj : set) {
            array.put(objectEncoder.encode(obj));
        }
        return array;
    }

    public JSONObject encode(ParseEncoder objectEncoder) throws JSONException {
        JSONObject adds = null;
        JSONObject removes = null;
        if (this.relationsToAdd.size() > 0) {
            adds = new JSONObject();
            adds.put("__op", "AddRelation");
            adds.put("objects", convertSetToArray(this.relationsToAdd, objectEncoder));
        }
        if (this.relationsToRemove.size() > 0) {
            removes = new JSONObject();
            removes.put("__op", "RemoveRelation");
            removes.put("objects", convertSetToArray(this.relationsToRemove, objectEncoder));
        }
        if (adds != null && removes != null) {
            JSONObject result = new JSONObject();
            result.put("__op", "Batch");
            JSONArray ops = new JSONArray();
            ops.put(adds);
            ops.put(removes);
            result.put("ops", ops);
            return result;
        } else if (adds != null) {
            return adds;
        } else {
            if (removes != null) {
                return removes;
            }
            throw new IllegalArgumentException("A ParseRelationOperation was created without any data.");
        }
    }

    public ParseFieldOperation mergeWithPrevious(ParseFieldOperation previous) {
        if (previous == null) {
            return this;
        }
        if (previous instanceof ParseDeleteOperation) {
            throw new IllegalArgumentException("You can't modify a relation after deleting it.");
        } else if (previous instanceof ParseRelationOperation) {
            ParseRelationOperation<T> previousOperation = (ParseRelationOperation) previous;
            if (previousOperation.targetClass == null || previousOperation.targetClass.equals(this.targetClass)) {
                Set<ParseObject> newRelationsToAdd = new HashSet(previousOperation.relationsToAdd);
                Set<ParseObject> newRelationsToRemove = new HashSet(previousOperation.relationsToRemove);
                if (this.relationsToAdd != null) {
                    addAllParseObjectsToSet(this.relationsToAdd, newRelationsToAdd);
                    removeAllParseObjectsFromSet(this.relationsToAdd, newRelationsToRemove);
                }
                if (this.relationsToRemove != null) {
                    removeAllParseObjectsFromSet(this.relationsToRemove, newRelationsToAdd);
                    addAllParseObjectsToSet(this.relationsToRemove, newRelationsToRemove);
                }
                return new ParseRelationOperation(this.targetClass, newRelationsToAdd, newRelationsToRemove);
            }
            throw new IllegalArgumentException("Related object object must be of class " + previousOperation.targetClass + ", but " + this.targetClass + " was passed in.");
        } else {
            throw new IllegalArgumentException("Operation is invalid after previous operation.");
        }
    }

    public Object apply(Object oldValue, String key) {
        ParseRelation<T> relation;
        if (oldValue == null) {
            relation = new ParseRelation(this.targetClass);
        } else if (oldValue instanceof ParseRelation) {
            relation = (ParseRelation) oldValue;
            if (!(this.targetClass == null || this.targetClass.equals(relation.getTargetClass()))) {
                throw new IllegalArgumentException("Related object object must be of class " + relation.getTargetClass() + ", but " + this.targetClass + " was passed in.");
            }
        } else {
            throw new IllegalArgumentException("Operation is invalid after previous operation.");
        }
        for (ParseObject relationToAdd : this.relationsToAdd) {
            relation.addKnownObject(relationToAdd);
        }
        for (ParseObject relationToRemove : this.relationsToRemove) {
            relation.removeKnownObject(relationToRemove);
        }
        return relation;
    }
}
