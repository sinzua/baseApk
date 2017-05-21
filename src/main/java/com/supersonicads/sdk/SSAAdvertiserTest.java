package com.supersonicads.sdk;

import android.content.Context;

public interface SSAAdvertiserTest extends SSAAdvertiser {
    void clearReportApp(Context context);

    void setDomain(String str, String str2, int i);

    void setPackageName(String str);

    void setTimeAPI(String str);
}
