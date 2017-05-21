package com.nativex.monetization.business;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GetOfferCacheResponseData {
    @SerializedName("CacheSizeMax")
    private int cacheSizeMax;
    @SerializedName("FreeSpaceMin")
    private Integer freeSpaceMinMegabytes;
    @SerializedName("GlobalFiles")
    private List<CacheFile> globalFiles;
    @SerializedName("Offers")
    private List<CacheOffers> offers = null;

    public int getCacheSizeMax() {
        return this.cacheSizeMax;
    }

    public Integer getFreeSpaceMinMegabytes() {
        return this.freeSpaceMinMegabytes;
    }

    public List<CacheOffers> getOffers() {
        return this.offers;
    }
}
