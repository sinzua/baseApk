package com.nativex.common;

public class Log {
    private static final String TAG = "nativeX SDK";
    private static boolean showLog = false;

    public static void enableLogging(boolean enable) {
        showLog = enable;
    }

    public static boolean isLogging() {
        return showLog;
    }

    public static void v(String msg) {
        print(2, msg);
    }

    public static void v(String msg, Throwable tr) {
        print(2, msg, tr);
    }

    public static void d(String msg) {
        print(3, msg);
    }

    public static void d(String msg, Throwable tr) {
        print(3, msg, tr);
    }

    public static void i(String msg) {
        print(4, msg);
    }

    public static void i(String msg, Throwable tr) {
        print(4, msg, tr);
    }

    public static void w(String msg) {
        print(5, msg);
    }

    public static void w(String msg, Throwable tr) {
        print(5, msg, tr);
    }

    public static void e(String msg) {
        print(6, msg);
    }

    public static void e(String msg, Throwable tr) {
        print(6, msg, tr);
    }

    public static void a(String msg) {
        print(7, msg);
    }

    public static void a(String msg, Throwable tr) {
        print(7, msg, tr);
    }

    private static void print(int priority, String msg, Throwable tr) {
        if (showLog) {
            android.util.Log.println(priority, TAG, msg + '\n' + android.util.Log.getStackTraceString(tr));
        }
    }

    private static void print(int priority, String msg) {
        if (showLog) {
            android.util.Log.println(priority, TAG, msg);
        }
    }

    public static void fname() {
        fname("", 1);
    }

    public static void fname(String msg) {
        fname(msg, 1);
    }

    private static void fname(String msg, int lvl) {
        if (showLog) {
            StackTraceElement[] ste = Thread.currentThread().getStackTrace();
            String className = ste[lvl + 3].getClassName();
            d("(jbtest) --- " + className + "." + ste[lvl + 3].getMethodName() + " (" + Integer.toString(ste[lvl + 3].getLineNumber()) + ") --- " + msg);
        }
    }
}
