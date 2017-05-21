package com.flurry.sdk;

import java.io.PrintStream;
import java.io.PrintWriter;

public abstract class ly implements Runnable {
    private static final String a = ly.class.getSimpleName();
    PrintStream h;
    PrintWriter i;

    public abstract void a();

    public final void run() {
        try {
            a();
        } catch (Throwable th) {
            if (this.h != null) {
                th.printStackTrace(this.h);
            } else if (this.i != null) {
                th.printStackTrace(this.i);
            } else {
                th.printStackTrace();
            }
            kg.a(6, a, "", th);
        }
    }
}
