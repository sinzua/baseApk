package com.supersonic.environment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.TimeZone;

public class DeviceStatus {
    private static final String DEVICE_OS = "android";
    private static final String GOOGLE_PLAY_SERVICES_CLASS_NAME = "com.google.android.gms.ads.identifier.AdvertisingIdClient";
    private static final String GOOGLE_PLAY_SERVICES_GET_AID_INFO_METHOD_NAME = "getAdvertisingIdInfo";
    private static final String GOOGLE_PLAY_SERVICES_GET_AID_METHOD_NAME = "getId";
    private static final String GOOGLE_PLAY_SERVICES_IS_LIMITED_AD_TRACKING_METHOD_NAME = "isLimitAdTrackingEnabled";

    public static long getDeviceLocalTime() {
        return Calendar.getInstance(TimeZone.getDefault()).getTime().getTime();
    }

    public static int getDeviceTimeZoneOffsetInMinutes() {
        return -(TimeZone.getDefault().getRawOffset() / 60000);
    }

    public static String[] getAdvertisingIdInfo(Context c) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> mAdvertisingIdClientClass = Class.forName(GOOGLE_PLAY_SERVICES_CLASS_NAME);
        Object mInfoClass = mAdvertisingIdClientClass.getMethod(GOOGLE_PLAY_SERVICES_GET_AID_INFO_METHOD_NAME, new Class[]{Context.class}).invoke(mAdvertisingIdClientClass, new Object[]{c});
        Method getIdMethod = mInfoClass.getClass().getMethod(GOOGLE_PLAY_SERVICES_GET_AID_METHOD_NAME, new Class[0]);
        Method isLimitAdTrackingEnabledMethod = mInfoClass.getClass().getMethod("isLimitAdTrackingEnabled", new Class[0]);
        String advertisingId = getIdMethod.invoke(mInfoClass, new Object[0]).toString();
        boolean isLimitedTrackingEnabled = ((Boolean) isLimitAdTrackingEnabledMethod.invoke(mInfoClass, new Object[0])).booleanValue();
        return new String[]{advertisingId, "" + isLimitedTrackingEnabled};
    }

    public static String getDeviceLanguage(Context c) throws Exception {
        return c.getResources().getConfiguration().locale.getLanguage();
    }

    public static long getAvailableInternalMemorySizeInMegaBytes() {
        return getFreeStorageInBytes(Environment.getDataDirectory());
    }

    public static long getAvailableExternalMemorySizeInMegaBytes() {
        if (isExternalMemoryAvailable()) {
            return getFreeStorageInBytes(Environment.getExternalStorageDirectory());
        }
        return 0;
    }

    private static long getFreeStorageInBytes(File f) {
        long res;
        StatFs stat = new StatFs(f.getPath());
        if (VERSION.SDK_INT < 19) {
            res = ((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize());
        } else {
            res = stat.getAvailableBlocksLong() * stat.getBlockSizeLong();
        }
        return res / ParseFileUtils.ONE_MB;
    }

    public static boolean isExternalMemoryAvailable() {
        return "mounted".equals(Environment.getExternalStorageState()) && Environment.isExternalStorageRemovable();
    }

    public static String getMobileCarrier(Context c) {
        return ((TelephonyManager) c.getSystemService("phone")).getNetworkOperatorName();
    }

    public static String getConnectionType(Context c) throws Exception {
        String typeName = "";
        NetworkInfo activeNetwork = ((ConnectivityManager) c.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetwork == null || !activeNetwork.isConnected()) {
            return typeName;
        }
        return activeNetwork.getTypeName();
    }

    public static String getAndroidOsVersion() {
        return VERSION.RELEASE;
    }

    public static int getAndroidAPIVersion() {
        return VERSION.SDK_INT;
    }

    public static String getDeviceModel() {
        return Build.MODEL;
    }

    public static String getDeviceOEM() {
        return Build.MANUFACTURER;
    }

    public static String getDeviceOs() {
        return DEVICE_OS;
    }

    public static boolean isRootedDevice() {
        return findBinary("su");
    }

    private static boolean findBinary(String binaryName) {
        try {
            for (String path : new String[]{"/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/", "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"}) {
                if (new File(path + binaryName).exists()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static float getDeviceDPI(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }
}
