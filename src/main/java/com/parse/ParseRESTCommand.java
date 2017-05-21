package com.parse;

import bolts.Task;
import com.parse.ParseRequest.Method;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

class ParseRESTCommand extends ParseRequest<JSONObject> {
    static final String HEADER_APPLICATION_ID = "X-Parse-Application-Id";
    static final String HEADER_APP_BUILD_VERSION = "X-Parse-App-Build-Version";
    static final String HEADER_APP_DISPLAY_VERSION = "X-Parse-App-Display-Version";
    static final String HEADER_CLIENT_KEY = "X-Parse-Client-Key";
    static final String HEADER_CLIENT_VERSION = "X-Parse-Client-Version";
    static final String HEADER_INSTALLATION_ID = "X-Parse-Installation-Id";
    private static final String HEADER_MASTER_KEY = "X-Parse-Master-Key";
    static final String HEADER_OS_VERSION = "X-Parse-OS-Version";
    private static final String HEADER_SESSION_TOKEN = "X-Parse-Session-Token";
    private static final String PARAMETER_METHOD_OVERRIDE = "_method";
    static final String USER_AGENT = "User-Agent";
    String httpPath;
    private String installationId;
    final JSONObject jsonParameters;
    private String localId;
    public String masterKey;
    private String operationSetUUID;
    private final String sessionToken;

    static abstract class Init<T extends Init<T>> {
        private String httpPath;
        private String installationId;
        private JSONObject jsonParameters;
        private String localId;
        public String masterKey;
        private Method method = Method.GET;
        private String operationSetUUID;
        private String sessionToken;

        abstract T self();

        Init() {
        }

        public T sessionToken(String sessionToken) {
            this.sessionToken = sessionToken;
            return self();
        }

        public T installationId(String installationId) {
            this.installationId = installationId;
            return self();
        }

        public T masterKey(String masterKey) {
            this.masterKey = masterKey;
            return self();
        }

        public T method(Method method) {
            this.method = method;
            return self();
        }

        public T httpPath(String httpPath) {
            this.httpPath = httpPath;
            return self();
        }

        public T jsonParameters(JSONObject jsonParameters) {
            this.jsonParameters = jsonParameters;
            return self();
        }

        public T operationSetUUID(String operationSetUUID) {
            this.operationSetUUID = operationSetUUID;
            return self();
        }

        public T localId(String localId) {
            this.localId = localId;
            return self();
        }
    }

    public static class Builder extends Init<Builder> {
        Builder self() {
            return this;
        }

        public ParseRESTCommand build() {
            return new ParseRESTCommand(this);
        }
    }

    private static LocalIdManager getLocalIdManager() {
        return ParseCorePlugins.getInstance().getLocalIdManager();
    }

    public ParseRESTCommand(String httpPath, Method httpMethod, Map<String, ?> parameters, String sessionToken) {
        this(httpPath, httpMethod, parameters != null ? (JSONObject) NoObjectsEncoder.get().encode(parameters) : null, sessionToken);
    }

    protected ParseRESTCommand(String httpPath, Method httpMethod, JSONObject jsonParameters, String sessionToken) {
        this(httpPath, httpMethod, jsonParameters, null, sessionToken);
    }

    private ParseRESTCommand(String httpPath, Method httpMethod, JSONObject jsonParameters, String localId, String sessionToken) {
        super(httpMethod, createUrl(httpPath));
        this.httpPath = httpPath;
        this.jsonParameters = jsonParameters;
        this.localId = localId;
        this.sessionToken = sessionToken;
    }

    ParseRESTCommand(Init<?> builder) {
        super(builder.method, createUrl(builder.httpPath));
        this.sessionToken = builder.sessionToken;
        this.installationId = builder.installationId;
        this.masterKey = builder.masterKey;
        this.httpPath = builder.httpPath;
        this.jsonParameters = builder.jsonParameters;
        this.operationSetUUID = builder.operationSetUUID;
        this.localId = builder.localId;
    }

    public static ParseRESTCommand fromJSONObject(JSONObject jsonObject) {
        String httpPath = jsonObject.optString("httpPath");
        Method httpMethod = Method.fromString(jsonObject.optString("httpMethod"));
        String sessionToken = jsonObject.optString("sessionToken", null);
        return new ParseRESTCommand(httpPath, httpMethod, jsonObject.optJSONObject("parameters"), jsonObject.optString("localId", null), sessionToken);
    }

    void enableRetrying() {
        setMaxRetries(4);
    }

    private static String createUrl(String httpPath) {
        return String.format("%s/1/%s", new Object[]{ParseObject.server, httpPath});
    }

    protected void addAdditionalHeaders(com.parse.ParseHttpRequest.Builder requestBuilder) {
        if (this.installationId != null) {
            requestBuilder.addHeader(HEADER_INSTALLATION_ID, this.installationId);
        }
        if (this.sessionToken != null) {
            requestBuilder.addHeader(HEADER_SESSION_TOKEN, this.sessionToken);
        }
        if (this.masterKey != null) {
            requestBuilder.addHeader(HEADER_MASTER_KEY, this.masterKey);
        }
    }

    protected ParseHttpRequest newRequest(Method method, String url, ProgressCallback uploadProgressCallback) {
        ParseHttpRequest request;
        if (this.jsonParameters == null || method == Method.POST || method == Method.PUT) {
            request = super.newRequest(method, url, uploadProgressCallback);
        } else {
            request = super.newRequest(Method.POST, url, uploadProgressCallback);
        }
        com.parse.ParseHttpRequest.Builder requestBuilder = new com.parse.ParseHttpRequest.Builder(request);
        addAdditionalHeaders(requestBuilder);
        return requestBuilder.build();
    }

    protected ParseHttpBody newBody(ProgressCallback uploadProgressCallback) {
        if (this.jsonParameters == null) {
            throw new IllegalArgumentException(String.format("Trying to execute a %s command without body parameters.", new Object[]{this.method.toString()}));
        }
        try {
            JSONObject parameters = this.jsonParameters;
            if (this.method == Method.GET || this.method == Method.DELETE) {
                parameters = new JSONObject(this.jsonParameters.toString());
                parameters.put(PARAMETER_METHOD_OVERRIDE, this.method.toString());
            }
            return new ParseByteArrayHttpBody(parameters.toString(), "application/json");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Task<JSONObject> executeAsync(ParseHttpClient client, ProgressCallback uploadProgressCallback, ProgressCallback downloadProgressCallback, Task<Void> cancellationToken) {
        resolveLocalIds();
        return super.executeAsync(client, uploadProgressCallback, downloadProgressCallback, (Task) cancellationToken);
    }

    protected Task<JSONObject> onResponseAsync(ParseHttpResponse response, ProgressCallback downloadProgressCallback) {
        try {
            String content = new String(ParseIOUtils.toByteArray(response.getContent()));
            int statusCode = response.getStatusCode();
            if (statusCode < 200 || statusCode >= 600) {
                return Task.forError(newPermanentException(-1, content));
            }
            try {
                JSONObject json = new JSONObject(content);
                if (statusCode >= 400 && statusCode < 500) {
                    return Task.forError(newPermanentException(json.optInt("code"), json.optString("error")));
                }
                if (statusCode >= 500) {
                    return Task.forError(newTemporaryException(json.optInt("code"), json.optString("error")));
                }
                return Task.forResult(json);
            } catch (JSONException e) {
                return Task.forError(newTemporaryException("bad json response", (Throwable) e));
            }
        } catch (IOException e2) {
            return Task.forError(e2);
        }
    }

    public String getCacheKey() {
        String json;
        if (this.jsonParameters != null) {
            try {
                json = toDeterministicString(this.jsonParameters);
            } catch (JSONException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        json = "";
        if (this.sessionToken != null) {
            json = json + this.sessionToken;
        }
        return String.format("ParseRESTCommand.%s.%s.%s", new Object[]{this.method.toString(), ParseDigestUtils.md5(this.httpPath), ParseDigestUtils.md5(json)});
    }

    static String toDeterministicString(Object o) throws JSONException {
        JSONStringer stringer = new JSONStringer();
        addToStringer(stringer, o);
        return stringer.toString();
    }

    private static void addToStringer(JSONStringer stringer, Object o) throws JSONException {
        if (o instanceof JSONObject) {
            stringer.object();
            JSONObject object = (JSONObject) o;
            Iterator<String> keyIterator = object.keys();
            ArrayList<String> keys = new ArrayList();
            while (keyIterator.hasNext()) {
                keys.add(keyIterator.next());
            }
            Collections.sort(keys);
            Iterator i$ = keys.iterator();
            while (i$.hasNext()) {
                String key = (String) i$.next();
                stringer.key(key);
                addToStringer(stringer, object.opt(key));
            }
            stringer.endObject();
        } else if (o instanceof JSONArray) {
            JSONArray array = (JSONArray) o;
            stringer.array();
            for (int i = 0; i < array.length(); i++) {
                addToStringer(stringer, array.get(i));
            }
            stringer.endArray();
        } else {
            stringer.value(o);
        }
    }

    static boolean isValidCommandJSONObject(JSONObject jsonObject) {
        return jsonObject.has("httpPath");
    }

    static boolean isValidOldFormatCommandJSONObject(JSONObject jsonObject) {
        return jsonObject.has("op");
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (this.httpPath != null) {
                jsonObject.put("httpPath", this.httpPath);
            }
            jsonObject.put("httpMethod", this.method.toString());
            if (this.jsonParameters != null) {
                jsonObject.put("parameters", this.jsonParameters);
            }
            if (this.sessionToken != null) {
                jsonObject.put("sessionToken", this.sessionToken);
            }
            if (this.localId != null) {
                jsonObject.put("localId", this.localId);
            }
            return jsonObject;
        } catch (JSONException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String getSessionToken() {
        return this.sessionToken;
    }

    public String getOperationSetUUID() {
        return this.operationSetUUID;
    }

    void setOperationSetUUID(String operationSetUUID) {
        this.operationSetUUID = operationSetUUID;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getLocalId() {
        return this.localId;
    }

    private void maybeChangeServerOperation() throws JSONException {
        if (this.localId != null && getLocalIdManager().getObjectId(this.localId) != null) {
            this.localId = null;
            this.httpPath += String.format("/%s", new Object[]{objectId});
            this.url = createUrl(this.httpPath);
            if (this.httpPath.startsWith("classes") && this.method == Method.POST) {
                this.method = Method.PUT;
            }
        }
    }

    public void resolveLocalIds() {
        try {
            ArrayList<JSONObject> localPointers = new ArrayList();
            getLocalPointersIn(this.jsonParameters, localPointers);
            Iterator i$ = localPointers.iterator();
            while (i$.hasNext()) {
                JSONObject pointer = (JSONObject) i$.next();
                String objectId = getLocalIdManager().getObjectId((String) pointer.get("localId"));
                if (objectId == null) {
                    throw new IllegalStateException("Tried to serialize a command referencing a new, unsaved object.");
                }
                pointer.put("objectId", objectId);
                pointer.remove("localId");
            }
            maybeChangeServerOperation();
        } catch (JSONException e) {
        }
    }

    public void retainLocalIds() {
        if (this.localId != null) {
            getLocalIdManager().retainLocalIdOnDisk(this.localId);
        }
        try {
            ArrayList<JSONObject> localPointers = new ArrayList();
            getLocalPointersIn(this.jsonParameters, localPointers);
            Iterator i$ = localPointers.iterator();
            while (i$.hasNext()) {
                getLocalIdManager().retainLocalIdOnDisk((String) ((JSONObject) i$.next()).get("localId"));
            }
        } catch (JSONException e) {
        }
    }

    public void releaseLocalIds() {
        if (this.localId != null) {
            getLocalIdManager().releaseLocalIdOnDisk(this.localId);
        }
        try {
            ArrayList<JSONObject> localPointers = new ArrayList();
            getLocalPointersIn(this.jsonParameters, localPointers);
            Iterator i$ = localPointers.iterator();
            while (i$.hasNext()) {
                getLocalIdManager().releaseLocalIdOnDisk((String) ((JSONObject) i$.next()).get("localId"));
            }
        } catch (JSONException e) {
        }
    }

    protected static void getLocalPointersIn(Object container, ArrayList<JSONObject> localPointers) throws JSONException {
        if (container instanceof JSONObject) {
            JSONObject object = (JSONObject) container;
            if ("Pointer".equals(object.opt("__type")) && object.has("localId")) {
                localPointers.add((JSONObject) container);
                return;
            }
            Iterator<String> keyIterator = object.keys();
            while (keyIterator.hasNext()) {
                getLocalPointersIn(object.get((String) keyIterator.next()), localPointers);
            }
        }
        if (container instanceof JSONArray) {
            JSONArray array = (JSONArray) container;
            for (int i = 0; i < array.length(); i++) {
                getLocalPointersIn(array.get(i), localPointers);
            }
        }
    }
}
