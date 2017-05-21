package com.parse;

import bolts.Continuation;
import bolts.Task;
import com.parse.ParseQuery.CachePolicy;
import java.util.List;
import java.util.concurrent.Callable;
import org.json.JSONException;
import org.json.JSONObject;

class CacheQueryController extends AbstractQueryController {
    private final NetworkQueryController networkController;

    private interface CommandDelegate<T> {
        Task<T> runFromCacheAsync();

        Task<T> runOnNetworkAsync(boolean z);
    }

    public CacheQueryController(NetworkQueryController network) {
        this.networkController = network;
    }

    public <T extends ParseObject> Task<List<T>> findAsync(final State<T> state, ParseUser user, final Task<Void> cancellationToken) {
        final String sessionToken = user != null ? user.getSessionToken() : null;
        return runCommandWithPolicyAsync(new CommandDelegate<List<T>>() {
            public Task<List<T>> runOnNetworkAsync(boolean retry) {
                return CacheQueryController.this.networkController.findAsync(state, sessionToken, retry, cancellationToken);
            }

            public Task<List<T>> runFromCacheAsync() {
                return CacheQueryController.this.findFromCacheAsync(state, sessionToken);
            }
        }, state.cachePolicy());
    }

    public <T extends ParseObject> Task<Integer> countAsync(final State<T> state, ParseUser user, final Task<Void> cancellationToken) {
        final String sessionToken = user != null ? user.getSessionToken() : null;
        return runCommandWithPolicyAsync(new CommandDelegate<Integer>() {
            public Task<Integer> runOnNetworkAsync(boolean retry) {
                return CacheQueryController.this.networkController.countAsync(state, sessionToken, retry, cancellationToken);
            }

            public Task<Integer> runFromCacheAsync() {
                return CacheQueryController.this.countFromCacheAsync(state, sessionToken);
            }
        }, state.cachePolicy());
    }

    private <T extends ParseObject> Task<List<T>> findFromCacheAsync(final State<T> state, String sessionToken) {
        final String cacheKey = ParseRESTQueryCommand.findCommand(state, sessionToken).getCacheKey();
        return Task.call(new Callable<List<T>>() {
            public List<T> call() throws Exception {
                JSONObject cached = ParseKeyValueCache.jsonFromKeyValueCache(cacheKey, state.maxCacheAge());
                if (cached == null) {
                    throw new ParseException(ParseException.CACHE_MISS, "results not cached");
                }
                try {
                    return CacheQueryController.this.networkController.convertFindResponse(state, cached);
                } catch (JSONException e) {
                    throw new ParseException(ParseException.CACHE_MISS, "the cache contains corrupted json");
                }
            }
        }, Task.BACKGROUND_EXECUTOR);
    }

    private <T extends ParseObject> Task<Integer> countFromCacheAsync(final State<T> state, String sessionToken) {
        final String cacheKey = ParseRESTQueryCommand.countCommand(state, sessionToken).getCacheKey();
        return Task.call(new Callable<Integer>() {
            public Integer call() throws Exception {
                JSONObject cached = ParseKeyValueCache.jsonFromKeyValueCache(cacheKey, state.maxCacheAge());
                if (cached == null) {
                    throw new ParseException(ParseException.CACHE_MISS, "results not cached");
                }
                try {
                    return Integer.valueOf(cached.getInt("count"));
                } catch (JSONException e) {
                    throw new ParseException(ParseException.CACHE_MISS, "the cache contains corrupted json");
                }
            }
        }, Task.BACKGROUND_EXECUTOR);
    }

    private <TResult> Task<TResult> runCommandWithPolicyAsync(final CommandDelegate<TResult> c, CachePolicy policy) {
        switch (policy) {
            case IGNORE_CACHE:
            case NETWORK_ONLY:
                return c.runOnNetworkAsync(true);
            case CACHE_ONLY:
                return c.runFromCacheAsync();
            case CACHE_ELSE_NETWORK:
                return c.runFromCacheAsync().continueWithTask(new Continuation<TResult, Task<TResult>>() {
                    public Task<TResult> then(Task<TResult> task) throws Exception {
                        if (task.getError() instanceof ParseException) {
                            return c.runOnNetworkAsync(true);
                        }
                        return task;
                    }
                });
            case NETWORK_ELSE_CACHE:
                return c.runOnNetworkAsync(false).continueWithTask(new Continuation<TResult, Task<TResult>>() {
                    public Task<TResult> then(Task<TResult> task) throws Exception {
                        Exception error = task.getError();
                        if ((error instanceof ParseException) && ((ParseException) error).getCode() == 100) {
                            return c.runFromCacheAsync();
                        }
                        return task;
                    }
                });
            case CACHE_THEN_NETWORK:
                throw new RuntimeException("You cannot use the cache policy CACHE_THEN_NETWORK with find()");
            default:
                throw new RuntimeException("Unknown cache policy: " + policy);
        }
    }
}
