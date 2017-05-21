package com.supersonic.mediationsdk.logger;

public abstract class SupersonicLogger {
    protected int mDebugLevel;
    protected String mLoggerName;

    public class SupersonicLogLevel {
        public static final int ERROR = 3;
        public static final int INFO = 1;
        public static final int VERBOSE = 0;
        public static final int WARNING = 2;
    }

    public enum SupersonicTag {
        API,
        ADAPTER_API,
        CALLBACK,
        ADAPTER_CALLBACK,
        NETWORK,
        INTERNAL,
        NATIVE
    }

    public abstract void log(SupersonicTag supersonicTag, String str, int i);

    public abstract void logException(SupersonicTag supersonicTag, String str, Throwable th);

    public SupersonicLogger(String loggerName) {
        this.mLoggerName = loggerName;
        this.mDebugLevel = 0;
    }

    public SupersonicLogger(String loggerName, int debugLevel) {
        this.mLoggerName = loggerName;
        this.mDebugLevel = debugLevel;
    }

    public String getLoggerName() {
        return this.mLoggerName;
    }

    public int getDebugLevel() {
        return this.mDebugLevel;
    }

    public void setDebugLevel(int debugLevel) {
        this.mDebugLevel = debugLevel;
    }

    public boolean equals(Object other) {
        if (other == null || !(other instanceof SupersonicLogger)) {
            return false;
        }
        SupersonicLogger otherLogger = (SupersonicLogger) other;
        if (this.mLoggerName != null) {
            return this.mLoggerName.equals(otherLogger.mLoggerName);
        }
        return false;
    }
}
