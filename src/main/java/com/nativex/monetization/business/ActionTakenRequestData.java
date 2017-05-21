package com.nativex.monetization.business;

import com.google.gson.annotations.SerializedName;
import com.nativex.common.UDIDs;

public class ActionTakenRequestData {
    @SerializedName("ActionId")
    private Integer actionId;
    @SerializedName("IsHacked")
    private String isHacked;
    @SerializedName("UDIDs")
    private UDIDs udids;

    public void setUdids(UDIDs udids) {
        if (udids == null) {
            throw new NullPointerException("UDIDs must not be null");
        }
        this.udids = udids;
    }

    public void setActionId(int actionId) {
        this.actionId = Integer.valueOf(actionId);
    }

    public void setIsHacked(String isFirstRun) {
        this.isHacked = isFirstRun;
    }
}
