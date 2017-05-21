package com.nativex.monetization.mraid;

import com.nativex.common.Log;
import com.supersonicads.sdk.utils.Constants.RequestParameters;

public class MRAIDLogger {
    private static final String PREFIX = "RichMedia";

    public static void i(MRAIDContainer container, String msg) {
        if (container == null) {
            i(msg);
        } else {
            Log.i("RichMedia: [" + container.getContainerName() + "] " + msg);
        }
    }

    public static void i(String msg) {
        Log.i("RichMedia: " + msg);
    }

    public static void d(String msg) {
        Log.d("RichMedia: " + msg);
    }

    public static void d(MRAIDContainer container, String msg) {
        if (container != null) {
            Log.d("RichMedia: [" + container.getContainerName() + "] " + msg);
        } else {
            Log.d(msg);
        }
    }

    public static void e(String msg, Throwable e) {
        if (e == null) {
            Log.e("RichMedia " + msg);
        } else {
            Log.e("RichMedia: " + msg, e);
        }
    }

    public static void e(MRAIDContainer container, String msg, Throwable e) {
        e(RequestParameters.LEFT_BRACKETS + container.getContainerName() + "] " + msg, e);
    }

    public static void e(String msg) {
        e(msg, null);
    }
}
