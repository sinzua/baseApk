package com.amazon.device.iap.internal.b.g;

import android.os.RemoteException;
import com.amazon.android.framework.exception.KiwiException;
import com.amazon.device.iap.internal.b.e;
import com.amazon.device.iap.internal.b.i;
import com.amazon.venezia.command.SuccessResult;
import java.util.Set;

/* compiled from: PurchaseFulfilledCommandV2 */
public final class a extends i {
    protected final Set<String> a;
    protected final String b;

    public a(e eVar, Set<String> set, String str) {
        super(eVar, "purchase_fulfilled", "2.0");
        this.a = set;
        this.b = str;
        b(false);
        a("receiptIds", this.a);
        a("fulfillmentStatus", this.b);
    }

    protected boolean a(SuccessResult successResult) throws RemoteException, KiwiException {
        return true;
    }

    public void a_() {
        Object a = b().d().a("notifyListenerResult");
        if (a != null && Boolean.FALSE.equals(a)) {
            a("fulfillmentStatus", com.amazon.device.iap.internal.model.a.DELIVERY_ATTEMPTED.toString());
        }
        super.a_();
    }
}
