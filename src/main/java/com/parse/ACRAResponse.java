package com.parse;

class ACRAResponse {
    private int mStatus;

    ACRAResponse() {
    }

    public void setStatusCode(int status) {
        this.mStatus = status;
    }

    public int getStatusCode() {
        return this.mStatus;
    }
}
