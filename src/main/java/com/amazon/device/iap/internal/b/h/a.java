package com.amazon.device.iap.internal.b.h;

import android.os.RemoteException;
import com.amazon.android.framework.exception.KiwiException;
import com.amazon.device.iap.internal.b.e;
import com.amazon.device.iap.internal.b.i;
import com.amazon.venezia.command.SuccessResult;

/* compiled from: SubmitMetricCommand */
public class a extends i {
    public a(e eVar, String str, String str2) {
        super(eVar, "submit_metric", "1.0");
        a("metricName", str);
        a("metricAttributes", str2);
        b(false);
    }

    protected boolean a(SuccessResult successResult) throws RemoteException, KiwiException {
        return true;
    }
}
