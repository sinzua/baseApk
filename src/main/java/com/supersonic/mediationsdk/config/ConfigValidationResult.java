package com.supersonic.mediationsdk.config;

import com.supersonic.mediationsdk.logger.SupersonicError;

public class ConfigValidationResult {
    private boolean mIsValid = true;
    private SupersonicError mSupersonicError = null;

    public void setInvalid(SupersonicError error) {
        this.mIsValid = false;
        this.mSupersonicError = error;
    }

    public void setValid() {
        this.mIsValid = true;
        this.mSupersonicError = null;
    }

    public boolean isValid() {
        return this.mIsValid;
    }

    public SupersonicError getSupersonicError() {
        return this.mSupersonicError;
    }

    public String toString() {
        if (isValid()) {
            return "valid:" + this.mIsValid;
        }
        return "valid:" + this.mIsValid + ", SupersonicError:" + this.mSupersonicError;
    }
}
