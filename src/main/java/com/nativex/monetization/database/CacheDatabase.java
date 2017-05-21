package com.nativex.monetization.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CacheDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "nativex_cache.db";
    private static final int DATABASE_VERSION = 4;

    public CacheDatabase(Context context) {
        super(context, DATABASE_NAME, null, 4);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table if not exists cache_files(cache_id INTEGER PRIMARY KEY AUTOINCREMENT,OfferId INTEGER NOT NULL,ExpirationDateUTC INTEGER NOT NULL,RelativeUrl TEXT NOT NULL,MD5 TEXT NOT NULL,CDN TEXT NOT NULL,Ext TEXT NOT NULL,FileSize INTEGER NOT NULL,FileStatus INTEGER NOT NULL);");
        db.execSQL("Create table if not exists cache_utils(cache_utils_id INTEGER PRIMARY KEY AUTOINCREMENT,cache_util_value_int INTEGER,cache_util TEXT NOT NULL UNIQUE);");
        db.execSQL("Create table if not exists download_map(download_id INTEGER PRIMARY KEY AUTOINCREMENT,cachefile TEXT NOT NULL);");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS file_status");
        db.execSQL("DROP TABLE IF EXISTS cache_files");
        db.execSQL("DROP TABLE IF EXISTS cache_utils");
        db.execSQL("DROP TABLE IF EXISTS download_map");
        db.execSQL("DROP TABLE IF EXISTS downloaded_files");
        db.execSQL("DROP TABLE IF EXISTS cache_error");
        onCreate(db);
    }
}
