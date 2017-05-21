package com.supersonic.mediationsdk.logger;

import android.util.Log;
import com.supersonic.mediationsdk.logger.SupersonicLogger.SupersonicTag;
import com.supersonicads.sdk.utils.Constants.RequestParameters;

public class ConsoleLogger extends SupersonicLogger {
    public static final String NAME = "console";

    private ConsoleLogger() {
        super(NAME);
    }

    public ConsoleLogger(int debugLevel) {
        super(NAME, debugLevel);
    }

    public void log(SupersonicTag tag, String message, int logLevel) {
        switch (logLevel) {
            case 0:
                Log.v("" + tag, message);
                return;
            case 1:
                Log.i("" + tag, message);
                return;
            case 2:
                Log.w("" + tag, message);
                return;
            case 3:
                Log.e("" + tag, message);
                return;
            default:
                return;
        }
    }

    public void logException(SupersonicTag tag, String message, Throwable e) {
        log(tag, message + ":stacktrace[" + Log.getStackTraceString(e) + RequestParameters.RIGHT_BRACKETS, 3);
    }
}
