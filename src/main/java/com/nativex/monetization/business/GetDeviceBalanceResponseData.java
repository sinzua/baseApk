package com.nativex.monetization.business;

import com.google.gson.annotations.SerializedName;
import com.nativex.common.LogItem;
import com.nativex.common.Message;
import com.nativex.common.Violation;
import com.nativex.monetization.business.reward.Reward;
import java.util.List;

public class GetDeviceBalanceResponseData {
    @SerializedName("Balances")
    private List<Reward> balances = null;
    @SerializedName("Log")
    private List<LogItem> log = null;
    @SerializedName("Messages")
    private List<Message> messages = null;
    @SerializedName("Violations")
    private List<Violation> violations = null;

    public List<Reward> getBalances() {
        return this.balances;
    }
}
