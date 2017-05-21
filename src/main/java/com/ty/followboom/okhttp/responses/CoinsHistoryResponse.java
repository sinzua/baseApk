package com.ty.followboom.okhttp.responses;

import com.ty.followboom.entities.CoinsHistoryData;

public class CoinsHistoryResponse extends BasicResponse {
    private CoinsHistoryData data;

    public CoinsHistoryData getData() {
        return this.data;
    }

    public void setData(CoinsHistoryData data) {
        this.data = data;
    }
}
