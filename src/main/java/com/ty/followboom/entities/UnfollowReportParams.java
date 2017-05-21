package com.ty.followboom.entities;

import java.util.ArrayList;

public class UnfollowReportParams {
    private ArrayList<Long> userIds;

    public UnfollowReportParams(ArrayList<Long> userIds) {
        this.userIds = userIds;
    }
}
