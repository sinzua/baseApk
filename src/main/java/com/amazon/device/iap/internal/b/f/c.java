package com.amazon.device.iap.internal.b.f;

import com.amazon.device.iap.internal.b.e;

/* compiled from: ResponseReceivedCommandV2 */
public final class c extends a {
    public c(e eVar, boolean z) {
        super(eVar, "2.0");
        a("receiptDelivered", Boolean.valueOf(z));
    }

    public void a_() {
        Object a = b().d().a("notifyListenerResult");
        if (a == null || !Boolean.TRUE.equals(a)) {
            a("notifyListenerSucceeded", Boolean.valueOf(false));
        } else {
            a("notifyListenerSucceeded", Boolean.valueOf(true));
        }
        super.a_();
    }
}
