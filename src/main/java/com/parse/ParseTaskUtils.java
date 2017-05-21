package com.parse;

import bolts.AggregateException;
import bolts.Continuation;
import bolts.Task;
import bolts.Task.TaskCompletionSource;
import java.util.concurrent.CancellationException;

class ParseTaskUtils {
    ParseTaskUtils() {
    }

    static <T> T wait(Task<T> task) throws ParseException {
        try {
            task.waitForCompletion();
            if (task.isFaulted()) {
                Exception error = task.getError();
                if (error instanceof ParseException) {
                    throw ((ParseException) error);
                } else if (error instanceof AggregateException) {
                    throw new ParseException(error);
                } else if (error instanceof RuntimeException) {
                    throw ((RuntimeException) error);
                } else {
                    throw new RuntimeException(error);
                }
            } else if (!task.isCancelled()) {
                return task.getResult();
            } else {
                throw new RuntimeException(new CancellationException());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static Task<Void> callbackOnMainThreadAsync(Task<Void> task, ParseCallback1<ParseException> callback) {
        return callbackOnMainThreadAsync((Task) task, (ParseCallback1) callback, false);
    }

    static Task<Void> callbackOnMainThreadAsync(Task<Void> task, final ParseCallback1<ParseException> callback, boolean reportCancellation) {
        return callback == null ? task : callbackOnMainThreadAsync((Task) task, new ParseCallback2<Void, ParseException>() {
            public void done(Void aVoid, ParseException e) {
                callback.done(e);
            }
        }, reportCancellation);
    }

    static <T> Task<T> callbackOnMainThreadAsync(Task<T> task, ParseCallback2<T, ParseException> callback) {
        return callbackOnMainThreadAsync((Task) task, (ParseCallback2) callback, false);
    }

    static <T> Task<T> callbackOnMainThreadAsync(Task<T> task, final ParseCallback2<T, ParseException> callback, final boolean reportCancellation) {
        if (callback == null) {
            return task;
        }
        final TaskCompletionSource tcs = Task.create();
        task.continueWith(new Continuation<T, Void>() {
            public Void then(final Task<T> task) throws Exception {
                if (!task.isCancelled() || reportCancellation) {
                    ParseExecutors.main().execute(new Runnable() {
                        public void run() {
                            try {
                                Exception error = task.getError();
                                if (!(error == null || (error instanceof ParseException))) {
                                    error = new ParseException(error);
                                }
                                callback.done(task.getResult(), (ParseException) error);
                            } finally {
                                if (task.isCancelled()) {
                                    tcs.setCancelled();
                                } else if (task.isFaulted()) {
                                    tcs.setError(task.getError());
                                } else {
                                    tcs.setResult(task.getResult());
                                }
                            }
                        }
                    });
                } else {
                    tcs.setCancelled();
                }
                return null;
            }
        });
        return tcs.getTask();
    }
}
