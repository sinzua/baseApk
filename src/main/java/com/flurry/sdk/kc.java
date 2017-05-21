package com.flurry.sdk;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class kc {
    private static kc a = null;
    private final jx<String, kq<kb<?>>> b = new jx();
    private final jx<kq<kb<?>>, String> c = new jx();

    public static synchronized kc a() {
        kc kcVar;
        synchronized (kc.class) {
            if (a == null) {
                a = new kc();
            }
            kcVar = a;
        }
        return kcVar;
    }

    public static synchronized void b() {
        synchronized (kc.class) {
            if (a != null) {
                a.c();
                a = null;
            }
        }
    }

    private kc() {
    }

    public synchronized void a(String str, kb<?> kbVar) {
        if (!(TextUtils.isEmpty(str) || kbVar == null)) {
            Object kqVar = new kq(kbVar);
            if (!this.b.c(str, kqVar)) {
                this.b.a((Object) str, kqVar);
                this.c.a(kqVar, (Object) str);
            }
        }
    }

    public synchronized void b(String str, kb<?> kbVar) {
        if (!TextUtils.isEmpty(str)) {
            kq kqVar = new kq(kbVar);
            this.b.b(str, kqVar);
            this.c.b(kqVar, str);
        }
    }

    public synchronized void a(String str) {
        if (!TextUtils.isEmpty(str)) {
            for (kq b : this.b.a((Object) str)) {
                this.c.b(b, str);
            }
            this.b.b(str);
        }
    }

    public synchronized void a(kb<?> kbVar) {
        if (kbVar != null) {
            Object kqVar = new kq(kbVar);
            for (String b : this.c.a(kqVar)) {
                this.b.b(b, kqVar);
            }
            this.c.b(kqVar);
        }
    }

    public synchronized void c() {
        this.b.a();
        this.c.a();
    }

    public synchronized int b(String str) {
        int i;
        if (TextUtils.isEmpty(str)) {
            i = 0;
        } else {
            i = this.b.a((Object) str).size();
        }
        return i;
    }

    public synchronized List<kb<?>> c(String str) {
        List<kb<?>> emptyList;
        if (TextUtils.isEmpty(str)) {
            emptyList = Collections.emptyList();
        } else {
            List<kb<?>> arrayList = new ArrayList();
            Iterator it = this.b.a((Object) str).iterator();
            while (it.hasNext()) {
                kb kbVar = (kb) ((kq) it.next()).get();
                if (kbVar == null) {
                    it.remove();
                } else {
                    arrayList.add(kbVar);
                }
            }
            emptyList = arrayList;
        }
        return emptyList;
    }

    public void a(final ka kaVar) {
        if (kaVar != null) {
            for (final kb kbVar : c(kaVar.a())) {
                js.a().b(new ly(this) {
                    final /* synthetic */ kc c;

                    public void a() {
                        kbVar.a(kaVar);
                    }
                });
            }
        }
    }
}
