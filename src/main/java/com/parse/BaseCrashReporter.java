package com.parse;

import android.content.Context;
import com.google.android.gms.common.api.CommonStatusCodes;

class BaseCrashReporter implements ReportsCrashes {
    protected Context mApplicationContext;

    public BaseCrashReporter(Context applicationContext) {
        if (applicationContext == null) {
            throw new IllegalArgumentException("Application context cannot be null");
        }
        this.mApplicationContext = applicationContext;
    }

    public boolean includeDropBoxSystemTags() {
        return false;
    }

    public String[] additionalDropBoxTags() {
        return new String[0];
    }

    public int dropboxCollectionMinutes() {
        return 5;
    }

    public String[] logcatArguments() {
        return new String[]{"-t", "200", "-v", "time"};
    }

    public String formPostFormat() {
        return "application/x-www-form-urlencoded";
    }

    public int socketTimeout() {
        return CommonStatusCodes.AUTH_API_INVALID_CREDENTIALS;
    }

    public boolean checkSSLCertsOnCrashReport() {
        return true;
    }

    public Context getApplicationContext() {
        return this.mApplicationContext;
    }
}
