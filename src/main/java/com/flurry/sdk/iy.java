package com.flurry.sdk;

import android.widget.Toast;
import com.flurry.sdk.ll.a;
import java.util.Arrays;

public class iy extends kt implements a {
    private static final String e = iy.class.getSimpleName();
    private static String f = "http://data.flurry.com/aap.do";
    private static String g = "https://data.flurry.com/aap.do";
    private String h;
    private boolean i;

    public iy() {
        this(null);
    }

    public iy(kt.a aVar) {
        super("Analytics", iy.class.getSimpleName());
        this.d = "AnalyticsData_";
        h();
        a(aVar);
    }

    private void h() {
        ll a = lk.a();
        this.i = ((Boolean) a.a("UseHttps")).booleanValue();
        a.a("UseHttps", (a) this);
        kg.a(4, e, "initSettings, UseHttps = " + this.i);
        String str = (String) a.a("ReportUrl");
        a.a("ReportUrl", (a) this);
        b(str);
        kg.a(4, e, "initSettings, ReportUrl = " + str);
    }

    public void a() {
        lk.a().b("UseHttps", (a) this);
        lk.a().b("ReportUrl", (a) this);
    }

    public void a(String str, Object obj) {
        Object obj2 = -1;
        switch (str.hashCode()) {
            case -239660092:
                if (str.equals("UseHttps")) {
                    obj2 = null;
                    break;
                }
                break;
            case 1650629499:
                if (str.equals("ReportUrl")) {
                    obj2 = 1;
                    break;
                }
                break;
        }
        switch (obj2) {
            case null:
                this.i = ((Boolean) obj).booleanValue();
                kg.a(4, e, "onSettingUpdate, UseHttps = " + this.i);
                return;
            case 1:
                String str2 = (String) obj;
                b(str2);
                kg.a(4, e, "onSettingUpdate, ReportUrl = " + str2);
                return;
            default:
                kg.a(6, e, "onSettingUpdate internal error!");
                return;
        }
    }

    private void b(String str) {
        if (!(str == null || str.endsWith(".do"))) {
            kg.a(5, e, "overriding analytics agent report URL without an endpoint, are you sure?");
        }
        this.h = str;
    }

    String b() {
        if (this.h != null) {
            return this.h;
        }
        if (this.i) {
            return g;
        }
        return f;
    }

    protected void a(byte[] bArr, final String str, final String str2) {
        String b = b();
        kg.a(4, e, "FlurryDataSender: start upload data " + Arrays.toString(bArr) + " with id = " + str + " to " + b);
        lz knVar = new kn();
        knVar.a(b);
        knVar.d(100000);
        knVar.a(kp.a.kPost);
        knVar.a("Content-Type", "application/octet-stream");
        knVar.a(new kx());
        knVar.a((Object) bArr);
        knVar.a(new kn.a<byte[], Void>(this) {
            final /* synthetic */ iy c;

            public void a(kn<byte[], Void> knVar, Void voidR) {
                final int h = knVar.h();
                if (h > 0) {
                    kg.e(iy.e, "Analytics report sent.");
                    kg.a(3, iy.e, "FlurryDataSender: report " + str + " sent. HTTP response: " + h);
                    if (kg.c() <= 3 && kg.d()) {
                        js.a().a(new Runnable(this) {
                            final /* synthetic */ AnonymousClass1 b;

                            public void run() {
                                Toast.makeText(js.a().c(), "SD HTTP Response Code: " + h, 0).show();
                            }
                        });
                    }
                    this.c.a(str, str2, h);
                    this.c.e();
                    return;
                }
                this.c.b(str, str2);
            }
        });
        jq.a().a((Object) this, knVar);
    }

    protected void a(String str, String str2, final int i) {
        a(new ly(this) {
            final /* synthetic */ iy b;

            public void a() {
                if (i == 200) {
                    hl.a().f();
                }
            }
        });
        super.a(str, str2, i);
    }
}
