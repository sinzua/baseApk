package com.amazon.device.iap.internal;

import android.content.Context;
import android.content.Intent;
import com.amazon.device.iap.model.FulfillmentResult;
import com.amazon.device.iap.model.RequestId;
import java.util.Set;

/* compiled from: RequestHandler */
public interface c {
    void a(Context context, Intent intent);

    void a(RequestId requestId);

    void a(RequestId requestId, String str);

    void a(RequestId requestId, String str, FulfillmentResult fulfillmentResult);

    void a(RequestId requestId, Set<String> set);

    void a(RequestId requestId, boolean z);
}
