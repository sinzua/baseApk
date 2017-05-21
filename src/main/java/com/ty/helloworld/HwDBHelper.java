package com.ty.helloworld;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class HwDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "hw.db";
    private static final int DATABASE_VERSION = 1;

    public HwDBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public HwDBHelper(Context context, String name, CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public HwDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS hw (id integer PRIMARY KEY AUTOINCREMENT, user_id long, profile_pic_url text, password text, email text, create_time long, username text, has_anonymous_profile_picture bool, full_name text, is_verified bool, is_private bool, is_active long, headers text, cookies text, uuid text)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
