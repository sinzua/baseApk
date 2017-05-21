package com.nativex.common.billingtracking;

import com.google.gson.annotations.SerializedName;
import com.nativex.common.LogItem;
import com.nativex.common.Message;
import com.nativex.common.Violation;
import java.util.List;

class InAppPurchaseResponseData {
    @SerializedName("Log")
    private List<LogItem> log;
    @SerializedName("Messages")
    private List<Message> messages;
    @SerializedName("IsSuccessful")
    private Boolean successful;
    @SerializedName("Violations")
    private List<Violation> violations;

    InAppPurchaseResponseData() {
    }

    public Boolean isSuccessful() {
        return this.successful;
    }
}
