package com.ty.followboom.okhttp.responses;

import com.ty.followboom.entities.CoinsInAccount;

public class CommonCoinsResponse extends BasicResponse {
    private CoinsInAccount data;

    public CoinsInAccount getData() {
        return this.data;
    }

    public void setData(CoinsInAccount data) {
        this.data = data;
    }
}
