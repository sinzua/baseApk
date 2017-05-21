package com.parse;

import bolts.Task;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

class ParseExecutors {
    private static final Object SCHEDULED_EXECUTOR_LOCK = new Object();
    private static ScheduledExecutorService scheduledExecutor;

    ParseExecutors() {
    }

    static ScheduledExecutorService scheduled() {
        synchronized (SCHEDULED_EXECUTOR_LOCK) {
            if (scheduledExecutor == null) {
                scheduledExecutor = Executors.newScheduledThreadPool(1);
            }
        }
        return scheduledExecutor;
    }

    static Executor main() {
        return Task.UI_THREAD_EXECUTOR;
    }

    static Executor io() {
        return Task.BACKGROUND_EXECUTOR;
    }
}
