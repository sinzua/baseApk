package com.parse;

import com.parse.ParseRequest.Method;
import java.util.Map;

class ParseRESTCloudCommand extends ParseRESTCommand {
    private ParseRESTCloudCommand(String httpPath, Method httpMethod, Map<String, ?> parameters, String sessionToken) {
        super(httpPath, httpMethod, (Map) parameters, sessionToken);
    }

    public static ParseRESTCloudCommand callFunctionCommand(String functionName, Map<String, ?> parameters, String sessionToken) {
        return new ParseRESTCloudCommand(String.format("functions/%s", new Object[]{functionName}), Method.POST, parameters, sessionToken);
    }
}
