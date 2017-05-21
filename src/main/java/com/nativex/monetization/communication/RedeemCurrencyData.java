package com.nativex.monetization.communication;

import android.app.Activity;
import com.google.gson.annotations.SerializedName;
import com.nativex.common.Message;
import com.nativex.monetization.business.reward.Balance;
import com.nativex.monetization.business.reward.Reward;
import com.nativex.monetization.manager.DialogManager;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class RedeemCurrencyData {
    @SerializedName("messages")
    private List<Message> messages;
    @SerializedName("receipts")
    private List<String> receipts;
    @SerializedName("balances")
    private List<Balance> redeemedBalances = new ArrayList();

    public RedeemCurrencyData(RedeemRewardData d) {
        this.receipts = new ArrayList(d.receipts);
        this.messages = new ArrayList(d.messages);
        for (Reward r : d.getRewards()) {
            this.redeemedBalances.add(new Balance(r));
        }
    }

    public List<Balance> getBalances() {
        return new ArrayList(this.redeemedBalances);
    }

    public List<String> getReceipts() {
        return new ArrayList(this.receipts);
    }

    public void showAlert(Activity activity) {
        if (this.messages != null) {
            DialogManager.getInstance().showMessagesDialog(activity, this.messages, new ArrayList());
        }
    }
}
