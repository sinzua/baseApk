package com.parse;

import bolts.Continuation;
import bolts.Task;
import com.parse.ParseQuery.CachePolicy;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class NetworkQueryController extends AbstractQueryController {
    private static final String TAG = "NetworkQueryController";
    private final ParseHttpClient restClient;

    public NetworkQueryController(ParseHttpClient restClient) {
        this.restClient = restClient;
    }

    public <T extends ParseObject> Task<List<T>> findAsync(State<T> state, ParseUser user, Task<Void> cancellationToken) {
        return findAsync(state, user != null ? user.getSessionToken() : null, true, cancellationToken);
    }

    public <T extends ParseObject> Task<Integer> countAsync(State<T> state, ParseUser user, Task<Void> cancellationToken) {
        return countAsync(state, user != null ? user.getSessionToken() : null, true, cancellationToken);
    }

    <T extends ParseObject> Task<List<T>> findAsync(State<T> state, String sessionToken, boolean shouldRetry, Task<Void> ct) {
        final long queryStart = System.nanoTime();
        final ParseRESTCommand command = ParseRESTQueryCommand.findCommand(state, sessionToken);
        if (shouldRetry) {
            command.enableRetrying();
        }
        final long querySent = System.nanoTime();
        final State<T> state2 = state;
        return command.executeAsync(this.restClient, ct).onSuccess(new Continuation<JSONObject, List<T>>() {
            public List<T> then(Task<JSONObject> task) throws Exception {
                JSONObject json = (JSONObject) task.getResult();
                CachePolicy policy = state2.cachePolicy();
                if (!(policy == null || policy == CachePolicy.IGNORE_CACHE)) {
                    ParseKeyValueCache.saveToKeyValueCache(command.getCacheKey(), json.toString());
                }
                long queryReceived = System.nanoTime();
                List<T> response = NetworkQueryController.this.convertFindResponse(state2, (JSONObject) task.getResult());
                long objectsParsed = System.nanoTime();
                if (json.has("trace")) {
                    Object serverTrace = json.get("trace");
                    PLog.d("ParseQuery", String.format("Query pre-processing took %f seconds\n%s\nClient side parsing took %f seconds\n", new Object[]{Float.valueOf(((float) (querySent - queryStart)) / 1000000.0f), serverTrace, Float.valueOf(((float) (objectsParsed - queryReceived)) / 1000000.0f)}));
                }
                return response;
            }
        }, Task.BACKGROUND_EXECUTOR);
    }

    <T extends ParseObject> Task<Integer> countAsync(final State<T> state, String sessionToken, boolean shouldRetry, Task<Void> ct) {
        final ParseRESTCommand command = ParseRESTQueryCommand.countCommand(state, sessionToken);
        if (shouldRetry) {
            command.enableRetrying();
        }
        return command.executeAsync(this.restClient, ct).onSuccessTask(new Continuation<JSONObject, Task<JSONObject>>() {
            public Task<JSONObject> then(Task<JSONObject> task) throws Exception {
                CachePolicy policy = state.cachePolicy();
                if (!(policy == null || policy == CachePolicy.IGNORE_CACHE)) {
                    ParseKeyValueCache.saveToKeyValueCache(command.getCacheKey(), ((JSONObject) task.getResult()).toString());
                }
                return task;
            }
        }, Task.BACKGROUND_EXECUTOR).onSuccess(new Continuation<JSONObject, Integer>() {
            public Integer then(Task<JSONObject> task) throws Exception {
                return Integer.valueOf(((JSONObject) task.getResult()).optInt("count"));
            }
        });
    }

    <T extends ParseObject> List<T> convertFindResponse(State<T> state, JSONObject response) throws JSONException {
        ArrayList<T> answer = new ArrayList();
        JSONArray results = response.getJSONArray("results");
        if (results == null) {
            PLog.d(TAG, "null results in find response");
        } else {
            String resultClassName = response.optString("className", null);
            if (resultClassName == null) {
                resultClassName = state.className();
            }
            for (int i = 0; i < results.length(); i++) {
                T object = ParseObject.fromJSON(results.getJSONObject(i), resultClassName, state.selectedKeys() == null);
                answer.add(object);
                RelationConstraint relation = (RelationConstraint) state.constraints().get("$relatedTo");
                if (relation != null) {
                    relation.getRelation().addKnownObject(object);
                }
            }
        }
        return answer;
    }
}
