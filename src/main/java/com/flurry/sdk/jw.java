package com.flurry.sdk;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import com.flurry.sdk.jv.a;

public class jw {
    private static jw a;
    private static final String b = jw.class.getSimpleName();
    private Object c;

    public static synchronized jw a() {
        jw jwVar;
        synchronized (jw.class) {
            if (a == null) {
                a = new jw();
            }
            jwVar = a;
        }
        return jwVar;
    }

    public static synchronized void b() {
        synchronized (jw.class) {
            if (a != null) {
                a.f();
            }
            a = null;
        }
    }

    private jw() {
        e();
    }

    public boolean c() {
        return this.c != null;
    }

    @TargetApi(14)
    private void e() {
        if (VERSION.SDK_INT >= 14 && this.c == null) {
            Context c = js.a().c();
            if (c instanceof Application) {
                this.c = new ActivityLifecycleCallbacks(this) {
                    final /* synthetic */ jw a;

                    {
                        this.a = r1;
                    }

                    public void onActivityCreated(Activity activity, Bundle bundle) {
                        kg.a(3, jw.b, "onActivityCreated for activity:" + activity);
                        a(activity, a.kCreated);
                    }

                    public void onActivityStarted(Activity activity) {
                        kg.a(3, jw.b, "onActivityStarted for activity:" + activity);
                        a(activity, a.kStarted);
                    }

                    public void onActivityResumed(Activity activity) {
                        kg.a(3, jw.b, "onActivityResumed for activity:" + activity);
                        a(activity, a.kResumed);
                    }

                    public void onActivityPaused(Activity activity) {
                        kg.a(3, jw.b, "onActivityPaused for activity:" + activity);
                        a(activity, a.kPaused);
                    }

                    public void onActivityStopped(Activity activity) {
                        kg.a(3, jw.b, "onActivityStopped for activity:" + activity);
                        a(activity, a.kStopped);
                    }

                    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                        kg.a(3, jw.b, "onActivitySaveInstanceState for activity:" + activity);
                        a(activity, a.kSaveState);
                    }

                    public void onActivityDestroyed(Activity activity) {
                        kg.a(3, jw.b, "onActivityDestroyed for activity:" + activity);
                        a(activity, a.kDestroyed);
                    }

                    protected void a(Activity activity, a aVar) {
                        jv jvVar = new jv();
                        jvVar.a = activity;
                        jvVar.b = aVar;
                        jvVar.b();
                    }
                };
                ((Application) c).registerActivityLifecycleCallbacks((ActivityLifecycleCallbacks) this.c);
            }
        }
    }

    @TargetApi(14)
    private void f() {
        if (VERSION.SDK_INT >= 14 && this.c != null) {
            Context c = js.a().c();
            if (c instanceof Application) {
                ((Application) c).unregisterActivityLifecycleCallbacks((ActivityLifecycleCallbacks) this.c);
                this.c = null;
            }
        }
    }
}
