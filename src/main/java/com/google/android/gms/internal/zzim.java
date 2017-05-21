package com.google.android.gms.internal;

import java.util.concurrent.Future;

@zzhb
public abstract class zzim implements zzit<Future> {
    private volatile Thread zzLM;
    private boolean zzLN;
    private final Runnable zzx;

    public zzim() {
        this.zzx = new Runnable(this) {
            final /* synthetic */ zzim zzLO;

            {
                this.zzLO = r1;
            }

            public final void run() {
                this.zzLO.zzLM = Thread.currentThread();
                this.zzLO.zzbr();
            }
        };
        this.zzLN = false;
    }

    public zzim(boolean z) {
        this.zzx = /* anonymous class already generated */;
        this.zzLN = z;
    }

    public final void cancel() {
        onStop();
        if (this.zzLM != null) {
            this.zzLM.interrupt();
        }
    }

    public abstract void onStop();

    public abstract void zzbr();

    public /* synthetic */ Object zzgd() {
        return zzhn();
    }

    public final Future zzhn() {
        return this.zzLN ? zziq.zza(1, this.zzx) : zziq.zza(this.zzx);
    }
}
