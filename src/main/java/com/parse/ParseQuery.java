package com.parse;

import bolts.Continuation;
import bolts.Task;
import bolts.Task.TaskCompletionSource;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseQuery<T extends ParseObject> {
    private final Builder<T> builder;
    private TaskCompletionSource cts;
    private boolean isRunning;
    private final Object lock;
    private ParseUser user;

    public enum CachePolicy {
        IGNORE_CACHE,
        CACHE_ONLY,
        NETWORK_ONLY,
        CACHE_ELSE_NETWORK,
        NETWORK_ELSE_CACHE,
        CACHE_THEN_NETWORK
    }

    private interface CacheThenNetworkCallable<T extends ParseObject, TResult> {
        TResult call(State<T> state, ParseUser parseUser, Task<Void> task);
    }

    static class KeyConstraints extends HashMap<String, Object> {
        KeyConstraints() {
        }
    }

    static class QueryConstraints extends HashMap<String, Object> {
        public QueryConstraints(Map<? extends String, ?> map) {
            super(map);
        }
    }

    static class RelationConstraint {
        private String key;
        private ParseObject object;

        public RelationConstraint(String key, ParseObject object) {
            if (key == null || object == null) {
                throw new IllegalArgumentException("Arguments must not be null.");
            }
            this.key = key;
            this.object = object;
        }

        public String getKey() {
            return this.key;
        }

        public ParseObject getObject() {
            return this.object;
        }

        public ParseRelation<ParseObject> getRelation() {
            return this.object.getRelation(this.key);
        }

        public JSONObject encode(ParseEncoder objectEncoder) {
            JSONObject json = new JSONObject();
            try {
                json.put(ParametersKeys.KEY, this.key);
                json.put("object", objectEncoder.encodeRelatedObject(this.object));
                return json;
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class State<T extends ParseObject> {
        private final CachePolicy cachePolicy;
        private final String className;
        private final Map<String, Object> extraOptions;
        private final boolean ignoreACLs;
        private final Set<String> include;
        private final boolean isFromLocalDatastore;
        private final int limit;
        private final long maxCacheAge;
        private final List<String> order;
        private final String pinName;
        private final Set<String> selectedKeys;
        private final int skip;
        private final boolean trace;
        private final QueryConstraints where;

        static class Builder<T extends ParseObject> {
            private CachePolicy cachePolicy;
            private final String className;
            private final Map<String, Object> extraOptions;
            private boolean ignoreACLs;
            private final Set<String> includes;
            private boolean isFromLocalDatastore;
            private int limit;
            private long maxCacheAge;
            private List<String> order;
            private String pinName;
            private Set<String> selectedKeys;
            private int skip;
            private boolean trace;
            private final QueryConstraints where;

            public static <T extends ParseObject> Builder<T> or(List<Builder<T>> builders) {
                if (builders.isEmpty()) {
                    throw new IllegalArgumentException("Can't take an or of an empty list of queries");
                }
                String className = null;
                List<QueryConstraints> constraints = new ArrayList();
                for (Builder<T> builder : builders) {
                    if (className != null && !builder.className.equals(className)) {
                        throw new IllegalArgumentException("All of the queries in an or query must be on the same class ");
                    } else if (builder.limit >= 0) {
                        throw new IllegalArgumentException("Cannot have limits in sub queries of an 'OR' query");
                    } else if (builder.skip > 0) {
                        throw new IllegalArgumentException("Cannot have skips in sub queries of an 'OR' query");
                    } else if (!builder.order.isEmpty()) {
                        throw new IllegalArgumentException("Cannot have an order in sub queries of an 'OR' query");
                    } else if (!builder.includes.isEmpty()) {
                        throw new IllegalArgumentException("Cannot have an include in sub queries of an 'OR' query");
                    } else if (builder.selectedKeys != null) {
                        throw new IllegalArgumentException("Cannot have an selectKeys in sub queries of an 'OR' query");
                    } else {
                        className = builder.className;
                        constraints.add(builder.where);
                    }
                }
                return new Builder(className).whereSatifiesAnyOf(constraints);
            }

            public Builder(String className) {
                this.where = new QueryConstraints();
                this.includes = new HashSet();
                this.limit = -1;
                this.skip = 0;
                this.order = new ArrayList();
                this.extraOptions = new HashMap();
                this.cachePolicy = CachePolicy.IGNORE_CACHE;
                this.maxCacheAge = Long.MAX_VALUE;
                this.isFromLocalDatastore = false;
                this.className = className;
            }

            public Builder(Class<T> subclass) {
                this(ParseObject.getClassName(subclass));
            }

            public Builder(State state) {
                this.where = new QueryConstraints();
                this.includes = new HashSet();
                this.limit = -1;
                this.skip = 0;
                this.order = new ArrayList();
                this.extraOptions = new HashMap();
                this.cachePolicy = CachePolicy.IGNORE_CACHE;
                this.maxCacheAge = Long.MAX_VALUE;
                this.isFromLocalDatastore = false;
                this.className = state.className();
                this.where.putAll(state.constraints());
                this.includes.addAll(state.includes());
                this.selectedKeys = state.selectedKeys() != null ? new HashSet(state.selectedKeys()) : null;
                this.limit = state.limit();
                this.skip = state.skip();
                this.order.addAll(state.order());
                this.extraOptions.putAll(state.extraOptions());
                this.trace = state.isTracingEnabled();
                this.cachePolicy = state.cachePolicy();
                this.maxCacheAge = state.maxCacheAge();
                this.isFromLocalDatastore = state.isFromLocalDatastore();
                this.pinName = state.pinName();
                this.ignoreACLs = state.ignoreACLs();
            }

            public Builder(Builder<T> builder) {
                this.where = new QueryConstraints();
                this.includes = new HashSet();
                this.limit = -1;
                this.skip = 0;
                this.order = new ArrayList();
                this.extraOptions = new HashMap();
                this.cachePolicy = CachePolicy.IGNORE_CACHE;
                this.maxCacheAge = Long.MAX_VALUE;
                this.isFromLocalDatastore = false;
                this.className = builder.className;
                this.where.putAll(builder.where);
                this.includes.addAll(builder.includes);
                this.selectedKeys = builder.selectedKeys != null ? new HashSet(builder.selectedKeys) : null;
                this.limit = builder.limit;
                this.skip = builder.skip;
                this.order.addAll(builder.order);
                this.extraOptions.putAll(builder.extraOptions);
                this.trace = builder.trace;
                this.cachePolicy = builder.cachePolicy;
                this.maxCacheAge = builder.maxCacheAge;
                this.isFromLocalDatastore = builder.isFromLocalDatastore;
                this.pinName = builder.pinName;
                this.ignoreACLs = builder.ignoreACLs;
            }

            public String getClassName() {
                return this.className;
            }

            public Builder<T> whereEqualTo(String key, Object value) {
                this.where.put(key, value);
                return this;
            }

            public Builder<T> whereDoesNotMatchKeyInQuery(String key, String keyInQuery, Builder<?> builder) {
                Map<String, Object> condition = new HashMap();
                condition.put(ParametersKeys.KEY, keyInQuery);
                condition.put("query", builder);
                return addConditionInternal(key, "$dontSelect", Collections.unmodifiableMap(condition));
            }

            public Builder<T> whereMatchesKeyInQuery(String key, String keyInQuery, Builder<?> builder) {
                Map<String, Object> condition = new HashMap();
                condition.put(ParametersKeys.KEY, keyInQuery);
                condition.put("query", builder);
                return addConditionInternal(key, "$select", Collections.unmodifiableMap(new HashMap(condition)));
            }

            public Builder<T> whereDoesNotMatchQuery(String key, Builder<?> builder) {
                return addConditionInternal(key, "$notInQuery", builder);
            }

            public Builder<T> whereMatchesQuery(String key, Builder<?> builder) {
                return addConditionInternal(key, "$inQuery", builder);
            }

            public Builder<T> whereNear(String key, ParseGeoPoint point) {
                return addCondition(key, "$nearSphere", (Object) point);
            }

            public Builder<T> maxDistance(String key, double maxDistance) {
                return addCondition(key, "$maxDistance", Double.valueOf(maxDistance));
            }

            public Builder<T> whereWithin(String key, ParseGeoPoint southwest, ParseGeoPoint northeast) {
                List<Object> array = new ArrayList();
                array.add(southwest);
                array.add(northeast);
                Object dictionary = new HashMap();
                dictionary.put("$box", array);
                return addCondition(key, "$within", dictionary);
            }

            public Builder<T> addCondition(String key, String condition, Collection<? extends Object> value) {
                return addConditionInternal(key, condition, Collections.unmodifiableCollection(value));
            }

            public Builder<T> addCondition(String key, String condition, Object value) {
                return addConditionInternal(key, condition, value);
            }

            private Builder<T> addConditionInternal(String key, String condition, Object value) {
                KeyConstraints whereValue = null;
                if (this.where.containsKey(key)) {
                    KeyConstraints existingValue = this.where.get(key);
                    if (existingValue instanceof KeyConstraints) {
                        whereValue = existingValue;
                    }
                }
                if (whereValue == null) {
                    whereValue = new KeyConstraints();
                }
                whereValue.put(condition, value);
                this.where.put(key, whereValue);
                return this;
            }

            Builder<T> whereRelatedTo(ParseObject parent, String key) {
                this.where.put("$relatedTo", new RelationConstraint(key, parent));
                return this;
            }

            private Builder<T> whereSatifiesAnyOf(List<QueryConstraints> constraints) {
                this.where.put("$or", constraints);
                return this;
            }

            Builder<T> whereObjectIdEquals(String objectId) {
                this.where.clear();
                this.where.put("objectId", objectId);
                return this;
            }

            private Builder<T> setOrder(String key) {
                this.order.clear();
                this.order.add(key);
                return this;
            }

            private Builder<T> addOrder(String key) {
                this.order.add(key);
                return this;
            }

            public Builder<T> orderByAscending(String key) {
                return setOrder(key);
            }

            public Builder<T> addAscendingOrder(String key) {
                return addOrder(key);
            }

            public Builder<T> orderByDescending(String key) {
                return setOrder(String.format("-%s", new Object[]{key}));
            }

            public Builder<T> addDescendingOrder(String key) {
                return addOrder(String.format("-%s", new Object[]{key}));
            }

            public Builder<T> include(String key) {
                this.includes.add(key);
                return this;
            }

            public Builder<T> selectKeys(Collection<String> keys) {
                if (this.selectedKeys == null) {
                    this.selectedKeys = new HashSet();
                }
                this.selectedKeys.addAll(keys);
                return this;
            }

            public int getLimit() {
                return this.limit;
            }

            public Builder<T> setLimit(int limit) {
                this.limit = limit;
                return this;
            }

            public int getSkip() {
                return this.skip;
            }

            public Builder<T> setSkip(int skip) {
                this.skip = skip;
                return this;
            }

            Builder<T> redirectClassNameForKey(String key) {
                this.extraOptions.put("redirectClassNameForKey", key);
                return this;
            }

            public Builder<T> setTracingEnabled(boolean trace) {
                this.trace = trace;
                return this;
            }

            public CachePolicy getCachePolicy() {
                ParseQuery.throwIfLDSEnabled();
                return this.cachePolicy;
            }

            public Builder<T> setCachePolicy(CachePolicy cachePolicy) {
                ParseQuery.throwIfLDSEnabled();
                this.cachePolicy = cachePolicy;
                return this;
            }

            public long getMaxCacheAge() {
                ParseQuery.throwIfLDSEnabled();
                return this.maxCacheAge;
            }

            public Builder<T> setMaxCacheAge(long maxCacheAge) {
                ParseQuery.throwIfLDSEnabled();
                this.maxCacheAge = maxCacheAge;
                return this;
            }

            public boolean isFromNetwork() {
                ParseQuery.throwIfLDSDisabled();
                return !this.isFromLocalDatastore;
            }

            public Builder<T> fromNetwork() {
                ParseQuery.throwIfLDSDisabled();
                this.isFromLocalDatastore = false;
                this.pinName = null;
                return this;
            }

            public Builder<T> fromLocalDatastore() {
                return fromPin(null);
            }

            public boolean isFromLocalDatstore() {
                return this.isFromLocalDatastore;
            }

            public Builder<T> fromPin() {
                return fromPin(ParseObject.DEFAULT_PIN);
            }

            public Builder<T> fromPin(String pinName) {
                ParseQuery.throwIfLDSDisabled();
                this.isFromLocalDatastore = true;
                this.pinName = pinName;
                return this;
            }

            public Builder<T> ignoreACLs() {
                ParseQuery.throwIfLDSDisabled();
                this.ignoreACLs = true;
                return this;
            }

            public State<T> build() {
                if (this.isFromLocalDatastore || !this.ignoreACLs) {
                    return new State();
                }
                throw new IllegalStateException("`ignoreACLs` cannot be combined with network queries");
            }
        }

        private State(Builder<T> builder) {
            this.className = builder.className;
            this.where = new QueryConstraints(builder.where);
            this.include = Collections.unmodifiableSet(new HashSet(builder.includes));
            this.selectedKeys = builder.selectedKeys != null ? Collections.unmodifiableSet(new HashSet(builder.selectedKeys)) : null;
            this.limit = builder.limit;
            this.skip = builder.skip;
            this.order = Collections.unmodifiableList(new ArrayList(builder.order));
            this.extraOptions = Collections.unmodifiableMap(new HashMap(builder.extraOptions));
            this.trace = builder.trace;
            this.cachePolicy = builder.cachePolicy;
            this.maxCacheAge = builder.maxCacheAge;
            this.isFromLocalDatastore = builder.isFromLocalDatastore;
            this.pinName = builder.pinName;
            this.ignoreACLs = builder.ignoreACLs;
        }

        public String className() {
            return this.className;
        }

        public QueryConstraints constraints() {
            return this.where;
        }

        public Set<String> includes() {
            return this.include;
        }

        public Set<String> selectedKeys() {
            return this.selectedKeys;
        }

        public int limit() {
            return this.limit;
        }

        public int skip() {
            return this.skip;
        }

        public List<String> order() {
            return this.order;
        }

        public Map<String, Object> extraOptions() {
            return this.extraOptions;
        }

        public boolean isTracingEnabled() {
            return this.trace;
        }

        public CachePolicy cachePolicy() {
            return this.cachePolicy;
        }

        public long maxCacheAge() {
            return this.maxCacheAge;
        }

        public boolean isFromLocalDatastore() {
            return this.isFromLocalDatastore;
        }

        public String pinName() {
            return this.pinName;
        }

        public boolean ignoreACLs() {
            return this.ignoreACLs;
        }

        JSONObject toJSON(ParseEncoder encoder) {
            JSONObject params = new JSONObject();
            try {
                params.put("className", this.className);
                params.put("where", encoder.encode(this.where));
                if (this.limit >= 0) {
                    params.put("limit", this.limit);
                }
                if (this.skip > 0) {
                    params.put("skip", this.skip);
                }
                if (!this.order.isEmpty()) {
                    params.put("order", ParseTextUtils.join(",", this.order));
                }
                if (!this.include.isEmpty()) {
                    params.put("include", ParseTextUtils.join(",", this.include));
                }
                if (this.selectedKeys != null) {
                    params.put("fields", ParseTextUtils.join(",", this.selectedKeys));
                }
                if (this.trace) {
                    params.put("trace", 1);
                }
                for (String key : this.extraOptions.keySet()) {
                    params.put(key, encoder.encode(this.extraOptions.get(key)));
                }
                return params;
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        public String toString() {
            return String.format(Locale.US, "%s[className=%s, where=%s, include=%s, selectedKeys=%s, limit=%s, skip=%s, order=%s, extraOptions=%s, cachePolicy=%s, maxCacheAge=%s, trace=%s]", new Object[]{getClass().getName(), this.className, this.where, this.include, this.selectedKeys, Integer.valueOf(this.limit), Integer.valueOf(this.skip), this.order, this.extraOptions, this.cachePolicy, Long.valueOf(this.maxCacheAge), Boolean.valueOf(this.trace)});
        }
    }

    private static ParseQueryController getQueryController() {
        return ParseCorePlugins.getInstance().getQueryController();
    }

    public static <T extends ParseObject> ParseQuery<T> or(List<ParseQuery<T>> queries) {
        if (queries.isEmpty()) {
            throw new IllegalArgumentException("Can't take an or of an empty list of queries");
        }
        List<Builder<T>> builders = new ArrayList();
        for (ParseQuery<T> query : queries) {
            builders.add(query.getBuilder());
        }
        return new ParseQuery(Builder.or(builders));
    }

    public static <T extends ParseObject> ParseQuery<T> getQuery(Class<T> subclass) {
        return new ParseQuery((Class) subclass);
    }

    public static <T extends ParseObject> ParseQuery<T> getQuery(String className) {
        return new ParseQuery(className);
    }

    @Deprecated
    public static ParseQuery<ParseUser> getUserQuery() {
        return ParseUser.getQuery();
    }

    private static void throwIfLDSEnabled() {
        throwIfLDSEnabled(false);
    }

    private static void throwIfLDSDisabled() {
        throwIfLDSEnabled(true);
    }

    private static void throwIfLDSEnabled(boolean enabled) {
        boolean ldsEnabled = Parse.isLocalDatastoreEnabled();
        if (enabled && !ldsEnabled) {
            throw new IllegalStateException("Method requires Local Datastore. Please refer to `Parse#enableLocalDatastore(Context)`.");
        } else if (!enabled && ldsEnabled) {
            throw new IllegalStateException("Unsupported method when Local Datastore is enabled.");
        }
    }

    public ParseQuery(Class<T> subclass) {
        this(ParseObject.getClassName(subclass));
    }

    public ParseQuery(String theClassName) {
        this(new Builder(theClassName));
    }

    ParseQuery(Builder<T> builder) {
        this.lock = new Object();
        this.isRunning = false;
        this.builder = builder;
    }

    Builder<T> getBuilder() {
        return this.builder;
    }

    ParseQuery<T> setUser(ParseUser user) {
        this.user = user;
        return this;
    }

    Task<ParseUser> getUserAsync(State<T> state) {
        if (state.ignoreACLs()) {
            return Task.forResult(null);
        }
        if (this.user != null) {
            return Task.forResult(this.user);
        }
        return ParseUser.getCurrentUserAsync();
    }

    private void checkIfRunning() {
        checkIfRunning(false);
    }

    private void checkIfRunning(boolean grabLock) {
        synchronized (this.lock) {
            if (this.isRunning) {
                throw new RuntimeException("This query has an outstanding network connection. You have to wait until it's done.");
            }
            if (grabLock) {
                this.isRunning = true;
                this.cts = Task.create();
            }
        }
    }

    public void cancel() {
        synchronized (this.lock) {
            if (this.cts != null) {
                this.cts.trySetCancelled();
                this.cts = null;
            }
            this.isRunning = false;
        }
    }

    public List<T> find() throws ParseException {
        return (List) ParseTaskUtils.wait(findInBackground());
    }

    public T getFirst() throws ParseException {
        return (ParseObject) ParseTaskUtils.wait(getFirstInBackground());
    }

    public ParseQuery<T> setCachePolicy(CachePolicy newCachePolicy) {
        checkIfRunning();
        this.builder.setCachePolicy(newCachePolicy);
        return this;
    }

    public CachePolicy getCachePolicy() {
        return this.builder.getCachePolicy();
    }

    ParseQuery<T> fromNetwork() {
        checkIfRunning();
        this.builder.fromNetwork();
        return this;
    }

    boolean isFromNetwork() {
        return this.builder.isFromNetwork();
    }

    public ParseQuery<T> fromLocalDatastore() {
        this.builder.fromLocalDatastore();
        return this;
    }

    public ParseQuery<T> fromPin() {
        checkIfRunning();
        this.builder.fromPin();
        return this;
    }

    public ParseQuery<T> fromPin(String name) {
        checkIfRunning();
        this.builder.fromPin(name);
        return this;
    }

    public ParseQuery<T> ignoreACLs() {
        checkIfRunning();
        this.builder.ignoreACLs();
        return this;
    }

    public ParseQuery<T> setMaxCacheAge(long maxAgeInMilliseconds) {
        checkIfRunning();
        this.builder.setMaxCacheAge(maxAgeInMilliseconds);
        return this;
    }

    public long getMaxCacheAge() {
        return this.builder.getMaxCacheAge();
    }

    private <TResult> Task<TResult> doWithRunningCheck(Callable<Task<TResult>> runnable) {
        Task<TResult> task;
        checkIfRunning(true);
        try {
            task = (Task) runnable.call();
        } catch (Exception e) {
            task = Task.forError(e);
        }
        return task.continueWithTask(new Continuation<TResult, Task<TResult>>() {
            public Task<TResult> then(Task<TResult> task) throws Exception {
                synchronized (ParseQuery.this.lock) {
                    ParseQuery.this.isRunning = false;
                    if (ParseQuery.this.cts != null) {
                        ParseQuery.this.cts.trySetResult(null);
                    }
                    ParseQuery.this.cts = null;
                }
                return task;
            }
        });
    }

    public Task<List<T>> findInBackground() {
        return findAsync(this.builder.build());
    }

    public void findInBackground(FindCallback<T> callback) {
        Task task;
        State<T> state = this.builder.build();
        if (state.cachePolicy() != CachePolicy.CACHE_THEN_NETWORK || state.isFromLocalDatastore()) {
            task = findAsync(state);
        } else {
            task = doCacheThenNetwork(state, callback, new CacheThenNetworkCallable<T, Task<List<T>>>() {
                public Task<List<T>> call(State<T> state, ParseUser user, Task<Void> cancellationToken) {
                    return ParseQuery.this.findAsync(state, user, cancellationToken);
                }
            });
        }
        ParseTaskUtils.callbackOnMainThreadAsync(task, (ParseCallback2) callback);
    }

    private Task<List<T>> findAsync(final State<T> state) {
        return doWithRunningCheck(new Callable<Task<List<T>>>() {
            public Task<List<T>> call() throws Exception {
                return ParseQuery.this.getUserAsync(state).onSuccessTask(new Continuation<ParseUser, Task<List<T>>>() {
                    public Task<List<T>> then(Task<ParseUser> task) throws Exception {
                        return ParseQuery.this.findAsync(state, (ParseUser) task.getResult(), ParseQuery.this.cts.getTask());
                    }
                });
            }
        });
    }

    Task<List<T>> findAsync(State<T> state, ParseUser user, Task<Void> cancellationToken) {
        return getQueryController().findAsync(state, user, cancellationToken);
    }

    public Task<T> getFirstInBackground() {
        return getFirstAsync(this.builder.setLimit(1).build());
    }

    public void getFirstInBackground(GetCallback<T> callback) {
        Task task;
        State<T> state = this.builder.setLimit(1).build();
        if (state.cachePolicy() != CachePolicy.CACHE_THEN_NETWORK || state.isFromLocalDatastore()) {
            task = getFirstAsync(state);
        } else {
            task = doCacheThenNetwork(state, callback, new CacheThenNetworkCallable<T, Task<T>>() {
                public Task<T> call(State<T> state, ParseUser user, Task<Void> cancellationToken) {
                    return ParseQuery.this.getFirstAsync(state, user, cancellationToken);
                }
            });
        }
        ParseTaskUtils.callbackOnMainThreadAsync(task, (ParseCallback2) callback);
    }

    private Task<T> getFirstAsync(final State<T> state) {
        return doWithRunningCheck(new Callable<Task<T>>() {
            public Task<T> call() throws Exception {
                return ParseQuery.this.getUserAsync(state).onSuccessTask(new Continuation<ParseUser, Task<T>>() {
                    public Task<T> then(Task<ParseUser> task) throws Exception {
                        return ParseQuery.this.getFirstAsync(state, (ParseUser) task.getResult(), ParseQuery.this.cts.getTask());
                    }
                });
            }
        });
    }

    private Task<T> getFirstAsync(State<T> state, ParseUser user, Task<Void> cancellationToken) {
        return getQueryController().getFirstAsync(state, user, cancellationToken);
    }

    public int count() throws ParseException {
        return ((Integer) ParseTaskUtils.wait(countInBackground())).intValue();
    }

    public Task<Integer> countInBackground() {
        return countAsync(new Builder(this.builder).setLimit(0).build());
    }

    public void countInBackground(final CountCallback callback) {
        Task task;
        State<T> state = new Builder(this.builder).setLimit(0).build();
        ParseCallback2 c = callback != null ? new ParseCallback2<Integer, ParseException>() {
            public void done(Integer integer, ParseException e) {
                callback.done(e == null ? integer.intValue() : -1, e);
            }
        } : null;
        if (state.cachePolicy() != CachePolicy.CACHE_THEN_NETWORK || state.isFromLocalDatastore()) {
            task = countAsync(state);
        } else {
            task = doCacheThenNetwork(state, c, new CacheThenNetworkCallable<T, Task<Integer>>() {
                public Task<Integer> call(State<T> state, ParseUser user, Task<Void> cancellationToken) {
                    return ParseQuery.this.countAsync(state, user, cancellationToken);
                }
            });
        }
        ParseTaskUtils.callbackOnMainThreadAsync(task, c);
    }

    private Task<Integer> countAsync(final State<T> state) {
        return doWithRunningCheck(new Callable<Task<Integer>>() {
            public Task<Integer> call() throws Exception {
                return ParseQuery.this.getUserAsync(state).onSuccessTask(new Continuation<ParseUser, Task<Integer>>() {
                    public Task<Integer> then(Task<ParseUser> task) throws Exception {
                        return ParseQuery.this.countAsync(state, (ParseUser) task.getResult(), ParseQuery.this.cts.getTask());
                    }
                });
            }
        });
    }

    private Task<Integer> countAsync(State<T> state, ParseUser user, Task<Void> cancellationToken) {
        return getQueryController().countAsync(state, user, cancellationToken);
    }

    public T get(String objectId) throws ParseException {
        return (ParseObject) ParseTaskUtils.wait(getInBackground(objectId));
    }

    public boolean hasCachedResult() {
        throwIfLDSEnabled();
        State<T> state = this.builder.build();
        ParseUser user = null;
        try {
            user = (ParseUser) ParseTaskUtils.wait(getUserAsync(state));
        } catch (ParseException e) {
        }
        if (ParseKeyValueCache.loadFromKeyValueCache(ParseRESTQueryCommand.findCommand(state, user != null ? user.getSessionToken() : null).getCacheKey(), state.maxCacheAge()) != null) {
            return true;
        }
        return false;
    }

    public void clearCachedResult() {
        throwIfLDSEnabled();
        State<T> state = this.builder.build();
        ParseUser user = null;
        try {
            user = (ParseUser) ParseTaskUtils.wait(getUserAsync(state));
        } catch (ParseException e) {
        }
        ParseKeyValueCache.clearFromKeyValueCache(ParseRESTQueryCommand.findCommand(state, user != null ? user.getSessionToken() : null).getCacheKey());
    }

    public static void clearAllCachedResults() {
        throwIfLDSEnabled();
        ParseKeyValueCache.clearKeyValueCacheDir();
    }

    public Task<T> getInBackground(String objectId) {
        return getFirstAsync(this.builder.setSkip(-1).whereObjectIdEquals(objectId).build());
    }

    public void getInBackground(String objectId, GetCallback<T> callback) {
        Task task;
        State<T> state = this.builder.setSkip(-1).whereObjectIdEquals(objectId).build();
        if (state.cachePolicy() != CachePolicy.CACHE_THEN_NETWORK || state.isFromLocalDatastore()) {
            task = getFirstAsync(state);
        } else {
            task = doCacheThenNetwork(state, callback, new CacheThenNetworkCallable<T, Task<T>>() {
                public Task<T> call(State<T> state, ParseUser user, Task<Void> cancellationToken) {
                    return ParseQuery.this.getFirstAsync(state, user, cancellationToken);
                }
            });
        }
        ParseTaskUtils.callbackOnMainThreadAsync(task, (ParseCallback2) callback);
    }

    private <TResult> Task<TResult> doCacheThenNetwork(final State<T> state, final ParseCallback2<TResult, ParseException> callback, final CacheThenNetworkCallable<T, Task<TResult>> delegate) {
        return doWithRunningCheck(new Callable<Task<TResult>>() {
            public Task<TResult> call() throws Exception {
                return ParseQuery.this.getUserAsync(state).onSuccessTask(new Continuation<ParseUser, Task<TResult>>() {
                    public Task<TResult> then(Task<ParseUser> task) throws Exception {
                        final ParseUser user = (ParseUser) task.getResult();
                        State<T> cacheState = new Builder(state).setCachePolicy(CachePolicy.CACHE_ONLY).build();
                        final State<T> networkState = new Builder(state).setCachePolicy(CachePolicy.NETWORK_ONLY).build();
                        return ParseTaskUtils.callbackOnMainThreadAsync((Task) delegate.call(cacheState, user, ParseQuery.this.cts.getTask()), callback).continueWithTask(new Continuation<TResult, Task<TResult>>() {
                            public Task<TResult> then(Task<TResult> task) throws Exception {
                                return task.isCancelled() ? task : (Task) delegate.call(networkState, user, ParseQuery.this.cts.getTask());
                            }
                        });
                    }
                });
            }
        });
    }

    public ParseQuery<T> whereEqualTo(String key, Object value) {
        checkIfRunning();
        this.builder.whereEqualTo(key, value);
        return this;
    }

    public ParseQuery<T> whereLessThan(String key, Object value) {
        checkIfRunning();
        this.builder.addCondition(key, "$lt", value);
        return this;
    }

    public ParseQuery<T> whereNotEqualTo(String key, Object value) {
        checkIfRunning();
        this.builder.addCondition(key, "$ne", value);
        return this;
    }

    public ParseQuery<T> whereGreaterThan(String key, Object value) {
        checkIfRunning();
        this.builder.addCondition(key, "$gt", value);
        return this;
    }

    public ParseQuery<T> whereLessThanOrEqualTo(String key, Object value) {
        checkIfRunning();
        this.builder.addCondition(key, "$lte", value);
        return this;
    }

    public ParseQuery<T> whereGreaterThanOrEqualTo(String key, Object value) {
        checkIfRunning();
        this.builder.addCondition(key, "$gte", value);
        return this;
    }

    public ParseQuery<T> whereContainedIn(String key, Collection<? extends Object> values) {
        checkIfRunning();
        this.builder.addCondition(key, "$in", (Collection) values);
        return this;
    }

    public ParseQuery<T> whereContainsAll(String key, Collection<?> values) {
        checkIfRunning();
        this.builder.addCondition(key, "$all", (Collection) values);
        return this;
    }

    public ParseQuery<T> whereMatchesQuery(String key, ParseQuery<?> query) {
        checkIfRunning();
        this.builder.whereMatchesQuery(key, query.getBuilder());
        return this;
    }

    public ParseQuery<T> whereDoesNotMatchQuery(String key, ParseQuery<?> query) {
        checkIfRunning();
        this.builder.whereDoesNotMatchQuery(key, query.getBuilder());
        return this;
    }

    public ParseQuery<T> whereMatchesKeyInQuery(String key, String keyInQuery, ParseQuery<?> query) {
        checkIfRunning();
        this.builder.whereMatchesKeyInQuery(key, keyInQuery, query.getBuilder());
        return this;
    }

    public ParseQuery<T> whereDoesNotMatchKeyInQuery(String key, String keyInQuery, ParseQuery<?> query) {
        checkIfRunning();
        this.builder.whereDoesNotMatchKeyInQuery(key, keyInQuery, query.getBuilder());
        return this;
    }

    public ParseQuery<T> whereNotContainedIn(String key, Collection<? extends Object> values) {
        checkIfRunning();
        this.builder.addCondition(key, "$nin", (Collection) values);
        return this;
    }

    public ParseQuery<T> whereNear(String key, ParseGeoPoint point) {
        checkIfRunning();
        this.builder.whereNear(key, point);
        return this;
    }

    public ParseQuery<T> whereWithinMiles(String key, ParseGeoPoint point, double maxDistance) {
        checkIfRunning();
        return whereWithinRadians(key, point, maxDistance / ParseGeoPoint.EARTH_MEAN_RADIUS_MILE);
    }

    public ParseQuery<T> whereWithinKilometers(String key, ParseGeoPoint point, double maxDistance) {
        checkIfRunning();
        return whereWithinRadians(key, point, maxDistance / ParseGeoPoint.EARTH_MEAN_RADIUS_KM);
    }

    public ParseQuery<T> whereWithinRadians(String key, ParseGeoPoint point, double maxDistance) {
        checkIfRunning();
        this.builder.whereNear(key, point).maxDistance(key, maxDistance);
        return this;
    }

    public ParseQuery<T> whereWithinGeoBox(String key, ParseGeoPoint southwest, ParseGeoPoint northeast) {
        checkIfRunning();
        this.builder.whereWithin(key, southwest, northeast);
        return this;
    }

    public ParseQuery<T> whereMatches(String key, String regex) {
        checkIfRunning();
        this.builder.addCondition(key, "$regex", (Object) regex);
        return this;
    }

    public ParseQuery<T> whereMatches(String key, String regex, String modifiers) {
        checkIfRunning();
        this.builder.addCondition(key, "$regex", (Object) regex);
        if (modifiers.length() != 0) {
            this.builder.addCondition(key, "$options", (Object) modifiers);
        }
        return this;
    }

    public ParseQuery<T> whereContains(String key, String substring) {
        whereMatches(key, Pattern.quote(substring));
        return this;
    }

    public ParseQuery<T> whereStartsWith(String key, String prefix) {
        whereMatches(key, "^" + Pattern.quote(prefix));
        return this;
    }

    public ParseQuery<T> whereEndsWith(String key, String suffix) {
        whereMatches(key, Pattern.quote(suffix) + "$");
        return this;
    }

    public ParseQuery<T> include(String key) {
        checkIfRunning();
        this.builder.include(key);
        return this;
    }

    public ParseQuery<T> selectKeys(Collection<String> keys) {
        checkIfRunning();
        this.builder.selectKeys(keys);
        return this;
    }

    public ParseQuery<T> whereExists(String key) {
        checkIfRunning();
        this.builder.addCondition(key, "$exists", Boolean.valueOf(true));
        return this;
    }

    public ParseQuery<T> whereDoesNotExist(String key) {
        checkIfRunning();
        this.builder.addCondition(key, "$exists", Boolean.valueOf(false));
        return this;
    }

    public ParseQuery<T> orderByAscending(String key) {
        checkIfRunning();
        this.builder.orderByAscending(key);
        return this;
    }

    public ParseQuery<T> addAscendingOrder(String key) {
        checkIfRunning();
        this.builder.addAscendingOrder(key);
        return this;
    }

    public ParseQuery<T> orderByDescending(String key) {
        checkIfRunning();
        this.builder.orderByDescending(key);
        return this;
    }

    public ParseQuery<T> addDescendingOrder(String key) {
        checkIfRunning();
        this.builder.addDescendingOrder(key);
        return this;
    }

    public ParseQuery<T> setLimit(int newLimit) {
        checkIfRunning();
        this.builder.setLimit(newLimit);
        return this;
    }

    public int getLimit() {
        return this.builder.getLimit();
    }

    public ParseQuery<T> setSkip(int newSkip) {
        checkIfRunning();
        this.builder.setSkip(newSkip);
        return this;
    }

    public int getSkip() {
        return this.builder.getSkip();
    }

    public String getClassName() {
        return this.builder.getClassName();
    }

    public ParseQuery<T> setTrace(boolean shouldTrace) {
        checkIfRunning();
        this.builder.setTracingEnabled(shouldTrace);
        return this;
    }
}
