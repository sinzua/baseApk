package com.parse;

import bolts.Task;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import org.json.JSONException;
import org.json.JSONObject;

class ParseCurrentConfigController {
    ParseConfig currentConfig;
    private File currentConfigFile;
    private final Object currentConfigMutex = new Object();

    public ParseCurrentConfigController(File currentConfigFile) {
        this.currentConfigFile = currentConfigFile;
    }

    public Task<Void> setCurrentConfigAsync(final ParseConfig config) {
        return Task.call(new Callable<Void>() {
            public Void call() throws Exception {
                synchronized (ParseCurrentConfigController.this.currentConfigMutex) {
                    ParseCurrentConfigController.this.currentConfig = config;
                    ParseCurrentConfigController.this.saveToDisk(config);
                }
                return null;
            }
        }, ParseExecutors.io());
    }

    public Task<ParseConfig> getCurrentConfigAsync() {
        return Task.call(new Callable<ParseConfig>() {
            public ParseConfig call() throws Exception {
                synchronized (ParseCurrentConfigController.this.currentConfigMutex) {
                    if (ParseCurrentConfigController.this.currentConfig == null) {
                        ParseConfig config = ParseCurrentConfigController.this.getFromDisk();
                        ParseCurrentConfigController parseCurrentConfigController = ParseCurrentConfigController.this;
                        if (config == null) {
                            config = new ParseConfig();
                        }
                        parseCurrentConfigController.currentConfig = config;
                    }
                }
                return ParseCurrentConfigController.this.currentConfig;
            }
        }, ParseExecutors.io());
    }

    ParseConfig getFromDisk() {
        try {
            return new ParseConfig(ParseFileUtils.readFileToJSONObject(this.currentConfigFile), ParseDecoder.get());
        } catch (IOException e) {
            return null;
        } catch (JSONException e2) {
            return null;
        }
    }

    void clearCurrentConfigForTesting() {
        synchronized (this.currentConfigMutex) {
            this.currentConfig = null;
        }
    }

    void saveToDisk(ParseConfig config) {
        JSONObject object = new JSONObject();
        try {
            object.put("params", (JSONObject) NoObjectsEncoder.get().encode(config.getParams()));
            try {
                ParseFileUtils.writeJSONObjectToFile(this.currentConfigFile, object);
            } catch (IOException e) {
            }
        } catch (JSONException e2) {
            throw new RuntimeException("could not serialize config to JSON");
        }
    }
}
