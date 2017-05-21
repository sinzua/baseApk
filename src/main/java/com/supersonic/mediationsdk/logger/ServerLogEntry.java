package com.supersonic.mediationsdk.logger;

import com.supersonic.mediationsdk.logger.SupersonicLogger.SupersonicTag;
import org.json.JSONException;
import org.json.JSONObject;

class ServerLogEntry {
    private int mLogLevel;
    private String mMessage;
    private SupersonicTag mTag;
    private String mTimetamp;

    public ServerLogEntry(SupersonicTag tag, String timestamp, String message, int level) {
        this.mTag = tag;
        this.mTimetamp = timestamp;
        this.mMessage = message;
        this.mLogLevel = level;
    }

    public JSONObject toJSON() {
        JSONObject result = new JSONObject();
        try {
            result.put("timestamp", this.mTimetamp);
            result.put("tag", this.mTag);
            result.put("level", this.mLogLevel);
            result.put("message", this.mMessage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int getLogLevel() {
        return this.mLogLevel;
    }
}
