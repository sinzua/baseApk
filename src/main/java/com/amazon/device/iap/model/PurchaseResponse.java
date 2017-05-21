package com.amazon.device.iap.model;

import com.amazon.device.iap.internal.model.PurchaseResponseBuilder;
import com.amazon.device.iap.internal.util.d;
import org.json.JSONException;
import org.json.JSONObject;

public final class PurchaseResponse {
    private static final String RECEIPT = "receipt";
    private static final String REQUEST_ID = "requestId";
    private static final String REQUEST_STATUS = "requestStatus";
    private static final String TO_STRING_FORMAT = "(%s, requestId: \"%s\", purchaseRequestStatus: \"%s\", userId: \"%s\", receipt: %s)";
    private static final String USER_DATA = "userData";
    private final Receipt receipt;
    private final RequestId requestId;
    private final RequestStatus requestStatus;
    private final UserData userData;

    public enum RequestStatus {
        SUCCESSFUL,
        FAILED,
        INVALID_SKU,
        ALREADY_PURCHASED,
        NOT_SUPPORTED;

        public static RequestStatus safeValueOf(String str) {
            if (d.a(str)) {
                return null;
            }
            if ("ALREADY_ENTITLED".equalsIgnoreCase(str)) {
                return ALREADY_PURCHASED;
            }
            return valueOf(str.toUpperCase());
        }
    }

    public PurchaseResponse(PurchaseResponseBuilder purchaseResponseBuilder) {
        d.a(purchaseResponseBuilder.getRequestId(), REQUEST_ID);
        d.a(purchaseResponseBuilder.getRequestStatus(), REQUEST_STATUS);
        if (purchaseResponseBuilder.getRequestStatus() == RequestStatus.SUCCESSFUL) {
            d.a(purchaseResponseBuilder.getReceipt(), RECEIPT);
            d.a(purchaseResponseBuilder.getUserData(), USER_DATA);
        }
        this.requestId = purchaseResponseBuilder.getRequestId();
        this.userData = purchaseResponseBuilder.getUserData();
        this.receipt = purchaseResponseBuilder.getReceipt();
        this.requestStatus = purchaseResponseBuilder.getRequestStatus();
    }

    public RequestId getRequestId() {
        return this.requestId;
    }

    public UserData getUserData() {
        return this.userData;
    }

    public Receipt getReceipt() {
        return this.receipt;
    }

    public RequestStatus getRequestStatus() {
        return this.requestStatus;
    }

    public String toString() {
        String str = TO_STRING_FORMAT;
        Object[] objArr = new Object[5];
        objArr[0] = super.toString();
        objArr[1] = this.requestId;
        objArr[2] = this.requestStatus != null ? this.requestStatus.toString() : "null";
        objArr[3] = this.userData;
        objArr[4] = this.receipt;
        return String.format(str, objArr);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(REQUEST_ID, this.requestId);
        jSONObject.put(REQUEST_STATUS, this.requestStatus);
        jSONObject.put(USER_DATA, this.userData != null ? this.userData.toJSON() : "");
        jSONObject.put(RECEIPT, getReceipt() != null ? getReceipt().toJSON() : "");
        return jSONObject;
    }
}
