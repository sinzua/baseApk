package com.flurry.sdk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.flurry.sdk.ll.a;

public class jo implements a {
    private static jo a;
    private static final String b = jo.class.getSimpleName();
    private String c;
    private String d;

    public static synchronized jo a() {
        jo joVar;
        synchronized (jo.class) {
            if (a == null) {
                a = new jo();
            }
            joVar = a;
        }
        return joVar;
    }

    public static void b() {
        if (a != null) {
            lk.a().b("VersionName", a);
        }
        a = null;
    }

    private jo() {
        ll a = lk.a();
        this.c = (String) a.a("VersionName");
        a.a("VersionName", (a) this);
        kg.a(4, b, "initSettings, VersionName = " + this.c);
    }

    public String c() {
        return VERSION.RELEASE;
    }

    public String d() {
        return Build.DEVICE;
    }

    public synchronized String e() {
        String str;
        if (!TextUtils.isEmpty(this.c)) {
            str = this.c;
        } else if (TextUtils.isEmpty(this.d)) {
            this.d = f();
            str = this.d;
        } else {
            str = this.d;
        }
        return str;
    }

    private String f() {
        try {
            Context c = js.a().c();
            PackageInfo packageInfo = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
            if (packageInfo.versionName != null) {
                return packageInfo.versionName;
            }
            if (packageInfo.versionCode != 0) {
                return Integer.toString(packageInfo.versionCode);
            }
            return "Unknown";
        } catch (Throwable th) {
            kg.a(6, b, "", th);
        }
    }

    public void a(String str, Object obj) {
        if (str.equals("VersionName")) {
            this.c = (String) obj;
            kg.a(4, b, "onSettingUpdate, VersionName = " + this.c);
            return;
        }
        kg.a(6, b, "onSettingUpdate internal error!");
    }
}
