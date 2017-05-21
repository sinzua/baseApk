package com.nativex.monetization.listeners;

import com.nativex.monetization.business.GetOfferCacheResponseData;

public interface OnGetCacheDownloadCompletedListener {
    void downloadComplete(GetOfferCacheResponseData getOfferCacheResponseData);
}
