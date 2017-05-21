package com.amazon.device.iap.internal.b.a;

import com.amazon.device.iap.internal.b.e;
import com.amazon.device.iap.internal.model.PurchaseResponseBuilder;
import com.amazon.device.iap.internal.model.UserDataBuilder;
import com.amazon.device.iap.internal.util.d;
import com.amazon.device.iap.model.PurchaseResponse.RequestStatus;
import com.amazon.device.iap.model.Receipt;
import com.amazon.venezia.command.SuccessResult;
import java.util.Map;
import org.json.JSONObject;

/* compiled from: PurchaseResponseCommandV2 */
public final class a extends c {
    private static final String a = a.class.getSimpleName();

    public a(e eVar) {
        super(eVar, "2.0");
    }

    protected boolean a(SuccessResult successResult) throws Exception {
        Map data = successResult.getData();
        com.amazon.device.iap.internal.util.e.a(a, "data: " + data);
        String str = (String) getCommandData().get("requestId");
        String str2 = (String) data.get("userId");
        String str3 = (String) data.get("marketplace");
        String str4 = (String) data.get("receipt");
        if (d.a(str4)) {
            a(str2, str3, str, RequestStatus.FAILED);
            return false;
        }
        Receipt a;
        JSONObject jSONObject = new JSONObject(str4);
        RequestStatus safeValueOf = RequestStatus.safeValueOf(jSONObject.getString("orderStatus"));
        if (safeValueOf == RequestStatus.SUCCESSFUL) {
            try {
                a = com.amazon.device.iap.internal.util.a.a(jSONObject, str2, str);
            } catch (Throwable th) {
                a(str2, str3, str, RequestStatus.FAILED);
                return false;
            }
        }
        a = null;
        e b = b();
        b.d().a(new PurchaseResponseBuilder().setRequestId(b.c()).setRequestStatus(safeValueOf).setUserData(new UserDataBuilder().setUserId(str2).setMarketplace(str3).build()).setReceipt(a).build());
        return true;
    }
}
