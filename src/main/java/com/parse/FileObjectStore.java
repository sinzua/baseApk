package com.parse;

import bolts.Task;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import org.json.JSONException;

class FileObjectStore<T extends ParseObject> implements ParseObjectStore<T> {
    private final String className;
    private final ParseObjectCurrentCoder coder;
    private final File file;

    private static void saveToDisk(ParseObjectCurrentCoder coder, ParseObject current, File file) {
        try {
            ParseFileUtils.writeJSONObjectToFile(file, coder.encode(current.getState(), null, PointerEncoder.get()));
        } catch (IOException e) {
        }
    }

    private static <T extends ParseObject> T getFromDisk(ParseObjectCurrentCoder coder, File file, Init builder) {
        try {
            return ParseObject.from(coder.decode(builder, ParseFileUtils.readFileToJSONObject(file), ParseDecoder.get()).isComplete(true).build());
        } catch (IOException e) {
            return null;
        } catch (JSONException e2) {
            return null;
        }
    }

    public FileObjectStore(Class<T> clazz, File file, ParseObjectCurrentCoder coder) {
        this(ParseObject.getClassName(clazz), file, coder);
    }

    public FileObjectStore(String className, File file, ParseObjectCurrentCoder coder) {
        this.className = className;
        this.file = file;
        this.coder = coder;
    }

    public Task<Void> setAsync(final T object) {
        return Task.call(new Callable<Void>() {
            public Void call() throws Exception {
                FileObjectStore.saveToDisk(FileObjectStore.this.coder, object, FileObjectStore.this.file);
                return null;
            }
        }, ParseExecutors.io());
    }

    public Task<T> getAsync() {
        return Task.call(new Callable<T>() {
            public T call() throws Exception {
                if (FileObjectStore.this.file.exists()) {
                    return FileObjectStore.getFromDisk(FileObjectStore.this.coder, FileObjectStore.this.file, State.newBuilder(FileObjectStore.this.className));
                }
                return null;
            }
        }, ParseExecutors.io());
    }

    public Task<Boolean> existsAsync() {
        return Task.call(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return Boolean.valueOf(FileObjectStore.this.file.exists());
            }
        }, ParseExecutors.io());
    }

    public Task<Void> deleteAsync() {
        return Task.call(new Callable<Void>() {
            public Void call() throws Exception {
                if (!FileObjectStore.this.file.exists() || ParseFileUtils.deleteQuietly(FileObjectStore.this.file)) {
                    return null;
                }
                throw new RuntimeException("Unable to delete");
            }
        }, ParseExecutors.io());
    }
}
