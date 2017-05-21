package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.content.Context;
import android.view.View;
import com.google.android.gms.auth.zzd;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.internal.zzro;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class zzf {
    private final Account zzTI;
    private final String zzUW;
    private final Set<Scope> zzagh;
    private final int zzagj;
    private final View zzagk;
    private final String zzagl;
    private final Set<Scope> zzalb;
    private final Map<Api<?>, zza> zzalc;
    private final zzro zzald;
    private Integer zzale;

    public static final class zza {
        public final Set<Scope> zzXf;
        public final boolean zzalf;

        public zza(Set<Scope> set, boolean z) {
            zzx.zzz(set);
            this.zzXf = Collections.unmodifiableSet(set);
            this.zzalf = z;
        }
    }

    public zzf(Account account, Set<Scope> set, Map<Api<?>, zza> map, int i, View view, String str, String str2, zzro com_google_android_gms_internal_zzro) {
        Map map2;
        this.zzTI = account;
        this.zzagh = set == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(set);
        if (map == null) {
            map2 = Collections.EMPTY_MAP;
        }
        this.zzalc = map2;
        this.zzagk = view;
        this.zzagj = i;
        this.zzUW = str;
        this.zzagl = str2;
        this.zzald = com_google_android_gms_internal_zzro;
        Set hashSet = new HashSet(this.zzagh);
        for (zza com_google_android_gms_common_internal_zzf_zza : this.zzalc.values()) {
            hashSet.addAll(com_google_android_gms_common_internal_zzf_zza.zzXf);
        }
        this.zzalb = Collections.unmodifiableSet(hashSet);
    }

    public static zzf zzat(Context context) {
        return new Builder(context).zzoY();
    }

    public Account getAccount() {
        return this.zzTI;
    }

    @Deprecated
    public String getAccountName() {
        return this.zzTI != null ? this.zzTI.name : null;
    }

    public void zza(Integer num) {
        this.zzale = num;
    }

    public Set<Scope> zzb(Api<?> api) {
        zza com_google_android_gms_common_internal_zzf_zza = (zza) this.zzalc.get(api);
        if (com_google_android_gms_common_internal_zzf_zza == null || com_google_android_gms_common_internal_zzf_zza.zzXf.isEmpty()) {
            return this.zzagh;
        }
        Set<Scope> hashSet = new HashSet(this.zzagh);
        hashSet.addAll(com_google_android_gms_common_internal_zzf_zza.zzXf);
        return hashSet;
    }

    public Account zzqq() {
        return this.zzTI != null ? this.zzTI : new Account("<<default account>>", zzd.GOOGLE_ACCOUNT_TYPE);
    }

    public int zzqr() {
        return this.zzagj;
    }

    public Set<Scope> zzqs() {
        return this.zzagh;
    }

    public Set<Scope> zzqt() {
        return this.zzalb;
    }

    public Map<Api<?>, zza> zzqu() {
        return this.zzalc;
    }

    public String zzqv() {
        return this.zzUW;
    }

    public String zzqw() {
        return this.zzagl;
    }

    public View zzqx() {
        return this.zzagk;
    }

    public zzro zzqy() {
        return this.zzald;
    }

    public Integer zzqz() {
        return this.zzale;
    }
}
