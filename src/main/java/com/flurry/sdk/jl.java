package com.flurry.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class jl extends BroadcastReceiver {
    private static jl c;
    boolean a;
    boolean b;
    private boolean d = false;

    public enum a {
        NONE_OR_UNKNOWN(0),
        NETWORK_AVAILABLE(1),
        WIFI(2),
        CELL(3);
        
        private int e;

        private a(int i) {
            this.e = i;
        }

        public int a() {
            return this.e;
        }
    }

    public static synchronized jl a() {
        jl jlVar;
        synchronized (jl.class) {
            if (c == null) {
                c = new jl();
            }
            jlVar = c;
        }
        return jlVar;
    }

    public static synchronized void b() {
        synchronized (jl.class) {
            if (c != null) {
                c.f();
            }
            c = null;
        }
    }

    private jl() {
        boolean z = false;
        Context c = js.a().c();
        if (c.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == 0) {
            z = true;
        }
        this.d = z;
        this.b = a(c);
        if (this.d) {
            e();
        }
    }

    public boolean c() {
        return this.b;
    }

    private synchronized void e() {
        if (!this.a) {
            Context c = js.a().c();
            this.b = a(c);
            c.registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            this.a = true;
        }
    }

    private synchronized void f() {
        if (this.a) {
            js.a().c().unregisterReceiver(this);
            this.a = false;
        }
    }

    public void onReceive(Context context, Intent intent) {
        boolean a = a(context);
        if (this.b != a) {
            this.b = a;
            jk jkVar = new jk();
            jkVar.a = a;
            jkVar.b = d();
            jkVar.b();
        }
    }

    private boolean a(Context context) {
        if (!this.d || context == null) {
            return true;
        }
        NetworkInfo activeNetworkInfo = g().getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            return false;
        }
        return true;
    }

    public a d() {
        if (!this.d) {
            return a.NONE_OR_UNKNOWN;
        }
        NetworkInfo activeNetworkInfo = g().getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            return a.NONE_OR_UNKNOWN;
        }
        switch (activeNetworkInfo.getType()) {
            case 0:
            case 2:
            case 3:
            case 4:
            case 5:
                return a.CELL;
            case 1:
                return a.WIFI;
            case 8:
                return a.NONE_OR_UNKNOWN;
            default:
                if (activeNetworkInfo.isConnected()) {
                    return a.NETWORK_AVAILABLE;
                }
                return a.NONE_OR_UNKNOWN;
        }
    }

    private ConnectivityManager g() {
        return (ConnectivityManager) js.a().c().getSystemService("connectivity");
    }
}
