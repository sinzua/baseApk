package com.google.ads.mediation;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdLoader.Builder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAdView;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAd.OnAppInstallAdLoadedListener;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAd.OnContentAdLoadedListener;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.MediationBannerAdapter;
import com.google.android.gms.ads.mediation.MediationBannerListener;
import com.google.android.gms.ads.mediation.MediationInterstitialListener;
import com.google.android.gms.ads.mediation.MediationNativeAdapter;
import com.google.android.gms.ads.mediation.MediationNativeListener;
import com.google.android.gms.ads.mediation.NativeAppInstallAdMapper;
import com.google.android.gms.ads.mediation.NativeContentAdMapper;
import com.google.android.gms.ads.mediation.NativeMediationAdRequest;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdAdapter;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdListener;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzka;
import java.util.Date;
import java.util.Set;

@zzhb
public abstract class AbstractAdViewAdapter implements MediationBannerAdapter, MediationNativeAdapter, MediationRewardedVideoAdAdapter, zzka {
    public static final String AD_UNIT_ID_PARAMETER = "pubid";
    protected AdView zzaQ;
    protected InterstitialAd zzaR;
    private AdLoader zzaS;
    private Context zzaT;
    private InterstitialAd zzaU;
    private MediationRewardedVideoAdListener zzaV;
    private String zzaW;
    final RewardedVideoAdListener zzaX = new RewardedVideoAdListener(this) {
        final /* synthetic */ AbstractAdViewAdapter zzaY;

        {
            this.zzaY = r1;
        }

        public void onRewarded(RewardItem reward) {
            this.zzaY.zzaV.onRewarded(this.zzaY, reward);
        }

        public void onRewardedVideoAdClosed() {
            this.zzaY.zzaV.onAdClosed(this.zzaY);
            this.zzaY.zzaU = null;
        }

        public void onRewardedVideoAdFailedToLoad(int errorCode) {
            this.zzaY.zzaV.onAdFailedToLoad(this.zzaY, errorCode);
        }

        public void onRewardedVideoAdLeftApplication() {
            this.zzaY.zzaV.onAdLeftApplication(this.zzaY);
        }

        public void onRewardedVideoAdLoaded() {
            this.zzaY.zzaV.onAdLoaded(this.zzaY);
        }

        public void onRewardedVideoAdOpened() {
            this.zzaY.zzaV.onAdOpened(this.zzaY);
        }

        public void onRewardedVideoStarted() {
            this.zzaY.zzaV.onVideoStarted(this.zzaY);
        }
    };

    static final class zzc extends AdListener implements com.google.android.gms.ads.internal.client.zza {
        final AbstractAdViewAdapter zzbb;
        final MediationBannerListener zzbc;

        public zzc(AbstractAdViewAdapter abstractAdViewAdapter, MediationBannerListener mediationBannerListener) {
            this.zzbb = abstractAdViewAdapter;
            this.zzbc = mediationBannerListener;
        }

        public void onAdClicked() {
            this.zzbc.onAdClicked(this.zzbb);
        }

        public void onAdClosed() {
            this.zzbc.onAdClosed(this.zzbb);
        }

        public void onAdFailedToLoad(int errorCode) {
            this.zzbc.onAdFailedToLoad(this.zzbb, errorCode);
        }

        public void onAdLeftApplication() {
            this.zzbc.onAdLeftApplication(this.zzbb);
        }

        public void onAdLoaded() {
            this.zzbc.onAdLoaded(this.zzbb);
        }

        public void onAdOpened() {
            this.zzbc.onAdOpened(this.zzbb);
        }
    }

    static final class zzd extends AdListener implements com.google.android.gms.ads.internal.client.zza {
        final AbstractAdViewAdapter zzbb;
        final MediationInterstitialListener zzbd;

        public zzd(AbstractAdViewAdapter abstractAdViewAdapter, MediationInterstitialListener mediationInterstitialListener) {
            this.zzbb = abstractAdViewAdapter;
            this.zzbd = mediationInterstitialListener;
        }

        public void onAdClicked() {
            this.zzbd.onAdClicked(this.zzbb);
        }

        public void onAdClosed() {
            this.zzbd.onAdClosed(this.zzbb);
        }

        public void onAdFailedToLoad(int errorCode) {
            this.zzbd.onAdFailedToLoad(this.zzbb, errorCode);
        }

        public void onAdLeftApplication() {
            this.zzbd.onAdLeftApplication(this.zzbb);
        }

        public void onAdLoaded() {
            this.zzbd.onAdLoaded(this.zzbb);
        }

        public void onAdOpened() {
            this.zzbd.onAdOpened(this.zzbb);
        }
    }

    static final class zze extends AdListener implements OnAppInstallAdLoadedListener, OnContentAdLoadedListener, com.google.android.gms.ads.internal.client.zza {
        final AbstractAdViewAdapter zzbb;
        final MediationNativeListener zzbe;

        public zze(AbstractAdViewAdapter abstractAdViewAdapter, MediationNativeListener mediationNativeListener) {
            this.zzbb = abstractAdViewAdapter;
            this.zzbe = mediationNativeListener;
        }

        public void onAdClicked() {
            this.zzbe.onAdClicked(this.zzbb);
        }

        public void onAdClosed() {
            this.zzbe.onAdClosed(this.zzbb);
        }

        public void onAdFailedToLoad(int errorCode) {
            this.zzbe.onAdFailedToLoad(this.zzbb, errorCode);
        }

        public void onAdLeftApplication() {
            this.zzbe.onAdLeftApplication(this.zzbb);
        }

        public void onAdLoaded() {
        }

        public void onAdOpened() {
            this.zzbe.onAdOpened(this.zzbb);
        }

        public void onAppInstallAdLoaded(NativeAppInstallAd ad) {
            this.zzbe.onAdLoaded(this.zzbb, new zza(ad));
        }

        public void onContentAdLoaded(NativeContentAd ad) {
            this.zzbe.onAdLoaded(this.zzbb, new zzb(ad));
        }
    }

    static class zza extends NativeAppInstallAdMapper {
        private final NativeAppInstallAd zzaZ;

        public zza(NativeAppInstallAd nativeAppInstallAd) {
            this.zzaZ = nativeAppInstallAd;
            setHeadline(nativeAppInstallAd.getHeadline().toString());
            setImages(nativeAppInstallAd.getImages());
            setBody(nativeAppInstallAd.getBody().toString());
            setIcon(nativeAppInstallAd.getIcon());
            setCallToAction(nativeAppInstallAd.getCallToAction().toString());
            setStarRating(nativeAppInstallAd.getStarRating().doubleValue());
            setStore(nativeAppInstallAd.getStore().toString());
            setPrice(nativeAppInstallAd.getPrice().toString());
            setOverrideImpressionRecording(true);
            setOverrideClickHandling(true);
        }

        public void trackView(View view) {
            if (view instanceof NativeAdView) {
                ((NativeAdView) view).setNativeAd(this.zzaZ);
            }
        }
    }

    static class zzb extends NativeContentAdMapper {
        private final NativeContentAd zzba;

        public zzb(NativeContentAd nativeContentAd) {
            this.zzba = nativeContentAd;
            setHeadline(nativeContentAd.getHeadline().toString());
            setImages(nativeContentAd.getImages());
            setBody(nativeContentAd.getBody().toString());
            setLogo(nativeContentAd.getLogo());
            setCallToAction(nativeContentAd.getCallToAction().toString());
            setAdvertiser(nativeContentAd.getAdvertiser().toString());
            setOverrideImpressionRecording(true);
            setOverrideClickHandling(true);
        }

        public void trackView(View view) {
            if (view instanceof NativeAdView) {
                ((NativeAdView) view).setNativeAd(this.zzba);
            }
        }
    }

    public String getAdUnitId(Bundle serverParameters) {
        return serverParameters.getString(AD_UNIT_ID_PARAMETER);
    }

    public View getBannerView() {
        return this.zzaQ;
    }

    public Bundle getInterstitialAdapterInfo() {
        return new com.google.android.gms.ads.mediation.MediationAdapter.zza().zzS(1).zziw();
    }

    public void initialize(Context context, MediationAdRequest mediationAdRequest, String userId, MediationRewardedVideoAdListener listener, Bundle serverParameters, Bundle networkExtras) {
        this.zzaT = context.getApplicationContext();
        this.zzaW = userId;
        this.zzaV = listener;
        this.zzaV.onInitializationSucceeded(this);
    }

    public boolean isInitialized() {
        return this.zzaV != null;
    }

    public void loadAd(MediationAdRequest mediationAdRequest, Bundle serverParameters, Bundle networkExtras) {
        if (this.zzaT == null || this.zzaV == null) {
            com.google.android.gms.ads.internal.util.client.zzb.e("AdMobAdapter.loadAd called before initialize.");
            return;
        }
        this.zzaU = new InterstitialAd(this.zzaT);
        this.zzaU.zza(true);
        this.zzaU.setAdUnitId(getAdUnitId(serverParameters));
        this.zzaU.setRewardedVideoAdListener(this.zzaX);
        this.zzaU.zzm(this.zzaW);
        this.zzaU.loadAd(zza(this.zzaT, mediationAdRequest, networkExtras, serverParameters));
    }

    public void onDestroy() {
        if (this.zzaQ != null) {
            this.zzaQ.destroy();
            this.zzaQ = null;
        }
        if (this.zzaR != null) {
            this.zzaR = null;
        }
        if (this.zzaS != null) {
            this.zzaS = null;
        }
        if (this.zzaU != null) {
            this.zzaU = null;
        }
    }

    public void onPause() {
        if (this.zzaQ != null) {
            this.zzaQ.pause();
        }
    }

    public void onResume() {
        if (this.zzaQ != null) {
            this.zzaQ.resume();
        }
    }

    public void requestBannerAd(Context context, MediationBannerListener bannerListener, Bundle serverParameters, AdSize adSize, MediationAdRequest mediationAdRequest, Bundle extras) {
        this.zzaQ = new AdView(context);
        this.zzaQ.setAdSize(new AdSize(adSize.getWidth(), adSize.getHeight()));
        this.zzaQ.setAdUnitId(getAdUnitId(serverParameters));
        this.zzaQ.setAdListener(new zzc(this, bannerListener));
        this.zzaQ.loadAd(zza(context, mediationAdRequest, extras, serverParameters));
    }

    public void requestInterstitialAd(Context context, MediationInterstitialListener interstitialListener, Bundle serverParameters, MediationAdRequest mediationAdRequest, Bundle extras) {
        this.zzaR = new InterstitialAd(context);
        this.zzaR.setAdUnitId(getAdUnitId(serverParameters));
        this.zzaR.setAdListener(new zzd(this, interstitialListener));
        this.zzaR.loadAd(zza(context, mediationAdRequest, extras, serverParameters));
    }

    public void requestNativeAd(Context context, MediationNativeListener listener, Bundle serverParameters, NativeMediationAdRequest mediationAdRequest, Bundle extras) {
        OnContentAdLoadedListener com_google_ads_mediation_AbstractAdViewAdapter_zze = new zze(this, listener);
        Builder withAdListener = zza(context, serverParameters.getString(AD_UNIT_ID_PARAMETER)).withAdListener(com_google_ads_mediation_AbstractAdViewAdapter_zze);
        NativeAdOptions nativeAdOptions = mediationAdRequest.getNativeAdOptions();
        if (nativeAdOptions != null) {
            withAdListener.withNativeAdOptions(nativeAdOptions);
        }
        if (mediationAdRequest.isAppInstallAdRequested()) {
            withAdListener.forAppInstallAd(com_google_ads_mediation_AbstractAdViewAdapter_zze);
        }
        if (mediationAdRequest.isContentAdRequested()) {
            withAdListener.forContentAd(com_google_ads_mediation_AbstractAdViewAdapter_zze);
        }
        this.zzaS = withAdListener.build();
        this.zzaS.loadAd(zza(context, mediationAdRequest, extras, serverParameters));
    }

    public void showInterstitial() {
        this.zzaR.show();
    }

    public void showVideo() {
        this.zzaU.show();
    }

    protected abstract Bundle zza(Bundle bundle, Bundle bundle2);

    Builder zza(Context context, String str) {
        return new Builder(context, str);
    }

    AdRequest zza(Context context, MediationAdRequest mediationAdRequest, Bundle bundle, Bundle bundle2) {
        AdRequest.Builder builder = new AdRequest.Builder();
        Date birthday = mediationAdRequest.getBirthday();
        if (birthday != null) {
            builder.setBirthday(birthday);
        }
        int gender = mediationAdRequest.getGender();
        if (gender != 0) {
            builder.setGender(gender);
        }
        Set<String> keywords = mediationAdRequest.getKeywords();
        if (keywords != null) {
            for (String addKeyword : keywords) {
                builder.addKeyword(addKeyword);
            }
        }
        Location location = mediationAdRequest.getLocation();
        if (location != null) {
            builder.setLocation(location);
        }
        if (mediationAdRequest.isTesting()) {
            builder.addTestDevice(zzn.zzcS().zzT(context));
        }
        if (mediationAdRequest.taggedForChildDirectedTreatment() != -1) {
            builder.tagForChildDirectedTreatment(mediationAdRequest.taggedForChildDirectedTreatment() == 1);
        }
        builder.setIsDesignedForFamilies(mediationAdRequest.isDesignedForFamilies());
        builder.addNetworkExtrasBundle(AdMobAdapter.class, zza(bundle, bundle2));
        return builder.build();
    }
}
