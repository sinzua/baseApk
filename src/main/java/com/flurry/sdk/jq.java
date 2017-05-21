package com.flurry.sdk;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public final class jq extends kf<kp> {
    private static jq a = null;

    public static synchronized jq a() {
        jq jqVar;
        synchronized (jq.class) {
            if (a == null) {
                a = new jq();
            }
            jqVar = a;
        }
        return jqVar;
    }

    public static synchronized void b() {
        synchronized (jq.class) {
            if (a != null) {
                a.c();
            }
            a = null;
        }
    }

    protected jq() {
        super(jq.class.getName(), 0, 5, 5000, TimeUnit.MILLISECONDS, new PriorityBlockingQueue(11, new kd()));
    }
}
