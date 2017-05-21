package com.amazon.device.iap.internal.b.a;

import com.amazon.device.iap.internal.b.e;
import com.amazon.device.iap.internal.b.i;
import com.amazon.device.iap.internal.model.PurchaseResponseBuilder;
import com.amazon.device.iap.internal.model.UserDataBuilder;
import com.amazon.device.iap.model.PurchaseResponse.RequestStatus;

/* compiled from: PurchaseResponseCommandBase */
abstract class c extends i {
    c(e eVar, String str) {
        super(eVar, "purchase_response", str);
    }

    protected void a(String str, String str2, String str3, RequestStatus requestStatus) {
        e b = b();
        b.d().a(new PurchaseResponseBuilder().setRequestId(b.c()).setRequestStatus(requestStatus).setUserData(new UserDataBuilder().setUserId(str).setMarketplace(str2).build()).setReceipt(null).build());
    }
}
