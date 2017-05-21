package com.amazon.device.iap.internal.b.g;

import com.amazon.device.iap.internal.b.e;
import com.amazon.device.iap.internal.c.a;
import com.amazon.device.iap.model.FulfillmentResult;
import com.amazon.device.iap.model.RequestId;
import java.util.HashSet;
import java.util.Set;

/* compiled from: NotifyFulfillmentRequest */
public final class b extends e {
    private final String a;
    private final FulfillmentResult b;

    public b(RequestId requestId, String str, FulfillmentResult fulfillmentResult) {
        super(requestId);
        Set hashSet = new HashSet();
        hashSet.add(str);
        this.a = str;
        this.b = fulfillmentResult;
        a(new a(this, hashSet, fulfillmentResult.toString()));
    }

    public void a() {
    }

    public void b() {
        if (FulfillmentResult.FULFILLED == this.b || FulfillmentResult.UNAVAILABLE == this.b) {
            String c = a.a().c(this.a);
            if (c != null) {
                new com.amazon.device.iap.internal.b.f.b(this, c).a_();
                a.a().a(this.a);
            }
        }
    }
}
