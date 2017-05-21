package com.ty.followboom.models;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.ty.entities.LoginUser;
import com.ty.http.RequestCallback;
import com.ty.http.responses.FollowResponse;
import com.ty.instagramapi.InstagramService;
import java.util.ArrayList;
import java.util.Date;

public class TrackerManager {
    private static final int GET_FOLLOWERS_SUCCEED = 0;
    private static final int GET_FOLLOWINGS_SUCCEED = 1;
    private static final String TAG = "TrackerManager";
    private static TrackerManager instance;
    private int TYPE_FOLLOWERS = 0;
    private int TYPE_FOLLOWINGS = 1;
    private RequestCallback<FollowResponse> followersCallback = new RequestCallback<FollowResponse>() {
        public void onResponse(FollowResponse followResponse) {
            TrackerManager.this.mFollowerResponse = followResponse;
            if (TrackerManager.this.mFollowerResponse != null) {
                TrackerManager.this.mFollowers.addAll(TrackerManager.this.mFollowerResponse.getUsers());
                if (!TrackerManager.this.mFollowerResponse.isBig_list() || TextUtils.isEmpty(TrackerManager.this.mFollowerResponse.getNext_max_id())) {
                    TrackerManager.this.mActionHandler.sendEmptyMessage(0);
                } else {
                    InstagramService.getInstance().followers("" + InstagramService.getInstance().getUserInfo().getUserid(), TrackerManager.this.mFollowerResponse.getNext_max_id(), TrackerManager.this.followersCallback);
                }
            }
        }

        public void onFailure(Exception e) {
        }
    };
    private RequestCallback<FollowResponse> followingsCallback = new RequestCallback<FollowResponse>() {
        public void onResponse(FollowResponse followResponse) {
            TrackerManager.this.mFollowingResponse = followResponse;
            if (TrackerManager.this.mFollowingResponse != null) {
                TrackerManager.this.mFollowings.addAll(TrackerManager.this.mFollowingResponse.getUsers());
                if (!TrackerManager.this.mFollowingResponse.isBig_list() || TextUtils.isEmpty(TrackerManager.this.mFollowingResponse.getNext_max_id())) {
                    TrackerManager.this.mActionHandler.sendEmptyMessage(1);
                } else {
                    InstagramService.getInstance().following("" + InstagramService.getInstance().getUserInfo().getUserid(), TrackerManager.this.mFollowingResponse.getNext_max_id(), TrackerManager.this.followingsCallback);
                }
            }
        }

        public void onFailure(Exception e) {
        }
    };
    private Handler mActionHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Log.e("fuwen", "mFollowers size" + TrackerManager.this.mFollowers.size());
                    TrackerManager.this.mGetFollowersSucceed = true;
                    if (TrackerManager.this.mGetFollowingsSucceed) {
                        TrackerDBManager.getSingleton().addFollowListToDB(TrackerManager.this.mFollowers, TrackerManager.this.mFollowings);
                        TrackerManager.this.getFollowResult();
                        return;
                    }
                    return;
                case 1:
                    Log.e("fuwen", "mFollowings size" + TrackerManager.this.mFollowings.size());
                    TrackerManager.this.mGetFollowingsSucceed = true;
                    if (TrackerManager.this.mGetFollowersSucceed) {
                        TrackerDBManager.getSingleton().addFollowListToDB(TrackerManager.this.mFollowers, TrackerManager.this.mFollowings);
                        TrackerManager.this.getFollowResult();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private ArrayList<LoginUser> mFans = new ArrayList();
    private FollowResponse mFollowerResponse;
    private ArrayList<LoginUser> mFollowers = new ArrayList();
    private FollowResponse mFollowingResponse;
    private ArrayList<LoginUser> mFollowings = new ArrayList();
    private boolean mGetFollowersSucceed;
    private boolean mGetFollowingsSucceed;
    private ArrayList<LoginUser> mMutualFriends = new ArrayList();
    private ArrayList<LoginUser> mNewGainedFollowers = new ArrayList();
    private ArrayList<LoginUser> mNewLostFollowers = new ArrayList();
    private ArrayList<LoginUser> mNonFollowers = new ArrayList();
    private Handler mResultActionHandler;

    public static TrackerManager getSingleton() {
        if (instance == null) {
            synchronized (TrackerManager.class) {
                if (instance == null) {
                    instance = new TrackerManager();
                }
            }
        }
        return instance;
    }

    public void init(Handler resultActionHandler, boolean forceRefresh) {
        this.mResultActionHandler = resultActionHandler;
        if (needToRefresh() || forceRefresh) {
            this.mGetFollowersSucceed = false;
            this.mGetFollowingsSucceed = false;
            this.mFollowers.clear();
            this.mFollowings.clear();
            InstagramService.getInstance().followers("" + InstagramService.getInstance().getUserInfo().getUserid(), this.followersCallback);
            InstagramService.getInstance().following("" + InstagramService.getInstance().getUserInfo().getUserid(), this.followingsCallback);
            return;
        }
        getFollowResult();
    }

    private boolean needToRefresh() {
        return !TrackerDBManager.getSingleton().hasDataToday(getTimeStamp0());
    }

    public long getTimeStamp0() {
        Date date = new Date(System.currentTimeMillis());
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        long timeStamp0 = date.getTime();
        Log.e(TAG, "timeStamp0: " + timeStamp0);
        return timeStamp0;
    }

    private void getFollowResult() {
        this.mMutualFriends = TrackerDBManager.getSingleton().getMutualFollowersFromDB();
        this.mNonFollowers = TrackerDBManager.getSingleton().getNonFollowersFromDB();
        this.mFans = TrackerDBManager.getSingleton().getFansFromDB();
        this.mNewGainedFollowers = TrackerDBManager.getSingleton().getNewGainFollowersFromDB(getTimeStamp0());
        this.mNewLostFollowers = TrackerDBManager.getSingleton().getNewLostFromDB(getTimeStamp0());
        this.mResultActionHandler.sendEmptyMessage(0);
    }

    public ArrayList<LoginUser> getFollowers() {
        return this.mFollowers;
    }

    public void setFollowers(ArrayList<LoginUser> followers) {
        this.mFollowers = followers;
    }

    public ArrayList<LoginUser> getFollowings() {
        return this.mFollowings;
    }

    public void setFollowings(ArrayList<LoginUser> followings) {
        this.mFollowings = followings;
    }

    public ArrayList<LoginUser> getNewGainedFollowers() {
        return this.mNewGainedFollowers;
    }

    public void setNewGainedFollowers(ArrayList<LoginUser> newGainedFollowers) {
        this.mNewGainedFollowers = newGainedFollowers;
    }

    public ArrayList<LoginUser> getNewLostFollowers() {
        return this.mNewLostFollowers;
    }

    public void setNewLostFollowers(ArrayList<LoginUser> newLostFollowers) {
        this.mNewLostFollowers = newLostFollowers;
    }

    public ArrayList<LoginUser> getNonFollowers() {
        return this.mNonFollowers;
    }

    public void setNonFollowers(ArrayList<LoginUser> nonFollowers) {
        this.mNonFollowers = nonFollowers;
    }

    public ArrayList<LoginUser> getMutualFriends() {
        return this.mMutualFriends;
    }

    public void setMutualFriends(ArrayList<LoginUser> mutualFriends) {
        this.mMutualFriends = mutualFriends;
    }

    public ArrayList<LoginUser> getFans() {
        return this.mFans;
    }

    public void setFans(ArrayList<LoginUser> fans) {
        this.mFans = fans;
    }
}
