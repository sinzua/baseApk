package com.supersonic.mediationsdk.sdk;

import android.os.Handler;
import android.os.Looper;
import com.supersonic.mediationsdk.events.EventsHelper;
import com.supersonic.mediationsdk.logger.SupersonicError;
import com.supersonic.mediationsdk.logger.SupersonicLogger.SupersonicTag;
import com.supersonic.mediationsdk.logger.SupersonicLoggerManager;
import com.supersonic.mediationsdk.model.Placement;
import com.supersonic.mediationsdk.utils.SupersonicConstants;

public class ListenersWrapper implements RewardedVideoListener, InterstitialListener, OfferwallListener {
    private CallbackHandlerThread mCallbackHandlerThread = new CallbackHandlerThread();
    private InterstitialListener mInterstitialListener;
    private OfferwallListener mOfferwallListener;
    private RewardedVideoListener mRewardedVideoListener;

    private class CallbackHandlerThread extends Thread {
        private Handler mCallbackHandler;

        private CallbackHandlerThread() {
        }

        public void run() {
            Looper.prepare();
            this.mCallbackHandler = new Handler();
            Looper.loop();
        }

        public Handler getCallbackHandler() {
            return this.mCallbackHandler;
        }
    }

    public ListenersWrapper() {
        this.mCallbackHandlerThread.start();
    }

    private boolean canSendCallback(Object productListener) {
        return (productListener == null || this.mCallbackHandlerThread == null) ? false : true;
    }

    private void sendCallback(Runnable callbackRunnable) {
        if (this.mCallbackHandlerThread != null) {
            Handler callbackHandler = this.mCallbackHandlerThread.getCallbackHandler();
            if (callbackHandler != null) {
                callbackHandler.post(callbackRunnable);
            }
        }
    }

    public void setRewardedVideoListener(RewardedVideoListener rewardedVideoListener) {
        this.mRewardedVideoListener = rewardedVideoListener;
    }

    public void setInterstitialListener(InterstitialListener interstitialListener) {
        this.mInterstitialListener = interstitialListener;
    }

    public void setOfferwallListener(OfferwallListener offerwallListener) {
        this.mOfferwallListener = offerwallListener;
    }

    public void onRewardedVideoInitSuccess() {
        SupersonicLoggerManager.getLogger().log(SupersonicTag.CALLBACK, "onRewardedVideoInitSuccess()", 1);
        EventsHelper.getInstance().notifyInitRewardedVideoResultEvent(SupersonicConstants.MEDIATION_PROVIDER_NAME, true);
        if (canSendCallback(this.mRewardedVideoListener)) {
            sendCallback(new Runnable() {
                public void run() {
                    ListenersWrapper.this.mRewardedVideoListener.onRewardedVideoInitSuccess();
                }
            });
        }
    }

    public void onRewardedVideoInitFail(final SupersonicError error) {
        SupersonicLoggerManager.getLogger().log(SupersonicTag.CALLBACK, "onRewardedVideoInitFail(" + error.toString() + ")", 1);
        EventsHelper.getInstance().notifyInitRewardedVideoResultEvent(SupersonicConstants.MEDIATION_PROVIDER_NAME, false);
        if (canSendCallback(this.mRewardedVideoListener)) {
            sendCallback(new Runnable() {
                public void run() {
                    ListenersWrapper.this.mRewardedVideoListener.onRewardedVideoInitFail(error);
                }
            });
        }
    }

    public void onRewardedVideoAdOpened() {
        SupersonicLoggerManager.getLogger().log(SupersonicTag.CALLBACK, "onRewardedVideoAdOpened()", 1);
        EventsHelper.getInstance().notifyVideoAdOpenedEvent(SupersonicConstants.MEDIATION_PROVIDER_NAME);
        if (canSendCallback(this.mRewardedVideoListener)) {
            sendCallback(new Runnable() {
                public void run() {
                    ListenersWrapper.this.mRewardedVideoListener.onRewardedVideoAdOpened();
                }
            });
        }
    }

    public void onRewardedVideoAdClosed() {
        SupersonicLoggerManager.getLogger().log(SupersonicTag.CALLBACK, "onRewardedVideoAdClosed()", 1);
        EventsHelper.getInstance().notifyVideoAdClosedEvent(SupersonicConstants.MEDIATION_PROVIDER_NAME);
        if (canSendCallback(this.mRewardedVideoListener)) {
            sendCallback(new Runnable() {
                public void run() {
                    ListenersWrapper.this.mRewardedVideoListener.onRewardedVideoAdClosed();
                }
            });
        }
    }

    public void onVideoAvailabilityChanged(final boolean available) {
        SupersonicLoggerManager.getLogger().log(SupersonicTag.CALLBACK, "onVideoAvailabilityChanged(available:" + available + ")", 1);
        EventsHelper.getInstance().notifyVideoAvailabilityChangedEvent(SupersonicConstants.MEDIATION_PROVIDER_NAME, available);
        if (canSendCallback(this.mRewardedVideoListener)) {
            sendCallback(new Runnable() {
                public void run() {
                    ListenersWrapper.this.mRewardedVideoListener.onVideoAvailabilityChanged(available);
                }
            });
        }
    }

    public void onVideoStart() {
        SupersonicLoggerManager.getLogger().log(SupersonicTag.CALLBACK, "onVideoStart()", 1);
        EventsHelper.getInstance().notifyVideoStartEvent(SupersonicConstants.MEDIATION_PROVIDER_NAME);
        if (canSendCallback(this.mRewardedVideoListener)) {
            sendCallback(new Runnable() {
                public void run() {
                    ListenersWrapper.this.mRewardedVideoListener.onVideoStart();
                }
            });
        }
    }

    public void onVideoEnd() {
        SupersonicLoggerManager.getLogger().log(SupersonicTag.CALLBACK, "onVideoEnd()", 1);
        EventsHelper.getInstance().notifyVideoEndEvent(SupersonicConstants.MEDIATION_PROVIDER_NAME);
        if (canSendCallback(this.mRewardedVideoListener)) {
            sendCallback(new Runnable() {
                public void run() {
                    ListenersWrapper.this.mRewardedVideoListener.onVideoEnd();
                }
            });
        }
    }

    public void onRewardedVideoAdRewarded(final Placement placement) {
        SupersonicLoggerManager.getLogger().log(SupersonicTag.CALLBACK, "onRewardedVideoAdRewarded(" + placement.toString() + ")", 1);
        EventsHelper.getInstance().notifyMediationVideoAdRewardedEvent(SupersonicConstants.MEDIATION_PROVIDER_NAME, placement.getPlacementName(), placement.getRewardName(), placement.getRewardAmount());
        if (canSendCallback(this.mRewardedVideoListener)) {
            sendCallback(new Runnable() {
                public void run() {
                    ListenersWrapper.this.mRewardedVideoListener.onRewardedVideoAdRewarded(placement);
                }
            });
        }
    }

    public void onRewardedVideoShowFail(final SupersonicError error) {
        SupersonicLoggerManager.getLogger().log(SupersonicTag.CALLBACK, "onRewardedVideoShowFail(" + error.toString() + ")", 1);
        EventsHelper.getInstance().notifyShowRewardedVideoResultEvent(SupersonicConstants.MEDIATION_PROVIDER_NAME, false);
        if (canSendCallback(this.mRewardedVideoListener)) {
            sendCallback(new Runnable() {
                public void run() {
                    ListenersWrapper.this.mRewardedVideoListener.onRewardedVideoShowFail(error);
                }
            });
        }
    }

    public void onInterstitialInitSuccess() {
        SupersonicLoggerManager.getLogger().log(SupersonicTag.CALLBACK, "onInterstitialInitSuccess()", 1);
        if (canSendCallback(this.mInterstitialListener)) {
            sendCallback(new Runnable() {
                public void run() {
                    ListenersWrapper.this.mInterstitialListener.onInterstitialInitSuccess();
                }
            });
        }
    }

    public void onInterstitialInitFail(final SupersonicError supersonicError) {
        SupersonicLoggerManager.getLogger().log(SupersonicTag.CALLBACK, "onInterstitialInitFail(" + supersonicError + ")", 1);
        if (canSendCallback(this.mInterstitialListener)) {
            sendCallback(new Runnable() {
                public void run() {
                    ListenersWrapper.this.mInterstitialListener.onInterstitialInitFail(supersonicError);
                }
            });
        }
    }

    public void onInterstitialAvailability(final boolean available) {
        SupersonicLoggerManager.getLogger().log(SupersonicTag.CALLBACK, "onInterstitialAvailability(available" + available + ")", 1);
        if (canSendCallback(this.mInterstitialListener)) {
            sendCallback(new Runnable() {
                public void run() {
                    ListenersWrapper.this.mInterstitialListener.onInterstitialAvailability(available);
                }
            });
        }
    }

    public void onInterstitialShowSuccess() {
        SupersonicLoggerManager.getLogger().log(SupersonicTag.CALLBACK, "onInterstitialShowSuccess()", 1);
        if (canSendCallback(this.mInterstitialListener)) {
            sendCallback(new Runnable() {
                public void run() {
                    ListenersWrapper.this.mInterstitialListener.onInterstitialShowSuccess();
                }
            });
        }
    }

    public void onInterstitialShowFail(final SupersonicError supersonicError) {
        SupersonicLoggerManager.getLogger().log(SupersonicTag.CALLBACK, "onInterstitialShowFail(" + supersonicError + ")", 1);
        if (canSendCallback(this.mInterstitialListener)) {
            sendCallback(new Runnable() {
                public void run() {
                    ListenersWrapper.this.mInterstitialListener.onInterstitialShowFail(supersonicError);
                }
            });
        }
    }

    public void onInterstitialAdClicked() {
        SupersonicLoggerManager.getLogger().log(SupersonicTag.CALLBACK, "onInterstitialAdClicked()", 1);
        if (canSendCallback(this.mInterstitialListener)) {
            sendCallback(new Runnable() {
                public void run() {
                    ListenersWrapper.this.mInterstitialListener.onInterstitialAdClicked();
                }
            });
        }
    }

    public void onInterstitialAdClosed() {
        SupersonicLoggerManager.getLogger().log(SupersonicTag.CALLBACK, "onInterstitialAdClosed()", 1);
        if (canSendCallback(this.mInterstitialListener)) {
            sendCallback(new Runnable() {
                public void run() {
                    ListenersWrapper.this.mInterstitialListener.onInterstitialAdClosed();
                }
            });
        }
    }

    public void onOfferwallInitSuccess() {
        SupersonicLoggerManager.getLogger().log(SupersonicTag.CALLBACK, "onOfferwallInitSuccess()", 1);
        if (canSendCallback(this.mOfferwallListener)) {
            sendCallback(new Runnable() {
                public void run() {
                    ListenersWrapper.this.mOfferwallListener.onOfferwallInitSuccess();
                }
            });
        }
    }

    public void onOfferwallInitFail(final SupersonicError supersonicError) {
        SupersonicLoggerManager.getLogger().log(SupersonicTag.CALLBACK, "onOfferwallInitFail(" + supersonicError + ")", 1);
        if (canSendCallback(this.mOfferwallListener)) {
            sendCallback(new Runnable() {
                public void run() {
                    ListenersWrapper.this.mOfferwallListener.onOfferwallInitFail(supersonicError);
                }
            });
        }
    }

    public void onOfferwallOpened() {
        SupersonicLoggerManager.getLogger().log(SupersonicTag.CALLBACK, "onOfferwallOpened()", 1);
        if (canSendCallback(this.mOfferwallListener)) {
            sendCallback(new Runnable() {
                public void run() {
                    ListenersWrapper.this.mOfferwallListener.onOfferwallOpened();
                }
            });
        }
    }

    public void onOfferwallShowFail(final SupersonicError supersonicError) {
        SupersonicLoggerManager.getLogger().log(SupersonicTag.CALLBACK, "onOfferwallShowFail(" + supersonicError + ")", 1);
        if (canSendCallback(this.mOfferwallListener)) {
            sendCallback(new Runnable() {
                public void run() {
                    ListenersWrapper.this.mOfferwallListener.onOfferwallShowFail(supersonicError);
                }
            });
        }
    }

    public boolean onOfferwallAdCredited(int credits, int totalCredits, boolean totalCreditsFlag) {
        boolean result = false;
        if (this.mOfferwallListener != null) {
            result = this.mOfferwallListener.onOfferwallAdCredited(credits, totalCredits, totalCreditsFlag);
        }
        SupersonicLoggerManager.getLogger().log(SupersonicTag.CALLBACK, "onOfferwallAdCredited(credits:" + credits + ", " + "totalCredits:" + totalCredits + ", " + "totalCreditsFlag:" + totalCreditsFlag + "):" + result, 1);
        return result;
    }

    public void onGetOfferwallCreditsFail(final SupersonicError supersonicError) {
        SupersonicLoggerManager.getLogger().log(SupersonicTag.CALLBACK, "onGetOfferwallCreditsFail(" + supersonicError + ")", 1);
        if (canSendCallback(this.mOfferwallListener)) {
            sendCallback(new Runnable() {
                public void run() {
                    ListenersWrapper.this.mOfferwallListener.onGetOfferwallCreditsFail(supersonicError);
                }
            });
        }
    }

    public void onOfferwallClosed() {
        SupersonicLoggerManager.getLogger().log(SupersonicTag.CALLBACK, "onOfferwallClosed()", 1);
        if (canSendCallback(this.mOfferwallListener)) {
            sendCallback(new Runnable() {
                public void run() {
                    ListenersWrapper.this.mOfferwallListener.onOfferwallClosed();
                }
            });
        }
    }
}
