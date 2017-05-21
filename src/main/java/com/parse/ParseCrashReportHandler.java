package com.parse;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

class ParseCrashReportHandler implements ReportSender {
    private static final ReportField[] CRASH_REPORT_FIELDS = new ReportField[]{ReportField.REPORT_ID, ReportField.APP_INSTALL_TIME, ReportField.APP_UPGRADE_TIME, ReportField.AVAILABLE_MEM_SIZE, ReportField.BRAND, ReportField.BUILD, ReportField.CRASH_CONFIGURATION, ReportField.DEVICE_FEATURES, ReportField.DEVICE_UPTIME, ReportField.DUMPSYS_MEMINFO, ReportField.EXCEPTION_CAUSE, ReportField.IS_LOW_RAM_DEVICE, ReportField.IS_SILENT, ReportField.OPEN_FD_COUNT, ReportField.OPEN_FD_HARD_LIMIT, ReportField.OPEN_FD_SOFT_LIMIT, ReportField.PACKAGE_NAME, ReportField.PHONE_MODEL, ReportField.PROCESS_NAME, ReportField.PROCESS_UPTIME, ReportField.PRODUCT, ReportField.SIGQUIT, ReportField.STACK_TRACE, ReportField.TOTAL_MEM_SIZE, ReportField.USER_APP_START_DATE, ReportField.USER_CRASH_DATE};

    ParseCrashReportHandler() {
    }

    public void send(CrashReportData crashReportData) throws ReportSenderException {
        try {
            JSONObject payload = getCrashReportEventPayload(crashReportData);
            Log.d(ACRA.LOG_TAG, "Sending crash report to Parse...");
            ParseCrashReporting.trackCrashReport(payload);
        } catch (JSONException e) {
            throw new ReportSenderException("Failed to convert crash report into event payload", e);
        }
    }

    private JSONObject getCrashReportEventPayload(CrashReportData crashReportData) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        for (ReportField field : CRASH_REPORT_FIELDS) {
            jsonObject.put(field.toString(), crashReportData.get(field));
        }
        return jsonObject;
    }
}
