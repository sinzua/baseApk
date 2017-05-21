package com.nativex.common;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import com.nativex.monetization.manager.MonetizationSharedDataManager;

public class DeviceManager {
    private static Device device;
    private static String deviceId = null;
    private static DeviceManager deviceManager;
    private Context context;

    private DeviceManager(Context context) {
        this.context = context;
        deviceManager = this;
    }

    public static synchronized Device getDeviceInstance(Context context) {
        Device device;
        synchronized (DeviceManager.class) {
            if (context == null) {
                context = MonetizationSharedDataManager.getContext();
            }
            if (deviceManager == null) {
                deviceManager = new DeviceManager(context);
            } else if (deviceManager.context == null) {
                deviceManager.context = context;
            }
            if (device == null) {
                device = new Device();
                device.setAndroidDeviceID(deviceManager.getAndroidDeviceId());
                device.setAndroidID(deviceManager.getAndroidId());
                device.setHacked(deviceManager.isHacked());
                device.setDeviceId(deviceManager.getDeviceId());
                device.setDeviceName(Build.MODEL);
                device.setOsVersion(deviceManager.getOsVersion());
                device.setUsingSdk(true);
            } else if (device.getAndroidDeviceID() == null) {
                device.setAndroidDeviceID(deviceManager.getAndroidDeviceId());
            }
            device = device;
        }
        return device;
    }

    private String getOsVersion() {
        String version = verifyVersionString(VERSION.RELEASE);
        if (!Utilities.stringIsEmpty(version)) {
            return version;
        }
        version = verifyVersionString(VERSION.SDK);
        if (Utilities.stringIsEmpty(version)) {
            return VERSION.RELEASE;
        }
        return version;
    }

    private String verifyVersionString(String version) {
        String regEx = "^\\d[\\d+\\.?]+\\d$";
        if (!version.matches(regEx)) {
            version = version.replaceAll("[^0-9\\.]", "");
            if (!version.matches(regEx)) {
                return null;
            }
        }
        return version;
    }

    public static void updateDeviceId() {
        if (device != null && deviceManager != null) {
            deviceId = null;
            device.setDeviceId(deviceManager.getDeviceId());
        }
    }

    public static void resetAndroidId() {
        if (device != null && deviceManager != null) {
            deviceId = null;
            device.setAndroidID(deviceManager.getAndroidId());
        }
    }

    public static void resetAndroidDeviceId() {
        if (device != null && deviceManager != null) {
            deviceId = null;
            device.setAndroidDeviceID(deviceManager.getAndroidDeviceId());
        }
    }

    private boolean isHacked() {
        String buildTags = Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private String getAndroidDeviceId() {
        String deviceId = null;
        try {
            deviceId = ((TelephonyManager) this.context.getSystemService("phone")).getDeviceId();
        } catch (Exception e) {
            Log.d("No permissions to get device id, continuing... " + e.getMessage());
        }
        if (deviceId != null) {
            Log.d("Found an AndroidDeviceId (IMEI): " + deviceId);
        } else {
            Log.d("Could not retrieve an AndroidDeviceId (IMEI)");
        }
        return deviceId;
    }

    private String getAndroidId() {
        try {
            return Secure.getString(this.context.getContentResolver(), "android_id");
        } catch (Exception e) {
            Log.d("DeviceManager: Unexpected exception caught in getAndroidId()");
            e.printStackTrace();
            return null;
        }
    }

    private String getDeviceId() {
        try {
            if (deviceId == null) {
                deviceId = SharedPreferenceManager.getDeviceId();
            }
            return deviceId;
        } catch (Exception e) {
            Log.d("DeviceManager: Unexpected exception caught in getDeviceId()");
            e.printStackTrace();
            return null;
        }
    }

    public static void release() {
        device = null;
        deviceManager = null;
        deviceId = null;
    }
}
