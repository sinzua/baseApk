package com.parse;

import android.net.Uri;
import com.parse.ParseRequest.Method;
import org.json.JSONObject;

class ParseRESTObjectCommand extends ParseRESTCommand {
    public ParseRESTObjectCommand(String httpPath, Method httpMethod, JSONObject parameters, String sessionToken) {
        super(httpPath, httpMethod, parameters, sessionToken);
    }

    public static ParseRESTObjectCommand getObjectCommand(String objectId, String className, String sessionToken) {
        return new ParseRESTObjectCommand(String.format("classes/%s/%s", new Object[]{Uri.encode(className), Uri.encode(objectId)}), Method.GET, null, sessionToken);
    }

    public static ParseRESTObjectCommand saveObjectCommand(State state, JSONObject operations, String sessionToken) {
        if (state.objectId() == null) {
            return createObjectCommand(state.className(), operations, sessionToken);
        }
        return updateObjectCommand(state.objectId(), state.className(), operations, sessionToken);
    }

    private static ParseRESTObjectCommand createObjectCommand(String className, JSONObject changes, String sessionToken) {
        return new ParseRESTObjectCommand(String.format("classes/%s", new Object[]{Uri.encode(className)}), Method.POST, changes, sessionToken);
    }

    private static ParseRESTObjectCommand updateObjectCommand(String objectId, String className, JSONObject changes, String sessionToken) {
        return new ParseRESTObjectCommand(String.format("classes/%s/%s", new Object[]{Uri.encode(className), Uri.encode(objectId)}), Method.PUT, changes, sessionToken);
    }

    public static ParseRESTObjectCommand deleteObjectCommand(State state, String sessionToken) {
        String httpPath = String.format("classes/%s", new Object[]{Uri.encode(state.className())});
        if (state.objectId() != null) {
            httpPath = httpPath + String.format("/%s", new Object[]{Uri.encode(objectId)});
        }
        return new ParseRESTObjectCommand(httpPath, Method.DELETE, null, sessionToken);
    }
}
