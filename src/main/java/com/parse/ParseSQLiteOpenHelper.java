package com.parse;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import bolts.Task;

abstract class ParseSQLiteOpenHelper {
    private final SQLiteOpenHelper helper;

    public abstract void onCreate(SQLiteDatabase sQLiteDatabase);

    public abstract void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2);

    public ParseSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        this.helper = new SQLiteOpenHelper(context, name, factory, version) {
            public void onOpen(SQLiteDatabase db) {
                super.onOpen(db);
                ParseSQLiteOpenHelper.this.onOpen(db);
            }

            public void onCreate(SQLiteDatabase db) {
                ParseSQLiteOpenHelper.this.onCreate(db);
            }

            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                ParseSQLiteOpenHelper.this.onUpgrade(db, oldVersion, newVersion);
            }
        };
    }

    public Task<ParseSQLiteDatabase> getReadableDatabaseAsync() {
        return getDatabaseAsync(false);
    }

    public Task<ParseSQLiteDatabase> getWritableDatabaseAsync() {
        return getDatabaseAsync(true);
    }

    private Task<ParseSQLiteDatabase> getDatabaseAsync(boolean writable) {
        return ParseSQLiteDatabase.openDatabaseAsync(this.helper, !writable ? 1 : 0);
    }

    public void onOpen(SQLiteDatabase db) {
    }
}
