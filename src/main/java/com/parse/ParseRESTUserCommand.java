package com.parse;

import bolts.Task;
import com.parse.ParseHttpRequest.Builder;
import com.parse.ParseRequest.Method;
import com.ty.followboom.helpers.TrackHelper;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

class ParseRESTUserCommand extends ParseRESTCommand {
    private static final String HEADER_REVOCABLE_SESSION = "X-Parse-Revocable-Session";
    private static final String HEADER_TRUE = "1";
    private boolean isRevocableSessionEnabled;
    private int statusCode;

    public static ParseRESTUserCommand getCurrentUserCommand(String sessionToken) {
        return new ParseRESTUserCommand("users/me", Method.GET, null, sessionToken);
    }

    public static ParseRESTUserCommand signUpUserCommand(JSONObject parameters, String sessionToken, boolean revocableSession) {
        return new ParseRESTUserCommand("classes/_User", Method.POST, parameters, sessionToken, revocableSession);
    }

    public static ParseRESTUserCommand logInUserCommand(String username, String password, boolean revocableSession) {
        Map parameters = new HashMap();
        parameters.put("username", username);
        parameters.put("password", password);
        return new ParseRESTUserCommand(TrackHelper.LABEL_LOGIN, Method.GET, parameters, null, revocableSession);
    }

    public static ParseRESTUserCommand serviceLogInUserCommand(String authType, Map<String, String> authData, boolean revocableSession) {
        try {
            JSONObject authenticationData = new JSONObject();
            authenticationData.put(authType, PointerEncoder.get().encode(authData));
            JSONObject parameters = new JSONObject();
            parameters.put("authData", authenticationData);
            return serviceLogInUserCommand(parameters, null, revocableSession);
        } catch (JSONException e) {
            throw new RuntimeException("could not serialize object to JSON");
        }
    }

    public static ParseRESTUserCommand serviceLogInUserCommand(JSONObject parameters, String sessionToken, boolean revocableSession) {
        return new ParseRESTUserCommand("users", Method.POST, parameters, sessionToken, revocableSession);
    }

    public static ParseRESTUserCommand resetPasswordResetCommand(String email) {
        Map<String, String> parameters = new HashMap();
        parameters.put("email", email);
        return new ParseRESTUserCommand("requestPasswordReset", Method.POST, parameters, null);
    }

    private ParseRESTUserCommand(String httpPath, Method httpMethod, Map<String, ?> parameters, String sessionToken) {
        this(httpPath, httpMethod, (Map) parameters, sessionToken, false);
    }

    private ParseRESTUserCommand(String httpPath, Method httpMethod, Map<String, ?> parameters, String sessionToken, boolean isRevocableSessionEnabled) {
        super(httpPath, httpMethod, (Map) parameters, sessionToken);
        this.isRevocableSessionEnabled = isRevocableSessionEnabled;
    }

    private ParseRESTUserCommand(String httpPath, Method httpMethod, JSONObject parameters, String sessionToken, boolean isRevocableSessionEnabled) {
        super(httpPath, httpMethod, parameters, sessionToken);
        this.isRevocableSessionEnabled = isRevocableSessionEnabled;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    protected void addAdditionalHeaders(Builder requestBuilder) {
        super.addAdditionalHeaders(requestBuilder);
        if (this.isRevocableSessionEnabled) {
            requestBuilder.addHeader(HEADER_REVOCABLE_SESSION, "1");
        }
    }

    protected Task<JSONObject> onResponseAsync(ParseHttpResponse response, ProgressCallback progressCallback) {
        this.statusCode = response.getStatusCode();
        return super.onResponseAsync(response, progressCallback);
    }
}
