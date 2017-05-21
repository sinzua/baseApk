package com.amazon.device.iap.internal.b.f;

import com.amazon.device.iap.internal.b.e;
import com.amazon.device.iap.internal.b.i;
import com.amazon.venezia.command.SuccessResult;

/* compiled from: ResponseReceivedCommandBase */
abstract class a extends i {
    a(e eVar, String str) {
        super(eVar, "response_received", str);
        b(false);
    }

    protected boolean a(SuccessResult successResult) throws Exception {
        return true;
    }
}
