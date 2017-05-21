package com.amazon.device.iap.internal.b.d;

import com.amazon.device.iap.internal.b.e;
import com.amazon.device.iap.internal.b.e.c;
import com.amazon.device.iap.internal.b.e.d;
import com.amazon.device.iap.internal.b.i;
import com.amazon.device.iap.internal.model.PurchaseUpdatesResponseBuilder;
import com.amazon.device.iap.model.PurchaseUpdatesResponse;
import com.amazon.device.iap.model.PurchaseUpdatesResponse.RequestStatus;
import com.amazon.device.iap.model.Receipt;
import com.amazon.device.iap.model.RequestId;
import java.util.HashSet;
import java.util.Set;

/* compiled from: GetPurchaseUpdatesRequest */
public final class a extends e {
    public a(RequestId requestId, boolean z) {
        super(requestId);
        i cVar = new c(this);
        cVar.a(new c(this, z));
        i dVar = new d(this);
        dVar.a(new d(this));
        cVar.b(dVar);
        a(cVar);
    }

    public void a() {
        i iVar = null;
        PurchaseUpdatesResponse purchaseUpdatesResponse = (PurchaseUpdatesResponse) d().a();
        if (purchaseUpdatesResponse.getReceipts() != null && purchaseUpdatesResponse.getReceipts().size() > 0) {
            Set hashSet = new HashSet();
            for (Receipt receipt : purchaseUpdatesResponse.getReceipts()) {
                if (!com.amazon.device.iap.internal.util.d.a(receipt.getReceiptId())) {
                    hashSet.add(receipt.getReceiptId());
                }
            }
            iVar = new com.amazon.device.iap.internal.b.g.a(this, hashSet, com.amazon.device.iap.internal.model.a.DELIVERED.toString());
        }
        a(purchaseUpdatesResponse, iVar);
    }

    public void b() {
        Object a = d().a();
        if (a == null || !(a instanceof PurchaseUpdatesResponse)) {
            a = new PurchaseUpdatesResponseBuilder().setRequestId(c()).setRequestStatus(RequestStatus.FAILED).build();
        } else {
            PurchaseUpdatesResponse purchaseUpdatesResponse = (PurchaseUpdatesResponse) a;
        }
        a(a);
    }
}
