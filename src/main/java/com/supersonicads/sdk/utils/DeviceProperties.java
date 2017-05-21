package com.supersonicads.sdk.utils;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import java.util.Map;
import java.util.TreeMap;

public class DeviceProperties {
    private static DeviceProperties mInstance = null;
    private String mDeviceCarrier;
    private Map<String, String> mDeviceIds;
    private String mDeviceModel;
    private String mDeviceOem;
    private String mDeviceOsType;
    private int mDeviceOsVersion;
    private final String mSupersonicSdkVersion = Constants.SDK_VERSION;

    private DeviceProperties(Context context) {
        init(context);
    }

    public static DeviceProperties getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DeviceProperties(context);
        }
        return mInstance;
    }

    private void init(Context context) {
        this.mDeviceOem = Build.MANUFACTURER;
        this.mDeviceModel = Build.MODEL;
        this.mDeviceOsType = "android";
        this.mDeviceOsVersion = VERSION.SDK_INT;
        this.mDeviceIds = new TreeMap();
        this.mDeviceCarrier = ((TelephonyManager) context.getSystemService("phone")).getNetworkOperatorName();
    }

    public String getDeviceOem() {
        return this.mDeviceOem;
    }

    public String getDeviceModel() {
        return this.mDeviceModel;
    }

    public String getDeviceOsType() {
        return this.mDeviceOsType;
    }

    public int getDeviceOsVersion() {
        return this.mDeviceOsVersion;
    }

    public Map<String, String> getDeviceIds() {
        return this.mDeviceIds;
    }

    public String getDeviceCarrier() {
        return this.mDeviceCarrier;
    }

    public String getSupersonicSdkVersion() {
        return Constants.SDK_VERSION;
    }

    public static void release() {
        mInstance = null;
    }
}
