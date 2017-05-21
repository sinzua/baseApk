package com.supersonic.environment;

import android.app.Activity;
import android.content.Context;
import java.io.File;

public class ApplicationContext {
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    public static int getAppOrientation(Activity a) {
        return a.getRequestedOrientation();
    }

    public static String getDiskCacheDirPath(Context context) {
        File internalFile = context.getCacheDir();
        if (internalFile != null) {
            return internalFile.getPath();
        }
        return null;
    }
}
