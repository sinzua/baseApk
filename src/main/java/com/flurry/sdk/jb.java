package com.flurry.sdk;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.text.TextUtils;
import com.flurry.android.FlurryEventRecordStatus;
import com.flurry.sdk.ll.a;
import com.supersonicads.sdk.utils.Constants.ControllerParameters;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class jb implements a {
    static int a = 100;
    static int b = 10;
    static int c = ControllerParameters.SECOND;
    static int d = 160000;
    static int e = 50;
    private static final String f = jb.class.getSimpleName();
    private int A = 0;
    private final List<iu> B = new ArrayList();
    private int C = 0;
    private int D = 0;
    private boolean E = true;
    private final hm F = new hm();
    private final kb<jg> G = new kb<jg>(this) {
        final /* synthetic */ jb a;

        {
            this.a = r1;
        }

        public void a(jg jgVar) {
            js.a().b(new ly(this) {
                final /* synthetic */ AnonymousClass9 a;

                {
                    this.a = r1;
                }

                public void a() {
                    this.a.a.a(true, je.a().d());
                }
            });
        }
    };
    private final AtomicInteger g = new AtomicInteger(0);
    private final AtomicInteger h = new AtomicInteger(0);
    private final AtomicInteger i = new AtomicInteger(0);
    private final kb<lg> j = new kb<lg>(this) {
        final /* synthetic */ jb a;

        {
            this.a = r1;
        }

        public void a(lg lgVar) {
            if (this.a.k == null || lgVar.b == this.a.k.get()) {
                switch (lgVar.c) {
                    case CREATE:
                        this.a.a(lgVar.b, (Context) lgVar.a.get());
                        return;
                    case START:
                        this.a.a((Context) lgVar.a.get());
                        return;
                    case END:
                        this.a.b((Context) lgVar.a.get());
                        return;
                    case FINALIZE:
                        kc.a().b("com.flurry.android.sdk.FlurrySessionEvent", this.a.j);
                        this.a.a(lgVar.d);
                        return;
                    default:
                        return;
                }
            }
        }
    };
    private WeakReference<lf> k;
    private File l;
    private jz<List<iz>> m;
    private boolean n;
    private long o;
    private boolean p;
    private String q;
    private byte r;
    private Long s;
    private int t = -1;
    private final List<iz> u = new ArrayList();
    private final Map<String, List<String>> v = new HashMap();
    private final Map<String, String> w = new HashMap();
    private final Map<String, iv> x = new HashMap();
    private final List<iw> y = new ArrayList();
    private boolean z = true;

    public jb() {
        kc.a().a("com.flurry.android.sdk.FlurrySessionEvent", this.j);
    }

    public void a(lf lfVar, Context context) {
        this.k = new WeakReference(lfVar);
        ll a = lk.a();
        this.p = ((Boolean) a.a("LogEvents")).booleanValue();
        a.a("LogEvents", (a) this);
        kg.a(4, f, "initSettings, LogEvents = " + this.p);
        this.q = (String) a.a("UserId");
        a.a("UserId", (a) this);
        kg.a(4, f, "initSettings, UserId = " + this.q);
        this.r = ((Byte) a.a("Gender")).byteValue();
        a.a("Gender", (a) this);
        kg.a(4, f, "initSettings, Gender = " + this.r);
        this.s = (Long) a.a("Age");
        a.a("Age", (a) this);
        kg.a(4, f, "initSettings, BirthDate = " + this.s);
        this.E = ((Boolean) a.a("analyticsEnabled")).booleanValue();
        a.a("analyticsEnabled", (a) this);
        kg.a(4, f, "initSettings, AnalyticsEnabled = " + this.E);
        this.l = context.getFileStreamPath(k());
        this.m = new jz(context.getFileStreamPath(l()), ".yflurryreport.", 1, new le<List<iz>>(this) {
            final /* synthetic */ jb a;

            {
                this.a = r1;
            }

            public lb<List<iz>> a(int i) {
                return new la(new iz.a());
            }
        });
        c(context);
        a(true);
        if (hl.a().c() != null) {
            js.a().b(new ly(this) {
                final /* synthetic */ jb a;

                {
                    this.a = r1;
                }

                public void a() {
                    hl.a().c().b();
                }
            });
        }
        js.a().b(new ly(this) {
            final /* synthetic */ jb a;

            {
                this.a = r1;
            }

            public void a() {
                this.a.i();
            }
        });
        js.a().b(new ly(this) {
            final /* synthetic */ jb a;

            {
                this.a = r1;
            }

            public void a() {
                this.a.m();
            }
        });
        if (jf.a().c()) {
            js.a().b(new ly(this) {
                final /* synthetic */ jb a;

                {
                    this.a = r1;
                }

                public void a() {
                    this.a.a(true, je.a().d());
                }
            });
        } else {
            kc.a().a("com.flurry.android.sdk.IdProviderFinishedEvent", this.G);
        }
    }

    public synchronized void a(Context context) {
        this.t = lr.j();
        if (hl.a().e() != null) {
            js.a().b(new ly(this) {
                final /* synthetic */ jb a;

                {
                    this.a = r1;
                }

                public void a() {
                    hl.a().e().e();
                }
            });
        }
        if (this.E && hl.a().c() != null) {
            js.a().b(new ly(this) {
                final /* synthetic */ jb a;

                {
                    this.a = r1;
                }

                public void a() {
                    hl.a().c().c();
                }
            });
        }
    }

    public synchronized void b(Context context) {
        a(false);
        final long d = je.a().d();
        final long f = je.a().f();
        final long h = je.a().h();
        final int a = je.a().l().a();
        b(je.a().f());
        if (this.E && hl.a().c() != null) {
            js.a().b(new ly(this) {
                final /* synthetic */ jb b;

                public void a() {
                    hl.a().c().a(d);
                }
            });
        }
        js.a().b(new ly(this) {
            final /* synthetic */ jb a;

            {
                this.a = r1;
            }

            public void a() {
                this.a.n();
            }
        });
        if (jf.a().c()) {
            js.a().b(new ly(this) {
                final /* synthetic */ jb e;

                public void a() {
                    iz a = this.e.a(d, f, h, a);
                    this.e.u.clear();
                    this.e.u.add(a);
                    this.e.c();
                }
            });
        }
    }

    public synchronized void a(final long j) {
        kc.a().a(this.G);
        js.a().b(new ly(this) {
            final /* synthetic */ jb a;

            {
                this.a = r1;
            }

            public void a() {
                if (this.a.E && hl.a().c() != null) {
                    hl.a().c().d();
                }
                if (hl.a().e() != null) {
                    js.a().b(new ly(this) {
                        final /* synthetic */ AnonymousClass5 a;

                        {
                            this.a = r1;
                        }

                        public void a() {
                            hl.a().e().d();
                        }
                    });
                }
            }
        });
        if (jf.a().c()) {
            js.a().b(new ly(this) {
                final /* synthetic */ jb b;

                public void a() {
                    this.b.a(false, j);
                }
            });
        }
        lk.a().b("Gender", (a) this);
        lk.a().b("UserId", (a) this);
        lk.a().b("Age", (a) this);
        lk.a().b("LogEvents", (a) this);
    }

    public void a(String str, Object obj) {
        int i = -1;
        switch (str.hashCode()) {
            case -1752163738:
                if (str.equals("UserId")) {
                    i = 1;
                    break;
                }
                break;
            case -1720015653:
                if (str.equals("analyticsEnabled")) {
                    i = 4;
                    break;
                }
                break;
            case -738063011:
                if (str.equals("LogEvents")) {
                    i = 0;
                    break;
                }
                break;
            case 65759:
                if (str.equals("Age")) {
                    i = 3;
                    break;
                }
                break;
            case 2129321697:
                if (str.equals("Gender")) {
                    i = 2;
                    break;
                }
                break;
        }
        switch (i) {
            case 0:
                this.p = ((Boolean) obj).booleanValue();
                kg.a(4, f, "onSettingUpdate, LogEvents = " + this.p);
                return;
            case 1:
                this.q = (String) obj;
                kg.a(4, f, "onSettingUpdate, UserId = " + this.q);
                return;
            case 2:
                this.r = ((Byte) obj).byteValue();
                kg.a(4, f, "onSettingUpdate, Gender = " + this.r);
                return;
            case 3:
                this.s = (Long) obj;
                kg.a(4, f, "onSettingUpdate, Birthdate = " + this.s);
                return;
            case 4:
                this.E = ((Boolean) obj).booleanValue();
                kg.a(4, f, "onSettingUpdate, AnalyticsEnabled = " + this.E);
                return;
            default:
                kg.a(6, f, "onSettingUpdate internal error!");
                return;
        }
    }

    public void a() {
        this.n = true;
    }

    private void c(Context context) {
        if (context instanceof Activity) {
            Bundle extras = ((Activity) context).getIntent().getExtras();
            if (extras != null) {
                kg.a(3, f, "Launch Options Bundle is present " + extras.toString());
                for (String str : extras.keySet()) {
                    if (str != null) {
                        Object obj = extras.get(str);
                        this.v.put(str, new ArrayList(Arrays.asList(new String[]{obj != null ? obj.toString() : "null"})));
                        kg.a(3, f, "Launch options Key: " + str + ". Its value: " + r1);
                    }
                }
            }
        }
    }

    @TargetApi(18)
    private void a(boolean z) {
        boolean z2;
        int intExtra;
        Exception exception;
        int i;
        Object obj;
        float f;
        int i2 = -1;
        if (z) {
            this.w.put("boot.time", Long.toString(System.currentTimeMillis() - SystemClock.elapsedRealtime()));
            StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
            StatFs statFs2 = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
            if (VERSION.SDK_INT >= 18) {
                this.w.put("disk.size.total.internal", Long.toString(statFs.getAvailableBlocksLong()));
                this.w.put("disk.size.available.internal", Long.toString(statFs.getAvailableBlocksLong()));
                this.w.put("disk.size.total.external", Long.toString(statFs2.getAvailableBlocksLong()));
                this.w.put("disk.size.available.external", Long.toString(statFs2.getAvailableBlocksLong()));
            } else {
                this.w.put("disk.size.total.internal", Long.toString((long) statFs.getAvailableBlocks()));
                this.w.put("disk.size.available.internal", Long.toString((long) statFs.getAvailableBlocks()));
                this.w.put("disk.size.total.external", Long.toString((long) statFs2.getAvailableBlocks()));
                this.w.put("disk.size.available.external", Long.toString((long) statFs2.getAvailableBlocks()));
            }
            this.w.put("carrier.name", jm.a().c());
            this.w.put("carrier.details", jm.a().d());
        }
        ActivityManager activityManager = (ActivityManager) js.a().c().getSystemService("activity");
        MemoryInfo memoryInfo = new MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        this.w.put("memory.available" + (z ? ".start" : ".end"), Long.toString(memoryInfo.availMem));
        if (VERSION.SDK_INT >= 16) {
            this.w.put("memory.total" + (z ? ".start" : ".end"), Long.toString(memoryInfo.availMem));
        }
        try {
            Intent registerReceiver = js.a().c().registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            if (registerReceiver != null) {
                int intExtra2 = registerReceiver.getIntExtra("status", -1);
                z2 = intExtra2 == 2 || intExtra2 == 5;
                try {
                    intExtra = registerReceiver.getIntExtra("level", -1);
                } catch (Exception e) {
                    exception = e;
                    i = -1;
                    kg.a(5, f, "Error getting battery status: " + obj);
                    i2 = i;
                    i = -1;
                    f = ((float) i2) / ((float) i);
                    this.w.put("battery.charging" + (z ? ".end" : ".start"), Boolean.toString(z2));
                    this.w.put("battery.remaining" + (z ? ".end" : ".start"), Float.toString(f));
                }
                try {
                    i = registerReceiver.getIntExtra("scale", -1);
                    i2 = intExtra;
                } catch (Exception e2) {
                    Exception exception2 = e2;
                    i = intExtra;
                    exception = exception2;
                    kg.a(5, f, "Error getting battery status: " + obj);
                    i2 = i;
                    i = -1;
                    f = ((float) i2) / ((float) i);
                    if (z) {
                    }
                    this.w.put("battery.charging" + (z ? ".end" : ".start"), Boolean.toString(z2));
                    if (z) {
                    }
                    this.w.put("battery.remaining" + (z ? ".end" : ".start"), Float.toString(f));
                }
            }
            z2 = false;
            i = -1;
        } catch (Exception e3) {
            obj = e3;
            z2 = false;
            i = -1;
            kg.a(5, f, "Error getting battery status: " + obj);
            i2 = i;
            i = -1;
            f = ((float) i2) / ((float) i);
            if (z) {
            }
            this.w.put("battery.charging" + (z ? ".end" : ".start"), Boolean.toString(z2));
            if (z) {
            }
            this.w.put("battery.remaining" + (z ? ".end" : ".start"), Float.toString(f));
        }
        f = ((float) i2) / ((float) i);
        if (z) {
        }
        this.w.put("battery.charging" + (z ? ".end" : ".start"), Boolean.toString(z2));
        if (z) {
        }
        this.w.put("battery.remaining" + (z ? ".end" : ".start"), Float.toString(f));
    }

    private synchronized void b(long j) {
        for (iw iwVar : this.y) {
            if (iwVar.a() && !iwVar.b()) {
                iwVar.a(j);
            }
        }
    }

    synchronized iz a(long j, long j2, long j3, int i) {
        iz izVar;
        ja jaVar = new ja();
        jaVar.a(jo.a().e());
        jaVar.a(j);
        jaVar.b(j2);
        jaVar.c(j3);
        jaVar.a(this.w);
        jaVar.b(je.a().i());
        jaVar.c(je.a().j());
        jaVar.b(je.a().k());
        jaVar.d(ji.a().c());
        jaVar.e(ji.a().d());
        jaVar.a(i);
        jaVar.b(this.t != -1 ? this.t : lr.j());
        jaVar.f(d());
        jaVar.a(jj.a().e());
        jaVar.c(h());
        jaVar.a(this.r);
        jaVar.a(this.s);
        jaVar.c(g());
        jaVar.a(e());
        jaVar.a(this.z);
        jaVar.b(f());
        jaVar.d(this.C);
        try {
            izVar = new iz(jaVar);
        } catch (IOException e) {
            kg.a(5, f, "Error creating analytics session report: " + e);
            izVar = null;
        }
        if (izVar == null) {
            kg.e(f, "New session report wasn't created");
        }
        return izVar;
    }

    public synchronized void b() {
        this.D++;
    }

    public synchronized FlurryEventRecordStatus a(String str, String str2, Map<String, String> map, boolean z) {
        FlurryEventRecordStatus flurryEventRecordStatus;
        flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventFailed;
        if (map != null) {
            if (!TextUtils.isEmpty(str2)) {
                map.put("sid+Tumblr", str2);
                flurryEventRecordStatus = a(str, (Map) map, false);
                kg.a(5, f, "logEvent status for syndication:" + flurryEventRecordStatus);
            }
        }
        return flurryEventRecordStatus;
    }

    public synchronized FlurryEventRecordStatus a(String str, Map<String, String> map, boolean z) {
        FlurryEventRecordStatus flurryEventRecordStatus;
        FlurryEventRecordStatus flurryEventRecordStatus2 = FlurryEventRecordStatus.kFlurryEventRecorded;
        if (this.E) {
            long elapsedRealtime = SystemClock.elapsedRealtime() - je.a().e();
            final String b = lt.b(str);
            if (b.length() == 0) {
                flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventFailed;
            } else {
                iv ivVar = (iv) this.x.get(b);
                if (ivVar != null) {
                    ivVar.a++;
                    kg.e(f, "Event count incremented: " + b);
                    flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventRecorded;
                } else if (this.x.size() < a) {
                    ivVar = new iv();
                    ivVar.a = 1;
                    this.x.put(b, ivVar);
                    kg.e(f, "Event count started: " + b);
                    flurryEventRecordStatus = flurryEventRecordStatus2;
                } else {
                    kg.e(f, "Too many different events. Event not counted: " + b);
                    flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventUniqueCountExceeded;
                }
                if (!this.p || this.y.size() >= c || this.A >= d) {
                    this.z = false;
                } else {
                    Map emptyMap;
                    if (map == null) {
                        emptyMap = Collections.emptyMap();
                    } else {
                        Map<String, String> map2 = map;
                    }
                    if (emptyMap.size() > b) {
                        kg.e(f, "MaxEventParams exceeded: " + emptyMap.size());
                        flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventParamsCountExceeded;
                    } else {
                        iw iwVar = new iw(o(), b, emptyMap, elapsedRealtime, z);
                        if (iwVar.d() + this.A <= d) {
                            this.y.add(iwVar);
                            this.A = iwVar.d() + this.A;
                            flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventRecorded;
                            if (this.E && hl.a().c() != null) {
                                js.a().b(new Runnable(this) {
                                    final /* synthetic */ jb c;

                                    public void run() {
                                        hl.a().c().a(b, emptyMap);
                                    }
                                });
                            }
                        } else {
                            this.A = d;
                            this.z = false;
                            kg.e(f, "Event Log size exceeded. No more event details logged.");
                            flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventLogCountExceeded;
                        }
                    }
                }
            }
        } else {
            flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventAnalyticsDisabled;
            kg.e(f, "Analytics has been disabled, not logging event.");
        }
        return flurryEventRecordStatus;
    }

    public synchronized void a(String str, Map<String, String> map) {
        for (iw iwVar : this.y) {
            if (iwVar.a(str)) {
                long elapsedRealtime = SystemClock.elapsedRealtime() - je.a().e();
                if (map != null && map.size() > 0 && this.A < d) {
                    int d = this.A - iwVar.d();
                    Map hashMap = new HashMap(iwVar.c());
                    iwVar.a((Map) map);
                    if (iwVar.d() + d > d) {
                        iwVar.b(hashMap);
                        this.z = false;
                        this.A = d;
                        kg.e(f, "Event Log size exceeded. No more event details logged.");
                    } else if (iwVar.c().size() > b) {
                        kg.e(f, "MaxEventParams exceeded on endEvent: " + iwVar.c().size());
                        iwVar.b(hashMap);
                    } else {
                        this.A = d + iwVar.d();
                    }
                }
                iwVar.a(elapsedRealtime);
            }
        }
    }

    public synchronized void a(String str, String str2, String str3, Throwable th) {
        Object obj;
        iu iuVar;
        int i;
        if (str != null) {
            if ("uncaught".equals(str)) {
                obj = 1;
                this.C++;
                if (this.B.size() < e) {
                    iuVar = new iu(p(), Long.valueOf(System.currentTimeMillis()).longValue(), str, str2, str3, th);
                    this.B.add(iuVar);
                    kg.e(f, "Error logged: " + iuVar.c());
                } else if (obj == null) {
                    for (i = 0; i < this.B.size(); i++) {
                        iuVar = (iu) this.B.get(i);
                        if (iuVar.c() == null && !"uncaught".equals(iuVar.c())) {
                            this.B.set(i, new iu(p(), Long.valueOf(System.currentTimeMillis()).longValue(), str, str2, str3, th));
                            break;
                        }
                    }
                } else {
                    kg.e(f, "Max errors logged. No more errors logged.");
                }
            }
        }
        obj = null;
        this.C++;
        if (this.B.size() < e) {
            iuVar = new iu(p(), Long.valueOf(System.currentTimeMillis()).longValue(), str, str2, str3, th);
            this.B.add(iuVar);
            kg.e(f, "Error logged: " + iuVar.c());
        } else if (obj == null) {
            kg.e(f, "Max errors logged. No more errors logged.");
        } else {
            while (i < this.B.size()) {
                iuVar = (iu) this.B.get(i);
                if (iuVar.c() == null) {
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void a(boolean r21, long r22) {
        /*
        r20 = this;
        monitor-enter(r20);
        r0 = r20;
        r2 = r0.E;	 Catch:{ all -> 0x0084 }
        if (r2 != 0) goto L_0x0011;
    L_0x0007:
        r2 = 3;
        r3 = f;	 Catch:{ all -> 0x0084 }
        r4 = "Analytics disabled, not sending agent report.";
        com.flurry.sdk.kg.a(r2, r3, r4);	 Catch:{ all -> 0x0084 }
    L_0x000f:
        monitor-exit(r20);
        return;
    L_0x0011:
        if (r21 != 0) goto L_0x001d;
    L_0x0013:
        r0 = r20;
        r2 = r0.u;	 Catch:{ all -> 0x0084 }
        r2 = r2.isEmpty();	 Catch:{ all -> 0x0084 }
        if (r2 != 0) goto L_0x000f;
    L_0x001d:
        r2 = 3;
        r3 = f;	 Catch:{ all -> 0x0084 }
        r4 = "generating agent report";
        com.flurry.sdk.kg.a(r2, r3, r4);	 Catch:{ all -> 0x0084 }
        r19 = 0;
        r3 = new com.flurry.sdk.ix;	 Catch:{ Exception -> 0x0087 }
        r2 = com.flurry.sdk.js.a();	 Catch:{ Exception -> 0x0087 }
        r4 = r2.d();	 Catch:{ Exception -> 0x0087 }
        r2 = com.flurry.sdk.jo.a();	 Catch:{ Exception -> 0x0087 }
        r5 = r2.e();	 Catch:{ Exception -> 0x0087 }
        r0 = r20;
        r6 = r0.n;	 Catch:{ Exception -> 0x0087 }
        r2 = com.flurry.sdk.jf.a();	 Catch:{ Exception -> 0x0087 }
        r7 = r2.e();	 Catch:{ Exception -> 0x0087 }
        r0 = r20;
        r8 = r0.o;	 Catch:{ Exception -> 0x0087 }
        r0 = r20;
        r12 = r0.u;	 Catch:{ Exception -> 0x0087 }
        r2 = com.flurry.sdk.jf.a();	 Catch:{ Exception -> 0x0087 }
        r13 = r2.h();	 Catch:{ Exception -> 0x0087 }
        r0 = r20;
        r2 = r0.F;	 Catch:{ Exception -> 0x0087 }
        r10 = 0;
        r14 = r2.a(r10);	 Catch:{ Exception -> 0x0087 }
        r0 = r20;
        r15 = r0.v;	 Catch:{ Exception -> 0x0087 }
        r2 = com.flurry.sdk.ju.a();	 Catch:{ Exception -> 0x0087 }
        r16 = r2.c();	 Catch:{ Exception -> 0x0087 }
        r17 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x0087 }
        r10 = r22;
        r3.<init>(r4, r5, r6, r7, r8, r10, r12, r13, r14, r15, r16, r17);	 Catch:{ Exception -> 0x0087 }
        r2 = r3.a();	 Catch:{ Exception -> 0x0087 }
    L_0x0077:
        if (r2 != 0) goto L_0x00a3;
    L_0x0079:
        r2 = f;	 Catch:{ all -> 0x0084 }
        r3 = "Error generating report";
        com.flurry.sdk.kg.e(r2, r3);	 Catch:{ all -> 0x0084 }
    L_0x0080:
        r20.j();	 Catch:{ all -> 0x0084 }
        goto L_0x000f;
    L_0x0084:
        r2 = move-exception;
        monitor-exit(r20);
        throw r2;
    L_0x0087:
        r2 = move-exception;
        r3 = f;	 Catch:{ all -> 0x0084 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0084 }
        r4.<init>();	 Catch:{ all -> 0x0084 }
        r5 = "Exception while generating report: ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0084 }
        r2 = r4.append(r2);	 Catch:{ all -> 0x0084 }
        r2 = r2.toString();	 Catch:{ all -> 0x0084 }
        com.flurry.sdk.kg.e(r3, r2);	 Catch:{ all -> 0x0084 }
        r2 = r19;
        goto L_0x0077;
    L_0x00a3:
        r3 = 3;
        r4 = f;	 Catch:{ all -> 0x0084 }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0084 }
        r5.<init>();	 Catch:{ all -> 0x0084 }
        r6 = "generated report of size ";
        r5 = r5.append(r6);	 Catch:{ all -> 0x0084 }
        r6 = r2.length;	 Catch:{ all -> 0x0084 }
        r5 = r5.append(r6);	 Catch:{ all -> 0x0084 }
        r6 = " with ";
        r5 = r5.append(r6);	 Catch:{ all -> 0x0084 }
        r0 = r20;
        r6 = r0.u;	 Catch:{ all -> 0x0084 }
        r6 = r6.size();	 Catch:{ all -> 0x0084 }
        r5 = r5.append(r6);	 Catch:{ all -> 0x0084 }
        r6 = " reports.";
        r5 = r5.append(r6);	 Catch:{ all -> 0x0084 }
        r5 = r5.toString();	 Catch:{ all -> 0x0084 }
        com.flurry.sdk.kg.a(r3, r4, r5);	 Catch:{ all -> 0x0084 }
        r3 = com.flurry.sdk.hl.a();	 Catch:{ all -> 0x0084 }
        r3 = r3.d();	 Catch:{ all -> 0x0084 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0084 }
        r4.<init>();	 Catch:{ all -> 0x0084 }
        r5 = "";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0084 }
        r5 = com.flurry.sdk.jt.a();	 Catch:{ all -> 0x0084 }
        r4 = r4.append(r5);	 Catch:{ all -> 0x0084 }
        r4 = r4.toString();	 Catch:{ all -> 0x0084 }
        r5 = com.flurry.sdk.js.a();	 Catch:{ all -> 0x0084 }
        r5 = r5.d();	 Catch:{ all -> 0x0084 }
        r3.b(r2, r5, r4);	 Catch:{ all -> 0x0084 }
        goto L_0x0080;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flurry.sdk.jb.a(boolean, long):void");
    }

    public synchronized void c() {
        kg.a(4, f, "Saving persistent agent data.");
        this.m.a(this.u);
    }

    private synchronized void i() {
        kg.a(4, f, "Loading persistent session report data.");
        List list = (List) this.m.a();
        if (list != null) {
            this.u.addAll(list);
        } else if (this.l.exists()) {
            kg.a(4, f, "Legacy persistent agent data found, converting.");
            jc a = ho.a(this.l);
            if (a != null) {
                boolean a2 = a.a();
                long b = a.b();
                if (b <= 0) {
                    b = je.a().d();
                }
                this.n = a2;
                this.o = b;
                n();
                Collection c = a.c();
                if (c != null) {
                    this.u.addAll(c);
                }
            }
            this.l.delete();
            c();
        }
    }

    private void j() {
        this.u.clear();
        this.m.b();
    }

    private String k() {
        return ".flurryagent." + Integer.toString(js.a().d().hashCode(), 16);
    }

    private String l() {
        return ".yflurryreport." + Long.toString(lt.i(js.a().d()), 16);
    }

    private void m() {
        SharedPreferences sharedPreferences = js.a().c().getSharedPreferences("FLURRY_SHARED_PREFERENCES", 0);
        this.n = sharedPreferences.getBoolean("com.flurry.sdk.previous_successful_report", false);
        this.o = sharedPreferences.getLong("com.flurry.sdk.initial_run_time", je.a().d());
    }

    private void n() {
        Editor edit = js.a().c().getSharedPreferences("FLURRY_SHARED_PREFERENCES", 0).edit();
        edit.putBoolean("com.flurry.sdk.previous_successful_report", this.n);
        edit.putLong("com.flurry.sdk.initial_run_time", this.o);
        edit.commit();
    }

    private int o() {
        return this.g.incrementAndGet();
    }

    private int p() {
        return this.h.incrementAndGet();
    }

    String d() {
        return this.q == null ? "" : this.q;
    }

    List<iw> e() {
        return this.y;
    }

    List<iu> f() {
        return this.B;
    }

    Map<String, iv> g() {
        return this.x;
    }

    int h() {
        return this.D;
    }
}
