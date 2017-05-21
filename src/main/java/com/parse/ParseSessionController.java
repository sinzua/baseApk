package com.parse;

import bolts.Task;

interface ParseSessionController {
    Task<State> getSessionAsync(String str);

    Task<Void> revokeAsync(String str);

    Task<State> upgradeToRevocable(String str);
}
