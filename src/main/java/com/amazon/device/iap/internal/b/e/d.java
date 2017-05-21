package com.amazon.device.iap.internal.b.e;

import android.os.RemoteException;
import com.amazon.android.framework.exception.KiwiException;
import com.amazon.device.iap.internal.b.e;
import com.amazon.device.iap.internal.model.UserDataBuilder;
import com.amazon.device.iap.internal.model.UserDataResponseBuilder;
import com.amazon.device.iap.model.UserData;
import com.amazon.device.iap.model.UserDataResponse.RequestStatus;
import com.amazon.venezia.command.SuccessResult;
import java.util.Map;

/* compiled from: GetUserIdCommandV1 */
public final class d extends b {
    private static final String b = d.class.getSimpleName();

    public d(e eVar) {
        super(eVar, "1.0");
    }

    protected boolean a(SuccessResult successResult) throws RemoteException, KiwiException {
        com.amazon.device.iap.internal.util.e.a(b, "onSuccessInternal: result = " + successResult);
        Map data = successResult.getData();
        com.amazon.device.iap.internal.util.e.a(b, "data: " + data);
        String str = (String) data.get("userId");
        e b = b();
        if (com.amazon.device.iap.internal.util.d.a(str)) {
            b.d().a(new UserDataResponseBuilder().setRequestId(b.c()).setRequestStatus(RequestStatus.FAILED).build());
            return false;
        }
        UserData build = new UserDataBuilder().setUserId(str).setMarketplace(a).build();
        Object build2 = new UserDataResponseBuilder().setRequestId(b.c()).setRequestStatus(RequestStatus.SUCCESSFUL).setUserData(build).build();
        b.d().a("userId", build.getUserId());
        b.d().a(build2);
        return true;
    }
}
