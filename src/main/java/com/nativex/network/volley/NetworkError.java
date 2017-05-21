package com.nativex.network.volley;

public class NetworkError extends VolleyError {
    public NetworkError(Throwable cause) {
        super(cause);
    }

    public NetworkError(NetworkResponse networkResponse) {
        super(networkResponse);
    }
}
