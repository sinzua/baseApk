package com.nativex.monetization.listeners;

import com.nativex.monetization.communication.RedeemRewardData;

public interface RewardListener {
    void onRedeem(RedeemRewardData redeemRewardData);
}
