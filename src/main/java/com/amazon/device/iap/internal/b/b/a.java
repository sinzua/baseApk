package com.amazon.device.iap.internal.b.b;

import android.app.Activity;
import android.content.Intent;
import android.os.RemoteException;
import com.amazon.android.framework.context.ContextManager;
import com.amazon.android.framework.exception.KiwiException;
import com.amazon.android.framework.resource.Resource;
import com.amazon.android.framework.task.Task;
import com.amazon.android.framework.task.TaskManager;
import com.amazon.android.framework.task.pipeline.TaskPipelineId;
import com.amazon.device.iap.internal.b.e;
import com.amazon.device.iap.internal.b.i;
import com.amazon.device.iap.internal.util.MetricsHelper;
import com.amazon.venezia.command.SuccessResult;
import java.util.Map;

/* compiled from: PurchaseItemCommandBase */
abstract class a extends i {
    private static final String d = a.class.getSimpleName();
    @Resource
    protected TaskManager a;
    @Resource
    protected ContextManager b;
    protected final String c;

    a(e eVar, String str, String str2) {
        super(eVar, "purchase_item", str);
        this.c = str2;
        a("sku", this.c);
    }

    protected boolean a(SuccessResult successResult) throws RemoteException, KiwiException {
        Map data = successResult.getData();
        com.amazon.device.iap.internal.util.e.a(d, "data: " + data);
        if (data.containsKey("purchaseItemIntent")) {
            com.amazon.device.iap.internal.util.e.a(d, "found intent");
            final Intent intent = (Intent) data.remove("purchaseItemIntent");
            this.a.enqueueAtFront(TaskPipelineId.FOREGROUND, new Task(this) {
                final /* synthetic */ a b;

                public void execute() {
                    try {
                        Activity visible = this.b.b.getVisible();
                        if (visible == null) {
                            visible = this.b.b.getRoot();
                        }
                        com.amazon.device.iap.internal.util.e.a(a.d, "About to fire intent with activity " + visible);
                        visible.startActivity(intent);
                    } catch (Exception e) {
                        MetricsHelper.submitExceptionMetrics(this.b.c(), a.d + ".onResult().execute()", e);
                        com.amazon.device.iap.internal.util.e.b(a.d, "Exception when attempting to fire intent: " + e);
                    }
                }
            });
            return true;
        }
        com.amazon.device.iap.internal.util.e.b(d, "did not find intent");
        return false;
    }
}
