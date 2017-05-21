package com.parse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Pair;
import bolts.Capture;
import bolts.Continuation;
import bolts.Task;
import bolts.Task.TaskCompletionSource;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import org.json.JSONException;
import org.json.JSONObject;

class OfflineStore {
    private static final int MAX_SQL_VARIABLES = 999;
    private final WeakValueHashMap<Pair<String, String>, ParseObject> classNameAndObjectIdToObjectMap;
    private final WeakHashMap<ParseObject, Task<ParseObject>> fetchedObjects;
    private final OfflineSQLiteOpenHelper helper;
    private final Object lock;
    private final WeakHashMap<ParseObject, Task<String>> objectToUuidMap;
    private final WeakValueHashMap<String, ParseObject> uuidToObjectMap;

    private interface SQLiteDatabaseCallable<T> {
        T call(ParseSQLiteDatabase parseSQLiteDatabase);
    }

    private class OfflineDecoder extends ParseDecoder {
        private Map<String, Task<ParseObject>> offlineObjects;

        private OfflineDecoder(Map<String, Task<ParseObject>> offlineObjects) {
            this.offlineObjects = offlineObjects;
        }

        public Object decode(Object object) {
            if (!(object instanceof JSONObject) || !((JSONObject) object).optString("__type").equals("OfflineObject")) {
                return super.decode(object);
            }
            return ((Task) this.offlineObjects.get(((JSONObject) object).optString("uuid"))).getResult();
        }
    }

    private class OfflineEncoder extends ParseEncoder {
        private ParseSQLiteDatabase db;
        private ArrayList<Task<Void>> tasks = new ArrayList();
        private final Object tasksLock = new Object();

        public OfflineEncoder(ParseSQLiteDatabase db) {
            this.db = db;
        }

        public Task<Void> whenFinished() {
            return Task.whenAll(this.tasks).continueWithTask(new Continuation<Void, Task<Void>>() {
                public Task<Void> then(Task<Void> task) throws Exception {
                    Task<Void> task2;
                    synchronized (OfflineEncoder.this.tasksLock) {
                        Iterator i$ = OfflineEncoder.this.tasks.iterator();
                        while (i$.hasNext()) {
                            task2 = (Task) i$.next();
                            if (!task2.isFaulted()) {
                                if (task2.isCancelled()) {
                                }
                            }
                        }
                        OfflineEncoder.this.tasks.clear();
                        task2 = Task.forResult((Void) null);
                    }
                    return task2;
                }
            });
        }

        public JSONObject encodeRelatedObject(ParseObject object) {
            try {
                JSONObject result;
                if (object.getObjectId() != null) {
                    result = new JSONObject();
                    result.put("__type", "Pointer");
                    result.put("objectId", object.getObjectId());
                    result.put("className", object.getClassName());
                    return result;
                }
                result = new JSONObject();
                result.put("__type", "OfflineObject");
                synchronized (this.tasksLock) {
                    this.tasks.add(OfflineStore.this.getOrCreateUUIDAsync(object, this.db).onSuccess(new Continuation<String, Void>() {
                        public Void then(Task<String> task) throws Exception {
                            result.put("uuid", task.getResult());
                            return null;
                        }
                    }));
                }
                return result;
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    OfflineStore(Context context) {
        this(new OfflineSQLiteOpenHelper(context));
    }

    OfflineStore(OfflineSQLiteOpenHelper helper) {
        this.lock = new Object();
        this.uuidToObjectMap = new WeakValueHashMap();
        this.objectToUuidMap = new WeakHashMap();
        this.fetchedObjects = new WeakHashMap();
        this.classNameAndObjectIdToObjectMap = new WeakValueHashMap();
        this.helper = helper;
    }

    private Task<String> getOrCreateUUIDAsync(final ParseObject object, ParseSQLiteDatabase db) {
        final String newUUID = UUID.randomUUID().toString();
        final TaskCompletionSource tcs = Task.create();
        synchronized (this.lock) {
            Task<String> uuidTask = (Task) this.objectToUuidMap.get(object);
            if (uuidTask != null) {
                return uuidTask;
            }
            this.objectToUuidMap.put(object, tcs.getTask());
            this.uuidToObjectMap.put(newUUID, object);
            this.fetchedObjects.put(object, tcs.getTask().onSuccess(new Continuation<String, ParseObject>() {
                public ParseObject then(Task<String> task) throws Exception {
                    return object;
                }
            }));
            ContentValues values = new ContentValues();
            values.put("uuid", newUUID);
            values.put("className", object.getClassName());
            db.insertOrThrowAsync("ParseObjects", values).continueWith(new Continuation<Void, Void>() {
                public Void then(Task<Void> task) throws Exception {
                    tcs.setResult(newUUID);
                    return null;
                }
            });
            return tcs.getTask();
        }
    }

    private <T extends ParseObject> Task<T> getPointerAsync(final String uuid, ParseSQLiteDatabase db) {
        synchronized (this.lock) {
            ParseObject existing = (ParseObject) this.uuidToObjectMap.get(uuid);
            if (existing != null) {
                Task<T> forResult = Task.forResult(existing);
                return forResult;
            }
            return db.queryAsync("ParseObjects", new String[]{"className", "objectId"}, "uuid = ?", new String[]{uuid}).onSuccess(new Continuation<Cursor, T>() {
                /* JADX WARNING: inconsistent code. */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public T then(bolts.Task<android.database.Cursor> r9) throws java.lang.Exception {
                    /*
                    r8 = this;
                    r1 = r9.getResult();
                    r1 = (android.database.Cursor) r1;
                    r1.moveToFirst();
                    r5 = r1.isAfterLast();
                    if (r5 == 0) goto L_0x002d;
                L_0x000f:
                    r1.close();
                    r5 = new java.lang.IllegalStateException;
                    r6 = new java.lang.StringBuilder;
                    r6.<init>();
                    r7 = "Attempted to find non-existent uuid ";
                    r6 = r6.append(r7);
                    r7 = r9;
                    r6 = r6.append(r7);
                    r6 = r6.toString();
                    r5.<init>(r6);
                    throw r5;
                L_0x002d:
                    r5 = com.parse.OfflineStore.this;
                    r6 = r5.lock;
                    monitor-enter(r6);
                    r5 = com.parse.OfflineStore.this;	 Catch:{ all -> 0x0076 }
                    r5 = r5.uuidToObjectMap;	 Catch:{ all -> 0x0076 }
                    r7 = r9;	 Catch:{ all -> 0x0076 }
                    r2 = r5.get(r7);	 Catch:{ all -> 0x0076 }
                    r2 = (com.parse.ParseObject) r2;	 Catch:{ all -> 0x0076 }
                    if (r2 == 0) goto L_0x0046;
                L_0x0044:
                    monitor-exit(r6);	 Catch:{ all -> 0x0076 }
                L_0x0045:
                    return r2;
                L_0x0046:
                    r5 = 0;
                    r0 = r1.getString(r5);	 Catch:{ all -> 0x0076 }
                    r5 = 1;
                    r3 = r1.getString(r5);	 Catch:{ all -> 0x0076 }
                    r1.close();	 Catch:{ all -> 0x0076 }
                    r4 = com.parse.ParseObject.createWithoutData(r0, r3);	 Catch:{ all -> 0x0076 }
                    if (r3 != 0) goto L_0x0073;
                L_0x0059:
                    r5 = com.parse.OfflineStore.this;	 Catch:{ all -> 0x0076 }
                    r5 = r5.uuidToObjectMap;	 Catch:{ all -> 0x0076 }
                    r7 = r9;	 Catch:{ all -> 0x0076 }
                    r5.put(r7, r4);	 Catch:{ all -> 0x0076 }
                    r5 = com.parse.OfflineStore.this;	 Catch:{ all -> 0x0076 }
                    r5 = r5.objectToUuidMap;	 Catch:{ all -> 0x0076 }
                    r7 = r9;	 Catch:{ all -> 0x0076 }
                    r7 = bolts.Task.forResult(r7);	 Catch:{ all -> 0x0076 }
                    r5.put(r4, r7);	 Catch:{ all -> 0x0076 }
                L_0x0073:
                    monitor-exit(r6);	 Catch:{ all -> 0x0076 }
                    r2 = r4;
                    goto L_0x0045;
                L_0x0076:
                    r5 = move-exception;
                    monitor-exit(r6);	 Catch:{ all -> 0x0076 }
                    throw r5;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.parse.OfflineStore.3.then(bolts.Task):T");
                }
            });
        }
    }

    <T extends ParseObject> Task<List<T>> findAsync(State<T> query, ParseUser user, ParsePin pin, ParseSQLiteDatabase db) {
        return findAsync(query, user, pin, false, db);
    }

    private <T extends ParseObject> Task<List<T>> findAsync(State<T> query, ParseUser user, ParsePin pin, boolean isCount, ParseSQLiteDatabase db) {
        Task<Cursor> queryTask;
        final OfflineQueryLogic queryLogic = new OfflineQueryLogic(this);
        final List<T> results = new ArrayList();
        if (pin == null) {
            queryTask = db.queryAsync("ParseObjects", new String[]{"uuid"}, "className=?" + " AND isDeletingEventually=0", new String[]{query.className()});
        } else {
            Task<String> uuidTask = (Task) this.objectToUuidMap.get(pin);
            if (uuidTask == null) {
                return Task.forResult(results);
            }
            final State<T> state = query;
            final ParseSQLiteDatabase parseSQLiteDatabase = db;
            queryTask = uuidTask.onSuccessTask(new Continuation<String, Task<Cursor>>() {
                public Task<Cursor> then(Task<String> task) throws Exception {
                    String uuid = (String) task.getResult();
                    String[] select = new String[]{"A.uuid"};
                    String where = "className=? AND key=?" + " AND isDeletingEventually=0";
                    String[] args = new String[]{state.className(), uuid};
                    return parseSQLiteDatabase.queryAsync("ParseObjects A  INNER JOIN Dependencies B  ON A.uuid=B.uuid", select, where, args);
                }
            });
        }
        final State<T> state2 = query;
        final ParseUser parseUser = user;
        final ParseSQLiteDatabase parseSQLiteDatabase2 = db;
        final State<T> state3 = query;
        final boolean z = isCount;
        final OfflineQueryLogic offlineQueryLogic = queryLogic;
        final ParseSQLiteDatabase parseSQLiteDatabase3 = db;
        return queryTask.onSuccessTask(new Continuation<Cursor, Task<Void>>() {
            public Task<Void> then(Task<Cursor> task) throws Exception {
                Cursor cursor = (Cursor) task.getResult();
                List<String> uuids = new ArrayList();
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    uuids.add(cursor.getString(0));
                    cursor.moveToNext();
                }
                cursor.close();
                final ConstraintMatcher<T> matcher = queryLogic.createMatcher(state2, parseUser);
                Task<Void> checkedAllObjects = Task.forResult(null);
                for (final String uuid : uuids) {
                    final Capture<T> object = new Capture();
                    checkedAllObjects = checkedAllObjects.onSuccessTask(new Continuation<Void, Task<T>>() {
                        public Task<T> then(Task<Void> task) throws Exception {
                            return OfflineStore.this.getPointerAsync(uuid, parseSQLiteDatabase2);
                        }
                    }).onSuccessTask(new Continuation<T, Task<T>>() {
                        public Task<T> then(Task<T> task) throws Exception {
                            object.set(task.getResult());
                            return OfflineStore.this.fetchLocallyAsync((ParseObject) object.get(), parseSQLiteDatabase2);
                        }
                    }).onSuccessTask(new Continuation<T, Task<Boolean>>() {
                        public Task<Boolean> then(Task<T> task) throws Exception {
                            if (((ParseObject) object.get()).isDataAvailable()) {
                                return matcher.matchesAsync((ParseObject) object.get(), parseSQLiteDatabase2);
                            }
                            return Task.forResult(Boolean.valueOf(false));
                        }
                    }).onSuccess(new Continuation<Boolean, Void>() {
                        public Void then(Task<Boolean> task) {
                            if (((Boolean) task.getResult()).booleanValue()) {
                                results.add(object.get());
                            }
                            return null;
                        }
                    });
                }
                return checkedAllObjects;
            }
        }).onSuccessTask(new Continuation<Void, Task<List<T>>>() {
            public Task<List<T>> then(Task<Void> task) throws Exception {
                OfflineQueryLogic.sort(results, state3);
                List<T> trimmedResults = results;
                int skip = state3.skip();
                if (!z && skip >= 0) {
                    trimmedResults = trimmedResults.subList(Math.min(state3.skip(), trimmedResults.size()), trimmedResults.size());
                }
                int limit = state3.limit();
                if (!z && limit >= 0 && trimmedResults.size() > limit) {
                    trimmedResults = trimmedResults.subList(0, limit);
                }
                Task<Void> fetchedIncludesTask = Task.forResult(null);
                for (final T object : trimmedResults) {
                    fetchedIncludesTask = fetchedIncludesTask.onSuccessTask(new Continuation<Void, Task<Void>>() {
                        public Task<Void> then(Task<Void> task) throws Exception {
                            return offlineQueryLogic.fetchIncludesAsync(object, state3, parseSQLiteDatabase3);
                        }
                    });
                }
                final List<T> finalTrimmedResults = trimmedResults;
                return fetchedIncludesTask.onSuccess(new Continuation<Void, List<T>>() {
                    public List<T> then(Task<Void> task) throws Exception {
                        return finalTrimmedResults;
                    }
                });
            }
        });
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    <T extends com.parse.ParseObject> bolts.Task<T> fetchLocallyAsync(final T r14, final com.parse.ParseSQLiteDatabase r15) {
        /*
        r13 = this;
        r5 = bolts.Task.create();
        r10 = r13.lock;
        monitor-enter(r10);
        r9 = r13.fetchedObjects;	 Catch:{ all -> 0x004f }
        r9 = r9.containsKey(r14);	 Catch:{ all -> 0x004f }
        if (r9 == 0) goto L_0x0019;
    L_0x000f:
        r9 = r13.fetchedObjects;	 Catch:{ all -> 0x004f }
        r9 = r9.get(r14);	 Catch:{ all -> 0x004f }
        r9 = (bolts.Task) r9;	 Catch:{ all -> 0x004f }
        monitor-exit(r10);	 Catch:{ all -> 0x004f }
    L_0x0018:
        return r9;
    L_0x0019:
        r9 = r13.fetchedObjects;	 Catch:{ all -> 0x004f }
        r11 = r5.getTask();	 Catch:{ all -> 0x004f }
        r9.put(r14, r11);	 Catch:{ all -> 0x004f }
        r9 = r13.objectToUuidMap;	 Catch:{ all -> 0x004f }
        r7 = r9.get(r14);	 Catch:{ all -> 0x004f }
        r7 = (bolts.Task) r7;	 Catch:{ all -> 0x004f }
        monitor-exit(r10);	 Catch:{ all -> 0x004f }
        r1 = r14.getClassName();
        r3 = r14.getObjectId();
        r9 = 0;
        r2 = bolts.Task.forResult(r9);
        if (r3 != 0) goto L_0x0074;
    L_0x003a:
        if (r7 != 0) goto L_0x0052;
    L_0x003c:
        r9 = new com.parse.OfflineStore$11;
        r9.<init>(r15, r14);
        r9 = r2.onSuccessTask(r9);
        r10 = new com.parse.OfflineStore$10;
        r10.<init>(r5, r14);
        r9 = r9.continueWithTask(r10);
        goto L_0x0018;
    L_0x004f:
        r9 = move-exception;
        monitor-exit(r10);	 Catch:{ all -> 0x004f }
        throw r9;
    L_0x0052:
        r9 = 1;
        r4 = new java.lang.String[r9];
        r9 = 0;
        r10 = "json";
        r4[r9] = r10;
        r8 = "uuid = ?";
        r6 = new bolts.Capture;
        r6.<init>();
        r9 = new com.parse.OfflineStore$8;
        r9.<init>(r6, r15, r4);
        r9 = r7.onSuccessTask(r9);
        r10 = new com.parse.OfflineStore$7;
        r10.<init>(r6);
        r2 = r9.onSuccess(r10);
        goto L_0x003c;
    L_0x0074:
        if (r7 == 0) goto L_0x0091;
    L_0x0076:
        r9 = new java.lang.IllegalStateException;
        r10 = "This object must have already been fetched from the local datastore, but isn't marked as fetched.";
        r9.<init>(r10);
        r5.setError(r9);
        r10 = r13.lock;
        monitor-enter(r10);
        r9 = r13.fetchedObjects;	 Catch:{ all -> 0x008e }
        r9.remove(r14);	 Catch:{ all -> 0x008e }
        monitor-exit(r10);	 Catch:{ all -> 0x008e }
        r9 = r5.getTask();
        goto L_0x0018;
    L_0x008e:
        r9 = move-exception;
        monitor-exit(r10);	 Catch:{ all -> 0x008e }
        throw r9;
    L_0x0091:
        r9 = 2;
        r4 = new java.lang.String[r9];
        r9 = 0;
        r10 = "json";
        r4[r9] = r10;
        r9 = 1;
        r10 = "uuid";
        r4[r9] = r10;
        r9 = "%s = ? AND %s = ?";
        r10 = 2;
        r10 = new java.lang.Object[r10];
        r11 = 0;
        r12 = "className";
        r10[r11] = r12;
        r11 = 1;
        r12 = "objectId";
        r10[r11] = r12;
        r8 = java.lang.String.format(r9, r10);
        r9 = 2;
        r0 = new java.lang.String[r9];
        r9 = 0;
        r0[r9] = r1;
        r9 = 1;
        r0[r9] = r3;
        r9 = "ParseObjects";
        r9 = r15.queryAsync(r9, r4, r8, r0);
        r10 = new com.parse.OfflineStore$9;
        r10.<init>(r14);
        r2 = r9.onSuccess(r10);
        goto L_0x003c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.parse.OfflineStore.fetchLocallyAsync(com.parse.ParseObject, com.parse.ParseSQLiteDatabase):bolts.Task<T>");
    }

    <T extends ParseObject> Task<T> fetchLocallyAsync(final T object) {
        return runWithManagedConnection(new SQLiteDatabaseCallable<Task<T>>() {
            public Task<T> call(ParseSQLiteDatabase db) {
                return OfflineStore.this.fetchLocallyAsync(object, db);
            }
        });
    }

    private Task<Void> saveLocallyAsync(final String key, final ParseObject object, final ParseSQLiteDatabase db) {
        if (object.getObjectId() != null && !object.isDataAvailable() && !object.hasChanges() && !object.hasOutstandingOperations()) {
            return Task.forResult(null);
        }
        final Capture<String> uuidCapture = new Capture();
        return getOrCreateUUIDAsync(object, db).onSuccessTask(new Continuation<String, Task<Void>>() {
            public Task<Void> then(Task<String> task) throws Exception {
                String uuid = (String) task.getResult();
                uuidCapture.set(uuid);
                return OfflineStore.this.updateDataForObjectAsync(uuid, object, db);
            }
        }).onSuccessTask(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> task) throws Exception {
                ContentValues values = new ContentValues();
                values.put(ParametersKeys.KEY, key);
                values.put("uuid", (String) uuidCapture.get());
                return db.insertWithOnConflict("Dependencies", values, 4);
            }
        });
    }

    private Task<Void> saveLocallyAsync(ParseObject object, boolean includeAllChildren, ParseSQLiteDatabase db) {
        final List objectsInTree = new ArrayList();
        if (includeAllChildren) {
            new ParseTraverser() {
                protected boolean visit(Object object) {
                    if (object instanceof ParseObject) {
                        objectsInTree.add((ParseObject) object);
                    }
                    return true;
                }
            }.setYieldRoot(true).setTraverseParseObjects(true).traverse(object);
        } else {
            objectsInTree.add(object);
        }
        return saveLocallyAsync(object, objectsInTree, db);
    }

    private Task<Void> saveLocallyAsync(final ParseObject object, List<ParseObject> children, final ParseSQLiteDatabase db) {
        List<ParseObject> objects;
        if (children != null) {
            objects = new ArrayList(children);
        } else {
            objects = new ArrayList();
        }
        if (!objects.contains(object)) {
            objects.add(object);
        }
        List<Task<Void>> tasks = new ArrayList();
        for (ParseObject obj : objects) {
            tasks.add(fetchLocallyAsync(obj, db).makeVoid());
        }
        return Task.whenAll(tasks).continueWithTask(new Continuation<Void, Task<String>>() {
            public Task<String> then(Task<Void> task) throws Exception {
                return (Task) OfflineStore.this.objectToUuidMap.get(object);
            }
        }).onSuccessTask(new Continuation<String, Task<Void>>() {
            public Task<Void> then(Task<String> task) throws Exception {
                String uuid = (String) task.getResult();
                if (uuid == null) {
                    return null;
                }
                return OfflineStore.this.unpinAsync(uuid, db);
            }
        }).onSuccessTask(new Continuation<Void, Task<String>>() {
            public Task<String> then(Task<Void> task) throws Exception {
                return OfflineStore.this.getOrCreateUUIDAsync(object, db);
            }
        }).onSuccessTask(new Continuation<String, Task<Void>>() {
            public Task<Void> then(Task<String> task) throws Exception {
                String uuid = (String) task.getResult();
                List<Task<Void>> tasks = new ArrayList();
                for (ParseObject obj : objects) {
                    tasks.add(OfflineStore.this.saveLocallyAsync(uuid, obj, db));
                }
                return Task.whenAll(tasks);
            }
        });
    }

    private Task<Void> unpinAsync(ParseObject object, final ParseSQLiteDatabase db) {
        Task<String> uuidTask = (Task) this.objectToUuidMap.get(object);
        if (uuidTask == null) {
            return Task.forResult(null);
        }
        return uuidTask.continueWithTask(new Continuation<String, Task<Void>>() {
            public Task<Void> then(Task<String> task) throws Exception {
                String uuid = (String) task.getResult();
                if (uuid == null) {
                    return Task.forResult(null);
                }
                return OfflineStore.this.unpinAsync(uuid, db);
            }
        });
    }

    private Task<Void> unpinAsync(final String key, final ParseSQLiteDatabase db) {
        final List<String> uuidsToDelete = new LinkedList();
        return Task.forResult((Void) null).continueWithTask(new Continuation<Void, Task<Cursor>>() {
            public Task<Cursor> then(Task<Void> task) throws Exception {
                String[] args = new String[]{key};
                return db.rawQueryAsync("SELECT uuid FROM Dependencies WHERE key=? AND uuid IN ( SELECT uuid FROM Dependencies GROUP BY uuid HAVING COUNT(uuid)=1)", args);
            }
        }).onSuccessTask(new Continuation<Cursor, Task<Void>>() {
            public Task<Void> then(Task<Cursor> task) throws Exception {
                Cursor cursor = (Cursor) task.getResult();
                while (cursor.moveToNext()) {
                    uuidsToDelete.add(cursor.getString(0));
                }
                cursor.close();
                return OfflineStore.this.deleteObjects(uuidsToDelete, db);
            }
        }).onSuccessTask(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> task) throws Exception {
                String[] args = new String[]{key};
                return db.deleteAsync("Dependencies", "key=?", args);
            }
        }).onSuccess(new Continuation<Void, Void>() {
            public Void then(Task<Void> task) throws Exception {
                synchronized (OfflineStore.this.lock) {
                    for (String uuid : uuidsToDelete) {
                        ParseObject object = (ParseObject) OfflineStore.this.uuidToObjectMap.get(uuid);
                        if (object != null) {
                            OfflineStore.this.objectToUuidMap.remove(object);
                            OfflineStore.this.uuidToObjectMap.remove(uuid);
                        }
                    }
                }
                return null;
            }
        });
    }

    private Task<Void> deleteObjects(final List<String> uuids, final ParseSQLiteDatabase db) {
        if (uuids.size() <= 0) {
            return Task.forResult(null);
        }
        if (uuids.size() > MAX_SQL_VARIABLES) {
            return deleteObjects(uuids.subList(0, MAX_SQL_VARIABLES), db).onSuccessTask(new Continuation<Void, Task<Void>>() {
                public Task<Void> then(Task<Void> task) throws Exception {
                    return OfflineStore.this.deleteObjects(uuids.subList(OfflineStore.MAX_SQL_VARIABLES, uuids.size()), db);
                }
            });
        }
        String[] placeholders = new String[uuids.size()];
        for (int i = 0; i < placeholders.length; i++) {
            placeholders[i] = "?";
        }
        return db.deleteAsync("ParseObjects", "uuid IN (" + TextUtils.join(",", placeholders) + ")", (String[]) uuids.toArray(new String[uuids.size()]));
    }

    Task<Void> updateDataForObjectAsync(final ParseObject object) {
        synchronized (this.lock) {
            Task<ParseObject> fetched = (Task) this.fetchedObjects.get(object);
            if (fetched == null) {
                Task<Void> forError = Task.forError(new IllegalStateException("An object cannot be updated if it wasn't fetched."));
                return forError;
            }
            return fetched.continueWithTask(new Continuation<ParseObject, Task<Void>>() {
                public Task<Void> then(Task<ParseObject> task) throws Exception {
                    if (!task.isFaulted()) {
                        return OfflineStore.this.helper.getWritableDatabaseAsync().continueWithTask(new Continuation<ParseSQLiteDatabase, Task<Void>>() {
                            public Task<Void> then(Task<ParseSQLiteDatabase> task) throws Exception {
                                final ParseSQLiteDatabase db = (ParseSQLiteDatabase) task.getResult();
                                return db.beginTransactionAsync().onSuccessTask(new Continuation<Void, Task<Void>>() {
                                    public Task<Void> then(Task<Void> task) throws Exception {
                                        return OfflineStore.this.updateDataForObjectAsync(object, db).onSuccessTask(new Continuation<Void, Task<Void>>() {
                                            public Task<Void> then(Task<Void> task) throws Exception {
                                                return db.setTransactionSuccessfulAsync();
                                            }
                                        }).continueWithTask(new Continuation<Void, Task<Void>>() {
                                            public Task<Void> then(Task<Void> task) throws Exception {
                                                db.endTransactionAsync();
                                                db.closeAsync();
                                                return task;
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                    if ((task.getError() instanceof ParseException) && ((ParseException) task.getError()).getCode() == ParseException.CACHE_MISS) {
                        return Task.forResult(null);
                    }
                    return task.makeVoid();
                }
            });
        }
    }

    private Task<Void> updateDataForObjectAsync(final ParseObject object, final ParseSQLiteDatabase db) {
        synchronized (this.lock) {
            Task<String> uuidTask = (Task) this.objectToUuidMap.get(object);
            if (uuidTask == null) {
                Task<Void> forResult = Task.forResult(null);
                return forResult;
            }
            return uuidTask.onSuccessTask(new Continuation<String, Task<Void>>() {
                public Task<Void> then(Task<String> task) throws Exception {
                    return OfflineStore.this.updateDataForObjectAsync((String) task.getResult(), object, db);
                }
            });
        }
    }

    private Task<Void> updateDataForObjectAsync(String uuid, ParseObject object, ParseSQLiteDatabase db) {
        OfflineEncoder encoder = new OfflineEncoder(db);
        final JSONObject json = object.toRest(encoder);
        final ParseObject parseObject = object;
        final String str = uuid;
        final ParseSQLiteDatabase parseSQLiteDatabase = db;
        return encoder.whenFinished().onSuccessTask(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> task) throws Exception {
                String className = parseObject.getClassName();
                String objectId = parseObject.getObjectId();
                int isDeletingEventually = json.getInt("__isDeletingEventually");
                ContentValues values = new ContentValues();
                values.put("className", className);
                values.put("json", json.toString());
                if (objectId != null) {
                    values.put("objectId", objectId);
                }
                values.put("isDeletingEventually", Integer.valueOf(isDeletingEventually));
                String[] args = new String[]{str};
                return parseSQLiteDatabase.updateAsync("ParseObjects", values, "uuid = ?", args).makeVoid();
            }
        });
    }

    Task<Void> deleteDataForObjectAsync(final ParseObject object) {
        return this.helper.getWritableDatabaseAsync().continueWithTask(new Continuation<ParseSQLiteDatabase, Task<Void>>() {
            public Task<Void> then(Task<ParseSQLiteDatabase> task) throws Exception {
                final ParseSQLiteDatabase db = (ParseSQLiteDatabase) task.getResult();
                return db.beginTransactionAsync().onSuccessTask(new Continuation<Void, Task<Void>>() {
                    public Task<Void> then(Task<Void> task) throws Exception {
                        return OfflineStore.this.deleteDataForObjectAsync(object, db).onSuccessTask(new Continuation<Void, Task<Void>>() {
                            public Task<Void> then(Task<Void> task) throws Exception {
                                return db.setTransactionSuccessfulAsync();
                            }
                        }).continueWithTask(new Continuation<Void, Task<Void>>() {
                            public Task<Void> then(Task<Void> task) throws Exception {
                                db.endTransactionAsync();
                                db.closeAsync();
                                return task;
                            }
                        });
                    }
                });
            }
        });
    }

    private Task<Void> deleteDataForObjectAsync(final ParseObject object, final ParseSQLiteDatabase db) {
        final Capture<String> uuid = new Capture();
        synchronized (this.lock) {
            Task<String> uuidTask = (Task) this.objectToUuidMap.get(object);
            if (uuidTask == null) {
                Task<Void> forResult = Task.forResult(null);
                return forResult;
            }
            return uuidTask.onSuccessTask(new Continuation<String, Task<String>>() {
                public Task<String> then(Task<String> task) throws Exception {
                    uuid.set(task.getResult());
                    return task;
                }
            }).onSuccessTask(new Continuation<String, Task<Cursor>>() {
                public Task<Cursor> then(Task<String> task) throws Exception {
                    String[] select = new String[]{ParametersKeys.KEY};
                    String[] args = new String[]{(String) uuid.get()};
                    return db.queryAsync("Dependencies", select, "uuid=?", args);
                }
            }).onSuccessTask(new Continuation<Cursor, Task<Void>>() {
                public Task<Void> then(Task<Cursor> task) throws Exception {
                    Cursor cursor = (Cursor) task.getResult();
                    List<String> uuids = new ArrayList();
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        uuids.add(cursor.getString(0));
                        cursor.moveToNext();
                    }
                    cursor.close();
                    List<Task<Void>> tasks = new ArrayList();
                    for (final String uuid : uuids) {
                        tasks.add(OfflineStore.this.getPointerAsync(uuid, db).onSuccessTask(new Continuation<ParseObject, Task<ParsePin>>() {
                            public Task<ParsePin> then(Task<ParseObject> task) throws Exception {
                                return OfflineStore.this.fetchLocallyAsync((ParsePin) task.getResult(), db);
                            }
                        }).continueWithTask(new Continuation<ParsePin, Task<Void>>() {
                            public Task<Void> then(Task<ParsePin> task) throws Exception {
                                ParsePin pin = (ParsePin) task.getResult();
                                List<ParseObject> modified = pin.getObjects();
                                if (modified == null || !modified.contains(object)) {
                                    return task.makeVoid();
                                }
                                modified.remove(object);
                                if (modified.size() == 0) {
                                    return OfflineStore.this.unpinAsync(uuid, db);
                                }
                                pin.setObjects(modified);
                                return OfflineStore.this.saveLocallyAsync((ParseObject) pin, true, db);
                            }
                        }));
                    }
                    return Task.whenAll(tasks);
                }
            }).onSuccessTask(new Continuation<Void, Task<Void>>() {
                public Task<Void> then(Task<Void> task) throws Exception {
                    String[] args = new String[]{(String) uuid.get()};
                    return db.deleteAsync("Dependencies", "uuid=?", args);
                }
            }).onSuccessTask(new Continuation<Void, Task<Void>>() {
                public Task<Void> then(Task<Void> task) throws Exception {
                    String[] args = new String[]{(String) uuid.get()};
                    return db.deleteAsync("ParseObjects", "uuid=?", args);
                }
            }).onSuccessTask(new Continuation<Void, Task<Void>>() {
                public Task<Void> then(Task<Void> task) throws Exception {
                    synchronized (OfflineStore.this.lock) {
                        OfflineStore.this.fetchedObjects.remove(object);
                    }
                    return task;
                }
            });
        }
    }

    private Task<ParsePin> getParsePin(final String name, ParseSQLiteDatabase db) {
        return findAsync(new Builder(ParsePin.class).whereEqualTo("_name", name).build(), null, null, db).onSuccess(new Continuation<List<ParsePin>, ParsePin>() {
            public ParsePin then(Task<List<ParsePin>> task) throws Exception {
                ParsePin pin = null;
                if (task.getResult() != null && ((List) task.getResult()).size() > 0) {
                    pin = (ParsePin) ((List) task.getResult()).get(0);
                }
                if (pin != null) {
                    return pin;
                }
                pin = (ParsePin) ParseObject.create(ParsePin.class);
                pin.setName(name);
                return pin;
            }
        });
    }

    <T extends ParseObject> Task<Void> pinAllObjectsAsync(final String name, final List<T> objects, final boolean includeChildren) {
        return runWithManagedTransaction(new SQLiteDatabaseCallable<Task<Void>>() {
            public Task<Void> call(ParseSQLiteDatabase db) {
                return OfflineStore.this.pinAllObjectsAsync(name, objects, includeChildren, db);
            }
        });
    }

    private <T extends ParseObject> Task<Void> pinAllObjectsAsync(String name, final List<T> objects, final boolean includeChildren, final ParseSQLiteDatabase db) {
        if (objects == null || objects.size() == 0) {
            return Task.forResult(null);
        }
        return getParsePin(name, db).onSuccessTask(new Continuation<ParsePin, Task<Void>>() {
            public Task<Void> then(Task<ParsePin> task) throws Exception {
                ParsePin pin = (ParsePin) task.getResult();
                List<ParseObject> modified = pin.getObjects();
                if (modified == null) {
                    modified = new ArrayList(objects);
                } else {
                    for (ParseObject object : objects) {
                        if (!modified.contains(object)) {
                            modified.add(object);
                        }
                    }
                }
                pin.setObjects(modified);
                if (includeChildren) {
                    return OfflineStore.this.saveLocallyAsync((ParseObject) pin, true, db);
                }
                return OfflineStore.this.saveLocallyAsync((ParseObject) pin, pin.getObjects(), db);
            }
        });
    }

    <T extends ParseObject> Task<Void> unpinAllObjectsAsync(final String name, final List<T> objects) {
        return runWithManagedTransaction(new SQLiteDatabaseCallable<Task<Void>>() {
            public Task<Void> call(ParseSQLiteDatabase db) {
                return OfflineStore.this.unpinAllObjectsAsync(name, objects, db);
            }
        });
    }

    private <T extends ParseObject> Task<Void> unpinAllObjectsAsync(String name, final List<T> objects, final ParseSQLiteDatabase db) {
        if (objects == null || objects.size() == 0) {
            return Task.forResult(null);
        }
        return getParsePin(name, db).onSuccessTask(new Continuation<ParsePin, Task<Void>>() {
            public Task<Void> then(Task<ParsePin> task) throws Exception {
                ParsePin pin = (ParsePin) task.getResult();
                List<ParseObject> modified = pin.getObjects();
                if (modified == null) {
                    return Task.forResult(null);
                }
                modified.removeAll(objects);
                if (modified.size() == 0) {
                    return OfflineStore.this.unpinAsync((ParseObject) pin, db);
                }
                pin.setObjects(modified);
                return OfflineStore.this.saveLocallyAsync((ParseObject) pin, true, db);
            }
        });
    }

    Task<Void> unpinAllObjectsAsync(final String name) {
        return runWithManagedTransaction(new SQLiteDatabaseCallable<Task<Void>>() {
            public Task<Void> call(ParseSQLiteDatabase db) {
                return OfflineStore.this.unpinAllObjectsAsync(name, db);
            }
        });
    }

    private Task<Void> unpinAllObjectsAsync(String name, final ParseSQLiteDatabase db) {
        return getParsePin(name, db).continueWithTask(new Continuation<ParsePin, Task<Void>>() {
            public Task<Void> then(Task<ParsePin> task) throws Exception {
                if (task.isFaulted()) {
                    return task.makeVoid();
                }
                return OfflineStore.this.unpinAsync((ParseObject) (ParsePin) task.getResult(), db);
            }
        });
    }

    <T extends ParseObject> Task<List<T>> findFromPinAsync(final String name, final State<T> state, final ParseUser user) {
        return runWithManagedConnection(new SQLiteDatabaseCallable<Task<List<T>>>() {
            public Task<List<T>> call(ParseSQLiteDatabase db) {
                return OfflineStore.this.findFromPinAsync(name, state, user, db);
            }
        });
    }

    private <T extends ParseObject> Task<List<T>> findFromPinAsync(String name, final State<T> state, final ParseUser user, final ParseSQLiteDatabase db) {
        Task<ParsePin> task;
        if (name != null) {
            task = getParsePin(name, db);
        } else {
            task = Task.forResult(null);
        }
        return task.onSuccessTask(new Continuation<ParsePin, Task<List<T>>>() {
            public Task<List<T>> then(Task<ParsePin> task) throws Exception {
                return OfflineStore.this.findAsync(state, user, (ParsePin) task.getResult(), false, db);
            }
        });
    }

    <T extends ParseObject> Task<Integer> countFromPinAsync(final String name, final State<T> state, final ParseUser user) {
        return runWithManagedConnection(new SQLiteDatabaseCallable<Task<Integer>>() {
            public Task<Integer> call(ParseSQLiteDatabase db) {
                return OfflineStore.this.countFromPinAsync(name, state, user, db);
            }
        });
    }

    private <T extends ParseObject> Task<Integer> countFromPinAsync(String name, final State<T> state, final ParseUser user, final ParseSQLiteDatabase db) {
        Task<ParsePin> task;
        if (name != null) {
            task = getParsePin(name, db);
        } else {
            task = Task.forResult(null);
        }
        return task.onSuccessTask(new Continuation<ParsePin, Task<Integer>>() {
            public Task<Integer> then(Task<ParsePin> task) throws Exception {
                return OfflineStore.this.findAsync(state, user, (ParsePin) task.getResult(), true, db).onSuccess(new Continuation<List<T>, Integer>() {
                    public Integer then(Task<List<T>> task) throws Exception {
                        return Integer.valueOf(((List) task.getResult()).size());
                    }
                });
            }
        });
    }

    void registerNewObject(ParseObject object) {
        synchronized (this.lock) {
            String objectId = object.getObjectId();
            if (objectId != null) {
                this.classNameAndObjectIdToObjectMap.put(Pair.create(object.getClassName(), objectId), object);
            }
        }
    }

    void unregisterObject(ParseObject object) {
        synchronized (this.lock) {
            String objectId = object.getObjectId();
            if (objectId != null) {
                this.classNameAndObjectIdToObjectMap.remove(Pair.create(object.getClassName(), objectId));
            }
        }
    }

    ParseObject getObject(String className, String objectId) {
        if (objectId == null) {
            throw new IllegalStateException("objectId cannot be null.");
        }
        ParseObject parseObject;
        Pair<String, String> classNameAndObjectId = Pair.create(className, objectId);
        synchronized (this.lock) {
            parseObject = (ParseObject) this.classNameAndObjectIdToObjectMap.get(classNameAndObjectId);
        }
        return parseObject;
    }

    void updateObjectId(ParseObject object, String oldObjectId, String newObjectId) {
        if (oldObjectId == null) {
            Pair<String, String> classNameAndNewObjectId = Pair.create(object.getClassName(), newObjectId);
            synchronized (this.lock) {
                ParseObject existing = (ParseObject) this.classNameAndObjectIdToObjectMap.get(classNameAndNewObjectId);
                if (existing == null || existing == object) {
                    this.classNameAndObjectIdToObjectMap.put(classNameAndNewObjectId, object);
                } else {
                    throw new RuntimeException("Attempted to change an objectId to one that's already known to the Offline Store.");
                }
            }
        } else if (!oldObjectId.equals(newObjectId)) {
            throw new RuntimeException("objectIds cannot be changed in offline mode.");
        }
    }

    private <T> Task<T> runWithManagedConnection(final SQLiteDatabaseCallable<Task<T>> callable) {
        return this.helper.getWritableDatabaseAsync().onSuccessTask(new Continuation<ParseSQLiteDatabase, Task<T>>() {
            public Task<T> then(Task<ParseSQLiteDatabase> task) throws Exception {
                final ParseSQLiteDatabase db = (ParseSQLiteDatabase) task.getResult();
                return ((Task) callable.call(db)).continueWithTask(new Continuation<T, Task<T>>() {
                    public Task<T> then(Task<T> task) throws Exception {
                        db.closeAsync();
                        return task;
                    }
                });
            }
        });
    }

    private Task<Void> runWithManagedTransaction(final SQLiteDatabaseCallable<Task<Void>> callable) {
        return this.helper.getWritableDatabaseAsync().onSuccessTask(new Continuation<ParseSQLiteDatabase, Task<Void>>() {
            public Task<Void> then(Task<ParseSQLiteDatabase> task) throws Exception {
                final ParseSQLiteDatabase db = (ParseSQLiteDatabase) task.getResult();
                return db.beginTransactionAsync().onSuccessTask(new Continuation<Void, Task<Void>>() {
                    public Task<Void> then(Task<Void> task) throws Exception {
                        return ((Task) callable.call(db)).onSuccessTask(new Continuation<Void, Task<Void>>() {
                            public Task<Void> then(Task<Void> task) throws Exception {
                                return db.setTransactionSuccessfulAsync();
                            }
                        }).continueWithTask(new Continuation<Void, Task<Void>>() {
                            public Task<Void> then(Task<Void> task) throws Exception {
                                db.endTransactionAsync();
                                db.closeAsync();
                                return task;
                            }
                        });
                    }
                });
            }
        });
    }

    void simulateReboot() {
        synchronized (this.lock) {
            this.uuidToObjectMap.clear();
            this.objectToUuidMap.clear();
            this.classNameAndObjectIdToObjectMap.clear();
            this.fetchedObjects.clear();
        }
    }

    void clearDatabase(Context context) {
        this.helper.clearDatabase(context);
    }
}
