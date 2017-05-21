package com.parse;

import bolts.Continuation;
import bolts.Task;
import bolts.Task.TaskCompletionSource;
import com.parse.ParseRequest.Method;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class ParseRESTObjectBatchCommand extends ParseRESTCommand {
    public static final int COMMAND_OBJECT_BATCH_MAX_SIZE = 50;
    private static final String KEY_RESULTS = "results";

    public static List<Task<JSONObject>> executeBatch(ParseHttpClient client, List<ParseRESTObjectCommand> commands, String sessionToken) {
        final int batchSize = commands.size();
        List<Task<JSONObject>> tasks = new ArrayList(batchSize);
        if (batchSize == 1) {
            tasks.add(((ParseRESTObjectCommand) commands.get(0)).executeAsync(client));
        } else if (batchSize > 50) {
            List<List<ParseRESTObjectCommand>> batches = Lists.partition(commands, 50);
            int size = batches.size();
            for (i = 0; i < size; i++) {
                tasks.addAll(executeBatch(client, (List) batches.get(i), sessionToken));
            }
        } else {
            List<TaskCompletionSource> arrayList = new ArrayList(batchSize);
            for (i = 0; i < batchSize; i++) {
                TaskCompletionSource tcs = Task.create();
                arrayList.add(tcs);
                tasks.add(tcs.getTask());
            }
            List<JSONObject> requests = new ArrayList(batchSize);
            try {
                for (ParseRESTObjectCommand command : commands) {
                    JSONObject requestParameters = new JSONObject();
                    requestParameters.put(ParametersKeys.METHOD, command.method.toString());
                    requestParameters.put("path", String.format("/1/%s", new Object[]{command.httpPath}));
                    JSONObject body = command.jsonParameters;
                    if (body != null) {
                        requestParameters.put("body", body);
                    }
                    requests.add(requestParameters);
                }
                Map<String, List<JSONObject>> parameters = new HashMap();
                parameters.put("requests", requests);
                final List<TaskCompletionSource> list = arrayList;
                new ParseRESTObjectBatchCommand("batch", Method.POST, parameters, sessionToken).executeAsync(client).continueWith(new Continuation<JSONObject, Void>() {
                    public Void then(Task<JSONObject> task) throws Exception {
                        int i;
                        TaskCompletionSource tcs;
                        if (task.isFaulted() || task.isCancelled()) {
                            for (i = 0; i < batchSize; i++) {
                                tcs = (TaskCompletionSource) list.get(i);
                                if (task.isFaulted()) {
                                    tcs.setError(task.getError());
                                } else {
                                    tcs.setCancelled();
                                }
                            }
                        }
                        JSONArray results = ((JSONObject) task.getResult()).getJSONArray(ParseRESTObjectBatchCommand.KEY_RESULTS);
                        int resultLength = results.length();
                        if (resultLength != batchSize) {
                            for (i = 0; i < batchSize; i++) {
                                ((TaskCompletionSource) list.get(i)).setError(new IllegalStateException("Batch command result count expected: " + batchSize + " but was: " + resultLength));
                            }
                        }
                        for (i = 0; i < batchSize; i++) {
                            JSONObject result = results.getJSONObject(i);
                            tcs = (TaskCompletionSource) list.get(i);
                            if (result.has("success")) {
                                tcs.setResult(result.getJSONObject("success"));
                            } else if (result.has("error")) {
                                JSONObject error = result.getJSONObject("error");
                                tcs.setError(new ParseException(error.getInt("code"), error.getString("error")));
                            }
                        }
                        return null;
                    }
                });
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return tasks;
    }

    private ParseRESTObjectBatchCommand(String httpPath, Method httpMethod, Map<String, ?> parameters, String sessionToken) {
        super(httpPath, httpMethod, (Map) parameters, sessionToken);
    }

    protected Task<JSONObject> onResponseAsync(ParseHttpResponse response, ProgressCallback downloadProgressCallback) {
        Task<JSONObject> toByteArray;
        InputStream responseStream = null;
        try {
            responseStream = response.getContent();
            toByteArray = ParseIOUtils.toByteArray(responseStream);
            String content = new String(toByteArray);
            String str;
            try {
                JSONArray results = new JSONArray(content);
                JSONObject json = new JSONObject();
                json.put(KEY_RESULTS, results);
                str = content;
                return Task.forResult(json);
            } catch (JSONException e) {
                str = content;
                return Task.forError(newTemporaryException("bad json response", (Throwable) e));
            }
        } catch (IOException e2) {
            toByteArray = Task.forError(e2);
            return toByteArray;
        } finally {
            ParseIOUtils.closeQuietly(responseStream);
        }
    }
}
