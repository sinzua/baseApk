package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

@zzhb
public class zzjf {

    public interface zza<D, R> {
        R zzf(D d);
    }

    public static <A, B> zzjg<B> zza(final zzjg<A> com_google_android_gms_internal_zzjg_A, final zza<A, B> com_google_android_gms_internal_zzjf_zza_A__B) {
        final zzjg com_google_android_gms_internal_zzjd = new zzjd();
        com_google_android_gms_internal_zzjg_A.zzb(new Runnable() {
            public void run() {
                try {
                    com_google_android_gms_internal_zzjd.zzg(com_google_android_gms_internal_zzjf_zza_A__B.zzf(com_google_android_gms_internal_zzjg_A.get()));
                    return;
                } catch (CancellationException e) {
                } catch (InterruptedException e2) {
                } catch (ExecutionException e3) {
                }
                com_google_android_gms_internal_zzjd.cancel(true);
            }
        });
        return com_google_android_gms_internal_zzjd;
    }

    public static <V> zzjg<List<V>> zzl(final List<zzjg<V>> list) {
        final zzjg com_google_android_gms_internal_zzjd = new zzjd();
        final int size = list.size();
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        for (zzjg zzb : list) {
            zzb.zzb(new Runnable() {
                public void run() {
                    Throwable e;
                    if (atomicInteger.incrementAndGet() >= size) {
                        try {
                            com_google_android_gms_internal_zzjd.zzg(zzjf.zzm(list));
                            return;
                        } catch (ExecutionException e2) {
                            e = e2;
                        } catch (InterruptedException e3) {
                            e = e3;
                        }
                    } else {
                        return;
                    }
                    zzb.zzd("Unable to convert list of futures to a future of list", e);
                }
            });
        }
        return com_google_android_gms_internal_zzjd;
    }

    private static <V> List<V> zzm(List<zzjg<V>> list) throws ExecutionException, InterruptedException {
        List<V> arrayList = new ArrayList();
        for (zzjg com_google_android_gms_internal_zzjg : list) {
            Object obj = com_google_android_gms_internal_zzjg.get();
            if (obj != null) {
                arrayList.add(obj);
            }
        }
        return arrayList;
    }
}
