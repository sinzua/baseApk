package com.amazon.device.iap.internal.b;

import android.os.RemoteException;
import com.amazon.android.Kiwi;
import com.amazon.android.framework.exception.KiwiException;
import com.amazon.android.framework.prompt.PromptContent;
import com.amazon.android.framework.task.command.AbstractCommandTask;
import com.amazon.android.licensing.LicenseFailurePromptContentMapper;
import com.amazon.device.iap.PurchasingService;
import com.amazon.device.iap.internal.util.d;
import com.amazon.device.iap.internal.util.e;
import com.amazon.venezia.command.FailureResult;
import com.amazon.venezia.command.SuccessResult;
import java.util.HashMap;
import java.util.Map;

/* compiled from: KiwiCommand */
public abstract class i extends AbstractCommandTask {
    private static final String a = i.class.getSimpleName();
    private final e b;
    private final String c;
    private final String d;
    private final String e;
    private final Map<String, Object> f;
    private final LicenseFailurePromptContentMapper g = new LicenseFailurePromptContentMapper();
    private boolean h;
    private i i;
    private i j;
    private boolean k = false;

    protected abstract boolean a(SuccessResult successResult) throws Exception;

    public i(e eVar, String str, String str2) {
        this.b = eVar;
        this.c = eVar.c().toString();
        this.d = str;
        this.e = str2;
        this.f = new HashMap();
        this.f.put("requestId", this.c);
        this.f.put("sdkVersion", PurchasingService.SDK_VERSION);
        this.h = true;
        this.i = null;
        this.j = null;
    }

    public i a(boolean z) {
        this.k = z;
        return this;
    }

    public void a(i iVar) {
        this.i = iVar;
    }

    public void b(i iVar) {
        this.j = iVar;
    }

    protected void a(String str, Object obj) {
        this.f.put(str, obj);
    }

    protected e b() {
        return this.b;
    }

    protected String c() {
        return this.c;
    }

    protected boolean isExecutionNeeded() {
        return true;
    }

    protected String getCommandName() {
        return this.d;
    }

    protected String getCommandVersion() {
        return this.e;
    }

    protected Map<String, Object> getCommandData() {
        return this.f;
    }

    protected void b(boolean z) {
        this.h = z;
    }

    private void a(PromptContent promptContent) {
        if (promptContent != null) {
            Kiwi.getPromptManager().present(new b(promptContent));
        }
    }

    protected final void onSuccess(SuccessResult successResult) throws RemoteException {
        String str = (String) successResult.getData().get("errorMessage");
        e.a(a, "onSuccess: result = " + successResult + ", errorMessage: " + str);
        if (d.a(str)) {
            boolean z = false;
            try {
                z = a(successResult);
            } catch (Exception e) {
                e.b(a, "Error calling onResult: " + e);
            }
            if (z && this.i != null) {
                this.i.a_();
            } else if (!this.k) {
                if (z) {
                    this.b.a();
                } else {
                    this.b.b();
                }
            }
        } else if (!this.k) {
            this.b.b();
        }
    }

    protected final void onFailure(FailureResult failureResult) throws RemoteException, KiwiException {
        Object obj;
        e.a(a, "onFailure: result = " + failureResult);
        if (failureResult != null) {
            String str = (String) failureResult.getExtensionData().get("maxVersion");
            if (str != null && str.equalsIgnoreCase("1.0")) {
                obj = 1;
                if (obj != null || this.j == null) {
                    if (this.h) {
                        a(new PromptContent(failureResult.getDisplayableName(), failureResult.getDisplayableMessage(), failureResult.getButtonLabel(), failureResult.show()));
                    }
                    if (!this.k) {
                        this.b.b();
                    }
                }
                this.j.a(this.k);
                this.j.a_();
                return;
            }
        }
        obj = null;
        if (obj != null) {
        }
        if (this.h) {
            a(new PromptContent(failureResult.getDisplayableName(), failureResult.getDisplayableMessage(), failureResult.getButtonLabel(), failureResult.show()));
        }
        if (!this.k) {
            this.b.b();
        }
    }

    protected final void onException(KiwiException kiwiException) {
        e.a(a, "onException: exception = " + kiwiException.getMessage());
        if ("UNHANDLED_EXCEPTION".equals(kiwiException.getType()) && "2.0".equals(this.e) && this.j != null) {
            this.j.a(this.k);
            this.j.a_();
            return;
        }
        if (this.h) {
            a(this.g.map(kiwiException));
        }
        if (!this.k) {
            this.b.b();
        }
    }

    public void a_() {
        Kiwi.addCommandToCommandTaskPipeline(this);
    }
}
