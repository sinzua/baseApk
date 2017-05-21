package com.supersonic.mediationsdk.events;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

class SupersonicDbHelper extends SQLiteOpenHelper {
    private static final String COMMA_SEP = ",";
    private static final String DATABASE_NAME = "supersonic_sdk.db";
    private static final int DATABASE_VERSION = 4;
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE events (_id INTEGER PRIMARY KEY,eventid INTEGER,provider TEXT,timestamp INTEGER,sessiondepth INTEGER,status INTEGER,placement TEXT,reward_name TEXT,reward_amount INTEGER,transId TEXT )";
    private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS events";
    private static final String TYPE_INTEGER = " INTEGER";
    private static final String TYPE_TEXT = " TEXT";

    static abstract class EventEntry implements BaseColumns {
        public static final String COLUMN_NAME_EVENT_ID = "eventid";
        public static final String COLUMN_NAME_PLACEMENT = "placement";
        public static final String COLUMN_NAME_PROVIDER = "provider";
        public static final String COLUMN_NAME_REWARD_AMOUNT = "reward_amount";
        public static final String COLUMN_NAME_REWARD_NAME = "reward_name";
        public static final String COLUMN_NAME_SESSION_DEPTH = "sessiondepth";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_TRANS_ID = "transId";
        public static final int NUMBER_OF_COLUMNS = 9;
        public static final String TABLE_NAME = "events";

        EventEntry() {
        }
    }

    public SupersonicDbHelper(Context context) {
        super(context, DATABASE_NAME, null, 4);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }
}
