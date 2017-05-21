package com.parse;

import android.content.Context;
import android.util.Log;

class ACRA {
    public static final ReportField[] ALL_CRASH_REPORT_FIELDS = new ReportField[]{ReportField.REPORT_ID, ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME, ReportField.APP_INSTALL_TIME, ReportField.APP_UPGRADE_TIME, ReportField.PACKAGE_NAME, ReportField.FILE_PATH, ReportField.PHONE_MODEL, ReportField.BRAND, ReportField.PRODUCT, ReportField.ANDROID_VERSION, ReportField.OS_VERSION, ReportField.BUILD, ReportField.TOTAL_MEM_SIZE, ReportField.IS_CYANOGENMOD, ReportField.AVAILABLE_MEM_SIZE, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.CRASH_CONFIGURATION, ReportField.DISPLAY, ReportField.USER_APP_START_DATE, ReportField.USER_CRASH_DATE, ReportField.DUMPSYS_MEMINFO, ReportField.DROPBOX, ReportField.LOGCAT, ReportField.EVENTSLOG, ReportField.RADIOLOG, ReportField.DEVICE_ID, ReportField.INSTALLATION_ID, ReportField.DEVICE_FEATURES, ReportField.ENVIRONMENT, ReportField.SETTINGS_SYSTEM, ReportField.SETTINGS_SECURE, ReportField.PROCESS_NAME, ReportField.PROCESS_NAME_BY_AMS, ReportField.ACTIVITY_LOG, ReportField.JAIL_BROKEN, ReportField.PROCESS_UPTIME, ReportField.DEVICE_UPTIME, ReportField.ACRA_REPORT_FILENAME, ReportField.EXCEPTION_CAUSE, ReportField.REPORT_LOAD_THROW, ReportField.MINIDUMP, ReportField.ANDROID_ID, ReportField.UID, ReportField.UPLOADED_BY_PROCESS, ReportField.OPEN_FD_COUNT, ReportField.OPEN_FD_SOFT_LIMIT, ReportField.OPEN_FD_HARD_LIMIT, ReportField.IS_LOW_RAM_DEVICE, ReportField.SIGQUIT, ReportField.LARGE_MEM_HEAP, ReportField.ANDROID_RUNTIME};
    public static final String LOG_TAG = "CrashReporting";
    public static final ReportField[] MINIMAL_REPORT_FIELDS = new ReportField[]{ReportField.REPORT_ID, ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME, ReportField.APP_INSTALL_TIME, ReportField.APP_UPGRADE_TIME, ReportField.PACKAGE_NAME, ReportField.FILE_PATH, ReportField.PHONE_MODEL, ReportField.BRAND, ReportField.PRODUCT, ReportField.ANDROID_VERSION, ReportField.OS_VERSION, ReportField.BUILD, ReportField.TOTAL_MEM_SIZE, ReportField.IS_CYANOGENMOD, ReportField.AVAILABLE_MEM_SIZE, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.CRASH_CONFIGURATION, ReportField.DISPLAY, ReportField.USER_APP_START_DATE, ReportField.USER_CRASH_DATE, ReportField.DUMPSYS_MEMINFO, ReportField.DROPBOX, ReportField.LOGCAT, ReportField.EVENTSLOG, ReportField.RADIOLOG, ReportField.DEVICE_ID, ReportField.INSTALLATION_ID, ReportField.DEVICE_FEATURES, ReportField.ENVIRONMENT, ReportField.SETTINGS_SYSTEM, ReportField.SETTINGS_SECURE, ReportField.PROCESS_NAME, ReportField.PROCESS_NAME_BY_AMS, ReportField.ACTIVITY_LOG, ReportField.JAIL_BROKEN, ReportField.PROCESS_UPTIME, ReportField.DEVICE_UPTIME, ReportField.ACRA_REPORT_FILENAME, ReportField.EXCEPTION_CAUSE, ReportField.REPORT_LOAD_THROW, ReportField.MINIDUMP, ReportField.ANDROID_ID, ReportField.UID, ReportField.UPLOADED_BY_PROCESS, ReportField.IS_LOW_RAM_DEVICE, ReportField.LARGE_MEM_HEAP, ReportField.ANDROID_RUNTIME};
    public static final String NULL_VALUE = "CR-NULL-STRING";
    private static ReportsCrashes mReportsCrashes;

    ACRA() {
    }

    public static ErrorReporter init(ReportsCrashes reporter, String reportURL, boolean isInternalBuild, FileProvider fileProvider) {
        ErrorReporter errorReporter = ErrorReporter.getInstance();
        if (mReportsCrashes == null) {
            mReportsCrashes = reporter;
            Context applicationContext = mReportsCrashes.getApplicationContext();
            Log.d(LOG_TAG, "Crash reporting is enabled for " + applicationContext.getPackageName() + ", initializing...");
            errorReporter.init(applicationContext, isInternalBuild, fileProvider);
            Thread.setDefaultUncaughtExceptionHandler(errorReporter);
            if (reportURL != null) {
                errorReporter.setReportSender(new HttpPostSender(reportURL));
            }
            errorReporter.checkReportsOnApplicationStart();
        }
        return errorReporter;
    }

    public static ReportsCrashes getConfig() {
        return mReportsCrashes;
    }
}
