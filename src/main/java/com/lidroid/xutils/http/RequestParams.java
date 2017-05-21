package com.lidroid.xutils.http;

import android.text.TextUtils;
import com.lidroid.xutils.http.client.entity.BodyParamsEntity;
import com.lidroid.xutils.http.client.multipart.HttpMultipartMode;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.ContentBody;
import com.lidroid.xutils.http.client.multipart.content.FileBody;
import com.lidroid.xutils.http.client.multipart.content.InputStreamBody;
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.lidroid.xutils.task.Priority;
import com.lidroid.xutils.util.LogUtils;
import com.supersonicads.sdk.precache.DownloadManager;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

public class RequestParams {
    private HttpEntity bodyEntity;
    private List<NameValuePair> bodyParams;
    private String charset = DownloadManager.UTF8_CHARSET;
    private HashMap<String, ContentBody> fileParams;
    private List<HeaderItem> headers;
    private Priority priority;
    private List<NameValuePair> queryStringParams;

    public class HeaderItem {
        public final Header header;
        public final boolean overwrite;

        public HeaderItem(Header header) {
            this.overwrite = false;
            this.header = header;
        }

        public HeaderItem(Header header, boolean overwrite) {
            this.overwrite = overwrite;
            this.header = header;
        }

        public HeaderItem(String name, String value) {
            this.overwrite = false;
            this.header = new BasicHeader(name, value);
        }

        public HeaderItem(String name, String value, boolean overwrite) {
            this.overwrite = overwrite;
            this.header = new BasicHeader(name, value);
        }
    }

    public RequestParams(String charset) {
        if (!TextUtils.isEmpty(charset)) {
            this.charset = charset;
        }
    }

    public Priority getPriority() {
        return this.priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getCharset() {
        return this.charset;
    }

    public void setContentType(String contentType) {
        setHeader("Content-Type", contentType);
    }

    public void addHeader(Header header) {
        if (this.headers == null) {
            this.headers = new ArrayList();
        }
        this.headers.add(new HeaderItem(header));
    }

    public void addHeader(String name, String value) {
        if (this.headers == null) {
            this.headers = new ArrayList();
        }
        this.headers.add(new HeaderItem(name, value));
    }

    public void addHeaders(List<Header> headers) {
        if (this.headers == null) {
            this.headers = new ArrayList();
        }
        for (Header header : headers) {
            this.headers.add(new HeaderItem(header));
        }
    }

    public void setHeader(Header header) {
        if (this.headers == null) {
            this.headers = new ArrayList();
        }
        this.headers.add(new HeaderItem(header, true));
    }

    public void setHeader(String name, String value) {
        if (this.headers == null) {
            this.headers = new ArrayList();
        }
        this.headers.add(new HeaderItem(name, value, true));
    }

    public void setHeaders(List<Header> headers) {
        if (this.headers == null) {
            this.headers = new ArrayList();
        }
        for (Header header : headers) {
            this.headers.add(new HeaderItem(header, true));
        }
    }

    public void addQueryStringParameter(String name, String value) {
        if (this.queryStringParams == null) {
            this.queryStringParams = new ArrayList();
        }
        this.queryStringParams.add(new BasicNameValuePair(name, value));
    }

    public void addQueryStringParameter(NameValuePair nameValuePair) {
        if (this.queryStringParams == null) {
            this.queryStringParams = new ArrayList();
        }
        this.queryStringParams.add(nameValuePair);
    }

    public void addQueryStringParameter(List<NameValuePair> nameValuePairs) {
        if (this.queryStringParams == null) {
            this.queryStringParams = new ArrayList();
        }
        if (nameValuePairs != null && nameValuePairs.size() > 0) {
            for (NameValuePair pair : nameValuePairs) {
                this.queryStringParams.add(pair);
            }
        }
    }

    public void addBodyParameter(String name, String value) {
        if (this.bodyParams == null) {
            this.bodyParams = new ArrayList();
        }
        this.bodyParams.add(new BasicNameValuePair(name, value));
    }

    public void addBodyParameter(NameValuePair nameValuePair) {
        if (this.bodyParams == null) {
            this.bodyParams = new ArrayList();
        }
        this.bodyParams.add(nameValuePair);
    }

    public void addBodyParameter(List<NameValuePair> nameValuePairs) {
        if (this.bodyParams == null) {
            this.bodyParams = new ArrayList();
        }
        if (nameValuePairs != null && nameValuePairs.size() > 0) {
            for (NameValuePair pair : nameValuePairs) {
                this.bodyParams.add(pair);
            }
        }
    }

    public void addBodyParameter(String key, File file) {
        if (this.fileParams == null) {
            this.fileParams = new HashMap();
        }
        this.fileParams.put(key, new FileBody(file));
    }

    public void addBodyParameter(String key, File file, String mimeType) {
        if (this.fileParams == null) {
            this.fileParams = new HashMap();
        }
        this.fileParams.put(key, new FileBody(file, mimeType));
    }

    public void addBodyParameter(String key, File file, String mimeType, String charset) {
        if (this.fileParams == null) {
            this.fileParams = new HashMap();
        }
        this.fileParams.put(key, new FileBody(file, mimeType, charset));
    }

    public void addBodyParameter(String key, File file, String fileName, String mimeType, String charset) {
        if (this.fileParams == null) {
            this.fileParams = new HashMap();
        }
        this.fileParams.put(key, new FileBody(file, fileName, mimeType, charset));
    }

    public void addBodyParameter(String key, InputStream stream, long length) {
        if (this.fileParams == null) {
            this.fileParams = new HashMap();
        }
        this.fileParams.put(key, new InputStreamBody(stream, length));
    }

    public void addBodyParameter(String key, InputStream stream, long length, String fileName) {
        if (this.fileParams == null) {
            this.fileParams = new HashMap();
        }
        this.fileParams.put(key, new InputStreamBody(stream, length, fileName));
    }

    public void addBodyParameter(String key, InputStream stream, long length, String fileName, String mimeType) {
        if (this.fileParams == null) {
            this.fileParams = new HashMap();
        }
        this.fileParams.put(key, new InputStreamBody(stream, length, fileName, mimeType));
    }

    public void setBodyEntity(HttpEntity bodyEntity) {
        this.bodyEntity = bodyEntity;
        if (this.bodyParams != null) {
            this.bodyParams.clear();
            this.bodyParams = null;
        }
        if (this.fileParams != null) {
            this.fileParams.clear();
            this.fileParams = null;
        }
    }

    public HttpEntity getEntity() {
        if (this.bodyEntity != null) {
            return this.bodyEntity;
        }
        if (this.fileParams != null && !this.fileParams.isEmpty()) {
            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.STRICT, null, Charset.forName(this.charset));
            if (!(this.bodyParams == null || this.bodyParams.isEmpty())) {
                for (NameValuePair param : this.bodyParams) {
                    try {
                        multipartEntity.addPart(param.getName(), new StringBody(param.getValue()));
                    } catch (UnsupportedEncodingException e) {
                        LogUtils.e(e.getMessage(), e);
                    }
                }
            }
            for (Entry<String, ContentBody> entry : this.fileParams.entrySet()) {
                multipartEntity.addPart((String) entry.getKey(), (ContentBody) entry.getValue());
            }
            return multipartEntity;
        } else if (this.bodyParams == null || this.bodyParams.isEmpty()) {
            return null;
        } else {
            return new BodyParamsEntity(this.bodyParams, this.charset);
        }
    }

    public List<NameValuePair> getQueryStringParams() {
        return this.queryStringParams;
    }

    public List<HeaderItem> getHeaders() {
        return this.headers;
    }
}
