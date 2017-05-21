package com.google.android.gms.ads.mediation;

import android.os.Bundle;

public interface MediationAdapter {

    public static class zza {
        private int zzOn;

        public zza zzS(int i) {
            this.zzOn = i;
            return this;
        }

        public Bundle zziw() {
            Bundle bundle = new Bundle();
            bundle.putInt("capabilities", this.zzOn);
            return bundle;
        }
    }

    void onDestroy();

    void onPause();

    void onResume();
}
