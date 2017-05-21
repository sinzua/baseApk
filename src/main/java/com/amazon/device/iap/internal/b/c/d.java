package com.amazon.device.iap.internal.b.c;

import com.amazon.device.iap.internal.b.e;
import com.amazon.device.iap.internal.b.i;
import com.amazon.device.iap.internal.model.ProductDataResponseBuilder;
import com.amazon.device.iap.model.ProductDataResponse;
import com.amazon.device.iap.model.ProductDataResponse.RequestStatus;
import com.amazon.device.iap.model.RequestId;
import java.util.Set;

/* compiled from: GetProductDataRequest */
public final class d extends e {
    public d(RequestId requestId, Set<String> set) {
        super(requestId);
        i aVar = new a(this, set);
        aVar.b(new b(this, set));
        a(aVar);
    }

    public void a() {
        a((Object) (ProductDataResponse) d().a());
    }

    public void b() {
        Object obj = (ProductDataResponse) d().a();
        if (obj == null) {
            obj = new ProductDataResponseBuilder().setRequestId(c()).setRequestStatus(RequestStatus.FAILED).build();
        }
        a(obj);
    }
}
