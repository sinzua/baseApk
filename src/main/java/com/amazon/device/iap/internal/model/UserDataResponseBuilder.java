package com.amazon.device.iap.internal.model;

import com.amazon.device.iap.model.RequestId;
import com.amazon.device.iap.model.UserData;
import com.amazon.device.iap.model.UserDataResponse;
import com.amazon.device.iap.model.UserDataResponse.RequestStatus;

public class UserDataResponseBuilder {
    private RequestId requestId;
    private RequestStatus requestStatus;
    private UserData userData;

    public RequestId getRequestId() {
        return this.requestId;
    }

    public RequestStatus getRequestStatus() {
        return this.requestStatus;
    }

    public UserData getUserData() {
        return this.userData;
    }

    public UserDataResponse build() {
        return new UserDataResponse(this);
    }

    public UserDataResponseBuilder setRequestId(RequestId requestId) {
        this.requestId = requestId;
        return this;
    }

    public UserDataResponseBuilder setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
        return this;
    }

    public UserDataResponseBuilder setUserData(UserData userData) {
        this.userData = userData;
        return this;
    }
}
