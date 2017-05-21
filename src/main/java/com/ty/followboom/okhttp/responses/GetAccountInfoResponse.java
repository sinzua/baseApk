package com.ty.followboom.okhttp.responses;

import com.ty.followboom.entities.AccountInfo;

public class GetAccountInfoResponse extends BasicResponse {
    private AccountInfo data;

    public AccountInfo getData() {
        return this.data;
    }

    public void setData(AccountInfo data) {
        this.data = data;
    }
}
