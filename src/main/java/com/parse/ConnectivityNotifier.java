package com.parse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ReceiverCallNotAllowedException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class ConnectivityNotifier extends BroadcastReceiver {
    private static final String TAG = "com.parse.ConnectivityNotifier";
    private static final ConnectivityNotifier singleton = new ConnectivityNotifier();
    private boolean hasRegisteredReceiver = false;
    private Set<ConnectivityListener> listeners = new HashSet();
    private final Object lock = new Object();

    public interface ConnectivityListener {
        void networkConnectivityStatusChanged(Context context, Intent intent);
    }

    ConnectivityNotifier() {
    }

    public static ConnectivityNotifier getNotifier(Context context) {
        singleton.tryToRegisterForNetworkStatusNotifications(context);
        return singleton;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo network = connectivityManager.getActiveNetworkInfo();
        if (network == null || !network.isConnected()) {
            return false;
        }
        return true;
    }

    public void addListener(ConnectivityListener delegate) {
        synchronized (this.lock) {
            this.listeners.add(delegate);
        }
    }

    public void removeListener(ConnectivityListener delegate) {
        synchronized (this.lock) {
            this.listeners.remove(delegate);
        }
    }

    private boolean tryToRegisterForNetworkStatusNotifications(Context context) {
        synchronized (this.lock) {
            if (this.hasRegisteredReceiver) {
                return true;
            } else if (context == null) {
                return false;
            } else {
                try {
                    context.getApplicationContext().registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
                    this.hasRegisteredReceiver = true;
                    return true;
                } catch (ReceiverCallNotAllowedException e) {
                    PLog.v(TAG, "Cannot register a broadcast receiver because the executing thread is currently in a broadcast receiver. Will try again later.");
                    return false;
                }
            }
        }
    }

    public void onReceive(Context context, Intent intent) {
        synchronized (this.lock) {
            List<ConnectivityListener> listenersCopy = new ArrayList(this.listeners);
        }
        for (ConnectivityListener delegate : listenersCopy) {
            delegate.networkConnectivityStatusChanged(context, intent);
        }
    }
}
