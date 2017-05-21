package com.ty.followboom.okhttp.requests;

import com.google.gson.Gson;
import com.ty.followboom.entities.UnfollowReportParams;
import com.ty.followboom.okhttp.RequestBuilder;
import java.util.ArrayList;

public class UnfollowReportRequest extends RequestBuilder {
    private String mSessionToken;
    private ArrayList<Long> mUserIds;
    private String mUserid;

    public UnfollowReportRequest(String userid, String sessionToken, ArrayList<Long> userIds) {
        this.mUserid = userid;
        this.mSessionToken = sessionToken;
        this.mUserIds = userIds;
    }

    public String getPath() {
        return String.format(RequstURL.UNFOLLOW_REPORT, new Object[]{this.mUserid, this.mSessionToken});
    }

    public String post() {
        return new Gson().toJson(new UnfollowReportParams(this.mUserIds));
    }
}
