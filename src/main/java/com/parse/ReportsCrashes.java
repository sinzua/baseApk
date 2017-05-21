package com.parse;

import android.content.Context;

interface ReportsCrashes {
    String[] additionalDropBoxTags();

    boolean checkSSLCertsOnCrashReport();

    int dropboxCollectionMinutes();

    String formPostFormat();

    Context getApplicationContext();

    boolean includeDropBoxSystemTags();

    String[] logcatArguments();

    int socketTimeout();
}
