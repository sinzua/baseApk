package com.amazon.device.iap.internal.b.e;

import com.amazon.device.iap.internal.b.e;
import com.amazon.device.iap.internal.b.i;
import com.amazon.device.iap.internal.model.UserDataResponseBuilder;
import com.amazon.device.iap.model.RequestId;
import com.amazon.device.iap.model.UserDataResponse;
import com.amazon.device.iap.model.UserDataResponse.RequestStatus;

/* compiled from: GetUserDataRequest */
public final class a extends e {
    public a(RequestId requestId) {
        super(requestId);
        i cVar = new c(this);
        cVar.b(new d(this));
        a(cVar);
    }

    public void a() {
        a((Object) (UserDataResponse) d().a());
    }

    public void b() {
        Object obj = (UserDataResponse) d().a();
        if (obj == null) {
            obj = new UserDataResponseBuilder().setRequestId(c()).setRequestStatus(RequestStatus.FAILED).build();
        }
        a(obj);
    }
}
