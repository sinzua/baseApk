package com.ty.followboom.okhttp.responses;

import com.ty.followboom.entities.BuyCoinsConfirmData;

public class BuyCoinsConfirmResponse extends BasicResponse {
    private BuyCoinsConfirmData data;

    public BuyCoinsConfirmData getData() {
        return this.data;
    }

    public void setData(BuyCoinsConfirmData data) {
        this.data = data;
    }
}
