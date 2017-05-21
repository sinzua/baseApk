package com.nativex.network.volley.toolbox;

import com.nativex.network.volley.AuthFailureError;
import com.nativex.network.volley.Request;
import java.io.IOException;
import java.util.Map;
import org.apache.http.HttpResponse;

public interface HttpStack {
    HttpResponse performRequest(Request<?> request, Map<String, String> map) throws IOException, AuthFailureError;
}
