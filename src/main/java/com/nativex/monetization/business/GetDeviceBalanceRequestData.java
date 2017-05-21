package com.nativex.monetization.business;

import com.google.gson.annotations.SerializedName;

public class GetDeviceBalanceRequestData {
    @SerializedName("Session")
    private Session session;

    public void setSession(Session session) {
        this.session = session;
    }
}
