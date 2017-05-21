package com.amazon.device.iap.internal.b.b;

import com.amazon.device.iap.internal.b.e;
import com.amazon.device.iap.internal.b.i;
import com.amazon.device.iap.internal.model.PurchaseResponseBuilder;
import com.amazon.device.iap.model.PurchaseResponse;
import com.amazon.device.iap.model.PurchaseResponse.RequestStatus;
import com.amazon.device.iap.model.RequestId;

/* compiled from: PurchaseRequest */
public final class d extends e {
    public d(RequestId requestId, String str) {
        super(requestId);
        i cVar = new c(this, str);
        cVar.b(new b(this, str));
        a(cVar);
    }

    public void a() {
    }

    public void b() {
        Object obj = (PurchaseResponse) d().a();
        if (obj == null) {
            obj = new PurchaseResponseBuilder().setRequestId(c()).setRequestStatus(RequestStatus.FAILED).build();
        }
        a(obj);
    }
}
