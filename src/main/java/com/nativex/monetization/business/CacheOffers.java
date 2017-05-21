package com.nativex.monetization.business;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CacheOffers {
    @SerializedName("Files")
    private List<CacheFile> files;
    @SerializedName("OfferId")
    private long offerId;

    public List<CacheFile> getFiles() {
        return this.files;
    }

    public void setFiles(List<CacheFile> files) {
        this.files = files;
    }

    public long getOfferId() {
        return this.offerId;
    }
}
