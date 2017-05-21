package com.supersonicads.sdk.data;

import android.content.Context;
import com.supersonicads.sdk.utils.SDKUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class SSASession {
    public final String CONNECTIVITY = "connectivity";
    public final String SESSION_END_TIME = "sessionEndTime";
    public final String SESSION_START_TIME = "sessionStartTime";
    public final String SESSION_TYPE = "sessionType";
    private String connectivity;
    private long sessionEndTime;
    private long sessionStartTime;
    private SessionType sessionType;

    public enum SessionType {
        launched,
        backFromBG
    }

    public SSASession(Context context, SessionType type) {
        setSessionStartTime(SDKUtils.getCurrentTimeMillis().longValue());
        setSessionType(type);
        setConnectivity(SDKUtils.getConnectionType(context));
    }

    public SSASession(JSONObject jsonObj) {
        try {
            jsonObj.get("sessionStartTime");
            jsonObj.get("sessionEndTime");
            jsonObj.get("sessionType");
            jsonObj.get("connectivity");
        } catch (JSONException e) {
        }
    }

    public void endSession() {
        setSessionEndTime(SDKUtils.getCurrentTimeMillis().longValue());
    }

    public long getSessionStartTime() {
        return this.sessionStartTime;
    }

    public void setSessionStartTime(long sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }

    public long getSessionEndTime() {
        return this.sessionEndTime;
    }

    public void setSessionEndTime(long sessionEndTime) {
        this.sessionEndTime = sessionEndTime;
    }

    public SessionType getSessionType() {
        return this.sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public String getConnectivity() {
        return this.connectivity;
    }

    public void setConnectivity(String connectivity) {
        this.connectivity = connectivity;
    }
}
