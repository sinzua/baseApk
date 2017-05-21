package com.google.android.gms.common.internal;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

final class zzm extends zzl implements Callback {
    private final Handler mHandler;
    private final HashMap<zza, zzb> zzalZ = new HashMap();
    private final com.google.android.gms.common.stats.zzb zzama;
    private final long zzamb;
    private final Context zzsa;

    private static final class zza {
        private final String zzSU;
        private final ComponentName zzamc;

        public zza(ComponentName componentName) {
            this.zzSU = null;
            this.zzamc = (ComponentName) zzx.zzz(componentName);
        }

        public zza(String str) {
            this.zzSU = zzx.zzcM(str);
            this.zzamc = null;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof zza)) {
                return false;
            }
            zza com_google_android_gms_common_internal_zzm_zza = (zza) o;
            return zzw.equal(this.zzSU, com_google_android_gms_common_internal_zzm_zza.zzSU) && zzw.equal(this.zzamc, com_google_android_gms_common_internal_zzm_zza.zzamc);
        }

        public int hashCode() {
            return zzw.hashCode(this.zzSU, this.zzamc);
        }

        public String toString() {
            return this.zzSU == null ? this.zzamc.flattenToString() : this.zzSU;
        }

        public Intent zzqS() {
            return this.zzSU != null ? new Intent(this.zzSU).setPackage("com.google.android.gms") : new Intent().setComponent(this.zzamc);
        }
    }

    private final class zzb {
        private int mState = 2;
        private IBinder zzakD;
        private ComponentName zzamc;
        private final zza zzamd = new zza(this);
        private final Set<ServiceConnection> zzame = new HashSet();
        private boolean zzamf;
        private final zza zzamg;
        final /* synthetic */ zzm zzamh;

        public class zza implements ServiceConnection {
            final /* synthetic */ zzb zzami;

            public zza(zzb com_google_android_gms_common_internal_zzm_zzb) {
                this.zzami = com_google_android_gms_common_internal_zzm_zzb;
            }

            public void onServiceConnected(ComponentName component, IBinder binder) {
                synchronized (this.zzami.zzamh.zzalZ) {
                    this.zzami.zzakD = binder;
                    this.zzami.zzamc = component;
                    for (ServiceConnection onServiceConnected : this.zzami.zzame) {
                        onServiceConnected.onServiceConnected(component, binder);
                    }
                    this.zzami.mState = 1;
                }
            }

            public void onServiceDisconnected(ComponentName component) {
                synchronized (this.zzami.zzamh.zzalZ) {
                    this.zzami.zzakD = null;
                    this.zzami.zzamc = component;
                    for (ServiceConnection onServiceDisconnected : this.zzami.zzame) {
                        onServiceDisconnected.onServiceDisconnected(component);
                    }
                    this.zzami.mState = 2;
                }
            }
        }

        public zzb(zzm com_google_android_gms_common_internal_zzm, zza com_google_android_gms_common_internal_zzm_zza) {
            this.zzamh = com_google_android_gms_common_internal_zzm;
            this.zzamg = com_google_android_gms_common_internal_zzm_zza;
        }

        public IBinder getBinder() {
            return this.zzakD;
        }

        public ComponentName getComponentName() {
            return this.zzamc;
        }

        public int getState() {
            return this.mState;
        }

        public boolean isBound() {
            return this.zzamf;
        }

        public void zza(ServiceConnection serviceConnection, String str) {
            this.zzamh.zzama.zza(this.zzamh.zzsa, serviceConnection, str, this.zzamg.zzqS());
            this.zzame.add(serviceConnection);
        }

        public boolean zza(ServiceConnection serviceConnection) {
            return this.zzame.contains(serviceConnection);
        }

        public void zzb(ServiceConnection serviceConnection, String str) {
            this.zzamh.zzama.zzb(this.zzamh.zzsa, serviceConnection);
            this.zzame.remove(serviceConnection);
        }

        @TargetApi(14)
        public void zzcH(String str) {
            this.mState = 3;
            this.zzamf = this.zzamh.zzama.zza(this.zzamh.zzsa, str, this.zzamg.zzqS(), this.zzamd, 129);
            if (!this.zzamf) {
                this.mState = 2;
                try {
                    this.zzamh.zzama.zza(this.zzamh.zzsa, this.zzamd);
                } catch (IllegalArgumentException e) {
                }
            }
        }

        public void zzcI(String str) {
            this.zzamh.zzama.zza(this.zzamh.zzsa, this.zzamd);
            this.zzamf = false;
            this.mState = 2;
        }

        public boolean zzqT() {
            return this.zzame.isEmpty();
        }
    }

    zzm(Context context) {
        this.zzsa = context.getApplicationContext();
        this.mHandler = new Handler(context.getMainLooper(), this);
        this.zzama = com.google.android.gms.common.stats.zzb.zzrP();
        this.zzamb = 5000;
    }

    private boolean zza(zza com_google_android_gms_common_internal_zzm_zza, ServiceConnection serviceConnection, String str) {
        boolean isBound;
        zzx.zzb((Object) serviceConnection, (Object) "ServiceConnection must not be null");
        synchronized (this.zzalZ) {
            zzb com_google_android_gms_common_internal_zzm_zzb = (zzb) this.zzalZ.get(com_google_android_gms_common_internal_zzm_zza);
            if (com_google_android_gms_common_internal_zzm_zzb != null) {
                this.mHandler.removeMessages(0, com_google_android_gms_common_internal_zzm_zzb);
                if (!com_google_android_gms_common_internal_zzm_zzb.zza(serviceConnection)) {
                    com_google_android_gms_common_internal_zzm_zzb.zza(serviceConnection, str);
                    switch (com_google_android_gms_common_internal_zzm_zzb.getState()) {
                        case 1:
                            serviceConnection.onServiceConnected(com_google_android_gms_common_internal_zzm_zzb.getComponentName(), com_google_android_gms_common_internal_zzm_zzb.getBinder());
                            break;
                        case 2:
                            com_google_android_gms_common_internal_zzm_zzb.zzcH(str);
                            break;
                        default:
                            break;
                    }
                }
                throw new IllegalStateException("Trying to bind a GmsServiceConnection that was already connected before.  config=" + com_google_android_gms_common_internal_zzm_zza);
            }
            com_google_android_gms_common_internal_zzm_zzb = new zzb(this, com_google_android_gms_common_internal_zzm_zza);
            com_google_android_gms_common_internal_zzm_zzb.zza(serviceConnection, str);
            com_google_android_gms_common_internal_zzm_zzb.zzcH(str);
            this.zzalZ.put(com_google_android_gms_common_internal_zzm_zza, com_google_android_gms_common_internal_zzm_zzb);
            isBound = com_google_android_gms_common_internal_zzm_zzb.isBound();
        }
        return isBound;
    }

    private void zzb(zza com_google_android_gms_common_internal_zzm_zza, ServiceConnection serviceConnection, String str) {
        zzx.zzb((Object) serviceConnection, (Object) "ServiceConnection must not be null");
        synchronized (this.zzalZ) {
            zzb com_google_android_gms_common_internal_zzm_zzb = (zzb) this.zzalZ.get(com_google_android_gms_common_internal_zzm_zza);
            if (com_google_android_gms_common_internal_zzm_zzb == null) {
                throw new IllegalStateException("Nonexistent connection status for service config: " + com_google_android_gms_common_internal_zzm_zza);
            } else if (com_google_android_gms_common_internal_zzm_zzb.zza(serviceConnection)) {
                com_google_android_gms_common_internal_zzm_zzb.zzb(serviceConnection, str);
                if (com_google_android_gms_common_internal_zzm_zzb.zzqT()) {
                    this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0, com_google_android_gms_common_internal_zzm_zzb), this.zzamb);
                }
            } else {
                throw new IllegalStateException("Trying to unbind a GmsServiceConnection  that was not bound before.  config=" + com_google_android_gms_common_internal_zzm_zza);
            }
        }
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                zzb com_google_android_gms_common_internal_zzm_zzb = (zzb) msg.obj;
                synchronized (this.zzalZ) {
                    if (com_google_android_gms_common_internal_zzm_zzb.zzqT()) {
                        if (com_google_android_gms_common_internal_zzm_zzb.isBound()) {
                            com_google_android_gms_common_internal_zzm_zzb.zzcI("GmsClientSupervisor");
                        }
                        this.zzalZ.remove(com_google_android_gms_common_internal_zzm_zzb.zzamg);
                    }
                }
                return true;
            default:
                return false;
        }
    }

    public boolean zza(ComponentName componentName, ServiceConnection serviceConnection, String str) {
        return zza(new zza(componentName), serviceConnection, str);
    }

    public boolean zza(String str, ServiceConnection serviceConnection, String str2) {
        return zza(new zza(str), serviceConnection, str2);
    }

    public void zzb(ComponentName componentName, ServiceConnection serviceConnection, String str) {
        zzb(new zza(componentName), serviceConnection, str);
    }

    public void zzb(String str, ServiceConnection serviceConnection, String str2) {
        zzb(new zza(str), serviceConnection, str2);
    }
}
