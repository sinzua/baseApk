package com.ty.helloworld;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.hw.entities.LoginUser;
import com.hw.entities.UserInfo;
import com.ty.followboom.FollowlistActivity;
import java.util.ArrayList;
import java.util.List;

public class HwDBManager {
    public static final String COMMENTS = "comments";
    public static final String FOLLOWERS = "followers";
    public static final String FOLLOWINGS = "followings";
    public static final String LIKES = "likes";
    private static final String TAG = "HwDBManager";
    private static HwDBManager instance;
    private SQLiteDatabase mDb = this.mHwDBHelper.getWritableDatabase();
    private HwDBHelper mHwDBHelper;

    public static HwDBManager getSingleton() {
        return instance;
    }

    public static void initHwDBManager(Context context) {
        instance = new HwDBManager(context);
    }

    public HwDBManager(Context context) {
        this.mHwDBHelper = new HwDBHelper(context);
    }

    public boolean hasHw() {
        this.mDb.beginTransaction();
        try {
            Cursor cursor = this.mDb.rawQuery("select COUNT(1) from hw where isactive < 10", new String[0]);
            Log.e(TAG, "Has Hw: " + cursor.getCount());
            this.mDb.setTransactionSuccessful();
            this.mDb.endTransaction();
            boolean hasHw = cursor.moveToFirst();
            cursor.close();
            return hasHw;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public UserInfo getHw() {
        this.mDb.beginTransaction();
        try {
            Cursor cursor = this.mDb.rawQuery("select * from hw where isactive < 10 limit 1", new String[0]);
            Log.e(TAG, "Has Hw: " + cursor.getCount());
            this.mDb.setTransactionSuccessful();
            this.mDb.endTransaction();
            UserInfo userInfo = new UserInfo();
            if (cursor.moveToFirst()) {
                userInfo.setUserName(cursor.getString(cursor.getColumnIndex("username")));
                userInfo.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                userInfo.setUuid(cursor.getString(cursor.getColumnIndex("uuid")));
                userInfo.setUserid(Long.valueOf(cursor.getLong(cursor.getColumnIndex(FollowlistActivity.EXTRA_USER_ID))));
                userInfo.setCookie(cursor.getString(cursor.getColumnIndex("cookies")));
                userInfo.setHeader(cursor.getString(cursor.getColumnIndex("headers")));
            }
            cursor.close();
            return userInfo;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public boolean signUp() {
        this.mDb.beginTransaction();
        try {
            Cursor cursor = this.mDb.rawQuery("select COUNT(1) from hw where isactive < 10", new String[0]);
            Log.e(TAG, "Has Hw: " + cursor.getCount());
            this.mDb.setTransactionSuccessful();
            this.mDb.endTransaction();
            boolean hasHw = cursor.moveToFirst();
            cursor.close();
            return hasHw;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public void addFollowListToDB(List<LoginUser> followers, List<LoginUser> followings) {
        long timeStamp = System.currentTimeMillis();
        this.mDb.beginTransaction();
        for (LoginUser loginUser : followers) {
            try {
                this.mDb.execSQL("INSERT INTO followers VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?)", new Object[]{loginUser.getPk(), loginUser.getProfile_pic_url(), Long.valueOf(timeStamp), loginUser.getUsername(), Boolean.valueOf(loginUser.isHas_anonymous_profile_picture()), loginUser.getFull_name(), Boolean.valueOf(false), Boolean.valueOf(loginUser.is_private())});
            } catch (Exception e) {
                try {
                    Log.e(TAG, e.toString());
                } catch (Exception e2) {
                    Log.e(TAG, e2.toString());
                } finally {
                    this.mDb.endTransaction();
                }
            }
        }
        for (LoginUser loginUser2 : followings) {
            try {
                this.mDb.execSQL("INSERT INTO followings VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?)", new Object[]{loginUser2.getPk(), loginUser2.getProfile_pic_url(), Long.valueOf(timeStamp), loginUser2.getUsername(), Boolean.valueOf(loginUser2.isHas_anonymous_profile_picture()), loginUser2.getFull_name(), Boolean.valueOf(false), Boolean.valueOf(loginUser2.is_private())});
            } catch (Exception e22) {
                Log.e(TAG, e22.toString());
            }
        }
        this.mDb.setTransactionSuccessful();
    }

    public void getFollowers() {
        this.mDb.beginTransaction();
        Cursor cursor = this.mDb.rawQuery("SELECT * FROM followers", new String[0]);
        Log.e("fuwen", "" + cursor.getCount());
        this.mDb.setTransactionSuccessful();
        this.mDb.endTransaction();
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            Log.e("fuwen", "" + cursor.getLong(cursor.getColumnIndex(FollowlistActivity.EXTRA_USER_ID)));
        }
        cursor.close();
        this.mDb.beginTransaction();
        cursor = this.mDb.rawQuery("SELECT * FROM followings", new String[0]);
        Log.e("fuwen", "" + cursor.getCount());
        this.mDb.setTransactionSuccessful();
        this.mDb.endTransaction();
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            Log.e("fuwen", "" + cursor.getLong(cursor.getColumnIndex(FollowlistActivity.EXTRA_USER_ID)));
        }
        cursor.close();
    }

    public ArrayList<LoginUser> getMutualFollowersFromDB() {
        ArrayList<LoginUser> result = new ArrayList();
        this.mDb.beginTransaction();
        try {
            LoginUser loginUser;
            Cursor cursor = this.mDb.rawQuery("select * from followings where create_time = (select create_time from followers group by create_time order by create_time desc limit 1) and user_id in (select user_id from followers where create_time = (select create_time from followers group by create_time order by create_time desc limit 1))", new String[0]);
            Log.e(TAG, "MutualFollowers: " + cursor.getCount());
            this.mDb.setTransactionSuccessful();
            this.mDb.endTransaction();
            if (cursor.moveToFirst()) {
                loginUser = new LoginUser();
                loginUser.setPk(Long.toString(cursor.getLong(cursor.getColumnIndex(FollowlistActivity.EXTRA_USER_ID))));
                loginUser.setProfile_pic_url(cursor.getString(cursor.getColumnIndex("profile_pic_url")));
                loginUser.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                loginUser.setFull_name(cursor.getString(cursor.getColumnIndex("full_name")));
                result.add(loginUser);
            }
            while (cursor.moveToNext()) {
                loginUser = new LoginUser();
                loginUser.setPk(Long.toString(cursor.getLong(cursor.getColumnIndex(FollowlistActivity.EXTRA_USER_ID))));
                loginUser.setProfile_pic_url(cursor.getString(cursor.getColumnIndex("profile_pic_url")));
                loginUser.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                loginUser.setFull_name(cursor.getString(cursor.getColumnIndex("full_name")));
                result.add(loginUser);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return result;
    }

    public ArrayList<LoginUser> getNonFollowersFromDB() {
        ArrayList<LoginUser> result = new ArrayList();
        this.mDb.beginTransaction();
        try {
            LoginUser loginUser;
            Cursor cursor = this.mDb.rawQuery("select * from followings where create_time = (select create_time from followings group by create_time order by create_time desc limit 1) and user_id not in (select user_id from followers where create_time = (select create_time from followers group by create_time order by create_time desc limit 1))", new String[0]);
            Log.e(TAG, "NonFollowers: " + cursor.getCount());
            this.mDb.setTransactionSuccessful();
            this.mDb.endTransaction();
            if (cursor.moveToFirst()) {
                loginUser = new LoginUser();
                loginUser.setPk(Long.toString(cursor.getLong(cursor.getColumnIndex(FollowlistActivity.EXTRA_USER_ID))));
                loginUser.setProfile_pic_url(cursor.getString(cursor.getColumnIndex("profile_pic_url")));
                loginUser.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                loginUser.setFull_name(cursor.getString(cursor.getColumnIndex("full_name")));
                result.add(loginUser);
            }
            while (cursor.moveToNext()) {
                loginUser = new LoginUser();
                loginUser.setPk(Long.toString(cursor.getLong(cursor.getColumnIndex(FollowlistActivity.EXTRA_USER_ID))));
                loginUser.setProfile_pic_url(cursor.getString(cursor.getColumnIndex("profile_pic_url")));
                loginUser.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                loginUser.setFull_name(cursor.getString(cursor.getColumnIndex("full_name")));
                result.add(loginUser);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return result;
    }

    public ArrayList<LoginUser> getFansFromDB() {
        ArrayList<LoginUser> result = new ArrayList();
        this.mDb.beginTransaction();
        try {
            LoginUser loginUser;
            Cursor cursor = this.mDb.rawQuery("select * from followers where create_time = (select create_time from followers group by create_time order by create_time desc limit 1) and user_id not in (select user_id from followings where create_time = (select create_time from followings group by create_time order by create_time desc limit 1))", new String[0]);
            Log.e(TAG, "Fans: " + cursor.getCount());
            this.mDb.setTransactionSuccessful();
            this.mDb.endTransaction();
            if (cursor.moveToFirst()) {
                loginUser = new LoginUser();
                loginUser.setPk(Long.toString(cursor.getLong(cursor.getColumnIndex(FollowlistActivity.EXTRA_USER_ID))));
                loginUser.setProfile_pic_url(cursor.getString(cursor.getColumnIndex("profile_pic_url")));
                loginUser.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                loginUser.setFull_name(cursor.getString(cursor.getColumnIndex("full_name")));
                result.add(loginUser);
            }
            while (cursor.moveToNext()) {
                loginUser = new LoginUser();
                loginUser.setPk(Long.toString(cursor.getLong(cursor.getColumnIndex(FollowlistActivity.EXTRA_USER_ID))));
                loginUser.setProfile_pic_url(cursor.getString(cursor.getColumnIndex("profile_pic_url")));
                loginUser.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                loginUser.setFull_name(cursor.getString(cursor.getColumnIndex("full_name")));
                result.add(loginUser);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return result;
    }

    public ArrayList<LoginUser> getNewGainFollowersFromDB(long timeStamp0) {
        ArrayList<LoginUser> result = new ArrayList();
        this.mDb.beginTransaction();
        try {
            LoginUser loginUser;
            Cursor cursor = this.mDb.rawQuery("select * from followers where create_time = (select create_time from followers group by create_time order by create_time desc limit 1) and user_id not in (select user_id from followers where create_time = (select create_time from followers where create_time < " + timeStamp0 + " group by create_time order by create_time desc limit 1))", new String[0]);
            Log.e(TAG, "New Gain: " + cursor.getCount());
            this.mDb.setTransactionSuccessful();
            this.mDb.endTransaction();
            if (cursor.moveToFirst()) {
                loginUser = new LoginUser();
                loginUser.setPk(Long.toString(cursor.getLong(cursor.getColumnIndex(FollowlistActivity.EXTRA_USER_ID))));
                loginUser.setProfile_pic_url(cursor.getString(cursor.getColumnIndex("profile_pic_url")));
                loginUser.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                loginUser.setFull_name(cursor.getString(cursor.getColumnIndex("full_name")));
                result.add(loginUser);
            }
            while (cursor.moveToNext()) {
                loginUser = new LoginUser();
                loginUser.setPk(Long.toString(cursor.getLong(cursor.getColumnIndex(FollowlistActivity.EXTRA_USER_ID))));
                loginUser.setProfile_pic_url(cursor.getString(cursor.getColumnIndex("profile_pic_url")));
                loginUser.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                loginUser.setFull_name(cursor.getString(cursor.getColumnIndex("full_name")));
                result.add(loginUser);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return result;
    }

    public ArrayList<LoginUser> getNewLostFromDB(long timeStamp0) {
        ArrayList<LoginUser> result = new ArrayList();
        this.mDb.beginTransaction();
        try {
            LoginUser loginUser;
            Cursor cursor = this.mDb.rawQuery("select * from followers where create_time = (select create_time from followers where create_time < " + timeStamp0 + " group by create_time order by create_time desc limit 1) and user_id not in (select user_id from followers where create_time = (select create_time from followers group by create_time order by create_time desc limit 1))", new String[0]);
            Log.e(TAG, "New Lost: " + cursor.getCount());
            this.mDb.setTransactionSuccessful();
            this.mDb.endTransaction();
            if (cursor.moveToFirst()) {
                loginUser = new LoginUser();
                loginUser.setPk(Long.toString(cursor.getLong(cursor.getColumnIndex(FollowlistActivity.EXTRA_USER_ID))));
                loginUser.setProfile_pic_url(cursor.getString(cursor.getColumnIndex("profile_pic_url")));
                loginUser.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                loginUser.setFull_name(cursor.getString(cursor.getColumnIndex("full_name")));
                result.add(loginUser);
            }
            while (cursor.moveToNext()) {
                loginUser = new LoginUser();
                loginUser.setPk(Long.toString(cursor.getLong(cursor.getColumnIndex(FollowlistActivity.EXTRA_USER_ID))));
                loginUser.setProfile_pic_url(cursor.getString(cursor.getColumnIndex("profile_pic_url")));
                loginUser.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                loginUser.setFull_name(cursor.getString(cursor.getColumnIndex("full_name")));
                result.add(loginUser);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return result;
    }

    public boolean hasDataToday(long timeStamp0) {
        ArrayList<LoginUser> result = new ArrayList();
        this.mDb.beginTransaction();
        try {
            Cursor cursor = this.mDb.rawQuery("select COUNT(1) from followers where create_time > " + timeStamp0 + " group by create_time order by create_time desc", new String[0]);
            Log.e(TAG, "Has Data Today: " + cursor.getCount());
            this.mDb.setTransactionSuccessful();
            this.mDb.endTransaction();
            boolean hasDataToday = cursor.moveToFirst();
            cursor.close();
            return hasDataToday;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }
}
