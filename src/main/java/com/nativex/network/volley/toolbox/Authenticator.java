package com.nativex.network.volley.toolbox;

import com.nativex.network.volley.AuthFailureError;

public interface Authenticator {
    String getAuthToken() throws AuthFailureError;

    void invalidateAuthToken(String str);
}
