package com.nativex.network.volley.toolbox;

import com.nativex.network.volley.NetworkResponse;
import com.nativex.network.volley.Request;
import com.nativex.network.volley.Response;
import com.nativex.network.volley.Response.ErrorListener;
import com.nativex.network.volley.Response.Listener;
import com.nativex.network.volley.VolleyLog;
import java.io.UnsupportedEncodingException;

public abstract class JsonRequest<T> extends Request<T> {
    private static final String PROTOCOL_CHARSET = "utf-8";
    private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", new Object[]{PROTOCOL_CHARSET});
    private final Listener<T> mListener;
    private final String mRequestBody;

    protected abstract Response<T> parseNetworkResponse(NetworkResponse networkResponse);

    public JsonRequest(String url, String requestBody, Listener<T> listener, ErrorListener errorListener) {
        this(-1, url, requestBody, listener, errorListener);
    }

    public JsonRequest(int method, String url, String requestBody, Listener<T> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mRequestBody = requestBody;
    }

    protected void deliverResponse(T response) {
        this.mListener.onResponse(response);
    }

    public String getPostBodyContentType() {
        return getBodyContentType();
    }

    public byte[] getPostBody() {
        return getBody();
    }

    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    public byte[] getBody() {
        byte[] bArr = null;
        try {
            if (this.mRequestBody != null) {
                bArr = this.mRequestBody.getBytes(PROTOCOL_CHARSET);
            }
        } catch (UnsupportedEncodingException e) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", this.mRequestBody, PROTOCOL_CHARSET);
        }
        return bArr;
    }
}
