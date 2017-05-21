package com.flurry.sdk;

import android.content.Context;
import java.io.File;
import java.util.List;
import java.util.Map;

public class hm {
    private static final String b = hm.class.getSimpleName();
    boolean a;
    private final hn c;
    private final File d;
    private String e;

    public hm() {
        this(js.a().c());
    }

    public hm(Context context) {
        this.c = new hn();
        this.d = context.getFileStreamPath(".flurryinstallreceiver.");
        kg.a(3, b, "Referrer file name if it exists:  " + this.d);
    }

    public synchronized void a() {
        this.d.delete();
        this.e = null;
        this.a = true;
    }

    public synchronized Map<String, List<String>> a(boolean z) {
        Map<String, List<String>> a;
        c();
        a = this.c.a(this.e);
        if (z) {
            a();
        }
        return a;
    }

    public synchronized String b() {
        c();
        return this.e;
    }

    public synchronized void a(String str) {
        this.a = true;
        b(str);
        d();
    }

    private void b(String str) {
        if (str != null) {
            this.e = str;
        }
    }

    private void c() {
        if (!this.a) {
            this.a = true;
            kg.a(4, b, "Loading referrer info from file: " + this.d.getAbsolutePath());
            String c = ls.c(this.d);
            kg.a(b, "Referrer file contents: " + c);
            b(c);
        }
    }

    private void d() {
        ls.a(this.d, this.e);
    }
}
