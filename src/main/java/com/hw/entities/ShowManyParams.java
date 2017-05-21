package com.hw.entities;

import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.util.ArrayList;

public class ShowManyParams {
    private String user_ids;

    public ShowManyParams(ArrayList<Long> userIds) {
        this.user_ids = userIds.toString().replace(RequestParameters.LEFT_BRACKETS, "").replace(RequestParameters.RIGHT_BRACKETS, "");
    }

    public String getUser_ids() {
        return this.user_ids;
    }

    public void setUser_ids(String user_ids) {
        this.user_ids = user_ids;
    }
}
