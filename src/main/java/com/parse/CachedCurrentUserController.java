package com.parse;

import bolts.Continuation;
import bolts.Task;
import java.util.Arrays;
import java.util.Map;

class CachedCurrentUserController implements ParseCurrentUserController {
    ParseUser currentUser;
    boolean currentUserMatchesDisk = false;
    private final Object mutex = new Object();
    private final ParseObjectStore<ParseUser> store;
    private final TaskQueue taskQueue = new TaskQueue();

    public CachedCurrentUserController(ParseObjectStore<ParseUser> store) {
        this.store = store;
    }

    public Task<Void> setAsync(final ParseUser user) {
        return this.taskQueue.enqueue(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> toAwait) throws Exception {
                return toAwait.continueWithTask(new Continuation<Void, Task<Void>>() {
                    public Task<Void> then(Task<Void> task) throws Exception {
                        synchronized (CachedCurrentUserController.this.mutex) {
                            ParseUser oldCurrentUser = CachedCurrentUserController.this.currentUser;
                        }
                        if (!(oldCurrentUser == null || oldCurrentUser == user)) {
                            oldCurrentUser.logOutInternal();
                        }
                        synchronized (user.mutex) {
                            user.setIsCurrentUser(true);
                            user.synchronizeAllAuthData();
                        }
                        return CachedCurrentUserController.this.store.setAsync(user).continueWith(new Continuation<Void, Void>() {
                            public Void then(Task<Void> task) throws Exception {
                                synchronized (CachedCurrentUserController.this.mutex) {
                                    CachedCurrentUserController.this.currentUserMatchesDisk = !task.isFaulted();
                                    CachedCurrentUserController.this.currentUser = user;
                                }
                                return null;
                            }
                        });
                    }
                });
            }
        });
    }

    public Task<Void> setIfNeededAsync(ParseUser user) {
        synchronized (this.mutex) {
            if (!user.isCurrentUser() || this.currentUserMatchesDisk) {
                Task<Void> forResult = Task.forResult(null);
                return forResult;
            }
            return setAsync(user);
        }
    }

    public Task<ParseUser> getAsync() {
        return getAsync(ParseUser.isAutomaticUserEnabled());
    }

    public Task<Boolean> existsAsync() {
        synchronized (this.mutex) {
            if (this.currentUser != null) {
                Task<Boolean> forResult = Task.forResult(Boolean.valueOf(true));
                return forResult;
            }
            return this.taskQueue.enqueue(new Continuation<Void, Task<Boolean>>() {
                public Task<Boolean> then(Task<Void> toAwait) throws Exception {
                    return toAwait.continueWithTask(new Continuation<Void, Task<Boolean>>() {
                        public Task<Boolean> then(Task<Void> task) throws Exception {
                            return CachedCurrentUserController.this.store.existsAsync();
                        }
                    });
                }
            });
        }
    }

    public boolean isCurrent(ParseUser user) {
        boolean z;
        synchronized (this.mutex) {
            z = this.currentUser == user;
        }
        return z;
    }

    public void clearFromMemory() {
        synchronized (this.mutex) {
            this.currentUser = null;
            this.currentUserMatchesDisk = false;
        }
    }

    public void clearFromDisk() {
        synchronized (this.mutex) {
            this.currentUser = null;
            this.currentUserMatchesDisk = false;
        }
        try {
            ParseTaskUtils.wait(this.store.deleteAsync());
        } catch (ParseException e) {
        }
    }

    public Task<String> getCurrentSessionTokenAsync() {
        return getAsync(false).onSuccess(new Continuation<ParseUser, String>() {
            public String then(Task<ParseUser> task) throws Exception {
                ParseUser user = (ParseUser) task.getResult();
                return user != null ? user.getSessionToken() : null;
            }
        });
    }

    public Task<Void> logoutAsync() {
        return this.taskQueue.enqueue(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> toAwait) throws Exception {
                final Task<ParseUser> userTask = CachedCurrentUserController.this.getAsync(false);
                return Task.whenAll(Arrays.asList(new Task[]{userTask, toAwait})).continueWithTask(new Continuation<Void, Task<Void>>() {
                    public Task<Void> then(Task<Void> task) throws Exception {
                        Task<Void> logOutTask = userTask.onSuccessTask(new Continuation<ParseUser, Task<Void>>() {
                            public Task<Void> then(Task<ParseUser> task) throws Exception {
                                ParseUser user = (ParseUser) task.getResult();
                                if (user == null) {
                                    return task.cast();
                                }
                                return user.logOutAsync();
                            }
                        });
                        Task<Void> diskTask = CachedCurrentUserController.this.store.deleteAsync().continueWith(new Continuation<Void, Void>() {
                            public Void then(Task<Void> task) throws Exception {
                                boolean deleted = !task.isFaulted();
                                synchronized (CachedCurrentUserController.this.mutex) {
                                    CachedCurrentUserController.this.currentUserMatchesDisk = deleted;
                                    CachedCurrentUserController.this.currentUser = null;
                                }
                                return null;
                            }
                        });
                        return Task.whenAll(Arrays.asList(new Task[]{logOutTask, diskTask}));
                    }
                });
            }
        });
    }

    public Task<ParseUser> getAsync(final boolean shouldAutoCreateUser) {
        synchronized (this.mutex) {
            if (this.currentUser != null) {
                Task<ParseUser> forResult = Task.forResult(this.currentUser);
                return forResult;
            }
            return this.taskQueue.enqueue(new Continuation<Void, Task<ParseUser>>() {
                public Task<ParseUser> then(Task<Void> toAwait) throws Exception {
                    return toAwait.continueWithTask(new Continuation<Void, Task<ParseUser>>() {
                        public Task<ParseUser> then(Task<Void> task) throws Exception {
                            synchronized (CachedCurrentUserController.this.mutex) {
                                ParseUser current = CachedCurrentUserController.this.currentUser;
                                boolean matchesDisk = CachedCurrentUserController.this.currentUserMatchesDisk;
                            }
                            if (current != null) {
                                return Task.forResult(current);
                            }
                            if (!matchesDisk) {
                                return CachedCurrentUserController.this.store.getAsync().continueWith(new Continuation<ParseUser, ParseUser>() {
                                    public ParseUser then(Task<ParseUser> task) throws Exception {
                                        boolean matchesDisk;
                                        boolean z = true;
                                        ParseUser current = (ParseUser) task.getResult();
                                        if (task.isFaulted()) {
                                            matchesDisk = false;
                                        } else {
                                            matchesDisk = true;
                                        }
                                        synchronized (CachedCurrentUserController.this.mutex) {
                                            CachedCurrentUserController.this.currentUser = current;
                                            CachedCurrentUserController.this.currentUserMatchesDisk = matchesDisk;
                                        }
                                        if (current != null) {
                                            synchronized (current.mutex) {
                                                current.setIsCurrentUser(true);
                                                if (!(current.getObjectId() == null && ParseAnonymousUtils.isLinked(current))) {
                                                    z = false;
                                                }
                                                current.isLazy = z;
                                            }
                                            return current;
                                        } else if (shouldAutoCreateUser) {
                                            return CachedCurrentUserController.this.lazyLogIn();
                                        } else {
                                            return null;
                                        }
                                    }
                                });
                            }
                            if (shouldAutoCreateUser) {
                                return Task.forResult(CachedCurrentUserController.this.lazyLogIn());
                            }
                            return null;
                        }
                    });
                }
            });
        }
    }

    private ParseUser lazyLogIn() {
        AnonymousAuthenticationProvider provider = ParseAnonymousUtils.getProvider();
        return lazyLogIn(provider.getAuthType(), provider.getAuthData());
    }

    ParseUser lazyLogIn(String authType, Map<String, String> authData) {
        ParseUser user = (ParseUser) ParseObject.create(ParseUser.class);
        synchronized (user.mutex) {
            user.setIsCurrentUser(true);
            user.isLazy = true;
            user.putAuthData(authType, authData);
        }
        synchronized (this.mutex) {
            this.currentUserMatchesDisk = false;
            this.currentUser = user;
        }
        return user;
    }
}
