package com.parse;

import bolts.Continuation;
import bolts.Task;
import bolts.Task.TaskCompletionSource;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseFile {
    static final int MAX_FILE_SIZE = 10485760;
    private Set<TaskCompletionSource> currentTasks;
    byte[] data;
    private State state;
    final TaskQueue taskQueue;

    static class State {
        private final String contentType;
        private final String name;
        private final String url;

        static class Builder {
            private String mimeType;
            private String name;
            private String url;

            public Builder(State state) {
                this.name = state.name();
                this.mimeType = state.mimeType();
                this.url = state.url();
            }

            public Builder name(String name) {
                this.name = name;
                return this;
            }

            public Builder mimeType(String mimeType) {
                this.mimeType = mimeType;
                return this;
            }

            public Builder url(String url) {
                this.url = url;
                return this;
            }

            public State build() {
                return new State();
            }
        }

        private State(Builder builder) {
            this.name = builder.name != null ? builder.name : ParametersKeys.FILE;
            this.contentType = builder.mimeType;
            this.url = builder.url;
        }

        public String name() {
            return this.name;
        }

        public String mimeType() {
            return this.contentType;
        }

        public String url() {
            return this.url;
        }
    }

    static ParseFileController getFileController() {
        return ParseCorePlugins.getInstance().getFileController();
    }

    private static ProgressCallback progressCallbackOnMainThread(final ProgressCallback progressCallback) {
        if (progressCallback == null) {
            return null;
        }
        return new ProgressCallback() {
            public void done(final Integer percentDone) {
                Task.call(new Callable<Void>() {
                    public Void call() throws Exception {
                        progressCallback.done(percentDone);
                        return null;
                    }
                }, ParseExecutors.main());
            }
        };
    }

    public ParseFile(String name, byte[] data, String contentType) {
        this(new Builder().name(name).mimeType(contentType).build());
        if (data.length > 10485760) {
            throw new IllegalArgumentException(String.format("ParseFile must be less than %d bytes", new Object[]{Integer.valueOf(10485760)}));
        } else {
            this.data = data;
        }
    }

    public ParseFile(byte[] data) {
        this(null, data, null);
    }

    public ParseFile(String name, byte[] data) {
        this(name, data, null);
    }

    public ParseFile(byte[] data, String contentType) {
        this(null, data, contentType);
    }

    ParseFile(State state) {
        this.taskQueue = new TaskQueue();
        this.currentTasks = Collections.synchronizedSet(new HashSet());
        this.state = state;
    }

    State getState() {
        return this.state;
    }

    public String getName() {
        return this.state.name();
    }

    public boolean isDirty() {
        return this.state.url() == null;
    }

    public boolean isDataAvailable() {
        return this.data != null || getFileController().isDataAvailable(this.state) || isPinnedDataAvailable();
    }

    public String getUrl() {
        return this.state.url();
    }

    static File getFilesDir() {
        return Parse.getParseFilesDir("files");
    }

    private String getFilename() {
        return this.state.name();
    }

    File getCacheFile() {
        return getFileController().getCacheFile(this.state);
    }

    File getFilesFile() {
        String filename = getFilename();
        return filename != null ? new File(getFilesDir(), filename) : null;
    }

    boolean isPinned() {
        File file = getFilesFile();
        return file != null && file.exists();
    }

    private boolean isPinnedDataAvailable() {
        return getFilesFile().exists();
    }

    void pin() throws ParseException {
        setPinned(true);
    }

    void unpin() throws ParseException {
        setPinned(false);
    }

    Task<Void> pinInBackground() {
        return setPinnedInBackground(true);
    }

    Task<Void> unpinInBackground() {
        return setPinnedInBackground(false);
    }

    void pinInBackground(ParseCallback1<ParseException> callback) {
        setPinnedInBackground(true, callback);
    }

    void unpinInBackground(ParseCallback1<ParseException> callback) {
        setPinnedInBackground(false, callback);
    }

    private void setPinned(boolean pinned) throws ParseException {
        ParseTaskUtils.wait(setPinnedInBackground(pinned));
    }

    private void setPinnedInBackground(boolean pinned, ParseCallback1<ParseException> callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(setPinnedInBackground(pinned), (ParseCallback1) callback);
    }

    private Task<Void> setPinnedInBackground(final boolean pinned) {
        return this.taskQueue.enqueue(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> task) throws Exception {
                return task;
            }
        }).continueWith(new Continuation<Void, Void>() {
            public Void then(Task<Void> task) throws Exception {
                if (ParseFile.this.state.url() == null) {
                    throw new IllegalStateException("Unable to pin file before saving");
                }
                if (!(pinned && ParseFile.this.isPinned()) && (pinned || ParseFile.this.isPinned())) {
                    File src;
                    File dest;
                    if (pinned) {
                        src = ParseFile.this.getCacheFile();
                        dest = ParseFile.this.getFilesFile();
                    } else {
                        src = ParseFile.this.getFilesFile();
                        dest = ParseFile.this.getCacheFile();
                    }
                    if (dest.exists()) {
                        ParseFileUtils.deleteQuietly(dest);
                    }
                    if (pinned && ParseFile.this.data != null) {
                        ParseFileUtils.writeByteArrayToFile(dest, ParseFile.this.data);
                        if (src.exists()) {
                            ParseFileUtils.deleteQuietly(src);
                        }
                    } else if (src == null || !src.exists()) {
                        throw new IllegalStateException("Unable to pin file before retrieving");
                    } else {
                        ParseFileUtils.moveFile(src, dest);
                    }
                }
                return null;
            }
        }, Task.BACKGROUND_EXECUTOR);
    }

    public void save() throws ParseException {
        ParseTaskUtils.wait(saveInBackground());
    }

    private Task<Void> saveAsync(final String sessionToken, final ProgressCallback uploadProgressCallback, Task<Void> toAwait, final Task<Void> cancellationToken) {
        if (!isDirty()) {
            return Task.forResult(null);
        }
        if (cancellationToken == null || !cancellationToken.isCancelled()) {
            return toAwait.continueWithTask(new Continuation<Void, Task<Void>>() {
                public Task<Void> then(Task<Void> task) throws Exception {
                    if (!ParseFile.this.isDirty()) {
                        return Task.forResult(null);
                    }
                    if (cancellationToken == null || !cancellationToken.isCancelled()) {
                        return ParseFile.getFileController().saveAsync(ParseFile.this.state, ParseFile.this.data, sessionToken, ParseFile.progressCallbackOnMainThread(uploadProgressCallback), cancellationToken).onSuccessTask(new Continuation<State, Task<Void>>() {
                            public Task<Void> then(Task<State> task) throws Exception {
                                ParseFile.this.state = (State) task.getResult();
                                return task.makeVoid();
                            }
                        });
                    }
                    return Task.cancelled();
                }
            });
        }
        return Task.cancelled();
    }

    public Task<Void> saveInBackground(final ProgressCallback uploadProgressCallback) {
        final TaskCompletionSource cts = Task.create();
        this.currentTasks.add(cts);
        return ParseUser.getCurrentSessionTokenAsync().onSuccessTask(new Continuation<String, Task<Void>>() {
            public Task<Void> then(Task<String> task) throws Exception {
                return ParseFile.this.saveAsync((String) task.getResult(), uploadProgressCallback, cts.getTask());
            }
        }).continueWithTask(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> task) throws Exception {
                cts.trySetResult(null);
                ParseFile.this.currentTasks.remove(cts);
                return task;
            }
        });
    }

    Task<Void> saveAsync(final String sessionToken, final ProgressCallback uploadProgressCallback, final Task<Void> cancellationToken) {
        return this.taskQueue.enqueue(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> toAwait) throws Exception {
                return ParseFile.this.saveAsync(sessionToken, uploadProgressCallback, toAwait, cancellationToken);
            }
        });
    }

    public Task<Void> saveInBackground() {
        return saveInBackground((ProgressCallback) null);
    }

    public void saveInBackground(SaveCallback saveCallback, ProgressCallback progressCallback) {
        ParseTaskUtils.callbackOnMainThreadAsync(saveInBackground(progressCallback), (ParseCallback1) saveCallback);
    }

    public void saveInBackground(SaveCallback callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(saveInBackground(), (ParseCallback1) callback);
    }

    public byte[] getData() throws ParseException {
        return (byte[]) ParseTaskUtils.wait(getDataInBackground());
    }

    private Task<byte[]> getDataAsync(final ProgressCallback progressCallback, Task<Void> toAwait, final Task<Void> cancellationToken) {
        if (this.data != null) {
            return Task.forResult(this.data);
        }
        if (cancellationToken == null || !cancellationToken.isCancelled()) {
            return toAwait.continueWithTask(new Continuation<Void, Task<byte[]>>() {
                public Task<byte[]> then(Task<Void> task) throws Exception {
                    if (ParseFile.this.data != null) {
                        return Task.forResult(ParseFile.this.data);
                    }
                    if (cancellationToken == null || !cancellationToken.isCancelled()) {
                        return ParseFile.getFileController().fetchAsync(ParseFile.this.state, null, ParseFile.progressCallbackOnMainThread(progressCallback), cancellationToken).onSuccess(new Continuation<File, byte[]>() {
                            public byte[] then(Task<File> task) throws Exception {
                                File file = (File) task.getResult();
                                try {
                                    ParseFile.this.data = ParseFileUtils.readFileToByteArray(file);
                                    return ParseFile.this.data;
                                } catch (IOException e) {
                                    return null;
                                }
                            }
                        });
                    }
                    return Task.cancelled();
                }
            });
        }
        return Task.cancelled();
    }

    public Task<byte[]> getDataInBackground(final ProgressCallback progressCallback) {
        final TaskCompletionSource cts = Task.create();
        this.currentTasks.add(cts);
        return this.taskQueue.enqueue(new Continuation<Void, Task<byte[]>>() {
            public Task<byte[]> then(Task<Void> toAwait) throws Exception {
                return ParseFile.this.getDataAsync(progressCallback, toAwait, cts.getTask());
            }
        }).continueWithTask(new Continuation<byte[], Task<byte[]>>() {
            public Task<byte[]> then(Task<byte[]> task) throws Exception {
                cts.trySetResult(null);
                ParseFile.this.currentTasks.remove(cts);
                return task;
            }
        });
    }

    public Task<byte[]> getDataInBackground() {
        return getDataInBackground((ProgressCallback) null);
    }

    public void getDataInBackground(GetDataCallback dataCallback, ProgressCallback progressCallback) {
        ParseTaskUtils.callbackOnMainThreadAsync(getDataInBackground(progressCallback), (ParseCallback2) dataCallback);
    }

    public void getDataInBackground(GetDataCallback dataCallback) {
        ParseTaskUtils.callbackOnMainThreadAsync(getDataInBackground(), (ParseCallback2) dataCallback);
    }

    public void cancel() {
        Set<TaskCompletionSource> tasks = new HashSet(this.currentTasks);
        for (TaskCompletionSource tcs : tasks) {
            tcs.trySetCancelled();
        }
        this.currentTasks.removeAll(tasks);
    }

    ParseFile(JSONObject json, ParseDecoder decoder) {
        this(new Builder().name(json.optString("name")).url(json.optString(ParametersKeys.URL)).build());
    }

    JSONObject encode() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("__type", "File");
        json.put("name", getName());
        if (getUrl() == null) {
            throw new IllegalStateException("Unable to encode an unsaved ParseFile.");
        }
        json.put(ParametersKeys.URL, getUrl());
        return json;
    }
}
