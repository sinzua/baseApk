package com.parse;

import bolts.Capture;
import bolts.Continuation;
import bolts.Task;
import bolts.Task.TaskCompletionSource;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseObject {
    private static final String AUTO_CLASS_NAME = "_Automatic";
    public static final String DEFAULT_PIN = "_default";
    private static final String KEY_ACL = "ACL";
    private static final String KEY_CLASS_NAME = "className";
    private static final String KEY_COMPLETE = "__complete";
    private static final String KEY_CREATED_AT = "createdAt";
    static final String KEY_IS_DELETING_EVENTUALLY = "__isDeletingEventually";
    private static final String KEY_IS_DELETING_EVENTUALLY_OLD = "isDeletingEventually";
    private static final String KEY_OBJECT_ID = "objectId";
    private static final String KEY_OPERATIONS = "__operations";
    private static final String KEY_UPDATED_AT = "updatedAt";
    private static final String NEW_OFFLINE_OBJECT_ID_PLACEHOLDER = "*** Offline Object ***";
    static final String VERSION_NAME = "1.10.1";
    private static final Map<Class<? extends ParseObject>, String> classNames = new ConcurrentHashMap();
    private static final ThreadLocal<String> isCreatingPointerForObjectId = new ThreadLocal<String>() {
        protected String initialValue() {
            return null;
        }
    };
    private static final Map<String, Class<? extends ParseObject>> objectTypes = new ConcurrentHashMap();
    static String server = "https://api.parse.com";
    private final Map<String, Boolean> dataAvailability;
    private final Map<String, Object> estimatedData;
    private final Map<Object, ParseJSONCacheItem> hashedObjects;
    boolean isDeleted;
    int isDeletingEventually;
    private String localId;
    final Object mutex;
    final LinkedList<ParseOperationSet> operationSetQueue;
    private final ParseMulticastDelegate<ParseObject> saveEvent;
    private State state;
    final TaskQueue taskQueue;

    static class State {
        private final String className;
        private final long createdAt;
        private final boolean isComplete;
        private final String objectId;
        private final Map<String, Object> serverData;
        private final long updatedAt;

        static abstract class Init<T extends Init> {
            private final String className;
            private long createdAt = -1;
            private boolean isComplete;
            private String objectId;
            Map<String, Object> serverData = new HashMap();
            private long updatedAt = -1;

            abstract <S extends State> S build();

            abstract T self();

            public Init(String className) {
                this.className = className;
            }

            Init(State state) {
                this.className = state.className();
                this.objectId = state.objectId();
                this.createdAt = state.createdAt();
                this.updatedAt = state.updatedAt();
                for (String key : state.keySet()) {
                    this.serverData.put(key, state.get(key));
                }
                this.isComplete = state.isComplete();
            }

            public T objectId(String objectId) {
                this.objectId = objectId;
                return self();
            }

            public T createdAt(Date createdAt) {
                this.createdAt = createdAt.getTime();
                return self();
            }

            public T createdAt(long createdAt) {
                this.createdAt = createdAt;
                return self();
            }

            public T updatedAt(Date updatedAt) {
                this.updatedAt = updatedAt.getTime();
                return self();
            }

            public T updatedAt(long updatedAt) {
                this.updatedAt = updatedAt;
                return self();
            }

            public T isComplete(boolean complete) {
                this.isComplete = complete;
                return self();
            }

            public T put(String key, Object value) {
                this.serverData.put(key, value);
                return self();
            }

            public T remove(String key) {
                this.serverData.remove(key);
                return self();
            }

            public T clear() {
                this.objectId = null;
                this.createdAt = -1;
                this.updatedAt = -1;
                this.isComplete = false;
                this.serverData.clear();
                return self();
            }

            public T apply(State other) {
                if (other.objectId() != null) {
                    objectId(other.objectId());
                }
                if (other.createdAt() > 0) {
                    createdAt(other.createdAt());
                }
                if (other.updatedAt() > 0) {
                    updatedAt(other.updatedAt());
                }
                boolean z = this.isComplete || other.isComplete();
                isComplete(z);
                for (String key : other.keySet()) {
                    put(key, other.get(key));
                }
                return self();
            }

            public T apply(ParseOperationSet operations) {
                for (String key : operations.keySet()) {
                    Object newValue = ((ParseFieldOperation) operations.get(key)).apply(this.serverData.get(key), key);
                    if (newValue != null) {
                        put(key, newValue);
                    } else {
                        remove(key);
                    }
                }
                return self();
            }
        }

        static class Builder extends Init<Builder> {
            public Builder(String className) {
                super(className);
            }

            public Builder(State state) {
                super(state);
            }

            Builder self() {
                return this;
            }

            public State build() {
                return new State(this);
            }
        }

        public static Init<?> newBuilder(String className) {
            if ("_User".equals(className)) {
                return new Builder();
            }
            return new Builder(className);
        }

        State(Init<?> builder) {
            this.className = builder.className;
            this.objectId = builder.objectId;
            this.createdAt = builder.createdAt;
            this.updatedAt = builder.updatedAt > 0 ? builder.updatedAt : this.createdAt;
            this.serverData = Collections.unmodifiableMap(new HashMap(builder.serverData));
            this.isComplete = builder.isComplete;
        }

        public <T extends Init<?>> T newBuilder() {
            return new Builder(this);
        }

        public String className() {
            return this.className;
        }

        public String objectId() {
            return this.objectId;
        }

        public long createdAt() {
            return this.createdAt;
        }

        public long updatedAt() {
            return this.updatedAt;
        }

        public boolean isComplete() {
            return this.isComplete;
        }

        public Object get(String key) {
            return this.serverData.get(key);
        }

        public Set<String> keySet() {
            return this.serverData.keySet();
        }

        public String toString() {
            return String.format(Locale.US, "%s@%s[className=%s, objectId=%s, createdAt=%d, updatedAt=%d, isComplete=%s, serverData=%s]", new Object[]{getClass().getName(), Integer.toHexString(hashCode()), this.className, this.objectId, Long.valueOf(this.createdAt), Long.valueOf(this.updatedAt), Boolean.valueOf(this.isComplete), this.serverData});
        }
    }

    private static ParseObjectController getObjectController() {
        return ParseCorePlugins.getInstance().getObjectController();
    }

    private static LocalIdManager getLocalIdManager() {
        return ParseCorePlugins.getInstance().getLocalIdManager();
    }

    protected ParseObject() {
        this(AUTO_CLASS_NAME);
    }

    public ParseObject(String theClassName) {
        this.mutex = new Object();
        this.taskQueue = new TaskQueue();
        this.saveEvent = new ParseMulticastDelegate();
        String objectIdForPointer = (String) isCreatingPointerForObjectId.get();
        if (theClassName == null) {
            throw new IllegalArgumentException("You must specify a Parse class name when creating a new ParseObject.");
        }
        if (AUTO_CLASS_NAME.equals(theClassName)) {
            theClassName = getClassName(getClass());
        }
        if (getClass().equals(ParseObject.class) && objectTypes.containsKey(theClassName) && !((Class) objectTypes.get(theClassName)).isInstance(this)) {
            throw new IllegalArgumentException("You must create this type of ParseObject using ParseObject.create() or the proper subclass.");
        } else if (getClass().equals(ParseObject.class) || getClass().equals(objectTypes.get(theClassName))) {
            this.operationSetQueue = new LinkedList();
            this.operationSetQueue.add(new ParseOperationSet());
            this.estimatedData = new HashMap();
            this.hashedObjects = new IdentityHashMap();
            this.dataAvailability = new HashMap();
            Init<?> builder = newStateBuilder(theClassName);
            if (objectIdForPointer == null) {
                setDefaultValues();
                builder.isComplete(true);
            } else {
                if (!objectIdForPointer.equals(NEW_OFFLINE_OBJECT_ID_PLACEHOLDER)) {
                    builder.objectId(objectIdForPointer);
                }
                builder.isComplete(false);
            }
            this.state = builder.build();
            OfflineStore store = Parse.getLocalDatastore();
            if (store != null) {
                store.registerNewObject(this);
            }
        } else {
            throw new IllegalArgumentException("You must register this ParseObject subclass before instantiating it.");
        }
    }

    public static ParseObject create(String className) {
        if (!objectTypes.containsKey(className)) {
            return new ParseObject(className);
        }
        try {
            return (ParseObject) ((Class) objectTypes.get(className)).newInstance();
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw ((RuntimeException) e);
            }
            throw new RuntimeException("Failed to create instance of subclass.", e);
        }
    }

    public static <T extends ParseObject> T create(Class<T> subclass) {
        return create(getClassName(subclass));
    }

    public static ParseObject createWithoutData(String className, String objectId) {
        OfflineStore store = Parse.getLocalDatastore();
        if (objectId == null) {
            try {
                isCreatingPointerForObjectId.set(NEW_OFFLINE_OBJECT_ID_PLACEHOLDER);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e2) {
                throw new RuntimeException("Failed to create instance of subclass.", e2);
            } catch (Throwable th) {
                isCreatingPointerForObjectId.set(null);
            }
        } else {
            isCreatingPointerForObjectId.set(objectId);
        }
        ParseObject object = null;
        if (!(store == null || objectId == null)) {
            object = store.getObject(className, objectId);
        }
        if (object == null) {
            object = create(className);
            if (object.hasChanges()) {
                throw new IllegalStateException("A ParseObject subclass default constructor must not make changes to the object that cause it to be dirty.");
            }
        }
        isCreatingPointerForObjectId.set(null);
        return object;
    }

    public static <T extends ParseObject> T createWithoutData(Class<T> subclass, String objectId) {
        return createWithoutData(getClassName(subclass), objectId);
    }

    private static boolean isAccessible(Member m) {
        return Modifier.isPublic(m.getModifiers()) || !(!m.getDeclaringClass().getPackage().getName().equals("com.parse") || Modifier.isPrivate(m.getModifiers()) || Modifier.isProtected(m.getModifiers()));
    }

    public static void registerSubclass(Class<? extends ParseObject> subclass) {
        String className = getClassName(subclass);
        if (className == null) {
            throw new IllegalArgumentException("No ParseClassName annotation provided on " + subclass);
        }
        if (subclass.getDeclaredConstructors().length > 0) {
            try {
                if (!isAccessible(subclass.getDeclaredConstructor(new Class[0]))) {
                    throw new IllegalArgumentException("Default constructor for " + subclass + " is not accessible.");
                }
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException("No default constructor provided for " + subclass);
            }
        }
        Class<? extends ParseObject> oldValue = (Class) objectTypes.get(className);
        if (oldValue == null || !subclass.isAssignableFrom(oldValue)) {
            objectTypes.put(className, subclass);
            if (oldValue != null && !subclass.equals(oldValue)) {
                if (className.equals(getClassName(ParseUser.class))) {
                    ParseUser.getCurrentUserController().clearFromMemory();
                } else if (className.equals(getClassName(ParseInstallation.class))) {
                    ParseInstallation.getCurrentInstallationController().clearFromMemory();
                }
            }
        }
    }

    static void unregisterSubclass(Class<? extends ParseObject> subclass) {
        unregisterSubclass(getClassName(subclass));
    }

    static void unregisterSubclass(String className) {
        objectTypes.remove(className);
    }

    static <T> Task<T> enqueueForAll(List<? extends ParseObject> objects, Continuation<Void, Task<T>> taskStart) {
        final TaskCompletionSource readyToStart = Task.create();
        List<Lock> locks = new ArrayList(objects.size());
        for (ParseObject obj : objects) {
            locks.add(obj.taskQueue.getLock());
        }
        LockSet lock = new LockSet(locks);
        lock.lock();
        try {
            final Task<T> fullTask = (Task) taskStart.then(readyToStart.getTask());
            final List<Task<Void>> childTasks = new ArrayList();
            for (ParseObject obj2 : objects) {
                obj2.taskQueue.enqueue(new Continuation<Void, Task<T>>() {
                    public Task<T> then(Task<Void> task) throws Exception {
                        childTasks.add(task);
                        return fullTask;
                    }
                });
            }
            Task.whenAll(childTasks).continueWith(new Continuation<Void, Void>() {
                public Void then(Task<Void> task) throws Exception {
                    readyToStart.setResult(null);
                    return null;
                }
            });
            lock.unlock();
            return fullTask;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        } catch (Throwable th) {
            lock.unlock();
        }
    }

    private void addToHashedObjects(Object object) {
        synchronized (this.mutex) {
            try {
                this.hashedObjects.put(object, new ParseJSONCacheItem(object));
            } catch (JSONException e) {
                throw new IllegalArgumentException("Couldn't serialize container value to JSON.");
            }
        }
    }

    static <T extends ParseObject> T from(State state) {
        T object = createWithoutData(state.className(), state.objectId());
        synchronized (object.mutex) {
            State newState;
            if (state.isComplete()) {
                newState = state;
            } else {
                newState = object.getState().newBuilder().apply(state).build();
            }
            object.setState(newState);
        }
        return object;
    }

    static <T extends ParseObject> T fromJSON(JSONObject json, String defaultClassName, boolean isComplete) {
        return fromJSON(json, defaultClassName, isComplete, ParseDecoder.get());
    }

    static <T extends ParseObject> T fromJSON(JSONObject json, String defaultClassName, boolean isComplete, ParseDecoder decoder) {
        String className = json.optString(KEY_CLASS_NAME, defaultClassName);
        if (className == null) {
            return null;
        }
        T object = createWithoutData(className, json.optString(KEY_OBJECT_ID, null));
        object.setState(object.mergeFromServer(object.getState(), json, decoder, isComplete));
        return object;
    }

    static <T extends ParseObject> T fromJSONPayload(JSONObject json, ParseDecoder decoder) {
        String className = json.optString(KEY_CLASS_NAME);
        if (className == null || ParseTextUtils.isEmpty(className)) {
            return null;
        }
        T object = createWithoutData(className, json.optString(KEY_OBJECT_ID, null));
        object.build(json, decoder);
        return object;
    }

    Init<?> newStateBuilder(String className) {
        return new Builder(className);
    }

    State getState() {
        State state;
        synchronized (this.mutex) {
            state = this.state;
        }
        return state;
    }

    void setState(State newState) {
        synchronized (this.mutex) {
            setState(newState, true);
        }
    }

    private void setState(State newState, boolean notifyIfObjectIdChanges) {
        synchronized (this.mutex) {
            String oldObjectId = this.state.objectId();
            String newObjectId = newState.objectId();
            this.state = newState;
            if (notifyIfObjectIdChanges && !ParseTextUtils.equals(oldObjectId, newObjectId)) {
                notifyObjectIdChanged(oldObjectId, newObjectId);
            }
            rebuildEstimatedData();
            rebuildDataAvailability();
            checkpointAllMutableContainers();
        }
    }

    public String getClassName() {
        String className;
        synchronized (this.mutex) {
            className = this.state.className();
        }
        return className;
    }

    public Date getUpdatedAt() {
        long updatedAt = getState().updatedAt();
        return updatedAt > 0 ? new Date(updatedAt) : null;
    }

    public Date getCreatedAt() {
        long createdAt = getState().createdAt();
        return createdAt > 0 ? new Date(createdAt) : null;
    }

    public Set<String> keySet() {
        Set<String> unmodifiableSet;
        synchronized (this.mutex) {
            unmodifiableSet = Collections.unmodifiableSet(this.estimatedData.keySet());
        }
        return unmodifiableSet;
    }

    void copyChangesFrom(ParseObject other) {
        synchronized (this.mutex) {
            ParseOperationSet operations = (ParseOperationSet) other.operationSetQueue.getFirst();
            for (String key : operations.keySet()) {
                performOperation(key, (ParseFieldOperation) operations.get(key));
            }
        }
    }

    void mergeFromObject(ParseObject other) {
        synchronized (this.mutex) {
            if (this == other) {
                return;
            }
            setState(other.getState().newBuilder().build(), false);
        }
    }

    void revert(String key) {
        synchronized (this.mutex) {
            currentOperations().remove(key);
            rebuildEstimatedData();
            rebuildDataAvailability();
            checkpointAllMutableContainers();
        }
    }

    void revert() {
        synchronized (this.mutex) {
            currentOperations().clear();
            rebuildEstimatedData();
            rebuildDataAvailability();
            checkpointAllMutableContainers();
        }
    }

    private Map<String, ParseObject> collectFetchedObjects() {
        final Map<String, ParseObject> fetchedObjects = new HashMap();
        new ParseTraverser() {
            protected boolean visit(Object object) {
                if (object instanceof ParseObject) {
                    ParseObject parseObj = (ParseObject) object;
                    State state = parseObj.getState();
                    if (state.objectId() != null && state.isComplete()) {
                        fetchedObjects.put(state.objectId(), parseObj);
                    }
                }
                return true;
            }
        }.traverse(this.estimatedData);
        return fetchedObjects;
    }

    void build(JSONObject json, ParseDecoder decoder) {
        try {
            Builder builder = (Builder) new Builder(this.state).isComplete(true);
            builder.clear();
            Iterator<?> keys = json.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (!key.equals(KEY_CLASS_NAME)) {
                    if (key.equals(KEY_OBJECT_ID)) {
                        builder.objectId(json.getString(key));
                    } else if (key.equals(KEY_CREATED_AT)) {
                        builder.createdAt(ParseDateFormat.getInstance().parse(json.getString(key)));
                    } else if (key.equals(KEY_UPDATED_AT)) {
                        builder.updatedAt(ParseDateFormat.getInstance().parse(json.getString(key)));
                    } else {
                        Object decodedObject = decoder.decode(json.get(key));
                        if (decodedObject instanceof ParseFieldOperation) {
                            performOperation(key, (ParseFieldOperation) decodedObject);
                        } else {
                            put(key, decodedObject);
                        }
                    }
                }
            }
            setState(builder.build());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    State mergeFromServer(State state, JSONObject json, ParseDecoder decoder, boolean completeData) {
        try {
            Init<?> builder = state.newBuilder();
            if (completeData) {
                builder.clear();
            }
            boolean z = state.isComplete() || completeData;
            builder.isComplete(z);
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
            return builder.build();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    JSONObject toRest(ParseEncoder encoder) {
        State state;
        List<ParseOperationSet> operationSetQueueCopy;
        synchronized (this.mutex) {
            state = getState();
            int operationSetQueueSize = this.operationSetQueue.size();
            operationSetQueueCopy = new ArrayList(operationSetQueueSize);
            for (int i = 0; i < operationSetQueueSize; i++) {
                operationSetQueueCopy.add(new ParseOperationSet((ParseOperationSet) this.operationSetQueue.get(i)));
            }
        }
        return toRest(state, operationSetQueueCopy, encoder);
    }

    JSONObject toRest(State state, List<ParseOperationSet> operationSetQueue, ParseEncoder objectEncoder) {
        checkForChangesToMutableContainers();
        JSONObject json = new JSONObject();
        try {
            json.put(KEY_CLASS_NAME, state.className());
            if (state.objectId() != null) {
                json.put(KEY_OBJECT_ID, state.objectId());
            }
            if (state.createdAt() > 0) {
                json.put(KEY_CREATED_AT, ParseDateFormat.getInstance().format(new Date(state.createdAt())));
            }
            if (state.updatedAt() > 0) {
                json.put(KEY_UPDATED_AT, ParseDateFormat.getInstance().format(new Date(state.updatedAt())));
            }
            for (String key : state.keySet()) {
                json.put(key, objectEncoder.encode(state.get(key)));
            }
            json.put(KEY_COMPLETE, state.isComplete());
            json.put(KEY_IS_DELETING_EVENTUALLY, this.isDeletingEventually);
            JSONArray operations = new JSONArray();
            for (ParseOperationSet operationSet : operationSetQueue) {
                operations.put(operationSet.toRest(objectEncoder));
            }
            json.put(KEY_OPERATIONS, operations);
            return json;
        } catch (JSONException e) {
            throw new RuntimeException("could not serialize object to JSON");
        }
    }

    void mergeREST(State state, JSONObject json, ParseDecoder decoder) {
        ArrayList<ParseOperationSet> saveEventuallyOperationSets = new ArrayList();
        synchronized (this.mutex) {
            try {
                boolean isComplete = json.getBoolean(KEY_COMPLETE);
                this.isDeletingEventually = ParseJSONUtils.getInt(json, Arrays.asList(new String[]{KEY_IS_DELETING_EVENTUALLY, KEY_IS_DELETING_EVENTUALLY_OLD}));
                JSONArray operations = json.getJSONArray(KEY_OPERATIONS);
                ParseOperationSet newerOperations = currentOperations();
                this.operationSetQueue.clear();
                ParseOperationSet current = null;
                for (int i = 0; i < operations.length(); i++) {
                    ParseOperationSet operationSet = ParseOperationSet.fromRest(operations.getJSONObject(i), decoder);
                    if (operationSet.isSaveEventually()) {
                        if (current != null) {
                            this.operationSetQueue.add(current);
                            current = null;
                        }
                        saveEventuallyOperationSets.add(operationSet);
                        this.operationSetQueue.add(operationSet);
                    } else {
                        if (current != null) {
                            operationSet.mergeFrom(current);
                        }
                        current = operationSet;
                    }
                }
                if (current != null) {
                    this.operationSetQueue.add(current);
                }
                currentOperations().mergeFrom(newerOperations);
                boolean mergeServerData = false;
                if (state.updatedAt() < 0) {
                    mergeServerData = true;
                } else if (json.has(KEY_UPDATED_AT)) {
                    if (new Date(state.updatedAt()).compareTo(ParseDateFormat.getInstance().parse(json.getString(KEY_UPDATED_AT))) < 0) {
                        mergeServerData = true;
                    }
                }
                if (mergeServerData) {
                    setState(mergeFromServer(state, ParseJSONUtils.create(json, Arrays.asList(new String[]{KEY_COMPLETE, KEY_IS_DELETING_EVENTUALLY, KEY_IS_DELETING_EVENTUALLY_OLD, KEY_OPERATIONS})), decoder, isComplete));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        Iterator i$ = saveEventuallyOperationSets.iterator();
        while (i$.hasNext()) {
            enqueueSaveEventuallyOperationAsync((ParseOperationSet) i$.next());
        }
    }

    private boolean hasDirtyChildren() {
        boolean z;
        synchronized (this.mutex) {
            List<ParseObject> unsavedChildren = new ArrayList();
            collectDirtyChildren(this.estimatedData, unsavedChildren, null);
            z = unsavedChildren.size() > 0;
        }
        return z;
    }

    public boolean isDirty() {
        return isDirty(true);
    }

    boolean isDirty(boolean considerChildren) {
        boolean z;
        synchronized (this.mutex) {
            checkForChangesToMutableContainers();
            z = this.isDeleted || getObjectId() == null || hasChanges() || (considerChildren && hasDirtyChildren());
        }
        return z;
    }

    boolean hasChanges() {
        boolean z;
        synchronized (this.mutex) {
            z = currentOperations().size() > 0;
        }
        return z;
    }

    boolean hasOutstandingOperations() {
        boolean z = true;
        synchronized (this.mutex) {
            if (this.operationSetQueue.size() <= 1) {
                z = false;
            }
        }
        return z;
    }

    public boolean isDirty(String key) {
        boolean containsKey;
        synchronized (this.mutex) {
            containsKey = currentOperations().containsKey(key);
        }
        return containsKey;
    }

    boolean isContainerObject(String key, Object object) {
        return (object instanceof JSONObject) || (object instanceof JSONArray) || (object instanceof Map) || (object instanceof List) || (object instanceof ParseACL) || (object instanceof ParseGeoPoint);
    }

    private void checkpointAllMutableContainers() {
        synchronized (this.mutex) {
            for (Entry<String, Object> entry : this.estimatedData.entrySet()) {
                checkpointMutableContainer((String) entry.getKey(), entry.getValue());
            }
        }
    }

    private void checkpointMutableContainer(String key, Object object) {
        synchronized (this.mutex) {
            if (isContainerObject(key, object)) {
                addToHashedObjects(object);
            }
        }
    }

    private void checkForChangesToMutableContainer(String key, Object object) {
        synchronized (this.mutex) {
            if (isContainerObject(key, object)) {
                ParseJSONCacheItem oldCacheItem = (ParseJSONCacheItem) this.hashedObjects.get(object);
                if (oldCacheItem == null) {
                    throw new IllegalArgumentException("ParseObject contains container item that isn't cached.");
                }
                try {
                    if (!oldCacheItem.equals(new ParseJSONCacheItem(object))) {
                        performOperation(key, new ParseSetOperation(object));
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            this.hashedObjects.remove(object);
        }
    }

    void checkForChangesToMutableContainers() {
        synchronized (this.mutex) {
            for (String key : this.estimatedData.keySet()) {
                checkForChangesToMutableContainer(key, this.estimatedData.get(key));
            }
            this.hashedObjects.keySet().retainAll(this.estimatedData.values());
        }
    }

    public String getObjectId() {
        String objectId;
        synchronized (this.mutex) {
            objectId = this.state.objectId();
        }
        return objectId;
    }

    public void setObjectId(String newObjectId) {
        synchronized (this.mutex) {
            String oldObjectId = this.state.objectId();
            if (ParseTextUtils.equals(oldObjectId, newObjectId)) {
                return;
            }
            this.state = this.state.newBuilder().objectId(newObjectId).build();
            notifyObjectIdChanged(oldObjectId, newObjectId);
        }
    }

    String getOrCreateLocalId() {
        String str;
        synchronized (this.mutex) {
            if (this.localId == null) {
                if (this.state.objectId() != null) {
                    throw new IllegalStateException("Attempted to get a localId for an object with an objectId.");
                }
                this.localId = getLocalIdManager().createLocalId();
            }
            str = this.localId;
        }
        return str;
    }

    private void notifyObjectIdChanged(String oldObjectId, String newObjectId) {
        synchronized (this.mutex) {
            OfflineStore store = Parse.getLocalDatastore();
            if (store != null) {
                store.updateObjectId(this, oldObjectId, newObjectId);
            }
            if (this.localId != null) {
                getLocalIdManager().setObjectId(this.localId, newObjectId);
                this.localId = null;
            }
        }
    }

    private ParseRESTObjectCommand currentSaveEventuallyCommand(ParseOperationSet operations, ParseEncoder objectEncoder, String sessionToken) throws ParseException {
        State state = getState();
        ParseRESTObjectCommand command = ParseRESTObjectCommand.saveObjectCommand(state, toJSONObjectForSaving(state, operations, objectEncoder), sessionToken);
        command.enableRetrying();
        return command;
    }

    <T extends State> JSONObject toJSONObjectForSaving(T state, ParseOperationSet operations, ParseEncoder objectEncoder) {
        JSONObject objectJSON = new JSONObject();
        try {
            for (String key : operations.keySet()) {
                objectJSON.put(key, objectEncoder.encode((ParseFieldOperation) operations.get(key)));
            }
            if (state.objectId() != null) {
                objectJSON.put(KEY_OBJECT_ID, state.objectId());
            }
            return objectJSON;
        } catch (JSONException e) {
            throw new RuntimeException("could not serialize object to JSON");
        }
    }

    Task<Void> handleSaveResultAsync(JSONObject result, ParseOperationSet operationsBeforeSave) {
        State newState = null;
        if (result != null) {
            synchronized (this.mutex) {
                newState = ParseObjectCoder.get().decode(getState().newBuilder().clear(), result, new KnownParseObjectDecoder(collectFetchedObjects())).isComplete(false).build();
            }
        }
        return handleSaveResultAsync(newState, operationsBeforeSave);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    bolts.Task<java.lang.Void> handleSaveResultAsync(final com.parse.ParseObject.State r10, final com.parse.ParseOperationSet r11) {
        /*
        r9 = this;
        r6 = 0;
        r6 = (java.lang.Void) r6;
        r4 = bolts.Task.forResult(r6);
        if (r10 == 0) goto L_0x002d;
    L_0x0009:
        r3 = 1;
    L_0x000a:
        r7 = r9.mutex;
        monitor-enter(r7);
        r6 = r9.operationSetQueue;	 Catch:{ all -> 0x005e }
        r8 = r9.operationSetQueue;	 Catch:{ all -> 0x005e }
        r8 = r8.indexOf(r11);	 Catch:{ all -> 0x005e }
        r1 = r6.listIterator(r8);	 Catch:{ all -> 0x005e }
        r1.next();	 Catch:{ all -> 0x005e }
        r1.remove();	 Catch:{ all -> 0x005e }
        r0 = r1.next();	 Catch:{ all -> 0x005e }
        r0 = (com.parse.ParseOperationSet) r0;	 Catch:{ all -> 0x005e }
        if (r3 != 0) goto L_0x002f;
    L_0x0027:
        r0.mergeFrom(r11);	 Catch:{ all -> 0x005e }
        monitor-exit(r7);	 Catch:{ all -> 0x005e }
        r5 = r4;
    L_0x002c:
        return r5;
    L_0x002d:
        r3 = 0;
        goto L_0x000a;
    L_0x002f:
        monitor-exit(r7);	 Catch:{ all -> 0x005e }
        r2 = com.parse.Parse.getLocalDatastore();
        if (r2 == 0) goto L_0x003f;
    L_0x0036:
        r6 = new com.parse.ParseObject$5;
        r6.<init>(r2);
        r4 = r4.onSuccessTask(r6);
    L_0x003f:
        r6 = new com.parse.ParseObject$6;
        r6.<init>(r10, r11);
        r4 = r4.continueWith(r6);
        if (r2 == 0) goto L_0x0053;
    L_0x004a:
        r6 = new com.parse.ParseObject$7;
        r6.<init>(r2);
        r4 = r4.onSuccessTask(r6);
    L_0x0053:
        r6 = new com.parse.ParseObject$8;
        r6.<init>();
        r4 = r4.onSuccess(r6);
        r5 = r4;
        goto L_0x002c;
    L_0x005e:
        r6 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x005e }
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.parse.ParseObject.handleSaveResultAsync(com.parse.ParseObject$State, com.parse.ParseOperationSet):bolts.Task<java.lang.Void>");
    }

    ParseOperationSet startSave() {
        ParseOperationSet currentOperations;
        synchronized (this.mutex) {
            currentOperations = currentOperations();
            this.operationSetQueue.addLast(new ParseOperationSet());
        }
        return currentOperations;
    }

    void validateSave() {
    }

    public final void save() throws ParseException {
        ParseTaskUtils.wait(saveInBackground());
    }

    public final Task<Void> saveInBackground() {
        return ParseUser.getCurrentUserAsync().onSuccessTask(new Continuation<ParseUser, Task<String>>() {
            public Task<String> then(Task<ParseUser> task) throws Exception {
                ParseUser current = (ParseUser) task.getResult();
                if (current == null) {
                    return Task.forResult(null);
                }
                if (!current.isLazy()) {
                    return Task.forResult(current.getSessionToken());
                }
                if (!ParseObject.this.isDataAvailable(ParseObject.KEY_ACL)) {
                    return Task.forResult(null);
                }
                final ParseACL acl = ParseObject.this.getACL(false);
                if (acl == null) {
                    return Task.forResult(null);
                }
                final ParseUser user = acl.getUnresolvedUser();
                if (user == null || !user.isCurrentUser()) {
                    return Task.forResult(null);
                }
                return user.saveAsync(null).onSuccess(new Continuation<Void, String>() {
                    public String then(Task<Void> task) throws Exception {
                        if (!acl.hasUnresolvedUser()) {
                            return user.getSessionToken();
                        }
                        throw new IllegalStateException("ACL has an unresolved ParseUser. Save or sign up before attempting to serialize the ACL.");
                    }
                });
            }
        }).onSuccessTask(new Continuation<String, Task<Void>>() {
            public Task<Void> then(Task<String> task) throws Exception {
                return ParseObject.this.saveAsync((String) task.getResult());
            }
        });
    }

    Task<Void> saveAsync(final String sessionToken) {
        return this.taskQueue.enqueue(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> toAwait) throws Exception {
                return ParseObject.this.saveAsync(sessionToken, (Task) toAwait);
            }
        });
    }

    Task<Void> saveAsync(final String sessionToken, Task<Void> toAwait) {
        if (!isDirty()) {
            return Task.forResult(null);
        }
        final ParseOperationSet operations;
        Task<Void> task;
        synchronized (this.mutex) {
            updateBeforeSave();
            validateSave();
            operations = startSave();
        }
        synchronized (this.mutex) {
            task = deepSaveAsync(this.estimatedData, sessionToken);
        }
        return task.onSuccessTask(TaskQueue.waitFor(toAwait)).onSuccessTask(new Continuation<Void, Task<State>>() {
            public Task<State> then(Task<Void> task) throws Exception {
                return ParseObject.getObjectController().saveAsync(ParseObject.this.getState(), operations, sessionToken, new KnownParseObjectDecoder(ParseObject.this.collectFetchedObjects()));
            }
        }).continueWithTask(new Continuation<State, Task<Void>>() {
            public Task<Void> then(final Task<State> saveTask) throws Exception {
                return ParseObject.this.handleSaveResultAsync((State) saveTask.getResult(), operations).continueWithTask(new Continuation<Void, Task<Void>>() {
                    public Task<Void> then(Task<Void> task) throws Exception {
                        return (task.isFaulted() || task.isCancelled()) ? task : saveTask.makeVoid();
                    }
                });
            }
        });
    }

    Task<JSONObject> saveAsync(ParseOperationSet operationSet, String sessionToken) throws ParseException {
        return currentSaveEventuallyCommand(operationSet, PointerEncoder.get(), sessionToken).executeAsync();
    }

    public final void saveInBackground(SaveCallback callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(saveInBackground(), (ParseCallback1) callback);
    }

    void validateSaveEventually() throws ParseException {
    }

    public final void saveEventually(SaveCallback callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(saveEventually(), (ParseCallback1) callback);
    }

    public final Task<Void> saveEventually() {
        if (isDirty()) {
            final ParseOperationSet operationSet;
            ParseRESTCommand command;
            synchronized (this.mutex) {
                updateBeforeSave();
                try {
                    validateSaveEventually();
                    List<ParseObject> unsavedChildren = new ArrayList();
                    collectDirtyChildren(this.estimatedData, unsavedChildren, null);
                    String localId = null;
                    if (getObjectId() == null) {
                        localId = getOrCreateLocalId();
                    }
                    operationSet = startSave();
                    operationSet.setIsSaveEventually(true);
                    try {
                        command = currentSaveEventuallyCommand(operationSet, PointerOrLocalIdEncoder.get(), ParseUser.getCurrentSessionToken());
                        command.setLocalId(localId);
                        command.setOperationSetUUID(operationSet.getUUID());
                        command.retainLocalIds();
                        for (ParseObject object : unsavedChildren) {
                            object.saveEventually();
                        }
                    } catch (ParseException exception) {
                        throw new IllegalStateException("Unable to saveEventually.", exception);
                    }
                } catch (ParseException e) {
                    return Task.forError(e);
                }
            }
            Task<JSONObject> runEventuallyTask = Parse.getEventuallyQueue().enqueueEventuallyAsync(command, this);
            enqueueSaveEventuallyOperationAsync(operationSet);
            command.releaseLocalIds();
            if (Parse.isLocalDatastoreEnabled()) {
                return runEventuallyTask.makeVoid();
            }
            return runEventuallyTask.onSuccessTask(new Continuation<JSONObject, Task<Void>>() {
                public Task<Void> then(Task<JSONObject> task) throws Exception {
                    return ParseObject.this.handleSaveEventuallyResultAsync((JSONObject) task.getResult(), operationSet);
                }
            });
        }
        Parse.getEventuallyQueue().fakeObjectUpdate();
        return Task.forResult(null);
    }

    private Task<Void> enqueueSaveEventuallyOperationAsync(final ParseOperationSet operationSet) {
        if (operationSet.isSaveEventually()) {
            return this.taskQueue.enqueue(new Continuation<Void, Task<Void>>() {
                public Task<Void> then(Task<Void> toAwait) throws Exception {
                    return toAwait.continueWithTask(new Continuation<Void, Task<Void>>() {
                        public Task<Void> then(Task<Void> task) throws Exception {
                            return Parse.getEventuallyQueue().waitForOperationSetAndEventuallyPin(operationSet, null).makeVoid();
                        }
                    });
                }
            });
        }
        throw new IllegalStateException("This should only be used to enqueue saveEventually operation sets");
    }

    Task<Void> handleSaveEventuallyResultAsync(JSONObject json, ParseOperationSet operationSet) {
        final boolean success = json != null;
        return handleSaveResultAsync(json, operationSet).onSuccessTask(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> task) throws Exception {
                if (success) {
                    Parse.getEventuallyQueue().notifyTestHelper(5);
                }
                return task;
            }
        });
    }

    void updateBeforeSave() {
    }

    public final void deleteEventually(DeleteCallback callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(deleteEventually(), (ParseCallback1) callback);
    }

    public final Task<Void> deleteEventually() {
        synchronized (this.mutex) {
            validateDelete();
            this.isDeletingEventually++;
            String localId = null;
            if (getObjectId() == null) {
                localId = getOrCreateLocalId();
            }
            ParseRESTCommand command = ParseRESTObjectCommand.deleteObjectCommand(getState(), ParseUser.getCurrentSessionToken());
            command.enableRetrying();
            command.setLocalId(localId);
            Task<JSONObject> runEventuallyTask = Parse.getEventuallyQueue().enqueueEventuallyAsync(command, this);
        }
        if (Parse.isLocalDatastoreEnabled()) {
            return runEventuallyTask.makeVoid();
        }
        return runEventuallyTask.onSuccessTask(new Continuation<JSONObject, Task<Void>>() {
            public Task<Void> then(Task<JSONObject> task) throws Exception {
                return ParseObject.this.handleDeleteEventuallyResultAsync();
            }
        });
    }

    Task<Void> handleDeleteEventuallyResultAsync() {
        synchronized (this.mutex) {
            this.isDeletingEventually--;
        }
        return handleDeleteResultAsync().onSuccessTask(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> task) throws Exception {
                Parse.getEventuallyQueue().notifyTestHelper(6);
                return task;
            }
        });
    }

    Task<Void> handleFetchResultAsync(final State result) {
        Task<Void> task = Task.forResult((Void) null);
        final OfflineStore store = Parse.getLocalDatastore();
        if (store != null) {
            task = task.onSuccessTask(new Continuation<Void, Task<Void>>() {
                public Task<Void> then(Task<Void> task) throws Exception {
                    return store.fetchLocallyAsync(ParseObject.this).makeVoid();
                }
            }).continueWithTask(new Continuation<Void, Task<Void>>() {
                public Task<Void> then(Task<Void> task) throws Exception {
                    if ((task.getError() instanceof ParseException) && ((ParseException) task.getError()).getCode() == ParseException.CACHE_MISS) {
                        return null;
                    }
                    return task;
                }
            });
        }
        task = task.onSuccessTask(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> task) throws Exception {
                synchronized (ParseObject.this.mutex) {
                    State newState;
                    if (result.isComplete()) {
                        newState = result;
                    } else {
                        newState = ParseObject.this.getState().newBuilder().apply(result).build();
                    }
                    ParseObject.this.setState(newState);
                }
                return null;
            }
        });
        return store != null ? task.onSuccessTask(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> task) throws Exception {
                return store.updateDataForObjectAsync(ParseObject.this);
            }
        }).continueWithTask(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> task) throws Exception {
                if ((task.getError() instanceof ParseException) && ((ParseException) task.getError()).getCode() == ParseException.CACHE_MISS) {
                    return null;
                }
                return task;
            }
        }) : task;
    }

    @Deprecated
    public final void refresh() throws ParseException {
        fetch();
    }

    @Deprecated
    public final void refreshInBackground(RefreshCallback callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(fetchInBackground(), (ParseCallback2) callback);
    }

    public <T extends ParseObject> T fetch() throws ParseException {
        return (ParseObject) ParseTaskUtils.wait(fetchInBackground());
    }

    <T extends ParseObject> Task<T> fetchAsync(final String sessionToken, Task<Void> toAwait) {
        return toAwait.onSuccessTask(new Continuation<Void, Task<State>>() {
            public Task<State> then(Task<Void> task) throws Exception {
                State state;
                Map<String, ParseObject> fetchedObjects;
                synchronized (ParseObject.this.mutex) {
                    state = ParseObject.this.getState();
                    fetchedObjects = ParseObject.this.collectFetchedObjects();
                }
                return ParseObject.getObjectController().fetchAsync(state, sessionToken, new KnownParseObjectDecoder(fetchedObjects));
            }
        }).onSuccessTask(new Continuation<State, Task<Void>>() {
            public Task<Void> then(Task<State> task) throws Exception {
                return ParseObject.this.handleFetchResultAsync((State) task.getResult());
            }
        }).onSuccess(new Continuation<Void, T>() {
            public T then(Task<Void> task) throws Exception {
                return ParseObject.this;
            }
        });
    }

    public final <T extends ParseObject> Task<T> fetchInBackground() {
        return ParseUser.getCurrentSessionTokenAsync().onSuccessTask(new Continuation<String, Task<T>>() {
            public Task<T> then(Task<String> task) throws Exception {
                final String sessionToken = (String) task.getResult();
                return ParseObject.this.taskQueue.enqueue(new Continuation<Void, Task<T>>() {
                    public Task<T> then(Task<Void> toAwait) throws Exception {
                        return ParseObject.this.fetchAsync(sessionToken, toAwait);
                    }
                });
            }
        });
    }

    public final <T extends ParseObject> void fetchInBackground(GetCallback<T> callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(fetchInBackground(), (ParseCallback2) callback);
    }

    public final <T extends ParseObject> Task<T> fetchIfNeededInBackground() {
        if (isDataAvailable()) {
            return Task.forResult(this);
        }
        return ParseUser.getCurrentSessionTokenAsync().onSuccessTask(new Continuation<String, Task<T>>() {
            public Task<T> then(Task<String> task) throws Exception {
                final String sessionToken = (String) task.getResult();
                return ParseObject.this.taskQueue.enqueue(new Continuation<Void, Task<T>>() {
                    public Task<T> then(Task<Void> toAwait) throws Exception {
                        if (ParseObject.this.isDataAvailable()) {
                            return Task.forResult(ParseObject.this);
                        }
                        return ParseObject.this.fetchAsync(sessionToken, toAwait);
                    }
                });
            }
        });
    }

    public <T extends ParseObject> T fetchIfNeeded() throws ParseException {
        return (ParseObject) ParseTaskUtils.wait(fetchIfNeededInBackground());
    }

    public final <T extends ParseObject> void fetchIfNeededInBackground(GetCallback<T> callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(fetchIfNeededInBackground(), (ParseCallback2) callback);
    }

    void validateDelete() {
    }

    private Task<Void> deleteAsync(final String sessionToken, Task<Void> toAwait) {
        validateDelete();
        return toAwait.onSuccessTask(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> task) throws Exception {
                if (ParseObject.this.state.objectId() == null) {
                    return task.cast();
                }
                return ParseObject.this.deleteAsync(sessionToken);
            }
        }).onSuccessTask(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> task) throws Exception {
                return ParseObject.this.handleDeleteResultAsync();
            }
        });
    }

    Task<Void> deleteAsync(String sessionToken) throws ParseException {
        return getObjectController().deleteAsync(getState(), sessionToken);
    }

    Task<Void> handleDeleteResultAsync() {
        Task<Void> task = Task.forResult(null);
        synchronized (this.mutex) {
            this.isDeleted = true;
        }
        final OfflineStore store = Parse.getLocalDatastore();
        return store != null ? task.continueWithTask(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> task) throws Exception {
                Task<Void> deleteDataForObjectAsync;
                synchronized (ParseObject.this.mutex) {
                    if (ParseObject.this.isDeleted) {
                        store.unregisterObject(ParseObject.this);
                        deleteDataForObjectAsync = store.deleteDataForObjectAsync(ParseObject.this);
                    } else {
                        deleteDataForObjectAsync = store.updateDataForObjectAsync(ParseObject.this);
                    }
                }
                return deleteDataForObjectAsync;
            }
        }) : task;
    }

    public final Task<Void> deleteInBackground() {
        return ParseUser.getCurrentSessionTokenAsync().onSuccessTask(new Continuation<String, Task<Void>>() {
            public Task<Void> then(Task<String> task) throws Exception {
                final String sessionToken = (String) task.getResult();
                return ParseObject.this.taskQueue.enqueue(new Continuation<Void, Task<Void>>() {
                    public Task<Void> then(Task<Void> toAwait) throws Exception {
                        return ParseObject.this.deleteAsync(sessionToken, toAwait);
                    }
                });
            }
        });
    }

    public final void delete() throws ParseException {
        ParseTaskUtils.wait(deleteInBackground());
    }

    public final void deleteInBackground(DeleteCallback callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(deleteInBackground(), (ParseCallback1) callback);
    }

    private static <T extends ParseObject> Task<Void> deleteAllAsync(List<T> objects, final String sessionToken) {
        if (objects.size() == 0) {
            return Task.forResult(null);
        }
        int objectCount = objects.size();
        final List<ParseObject> uniqueObjects = new ArrayList(objectCount);
        HashSet<String> idSet = new HashSet();
        for (int i = 0; i < objectCount; i++) {
            ParseObject obj = (ParseObject) objects.get(i);
            if (!idSet.contains(obj.getObjectId())) {
                idSet.add(obj.getObjectId());
                uniqueObjects.add(obj);
            }
        }
        return enqueueForAll(uniqueObjects, new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> toAwait) throws Exception {
                return ParseObject.deleteAllAsync(uniqueObjects, sessionToken, toAwait);
            }
        });
    }

    private static <T extends ParseObject> Task<Void> deleteAllAsync(final List<T> uniqueObjects, final String sessionToken, Task<Void> toAwait) {
        return toAwait.continueWithTask(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> task) throws Exception {
                int i;
                int objectCount = uniqueObjects.size();
                List<State> states = new ArrayList(objectCount);
                for (i = 0; i < objectCount; i++) {
                    ParseObject object = (ParseObject) uniqueObjects.get(i);
                    object.validateDelete();
                    states.add(object.getState());
                }
                List<Task<Void>> batchTasks = ParseObject.getObjectController().deleteAllAsync(states, sessionToken);
                List<Task<Void>> tasks = new ArrayList(objectCount);
                for (i = 0; i < objectCount; i++) {
                    final ParseObject object2 = (ParseObject) uniqueObjects.get(i);
                    tasks.add(((Task) batchTasks.get(i)).onSuccessTask(new Continuation<Void, Task<Void>>() {
                        public Task<Void> then(final Task<Void> batchTask) throws Exception {
                            return object2.handleDeleteResultAsync().continueWithTask(new Continuation<Void, Task<Void>>() {
                                public Task<Void> then(Task<Void> task) throws Exception {
                                    return batchTask;
                                }
                            });
                        }
                    }));
                }
                return Task.whenAll(tasks);
            }
        });
    }

    public static <T extends ParseObject> void deleteAll(List<T> objects) throws ParseException {
        ParseTaskUtils.wait(deleteAllInBackground(objects));
    }

    public static <T extends ParseObject> void deleteAllInBackground(List<T> objects, DeleteCallback callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(deleteAllInBackground(objects), (ParseCallback1) callback);
    }

    public static <T extends ParseObject> Task<Void> deleteAllInBackground(final List<T> objects) {
        return ParseUser.getCurrentSessionTokenAsync().onSuccessTask(new Continuation<String, Task<Void>>() {
            public Task<Void> then(Task<String> task) throws Exception {
                return ParseObject.deleteAllAsync(objects, (String) task.getResult());
            }
        });
    }

    private static void collectDirtyChildren(Object node, final Collection<ParseObject> dirtyChildren, final Collection<ParseFile> dirtyFiles, final Set<ParseObject> alreadySeen, final Set<ParseObject> alreadySeenNew) {
        new ParseTraverser() {
            protected boolean visit(Object node) {
                if (node instanceof ParseFile) {
                    if (dirtyFiles != null) {
                        ParseFile file = (ParseFile) node;
                        if (file.getUrl() == null) {
                            dirtyFiles.add(file);
                        }
                    }
                } else if ((node instanceof ParseObject) && dirtyChildren != null) {
                    ParseObject object = (ParseObject) node;
                    Set<ParseObject> seen = alreadySeen;
                    Set<ParseObject> seenNew = alreadySeenNew;
                    if (object.getObjectId() != null) {
                        seenNew = new HashSet();
                    } else if (seenNew.contains(object)) {
                        throw new RuntimeException("Found a circular dependency while saving.");
                    } else {
                        Set<ParseObject> seenNew2 = new HashSet(seenNew);
                        seenNew2.add(object);
                        seenNew = seenNew2;
                    }
                    if (!seen.contains(object)) {
                        Set<ParseObject> seen2 = new HashSet(seen);
                        seen2.add(object);
                        ParseObject.collectDirtyChildren(object.estimatedData, dirtyChildren, dirtyFiles, seen2, seenNew);
                        if (object.isDirty(false)) {
                            dirtyChildren.add(object);
                        }
                    }
                }
                return true;
            }
        }.setYieldRoot(true).traverse(node);
    }

    private static void collectDirtyChildren(Object node, Collection<ParseObject> dirtyChildren, Collection<ParseFile> dirtyFiles) {
        collectDirtyChildren(node, dirtyChildren, dirtyFiles, new HashSet(), new HashSet());
    }

    private boolean canBeSerialized() {
        boolean booleanValue;
        synchronized (this.mutex) {
            final Capture<Boolean> result = new Capture(Boolean.valueOf(true));
            new ParseTraverser() {
                protected boolean visit(Object value) {
                    if ((value instanceof ParseFile) && ((ParseFile) value).isDirty()) {
                        result.set(Boolean.valueOf(false));
                    }
                    if ((value instanceof ParseObject) && ((ParseObject) value).getObjectId() == null) {
                        result.set(Boolean.valueOf(false));
                    }
                    return ((Boolean) result.get()).booleanValue();
                }
            }.setYieldRoot(false).setTraverseParseObjects(true).traverse(this);
            booleanValue = ((Boolean) result.get()).booleanValue();
        }
        return booleanValue;
    }

    private static Task<Void> deepSaveAsync(Object object, String sessionToken) {
        Set<ParseObject> objects = new HashSet();
        Set<ParseFile> files = new HashSet();
        collectDirtyChildren(object, objects, files);
        Set<ParseUser> users = new HashSet();
        for (ParseObject o : objects) {
            if ((o instanceof ParseUser) && ((ParseUser) o).isLazy()) {
                users.add((ParseUser) o);
            }
        }
        objects.removeAll(users);
        final AtomicBoolean filesComplete = new AtomicBoolean(false);
        List<Task<Void>> tasks = new ArrayList();
        for (ParseFile file : files) {
            tasks.add(file.saveAsync(sessionToken, null, null));
        }
        Task<Void> filesTask = Task.whenAll(tasks).continueWith(new Continuation<Void, Void>() {
            public Void then(Task<Void> task) throws Exception {
                filesComplete.set(true);
                return null;
            }
        });
        final AtomicBoolean usersComplete = new AtomicBoolean(false);
        tasks = new ArrayList();
        for (ParseUser user : users) {
            tasks.add(user.saveAsync(sessionToken));
        }
        Task<Void> usersTask = Task.whenAll(tasks).continueWith(new Continuation<Void, Void>() {
            public Void then(Task<Void> task) throws Exception {
                usersComplete.set(true);
                return null;
            }
        });
        final Capture<Set<ParseObject>> remaining = new Capture(objects);
        final String str = sessionToken;
        Task<Void> objectsTask = Task.forResult(null).continueWhile(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return Boolean.valueOf(((Set) remaining.get()).size() > 0);
            }
        }, new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> task) throws Exception {
                final List<ParseObject> current = new ArrayList();
                Set<ParseObject> nextBatch = new HashSet();
                for (ParseObject obj : (Set) remaining.get()) {
                    if (obj.canBeSerialized()) {
                        current.add(obj);
                    } else {
                        nextBatch.add(obj);
                    }
                }
                remaining.set(nextBatch);
                if (current.size() == 0 && filesComplete.get() && usersComplete.get()) {
                    throw new RuntimeException("Unable to save a ParseObject with a relation to a cycle.");
                } else if (current.size() == 0) {
                    return Task.forResult(null);
                } else {
                    return ParseObject.enqueueForAll(current, new Continuation<Void, Task<Void>>() {
                        public Task<Void> then(Task<Void> toAwait) throws Exception {
                            return ParseObject.saveAllAsync(current, str, toAwait);
                        }
                    });
                }
            }
        });
        return Task.whenAll(Arrays.asList(new Task[]{filesTask, usersTask, objectsTask}));
    }

    private static <T extends ParseObject> Task<Void> saveAllAsync(final List<T> uniqueObjects, final String sessionToken, Task<Void> toAwait) {
        return toAwait.continueWithTask(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> task) throws Exception {
                int i;
                int objectCount = uniqueObjects.size();
                List<State> states = new ArrayList(objectCount);
                List<ParseOperationSet> operationsList = new ArrayList(objectCount);
                List<ParseDecoder> decoders = new ArrayList(objectCount);
                for (i = 0; i < objectCount; i++) {
                    ParseObject object = (ParseObject) uniqueObjects.get(i);
                    object.updateBeforeSave();
                    object.validateSave();
                    states.add(object.getState());
                    operationsList.add(object.startSave());
                    decoders.add(new KnownParseObjectDecoder(object.collectFetchedObjects()));
                }
                List<Task<State>> batchTasks = ParseObject.getObjectController().saveAllAsync(states, operationsList, sessionToken, decoders);
                List<Task<Void>> tasks = new ArrayList(objectCount);
                for (i = 0; i < objectCount; i++) {
                    final ParseObject object2 = (ParseObject) uniqueObjects.get(i);
                    final ParseOperationSet operations = (ParseOperationSet) operationsList.get(i);
                    tasks.add(((Task) batchTasks.get(i)).continueWithTask(new Continuation<State, Task<Void>>() {
                        public Task<Void> then(final Task<State> batchTask) throws Exception {
                            return object2.handleSaveResultAsync((State) batchTask.getResult(), operations).continueWithTask(new Continuation<Void, Task<Void>>() {
                                public Task<Void> then(Task<Void> task) throws Exception {
                                    return (task.isFaulted() || task.isCancelled()) ? task : batchTask.makeVoid();
                                }
                            });
                        }
                    }));
                }
                return Task.whenAll(tasks);
            }
        });
    }

    public static <T extends ParseObject> void saveAll(List<T> objects) throws ParseException {
        ParseTaskUtils.wait(saveAllInBackground(objects));
    }

    public static <T extends ParseObject> void saveAllInBackground(List<T> objects, SaveCallback callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(saveAllInBackground(objects), (ParseCallback1) callback);
    }

    public static <T extends ParseObject> Task<Void> saveAllInBackground(final List<T> objects) {
        return ParseUser.getCurrentUserAsync().onSuccessTask(new Continuation<ParseUser, Task<String>>() {
            public Task<String> then(Task<ParseUser> task) throws Exception {
                ParseUser current = (ParseUser) task.getResult();
                if (current == null) {
                    return Task.forResult(null);
                }
                if (!current.isLazy()) {
                    return Task.forResult(current.getSessionToken());
                }
                for (ParseObject object : objects) {
                    if (object.isDataAvailable(ParseObject.KEY_ACL)) {
                        final ParseACL acl = object.getACL(false);
                        if (acl != null) {
                            final ParseUser user = acl.getUnresolvedUser();
                            if (user != null && user.isCurrentUser()) {
                                return user.saveAsync(null).onSuccess(new Continuation<Void, String>() {
                                    public String then(Task<Void> task) throws Exception {
                                        if (!acl.hasUnresolvedUser()) {
                                            return user.getSessionToken();
                                        }
                                        throw new IllegalStateException("ACL has an unresolved ParseUser. Save or sign up before attempting to serialize the ACL.");
                                    }
                                });
                            }
                        }
                        continue;
                    }
                }
                return Task.forResult(null);
            }
        }).onSuccessTask(new Continuation<String, Task<Void>>() {
            public Task<Void> then(Task<String> task) throws Exception {
                return ParseObject.deepSaveAsync(objects, (String) task.getResult());
            }
        });
    }

    public static <T extends ParseObject> Task<List<T>> fetchAllIfNeededInBackground(List<T> objects) {
        return fetchAllAsync(objects, true);
    }

    public static <T extends ParseObject> List<T> fetchAllIfNeeded(List<T> objects) throws ParseException {
        return (List) ParseTaskUtils.wait(fetchAllIfNeededInBackground(objects));
    }

    public static <T extends ParseObject> void fetchAllIfNeededInBackground(List<T> objects, FindCallback<T> callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(fetchAllIfNeededInBackground(objects), (ParseCallback2) callback);
    }

    private static <T extends ParseObject> Task<List<T>> fetchAllAsync(final List<T> objects, final boolean onlyIfNeeded) {
        return ParseUser.getCurrentUserAsync().onSuccessTask(new Continuation<ParseUser, Task<List<T>>>() {
            public Task<List<T>> then(Task<ParseUser> task) throws Exception {
                final ParseUser user = (ParseUser) task.getResult();
                return ParseObject.enqueueForAll(objects, new Continuation<Void, Task<List<T>>>() {
                    public Task<List<T>> then(Task<Void> task) throws Exception {
                        return ParseObject.fetchAllAsync(objects, user, onlyIfNeeded, task);
                    }
                });
            }
        });
    }

    private static <T extends ParseObject> Task<List<T>> fetchAllAsync(final List<T> objects, final ParseUser user, final boolean onlyIfNeeded, Task<Void> toAwait) {
        if (objects.size() == 0) {
            return Task.forResult(objects);
        }
        List<String> objectIds = new ArrayList();
        String className = null;
        for (T object : objects) {
            if (!onlyIfNeeded || !object.isDataAvailable()) {
                if (className == null || object.getClassName().equals(className)) {
                    className = object.getClassName();
                    if (object.getObjectId() != null) {
                        objectIds.add(object.getObjectId());
                    } else if (!onlyIfNeeded) {
                        throw new IllegalArgumentException("All objects must exist on the server");
                    }
                } else {
                    throw new IllegalArgumentException("All objects should have the same class");
                }
            }
        }
        if (objectIds.size() == 0) {
            return Task.forResult(objects);
        }
        final ParseQuery<T> query = ParseQuery.getQuery(className).whereContainedIn(KEY_OBJECT_ID, objectIds);
        return toAwait.continueWithTask(new Continuation<Void, Task<List<T>>>() {
            public Task<List<T>> then(Task<Void> task) throws Exception {
                return query.findAsync(query.getBuilder().build(), user, null);
            }
        }).onSuccess(new Continuation<List<T>, List<T>>() {
            public List<T> then(Task<List<T>> task) throws Exception {
                Map<String, T> resultMap = new HashMap();
                for (ParseObject o : (List) task.getResult()) {
                    resultMap.put(o.getObjectId(), o);
                }
                for (ParseObject object : objects) {
                    if (!onlyIfNeeded || !object.isDataAvailable()) {
                        ParseObject newObject = (ParseObject) resultMap.get(object.getObjectId());
                        if (newObject == null) {
                            throw new ParseException(101, "Object id " + object.getObjectId() + " does not exist");
                        } else if (!Parse.isLocalDatastoreEnabled()) {
                            object.mergeFromObject(newObject);
                        }
                    }
                }
                return objects;
            }
        });
    }

    public static <T extends ParseObject> Task<List<T>> fetchAllInBackground(List<T> objects) {
        return fetchAllAsync(objects, false);
    }

    public static <T extends ParseObject> List<T> fetchAll(List<T> objects) throws ParseException {
        return (List) ParseTaskUtils.wait(fetchAllInBackground(objects));
    }

    public static <T extends ParseObject> void fetchAllInBackground(List<T> objects, FindCallback<T> callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(fetchAllInBackground(objects), (ParseCallback2) callback);
    }

    private ParseOperationSet currentOperations() {
        ParseOperationSet parseOperationSet;
        synchronized (this.mutex) {
            parseOperationSet = (ParseOperationSet) this.operationSetQueue.getLast();
        }
        return parseOperationSet;
    }

    private void applyOperations(ParseOperationSet operations, Map<String, Object> map) {
        for (String key : operations.keySet()) {
            Object newValue = ((ParseFieldOperation) operations.get(key)).apply(map.get(key), key);
            if (newValue != null) {
                map.put(key, newValue);
            } else {
                map.remove(key);
            }
        }
    }

    private void rebuildEstimatedData() {
        synchronized (this.mutex) {
            this.estimatedData.clear();
            for (String key : this.state.keySet()) {
                this.estimatedData.put(key, this.state.get(key));
            }
            Iterator i$ = this.operationSetQueue.iterator();
            while (i$.hasNext()) {
                applyOperations((ParseOperationSet) i$.next(), this.estimatedData);
            }
        }
    }

    private void rebuildDataAvailability() {
        synchronized (this.mutex) {
            this.dataAvailability.clear();
            for (String key : this.state.keySet()) {
                this.dataAvailability.put(key, Boolean.valueOf(true));
            }
        }
    }

    void performOperation(String key, ParseFieldOperation operation) {
        synchronized (this.mutex) {
            Object newValue = operation.apply(this.estimatedData.get(key), key);
            if (newValue != null) {
                this.estimatedData.put(key, newValue);
            } else {
                this.estimatedData.remove(key);
            }
            currentOperations().put(key, operation.mergeWithPrevious((ParseFieldOperation) currentOperations().get(key)));
            checkpointMutableContainer(key, newValue);
            this.dataAvailability.put(key, Boolean.TRUE);
        }
    }

    public void put(String key, Object value) {
        checkKeyIsMutable(key);
        performPut(key, value);
    }

    void performPut(String key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("key may not be null.");
        } else if (value == null) {
            throw new IllegalArgumentException("value may not be null.");
        } else if (ParseEncoder.isValidType(value)) {
            performOperation(key, new ParseSetOperation(value));
        } else {
            throw new IllegalArgumentException("invalid type for value: " + value.getClass().toString());
        }
    }

    public boolean has(String key) {
        return containsKey(key);
    }

    public void increment(String key) {
        increment(key, Integer.valueOf(1));
    }

    public void increment(String key, Number amount) {
        performOperation(key, new ParseIncrementOperation(amount));
    }

    public void add(String key, Object value) {
        addAll(key, Arrays.asList(new Object[]{value}));
    }

    public void addAll(String key, Collection<?> values) {
        performOperation(key, new ParseAddOperation(values));
    }

    public void addUnique(String key, Object value) {
        addAllUnique(key, Arrays.asList(new Object[]{value}));
    }

    public void addAllUnique(String key, Collection<?> values) {
        performOperation(key, new ParseAddUniqueOperation(values));
    }

    public void remove(String key) {
        checkKeyIsMutable(key);
        performRemove(key);
    }

    void performRemove(String key) {
        synchronized (this.mutex) {
            if (get(key) != null) {
                performOperation(key, ParseDeleteOperation.getInstance());
            }
        }
    }

    public void removeAll(String key, Collection<?> values) {
        checkKeyIsMutable(key);
        performOperation(key, new ParseRemoveOperation(values));
    }

    private void checkKeyIsMutable(String key) {
        if (!isKeyMutable(key)) {
            throw new IllegalArgumentException("Cannot modify `" + key + "` property of an " + getClassName() + " object.");
        }
    }

    boolean isKeyMutable(String key) {
        return true;
    }

    public boolean containsKey(String key) {
        boolean containsKey;
        synchronized (this.mutex) {
            containsKey = this.estimatedData.containsKey(key);
        }
        return containsKey;
    }

    public String getString(String key) {
        Object value;
        synchronized (this.mutex) {
            checkGetAccess(key);
            value = this.estimatedData.get(key);
            if (value instanceof String) {
                String value2 = (String) value;
            } else {
                value = null;
            }
        }
        return value;
    }

    public byte[] getBytes(String key) {
        Object value;
        synchronized (this.mutex) {
            checkGetAccess(key);
            value = this.estimatedData.get(key);
            if (value instanceof byte[]) {
                byte[] value2 = (byte[]) value;
            } else {
                value = null;
            }
        }
        return value;
    }

    public Number getNumber(String key) {
        Object value;
        synchronized (this.mutex) {
            checkGetAccess(key);
            value = this.estimatedData.get(key);
            if (value instanceof Number) {
                Number value2 = (Number) value;
            } else {
                value = null;
            }
        }
        return value;
    }

    public JSONArray getJSONArray(String key) {
        Object value;
        synchronized (this.mutex) {
            checkGetAccess(key);
            value = this.estimatedData.get(key);
            if (value instanceof List) {
                value = PointerOrLocalIdEncoder.get().encode(value);
                put(key, value);
            }
            if (value instanceof JSONArray) {
                JSONArray value2 = (JSONArray) value;
            } else {
                value = null;
            }
        }
        return value;
    }

    public <T> List<T> getList(String key) {
        List<T> returnValue;
        synchronized (this.mutex) {
            Object obj = this.estimatedData.get(key);
            if (obj instanceof JSONArray) {
                obj = ParseDecoder.get().convertJSONArrayToList((JSONArray) obj);
                put(key, obj);
            }
            if (obj instanceof List) {
                returnValue = (List) obj;
            } else {
                returnValue = null;
            }
        }
        return returnValue;
    }

    public <V> Map<String, V> getMap(String key) {
        Map<String, V> returnValue;
        synchronized (this.mutex) {
            Object obj = this.estimatedData.get(key);
            if (obj instanceof JSONObject) {
                obj = ParseDecoder.get().convertJSONObjectToMap((JSONObject) obj);
                put(key, obj);
            }
            if (obj instanceof Map) {
                returnValue = (Map) obj;
            } else {
                returnValue = null;
            }
        }
        return returnValue;
    }

    public JSONObject getJSONObject(String key) {
        Object value;
        synchronized (this.mutex) {
            checkGetAccess(key);
            value = this.estimatedData.get(key);
            if (value instanceof Map) {
                value = PointerOrLocalIdEncoder.get().encode(value);
                put(key, value);
            }
            if (value instanceof JSONObject) {
                JSONObject value2 = (JSONObject) value;
            } else {
                value = null;
            }
        }
        return value;
    }

    public int getInt(String key) {
        Number number = getNumber(key);
        if (number == null) {
            return 0;
        }
        return number.intValue();
    }

    public double getDouble(String key) {
        Number number = getNumber(key);
        if (number == null) {
            return 0.0d;
        }
        return number.doubleValue();
    }

    public long getLong(String key) {
        Number number = getNumber(key);
        if (number == null) {
            return 0;
        }
        return number.longValue();
    }

    public boolean getBoolean(String key) {
        boolean booleanValue;
        synchronized (this.mutex) {
            checkGetAccess(key);
            Object value = this.estimatedData.get(key);
            if (value instanceof Boolean) {
                booleanValue = ((Boolean) value).booleanValue();
            } else {
                booleanValue = false;
            }
        }
        return booleanValue;
    }

    public Date getDate(String key) {
        Object value;
        synchronized (this.mutex) {
            checkGetAccess(key);
            value = this.estimatedData.get(key);
            if (value instanceof Date) {
                Date value2 = (Date) value;
            } else {
                value = null;
            }
        }
        return value;
    }

    public ParseObject getParseObject(String key) {
        Object value = get(key);
        if (value instanceof ParseObject) {
            return (ParseObject) value;
        }
        return null;
    }

    public ParseUser getParseUser(String key) {
        Object value = get(key);
        if (value instanceof ParseUser) {
            return (ParseUser) value;
        }
        return null;
    }

    public ParseFile getParseFile(String key) {
        Object value = get(key);
        if (value instanceof ParseFile) {
            return (ParseFile) value;
        }
        return null;
    }

    public ParseGeoPoint getParseGeoPoint(String key) {
        Object value;
        synchronized (this.mutex) {
            checkGetAccess(key);
            value = this.estimatedData.get(key);
            if (value instanceof ParseGeoPoint) {
                ParseGeoPoint value2 = (ParseGeoPoint) value;
            } else {
                value = null;
            }
        }
        return value;
    }

    public ParseACL getACL() {
        return getACL(true);
    }

    private ParseACL getACL(boolean mayCopy) {
        synchronized (this.mutex) {
            checkGetAccess(KEY_ACL);
            Object acl = this.estimatedData.get(KEY_ACL);
            if (acl == null) {
                return null;
            } else if (acl instanceof ParseACL) {
                if (mayCopy) {
                    if (((ParseACL) acl).isShared()) {
                        ParseACL copy = ((ParseACL) acl).copy();
                        this.estimatedData.put(KEY_ACL, copy);
                        addToHashedObjects(copy);
                        return copy;
                    }
                }
                ParseACL parseACL = (ParseACL) acl;
                return parseACL;
            } else {
                throw new RuntimeException("only ACLs can be stored in the ACL key");
            }
        }
    }

    public void setACL(ParseACL acl) {
        put(KEY_ACL, acl);
    }

    public boolean isDataAvailable() {
        boolean isComplete;
        synchronized (this.mutex) {
            isComplete = this.state.isComplete();
        }
        return isComplete;
    }

    private boolean isDataAvailable(String key) {
        boolean z;
        synchronized (this.mutex) {
            z = isDataAvailable() || (this.dataAvailability.containsKey(key) && ((Boolean) this.dataAvailability.get(key)).booleanValue());
        }
        return z;
    }

    public <T extends ParseObject> ParseRelation<T> getRelation(String key) {
        synchronized (this.mutex) {
            Object value = this.estimatedData.get(key);
            if (value instanceof ParseRelation) {
                ParseRelation<T> relation = (ParseRelation) value;
                relation.ensureParentAndKey(this, key);
                return relation;
            }
            relation = new ParseRelation(this, key);
            this.estimatedData.put(key, relation);
            return relation;
        }
    }

    public Object get(String key) {
        Object acl;
        synchronized (this.mutex) {
            if (key.equals(KEY_ACL)) {
                acl = getACL();
            } else {
                checkGetAccess(key);
                acl = this.estimatedData.get(key);
                if (acl instanceof ParseRelation) {
                    ((ParseRelation) acl).ensureParentAndKey(this, key);
                }
            }
        }
        return acl;
    }

    private void checkGetAccess(String key) {
        if (!isDataAvailable(key)) {
            throw new IllegalStateException("ParseObject has no data for '" + key + "'. Call fetchIfNeeded() to get the data.");
        }
    }

    public boolean hasSameId(ParseObject other) {
        boolean z;
        synchronized (this.mutex) {
            z = getClassName() != null && getObjectId() != null && getClassName().equals(other.getClassName()) && getObjectId().equals(other.getObjectId());
        }
        return z;
    }

    void registerSaveListener(GetCallback<ParseObject> callback) {
        synchronized (this.mutex) {
            this.saveEvent.subscribe(callback);
        }
    }

    void unregisterSaveListener(GetCallback<ParseObject> callback) {
        synchronized (this.mutex) {
            this.saveEvent.unsubscribe(callback);
        }
    }

    static String getClassName(Class<? extends ParseObject> clazz) {
        String name = (String) classNames.get(clazz);
        if (name == null) {
            ParseClassName info = (ParseClassName) clazz.getAnnotation(ParseClassName.class);
            if (info == null) {
                return null;
            }
            name = info.value();
            classNames.put(clazz, name);
        }
        return name;
    }

    void setDefaultValues() {
        if (needsDefaultACL() && ParseACL.getDefaultACL() != null) {
            setACL(ParseACL.getDefaultACL());
        }
    }

    boolean needsDefaultACL() {
        return true;
    }

    static void registerParseSubclasses() {
        registerSubclass(ParseUser.class);
        registerSubclass(ParseRole.class);
        registerSubclass(ParseInstallation.class);
        registerSubclass(ParseSession.class);
        registerSubclass(ParsePin.class);
        registerSubclass(EventuallyPin.class);
    }

    static void unregisterParseSubclasses() {
        unregisterSubclass(ParseUser.class);
        unregisterSubclass(ParseRole.class);
        unregisterSubclass(ParseInstallation.class);
        unregisterSubclass(ParseSession.class);
        unregisterSubclass(ParsePin.class);
        unregisterSubclass(EventuallyPin.class);
    }

    public static <T extends ParseObject> void pinAllInBackground(String name, List<T> objects, SaveCallback callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(pinAllInBackground(name, (List) objects), (ParseCallback1) callback);
    }

    public static <T extends ParseObject> Task<Void> pinAllInBackground(String name, List<T> objects) {
        return pinAllInBackground(name, (List) objects, true);
    }

    private static <T extends ParseObject> Task<Void> pinAllInBackground(final String name, final List<T> objects, final boolean includeAllChildren) {
        if (Parse.isLocalDatastoreEnabled()) {
            Task<Void> task = Task.forResult(null);
            for (final T object : objects) {
                task = task.onSuccessTask(new Continuation<Void, Task<Void>>() {
                    public Task<Void> then(Task<Void> task) throws Exception {
                        if (!object.isDataAvailable(ParseObject.KEY_ACL)) {
                            return Task.forResult(null);
                        }
                        ParseACL acl = object.getACL(false);
                        if (acl == null) {
                            return Task.forResult(null);
                        }
                        ParseUser user = acl.getUnresolvedUser();
                        if (user == null || !user.isCurrentUser()) {
                            return Task.forResult(null);
                        }
                        return ParseUser.pinCurrentUserIfNeededAsync(user);
                    }
                });
            }
            return task.onSuccessTask(new Continuation<Void, Task<Void>>() {
                public Task<Void> then(Task<Void> task) throws Exception {
                    return Parse.getLocalDatastore().pinAllObjectsAsync(name != null ? name : ParseObject.DEFAULT_PIN, objects, includeAllChildren);
                }
            }).onSuccessTask(new Continuation<Void, Task<Void>>() {
                public Task<Void> then(Task<Void> task) throws Exception {
                    if ("_currentUser".equals(name)) {
                        return task;
                    }
                    for (ParseObject object : objects) {
                        if (object instanceof ParseUser) {
                            ParseUser user = (ParseUser) object;
                            if (user.isCurrentUser()) {
                                return ParseUser.pinCurrentUserIfNeededAsync(user);
                            }
                        }
                    }
                    return task;
                }
            });
        }
        throw new IllegalStateException("Method requires Local Datastore. Please refer to `Parse#enableLocalDatastore(Context)`.");
    }

    public static <T extends ParseObject> void pinAll(String name, List<T> objects) throws ParseException {
        ParseTaskUtils.wait(pinAllInBackground(name, (List) objects));
    }

    public static <T extends ParseObject> void pinAllInBackground(List<T> objects, SaveCallback callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(pinAllInBackground(DEFAULT_PIN, (List) objects), (ParseCallback1) callback);
    }

    public static <T extends ParseObject> Task<Void> pinAllInBackground(List<T> objects) {
        return pinAllInBackground(DEFAULT_PIN, (List) objects);
    }

    public static <T extends ParseObject> void pinAll(List<T> objects) throws ParseException {
        ParseTaskUtils.wait(pinAllInBackground(DEFAULT_PIN, (List) objects));
    }

    public static <T extends ParseObject> void unpinAllInBackground(String name, List<T> objects, DeleteCallback callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(unpinAllInBackground(name, (List) objects), (ParseCallback1) callback);
    }

    public static <T extends ParseObject> Task<Void> unpinAllInBackground(String name, List<T> objects) {
        if (Parse.isLocalDatastoreEnabled()) {
            if (name == null) {
                name = DEFAULT_PIN;
            }
            return Parse.getLocalDatastore().unpinAllObjectsAsync(name, (List) objects);
        }
        throw new IllegalStateException("Method requires Local Datastore. Please refer to `Parse#enableLocalDatastore(Context)`.");
    }

    public static <T extends ParseObject> void unpinAll(String name, List<T> objects) throws ParseException {
        ParseTaskUtils.wait(unpinAllInBackground(name, (List) objects));
    }

    public static <T extends ParseObject> void unpinAllInBackground(List<T> objects, DeleteCallback callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(unpinAllInBackground(DEFAULT_PIN, (List) objects), (ParseCallback1) callback);
    }

    public static <T extends ParseObject> Task<Void> unpinAllInBackground(List<T> objects) {
        return unpinAllInBackground(DEFAULT_PIN, (List) objects);
    }

    public static <T extends ParseObject> void unpinAll(List<T> objects) throws ParseException {
        ParseTaskUtils.wait(unpinAllInBackground(DEFAULT_PIN, (List) objects));
    }

    public static void unpinAllInBackground(String name, DeleteCallback callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(unpinAllInBackground(name), (ParseCallback1) callback);
    }

    public static Task<Void> unpinAllInBackground(String name) {
        if (Parse.isLocalDatastoreEnabled()) {
            if (name == null) {
                name = DEFAULT_PIN;
            }
            return Parse.getLocalDatastore().unpinAllObjectsAsync(name);
        }
        throw new IllegalStateException("Method requires Local Datastore. Please refer to `Parse#enableLocalDatastore(Context)`.");
    }

    public static void unpinAll(String name) throws ParseException {
        ParseTaskUtils.wait(unpinAllInBackground(name));
    }

    public static void unpinAllInBackground(DeleteCallback callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(unpinAllInBackground(), (ParseCallback1) callback);
    }

    public static Task<Void> unpinAllInBackground() {
        return unpinAllInBackground(DEFAULT_PIN);
    }

    public static void unpinAll() throws ParseException {
        ParseTaskUtils.wait(unpinAllInBackground());
    }

    <T extends ParseObject> Task<T> fetchFromLocalDatastoreAsync() {
        if (Parse.isLocalDatastoreEnabled()) {
            return Parse.getLocalDatastore().fetchLocallyAsync(this);
        }
        throw new IllegalStateException("Method requires Local Datastore. Please refer to `Parse#enableLocalDatastore(Context)`.");
    }

    public <T extends ParseObject> void fetchFromLocalDatastoreInBackground(GetCallback<T> callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(fetchFromLocalDatastoreAsync(), (ParseCallback2) callback);
    }

    public void fetchFromLocalDatastore() throws ParseException {
        ParseTaskUtils.wait(fetchFromLocalDatastoreAsync());
    }

    public void pinInBackground(String name, SaveCallback callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(pinInBackground(name), (ParseCallback1) callback);
    }

    public Task<Void> pinInBackground(String name) {
        return pinAllInBackground(name, Collections.singletonList(this));
    }

    Task<Void> pinInBackground(String name, boolean includeAllChildren) {
        return pinAllInBackground(name, Collections.singletonList(this), includeAllChildren);
    }

    public void pin(String name) throws ParseException {
        ParseTaskUtils.wait(pinInBackground(name));
    }

    public void pinInBackground(SaveCallback callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(pinInBackground(), (ParseCallback1) callback);
    }

    public Task<Void> pinInBackground() {
        return pinAllInBackground(DEFAULT_PIN, Arrays.asList(new ParseObject[]{this}));
    }

    public void pin() throws ParseException {
        ParseTaskUtils.wait(pinInBackground());
    }

    public void unpinInBackground(String name, DeleteCallback callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(unpinInBackground(name), (ParseCallback1) callback);
    }

    public Task<Void> unpinInBackground(String name) {
        return unpinAllInBackground(name, Arrays.asList(new ParseObject[]{this}));
    }

    public void unpin(String name) throws ParseException {
        ParseTaskUtils.wait(unpinInBackground(name));
    }

    public void unpinInBackground(DeleteCallback callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(unpinInBackground(), (ParseCallback1) callback);
    }

    public Task<Void> unpinInBackground() {
        return unpinAllInBackground(DEFAULT_PIN, Arrays.asList(new ParseObject[]{this}));
    }

    public void unpin() throws ParseException {
        ParseTaskUtils.wait(unpinInBackground());
    }
}
