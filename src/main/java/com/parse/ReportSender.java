package com.parse;

interface ReportSender {
    void send(CrashReportData crashReportData) throws ReportSenderException;
}
