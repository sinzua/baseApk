package com.nativex.monetization.business;

import com.google.gson.annotations.SerializedName;

public class Session {
    @SerializedName("SessionId")
    private String sessionId;

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof Session)) {
            return false;
        }
        if (getSessionId() != null) {
            return this.sessionId.equals(((Session) o).getSessionId());
        }
        if (((Session) o).getSessionId() == null) {
            return true;
        }
        return false;
    }
}
