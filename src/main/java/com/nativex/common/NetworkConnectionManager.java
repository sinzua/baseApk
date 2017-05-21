package com.nativex.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.nativex.monetization.manager.MonetizationSharedDataManager;

public class NetworkConnectionManager {
    private static Context context;
    private static NetworkConnectionManager instance;

    private NetworkConnectionManager(Context context) {
        try {
            context = context.getApplicationContext();
        } catch (Exception e) {
            Log.e("Failed to initialize NetworkConnectionManager", e);
        }
    }

    public static NetworkConnectionManager getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkConnectionManager(context);
        }
        return instance;
    }

    public static NetworkConnectionManager getInstance() {
        if (instance != null) {
            return instance;
        }
        if (context != null) {
            return getInstance(context);
        }
        return getInstance(MonetizationSharedDataManager.getContext());
    }

    public boolean isOnWiFi() {
        NetworkConnectionManager ncm = getInstance(MonetizationSharedDataManager.getContext());
        if (!ncm.isConnected() || ncm.isConnectedToCellular()) {
            return false;
        }
        return true;
    }

    boolean isConnectedToCellular() {
        try {
            if (((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo().getType() != 1) {
                return true;
            }
            return false;
        } catch (Exception e) {
            Log.e("Exception caught in DeviceData in isCellular Module:" + e, e);
            return false;
        }
    }

    public boolean isConnectedFast() {
        try {
            NetworkInfo info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (info != null && info.isConnected() && isConnectionFast(info.getType(), info.getSubtype())) {
                return true;
            }
            return false;
        } catch (Exception e) {
            Log.e("Exception caught in NetworkCommunicationManager.isConnectedFast() ");
            return false;
        }
    }

    private boolean isConnectionFast(int type, int subType) {
        if (type == 1) {
            return true;
        }
        if (type != 0) {
            return false;
        }
        switch (subType) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
                return false;
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 13:
            case 14:
            case 15:
                return true;
            default:
                return false;
        }
    }

    public boolean isConnected() {
        try {
            ConnectivityManager connectionMgr = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectionMgr == null) {
                Log.i("Connection manager is not available; no internet connection exists.");
                return false;
            } else if (connectionMgr.getActiveNetworkInfo() == null) {
                Log.i("Connection manager has no active network; no internet connection exists");
                return false;
            } else if (connectionMgr.getActiveNetworkInfo().isConnected()) {
                return true;
            } else {
                Log.i("Connection active network is not connected; no internet connection exists");
                return false;
            }
        } catch (Exception e) {
            Log.e("Exception caught in NetworkCommunicationManager.isConnected() ", e);
            Log.i("The connection manager is not currently available.  Unable to establish a network connection.");
            return false;
        }
    }

    public boolean checkWifiAccessPermissions() {
        if (context.checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE") == 0) {
            return true;
        }
        Log.d("Permission ACCESS_WIFI_STATE is unavailable.");
        Log.d("Unable to access MAC Address.");
        return false;
    }

    public static void release() {
        instance = null;
        context = null;
    }
}
