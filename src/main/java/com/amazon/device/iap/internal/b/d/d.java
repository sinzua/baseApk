package com.amazon.device.iap.internal.b.d;

import com.amazon.device.iap.internal.b.e;
import com.amazon.device.iap.internal.c.c;
import com.amazon.device.iap.internal.model.PurchaseUpdatesResponseBuilder;
import com.amazon.device.iap.internal.model.ReceiptBuilder;
import com.amazon.device.iap.internal.model.UserDataBuilder;
import com.amazon.device.iap.internal.util.a;
import com.amazon.device.iap.model.ProductType;
import com.amazon.device.iap.model.PurchaseUpdatesResponse.RequestStatus;
import com.amazon.device.iap.model.Receipt;
import com.amazon.venezia.command.SuccessResult;
import com.nativex.common.JsonRequestConstants.UnityRewardData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;

/* compiled from: PurchaseUpdatesCommandV1 */
public final class d extends b {
    private static final String b = d.class.getSimpleName();
    private static final Date c = new Date(0);

    public d(e eVar) {
        super(eVar, "1.0", true);
    }

    protected boolean a(SuccessResult successResult) throws Exception {
        String string;
        int i = 0;
        Map data = successResult.getData();
        com.amazon.device.iap.internal.util.e.a(b, "data: " + data);
        String str = (String) data.get("userId");
        String str2 = (String) data.get("requestId");
        str2 = (String) data.get("marketplace");
        List arrayList = new ArrayList();
        JSONArray jSONArray = new JSONArray((String) data.get(UnityRewardData.RECEIPTS));
        for (int i2 = 0; i2 < jSONArray.length(); i2++) {
            try {
                Receipt a = a.a(jSONArray.getJSONObject(i2), str, null);
                arrayList.add(a);
                if (ProductType.ENTITLED == a.getProductType()) {
                    c.a().a(str, a.getReceiptId(), a.getSku());
                }
            } catch (com.amazon.device.iap.internal.b.a e) {
                com.amazon.device.iap.internal.util.e.b(b, "fail to parse receipt, requestId:" + e.a());
            } catch (com.amazon.device.iap.internal.b.d e2) {
                com.amazon.device.iap.internal.util.e.b(b, "fail to verify receipt, requestId:" + e2.a());
            } catch (Throwable th) {
                com.amazon.device.iap.internal.util.e.b(b, "fail to verify receipt, requestId:" + th.getMessage());
            }
        }
        JSONArray jSONArray2 = new JSONArray((String) data.get("revocations"));
        while (i < jSONArray2.length()) {
            try {
                string = jSONArray2.getString(i);
                arrayList.add(new ReceiptBuilder().setSku(string).setProductType(ProductType.ENTITLED).setPurchaseDate(null).setCancelDate(c).setReceiptId(c.a().a(str, string)).build());
            } catch (JSONException e3) {
                com.amazon.device.iap.internal.util.e.b(b, "fail to parse JSON[" + i + "] in \"" + jSONArray2 + "\"");
            }
            i++;
        }
        string = (String) data.get("cursor");
        boolean equalsIgnoreCase = "true".equalsIgnoreCase((String) data.get("hasMore"));
        e b = b();
        Object build = new PurchaseUpdatesResponseBuilder().setRequestId(b.c()).setRequestStatus(RequestStatus.SUCCESSFUL).setUserData(new UserDataBuilder().setUserId(str).setMarketplace(str2).build()).setReceipts(arrayList).setHasMore(equalsIgnoreCase).build();
        build.getReceipts().addAll(com.amazon.device.iap.internal.c.a.a().b(build.getUserData().getUserId()));
        b.d().a(build);
        b.d().a("newCursor", string);
        return true;
    }
}
