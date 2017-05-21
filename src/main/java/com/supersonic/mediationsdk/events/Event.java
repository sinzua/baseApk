package com.supersonic.mediationsdk.events;

import android.text.TextUtils;
import com.supersonic.mediationsdk.utils.SupersonicUtils;
import org.json.JSONException;
import org.json.JSONObject;

class Event {
    public static final String KEY_EVENT_ID = "eventId";
    public static final String KEY_PLACEMENT_NAME = "placement";
    public static final String KEY_PROVIDER = "provider";
    public static final String KEY_REWARD_AMOUNT = "rewardAmount";
    public static final String KEY_REWARD_NAME = "rewardName";
    public static final String KEY_SESSION_DEPTH = "sessionDepth";
    public static final String KEY_STATUS = "status";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_TRANSTACTION_ID = "transId";
    private int mEventId = -1;
    private String mPlacementName = "";
    private String mProvider = "";
    private int mRewardAmount = -1;
    private String mRewardName = "";
    private int mSessionDepth = -1;
    private int mStatus = -1;
    private long mTimestamp = -1;
    private String mTransId = "";

    public Event(int eventId, String provider, long timeStamp, int sessionDepth) {
        this.mEventId = eventId;
        if (TextUtils.isEmpty(provider)) {
            provider = "";
        }
        this.mProvider = provider;
        this.mTimestamp = timeStamp;
        this.mSessionDepth = sessionDepth;
    }

    public void setStatus(boolean status) {
        this.mStatus = status ? 1 : 0;
    }

    public void setPlacementName(String placementName) {
        if (TextUtils.isEmpty(placementName)) {
            placementName = "";
        }
        this.mPlacementName = placementName;
    }

    public void setRewardName(String rewardName) {
        if (TextUtils.isEmpty(rewardName)) {
            rewardName = "";
        }
        this.mRewardName = rewardName;
    }

    public void setRewardAmount(int rewardAmount) {
        this.mRewardAmount = rewardAmount;
    }

    public void setTransId(String appKey) {
        if (!TextUtils.isEmpty(appKey)) {
            this.mTransId = SupersonicUtils.getTransId("" + Long.toString(this.mTimestamp) + appKey + this.mProvider);
        }
    }

    public int getEventId() {
        return this.mEventId;
    }

    public int getSessionDepth() {
        return this.mSessionDepth;
    }

    public long getTimestamp() {
        return this.mTimestamp;
    }

    public String getProvider() {
        String result = "";
        if (this.mProvider != null) {
            return this.mProvider;
        }
        return result;
    }

    public int getStatus() {
        return this.mStatus;
    }

    public String getPlacementName() {
        return this.mPlacementName;
    }

    public String getRewardName() {
        return this.mRewardName;
    }

    public int getRewardAmount() {
        return this.mRewardAmount;
    }

    public String getTransId() {
        return this.mTransId;
    }

    public JSONObject toJSON() {
        JSONObject event = new JSONObject();
        try {
            event.put(KEY_EVENT_ID, this.mEventId);
            event.put("provider", this.mProvider);
            event.put(KEY_SESSION_DEPTH, this.mSessionDepth);
            event.put("timestamp", this.mTimestamp);
            if (this.mStatus != -1) {
                event.put("status", this.mStatus == 1 ? "true" : "false");
            }
            if (!TextUtils.isEmpty(this.mPlacementName)) {
                event.put("placement", this.mPlacementName);
            }
            if (!TextUtils.isEmpty(this.mRewardName)) {
                event.put(KEY_REWARD_NAME, this.mRewardName);
            }
            if (this.mRewardAmount != -1) {
                event.put(KEY_REWARD_AMOUNT, this.mRewardAmount);
            }
            if (TextUtils.isEmpty(this.mTransId)) {
                return event;
            }
            event.put("transId", this.mTransId);
            return event;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
