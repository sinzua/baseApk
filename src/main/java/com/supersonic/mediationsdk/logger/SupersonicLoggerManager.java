package com.supersonic.mediationsdk.logger;

import com.supersonic.mediationsdk.logger.SupersonicLogger.SupersonicTag;
import java.util.ArrayList;
import java.util.Iterator;

public class SupersonicLoggerManager extends SupersonicLogger implements LogListener {
    private static SupersonicLoggerManager mInstance;
    private final String TAG = getClass().getName();
    private ArrayList<SupersonicLogger> mLoggers = new ArrayList();

    protected SupersonicLoggerManager(String loggerName) {
        super(loggerName);
        initSubLoggers();
    }

    protected SupersonicLoggerManager(String loggerName, int debugLevel) {
        super(loggerName, debugLevel);
        initSubLoggers();
    }

    private void initSubLoggers() {
        this.mLoggers.add(new ConsoleLogger(1));
        this.mLoggers.add(new ServerLogger(0));
    }

    public static synchronized SupersonicLoggerManager getLogger() {
        SupersonicLoggerManager supersonicLoggerManager;
        synchronized (SupersonicLoggerManager.class) {
            if (mInstance == null) {
                mInstance = new SupersonicLoggerManager(SupersonicLoggerManager.class.getSimpleName());
            }
            supersonicLoggerManager = mInstance;
        }
        return supersonicLoggerManager;
    }

    public static synchronized SupersonicLoggerManager getLogger(int debugLevel) {
        SupersonicLoggerManager supersonicLoggerManager;
        synchronized (SupersonicLoggerManager.class) {
            if (mInstance == null) {
                mInstance = new SupersonicLoggerManager(SupersonicLoggerManager.class.getSimpleName());
            } else {
                mInstance.mDebugLevel = debugLevel;
            }
            supersonicLoggerManager = mInstance;
        }
        return supersonicLoggerManager;
    }

    public void addLogger(SupersonicLogger toAdd) {
        this.mLoggers.add(toAdd);
    }

    public synchronized void log(SupersonicTag tag, String message, int logLevel) {
        if (logLevel >= this.mDebugLevel) {
            Iterator i$ = this.mLoggers.iterator();
            while (i$.hasNext()) {
                SupersonicLogger logger = (SupersonicLogger) i$.next();
                if (logger.getDebugLevel() <= logLevel) {
                    logger.log(tag, message, logLevel);
                }
            }
        }
    }

    public synchronized void onLog(SupersonicTag tag, String message, int logLevel) {
        log(tag, message, logLevel);
    }

    public synchronized void logException(SupersonicTag tag, String message, Throwable e) {
        Iterator i$;
        if (e == null) {
            i$ = this.mLoggers.iterator();
            while (i$.hasNext()) {
                ((SupersonicLogger) i$.next()).log(tag, message, 3);
            }
        } else {
            i$ = this.mLoggers.iterator();
            while (i$.hasNext()) {
                ((SupersonicLogger) i$.next()).logException(tag, message, e);
            }
        }
    }

    private SupersonicLogger findLoggerByName(String loggerName) {
        Iterator i$ = this.mLoggers.iterator();
        while (i$.hasNext()) {
            SupersonicLogger logger = (SupersonicLogger) i$.next();
            if (logger.getLoggerName().equals(loggerName)) {
                return logger;
            }
        }
        return null;
    }

    public void setLoggerDebugLevel(String loggerName, int debugLevel) {
        if (loggerName != null) {
            SupersonicLogger logger = findLoggerByName(loggerName);
            if (logger == null) {
                log(SupersonicTag.NATIVE, "Failed to find logger:setLoggerDebugLevel(loggerName:" + loggerName + " ,debugLevel:" + debugLevel + ")", 0);
            } else if (debugLevel < 0 || debugLevel > 3) {
                this.mLoggers.remove(logger);
            } else {
                log(SupersonicTag.NATIVE, "setLoggerDebugLevel(loggerName:" + loggerName + " ,debugLevel:" + debugLevel + ")", 0);
                logger.setDebugLevel(debugLevel);
            }
        }
    }
}
