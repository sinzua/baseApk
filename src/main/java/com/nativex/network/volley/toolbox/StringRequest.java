package com.nativex.network.volley.toolbox;

import com.nativex.network.volley.AuthFailureError;
import com.nativex.network.volley.NetworkResponse;
import com.nativex.network.volley.Request;
import com.nativex.network.volley.Response;
import com.nativex.network.volley.Response.ErrorListener;
import com.nativex.network.volley.Response.Listener;
import com.nativex.network.volley.VolleyLog;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class StringRequest extends Request<String> {
    private static final String PROTOCOL_CHARSET = "utf-8";
    private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", new Object[]{PROTOCOL_CHARSET});
    private Map<String, String> headers;
    private final Listener<String> mListener;
    private final String mRequestBody;

    public StringRequest(int method, String url, String requestBody, Listener<String> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.headers = new HashMap();
        this.mRequestBody = requestBody;
        this.mListener = listener;
    }

    public StringRequest(String url, Listener<String> listener, ErrorListener errorListener) {
        this(0, url, null, listener, errorListener);
    }

    public void setHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public Map<String, String> getHeaders() throws AuthFailureError {
        return this.headers;
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

    protected void deliverResponse(String response) {
        this.mListener.onResponse(response);
    }

    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }
}
