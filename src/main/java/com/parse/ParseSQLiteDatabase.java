package com.parse;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import bolts.Continuation;
import bolts.Task;
import bolts.Task.TaskCompletionSource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ParseSQLiteDatabase {
    private static final ExecutorService dbExecutor = Executors.newSingleThreadExecutor();
    private static final TaskQueue taskQueue = new TaskQueue();
    private Task<Void> current = null;
    private final Object currentLock = new Object();
    private SQLiteDatabase db;
    private int openFlags;
    private final TaskCompletionSource tcs = Task.create();

    static Task<ParseSQLiteDatabase> openDatabaseAsync(SQLiteOpenHelper helper, int flags) {
        final ParseSQLiteDatabase db = new ParseSQLiteDatabase(flags);
        return db.open(helper).continueWithTask(new Continuation<Void, Task<ParseSQLiteDatabase>>() {
            public Task<ParseSQLiteDatabase> then(Task<Void> task) throws Exception {
                return Task.forResult(db);
            }
        });
    }

    private ParseSQLiteDatabase(int flags) {
        this.openFlags = flags;
        taskQueue.enqueue(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> toAwait) throws Exception {
                synchronized (ParseSQLiteDatabase.this.currentLock) {
                    ParseSQLiteDatabase.this.current = toAwait;
                }
                return ParseSQLiteDatabase.this.tcs.getTask();
            }
        });
    }

    public Task<Boolean> isReadOnlyAsync() {
        Task<Boolean> task;
        synchronized (this.currentLock) {
            task = this.current.continueWith(new Continuation<Void, Boolean>() {
                public Boolean then(Task<Void> task) throws Exception {
                    return Boolean.valueOf(ParseSQLiteDatabase.this.db.isReadOnly());
                }
            });
            this.current = task.makeVoid();
        }
        return task;
    }

    public Task<Boolean> isOpenAsync() {
        Task<Boolean> task;
        synchronized (this.currentLock) {
            task = this.current.continueWith(new Continuation<Void, Boolean>() {
                public Boolean then(Task<Void> task) throws Exception {
                    return Boolean.valueOf(ParseSQLiteDatabase.this.db.isOpen());
                }
            });
            this.current = task.makeVoid();
        }
        return task;
    }

    public boolean inTransaction() {
        return this.db.inTransaction();
    }

    Task<Void> open(final SQLiteOpenHelper helper) {
        Task<Void> task;
        synchronized (this.currentLock) {
            this.current = this.current.continueWith(new Continuation<Void, SQLiteDatabase>() {
                public SQLiteDatabase then(Task<Void> task) throws Exception {
                    return (ParseSQLiteDatabase.this.openFlags & 1) == 1 ? helper.getReadableDatabase() : helper.getWritableDatabase();
                }
            }, dbExecutor).continueWithTask(new Continuation<SQLiteDatabase, Task<Void>>() {
                public Task<Void> then(Task<SQLiteDatabase> task) throws Exception {
                    ParseSQLiteDatabase.this.db = (SQLiteDatabase) task.getResult();
                    return task.makeVoid();
                }
            }, Task.BACKGROUND_EXECUTOR);
            task = this.current;
        }
        return task;
    }

    public Task<Void> beginTransactionAsync() {
        Task<Void> continueWithTask;
        synchronized (this.currentLock) {
            this.current = this.current.continueWithTask(new Continuation<Void, Task<Void>>() {
                public Task<Void> then(Task<Void> task) throws Exception {
                    ParseSQLiteDatabase.this.db.beginTransaction();
                    return task;
                }
            }, dbExecutor);
            continueWithTask = this.current.continueWithTask(new Continuation<Void, Task<Void>>() {
                public Task<Void> then(Task<Void> task) throws Exception {
                    return task;
                }
            }, Task.BACKGROUND_EXECUTOR);
        }
        return continueWithTask;
    }

    public Task<Void> setTransactionSuccessfulAsync() {
        Task<Void> continueWithTask;
        synchronized (this.currentLock) {
            this.current = this.current.onSuccessTask(new Continuation<Void, Task<Void>>() {
                public Task<Void> then(Task<Void> task) throws Exception {
                    ParseSQLiteDatabase.this.db.setTransactionSuccessful();
                    return task;
                }
            }, dbExecutor);
            continueWithTask = this.current.continueWithTask(new Continuation<Void, Task<Void>>() {
                public Task<Void> then(Task<Void> task) throws Exception {
                    return task;
                }
            }, Task.BACKGROUND_EXECUTOR);
        }
        return continueWithTask;
    }

    public Task<Void> endTransactionAsync() {
        Task<Void> continueWithTask;
        synchronized (this.currentLock) {
            this.current = this.current.continueWith(new Continuation<Void, Void>() {
                public Void then(Task<Void> task) throws Exception {
                    ParseSQLiteDatabase.this.db.endTransaction();
                    return null;
                }
            }, dbExecutor);
            continueWithTask = this.current.continueWithTask(new Continuation<Void, Task<Void>>() {
                public Task<Void> then(Task<Void> task) throws Exception {
                    return task;
                }
            }, Task.BACKGROUND_EXECUTOR);
        }
        return continueWithTask;
    }

    public Task<Void> closeAsync() {
        Task<Void> continueWithTask;
        synchronized (this.currentLock) {
            this.current = this.current.continueWithTask(new Continuation<Void, Task<Void>>() {
                public Task<Void> then(Task<Void> task) throws Exception {
                    try {
                        ParseSQLiteDatabase.this.db.close();
                        return ParseSQLiteDatabase.this.tcs.getTask();
                    } finally {
                        ParseSQLiteDatabase.this.tcs.setResult(null);
                    }
                }
            }, dbExecutor);
            continueWithTask = this.current.continueWithTask(new Continuation<Void, Task<Void>>() {
                public Task<Void> then(Task<Void> task) throws Exception {
                    return task;
                }
            }, Task.BACKGROUND_EXECUTOR);
        }
        return continueWithTask;
    }

    public Task<Cursor> queryAsync(String table, String[] select, String where, String[] args) {
        Task<Cursor> continueWithTask;
        synchronized (this.currentLock) {
            final String str = table;
            final String[] strArr = select;
            final String str2 = where;
            final String[] strArr2 = args;
            Task<Cursor> task = this.current.onSuccess(new Continuation<Void, Cursor>() {
                public Cursor then(Task<Void> task) throws Exception {
                    return ParseSQLiteDatabase.this.db.query(str, strArr, str2, strArr2, null, null, null);
                }
            }, dbExecutor).onSuccess(new Continuation<Cursor, Cursor>() {
                public Cursor then(Task<Cursor> task) throws Exception {
                    Cursor cursor = ParseSQLiteCursor.create((Cursor) task.getResult(), ParseSQLiteDatabase.dbExecutor);
                    cursor.getCount();
                    return cursor;
                }
            }, dbExecutor);
            this.current = task.makeVoid();
            continueWithTask = task.continueWithTask(new Continuation<Cursor, Task<Cursor>>() {
                public Task<Cursor> then(Task<Cursor> task) throws Exception {
                    return task;
                }
            }, Task.BACKGROUND_EXECUTOR);
        }
        return continueWithTask;
    }

    public Task<Void> insertWithOnConflict(final String table, final ContentValues values, final int conflictAlgorithm) {
        Task<Void> makeVoid;
        synchronized (this.currentLock) {
            Task<Long> task = this.current.onSuccess(new Continuation<Void, Long>() {
                public Long then(Task<Void> task) throws Exception {
                    return Long.valueOf(ParseSQLiteDatabase.this.db.insertWithOnConflict(table, null, values, conflictAlgorithm));
                }
            }, dbExecutor);
            this.current = task.makeVoid();
            makeVoid = task.continueWithTask(new Continuation<Long, Task<Long>>() {
                public Task<Long> then(Task<Long> task) throws Exception {
                    return task;
                }
            }, Task.BACKGROUND_EXECUTOR).makeVoid();
        }
        return makeVoid;
    }

    public Task<Void> insertOrThrowAsync(final String table, final ContentValues values) {
        Task<Void> makeVoid;
        synchronized (this.currentLock) {
            Task<Long> task = this.current.onSuccess(new Continuation<Void, Long>() {
                public Long then(Task<Void> task) throws Exception {
                    return Long.valueOf(ParseSQLiteDatabase.this.db.insertOrThrow(table, null, values));
                }
            }, dbExecutor);
            this.current = task.makeVoid();
            makeVoid = task.continueWithTask(new Continuation<Long, Task<Long>>() {
                public Task<Long> then(Task<Long> task) throws Exception {
                    return task;
                }
            }, Task.BACKGROUND_EXECUTOR).makeVoid();
        }
        return makeVoid;
    }

    public Task<Integer> updateAsync(String table, ContentValues values, String where, String[] args) {
        Task<Integer> continueWithTask;
        synchronized (this.currentLock) {
            final String str = table;
            final ContentValues contentValues = values;
            final String str2 = where;
            final String[] strArr = args;
            Task<Integer> task = this.current.onSuccess(new Continuation<Void, Integer>() {
                public Integer then(Task<Void> task) throws Exception {
                    return Integer.valueOf(ParseSQLiteDatabase.this.db.update(str, contentValues, str2, strArr));
                }
            }, dbExecutor);
            this.current = task.makeVoid();
            continueWithTask = task.continueWithTask(new Continuation<Integer, Task<Integer>>() {
                public Task<Integer> then(Task<Integer> task) throws Exception {
                    return task;
                }
            }, Task.BACKGROUND_EXECUTOR);
        }
        return continueWithTask;
    }

    public Task<Void> deleteAsync(final String table, final String where, final String[] args) {
        Task<Void> makeVoid;
        synchronized (this.currentLock) {
            Task<Integer> task = this.current.onSuccess(new Continuation<Void, Integer>() {
                public Integer then(Task<Void> task) throws Exception {
                    return Integer.valueOf(ParseSQLiteDatabase.this.db.delete(table, where, args));
                }
            }, dbExecutor);
            this.current = task.makeVoid();
            makeVoid = task.continueWithTask(new Continuation<Integer, Task<Integer>>() {
                public Task<Integer> then(Task<Integer> task) throws Exception {
                    return task;
                }
            }, Task.BACKGROUND_EXECUTOR).makeVoid();
        }
        return makeVoid;
    }

    public Task<Cursor> rawQueryAsync(final String sql, final String[] args) {
        Task<Cursor> continueWithTask;
        synchronized (this.currentLock) {
            Task<Cursor> task = this.current.onSuccess(new Continuation<Void, Cursor>() {
                public Cursor then(Task<Void> task) throws Exception {
                    return ParseSQLiteDatabase.this.db.rawQuery(sql, args);
                }
            }, dbExecutor).onSuccess(new Continuation<Cursor, Cursor>() {
                public Cursor then(Task<Cursor> task) throws Exception {
                    Cursor cursor = ParseSQLiteCursor.create((Cursor) task.getResult(), ParseSQLiteDatabase.dbExecutor);
                    cursor.getCount();
                    return cursor;
                }
            }, dbExecutor);
            this.current = task.makeVoid();
            continueWithTask = task.continueWithTask(new Continuation<Cursor, Task<Cursor>>() {
                public Task<Cursor> then(Task<Cursor> task) throws Exception {
                    return task;
                }
            }, Task.BACKGROUND_EXECUTOR);
        }
        return continueWithTask;
    }
}
