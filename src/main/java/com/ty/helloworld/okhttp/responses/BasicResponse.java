package com.ty.helloworld.okhttp.responses;

import com.ty.helloworld.entities.RequestStatus;

public class BasicResponse {
    protected RequestStatus status;

    public RequestStatus getStatus() {
        return this.status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public boolean isSuccessful() {
        return this.status != null && this.status.getStatus() == 200;
    }

    public boolean isSessionExpired() {
        return this.status != null && this.status.getStatus() == 400;
    }

    public boolean needUpdateVersion() {
        return this.status != null && this.status.getStatus() == 420;
    }
}
