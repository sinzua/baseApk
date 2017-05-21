package com.nativex.monetization.listeners;

import com.nativex.monetization.enums.AdEvent;

public interface OnAdEvent extends OnAdEventBase {
    void onEvent(AdEvent adEvent, String str);
}
