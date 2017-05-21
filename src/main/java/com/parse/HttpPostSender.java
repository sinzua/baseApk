package com.parse;

import android.net.Uri;
import android.util.Log;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

class HttpPostSender implements ReportSender {
    private Uri mFormUri = null;

    public HttpPostSender(String formUri) {
        this.mFormUri = Uri.parse(formUri);
    }

    public void send(CrashReportData report) throws ReportSenderException {
        try {
            Map<String, String> finalReport = remap(report);
            URL reportUrl = new URL(this.mFormUri.toString());
            Log.d(ACRA.LOG_TAG, "Connect to " + reportUrl.toString());
            HttpUtils.doPost(finalReport, reportUrl, ACRA.getConfig().formPostFormat());
        } catch (Exception e) {
            throw new ReportSenderException("Error while sending report to Http Post Form.", e);
        }
    }

    private Map<String, String> remap(Map<ReportField, String> report) {
        Map<String, String> finalReport = new HashMap(report.size());
        for (ReportField field : ACRA.ALL_CRASH_REPORT_FIELDS) {
            finalReport.put(field.toString(), report.get(field));
        }
        return finalReport;
    }
}
