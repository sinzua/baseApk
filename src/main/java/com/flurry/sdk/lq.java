package com.flurry.sdk;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

public final class lq {
    private static final String a = lq.class.getSimpleName();

    public static PackageInfo a(Context context) {
        PackageInfo packageInfo = null;
        if (context != null) {
            try {
                packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 20815);
            } catch (NameNotFoundException e) {
                kg.a(a, "Cannot find package info for package: " + context.getPackageName());
            }
        }
        return packageInfo;
    }

    public static ApplicationInfo b(Context context) {
        ApplicationInfo applicationInfo = null;
        if (context != null) {
            try {
                applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            } catch (NameNotFoundException e) {
                kg.a(a, "Cannot find application info for package: " + context.getPackageName());
            }
        }
        return applicationInfo;
    }

    public static String c(Context context) {
        PackageInfo a = a(context);
        if (a == null || a.packageName == null) {
            return "";
        }
        return a.packageName;
    }

    public static String d(Context context) {
        PackageInfo a = a(context);
        if (a == null || a.versionName == null) {
            return "";
        }
        return a.versionName;
    }

    public static Bundle e(Context context) {
        ApplicationInfo b = b(context);
        return (b == null || b.metaData == null) ? Bundle.EMPTY : b.metaData;
    }
}
