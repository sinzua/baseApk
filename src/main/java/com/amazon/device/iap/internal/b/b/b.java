package com.amazon.device.iap.internal.b.b;

import com.amazon.android.framework.exception.KiwiException;
import com.amazon.device.iap.internal.b.e;

/* compiled from: PurchaseItemCommandV1 */
public final class b extends a {
    public b(e eVar, String str) {
        super(eVar, "1.0", str);
    }

    protected void preExecution() throws KiwiException {
        super.preExecution();
        com.amazon.device.iap.internal.c.b.a().b(c());
    }
}
