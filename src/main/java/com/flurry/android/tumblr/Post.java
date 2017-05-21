package com.flurry.android.tumblr;

import android.os.Bundle;
import android.text.TextUtils;
import com.flurry.sdk.ec;
import com.flurry.sdk.ec.a;
import com.flurry.sdk.js;
import com.flurry.sdk.kb;
import com.flurry.sdk.kc;
import com.flurry.sdk.kg;
import com.flurry.sdk.ly;
import com.flurry.sdk.me;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Post {
    private static final String a = Post.class.getName();
    private static AtomicInteger g = new AtomicInteger(0);
    private String b;
    private String c;
    private String d;
    private PostListener e;
    private int f;
    private final kb<ec> h;

    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] a = new int[a.values().length];

        static {
            try {
                a[a.a.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                a[a.b.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    abstract Bundle c();

    Post() {
        this.f = 0;
        this.h = new kb<ec>(this) {
            final /* synthetic */ Post a;

            {
                this.a = r1;
            }

            public void a(final ec ecVar) {
                if (ecVar.c == this.a.f && ecVar.b != null) {
                    final PostListener b = this.a.e;
                    if (b != null) {
                        js.a().a(new ly(this) {
                            final /* synthetic */ AnonymousClass1 c;

                            public void a() {
                                switch (AnonymousClass2.a[ecVar.b.ordinal()]) {
                                    case 1:
                                        kg.a(3, Post.a, "Post success for " + ecVar.c);
                                        b.onPostSuccess(ecVar.f);
                                        kc.a().b("com.flurry.android.impl.analytics.tumblr.TumblrEvents", this.c.a.h);
                                        me.a().a(this.c.a.f);
                                        return;
                                    case 2:
                                        String str = ecVar.e;
                                        if (TextUtils.isEmpty(str)) {
                                            str = "Internal error.";
                                        }
                                        kg.a(3, Post.a, "Post failed for " + ecVar.c + " with error code: " + ecVar.d + "  and error message: " + str);
                                        b.onPostFailure(str);
                                        kc.a().b("com.flurry.android.impl.analytics.tumblr.TumblrEvents", this.c.a.h);
                                        me.a().a(this.c.a.f);
                                        return;
                                    default:
                                        return;
                                }
                            }
                        });
                    }
                }
            }
        };
        this.f = g.incrementAndGet();
        kc.a().a("com.flurry.android.impl.analytics.tumblr.TumblrEvents", this.h);
    }

    public void setAndroidDeeplink(String str) {
        this.c = str;
    }

    public void setIOSDeepLink(String str) {
        this.b = str;
    }

    public void setWebLink(String str) {
        this.d = str;
    }

    public void setPostListener(PostListener postListener) {
        this.e = postListener;
    }

    String d() {
        return this.c;
    }

    String e() {
        return this.b;
    }

    String f() {
        return this.d;
    }

    int g() {
        return this.f;
    }
}
