package com.parse;

import com.parse.ParseRequest.Method;
import org.json.JSONObject;

class ParseRESTSessionCommand extends ParseRESTCommand {
    public static ParseRESTSessionCommand getCurrentSessionCommand(String sessionToken) {
        return new ParseRESTSessionCommand("sessions/me", Method.GET, null, sessionToken);
    }

    public static ParseRESTSessionCommand revoke(String sessionToken) {
        return new ParseRESTSessionCommand("logout", Method.POST, new JSONObject(), sessionToken);
    }

    public static ParseRESTSessionCommand upgradeToRevocableSessionCommand(String sessionToken) {
        return new ParseRESTSessionCommand("upgradeToRevocableSession", Method.POST, new JSONObject(), sessionToken);
    }

    private ParseRESTSessionCommand(String httpPath, Method httpMethod, JSONObject jsonParameters, String sessionToken) {
        super(httpPath, httpMethod, jsonParameters, sessionToken);
    }
}
