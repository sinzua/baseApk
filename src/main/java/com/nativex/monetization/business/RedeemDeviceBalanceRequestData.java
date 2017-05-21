package com.nativex.monetization.business;

import com.google.gson.annotations.SerializedName;
import java.util.Arrays;
import java.util.List;

public class RedeemDeviceBalanceRequestData {
    @SerializedName("PayoutIds")
    private List<String> payoutIds;
    @SerializedName("Session")
    private Session session;

    public void setSession(Session session) {
        this.session = session;
    }

    public void setPayoutIds(String... ids) {
        if (ids != null && ids.length > 0) {
            this.payoutIds = Arrays.asList(ids);
        }
    }
}
