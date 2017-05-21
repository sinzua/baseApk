package com.google.android.gms.ads.internal.formats;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzcj.zza;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzir;
import com.google.android.gms.internal.zzjk;
import com.google.android.gms.internal.zzjp;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

@zzhb
public class zzk extends zza implements OnClickListener, OnTouchListener, OnGlobalLayoutListener, OnScrollChangedListener {
    private FrameLayout zzoQ;
    private final Object zzpV = new Object();
    private final FrameLayout zzyD;
    private Map<String, WeakReference<View>> zzyE = new HashMap();
    private zzb zzyF;
    boolean zzyG = false;
    int zzyH;
    int zzyI;
    private zzh zzyf;

    public zzk(FrameLayout frameLayout, FrameLayout frameLayout2) {
        this.zzyD = frameLayout;
        this.zzoQ = frameLayout2;
        zzjk.zza(this.zzyD, (OnGlobalLayoutListener) this);
        zzjk.zza(this.zzyD, (OnScrollChangedListener) this);
        this.zzyD.setOnTouchListener(this);
    }

    public void destroy() {
        this.zzoQ.removeAllViews();
        this.zzoQ = null;
        this.zzyE = null;
        this.zzyF = null;
        this.zzyf = null;
    }

    int getMeasuredHeight() {
        return this.zzyD.getMeasuredHeight();
    }

    int getMeasuredWidth() {
        return this.zzyD.getMeasuredWidth();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onClick(android.view.View r10) {
        /*
        r9 = this;
        r6 = r9.zzpV;
        monitor-enter(r6);
        r0 = r9.zzyf;	 Catch:{ all -> 0x0091 }
        if (r0 != 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r6);	 Catch:{ all -> 0x0091 }
    L_0x0008:
        return;
    L_0x0009:
        r3 = new org.json.JSONObject;	 Catch:{ all -> 0x0091 }
        r3.<init>();	 Catch:{ all -> 0x0091 }
        r0 = r9.zzyE;	 Catch:{ all -> 0x0091 }
        r0 = r0.entrySet();	 Catch:{ all -> 0x0091 }
        r2 = r0.iterator();	 Catch:{ all -> 0x0091 }
    L_0x0018:
        r0 = r2.hasNext();	 Catch:{ all -> 0x0091 }
        if (r0 == 0) goto L_0x0094;
    L_0x001e:
        r0 = r2.next();	 Catch:{ all -> 0x0091 }
        r0 = (java.util.Map.Entry) r0;	 Catch:{ all -> 0x0091 }
        r1 = r0.getValue();	 Catch:{ all -> 0x0091 }
        r1 = (java.lang.ref.WeakReference) r1;	 Catch:{ all -> 0x0091 }
        r1 = r1.get();	 Catch:{ all -> 0x0091 }
        r1 = (android.view.View) r1;	 Catch:{ all -> 0x0091 }
        r4 = r9.zzj(r1);	 Catch:{ all -> 0x0091 }
        r5 = new org.json.JSONObject;	 Catch:{ all -> 0x0091 }
        r5.<init>();	 Catch:{ all -> 0x0091 }
        r7 = "width";
        r8 = r1.getWidth();	 Catch:{ JSONException -> 0x0073 }
        r8 = r9.zzq(r8);	 Catch:{ JSONException -> 0x0073 }
        r5.put(r7, r8);	 Catch:{ JSONException -> 0x0073 }
        r7 = "height";
        r1 = r1.getHeight();	 Catch:{ JSONException -> 0x0073 }
        r1 = r9.zzq(r1);	 Catch:{ JSONException -> 0x0073 }
        r5.put(r7, r1);	 Catch:{ JSONException -> 0x0073 }
        r1 = "x";
        r7 = r4.x;	 Catch:{ JSONException -> 0x0073 }
        r7 = r9.zzq(r7);	 Catch:{ JSONException -> 0x0073 }
        r5.put(r1, r7);	 Catch:{ JSONException -> 0x0073 }
        r1 = "y";
        r4 = r4.y;	 Catch:{ JSONException -> 0x0073 }
        r4 = r9.zzq(r4);	 Catch:{ JSONException -> 0x0073 }
        r5.put(r1, r4);	 Catch:{ JSONException -> 0x0073 }
        r1 = r0.getKey();	 Catch:{ JSONException -> 0x0073 }
        r1 = (java.lang.String) r1;	 Catch:{ JSONException -> 0x0073 }
        r3.put(r1, r5);	 Catch:{ JSONException -> 0x0073 }
        goto L_0x0018;
    L_0x0073:
        r1 = move-exception;
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0091 }
        r1.<init>();	 Catch:{ all -> 0x0091 }
        r4 = "Unable to get view rectangle for view ";
        r1 = r1.append(r4);	 Catch:{ all -> 0x0091 }
        r0 = r0.getKey();	 Catch:{ all -> 0x0091 }
        r0 = (java.lang.String) r0;	 Catch:{ all -> 0x0091 }
        r0 = r1.append(r0);	 Catch:{ all -> 0x0091 }
        r0 = r0.toString();	 Catch:{ all -> 0x0091 }
        com.google.android.gms.ads.internal.util.client.zzb.zzaK(r0);	 Catch:{ all -> 0x0091 }
        goto L_0x0018;
    L_0x0091:
        r0 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x0091 }
        throw r0;
    L_0x0094:
        r4 = new org.json.JSONObject;	 Catch:{ all -> 0x0091 }
        r4.<init>();	 Catch:{ all -> 0x0091 }
        r0 = "x";
        r1 = r9.zzyH;	 Catch:{ JSONException -> 0x00e8 }
        r1 = r9.zzq(r1);	 Catch:{ JSONException -> 0x00e8 }
        r4.put(r0, r1);	 Catch:{ JSONException -> 0x00e8 }
        r0 = "y";
        r1 = r9.zzyI;	 Catch:{ JSONException -> 0x00e8 }
        r1 = r9.zzq(r1);	 Catch:{ JSONException -> 0x00e8 }
        r4.put(r0, r1);	 Catch:{ JSONException -> 0x00e8 }
    L_0x00af:
        r5 = new org.json.JSONObject;	 Catch:{ all -> 0x0091 }
        r5.<init>();	 Catch:{ all -> 0x0091 }
        r0 = "width";
        r1 = r9.getMeasuredWidth();	 Catch:{ JSONException -> 0x00ef }
        r1 = r9.zzq(r1);	 Catch:{ JSONException -> 0x00ef }
        r5.put(r0, r1);	 Catch:{ JSONException -> 0x00ef }
        r0 = "height";
        r1 = r9.getMeasuredHeight();	 Catch:{ JSONException -> 0x00ef }
        r1 = r9.zzq(r1);	 Catch:{ JSONException -> 0x00ef }
        r5.put(r0, r1);	 Catch:{ JSONException -> 0x00ef }
    L_0x00ce:
        r0 = r9.zzyF;	 Catch:{ all -> 0x0091 }
        if (r0 == 0) goto L_0x00f6;
    L_0x00d2:
        r0 = r9.zzyF;	 Catch:{ all -> 0x0091 }
        r0 = r0.zzdI();	 Catch:{ all -> 0x0091 }
        r0 = r0.equals(r10);	 Catch:{ all -> 0x0091 }
        if (r0 == 0) goto L_0x00f6;
    L_0x00de:
        r0 = r9.zzyf;	 Catch:{ all -> 0x0091 }
        r1 = "1007";
        r0.zza(r1, r3, r4, r5);	 Catch:{ all -> 0x0091 }
    L_0x00e5:
        monitor-exit(r6);	 Catch:{ all -> 0x0091 }
        goto L_0x0008;
    L_0x00e8:
        r0 = move-exception;
        r0 = "Unable to get click location";
        com.google.android.gms.ads.internal.util.client.zzb.zzaK(r0);	 Catch:{ all -> 0x0091 }
        goto L_0x00af;
    L_0x00ef:
        r0 = move-exception;
        r0 = "Unable to get native ad view bounding box";
        com.google.android.gms.ads.internal.util.client.zzb.zzaK(r0);	 Catch:{ all -> 0x0091 }
        goto L_0x00ce;
    L_0x00f6:
        r0 = r9.zzyf;	 Catch:{ all -> 0x0091 }
        r2 = r9.zzyE;	 Catch:{ all -> 0x0091 }
        r1 = r10;
        r0.zza(r1, r2, r3, r4, r5);	 Catch:{ all -> 0x0091 }
        goto L_0x00e5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.ads.internal.formats.zzk.onClick(android.view.View):void");
    }

    public void onGlobalLayout() {
        synchronized (this.zzpV) {
            if (this.zzyG) {
                int measuredWidth = getMeasuredWidth();
                int measuredHeight = getMeasuredHeight();
                if (!(measuredWidth == 0 || measuredHeight == 0)) {
                    this.zzoQ.setLayoutParams(new LayoutParams(measuredWidth, measuredHeight));
                    this.zzyG = false;
                }
            }
            if (this.zzyf != null) {
                this.zzyf.zzh(this.zzyD);
            }
        }
    }

    public void onScrollChanged() {
        synchronized (this.zzpV) {
            if (this.zzyf != null) {
                this.zzyf.zzh(this.zzyD);
            }
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        synchronized (this.zzpV) {
            if (this.zzyf == null) {
            } else {
                Point zzc = zzc(motionEvent);
                this.zzyH = zzc.x;
                this.zzyI = zzc.y;
                MotionEvent obtain = MotionEvent.obtain(motionEvent);
                obtain.setLocation((float) zzc.x, (float) zzc.y);
                this.zzyf.zzb(obtain);
                obtain.recycle();
            }
        }
        return false;
    }

    public zzd zzK(String str) {
        zzd zzC;
        synchronized (this.zzpV) {
            Object obj;
            WeakReference weakReference = (WeakReference) this.zzyE.get(str);
            if (weakReference == null) {
                obj = null;
            } else {
                View view = (View) weakReference.get();
            }
            zzC = zze.zzC(obj);
        }
        return zzC;
    }

    public void zza(zzd com_google_android_gms_dynamic_zzd) {
        synchronized (this.zzpV) {
            this.zzyG = true;
            zzi(null);
            Object zzp = zze.zzp(com_google_android_gms_dynamic_zzd);
            if (zzp instanceof zzi) {
                final zzi com_google_android_gms_ads_internal_formats_zzi = (zzi) zzp;
                if ((this.zzyf instanceof zzg) && ((zzg) this.zzyf).zzdP()) {
                    ((zzg) this.zzyf).zzc(com_google_android_gms_ads_internal_formats_zzi);
                } else {
                    this.zzyf = com_google_android_gms_ads_internal_formats_zzi;
                    if (this.zzyf instanceof zzg) {
                        ((zzg) this.zzyf).zzc(null);
                    }
                }
                this.zzoQ.removeAllViews();
                this.zzyF = zzd(com_google_android_gms_ads_internal_formats_zzi);
                if (this.zzyF != null) {
                    this.zzyE.put("1007", new WeakReference(this.zzyF.zzdI()));
                    this.zzoQ.addView(this.zzyF);
                }
                zzir.zzMc.post(new Runnable(this) {
                    final /* synthetic */ zzk zzyK;

                    public void run() {
                        zzjp zzdR = com_google_android_gms_ads_internal_formats_zzi.zzdR();
                        if (zzdR != null) {
                            this.zzyK.zzoQ.addView(zzdR.getView());
                        }
                    }
                });
                com_google_android_gms_ads_internal_formats_zzi.zzg(this.zzyD);
                zzi(this.zzyD);
                return;
            }
            zzb.zzaK("Not an instance of native engine. This is most likely a transient error");
        }
    }

    public void zza(String str, zzd com_google_android_gms_dynamic_zzd) {
        View view = (View) zze.zzp(com_google_android_gms_dynamic_zzd);
        synchronized (this.zzpV) {
            if (view == null) {
                this.zzyE.remove(str);
            } else {
                this.zzyE.put(str, new WeakReference(view));
                view.setOnTouchListener(this);
                view.setOnClickListener(this);
            }
        }
    }

    Point zzc(MotionEvent motionEvent) {
        int[] iArr = new int[2];
        this.zzyD.getLocationOnScreen(iArr);
        return new Point((int) (motionEvent.getRawX() - ((float) iArr[0])), (int) (motionEvent.getRawY() - ((float) iArr[1])));
    }

    zzb zzd(zzi com_google_android_gms_ads_internal_formats_zzi) {
        return com_google_android_gms_ads_internal_formats_zzi.zza((OnClickListener) this);
    }

    void zzi(View view) {
        if (this.zzyf != null) {
            zzh zzdQ = this.zzyf instanceof zzg ? ((zzg) this.zzyf).zzdQ() : this.zzyf;
            if (zzdQ != null) {
                zzdQ.zzi(view);
            }
        }
    }

    Point zzj(View view) {
        if (this.zzyF == null || !this.zzyF.zzdI().equals(view)) {
            Point point = new Point();
            view.getGlobalVisibleRect(new Rect(), point);
            return point;
        }
        Point point2 = new Point();
        this.zzyD.getGlobalVisibleRect(new Rect(), point2);
        Point point3 = new Point();
        view.getGlobalVisibleRect(new Rect(), point3);
        return new Point(point3.x - point2.x, point3.y - point2.y);
    }

    int zzq(int i) {
        return zzn.zzcS().zzc(this.zzyf.getContext(), i);
    }
}
