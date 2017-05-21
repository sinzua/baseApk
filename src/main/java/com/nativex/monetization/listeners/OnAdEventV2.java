package com.nativex.monetization.listeners;

import com.nativex.monetization.enums.AdEvent;
import com.nativex.monetization.mraid.AdInfo;

public interface OnAdEventV2 extends OnAdEventBase {
    void onEvent(AdEvent adEvent, AdInfo adInfo, String str);
}
