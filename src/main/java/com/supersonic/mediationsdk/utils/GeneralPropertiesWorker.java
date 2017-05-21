package com.supersonic.mediationsdk.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.nativex.monetization.mraid.objects.ObjectNames.CalendarEntryData;
import com.supersonic.environment.DeviceStatus;
import com.supersonic.mediationsdk.SupersonicObject;
import com.supersonic.mediationsdk.config.ConfigFile;
import com.supersonic.mediationsdk.logger.SupersonicLogger.SupersonicTag;
import com.supersonic.mediationsdk.logger.SupersonicLoggerManager;
import com.supersonic.mediationsdk.sdk.GeneralProperties;
import com.supersonic.mediationsdk.sdk.SupersonicFactory;
import com.supersonic.mediationsdk.server.ServerURL;
import com.supersonicads.sdk.utils.Constants;
import com.supersonicads.sdk.utils.Constants.RequestParameters;
import com.ty.followboom.okhttp.RequestBuilder;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

public class GeneralPropertiesWorker implements Runnable {
    public static final String SDK_VERSION = "sdkVersion";
    private final String ADVERTISING_ID = "advertisingId";
    private final String ADVERTISING_ID_IS_LIMIT_TRACKING = RequestParameters.isLAT;
    private final String ANDROID_OS_VERSION = "osVersion";
    private final String APPLICATION_KEY = ServerResponseWrapper.APP_KEY_FIELD;
    private final String BATTERY_LEVEL = "battery";
    private final String BUNDLE_ID = RequestParameters.PACKAGE_NAME;
    private final String CONNECTION_TYPE = RequestParameters.CONNECTION_TYPE;
    private final String DEVICE_MODEL = "deviceModel";
    private final String DEVICE_OEM = RequestParameters.DEVICE_OEM;
    private final String DEVICE_OS = "deviceOS";
    private final String EXTERNAL_FREE_MEMORY = "externalFreeMemory";
    private final String INTERNAL_FREE_MEMORY = "internalFreeMemory";
    private final String KEY_IS_ROOT = "jb";
    private final String KEY_PLUGIN_TYPE = ServerURL.PLUGIN_TYPE;
    private final String KEY_PLUGIN_VERSION = ServerURL.PLUGIN_VERSION;
    private final String KEY_SESSION_ID = "sessionId";
    private final String LANGUAGE = "language";
    private final String LOCATION_LAT = "lat";
    private final String LOCATION_LON = "lon";
    private final String MOBILE_CARRIER = RequestParameters.MOBILE_CARRIER;
    private final String PUBLISHER_APP_VERSION = "appVersion";
    private final String TAG = getClass().getSimpleName();
    private final String TIME_ZONE = RequestBuilder.HEADER_TIMEZONE;
    private Context mContext;

    private GeneralPropertiesWorker() {
    }

    public GeneralPropertiesWorker(Context ctx) {
        this.mContext = ctx.getApplicationContext();
    }

    public void run() {
        try {
            GeneralProperties.getProperties().putKeys(collectInformation());
            SupersonicUtils.saveGeneralProperties(this.mContext, GeneralProperties.getProperties().toJSON());
        } catch (Exception e) {
            SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, "Thread name = " + getClass().getSimpleName(), e);
        }
    }

    private Map<String, Object> collectInformation() {
        Map<String, Object> result = new HashMap();
        String str = "";
        str = generateUUID();
        if (!TextUtils.isEmpty(str)) {
            result.put("sessionId", str);
        }
        str = getBundleId();
        if (!TextUtils.isEmpty(str)) {
            result.put(RequestParameters.PACKAGE_NAME, str);
            String publAppVersion = getPublisherApplicationVersion(str);
            if (!TextUtils.isEmpty(publAppVersion)) {
                result.put("appVersion", publAppVersion);
            }
        }
        result.put(ServerResponseWrapper.APP_KEY_FIELD, getApplicationKey());
        String[] advertisingIdInfo = getAdvertisingIdInfo();
        if (advertisingIdInfo != null && advertisingIdInfo.length == 2) {
            result.put("advertisingId", advertisingIdInfo[0]);
            result.put(RequestParameters.isLAT, advertisingIdInfo[1]);
        }
        result.put("deviceOS", getDeviceOS());
        if (!TextUtils.isEmpty(getAndroidVersion())) {
            result.put("osVersion", getAndroidVersion());
        }
        str = getConnectionType();
        if (!TextUtils.isEmpty(str)) {
            result.put(RequestParameters.CONNECTION_TYPE, str);
        }
        result.put("sdkVersion", getSDKVersion());
        str = getLanguage();
        if (!TextUtils.isEmpty(str)) {
            result.put("language", str);
        }
        str = getDeviceOEM();
        if (!TextUtils.isEmpty(str)) {
            result.put(RequestParameters.DEVICE_OEM, str);
        }
        str = getDeviceModel();
        if (!TextUtils.isEmpty(str)) {
            result.put("deviceModel", str);
        }
        str = getMobileCarrier();
        if (!TextUtils.isEmpty(str)) {
            result.put(RequestParameters.MOBILE_CARRIER, str);
        }
        result.put("internalFreeMemory", Long.valueOf(getInternalStorageFreeSize()));
        result.put("externalFreeMemory", Long.valueOf(getExternalStorageFreeSize()));
        result.put("battery", Integer.valueOf(getBatteryLevel()));
        double[] lastKnownLocation = getLastKnownLocation();
        if (lastKnownLocation != null && lastKnownLocation.length == 2) {
            result.put("lat", Double.valueOf(lastKnownLocation[0]));
            result.put("lon", Double.valueOf(lastKnownLocation[1]));
        }
        str = getTimeZone();
        if (!TextUtils.isEmpty(str)) {
            result.put(RequestBuilder.HEADER_TIMEZONE, str);
        }
        str = getPluginType();
        if (!TextUtils.isEmpty(str)) {
            result.put(ServerURL.PLUGIN_TYPE, str);
        }
        str = getPluginVersion();
        if (!TextUtils.isEmpty(str)) {
            result.put(ServerURL.PLUGIN_VERSION, str);
        }
        str = String.valueOf(DeviceStatus.isRootedDevice());
        if (!TextUtils.isEmpty(str)) {
            result.put("jb", str);
        }
        return result;
    }

    private String getPublisherApplicationVersion(String packageName) {
        String result = "";
        try {
            return this.mContext.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (Exception e) {
            return result;
        }
    }

    private String getPluginType() {
        String result = "";
        try {
            result = ConfigFile.getConfigFile().getPluginType();
        } catch (Exception e) {
            SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, "getPluginType()", e);
        }
        return result;
    }

    private String getPluginVersion() {
        String result = "";
        try {
            result = ConfigFile.getConfigFile().getPluginVersion();
        } catch (Exception e) {
            SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, "getPluginVersion()", e);
        }
        return result;
    }

    private String getBundleId() {
        try {
            return this.mContext.getPackageName();
        } catch (Exception e) {
            return "";
        }
    }

    private String[] getAdvertisingIdInfo() {
        String advertisingId = "unknown";
        try {
            Class<?> mAdvertisingIdClientClass = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient");
            Object mInfoClass = mAdvertisingIdClientClass.getMethod("getAdvertisingIdInfo", new Class[]{Context.class}).invoke(mAdvertisingIdClientClass, new Object[]{this.mContext});
            Method getIdMethod = mInfoClass.getClass().getMethod("getId", new Class[0]);
            Method isLimitAdTrackingEnabledMethod = mInfoClass.getClass().getMethod(RequestParameters.isLAT, new Class[0]);
            advertisingId = getIdMethod.invoke(mInfoClass, new Object[0]).toString();
            boolean isLimitedTrackingEnabled = ((Boolean) isLimitAdTrackingEnabledMethod.invoke(mInfoClass, new Object[0])).booleanValue();
            if (!TextUtils.isEmpty(advertisingId)) {
                return new String[]{advertisingId, "" + isLimitedTrackingEnabled};
            }
        } catch (ClassNotFoundException e) {
            SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, "getAdvertisingIdInfo()", e);
        } catch (NoSuchMethodException e2) {
            SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, "getAdvertisingIdInfo()", e2);
        } catch (IllegalAccessException e3) {
            SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, "getAdvertisingIdInfo()", e3);
        } catch (IllegalArgumentException e4) {
            SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, "getAdvertisingIdInfo()", e4);
        } catch (InvocationTargetException e5) {
            SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, "getAdvertisingIdInfo()", e5);
        }
        return new String[0];
    }

    private String getApplicationKey() {
        return ((SupersonicObject) SupersonicFactory.getInstance()).getSupersonicAppKey();
    }

    private String getDeviceOS() {
        return Constants.JAVASCRIPT_INTERFACE_NAME;
    }

    private String getAndroidVersion() {
        try {
            return "" + VERSION.SDK_INT + "(" + VERSION.RELEASE + ")";
        } catch (Exception e) {
            return "";
        }
    }

    private String getConnectionType() {
        String type = "";
        try {
            NetworkInfo activeNetwork = ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetwork == null || !activeNetwork.isConnected()) {
                return type;
            }
            String typeName = activeNetwork.getTypeName();
            int typeId = activeNetwork.getType();
            if (typeId == 0) {
                return "" + ((TelephonyManager) this.mContext.getSystemService("phone")).getNetworkType();
            } else if (typeId == 1) {
                return RequestParameters.NETWORK_TYPE_WIFI;
            } else {
                return typeName;
            }
        } catch (Exception e) {
            SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, this.TAG + ":getConnectionType()", e);
            return type;
        }
    }

    private String getSDKVersion() {
        return SupersonicUtils.getSDKVersion();
    }

    private String getLanguage() {
        try {
            return Locale.getDefault().getLanguage().toString();
        } catch (Exception e) {
            return "";
        }
    }

    private String getDeviceOEM() {
        try {
            return Build.MANUFACTURER;
        } catch (Exception e) {
            return "";
        }
    }

    private String getDeviceModel() {
        try {
            return Build.MODEL;
        } catch (Exception e) {
            return "";
        }
    }

    private String getMobileCarrier() {
        String ret = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
            if (telephonyManager == null) {
                return ret;
            }
            String operatorName = telephonyManager.getNetworkOperatorName();
            if (operatorName.equals("")) {
                return ret;
            }
            return operatorName;
        } catch (Exception e) {
            SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, this.TAG + ":getMobileCarrier()", e);
            return ret;
        }
    }

    private boolean isExternalStorageAbvailable() {
        try {
            return Environment.getExternalStorageState().equals("mounted");
        } catch (Exception e) {
            return false;
        }
    }

    private long getInternalStorageFreeSize() {
        try {
            StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
            return (((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize())) / ParseFileUtils.ONE_MB;
        } catch (Exception e) {
            return -1;
        }
    }

    private long getExternalStorageFreeSize() {
        if (!isExternalStorageAbvailable()) {
            return -1;
        }
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return (((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize())) / ParseFileUtils.ONE_MB;
    }

    private int getBatteryLevel() {
        try {
            Intent batteryIntent = this.mContext.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            int level = batteryIntent.getIntExtra("level", -1);
            int scale = batteryIntent.getIntExtra("scale", -1);
            if (level == -1 || scale == -1) {
                return -1;
            }
            return (int) ((((float) level) / ((float) scale)) * 100.0f);
        } catch (Exception e) {
            SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, this.TAG + ":getBatteryLevel()", e);
            return -1;
        }
    }

    private double[] getLastKnownLocation() {
        double[] result = new double[0];
        long bestLocationTime = Long.MIN_VALUE;
        try {
            if (!locationPermissionGranted()) {
                return result;
            }
            LocationManager locationManager = (LocationManager) this.mContext.getApplicationContext().getSystemService(CalendarEntryData.LOCATION);
            Location bestLocation = null;
            for (String provider : locationManager.getAllProviders()) {
                Location location = locationManager.getLastKnownLocation(provider);
                if (location != null && location.getTime() > bestLocationTime) {
                    bestLocation = location;
                    bestLocationTime = bestLocation.getTime();
                }
            }
            if (bestLocation == null) {
                return result;
            }
            double lat = bestLocation.getLatitude();
            double lon = bestLocation.getLongitude();
            return new double[]{lat, lon};
        } catch (Exception e) {
            SupersonicLoggerManager.getLogger().logException(SupersonicTag.NATIVE, this.TAG + ":getLastLocation()", e);
            return new double[0];
        }
    }

    private boolean locationPermissionGranted() {
        try {
            if (this.mContext.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private String getTimeZone() {
        String result = "";
        try {
            TimeZone tz = TimeZone.getDefault();
            return "GMT" + ((tz.getOffset(GregorianCalendar.getInstance(tz).getTimeInMillis()) >= 0 ? "+" : "-") + String.format("%02d:%02d", new Object[]{Integer.valueOf(Math.abs(tz.getOffset(GregorianCalendar.getInstance(tz).getTimeInMillis()) / 3600000)), Integer.valueOf(Math.abs((tz.getOffset(GregorianCalendar.getInstance(tz).getTimeInMillis()) / 60000) % 60))}));
        } catch (Exception e) {
            return "";
        }
    }

    private String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "") + SupersonicUtils.getTimeStamp();
    }
}
