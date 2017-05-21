package com.nativex.monetization.mraid;

import android.app.Activity;
import android.content.Context;
import android.os.Build.VERSION;
import com.nativex.monetization.mraid.MRAIDUtils.Features;

class MRAIDDeviceManager {
    private MRAIDDeviceManager() {
    }

    public static Boolean checkFeatureSupport(Activity context, Features feature) {
        switch (feature) {
            case CALENDAR:
                return checkSupportForCalendar(context);
            case INLINE_VIDEO:
                return checkSupportForInlineVideo(context);
            case SMS:
                return checkSupportForSMS(context);
            case STORE_PICTURE:
                return checkSupportForStorePicture(context);
            case TEL:
                return checkSupportForTel(context);
            default:
                return Boolean.valueOf(false);
        }
    }

    private static boolean checkPermission(Context context, String permission) {
        return context.checkCallingOrSelfPermission(permission) == 0;
    }

    private static Boolean checkSupportForInlineVideo(Activity context) {
        if (VERSION.SDK_INT >= 11) {
            try {
                if ((context.getPackageManager().getActivityInfo(context.getComponentName(), 0).flags & 512) == 512) {
                    if (MRAIDWebView.forceHardware == null || MRAIDWebView.forceHardware.booleanValue()) {
                        return Boolean.valueOf(true);
                    }
                    return Boolean.valueOf(false);
                }
            } catch (Exception e) {
                return null;
            }
        }
        return Boolean.valueOf(false);
    }

    private static Boolean checkSupportForSMS(Activity context) {
        try {
            if (checkPermission(context, "android.permission.SEND_SMS") && context.getPackageManager().hasSystemFeature("android.hardware.telephony")) {
                return Boolean.valueOf(true);
            }
            return Boolean.valueOf(false);
        } catch (Exception e) {
            return null;
        }
    }

    private static Boolean checkSupportForCalendar(Activity context) {
        try {
            if (VERSION.SDK_INT < 8) {
                return Boolean.valueOf(false);
            }
            if (checkPermission(context, "android.permission.READ_CALENDAR")) {
                return Boolean.valueOf(checkPermission(context, "android.permission.WRITE_CALENDAR"));
            }
            return Boolean.valueOf(false);
        } catch (Exception e) {
            return null;
        }
    }

    private static Boolean checkSupportForStorePicture(Activity context) {
        try {
            if (VERSION.SDK_INT < 8) {
                return Boolean.valueOf(false);
            }
            return Boolean.valueOf(checkPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE"));
        } catch (Exception e) {
            return null;
        }
    }

    private static Boolean checkSupportForTel(Activity context) {
        try {
            if (checkPermission(context, "android.permission.CALL_PHONE") && context.getPackageManager().hasSystemFeature("android.hardware.telephony")) {
                return Boolean.valueOf(true);
            }
            return Boolean.valueOf(false);
        } catch (Exception e) {
            return null;
        }
    }

    public static int checkSupportLevel(Activity activity, Features features) {
        if (features == Features.CALENDAR) {
            return checkCalendarSupportLevel(activity);
        }
        return 0;
    }

    private static int checkCalendarSupportLevel(Activity activity) {
        if (MRAIDCalendarUtils.deviceSupportsCalendarProvider(activity)) {
            return 1;
        }
        return 0;
    }
}
