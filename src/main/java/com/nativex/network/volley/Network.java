package com.nativex.network.volley;

public interface Network {
    NetworkResponse performRequest(Request<?> request) throws VolleyError;
}
