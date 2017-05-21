package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.ads.mediation.AdUrlAdapter;
import com.google.ads.mediation.MediationAdapter;
import com.google.ads.mediation.MediationServerParameters;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.mediation.NetworkExtras;
import com.google.android.gms.ads.mediation.customevent.CustomEvent;
import com.google.android.gms.ads.mediation.customevent.CustomEventAdapter;
import com.google.android.gms.ads.mediation.customevent.CustomEventExtras;
import com.google.android.gms.internal.zzex.zza;
import java.util.Map;

@zzhb
public final class zzew extends zza {
    private Map<Class<? extends NetworkExtras>, NetworkExtras> zzCG;

    private <NETWORK_EXTRAS extends com.google.ads.mediation.NetworkExtras, SERVER_PARAMETERS extends MediationServerParameters> zzey zzah(String str) throws RemoteException {
        try {
            Class cls = Class.forName(str, false, zzew.class.getClassLoader());
            if (MediationAdapter.class.isAssignableFrom(cls)) {
                MediationAdapter mediationAdapter = (MediationAdapter) cls.newInstance();
                return new zzfj(mediationAdapter, (com.google.ads.mediation.NetworkExtras) this.zzCG.get(mediationAdapter.getAdditionalParametersType()));
            } else if (com.google.android.gms.ads.mediation.MediationAdapter.class.isAssignableFrom(cls)) {
                return new zzfe((com.google.android.gms.ads.mediation.MediationAdapter) cls.newInstance());
            } else {
                zzb.zzaK("Could not instantiate mediation adapter: " + str + " (not a valid adapter).");
                throw new RemoteException();
            }
        } catch (Throwable th) {
            return zzai(str);
        }
    }

    private zzey zzai(String str) throws RemoteException {
        try {
            zzb.zzaI("Reflection failed, retrying using direct instantiation");
            if ("com.google.ads.mediation.admob.AdMobAdapter".equals(str)) {
                return new zzfe(new AdMobAdapter());
            }
            if ("com.google.ads.mediation.AdUrlAdapter".equals(str)) {
                return new zzfe(new AdUrlAdapter());
            }
            if ("com.google.android.gms.ads.mediation.customevent.CustomEventAdapter".equals(str)) {
                return new zzfe(new CustomEventAdapter());
            }
            if ("com.google.ads.mediation.customevent.CustomEventAdapter".equals(str)) {
                MediationAdapter customEventAdapter = new com.google.ads.mediation.customevent.CustomEventAdapter();
                return new zzfj(customEventAdapter, (CustomEventExtras) this.zzCG.get(customEventAdapter.getAdditionalParametersType()));
            }
            throw new RemoteException();
        } catch (Throwable th) {
            zzb.zzd("Could not instantiate mediation adapter: " + str + ". ", th);
        }
    }

    public zzey zzaf(String str) throws RemoteException {
        return zzah(str);
    }

    public boolean zzag(String str) throws RemoteException {
        boolean z = false;
        try {
            z = CustomEvent.class.isAssignableFrom(Class.forName(str, false, zzew.class.getClassLoader()));
        } catch (Throwable th) {
            zzb.zzaK("Could not load custom event implementation class: " + str + ", assuming old implementation.");
        }
        return z;
    }

    public void zzg(Map<Class<? extends NetworkExtras>, NetworkExtras> map) {
        this.zzCG = map;
    }
}
