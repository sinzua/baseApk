package com.supersonicads.sdk.data;

public class SSABCParameters extends SSAObj {
    private String CONNECTION_RETRIES = "connectionRetries";
    private String mConnectionRetries;

    public SSABCParameters(String value) {
        super(value);
        if (containsKey(this.CONNECTION_RETRIES)) {
            setConnectionRetries(getString(this.CONNECTION_RETRIES));
        }
    }

    public String getConnectionRetries() {
        return this.mConnectionRetries;
    }

    public void setConnectionRetries(String connectionRetries) {
        this.mConnectionRetries = connectionRetries;
    }
}
