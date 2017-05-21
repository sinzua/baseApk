package com.nativex.volleytoolbox;

import com.google.gson.Gson;
import com.nativex.network.volley.AuthFailureError;
import com.nativex.network.volley.NetworkResponse;
import com.nativex.network.volley.ParseError;
import com.nativex.network.volley.Request;
import com.nativex.network.volley.Response;
import com.nativex.network.volley.Response.ErrorListener;
import com.nativex.network.volley.Response.Listener;
import com.nativex.network.volley.VolleyLog;
import com.nativex.network.volley.toolbox.HttpHeaderParser;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class GsonRequest<T> extends Request<T> {
    private static final String ACCEPT = "Accept";
    private static final String ACCEPT_TYPE = "application/json";
    private static final String PROTOCOL_CHARSET = "utf-8";
    private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", new Object[]{PROTOCOL_CHARSET});
    private final Map<String, String> headers = new HashMap();
    private final Gson mGson = new Gson();
    private final Class<T> mJavaClass;
    private final Listener<T> mListener;
    private final String mRequestBody;

    public GsonRequest(int method, String url, Class<T> cls, String requestBody, Listener<T> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mJavaClass = cls;
        this.mListener = listener;
        this.mRequestBody = requestBody;
        setHeader(ACCEPT, "application/json");
    }

    protected void deliverResponse(T response) {
        this.mListener.onResponse(response);
    }

    void setHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public Map<String, String> getHeaders() throws AuthFailureError {
        return this.headers;
    }

    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(this.mGson.fromJson(new String(response.data, HttpHeaderParser.parseCharset(response.headers)), this.mJavaClass), HttpHeaderParser.parseCacheHeaders(response));
        } catch (Throwable e) {
            return Response.error(new ParseError(e));
        } catch (Throwable je) {
            return Response.error(new ParseError(je));
        }
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
