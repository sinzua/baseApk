package com.parse;

import bolts.Continuation;
import bolts.Task;
import java.util.Map;

public final class ParseCloud {
    static ParseCloudCodeController getCloudCodeController() {
        return ParseCorePlugins.getInstance().getCloudCodeController();
    }

    public static <T> Task<T> callFunctionInBackground(final String name, final Map<String, ?> params) {
        return ParseUser.getCurrentSessionTokenAsync().onSuccessTask(new Continuation<String, Task<T>>() {
            public Task<T> then(Task<String> task) throws Exception {
                return ParseCloud.getCloudCodeController().callFunctionInBackground(name, params, (String) task.getResult());
            }
        });
    }

    public static <T> T callFunction(String name, Map<String, ?> params) throws ParseException {
        return ParseTaskUtils.wait(callFunctionInBackground(name, params));
    }

    public static <T> void callFunctionInBackground(String name, Map<String, ?> params, FunctionCallback<T> callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(callFunctionInBackground(name, params), (ParseCallback2) callback);
    }

    private ParseCloud() {
    }
}
