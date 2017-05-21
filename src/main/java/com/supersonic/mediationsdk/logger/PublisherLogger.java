package com.supersonic.mediationsdk.logger;

import com.supersonic.mediationsdk.logger.SupersonicLogger.SupersonicTag;

public class PublisherLogger extends SupersonicLogger {
    public static final String NAME = "publisher";
    private LogListener mLogListener;

    private PublisherLogger() {
        super(NAME);
    }

    public PublisherLogger(LogListener logListener, int debugLevel) {
        super(NAME, debugLevel);
        this.mLogListener = logListener;
    }

    public synchronized void log(SupersonicTag tag, String message, int logLevel) {
        if (this.mLogListener != null) {
            this.mLogListener.onLog(tag, message, logLevel);
        }
    }

    public void logException(SupersonicTag tag, String message, Throwable e) {
        log(tag, e.getMessage(), 3);
    }

    public void setLogListener(LogListener listener) {
        this.mLogListener = listener;
    }
}
