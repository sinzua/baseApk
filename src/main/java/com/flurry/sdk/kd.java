package com.flurry.sdk;

import java.util.Comparator;

public class kd implements Comparator<Runnable> {
    private static final String a = kd.class.getSimpleName();

    public /* synthetic */ int compare(Object obj, Object obj2) {
        return a((Runnable) obj, (Runnable) obj2);
    }

    public int a(Runnable runnable, Runnable runnable2) {
        int a = a(runnable);
        int a2 = a(runnable2);
        if (a < a2) {
            return -1;
        }
        if (a > a2) {
            return 1;
        }
        return 0;
    }

    private int a(Runnable runnable) {
        if (runnable == null) {
            return Integer.MAX_VALUE;
        }
        if (runnable instanceof ke) {
            int n;
            lz lzVar = (lz) ((ke) runnable).a();
            if (lzVar != null) {
                n = lzVar.n();
            } else {
                n = Integer.MAX_VALUE;
            }
            return n;
        } else if (runnable instanceof lz) {
            return ((lz) runnable).n();
        } else {
            kg.a(6, a, "Unknown runnable class: " + runnable.getClass().getName());
            return Integer.MAX_VALUE;
        }
    }
}
