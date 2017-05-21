package com.parse;

import bolts.Continuation;
import bolts.Task;

class CachedCurrentInstallationController implements ParseCurrentInstallationController {
    static final String TAG = "com.parse.CachedCurrentInstallationController";
    ParseInstallation currentInstallation;
    private final InstallationId installationId;
    private final Object mutex = new Object();
    private final ParseObjectStore<ParseInstallation> store;
    private final TaskQueue taskQueue = new TaskQueue();

    public CachedCurrentInstallationController(ParseObjectStore<ParseInstallation> store, InstallationId installationId) {
        this.store = store;
        this.installationId = installationId;
    }

    public Task<Void> setAsync(final ParseInstallation installation) {
        if (isCurrent(installation)) {
            return this.taskQueue.enqueue(new Continuation<Void, Task<Void>>() {
                public Task<Void> then(Task<Void> toAwait) throws Exception {
                    return toAwait.continueWithTask(new Continuation<Void, Task<Void>>() {
                        public Task<Void> then(Task<Void> task) throws Exception {
                            return CachedCurrentInstallationController.this.store.setAsync(installation);
                        }
                    }).continueWithTask(new Continuation<Void, Task<Void>>() {
                        public Task<Void> then(Task<Void> task) throws Exception {
                            CachedCurrentInstallationController.this.installationId.set(installation.getInstallationId());
                            return task;
                        }
                    }, ParseExecutors.io());
                }
            });
        }
        return Task.forResult(null);
    }

    public Task<ParseInstallation> getAsync() {
        synchronized (this.mutex) {
            ParseInstallation cachedCurrent = this.currentInstallation;
        }
        if (cachedCurrent != null) {
            return Task.forResult(cachedCurrent);
        }
        return this.taskQueue.enqueue(new Continuation<Void, Task<ParseInstallation>>() {
            public Task<ParseInstallation> then(Task<Void> toAwait) throws Exception {
                return toAwait.continueWithTask(new Continuation<Void, Task<ParseInstallation>>() {
                    public Task<ParseInstallation> then(Task<Void> task) throws Exception {
                        return CachedCurrentInstallationController.this.store.getAsync();
                    }
                }).continueWith(new Continuation<ParseInstallation, ParseInstallation>() {
                    public ParseInstallation then(Task<ParseInstallation> task) throws Exception {
                        ParseInstallation current = (ParseInstallation) task.getResult();
                        if (current == null) {
                            current = (ParseInstallation) ParseObject.create(ParseInstallation.class);
                            current.updateDeviceInfo(CachedCurrentInstallationController.this.installationId);
                        } else {
                            CachedCurrentInstallationController.this.installationId.set(current.getInstallationId());
                            PLog.v(CachedCurrentInstallationController.TAG, "Successfully deserialized Installation object");
                        }
                        synchronized (CachedCurrentInstallationController.this.mutex) {
                            CachedCurrentInstallationController.this.currentInstallation = current;
                        }
                        return current;
                    }
                }, ParseExecutors.io());
            }
        });
    }

    public Task<Boolean> existsAsync() {
        synchronized (this.mutex) {
            if (this.currentInstallation != null) {
                Task<Boolean> forResult = Task.forResult(Boolean.valueOf(true));
                return forResult;
            }
            return this.taskQueue.enqueue(new Continuation<Void, Task<Boolean>>() {
                public Task<Boolean> then(Task<Void> toAwait) throws Exception {
                    return toAwait.continueWithTask(new Continuation<Void, Task<Boolean>>() {
                        public Task<Boolean> then(Task<Void> task) throws Exception {
                            return CachedCurrentInstallationController.this.store.existsAsync();
                        }
                    });
                }
            });
        }
    }

    public void clearFromMemory() {
        synchronized (this.mutex) {
            this.currentInstallation = null;
        }
    }

    public void clearFromDisk() {
        synchronized (this.mutex) {
            this.currentInstallation = null;
        }
        try {
            this.installationId.clear();
            ParseTaskUtils.wait(this.store.deleteAsync());
        } catch (ParseException e) {
        }
    }

    public boolean isCurrent(ParseInstallation installation) {
        boolean z;
        synchronized (this.mutex) {
            z = this.currentInstallation == installation;
        }
        return z;
    }
}
