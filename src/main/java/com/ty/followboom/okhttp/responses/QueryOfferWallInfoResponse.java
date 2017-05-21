package com.ty.followboom.okhttp.responses;

import com.ty.followboom.entities.QueryOfferWallResult;

public class QueryOfferWallInfoResponse extends BasicResponse {
    private QueryOfferWallResult data;

    public QueryOfferWallResult getData() {
        return this.data;
    }

    public void setData(QueryOfferWallResult data) {
        this.data = data;
    }
}
