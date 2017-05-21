package com.nativex.monetization.communication;

import android.text.TextUtils;
import com.nativex.common.DeviceManager;
import com.nativex.common.Log;
import com.nativex.common.SharedPreferenceManager;
import com.nativex.monetization.business.CreateSessionResponseData;
import com.nativex.monetization.business.GetDeviceBalanceResponseData;
import com.nativex.monetization.business.Receipt;
import com.nativex.monetization.business.RedeemDeviceBalanceResponseData;
import com.nativex.monetization.business.reward.Reward;
import com.nativex.monetization.listeners.CurrencyListenerBase;
import com.nativex.monetization.listeners.CurrencyListenerV1;
import com.nativex.monetization.listeners.CurrencyListenerV2;
import com.nativex.monetization.listeners.RewardListener;
import com.nativex.monetization.manager.CacheDBManager;
import com.nativex.monetization.manager.MonetizationSharedDataManager;
import com.nativex.monetization.manager.SessionManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ServerResponseHandler {
    ServerResponseHandler() {
    }

    public void handleCreateSession(CreateSessionResponseData sessionResponse) {
        if (sessionResponse != null) {
            try {
                if (sessionResponse.getSession() != null) {
                    handleCreateSessionResponseData(sessionResponse);
                    SessionManager.storeSession();
                }
            } catch (Exception e) {
                Log.d("ServerResponseHandler: Unexpected exception caught while handling CreateSession request.");
                e.printStackTrace();
            }
        }
    }

    private void handleCreateSessionResponseData(CreateSessionResponseData sessionResponse) {
        SessionManager.setSessionResponse(sessionResponse);
        String previousDeviceId = SharedPreferenceManager.getDeviceId();
        if (previousDeviceId == null || !previousDeviceId.equals(sessionResponse.getDeviceId())) {
            SharedPreferenceManager.storeDeviceId(sessionResponse.getDeviceId());
            SharedPreferenceManager.storeBalances(new HashMap());
        }
        MonetizationSharedDataManager.setCurrencySupport(sessionResponse.isCurrencyEnabled().booleanValue());
        DeviceManager.updateDeviceId();
        if (sessionResponse.getFreeSpaceMinMegabytes() != null) {
            CacheDBManager.getInstance().updateCacheUtilFreeSpaceMin((long) sessionResponse.getFreeSpaceMinMegabytes().intValue());
        }
    }

    public boolean handleGetDeviceBalance(GetDeviceBalanceResponseData getDeviceBalance) {
        if (getDeviceBalance != null) {
            try {
                RedeemRewardData data = new RedeemRewardData();
                handleNewBalances(data, getDeviceBalance.getBalances());
                if (data.unRedeemedBalances.size() > 0) {
                    ServerRequestManager.getInstance().redeemCurrency(data);
                    return true;
                }
            } catch (Exception e) {
                Log.d("ServerResponseHandler: Unexpected exception caught while handling GetDeviceBalance request.");
                e.printStackTrace();
            }
        }
        return false;
    }

    private void handleNewBalances(RedeemRewardData data, List<Reward> newBalances) {
        data.unRedeemedBalances = SharedPreferenceManager.getBalances();
        if (newBalances != null && newBalances.size() > 0) {
            for (Reward balance : newBalances) {
                data.unRedeemedBalances.put(balance.getPayoutId(), balance);
            }
        }
        SharedPreferenceManager.storeBalances(data.unRedeemedBalances);
    }

    public void handleRedeemCurrency(RedeemDeviceBalanceResponseData redeemDeviceBalanceResponse, RedeemRewardData balanceData) {
        if (redeemDeviceBalanceResponse != null) {
            try {
                balanceData.messages = redeemDeviceBalanceResponse.getMessages();
                getRedeemedBalances(balanceData, redeemDeviceBalanceResponse.getReceipts());
                fireRedeemBalanceListener(balanceData);
                SharedPreferenceManager.storeBalances(balanceData.unRedeemedBalances);
            } catch (Exception e) {
                Log.d("ServerResponseHandler: Unexpected exception caught while handling RedeemCurrency request.");
                e.printStackTrace();
            }
        }
    }

    private void fireRedeemBalanceListener(RedeemRewardData data) {
        if (MonetizationSharedDataManager.isUsingRewardListener()) {
            RewardListener listener = MonetizationSharedDataManager.getRewardListener();
            if (listener != null) {
                listener.onRedeem(data);
                return;
            }
            return;
        }
        RedeemCurrencyData currencyData = new RedeemCurrencyData(data);
        CurrencyListenerBase currencyListener = MonetizationSharedDataManager.getCurrencyListener();
        if (currencyListener == null) {
            return;
        }
        if (currencyListener instanceof CurrencyListenerV1) {
            ((CurrencyListenerV1) currencyListener).onRedeem(currencyData.getBalances());
        } else if (currencyListener instanceof CurrencyListenerV2) {
            ((CurrencyListenerV2) currencyListener).onRedeem(currencyData);
        }
    }

    private void getRedeemedBalances(RedeemRewardData data, List<Receipt> receipts) {
        if (receipts != null && receipts.size() > 0) {
            if (MonetizationSharedDataManager.isUsingRewardListener()) {
                getRedeemedRewardBalances(data, receipts);
            } else {
                getRedeemedCurrencyBalances_OLD(data, receipts);
            }
        }
    }

    private void getRedeemedRewardBalances(RedeemRewardData rewardData, List<Receipt> receipts) {
        rewardData.receipts = new ArrayList();
        rewardData.redeemedBalances = new ArrayList();
        for (Receipt receipt : receipts) {
            if (!(TextUtils.isEmpty(receipt.getReceiptId()) || rewardData.receipts.contains(receipt.getReceiptId()))) {
                rewardData.receipts.add(receipt.getReceiptId());
            }
            Reward newReward = (Reward) rewardData.unRedeemedBalances.get(receipt.getPayoutId());
            if (newReward == null) {
                Log.e("unredeemed reward not found with payout ID " + receipt.getPayoutId());
            } else {
                rewardData.redeemedBalances.add(newReward);
                rewardData.unRedeemedBalances.remove(newReward.getPayoutId());
            }
        }
        for (Reward r : rewardData.unRedeemedBalances.values()) {
            Log.w("Unredeemed balance still exists: " + r.getPayoutId());
        }
    }

    private void getRedeemedCurrencyBalances_OLD(RedeemRewardData data, List<Receipt> receipts) {
        data.receipts = new ArrayList();
        data.redeemedBalances = new ArrayList();
        Map<String, Reward> balances = new HashMap();
        for (Receipt receipt : receipts) {
            Reward balance = (Reward) data.unRedeemedBalances.get(receipt.getPayoutId());
            if (balance != null) {
                if (!(TextUtils.isEmpty(receipt.getReceiptId()) || data.receipts.contains(receipt.getReceiptId()))) {
                    data.receipts.add(receipt.getReceiptId());
                }
                Reward storedBalance = (Reward) balances.get(balance.getRewardId());
                if (storedBalance == null) {
                    storedBalance = new Reward(balance);
                    storedBalance.setAmount(0.0d);
                    balances.put(balance.getRewardId(), storedBalance);
                }
                storedBalance.setAmount(storedBalance.getAmount() + balance.getAmount());
                data.unRedeemedBalances.remove(balance.getPayoutId());
            }
        }
        for (Reward b : balances.values()) {
            data.redeemedBalances.add(b);
        }
        for (Reward r : data.unRedeemedBalances.values()) {
            Log.w("Unredeemed balance still exists: " + r.getPayoutId());
        }
    }
}
