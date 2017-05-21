package com.flurry.sdk;

import com.supersonicads.sdk.utils.Constants.ControllerParameters;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;

public class kp extends lz {
    private static final String a = kp.class.getSimpleName();
    private String b;
    private a c;
    private int d = ControllerParameters.LOAD_RUNTIME;
    private int e = 15000;
    private int f;
    private int g;
    private boolean j = true;
    private final jx<String, String> k = new jx();
    private c l;
    private HttpURLConnection m;
    private boolean n;
    private boolean o;
    private boolean p;
    private long q = -1;
    private long r = -1;
    private Exception s;
    private int t = -1;
    private final jx<String, String> u = new jx();
    private final Object v = new Object();
    private boolean w;
    private int x = 25000;
    private ko y = new ko(this);
    private boolean z;

    public enum a {
        kUnknown,
        kGet,
        kPost,
        kPut,
        kDelete,
        kHead;

        public String toString() {
            switch (this) {
                case kPost:
                    return "POST";
                case kPut:
                    return "PUT";
                case kDelete:
                    return "DELETE";
                case kHead:
                    return "HEAD";
                case kGet:
                    return "GET";
                default:
                    return null;
            }
        }
    }

    public interface c {
        void a(kp kpVar);

        void a(kp kpVar, InputStream inputStream) throws Exception;

        void a(kp kpVar, OutputStream outputStream) throws Exception;
    }

    public static class b implements c {
        public void a(kp kpVar, OutputStream outputStream) throws Exception {
        }

        public void a(kp kpVar, InputStream inputStream) throws Exception {
        }

        public void a(kp kpVar) {
        }
    }

    public void a(String str) {
        this.b = str;
    }

    public String b() {
        return this.b;
    }

    public void a(a aVar) {
        this.c = aVar;
    }

    public long c() {
        return this.r;
    }

    public void a(int i) {
        this.d = i;
    }

    public void b(int i) {
        this.e = i;
    }

    public void a(boolean z) {
        this.w = z;
    }

    public void c(int i) {
        this.x = i;
    }

    public boolean d() {
        boolean z;
        if (this.s == null || !(this.s instanceof SocketTimeoutException)) {
            z = false;
        } else {
            z = true;
        }
        if (this.z || r0) {
            return true;
        }
        return false;
    }

    public void b(boolean z) {
        this.j = z;
    }

    public void c(boolean z) {
        this.p = z;
    }

    public void a(String str, String str2) {
        this.k.a((Object) str, (Object) str2);
    }

    public void a(c cVar) {
        this.l = cVar;
    }

    public boolean e() {
        boolean z;
        synchronized (this.v) {
            z = this.o;
        }
        return z;
    }

    public boolean f() {
        return !i() && g();
    }

    public boolean g() {
        return this.t >= 200 && this.t < 400 && !this.z;
    }

    public int h() {
        return this.t;
    }

    public boolean i() {
        return this.s != null;
    }

    public Exception j() {
        return this.s;
    }

    public List<String> b(String str) {
        if (str == null) {
            return null;
        }
        return this.u.a((Object) str);
    }

    public void k() {
        kg.a(3, a, "Timeout (" + (System.currentTimeMillis() - this.q) + "MS) for url: " + this.b);
        this.t = 629;
        this.z = true;
        s();
        l();
    }

    public void l() {
        kg.a(3, a, "Cancelling http request: " + this.b);
        synchronized (this.v) {
            this.o = true;
        }
        u();
    }

    public void c(String str) {
        if (this.u != null && this.u.c(str)) {
            this.u.b(str);
        }
    }

    public void a() {
        try {
            if (this.b != null) {
                if (jl.a().c()) {
                    if (this.c == null || a.kUnknown.equals(this.c)) {
                        this.c = a.kGet;
                    }
                    r();
                    kg.a(4, a, "HTTP status: " + this.t + " for url: " + this.b);
                    this.y.a();
                    s();
                    return;
                }
                kg.a(3, a, "Network not available, aborting http request: " + this.b);
                this.y.a();
                s();
            }
        } catch (Throwable e) {
            kg.a(4, a, "HTTP status: " + this.t + " for url: " + this.b);
            kg.a(3, a, "Exception during http request: " + this.b, e);
            this.g = this.m.getReadTimeout();
            this.f = this.m.getConnectTimeout();
            this.s = e;
        } finally {
            this.y.a();
            s();
        }
    }

    public void m() {
        l();
    }

    private void r() throws Exception {
        Throwable th;
        Closeable closeable = null;
        if (!this.o) {
            this.b = lt.a(this.b);
            this.m = (HttpURLConnection) new URL(this.b).openConnection();
            this.m.setConnectTimeout(this.d);
            this.m.setReadTimeout(this.e);
            this.m.setRequestMethod(this.c.toString());
            this.m.setInstanceFollowRedirects(this.j);
            this.m.setDoOutput(a.kPost.equals(this.c));
            this.m.setDoInput(true);
            for (Entry entry : this.k.b()) {
                this.m.addRequestProperty((String) entry.getKey(), (String) entry.getValue());
            }
            if (!(a.kGet.equals(this.c) || a.kPost.equals(this.c))) {
                this.m.setRequestProperty("Accept-Encoding", "");
            }
            if (this.o) {
                t();
                return;
            }
            Closeable outputStream;
            Closeable bufferedOutputStream;
            if (a.kPost.equals(this.c)) {
                try {
                    outputStream = this.m.getOutputStream();
                    try {
                        bufferedOutputStream = new BufferedOutputStream(outputStream);
                        try {
                            a((OutputStream) bufferedOutputStream);
                            lt.a(bufferedOutputStream);
                            lt.a(outputStream);
                        } catch (Throwable th2) {
                            th = th2;
                            closeable = outputStream;
                            lt.a(bufferedOutputStream);
                            lt.a(closeable);
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        bufferedOutputStream = null;
                        closeable = outputStream;
                        lt.a(bufferedOutputStream);
                        lt.a(closeable);
                        throw th;
                    }
                } catch (Throwable th4) {
                    t();
                }
            }
            if (this.p) {
                this.q = System.currentTimeMillis();
            }
            if (this.w) {
                this.y.a((long) this.x);
            }
            this.t = this.m.getResponseCode();
            if (this.p && this.q != -1) {
                this.r = System.currentTimeMillis() - this.q;
            }
            this.y.a();
            for (Entry entry2 : this.m.getHeaderFields().entrySet()) {
                for (Object a : (List) entry2.getValue()) {
                    this.u.a(entry2.getKey(), a);
                }
            }
            if (!a.kGet.equals(this.c) && !a.kPost.equals(this.c)) {
                t();
            } else if (this.o) {
                t();
            } else {
                try {
                    outputStream = this.m.getInputStream();
                    try {
                        bufferedOutputStream = new BufferedInputStream(outputStream);
                        try {
                            a((InputStream) bufferedOutputStream);
                            lt.a(bufferedOutputStream);
                            lt.a(outputStream);
                            t();
                        } catch (Throwable th5) {
                            th = th5;
                            closeable = bufferedOutputStream;
                            bufferedOutputStream = outputStream;
                            lt.a(closeable);
                            lt.a(bufferedOutputStream);
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        bufferedOutputStream = outputStream;
                        lt.a(closeable);
                        lt.a(bufferedOutputStream);
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    bufferedOutputStream = null;
                    lt.a(closeable);
                    lt.a(bufferedOutputStream);
                    throw th;
                }
            }
        }
    }

    private void a(OutputStream outputStream) throws Exception {
        if (this.l != null && !e() && outputStream != null) {
            this.l.a(this, outputStream);
        }
    }

    private void a(InputStream inputStream) throws Exception {
        if (this.l != null && !e() && inputStream != null) {
            this.l.a(this, inputStream);
        }
    }

    private void s() {
        if (this.l != null && !e()) {
            this.l.a(this);
        }
    }

    private void t() {
        if (!this.n) {
            this.n = true;
            if (this.m != null) {
                this.m.disconnect();
            }
        }
    }

    private void u() {
        if (!this.n) {
            this.n = true;
            if (this.m != null) {
                new Thread(this) {
                    final /* synthetic */ kp a;

                    {
                        this.a = r1;
                    }

                    public void run() {
                        try {
                            if (this.a.m != null) {
                                this.a.m.disconnect();
                            }
                        } catch (Throwable th) {
                        }
                    }
                }.start();
            }
        }
    }
}
