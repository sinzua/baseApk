package com.flurry.sdk;

import java.util.Timer;
import java.util.TimerTask;

public class ko {
    private static final String a = ko.class.getSimpleName();
    private Timer b;
    private a c;
    private kp d;

    class a extends TimerTask {
        final /* synthetic */ ko a;

        private a(ko koVar) {
            this.a = koVar;
        }

        public void run() {
            kg.a(3, ko.a, "HttpRequest timed out. Cancelling.");
            this.a.d.k();
        }
    }

    public ko(kp kpVar) {
        this.d = kpVar;
    }

    public synchronized void a(long j) {
        if (b()) {
            a();
        }
        this.b = new Timer("HttpRequestTimeoutTimer");
        this.c = new a();
        this.b.schedule(this.c, j);
        kg.a(3, a, "HttpRequestTimeoutTimer started: " + j + "MS");
    }

    public synchronized void a() {
        if (this.b != null) {
            this.b.cancel();
            this.b = null;
            kg.a(3, a, "HttpRequestTimeoutTimer stopped.");
        }
        this.c = null;
    }

    public boolean b() {
        if (this.b != null) {
            return true;
        }
        return false;
    }
}
