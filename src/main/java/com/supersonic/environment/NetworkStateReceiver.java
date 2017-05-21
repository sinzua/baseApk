package com.supersonic.environment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStateReceiver extends BroadcastReceiver {
    private boolean mConnected;
    private NetworkStateReceiverListener mListener;
    private ConnectivityManager mManager;

    public interface NetworkStateReceiverListener {
        void onNetworkAvailabilityChanged(boolean z);
    }

    public NetworkStateReceiver(Context context, NetworkStateReceiverListener listener) {
        this.mListener = listener;
        this.mManager = (ConnectivityManager) context.getSystemService("connectivity");
        checkAndSetState();
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getExtras() != null && checkAndSetState()) {
            notifyState();
        }
    }

    private boolean checkAndSetState() {
        boolean z;
        boolean prev = this.mConnected;
        NetworkInfo activeNetwork = this.mManager.getActiveNetworkInfo();
        if (activeNetwork == null || !activeNetwork.isConnectedOrConnecting()) {
            z = false;
        } else {
            z = true;
        }
        this.mConnected = z;
        if (prev != this.mConnected) {
            return true;
        }
        return false;
    }

    private void notifyState() {
        if (this.mListener == null) {
            return;
        }
        if (this.mConnected) {
            this.mListener.onNetworkAvailabilityChanged(true);
        } else {
            this.mListener.onNetworkAvailabilityChanged(false);
        }
    }
}
