package com.parse;

import android.content.Context;
import java.io.File;

class ParseCrashReporter {
    private static final String REPORT_ENDPOINT = "http://dev/null";
    private static ParseCrashReporter defaultInstance;
    private static final Object defaultInstanceLock = new Object();
    private ErrorReporter innerCrashReporter;

    static void enableCrashReporting(Context applicationContext) {
        synchronized (defaultInstanceLock) {
            if (defaultInstance == null) {
                defaultInstance = new ParseCrashReporter(applicationContext);
            } else {
                throw new RuntimeException("enableCrashReporting() called multiple times.");
            }
        }
    }

    static boolean isEnabled() {
        boolean z;
        synchronized (defaultInstanceLock) {
            z = defaultInstance != null;
        }
        return z;
    }

    static ParseCrashReporter getCurrent() {
        ParseCrashReporter parseCrashReporter;
        synchronized (defaultInstanceLock) {
            parseCrashReporter = defaultInstance;
        }
        return parseCrashReporter;
    }

    private ParseCrashReporter(final Context applicationContext) {
        final FileProvider fileProvider = new FileProvider() {
            public File getFile(String path) {
                return Parse.getParseFilesDir(path);
            }
        };
        try {
            Parse.registerParseCallbacks(new ParseCallbacks() {
                public void onParseInitialized() {
                    ParseCrashReporter.this.innerCrashReporter = ACRA.init(new BaseCrashReporter(applicationContext), ParseCrashReporter.REPORT_ENDPOINT, true, fileProvider);
                    ParseCrashReporter.this.innerCrashReporter.setReportSender(new ParseCrashReportHandler());
                }
            });
        } catch (IllegalStateException e) {
            throw new IllegalStateException("You must enable crash reporting before calling Parse.initialize(context, applicationId, clientKey");
        }
    }
}
