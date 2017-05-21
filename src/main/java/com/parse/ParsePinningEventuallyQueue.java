package com.parse;

import android.content.Context;
import android.content.Intent;
import bolts.Continuation;
import bolts.Task;
import bolts.Task.TaskCompletionSource;
import com.parse.ConnectivityNotifier.ConnectivityListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;

class ParsePinningEventuallyQueue extends ParseEventuallyQueue {
    private static final String TAG = "ParsePinningEventuallyQueue";
    private final Object connectionLock = new Object();
    private TaskCompletionSource connectionTaskCompletionSource = Task.create();
    private ArrayList<String> eventuallyPinUUIDQueue = new ArrayList();
    private ConnectivityListener listener = new ConnectivityListener() {
        public void networkConnectivityStatusChanged(Context context, Intent intent) {
            if (intent.getBooleanExtra("noConnectivity", false)) {
                ParsePinningEventuallyQueue.this.setConnected(false);
            } else {
                ParsePinningEventuallyQueue.this.setConnected(ConnectivityNotifier.isConnected(context));
            }
        }
    };
    private ConnectivityNotifier notifier;
    private TaskQueue operationSetTaskQueue = new TaskQueue();
    private HashMap<String, TaskCompletionSource> pendingEventuallyTasks = new HashMap();
    private HashMap<String, TaskCompletionSource> pendingOperationSetUUIDTasks = new HashMap();
    private TaskQueue taskQueue = new TaskQueue();
    private final Object taskQueueSyncLock = new Object();
    private HashMap<String, EventuallyPin> uuidToEventuallyPin = new HashMap();
    private HashMap<String, ParseOperationSet> uuidToOperationSet = new HashMap();

    private static class PauseException extends Exception {
        private PauseException() {
        }
    }

    public ParsePinningEventuallyQueue(Context context) {
        setConnected(ConnectivityNotifier.isConnected(context));
        this.notifier = ConnectivityNotifier.getNotifier(context);
        this.notifier.addListener(this.listener);
        resume();
    }

    public void onDestroy() {
        this.notifier.removeListener(this.listener);
    }

    public void setConnected(boolean connected) {
        synchronized (this.connectionLock) {
            if (isConnected() != connected) {
                super.setConnected(connected);
                if (connected) {
                    this.connectionTaskCompletionSource.trySetResult(null);
                    this.connectionTaskCompletionSource = Task.create();
                    this.connectionTaskCompletionSource.trySetResult(null);
                } else {
                    this.connectionTaskCompletionSource = Task.create();
                }
            }
        }
    }

    public int pendingCount() {
        try {
            return ((Integer) ParseTaskUtils.wait(pendingCountAsync())).intValue();
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }

    public Task<Integer> pendingCountAsync() {
        final TaskCompletionSource tcs = Task.create();
        this.taskQueue.enqueue(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> toAwait) throws Exception {
                return ParsePinningEventuallyQueue.this.pendingCountAsync(toAwait).continueWithTask(new Continuation<Integer, Task<Void>>() {
                    public Task<Void> then(Task<Integer> task) throws Exception {
                        tcs.setResult(Integer.valueOf(((Integer) task.getResult()).intValue()));
                        return Task.forResult(null);
                    }
                });
            }
        });
        return tcs.getTask();
    }

    public Task<Integer> pendingCountAsync(Task<Void> toAwait) {
        return toAwait.continueWithTask(new Continuation<Void, Task<Integer>>() {
            public Task<Integer> then(Task<Void> task) throws Exception {
                return EventuallyPin.findAllPinned().continueWithTask(new Continuation<List<EventuallyPin>, Task<Integer>>() {
                    public Task<Integer> then(Task<List<EventuallyPin>> task) throws Exception {
                        return Task.forResult(Integer.valueOf(((List) task.getResult()).size()));
                    }
                });
            }
        });
    }

    public void pause() {
        synchronized (this.connectionLock) {
            this.connectionTaskCompletionSource.trySetError(new PauseException());
            this.connectionTaskCompletionSource = Task.create();
            this.connectionTaskCompletionSource.trySetError(new PauseException());
        }
        synchronized (this.taskQueueSyncLock) {
            for (String key : this.pendingEventuallyTasks.keySet()) {
                ((TaskCompletionSource) this.pendingEventuallyTasks.get(key)).trySetError(new PauseException());
            }
            this.pendingEventuallyTasks.clear();
            this.uuidToOperationSet.clear();
            this.uuidToEventuallyPin.clear();
        }
        try {
            ParseTaskUtils.wait(whenAll(Arrays.asList(new TaskQueue[]{this.taskQueue, this.operationSetTaskQueue})));
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }

    public void resume() {
        if (isConnected()) {
            this.connectionTaskCompletionSource.trySetResult(null);
            this.connectionTaskCompletionSource = Task.create();
            this.connectionTaskCompletionSource.trySetResult(null);
        } else {
            this.connectionTaskCompletionSource = Task.create();
        }
        populateQueueAsync();
    }

    private Task<Void> waitForConnectionAsync() {
        Task<Void> task;
        synchronized (this.connectionLock) {
            task = this.connectionTaskCompletionSource.getTask();
        }
        return task;
    }

    public Task<JSONObject> enqueueEventuallyAsync(final ParseRESTCommand command, final ParseObject object) {
        Parse.requirePermission("android.permission.ACCESS_NETWORK_STATE");
        final TaskCompletionSource tcs = Task.create();
        this.taskQueue.enqueue(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> toAwait) throws Exception {
                return ParsePinningEventuallyQueue.this.enqueueEventuallyAsync(command, object, toAwait, tcs);
            }
        });
        return tcs.getTask();
    }

    private Task<Void> enqueueEventuallyAsync(final ParseRESTCommand command, final ParseObject object, Task<Void> toAwait, final TaskCompletionSource tcs) {
        return toAwait.continueWithTask(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> task) throws Exception {
                return EventuallyPin.pinEventuallyCommand(object, command).continueWithTask(new Continuation<EventuallyPin, Task<Void>>() {
                    public Task<Void> then(Task<EventuallyPin> task) throws Exception {
                        EventuallyPin pin = (EventuallyPin) task.getResult();
                        Exception error = task.getError();
                        if (error != null) {
                            if (5 >= Parse.getLogLevel()) {
                                PLog.w(ParsePinningEventuallyQueue.TAG, "Unable to save command for later.", error);
                            }
                            ParsePinningEventuallyQueue.this.notifyTestHelper(4);
                            return Task.forResult(null);
                        }
                        ParsePinningEventuallyQueue.this.pendingOperationSetUUIDTasks.put(pin.getUUID(), tcs);
                        ParsePinningEventuallyQueue.this.populateQueueAsync().continueWithTask(new Continuation<Void, Task<Void>>() {
                            public Task<Void> then(Task<Void> task) throws Exception {
                                ParsePinningEventuallyQueue.this.notifyTestHelper(3);
                                return task;
                            }
                        });
                        return task.makeVoid();
                    }
                });
            }
        });
    }

    private Task<Void> populateQueueAsync() {
        return this.taskQueue.enqueue(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> toAwait) throws Exception {
                return ParsePinningEventuallyQueue.this.populateQueueAsync(toAwait);
            }
        });
    }

    private Task<Void> populateQueueAsync(Task<Void> toAwait) {
        return toAwait.continueWithTask(new Continuation<Void, Task<List<EventuallyPin>>>() {
            public Task<List<EventuallyPin>> then(Task<Void> task) throws Exception {
                return EventuallyPin.findAllPinned(ParsePinningEventuallyQueue.this.eventuallyPinUUIDQueue);
            }
        }).onSuccessTask(new Continuation<List<EventuallyPin>, Task<Void>>() {
            public Task<Void> then(Task<List<EventuallyPin>> task) throws Exception {
                for (EventuallyPin pin : (List) task.getResult()) {
                    ParsePinningEventuallyQueue.this.runEventuallyAsync(pin);
                }
                return task.makeVoid();
            }
        });
    }

    private Task<Void> runEventuallyAsync(final EventuallyPin eventuallyPin) {
        final String uuid = eventuallyPin.getUUID();
        if (this.eventuallyPinUUIDQueue.contains(uuid)) {
            return Task.forResult(null);
        }
        this.eventuallyPinUUIDQueue.add(uuid);
        this.operationSetTaskQueue.enqueue(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> toAwait) throws Exception {
                return ParsePinningEventuallyQueue.this.runEventuallyAsync(eventuallyPin, toAwait).continueWithTask(new Continuation<Void, Task<Void>>() {
                    public Task<Void> then(Task<Void> task) throws Exception {
                        ParsePinningEventuallyQueue.this.eventuallyPinUUIDQueue.remove(uuid);
                        return task;
                    }
                });
            }
        });
        return Task.forResult(null);
    }

    private Task<Void> runEventuallyAsync(final EventuallyPin eventuallyPin, Task<Void> toAwait) {
        return toAwait.continueWithTask(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> task) throws Exception {
                return ParsePinningEventuallyQueue.this.waitForConnectionAsync();
            }
        }).onSuccessTask(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> task) throws Exception {
                return ParsePinningEventuallyQueue.this.waitForOperationSetAndEventuallyPin(null, eventuallyPin).continueWithTask(new Continuation<JSONObject, Task<Void>>() {
                    public Task<Void> then(Task<JSONObject> task) throws Exception {
                        Exception error = task.getError();
                        if (error == null) {
                            ParsePinningEventuallyQueue.this.notifyTestHelper(1);
                        } else if (error instanceof PauseException) {
                            return task.makeVoid();
                        } else {
                            if (6 >= Parse.getLogLevel()) {
                                PLog.e(ParsePinningEventuallyQueue.TAG, "Failed to run command.", error);
                            }
                            ParsePinningEventuallyQueue.this.notifyTestHelper(2, error);
                        }
                        TaskCompletionSource tcs = (TaskCompletionSource) ParsePinningEventuallyQueue.this.pendingOperationSetUUIDTasks.remove(eventuallyPin.getUUID());
                        if (tcs != null) {
                            if (error != null) {
                                tcs.setError(error);
                            } else {
                                tcs.setResult(task.getResult());
                            }
                        }
                        return task.makeVoid();
                    }
                });
            }
        });
    }

    Task<JSONObject> waitForOperationSetAndEventuallyPin(ParseOperationSet operationSet, EventuallyPin eventuallyPin) {
        if (eventuallyPin != null && eventuallyPin.getType() != 1) {
            return process(eventuallyPin, null);
        }
        synchronized (this.taskQueueSyncLock) {
            String uuid;
            if (operationSet != null && eventuallyPin == null) {
                uuid = operationSet.getUUID();
                this.uuidToOperationSet.put(uuid, operationSet);
            } else if (operationSet != null || eventuallyPin == null) {
                throw new IllegalStateException("Either operationSet or eventuallyPin must be set.");
            } else {
                uuid = eventuallyPin.getOperationSetUUID();
                this.uuidToEventuallyPin.put(uuid, eventuallyPin);
            }
            eventuallyPin = (EventuallyPin) this.uuidToEventuallyPin.get(uuid);
            operationSet = (ParseOperationSet) this.uuidToOperationSet.get(uuid);
            TaskCompletionSource tcs;
            if (eventuallyPin == null || operationSet == null) {
                if (this.pendingEventuallyTasks.containsKey(uuid)) {
                    tcs = (TaskCompletionSource) this.pendingEventuallyTasks.get(uuid);
                } else {
                    tcs = Task.create();
                    this.pendingEventuallyTasks.put(uuid, tcs);
                }
                Task<JSONObject> task = tcs.getTask();
                return task;
            }
            tcs = (TaskCompletionSource) this.pendingEventuallyTasks.get(uuid);
            return process(eventuallyPin, operationSet).continueWithTask(new Continuation<JSONObject, Task<JSONObject>>() {
                public Task<JSONObject> then(Task<JSONObject> task) throws Exception {
                    synchronized (ParsePinningEventuallyQueue.this.taskQueueSyncLock) {
                        ParsePinningEventuallyQueue.this.pendingEventuallyTasks.remove(uuid);
                        ParsePinningEventuallyQueue.this.uuidToOperationSet.remove(uuid);
                        ParsePinningEventuallyQueue.this.uuidToEventuallyPin.remove(uuid);
                    }
                    Exception error = task.getError();
                    if (error != null) {
                        tcs.trySetError(error);
                    } else if (task.isCancelled()) {
                        tcs.trySetCancelled();
                    } else {
                        tcs.trySetResult(task.getResult());
                    }
                    return tcs.getTask();
                }
            });
        }
    }

    private Task<JSONObject> process(final EventuallyPin eventuallyPin, final ParseOperationSet operationSet) {
        return waitForConnectionAsync().onSuccessTask(new Continuation<Void, Task<JSONObject>>() {
            public Task<JSONObject> then(Task<Void> task) throws Exception {
                Task<JSONObject> executeTask;
                final int type = eventuallyPin.getType();
                final ParseObject object = eventuallyPin.getObject();
                String sessionToken = eventuallyPin.getSessionToken();
                if (type == 1) {
                    executeTask = object.saveAsync(operationSet, sessionToken);
                } else if (type == 2) {
                    executeTask = object.deleteAsync(sessionToken).cast();
                } else {
                    ParseRESTCommand command = eventuallyPin.getCommand();
                    if (command == null) {
                        executeTask = Task.forResult(null);
                        ParsePinningEventuallyQueue.this.notifyTestHelper(8);
                    } else {
                        executeTask = command.executeAsync();
                    }
                }
                return executeTask.continueWithTask(new Continuation<JSONObject, Task<JSONObject>>() {
                    public Task<JSONObject> then(final Task<JSONObject> executeTask) throws Exception {
                        Exception error = executeTask.getError();
                        if (error == null || !(error instanceof ParseException) || ((ParseException) error).getCode() != 100) {
                            return eventuallyPin.unpinInBackground(EventuallyPin.PIN_NAME).continueWithTask(new Continuation<Void, Task<Void>>() {
                                public Task<Void> then(Task<Void> task) throws Exception {
                                    JSONObject result = (JSONObject) executeTask.getResult();
                                    if (type == 1) {
                                        return object.handleSaveEventuallyResultAsync(result, operationSet);
                                    }
                                    if (type != 2 || executeTask.isFaulted()) {
                                        return task;
                                    }
                                    return object.handleDeleteEventuallyResultAsync();
                                }
                            }).continueWithTask(new Continuation<Void, Task<JSONObject>>() {
                                public Task<JSONObject> then(Task<Void> task) throws Exception {
                                    return executeTask;
                                }
                            });
                        }
                        ParsePinningEventuallyQueue.this.setConnected(false);
                        ParsePinningEventuallyQueue.this.notifyTestHelper(7);
                        return ParsePinningEventuallyQueue.this.process(eventuallyPin, operationSet);
                    }
                });
            }
        });
    }

    void simulateReboot() {
        pause();
        this.pendingOperationSetUUIDTasks.clear();
        this.pendingEventuallyTasks.clear();
        this.uuidToOperationSet.clear();
        this.uuidToEventuallyPin.clear();
        resume();
    }

    public void clear() {
        pause();
        try {
            ParseTaskUtils.wait(this.taskQueue.enqueue(new Continuation<Void, Task<Void>>() {
                public Task<Void> then(Task<Void> toAwait) throws Exception {
                    return toAwait.continueWithTask(new Continuation<Void, Task<Void>>() {
                        public Task<Void> then(Task<Void> task) throws Exception {
                            return EventuallyPin.findAllPinned().onSuccessTask(new Continuation<List<EventuallyPin>, Task<Void>>() {
                                public Task<Void> then(Task<List<EventuallyPin>> task) throws Exception {
                                    List<EventuallyPin> pins = (List) task.getResult();
                                    List<Task<Void>> tasks = new ArrayList();
                                    for (EventuallyPin pin : pins) {
                                        tasks.add(pin.unpinInBackground(EventuallyPin.PIN_NAME));
                                    }
                                    return Task.whenAll(tasks);
                                }
                            });
                        }
                    });
                }
            }));
            simulateReboot();
            resume();
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }

    private Task<Void> whenAll(Collection<TaskQueue> taskQueues) {
        List<Task<Void>> tasks = new ArrayList();
        for (TaskQueue taskQueue : taskQueues) {
            tasks.add(taskQueue.enqueue(new Continuation<Void, Task<Void>>() {
                public Task<Void> then(Task<Void> toAwait) throws Exception {
                    return toAwait;
                }
            }));
        }
        return Task.whenAll(tasks);
    }
}
