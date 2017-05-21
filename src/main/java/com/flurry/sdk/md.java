package com.flurry.sdk;

public class md {
    private static final String a = md.class.getSimpleName();
    private static boolean b;

    public static synchronized void a() {
        synchronized (md.class) {
            if (!b) {
                ki.a(je.class, 10);
                try {
                    ki.a(hl.class, 10);
                } catch (NoClassDefFoundError e) {
                    kg.a(3, a, "Analytics module not available");
                }
                try {
                    ki.a(mb.class, 10);
                } catch (NoClassDefFoundError e2) {
                    kg.a(3, a, "Crash module not available");
                }
                try {
                    ki.a(i.class, 10);
                } catch (NoClassDefFoundError e3) {
                    kg.a(3, a, "Ads module not available");
                }
                b = true;
            }
        }
    }
}
