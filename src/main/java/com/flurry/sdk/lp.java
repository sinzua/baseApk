package com.flurry.sdk;

public class lp {
    private static final String a = lp.class.getSimpleName();
    private long b = 1000;
    private boolean c = true;
    private boolean d = false;
    private ly e = new ly(this) {
        final /* synthetic */ lp a;

        {
            this.a = r1;
        }

        public void a() {
            new ln().b();
            if (this.a.c && this.a.d) {
                js.a().b(this.a.e, this.a.b);
            }
        }
    };

    public void a(long j) {
        this.b = j;
    }

    public void a(boolean z) {
        this.c = z;
    }

    public synchronized void a() {
        if (!this.d) {
            js.a().b(this.e, this.b);
            this.d = true;
        }
    }

    public synchronized void b() {
        if (this.d) {
            js.a().c(this.e);
            this.d = false;
        }
    }
}
