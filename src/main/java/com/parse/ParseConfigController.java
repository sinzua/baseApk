package com.parse;

import bolts.Continuation;
import bolts.Task;
import org.json.JSONObject;

class ParseConfigController {
    private ParseCurrentConfigController currentConfigController;
    private final ParseHttpClient restClient;

    public ParseConfigController(ParseHttpClient restClient, ParseCurrentConfigController currentConfigController) {
        this.restClient = restClient;
        this.currentConfigController = currentConfigController;
    }

    ParseCurrentConfigController getCurrentConfigController() {
        return this.currentConfigController;
    }

    public Task<ParseConfig> getAsync(String sessionToken) {
        ParseRESTCommand command = ParseRESTConfigCommand.fetchConfigCommand(sessionToken);
        command.enableRetrying();
        return command.executeAsync(this.restClient).onSuccessTask(new Continuation<JSONObject, Task<ParseConfig>>() {
            public Task<ParseConfig> then(Task<JSONObject> task) throws Exception {
                final ParseConfig config = new ParseConfig((JSONObject) task.getResult(), ParseDecoder.get());
                return ParseConfigController.this.currentConfigController.setCurrentConfigAsync(config).continueWith(new Continuation<Void, ParseConfig>() {
                    public ParseConfig then(Task<Void> task) throws Exception {
                        return config;
                    }
                });
            }
        });
    }
}
