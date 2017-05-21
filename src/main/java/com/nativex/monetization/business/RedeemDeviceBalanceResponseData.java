package com.nativex.monetization.business;

import com.google.gson.annotations.SerializedName;
import com.nativex.common.LogItem;
import com.nativex.common.Message;
import com.nativex.common.Violation;
import java.util.List;

public class RedeemDeviceBalanceResponseData {
    @SerializedName("Log")
    private List<LogItem> log;
    @SerializedName("Messages")
    private List<Message> messages;
    @SerializedName("Receipts")
    private List<Receipt> receipts;
    @SerializedName("Violations")
    private List<Violation> violations;

    public List<Message> getMessages() {
        return this.messages;
    }

    public List<Receipt> getReceipts() {
        return this.receipts;
    }
}
