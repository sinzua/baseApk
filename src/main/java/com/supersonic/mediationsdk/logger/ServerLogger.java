package com.supersonic.mediationsdk.logger;

import android.util.Log;
import com.supersonic.mediationsdk.logger.SupersonicLogger.SupersonicTag;
import com.supersonic.mediationsdk.utils.SupersonicUtils;
import com.supersonicads.sdk.utils.Constants.ControllerParameters;
import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ServerLogger extends SupersonicLogger {
    public static final String NAME = "server";
    private final int SERVER_LOGS_SIZE_LIMIT = ControllerParameters.SECOND;
    private ArrayList<ServerLogEntry> mLogs = new ArrayList();

    private class SendingCalc {
        private int DEFAULT_DEBUG_LEVEL = 3;
        private int DEFAULT_SIZE = 1;
        private int DEFAULT_TIME = 1;

        public SendingCalc() {
            initDefaults();
        }

        private void initDefaults() {
        }

        public void notifyEvent(int event) {
            if (calc(event)) {
                ServerLogger.this.send();
            }
        }

        private boolean calc(int event) {
            if (error(event) || size() || time()) {
                return true;
            }
            return false;
        }

        private boolean time() {
            return false;
        }

        private boolean error(int event) {
            return event == 3;
        }

        private boolean size() {
            return false;
        }
    }

    public ServerLogger() {
        super(NAME);
    }

    public ServerLogger(int debugLevel) {
        super(NAME, debugLevel);
    }

    private synchronized void addLogEntry(ServerLogEntry entry) {
        this.mLogs.add(entry);
        if (shouldSendLogs()) {
            send();
        } else if (this.mLogs.size() > ControllerParameters.SECOND) {
            try {
                ArrayList<ServerLogEntry> newerLog = new ArrayList();
                for (int i = 500; i < this.mLogs.size(); i++) {
                    newerLog.add(this.mLogs.get(i));
                }
                this.mLogs = newerLog;
            } catch (Exception e) {
                this.mLogs = new ArrayList();
            }
        }
    }

    private boolean shouldSendLogs() {
        if (((ServerLogEntry) this.mLogs.get(this.mLogs.size() - 1)).getLogLevel() == 3) {
            return true;
        }
        return false;
    }

    private void send() {
        SupersonicUtils.createAndStartWorker(new LogsSender(this.mLogs), "LogsSender");
        this.mLogs = new ArrayList();
    }

    private String getTimestamp() {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()).toString();
    }

    public synchronized void log(SupersonicTag tag, String message, int logLevel) {
        addLogEntry(new ServerLogEntry(tag, getTimestamp(), message, logLevel));
    }

    public synchronized void logException(SupersonicTag tag, String message, Throwable e) {
        StringBuilder logMessage = new StringBuilder(message);
        if (e != null) {
            logMessage.append(":stacktrace[");
            logMessage.append(Log.getStackTraceString(e)).append(RequestParameters.RIGHT_BRACKETS);
        }
        addLogEntry(new ServerLogEntry(tag, getTimestamp(), logMessage.toString(), 3));
    }
}
