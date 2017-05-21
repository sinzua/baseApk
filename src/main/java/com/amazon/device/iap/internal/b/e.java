package com.amazon.device.iap.internal.b;

import android.content.Context;
import android.os.Handler;
import com.amazon.device.iap.PurchasingListener;
import com.amazon.device.iap.internal.util.b;
import com.amazon.device.iap.internal.util.d;
import com.amazon.device.iap.model.ProductDataResponse;
import com.amazon.device.iap.model.PurchaseResponse;
import com.amazon.device.iap.model.PurchaseUpdatesResponse;
import com.amazon.device.iap.model.RequestId;
import com.amazon.device.iap.model.UserDataResponse;

/* compiled from: KiwiRequest */
public class e {
    private static final String a = e.class.getSimpleName();
    private final RequestId b;
    private final h c = new h();
    private i d = null;

    public e(RequestId requestId) {
        this.b = requestId;
    }

    protected void a(i iVar) {
        this.d = iVar;
    }

    protected void a(Object obj) {
        a(obj, null);
    }

    protected void a(final Object obj, final i iVar) {
        d.a(obj, "response");
        Context b = com.amazon.device.iap.internal.d.d().b();
        final PurchasingListener a = com.amazon.device.iap.internal.d.d().a();
        if (b == null || a == null) {
            com.amazon.device.iap.internal.util.e.a(a, "PurchasingListener is not set. Dropping response: " + obj);
            return;
        }
        new Handler(b.getMainLooper()).post(new Runnable(this) {
            final /* synthetic */ e d;

            public void run() {
                this.d.d().a("notifyListenerResult", Boolean.FALSE);
                try {
                    if (obj instanceof ProductDataResponse) {
                        a.onProductDataResponse((ProductDataResponse) obj);
                    } else if (obj instanceof UserDataResponse) {
                        a.onUserDataResponse((UserDataResponse) obj);
                    } else if (obj instanceof PurchaseUpdatesResponse) {
                        PurchaseUpdatesResponse purchaseUpdatesResponse = (PurchaseUpdatesResponse) obj;
                        a.onPurchaseUpdatesResponse(purchaseUpdatesResponse);
                        Object a = this.d.d().a("newCursor");
                        if (a != null && (a instanceof String)) {
                            b.a(purchaseUpdatesResponse.getUserData().getUserId(), a.toString());
                        }
                    } else if (obj instanceof PurchaseResponse) {
                        a.onPurchaseResponse((PurchaseResponse) obj);
                    } else {
                        com.amazon.device.iap.internal.util.e.b(e.a, "Unknown response type:" + obj.getClass().getName());
                    }
                    this.d.d().a("notifyListenerResult", Boolean.TRUE);
                } catch (Throwable th) {
                    com.amazon.device.iap.internal.util.e.b(e.a, "Error in sendResponse: " + th);
                }
                if (iVar != null) {
                    iVar.a(true);
                    iVar.a_();
                }
            }
        });
    }

    public RequestId c() {
        return this.b;
    }

    public h d() {
        return this.c;
    }

    public void e() {
        if (this.d != null) {
            this.d.a_();
        } else {
            a();
        }
    }

    public void a() {
    }

    public void b() {
    }
}
