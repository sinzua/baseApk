package com.amazon.device.iap.model;

import com.amazon.device.iap.internal.model.PurchaseUpdatesResponseBuilder;
import com.amazon.device.iap.internal.util.d;
import com.nativex.common.JsonRequestConstants.UnityRewardData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class PurchaseUpdatesResponse {
    private static final String HAS_MORE = "HAS_MORE";
    private static final String RECEIPTS = "RECEIPTS";
    private static final String REQUEST_ID = "REQUEST_ID";
    private static final String REQUEST_STATUS = "REQUEST_STATUS";
    private static final String TO_STRING_FORMAT = "(%s, requestId: \"%s\", requestStatus: \"%s\", userData: \"%s\", receipts: %s, hasMore: \"%b\")";
    private static final String USER_DATA = "USER_DATA";
    private final boolean hasMore;
    private final List<Receipt> receipts;
    private final RequestId requestId;
    private final RequestStatus requestStatus;
    private final UserData userData;

    public enum RequestStatus {
        SUCCESSFUL,
        FAILED,
        NOT_SUPPORTED
    }

    public PurchaseUpdatesResponse(PurchaseUpdatesResponseBuilder purchaseUpdatesResponseBuilder) {
        d.a(purchaseUpdatesResponseBuilder.getRequestId(), "requestId");
        d.a(purchaseUpdatesResponseBuilder.getRequestStatus(), "requestStatus");
        if (RequestStatus.SUCCESSFUL == purchaseUpdatesResponseBuilder.getRequestStatus()) {
            d.a(purchaseUpdatesResponseBuilder.getUserData(), "userData");
            d.a(purchaseUpdatesResponseBuilder.getReceipts(), UnityRewardData.RECEIPTS);
        }
        this.requestId = purchaseUpdatesResponseBuilder.getRequestId();
        this.requestStatus = purchaseUpdatesResponseBuilder.getRequestStatus();
        this.userData = purchaseUpdatesResponseBuilder.getUserData();
        this.receipts = purchaseUpdatesResponseBuilder.getReceipts() == null ? new ArrayList() : purchaseUpdatesResponseBuilder.getReceipts();
        this.hasMore = purchaseUpdatesResponseBuilder.hasMore();
    }

    public RequestId getRequestId() {
        return this.requestId;
    }

    public UserData getUserData() {
        return this.userData;
    }

    public RequestStatus getRequestStatus() {
        return this.requestStatus;
    }

    public List<Receipt> getReceipts() {
        return this.receipts;
    }

    public boolean hasMore() {
        return this.hasMore;
    }

    public String toString() {
        String str = TO_STRING_FORMAT;
        Object[] objArr = new Object[6];
        objArr[0] = super.toString();
        objArr[1] = this.requestId;
        objArr[2] = this.requestStatus;
        objArr[3] = this.userData;
        objArr[4] = this.receipts != null ? Arrays.toString(this.receipts.toArray()) : "null";
        objArr[5] = Boolean.valueOf(this.hasMore);
        return String.format(str, objArr);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(REQUEST_ID, this.requestId);
        jSONObject.put(REQUEST_STATUS, this.requestStatus);
        jSONObject.put(USER_DATA, this.userData != null ? this.userData.toJSON() : "");
        JSONArray jSONArray = new JSONArray();
        if (this.receipts != null) {
            for (Receipt toJSON : this.receipts) {
                jSONArray.put(toJSON.toJSON());
            }
        }
        jSONObject.put(RECEIPTS, jSONArray);
        jSONObject.put(HAS_MORE, this.hasMore);
        return jSONObject;
    }
}
