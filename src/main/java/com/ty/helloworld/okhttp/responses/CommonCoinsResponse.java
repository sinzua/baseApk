package com.ty.helloworld.okhttp.responses;

import com.ty.helloworld.entities.CoinsInAccount;

public class CommonCoinsResponse extends BasicResponse {
    private CoinsInAccount data;

    public CoinsInAccount getData() {
        return this.data;
    }

    public void setData(CoinsInAccount data) {
        this.data = data;
    }
}
