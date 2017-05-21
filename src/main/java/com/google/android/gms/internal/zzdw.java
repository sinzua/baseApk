package com.google.android.gms.internal;

import android.os.Handler;
import android.os.RemoteException;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzk;
import com.google.android.gms.ads.internal.zzr;
import java.util.LinkedList;
import java.util.List;

@zzhb
class zzdw {
    private final List<zza> zzpH = new LinkedList();

    interface zza {
        void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException;
    }

    zzdw() {
    }

    void zza(final zzdx com_google_android_gms_internal_zzdx) {
        Handler handler = zzir.zzMc;
        for (final zza com_google_android_gms_internal_zzdw_zza : this.zzpH) {
            handler.post(new Runnable(this) {
                final /* synthetic */ zzdw zzAc;

                public void run() {
                    try {
                        com_google_android_gms_internal_zzdw_zza.zzb(com_google_android_gms_internal_zzdx);
                    } catch (Throwable e) {
                        zzb.zzd("Could not propagate interstitial ad event.", e);
                    }
                }
            });
        }
    }

    void zzc(zzk com_google_android_gms_ads_internal_zzk) {
        com_google_android_gms_ads_internal_zzk.zza(new com.google.android.gms.ads.internal.client.zzq.zza(this) {
            final /* synthetic */ zzdw zzAc;

            {
                this.zzAc = r1;
            }

            public void onAdClosed() throws RemoteException {
                this.zzAc.zzpH.add(new zza(this) {
                    final /* synthetic */ AnonymousClass1 zzAd;

                    {
                        this.zzAd = r1;
                    }

                    public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                        if (com_google_android_gms_internal_zzdx.zzpK != null) {
                            com_google_android_gms_internal_zzdx.zzpK.onAdClosed();
                        }
                        zzr.zzbN().zzee();
                    }
                });
            }

            public void onAdFailedToLoad(final int errorCode) throws RemoteException {
                this.zzAc.zzpH.add(new zza(this) {
                    final /* synthetic */ AnonymousClass1 zzAd;

                    public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                        if (com_google_android_gms_internal_zzdx.zzpK != null) {
                            com_google_android_gms_internal_zzdx.zzpK.onAdFailedToLoad(errorCode);
                        }
                    }
                });
                zzin.v("Pooled interstitial failed to load.");
            }

            public void onAdLeftApplication() throws RemoteException {
                this.zzAc.zzpH.add(new zza(this) {
                    final /* synthetic */ AnonymousClass1 zzAd;

                    {
                        this.zzAd = r1;
                    }

                    public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                        if (com_google_android_gms_internal_zzdx.zzpK != null) {
                            com_google_android_gms_internal_zzdx.zzpK.onAdLeftApplication();
                        }
                    }
                });
            }

            public void onAdLoaded() throws RemoteException {
                this.zzAc.zzpH.add(new zza(this) {
                    final /* synthetic */ AnonymousClass1 zzAd;

                    {
                        this.zzAd = r1;
                    }

                    public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                        if (com_google_android_gms_internal_zzdx.zzpK != null) {
                            com_google_android_gms_internal_zzdx.zzpK.onAdLoaded();
                        }
                    }
                });
                zzin.v("Pooled interstitial loaded.");
            }

            public void onAdOpened() throws RemoteException {
                this.zzAc.zzpH.add(new zza(this) {
                    final /* synthetic */ AnonymousClass1 zzAd;

                    {
                        this.zzAd = r1;
                    }

                    public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                        if (com_google_android_gms_internal_zzdx.zzpK != null) {
                            com_google_android_gms_internal_zzdx.zzpK.onAdOpened();
                        }
                    }
                });
            }
        });
        com_google_android_gms_ads_internal_zzk.zza(new com.google.android.gms.ads.internal.client.zzw.zza(this) {
            final /* synthetic */ zzdw zzAc;

            {
                this.zzAc = r1;
            }

            public void onAppEvent(final String name, final String info) throws RemoteException {
                this.zzAc.zzpH.add(new zza(this) {
                    final /* synthetic */ AnonymousClass2 zzAg;

                    public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                        if (com_google_android_gms_internal_zzdx.zzAq != null) {
                            com_google_android_gms_internal_zzdx.zzAq.onAppEvent(name, info);
                        }
                    }
                });
            }
        });
        com_google_android_gms_ads_internal_zzk.zza(new com.google.android.gms.internal.zzgd.zza(this) {
            final /* synthetic */ zzdw zzAc;

            {
                this.zzAc = r1;
            }

            public void zza(final zzgc com_google_android_gms_internal_zzgc) throws RemoteException {
                this.zzAc.zzpH.add(new zza(this) {
                    final /* synthetic */ AnonymousClass3 zzAi;

                    public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                        if (com_google_android_gms_internal_zzdx.zzAr != null) {
                            com_google_android_gms_internal_zzdx.zzAr.zza(com_google_android_gms_internal_zzgc);
                        }
                    }
                });
            }
        });
        com_google_android_gms_ads_internal_zzk.zza(new com.google.android.gms.internal.zzcf.zza(this) {
            final /* synthetic */ zzdw zzAc;

            {
                this.zzAc = r1;
            }

            public void zza(final zzce com_google_android_gms_internal_zzce) throws RemoteException {
                this.zzAc.zzpH.add(new zza(this) {
                    final /* synthetic */ AnonymousClass4 zzAk;

                    public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                        if (com_google_android_gms_internal_zzdx.zzAs != null) {
                            com_google_android_gms_internal_zzdx.zzAs.zza(com_google_android_gms_internal_zzce);
                        }
                    }
                });
            }
        });
        com_google_android_gms_ads_internal_zzk.zza(new com.google.android.gms.ads.internal.client.zzp.zza(this) {
            final /* synthetic */ zzdw zzAc;

            {
                this.zzAc = r1;
            }

            public void onAdClicked() throws RemoteException {
                this.zzAc.zzpH.add(new zza(this) {
                    final /* synthetic */ AnonymousClass5 zzAl;

                    {
                        this.zzAl = r1;
                    }

                    public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                        if (com_google_android_gms_internal_zzdx.zzAt != null) {
                            com_google_android_gms_internal_zzdx.zzAt.onAdClicked();
                        }
                    }
                });
            }
        });
        com_google_android_gms_ads_internal_zzk.zza(new com.google.android.gms.ads.internal.reward.client.zzd.zza(this) {
            final /* synthetic */ zzdw zzAc;

            {
                this.zzAc = r1;
            }

            public void onRewardedVideoAdClosed() throws RemoteException {
                this.zzAc.zzpH.add(new zza(this) {
                    final /* synthetic */ AnonymousClass6 zzAm;

                    {
                        this.zzAm = r1;
                    }

                    public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                        if (com_google_android_gms_internal_zzdx.zzAu != null) {
                            com_google_android_gms_internal_zzdx.zzAu.onRewardedVideoAdClosed();
                        }
                    }
                });
            }

            public void onRewardedVideoAdFailedToLoad(final int errorCode) throws RemoteException {
                this.zzAc.zzpH.add(new zza(this) {
                    final /* synthetic */ AnonymousClass6 zzAm;

                    public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                        if (com_google_android_gms_internal_zzdx.zzAu != null) {
                            com_google_android_gms_internal_zzdx.zzAu.onRewardedVideoAdFailedToLoad(errorCode);
                        }
                    }
                });
            }

            public void onRewardedVideoAdLeftApplication() throws RemoteException {
                this.zzAc.zzpH.add(new zza(this) {
                    final /* synthetic */ AnonymousClass6 zzAm;

                    {
                        this.zzAm = r1;
                    }

                    public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                        if (com_google_android_gms_internal_zzdx.zzAu != null) {
                            com_google_android_gms_internal_zzdx.zzAu.onRewardedVideoAdLeftApplication();
                        }
                    }
                });
            }

            public void onRewardedVideoAdLoaded() throws RemoteException {
                this.zzAc.zzpH.add(new zza(this) {
                    final /* synthetic */ AnonymousClass6 zzAm;

                    {
                        this.zzAm = r1;
                    }

                    public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                        if (com_google_android_gms_internal_zzdx.zzAu != null) {
                            com_google_android_gms_internal_zzdx.zzAu.onRewardedVideoAdLoaded();
                        }
                    }
                });
            }

            public void onRewardedVideoAdOpened() throws RemoteException {
                this.zzAc.zzpH.add(new zza(this) {
                    final /* synthetic */ AnonymousClass6 zzAm;

                    {
                        this.zzAm = r1;
                    }

                    public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                        if (com_google_android_gms_internal_zzdx.zzAu != null) {
                            com_google_android_gms_internal_zzdx.zzAu.onRewardedVideoAdOpened();
                        }
                    }
                });
            }

            public void onRewardedVideoStarted() throws RemoteException {
                this.zzAc.zzpH.add(new zza(this) {
                    final /* synthetic */ AnonymousClass6 zzAm;

                    {
                        this.zzAm = r1;
                    }

                    public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                        if (com_google_android_gms_internal_zzdx.zzAu != null) {
                            com_google_android_gms_internal_zzdx.zzAu.onRewardedVideoStarted();
                        }
                    }
                });
            }

            public void zza(final com.google.android.gms.ads.internal.reward.client.zza com_google_android_gms_ads_internal_reward_client_zza) throws RemoteException {
                this.zzAc.zzpH.add(new zza(this) {
                    final /* synthetic */ AnonymousClass6 zzAm;

                    public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                        if (com_google_android_gms_internal_zzdx.zzAu != null) {
                            com_google_android_gms_internal_zzdx.zzAu.zza(com_google_android_gms_ads_internal_reward_client_zza);
                        }
                    }
                });
            }
        });
    }
}
