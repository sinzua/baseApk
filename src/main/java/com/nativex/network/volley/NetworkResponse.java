package com.nativex.network.volley;

import java.util.Collections;
import java.util.Map;

public class NetworkResponse {
    public final byte[] data;
    public final Map<String, String> headers;
    public final boolean notModified;
    public final int statusCode;

    public NetworkResponse(int statusCode, byte[] data, Map<String, String> headers, boolean notModified) {
        this.statusCode = statusCode;
        this.data = data;
        this.headers = headers;
        this.notModified = notModified;
    }

    public NetworkResponse(byte[] data) {
        this(200, data, Collections.emptyMap(), false);
    }

    public NetworkResponse(byte[] data, Map<String, String> headers) {
        this(200, data, headers, false);
    }

    public String getMessage() {
        return this.data != null ? new String(this.data) : null;
    }
}
