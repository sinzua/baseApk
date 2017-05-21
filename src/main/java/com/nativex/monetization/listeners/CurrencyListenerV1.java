package com.nativex.monetization.listeners;

import com.nativex.monetization.business.reward.Balance;
import java.util.List;

@Deprecated
public interface CurrencyListenerV1 extends CurrencyListenerBase {
    void onRedeem(List<Balance> list);
}
