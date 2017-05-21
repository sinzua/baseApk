package com.parse;

import bolts.Task;
import java.util.List;

class OfflineQueryController extends AbstractQueryController {
    private final ParseQueryController networkController;
    private final OfflineStore offlineStore;

    public OfflineQueryController(OfflineStore store, ParseQueryController network) {
        this.offlineStore = store;
        this.networkController = network;
    }

    public <T extends ParseObject> Task<List<T>> findAsync(State<T> state, ParseUser user, Task<Void> cancellationToken) {
        if (state.isFromLocalDatastore()) {
            return this.offlineStore.findFromPinAsync(state.pinName(), state, user);
        }
        return this.networkController.findAsync(state, user, cancellationToken);
    }

    public <T extends ParseObject> Task<Integer> countAsync(State<T> state, ParseUser user, Task<Void> cancellationToken) {
        if (state.isFromLocalDatastore()) {
            return this.offlineStore.countFromPinAsync(state.pinName(), state, user);
        }
        return this.networkController.countAsync(state, user, cancellationToken);
    }
}
