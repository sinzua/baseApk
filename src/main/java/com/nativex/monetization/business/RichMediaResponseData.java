package com.nativex.monetization.business;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RichMediaResponseData {
    @SerializedName("CDNs")
    private List<String> CDNs;
    @SerializedName("Content")
    private String base64HtmlContent;
    @SerializedName("OfferIds")
    private List<String> offerIds;
    @SerializedName("ViewTimeout")
    private long viewTimeOut;

    public List<String> getOfferIds() {
        return this.offerIds;
    }

    public long getViewTimeOut() {
        return this.viewTimeOut;
    }

    public String getBase64HtmlContent() {
        return this.base64HtmlContent;
    }
}
