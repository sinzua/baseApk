package com.nativex.monetization.communication;

import android.app.Activity;
import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import com.nativex.common.Message;
import com.nativex.monetization.business.reward.Reward;
import com.nativex.monetization.manager.DialogManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RedeemRewardData {
    @SerializedName("messages")
    List<Message> messages;
    @SerializedName("receipts")
    List<String> receipts;
    @SerializedName("balances")
    List<Reward> redeemedBalances;
    Map<String, Reward> unRedeemedBalances;

    String[] getUnredeemedPayoutIds() {
        List<String> payoutIds = new ArrayList();
        if (this.unRedeemedBalances != null && this.unRedeemedBalances.size() > 0) {
            for (Reward balance : this.unRedeemedBalances.values()) {
                if (!TextUtils.isEmpty(balance.getPayoutId())) {
                    payoutIds.add(balance.getPayoutId());
                }
            }
        }
        return (String[]) payoutIds.toArray(new String[payoutIds.size()]);
    }

    public String[] getReceipts() {
        return (String[]) this.receipts.toArray(new String[this.receipts.size()]);
    }

    public void showAlert(Activity activity) {
        if (this.messages != null) {
            DialogManager.getInstance().showMessagesDialog(activity, this.messages, new ArrayList());
        }
    }

    public Reward[] getRewards() {
        if (this.redeemedBalances != null) {
            return (Reward[]) this.redeemedBalances.toArray(new Reward[this.redeemedBalances.size()]);
        }
        return null;
    }
}
