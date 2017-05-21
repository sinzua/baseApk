package com.flurry.sdk;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import bolts.MeasurementEvent;
import com.flurry.sdk.ll.a;
import com.supersonicads.sdk.precache.DownloadManager;
import com.supersonicads.sdk.utils.Constants.ControllerParameters;
import com.ty.followboom.helpers.VLTools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ig implements a {
    private static final String a = ig.class.getSimpleName();
    private static String b = "https://proton.flurry.com/sdk/v1/config";
    private final Runnable c = new ly(this) {
        final /* synthetic */ ig a;

        {
            this.a = r1;
        }

        public void a() {
            this.a.g();
        }
    };
    private final kb<jg> d = new kb<jg>(this) {
        final /* synthetic */ ig a;

        {
            this.a = r1;
        }

        public void a(jg jgVar) {
            this.a.g();
        }
    };
    private final kb<jh> e = new kb<jh>(this) {
        final /* synthetic */ ig a;

        {
            this.a = r1;
        }

        public void a(jh jhVar) {
            this.a.g();
        }
    };
    private final kb<jk> f = new kb<jk>(this) {
        final /* synthetic */ ig a;

        {
            this.a = r1;
        }

        public void a(jk jkVar) {
            if (jkVar.a) {
                this.a.g();
            }
        }
    };
    private final kk<hs> g = new kk("proton config request", new is());
    private final kk<ht> h = new kk("proton config response", new it());
    private jz<ie> i;
    private jz<List<in>> j;
    private final if k = new if();
    private final jx<String, hw> l = new jx();
    private final List<in> m = new ArrayList();
    private boolean n;
    private String o;
    private boolean p = true;
    private boolean q;
    private long r = VLTools.DEFAULT_RATE_US_THREHOLD;
    private long s;
    private boolean t;
    private ht u;
    private boolean v;

    public ig() {
        ll a = lk.a();
        this.n = ((Boolean) a.a("ProtonEnabled")).booleanValue();
        a.a("ProtonEnabled", (a) this);
        kg.a(4, a, "initSettings, protonEnabled = " + this.n);
        this.o = (String) a.a("ProtonConfigUrl");
        a.a("ProtonConfigUrl", (a) this);
        kg.a(4, a, "initSettings, protonConfigUrl = " + this.o);
        this.p = ((Boolean) a.a("analyticsEnabled")).booleanValue();
        a.a("analyticsEnabled", (a) this);
        kg.a(4, a, "initSettings, AnalyticsEnabled = " + this.p);
        kc.a().a("com.flurry.android.sdk.IdProviderFinishedEvent", this.d);
        kc.a().a("com.flurry.android.sdk.IdProviderUpdatedAdvertisingId", this.e);
        kc.a().a("com.flurry.android.sdk.NetworkStateEvent", this.f);
        this.i = new jz(js.a().c().getFileStreamPath(o()), ".yflurryprotonconfig.", 1, new le<ie>(this) {
            final /* synthetic */ ig a;

            {
                this.a = r1;
            }

            public lb<ie> a(int i) {
                return new ie.a();
            }
        });
        this.j = new jz(js.a().c().getFileStreamPath(p()), ".yflurryprotonreport.", 1, new le<List<in>>(this) {
            final /* synthetic */ ig a;

            {
                this.a = r1;
            }

            public lb<List<in>> a(int i) {
                return new la(new in.a());
            }
        });
        js.a().b(new ly(this) {
            final /* synthetic */ ig a;

            {
                this.a = r1;
            }

            public void a() {
                this.a.j();
            }
        });
        js.a().b(new ly(this) {
            final /* synthetic */ ig a;

            {
                this.a = r1;
            }

            public void a() {
                this.a.l();
            }
        });
    }

    public void a() {
        js.a().c(this.c);
        kc.a().b("com.flurry.android.sdk.NetworkStateEvent", this.f);
        kc.a().b("com.flurry.android.sdk.IdProviderUpdatedAdvertisingId", this.e);
        kc.a().b("com.flurry.android.sdk.IdProviderFinishedEvent", this.d);
        im.b();
        lk.a().b("ProtonEnabled", (a) this);
    }

    public void a(String str, Object obj) {
        Object obj2 = -1;
        switch (str.hashCode()) {
            case -1720015653:
                if (str.equals("analyticsEnabled")) {
                    obj2 = 2;
                    break;
                }
                break;
            case 640941243:
                if (str.equals("ProtonEnabled")) {
                    obj2 = null;
                    break;
                }
                break;
            case 1591403975:
                if (str.equals("ProtonConfigUrl")) {
                    obj2 = 1;
                    break;
                }
                break;
        }
        switch (obj2) {
            case null:
                this.n = ((Boolean) obj).booleanValue();
                kg.a(4, a, "onSettingUpdate, protonEnabled = " + this.n);
                return;
            case 1:
                this.o = (String) obj;
                kg.a(4, a, "onSettingUpdate, protonConfigUrl = " + this.o);
                return;
            case 2:
                this.p = ((Boolean) obj).booleanValue();
                kg.a(4, a, "onSettingUpdate, AnalyticsEnabled = " + this.p);
                return;
            default:
                kg.a(6, a, "onSettingUpdate internal error!");
                return;
        }
    }

    public synchronized void b() {
        if (this.n) {
            lt.b();
            ii.a = je.a().d();
            this.v = false;
            g();
        }
    }

    public synchronized void c() {
        if (this.n) {
            lt.b();
            b(je.a().d());
            k();
        }
    }

    public synchronized void a(long j) {
        if (this.n) {
            lt.b();
            b(j);
            b("flurry.session_end", null);
            js.a().b(new ly(this) {
                final /* synthetic */ ig a;

                {
                    this.a = r1;
                }

                public void a() {
                    this.a.m();
                }
            });
        }
    }

    public synchronized void d() {
        if (this.n) {
            lt.b();
            k();
        }
    }

    public synchronized void a(String str, Map<String, String> map) {
        if (this.n) {
            lt.b();
            b(str, (Map) map);
        }
    }

    private synchronized void f() {
        if (this.n) {
            lt.b();
            SharedPreferences sharedPreferences = js.a().c().getSharedPreferences("FLURRY_SHARED_PREFERENCES", 0);
            if (sharedPreferences.getBoolean("com.flurry.android.flurryAppInstall", true)) {
                b("flurry.app_install", null);
                Editor edit = sharedPreferences.edit();
                edit.putBoolean("com.flurry.android.flurryAppInstall", false);
                edit.apply();
            }
        }
    }

    private synchronized void g() {
        if (this.n) {
            lt.b();
            if (this.q && jf.a().c()) {
                boolean z;
                final long currentTimeMillis = System.currentTimeMillis();
                if (jf.a().e()) {
                    z = false;
                } else {
                    z = true;
                }
                if (this.u != null) {
                    if (this.t != z) {
                        kg.a(3, a, "Limit ad tracking value has changed, purging");
                        this.u = null;
                    } else if (System.currentTimeMillis() < this.s + (this.u.b * 1000)) {
                        kg.a(3, a, "Cached Proton config valid, no need to refresh");
                        if (!this.v) {
                            this.v = true;
                            b("flurry.session_start", null);
                        }
                    } else if (System.currentTimeMillis() >= this.s + (this.u.c * 1000)) {
                        kg.a(3, a, "Cached Proton config expired, purging");
                        this.u = null;
                        this.l.a();
                    }
                }
                jq.a().a((Object) this);
                kg.a(3, a, "Requesting proton config");
                Object h = h();
                if (h != null) {
                    lz knVar = new kn();
                    knVar.a(TextUtils.isEmpty(this.o) ? b : this.o);
                    knVar.d(DownloadManager.OPERATION_TIMEOUT);
                    knVar.a(kp.a.kPost);
                    knVar.a("Content-Type", "application/x-flurry;version=2");
                    knVar.a("Accept", "application/x-flurry;version=2");
                    knVar.a("FM-Checksum", Integer.toString(kk.c(h)));
                    knVar.a(new kx());
                    knVar.b(new kx());
                    knVar.a(h);
                    knVar.a(new kn.a<byte[], byte[]>(this) {
                        final /* synthetic */ ig c;

                        public void a(kn<byte[], byte[]> knVar, final byte[] bArr) {
                            Map map = null;
                            int h = knVar.h();
                            kg.a(3, ig.a, "Proton config request: HTTP status code is:" + h);
                            if (h == 400 || h == 406 || h == 412 || h == 415) {
                                this.c.r = VLTools.DEFAULT_RATE_US_THREHOLD;
                                return;
                            }
                            if (knVar.f() && bArr != null) {
                                ht htVar;
                                js.a().b(new ly(this) {
                                    final /* synthetic */ AnonymousClass2 b;

                                    public void a() {
                                        this.b.c.a(currentTimeMillis, z, bArr);
                                    }
                                });
                                try {
                                    htVar = (ht) this.c.h.d(bArr);
                                } catch (Exception e) {
                                    kg.a(5, ig.a, "Failed to decode proton config response: " + e);
                                    htVar = null;
                                }
                                if (!this.c.a(htVar)) {
                                    htVar = null;
                                }
                                if (htVar != null) {
                                    this.c.r = VLTools.DEFAULT_RATE_US_THREHOLD;
                                    this.c.s = currentTimeMillis;
                                    this.c.t = z;
                                    this.c.u = htVar;
                                    this.c.i();
                                    if (!this.c.v) {
                                        this.c.v = true;
                                        this.c.b("flurry.session_start", null);
                                    }
                                    this.c.f();
                                }
                                map = htVar;
                            }
                            if (map == null) {
                                long parseLong;
                                long i = this.c.r << 1;
                                if (h == 429) {
                                    List b = knVar.b("Retry-After");
                                    if (!b.isEmpty()) {
                                        String str = (String) b.get(0);
                                        kg.a(3, ig.a, "Server returned retry time: " + str);
                                        try {
                                            parseLong = Long.parseLong(str) * 1000;
                                        } catch (NumberFormatException e2) {
                                            kg.a(3, ig.a, "Server returned nonsensical retry time");
                                        }
                                        this.c.r = parseLong;
                                        kg.a(3, ig.a, "Proton config request failed, backing off: " + this.c.r + "ms");
                                        js.a().b(this.c.c, this.c.r);
                                    }
                                }
                                parseLong = i;
                                this.c.r = parseLong;
                                kg.a(3, ig.a, "Proton config request failed, backing off: " + this.c.r + "ms");
                                js.a().b(this.c.c, this.c.r);
                            }
                        }
                    });
                    jq.a().a((Object) this, knVar);
                }
            }
        }
    }

    private byte[] h() {
        try {
            Object hsVar = new hs();
            hsVar.a = js.a().d();
            hsVar.b = lq.c(js.a().c());
            hsVar.c = lq.d(js.a().c());
            hsVar.d = jt.a();
            hsVar.e = 3;
            hsVar.f = jo.a().c();
            hsVar.g = !jf.a().e();
            hsVar.h = new hv();
            hsVar.h.a = new hp();
            hsVar.h.a.a = Build.MODEL;
            hsVar.h.a.b = Build.BRAND;
            hsVar.h.a.c = Build.ID;
            hsVar.h.a.d = Build.DEVICE;
            hsVar.h.a.e = Build.PRODUCT;
            hsVar.h.a.f = VERSION.RELEASE;
            hsVar.i = new ArrayList();
            for (Entry entry : jf.a().h().entrySet()) {
                hu huVar = new hu();
                huVar.a = ((jn) entry.getKey()).d;
                if (((jn) entry.getKey()).e) {
                    huVar.b = new String((byte[]) entry.getValue());
                } else {
                    huVar.b = lt.b((byte[]) entry.getValue());
                }
                hsVar.i.add(huVar);
            }
            Location e = jj.a().e();
            if (e != null) {
                hsVar.j = new hz();
                hsVar.j.a = new hy();
                hsVar.j.a.a = lt.a(e.getLatitude(), 3);
                hsVar.j.a.b = lt.a(e.getLongitude(), 3);
                hsVar.j.a.c = (float) lt.a((double) e.getAccuracy(), 3);
            }
            String str = (String) lk.a().a("UserId");
            if (!str.equals("")) {
                hsVar.k = new ic();
                hsVar.k.a = str;
            }
            return this.g.a(hsVar);
        } catch (Exception e2) {
            kg.a(5, a, "Proton config request failed with exception: " + e2);
            return null;
        }
    }

    private boolean a(ht htVar) {
        if (htVar == null) {
            return false;
        }
        if (a(htVar.e) && !htVar.e.e.equals("")) {
            return true;
        }
        kg.a(3, a, "Config response is missing required values.");
        return false;
    }

    private boolean a(hr hrVar) {
        if (hrVar == null) {
            return true;
        }
        if (hrVar.a == null) {
            return true;
        }
        int i = 0;
        while (i < hrVar.a.size()) {
            hq hqVar = (hq) hrVar.a.get(i);
            if (hqVar == null || !(hqVar.b.equals("") || hqVar.a == -1 || hqVar.e.equals("") || !a(hqVar.c))) {
                i++;
            } else {
                kg.a(3, a, "A callback template is missing required values");
                return false;
            }
        }
        return true;
    }

    private boolean a(List<hw> list) {
        if (list == null) {
            return true;
        }
        for (hw hwVar : list) {
            if (hwVar.a.equals("")) {
                kg.a(3, a, "An event is missing a name");
                return false;
            } else if ((hwVar instanceof hx) && ((hx) hwVar).c.equals("")) {
                kg.a(3, a, "An event trigger is missing a param name");
                return false;
            }
        }
        return true;
    }

    private void i() {
        if (this.u != null) {
            kg.a(5, a, "Processing config response");
            im.a(this.u.e.c);
            im.b(this.u.e.d * ControllerParameters.SECOND);
            io.a().a(this.u.e.e);
            if (this.n) {
                lk.a().a("analyticsEnabled", (Object) Boolean.valueOf(this.u.f.b));
            }
            this.l.a();
            hr hrVar = this.u.e;
            if (hrVar != null) {
                List<hq> list = hrVar.a;
                if (list != null) {
                    for (hq hqVar : list) {
                        if (hqVar != null) {
                            List<Object> list2 = hqVar.c;
                            if (list2 != null) {
                                for (Object obj : list2) {
                                    if (!(obj == null || TextUtils.isEmpty(obj.a))) {
                                        obj.b = hqVar;
                                        this.l.a(obj.a, obj);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private synchronized void b(String str, Map<String, String> map) {
        kg.a(3, a, "Event triggered: " + str);
        if (!this.p) {
            kg.e(a, "Analytics and pulse have been disabled.");
        } else if (this.u == null) {
            kg.a(3, a, "Config response is empty. No events to fire.");
        } else {
            lt.b();
            if (!TextUtils.isEmpty(str)) {
                List<hw> a = this.l.a((Object) str);
                if (a == null) {
                    kg.a(3, a, "No events to fire. Returning.");
                } else if (a.size() == 0) {
                    kg.a(3, a, "No events to fire. Returning.");
                } else {
                    ir irVar;
                    long currentTimeMillis = System.currentTimeMillis();
                    boolean z = map != null;
                    Object obj = -1;
                    switch (str.hashCode()) {
                        case 645204782:
                            if (str.equals("flurry.session_end")) {
                                obj = 1;
                                break;
                            }
                            break;
                        case 1371447545:
                            if (str.equals("flurry.app_install")) {
                                obj = 2;
                                break;
                            }
                            break;
                        case 1579613685:
                            if (str.equals("flurry.session_start")) {
                                obj = null;
                                break;
                            }
                            break;
                    }
                    switch (obj) {
                        case null:
                            irVar = ir.SESSION_START;
                            break;
                        case 1:
                            irVar = ir.SESSION_END;
                            break;
                        case 2:
                            irVar = ir.INSTALL;
                            break;
                        default:
                            irVar = ir.APPLICATION_EVENT;
                            break;
                    }
                    Map hashMap = new HashMap();
                    for (hw hwVar : a) {
                        Object obj2 = null;
                        if (hwVar instanceof hx) {
                            kg.a(4, a, "Event contains triggers.");
                            String[] strArr = ((hx) hwVar).d;
                            if (strArr == null) {
                                kg.a(4, a, "Template does not contain trigger values. Firing.");
                                obj2 = 1;
                            } else if (strArr.length == 0) {
                                kg.a(4, a, "Template does not contain trigger values. Firing.");
                                obj2 = 1;
                            } else if (map == null) {
                                kg.a(4, a, "Publisher has not passed in params list. Not firing.");
                            }
                            String str2 = (String) map.get(((hx) hwVar).c);
                            if (str2 == null) {
                                kg.a(4, a, "Publisher params has no value associated with proton key. Not firing.");
                            } else {
                                Object obj3;
                                int i = 0;
                                while (i < strArr.length) {
                                    if (strArr[i].equals(str2)) {
                                        obj3 = 1;
                                        if (obj3 != null) {
                                            kg.a(4, a, "Publisher params list does not match proton param values. Not firing.");
                                        } else {
                                            kg.a(4, a, "Publisher params match proton values. Firing.");
                                        }
                                    } else {
                                        i++;
                                    }
                                }
                                obj3 = obj2;
                                if (obj3 != null) {
                                    kg.a(4, a, "Publisher params match proton values. Firing.");
                                } else {
                                    kg.a(4, a, "Publisher params list does not match proton param values. Not firing.");
                                }
                            }
                        }
                        hq hqVar = hwVar.b;
                        if (hqVar == null) {
                            kg.a(3, a, "Template is empty. Not firing current event.");
                        } else {
                            kg.a(3, a, "Creating callback report for partner: " + hqVar.b);
                            Map hashMap2 = new HashMap();
                            hashMap2.put(MeasurementEvent.MEASUREMENT_EVENT_NAME_KEY, str);
                            hashMap2.put("event_time_millis", String.valueOf(currentTimeMillis));
                            String a2 = this.k.a(hqVar.e, hashMap2);
                            String str3 = null;
                            if (hqVar.f != null) {
                                str3 = this.k.a(hqVar.f, hashMap2);
                            }
                            hashMap.put(Long.valueOf(hqVar.a), new ij(hqVar.b, hqVar.a, a2, System.currentTimeMillis() + 259200000, this.u.e.b, hqVar.g, hqVar.d, hqVar.j, hqVar.i, hqVar.h, str3));
                        }
                    }
                    if (hashMap.size() != 0) {
                        in inVar = new in(str, z, je.a().d(), je.a().g(), irVar, hashMap);
                        if ("flurry.session_end".equals(str)) {
                            kg.a(3, a, "Storing Pulse callbacks for event: " + str);
                            this.m.add(inVar);
                        } else {
                            kg.a(3, a, "Firing Pulse callbacks for event: " + str);
                            im.a().a(inVar);
                        }
                    }
                }
            }
        }
    }

    private synchronized void b(long j) {
        Iterator it = this.m.iterator();
        while (it.hasNext()) {
            if (j == ((in) it.next()).b()) {
                it.remove();
            }
        }
    }

    private synchronized void j() {
        ie ieVar = (ie) this.i.a();
        if (ieVar != null) {
            ht htVar;
            try {
                htVar = (ht) this.h.d(ieVar.c());
            } catch (Exception e) {
                kg.a(5, a, "Failed to decode saved proton config response: " + e);
                this.i.b();
                htVar = null;
            }
            if (!a(htVar)) {
                htVar = null;
            }
            if (htVar != null) {
                kg.a(4, a, "Loaded saved proton config response");
                this.r = VLTools.DEFAULT_RATE_US_THREHOLD;
                this.s = ieVar.a();
                this.t = ieVar.b();
                this.u = htVar;
                i();
            }
        }
        this.q = true;
        js.a().b(new ly(this) {
            final /* synthetic */ ig a;

            {
                this.a = r1;
            }

            public void a() {
                this.a.g();
            }
        });
    }

    private synchronized void a(long j, boolean z, byte[] bArr) {
        if (bArr != null) {
            kg.a(4, a, "Saving proton config response");
            ie ieVar = new ie();
            ieVar.a(j);
            ieVar.a(z);
            ieVar.a(bArr);
            this.i.a(ieVar);
        }
    }

    private synchronized void k() {
        if (this.p) {
            kg.a(4, a, "Sending " + this.m.size() + " queued reports.");
            for (in inVar : this.m) {
                kg.a(3, a, "Firing Pulse callbacks for event: " + inVar.c());
                im.a().a(inVar);
            }
            n();
        } else {
            kg.e(a, "Analytics disabled, not sending pulse reports.");
        }
    }

    private synchronized void l() {
        kg.a(4, a, "Loading queued report data.");
        List list = (List) this.j.a();
        if (list != null) {
            this.m.addAll(list);
        }
    }

    private synchronized void m() {
        kg.a(4, a, "Saving queued report data.");
        this.j.a(this.m);
    }

    private synchronized void n() {
        this.m.clear();
        this.j.b();
    }

    private String o() {
        return ".yflurryprotonconfig." + Long.toString(lt.i(js.a().d()), 16);
    }

    private String p() {
        return ".yflurryprotonreport." + Long.toString(lt.i(js.a().d()), 16);
    }
}
