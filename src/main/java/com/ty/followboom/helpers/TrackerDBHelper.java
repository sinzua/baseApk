package com.ty.followboom.helpers;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class TrackerDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "followtracker.db";
    private static final int DATABASE_VERSION = 1;

    public TrackerDBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public TrackerDBHelper(Context context, String name, CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public TrackerDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS followers (id integer PRIMARY KEY AUTOINCREMENT, user_id long, profile_pic_url text, create_time long, username text, has_anonymous_profile_picture bool, full_name text, is_verified bool, is_private bool)");
        db.execSQL("CREATE TABLE IF NOT EXISTS followings (id integer PRIMARY KEY AUTOINCREMENT, user_id long, profile_pic_url text, create_time long, username text, has_anonymous_profile_picture bool, full_name text, is_verified bool, is_private bool)");
        db.execSQL("CREATE TABLE IF NOT EXISTS likes (id integer PRIMARY KEY AUTOINCREMENT, post_id long, user_id long, create_time long, username text, profile_pic_url text, full_name text, is_verified bool, is_private bool, user_info text)");
        db.execSQL("CREATE TABLE IF NOT EXISTS comments (id integer PRIMARY KEY AUTOINCREMENT, post_id long, user_id long, user_info text, profile_pic_url text, create_time long, created_at long, bit_flags integer, content_type text, content text, comment_id long, type integer)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
