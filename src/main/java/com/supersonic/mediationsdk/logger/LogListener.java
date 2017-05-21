package com.supersonic.mediationsdk.logger;

import com.supersonic.mediationsdk.logger.SupersonicLogger.SupersonicTag;

public interface LogListener {
    void onLog(SupersonicTag supersonicTag, String str, int i);
}
