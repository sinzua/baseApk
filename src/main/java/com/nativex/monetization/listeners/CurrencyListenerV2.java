package com.nativex.monetization.listeners;

import com.nativex.monetization.communication.RedeemCurrencyData;

@Deprecated
public interface CurrencyListenerV2 extends CurrencyListenerBase {
    void onRedeem(RedeemCurrencyData redeemCurrencyData);
}
