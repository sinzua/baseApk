package com.amazon.device.iap.internal.b.a;

import com.amazon.device.iap.internal.b.e;
import com.amazon.device.iap.internal.c.a;
import com.amazon.device.iap.internal.model.PurchaseResponseBuilder;
import com.amazon.device.iap.internal.model.UserDataBuilder;
import com.amazon.device.iap.internal.util.d;
import com.amazon.device.iap.model.ProductType;
import com.amazon.device.iap.model.PurchaseResponse.RequestStatus;
import com.amazon.device.iap.model.Receipt;
import com.amazon.venezia.command.SuccessResult;
import java.util.Map;
import org.json.JSONObject;

/* compiled from: PurchaseResponseCommandV1 */
public final class b extends c {
    private static final String a = b.class.getSimpleName();

    public b(e eVar) {
        super(eVar, "1.0");
    }

    private void a(String str, String str2, String str3) {
        if (str != null && str2 != null && str3 != null) {
            try {
                JSONObject jSONObject = new JSONObject(str3);
                if (RequestStatus.safeValueOf(jSONObject.getString("orderStatus")) == RequestStatus.SUCCESSFUL) {
                    a.a().a(str, str2, com.amazon.device.iap.internal.util.a.a(jSONObject, str2, str).getReceiptId(), str3);
                }
            } catch (Throwable th) {
                com.amazon.device.iap.internal.util.e.b(a, "Error in savePendingReceipt: " + th);
            }
        }
    }

    protected boolean a(SuccessResult successResult) throws Exception {
        Map data = successResult.getData();
        com.amazon.device.iap.internal.util.e.a(a, "data: " + data);
        String str = (String) getCommandData().get("requestId");
        String str2 = (String) data.get("userId");
        String str3 = (String) data.get("marketplace");
        String str4 = (String) data.get("receipt");
        if (str == null || !com.amazon.device.iap.internal.c.b.a().a(str)) {
            b().d().b();
            return true;
        } else if (d.a(str4)) {
            a(str2, str3, str, RequestStatus.FAILED);
            return false;
        } else {
            Receipt receipt;
            JSONObject jSONObject = new JSONObject(str4);
            RequestStatus safeValueOf = RequestStatus.safeValueOf(jSONObject.getString("orderStatus"));
            if (safeValueOf == RequestStatus.SUCCESSFUL) {
                try {
                    Receipt a = com.amazon.device.iap.internal.util.a.a(jSONObject, str2, str);
                    if (ProductType.CONSUMABLE == a.getProductType()) {
                        a(str, str2, str4);
                    }
                    receipt = a;
                } catch (Throwable th) {
                    a(str2, str3, str, RequestStatus.FAILED);
                    return false;
                }
            }
            receipt = null;
            e b = b();
            b.d().a(new PurchaseResponseBuilder().setRequestId(b.c()).setRequestStatus(safeValueOf).setUserData(new UserDataBuilder().setUserId(str2).setMarketplace(str3).build()).setReceipt(receipt).build());
            return true;
        }
    }
}
