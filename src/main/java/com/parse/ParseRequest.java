package com.parse;

import android.os.Build.VERSION;
import bolts.Continuation;
import bolts.Task;
import bolts.Task.TaskCompletionSource;
import com.parse.ParseHttpRequest.Builder;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.http.client.ClientProtocolException;

abstract class ParseRequest<Response> {
    private static final int CORE_POOL_SIZE = ((CPU_COUNT * 2) + 1);
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    static final long DEFAULT_INITIAL_RETRY_DELAY = 1000;
    protected static final int DEFAULT_MAX_RETRIES = 4;
    private static final long KEEP_ALIVE_TIME = 1;
    private static final int MAX_POOL_SIZE = (((CPU_COUNT * 2) * 2) + 1);
    private static final int MAX_QUEUE_SIZE = 128;
    static final ExecutorService NETWORK_EXECUTOR = newThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, 1, TimeUnit.SECONDS, new LinkedBlockingQueue(128), sThreadFactory);
    private static ParseHttpClient defaultClient = null;
    private static long defaultInitialRetryDelay = DEFAULT_INITIAL_RETRY_DELAY;
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "ParseRequest.NETWORK_EXECUTOR-thread-" + this.mCount.getAndIncrement());
        }
    };
    private int maxRetries;
    Method method;
    String url;

    public enum Method {
        GET,
        POST,
        PUT,
        DELETE;

        public static Method fromString(String string) {
            Object obj = -1;
            switch (string.hashCode()) {
                case 70454:
                    if (string.equals("GET")) {
                        obj = null;
                        break;
                    }
                    break;
                case 79599:
                    if (string.equals("PUT")) {
                        obj = 2;
                        break;
                    }
                    break;
                case 2461856:
                    if (string.equals("POST")) {
                        obj = 1;
                        break;
                    }
                    break;
                case 2012838315:
                    if (string.equals("DELETE")) {
                        obj = 3;
                        break;
                    }
                    break;
            }
            switch (obj) {
                case null:
                    return GET;
                case 1:
                    return POST;
                case 2:
                    return PUT;
                case 3:
                    return DELETE;
                default:
                    return null;
            }
        }

        public String toString() {
            switch (this) {
                case GET:
                    return "GET";
                case POST:
                    return "POST";
                case PUT:
                    return "PUT";
                case DELETE:
                    return "DELETE";
                default:
                    return null;
            }
        }
    }

    private static class ParseRequestException extends ParseException {
        boolean isPermanentFailure = false;

        public ParseRequestException(int theCode, String theMessage) {
            super(theCode, theMessage);
        }

        public ParseRequestException(int theCode, String message, Throwable cause) {
            super(theCode, message, cause);
        }
    }

    protected abstract Task<Response> onResponseAsync(ParseHttpResponse parseHttpResponse, ProgressCallback progressCallback);

    private static ThreadPoolExecutor newThreadPoolExecutor(int corePoolSize, int maxPoolSize, long keepAliveTime, TimeUnit timeUnit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, timeUnit, workQueue, threadFactory);
        if (VERSION.SDK_INT >= 9) {
            executor.allowCoreThreadTimeOut(true);
        }
        return executor;
    }

    @Deprecated
    public static void setDefaultClient(ParseHttpClient client) {
        defaultClient = client;
    }

    @Deprecated
    public static ParseHttpClient getDefaultClient() {
        if (defaultClient != null) {
            return defaultClient;
        }
        throw new IllegalStateException("Can't send Parse HTTPS request before Parse.initialize()");
    }

    public static void setDefaultInitialRetryDelay(long delay) {
        defaultInitialRetryDelay = delay;
    }

    public static long getDefaultInitialRetryDelay() {
        return defaultInitialRetryDelay;
    }

    public ParseRequest(String url) {
        this(Method.GET, url);
    }

    public ParseRequest(Method method, String url) {
        this.maxRetries = 4;
        this.method = method;
        this.url = url;
    }

    public void setMaxRetries(int max) {
        this.maxRetries = max;
    }

    protected ParseHttpBody newBody(ProgressCallback uploadProgressCallback) {
        return null;
    }

    protected ParseHttpRequest newRequest(Method method, String url, ProgressCallback uploadProgressCallback) {
        Builder requestBuilder = new Builder().setMethod(method).setUrl(url);
        switch (method) {
            case GET:
            case DELETE:
                break;
            case POST:
            case PUT:
                requestBuilder.setBody(newBody(uploadProgressCallback));
                break;
            default:
                throw new IllegalStateException("Invalid method " + method);
        }
        return requestBuilder.build();
    }

    private Task<Response> sendOneRequestAsync(final ParseHttpClient client, final ParseHttpRequest request, final ProgressCallback downloadProgressCallback) {
        return Task.forResult(null).onSuccessTask(new Continuation<Void, Task<Response>>() {
            public Task<Response> then(Task<Void> task) throws Exception {
                return ParseRequest.this.onResponseAsync(client.execute(request), downloadProgressCallback);
            }
        }, NETWORK_EXECUTOR).continueWithTask(new Continuation<Response, Task<Response>>() {
            public Task<Response> then(Task<Response> task) throws Exception {
                if (!task.isFaulted()) {
                    return task;
                }
                Throwable error = task.getError();
                if (error instanceof ClientProtocolException) {
                    return Task.forError(ParseRequest.this.newTemporaryException("bad protocol", error));
                }
                if (error instanceof IOException) {
                    return Task.forError(ParseRequest.this.newTemporaryException("i/o failure", error));
                }
                return task;
            }
        }, Task.BACKGROUND_EXECUTOR);
    }

    @Deprecated
    public Task<Response> executeAsync() {
        return executeAsync(getDefaultClient());
    }

    @Deprecated
    public Task<Response> executeAsync(ProgressCallback uploadProgressCallback, ProgressCallback downloadProgressCallback, Task<Void> cancellationToken) {
        return executeAsync(getDefaultClient(), uploadProgressCallback, downloadProgressCallback, (Task) cancellationToken);
    }

    public Task<Response> executeAsync(ParseHttpClient client) {
        return executeAsync(client, (ProgressCallback) null, null, null);
    }

    public Task<Response> executeAsync(ParseHttpClient client, Task<Void> cancellationToken) {
        return executeAsync(client, null, null, (Task) cancellationToken);
    }

    public Task<Response> executeAsync(ParseHttpClient client, ProgressCallback uploadProgressCallback, ProgressCallback downloadProgressCallback) {
        return executeAsync(client, uploadProgressCallback, downloadProgressCallback, null);
    }

    public Task<Response> executeAsync(ParseHttpClient client, ProgressCallback uploadProgressCallback, ProgressCallback downloadProgressCallback, Task<Void> cancellationToken) {
        return executeAsync(client, newRequest(this.method, this.url, uploadProgressCallback), downloadProgressCallback, (Task) cancellationToken);
    }

    private Task<Response> executeAsync(ParseHttpClient client, ParseHttpRequest request, ProgressCallback downloadProgressCallback, Task<Void> cancellationToken) {
        return executeAsync(client, request, 0, defaultInitialRetryDelay + ((long) (((double) defaultInitialRetryDelay) * Math.random())), downloadProgressCallback, cancellationToken);
    }

    private Task<Response> executeAsync(ParseHttpClient client, ParseHttpRequest request, int attemptsMade, long delay, ProgressCallback downloadProgressCallback, Task<Void> cancellationToken) {
        if (cancellationToken != null && cancellationToken.isCancelled()) {
            return Task.cancelled();
        }
        final Task<Void> task = cancellationToken;
        final int i = attemptsMade;
        final long j = delay;
        final ParseHttpClient parseHttpClient = client;
        final ParseHttpRequest parseHttpRequest = request;
        final ProgressCallback progressCallback = downloadProgressCallback;
        return sendOneRequestAsync(client, request, downloadProgressCallback).continueWithTask(new Continuation<Response, Task<Response>>() {
            public Task<Response> then(Task<Response> task) throws Exception {
                Exception e = task.getError();
                if (!task.isFaulted() || !(e instanceof ParseException)) {
                    return task;
                }
                if (task != null && task.isCancelled()) {
                    return Task.cancelled();
                }
                if (((e instanceof ParseRequestException) && ((ParseRequestException) e).isPermanentFailure) || i >= ParseRequest.this.maxRetries) {
                    return task;
                }
                PLog.i("com.parse.ParseRequest", "Request failed. Waiting " + j + " milliseconds before attempt #" + (i + 1));
                final TaskCompletionSource retryTask = Task.create();
                ParseExecutors.scheduled().schedule(new Runnable() {
                    public void run() {
                        ParseRequest.this.executeAsync(parseHttpClient, parseHttpRequest, i + 1, j * 2, progressCallback, task).continueWithTask(new Continuation<Response, Task<Void>>() {
                            public Task<Void> then(Task<Response> task) throws Exception {
                                if (task.isCancelled()) {
                                    retryTask.setCancelled();
                                } else if (task.isFaulted()) {
                                    retryTask.setError(task.getError());
                                } else {
                                    retryTask.setResult(task.getResult());
                                }
                                return null;
                            }
                        });
                    }
                }, j, TimeUnit.MILLISECONDS);
                return retryTask.getTask();
            }
        });
    }

    protected ParseException newPermanentException(int code, String message) {
        ParseRequestException e = new ParseRequestException(code, message);
        e.isPermanentFailure = true;
        return e;
    }

    protected ParseException newTemporaryException(int code, String message) {
        ParseRequestException e = new ParseRequestException(code, message);
        e.isPermanentFailure = false;
        return e;
    }

    protected ParseException newTemporaryException(String message, Throwable t) {
        ParseRequestException e = new ParseRequestException(100, message, t);
        e.isPermanentFailure = false;
        return e;
    }
}
