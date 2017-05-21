package com.supersonic.mediationsdk.logger;

import com.supersonic.mediationsdk.logger.SupersonicLogger.SupersonicTag;
import java.lang.Thread.UncaughtExceptionHandler;

public class ThreadExceptionHandler implements UncaughtExceptionHandler {
    public void uncaughtException(Thread thread, Throwable ex) {
        SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, "Thread name =" + thread.getName(), ex);
    }
}
