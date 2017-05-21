package com.amazon.device.iap.model;

import com.amazon.device.iap.internal.model.ProductDataResponseBuilder;
import com.amazon.device.iap.internal.util.d;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductDataResponse {
    private static final String PRODUCT_DATA = "productData";
    private static final String REQUEST_ID = "requestId";
    private static final String REQUEST_STATUS = "requestStatus";
    private static final String TO_STRING_FORMAT = "(%s, requestId: \"%s\", unavailableSkus: %s, requestStatus: \"%s\", productData: %s)";
    private static final String UNAVAILABLE_SKUS = "UNAVAILABLE_SKUS";
    private final Map<String, Product> productData;
    private final RequestId requestId;
    private final RequestStatus requestStatus;
    private final Set<String> unavailableSkus;

    public enum RequestStatus {
        SUCCESSFUL,
        FAILED,
        NOT_SUPPORTED
    }

    public ProductDataResponse(ProductDataResponseBuilder productDataResponseBuilder) {
        d.a(productDataResponseBuilder.getRequestId(), REQUEST_ID);
        d.a(productDataResponseBuilder.getRequestStatus(), REQUEST_STATUS);
        if (productDataResponseBuilder.getUnavailableSkus() == null) {
            productDataResponseBuilder.setUnavailableSkus(new HashSet());
        }
        if (RequestStatus.SUCCESSFUL == productDataResponseBuilder.getRequestStatus()) {
            d.a(productDataResponseBuilder.getProductData(), PRODUCT_DATA);
        }
        this.requestId = productDataResponseBuilder.getRequestId();
        this.requestStatus = productDataResponseBuilder.getRequestStatus();
        this.unavailableSkus = productDataResponseBuilder.getUnavailableSkus();
        this.productData = productDataResponseBuilder.getProductData();
    }

    public RequestId getRequestId() {
        return this.requestId;
    }

    public Set<String> getUnavailableSkus() {
        return this.unavailableSkus;
    }

    public RequestStatus getRequestStatus() {
        return this.requestStatus;
    }

    public Map<String, Product> getProductData() {
        return this.productData;
    }

    public String toString() {
        String str = TO_STRING_FORMAT;
        Object[] objArr = new Object[5];
        objArr[0] = super.toString();
        objArr[1] = this.requestId;
        objArr[2] = this.unavailableSkus != null ? this.unavailableSkus.toString() : "null";
        objArr[3] = this.requestStatus != null ? this.requestStatus.toString() : "null";
        objArr[4] = this.productData != null ? this.productData.toString() : "null";
        return String.format(str, objArr);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(REQUEST_ID, this.requestId);
        jSONObject.put(UNAVAILABLE_SKUS, this.unavailableSkus);
        jSONObject.put(REQUEST_STATUS, this.requestStatus);
        JSONObject jSONObject2 = new JSONObject();
        if (this.productData != null) {
            for (String str : this.productData.keySet()) {
                jSONObject2.put(str, ((Product) this.productData.get(str)).toJSON());
            }
        }
        jSONObject.put(PRODUCT_DATA, jSONObject2);
        return jSONObject;
    }
}
