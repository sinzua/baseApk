package com.flurry.sdk;

import com.flurry.sdk.kp.c;
import java.io.InputStream;
import java.io.OutputStream;

public class kn<RequestObjectType, ResponseObjectType> extends kp {
    private a<RequestObjectType, ResponseObjectType> a;
    private RequestObjectType b;
    private ResponseObjectType c;
    private lb<RequestObjectType> d;
    private lb<ResponseObjectType> e;

    public interface a<RequestObjectType, ResponseObjectType> {
        void a(kn<RequestObjectType, ResponseObjectType> knVar, ResponseObjectType responseObjectType);
    }

    public void a(RequestObjectType requestObjectType) {
        this.b = requestObjectType;
    }

    public void a(lb<RequestObjectType> lbVar) {
        this.d = lbVar;
    }

    public void b(lb<ResponseObjectType> lbVar) {
        this.e = lbVar;
    }

    public void a(a<RequestObjectType, ResponseObjectType> aVar) {
        this.a = aVar;
    }

    public void a() {
        r();
        super.a();
    }

    private void r() {
        a(new c(this) {
            final /* synthetic */ kn a;

            {
                this.a = r1;
            }

            public void a(kp kpVar, OutputStream outputStream) throws Exception {
                if (this.a.b != null && this.a.d != null) {
                    this.a.d.a(outputStream, this.a.b);
                }
            }

            public void a(kp kpVar, InputStream inputStream) throws Exception {
                if (kpVar.g() && this.a.e != null) {
                    this.a.c = this.a.e.b(inputStream);
                }
            }

            public void a(kp kpVar) {
                this.a.s();
            }
        });
    }

    private void s() {
        if (this.a != null && !e()) {
            this.a.a(this, this.c);
        }
    }
}
