package com.lidroid.xutils.cache;

import com.lidroid.xutils.util.IOUtils;
import com.lidroid.xutils.util.LogUtils;
import com.supersonicads.sdk.precache.DownloadManager;
import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.codehaus.jackson.util.BufferRecycler;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public final class LruDiskCache implements Closeable {
    static final long ANY_SEQUENCE_NUMBER = -1;
    private static final char CLEAN = 'C';
    private static final char DELETE = 'D';
    private static final char EXPIRY_PREFIX = 't';
    static final String JOURNAL_FILE = "journal";
    static final String JOURNAL_FILE_BACKUP = "journal.bkp";
    static final String JOURNAL_FILE_TEMP = "journal.tmp";
    static final String MAGIC = "libcore.io.DiskLruCache";
    private static final OutputStream NULL_OUTPUT_STREAM = new OutputStream() {
        public void write(int b) throws IOException {
        }
    };
    private static final char READ = 'R';
    private static final char UPDATE = 'U';
    static final String VERSION = "1";
    private final int appVersion;
    private final Callable<Void> cleanupCallable = new Callable<Void>() {
        public Void call() throws Exception {
            synchronized (LruDiskCache.this) {
                if (LruDiskCache.this.journalWriter == null) {
                } else {
                    LruDiskCache.this.trimToSize();
                    if (LruDiskCache.this.journalRebuildRequired()) {
                        LruDiskCache.this.rebuildJournal();
                        LruDiskCache.this.redundantOpCount = 0;
                    }
                }
            }
            return null;
        }
    };
    private final File directory;
    final ThreadPoolExecutor executorService = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue());
    private FileNameGenerator fileNameGenerator = new MD5FileNameGenerator();
    private final File journalFile;
    private final File journalFileBackup;
    private final File journalFileTmp;
    private Writer journalWriter;
    private final LinkedHashMap<String, Entry> lruEntries = new LinkedHashMap(0, 0.75f, true);
    private long maxSize;
    private long nextSequenceNumber = 0;
    private int redundantOpCount;
    private long size = 0;
    private final int valueCount;

    public final class Editor {
        private boolean committed;
        private final Entry entry;
        private boolean hasErrors;
        private final boolean[] written;

        private class FaultHidingOutputStream extends FilterOutputStream {
            private FaultHidingOutputStream(OutputStream out) {
                super(out);
            }

            public void write(int oneByte) {
                try {
                    this.out.write(oneByte);
                } catch (Throwable th) {
                    Editor.this.hasErrors = true;
                }
            }

            public void write(byte[] buffer, int offset, int length) {
                try {
                    this.out.write(buffer, offset, length);
                    this.out.flush();
                } catch (Throwable th) {
                    Editor.this.hasErrors = true;
                }
            }

            public void close() {
                try {
                    this.out.close();
                } catch (Throwable th) {
                    Editor.this.hasErrors = true;
                }
            }

            public void flush() {
                try {
                    this.out.flush();
                } catch (Throwable th) {
                    Editor.this.hasErrors = true;
                }
            }
        }

        private Editor(Entry entry) {
            this.entry = entry;
            this.written = entry.readable ? null : new boolean[LruDiskCache.this.valueCount];
        }

        public void setEntryExpiryTimestamp(long timestamp) {
            this.entry.expiryTimestamp = timestamp;
        }

        public InputStream newInputStream(int index) throws IOException {
            synchronized (LruDiskCache.this) {
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                } else if (this.entry.readable) {
                    try {
                        InputStream fileInputStream = new FileInputStream(this.entry.getCleanFile(index));
                        return fileInputStream;
                    } catch (FileNotFoundException e) {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        }

        public String getString(int index) throws IOException {
            InputStream in = newInputStream(index);
            return in != null ? LruDiskCache.inputStreamToString(in) : null;
        }

        public OutputStream newOutputStream(int index) throws IOException {
            OutputStream access$9;
            synchronized (LruDiskCache.this) {
                FileOutputStream outputStream;
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                }
                if (!this.entry.readable) {
                    this.written[index] = true;
                }
                dirtyFile = this.entry.getDirtyFile(index);
                try {
                    outputStream = new FileOutputStream(dirtyFile);
                } catch (FileNotFoundException e) {
                    LruDiskCache.this.directory.mkdirs();
                    try {
                        File dirtyFile;
                        outputStream = new FileOutputStream(dirtyFile);
                    } catch (FileNotFoundException e2) {
                        access$9 = LruDiskCache.NULL_OUTPUT_STREAM;
                    }
                }
                access$9 = new FaultHidingOutputStream(outputStream);
            }
            return access$9;
        }

        public void set(int index, String value) throws IOException {
            Throwable th;
            Closeable writer = null;
            try {
                Closeable writer2 = new OutputStreamWriter(newOutputStream(index), DownloadManager.UTF8_CHARSET);
                try {
                    writer2.write(value);
                    IOUtils.closeQuietly(writer2);
                } catch (Throwable th2) {
                    th = th2;
                    writer = writer2;
                    IOUtils.closeQuietly(writer);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                IOUtils.closeQuietly(writer);
                throw th;
            }
        }

        public void commit() throws IOException {
            if (this.hasErrors) {
                LruDiskCache.this.completeEdit(this, false);
                LruDiskCache.this.removeByDiskKey(this.entry.diskKey);
            } else {
                LruDiskCache.this.completeEdit(this, true);
            }
            this.committed = true;
        }

        public void abort() throws IOException {
            LruDiskCache.this.completeEdit(this, false);
        }

        public void abortUnlessCommitted() {
            if (!this.committed) {
                try {
                    abort();
                } catch (Throwable th) {
                }
            }
        }
    }

    private final class Entry {
        private Editor currentEditor;
        private final String diskKey;
        private long expiryTimestamp;
        private final long[] lengths;
        private boolean readable;
        private long sequenceNumber;

        private Entry(String diskKey) {
            this.expiryTimestamp = Long.MAX_VALUE;
            this.diskKey = diskKey;
            this.lengths = new long[LruDiskCache.this.valueCount];
        }

        public String getLengths() throws IOException {
            StringBuilder result = new StringBuilder();
            for (long size : this.lengths) {
                result.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(size);
            }
            return result.toString();
        }

        private void setLengths(String[] strings, int startIndex) throws IOException {
            if (strings.length - startIndex != LruDiskCache.this.valueCount) {
                throw invalidLengths(strings);
            }
            int i = 0;
            while (i < LruDiskCache.this.valueCount) {
                try {
                    this.lengths[i] = Long.parseLong(strings[i + startIndex]);
                    i++;
                } catch (NumberFormatException e) {
                    throw invalidLengths(strings);
                }
            }
        }

        private IOException invalidLengths(String[] strings) throws IOException {
            throw new IOException("unexpected journal line: " + Arrays.toString(strings));
        }

        public File getCleanFile(int i) {
            return new File(LruDiskCache.this.directory, this.diskKey + "." + i);
        }

        public File getDirtyFile(int i) {
            return new File(LruDiskCache.this.directory, this.diskKey + "." + i + ".tmp");
        }
    }

    public final class Snapshot implements Closeable {
        private final String diskKey;
        private final FileInputStream[] ins;
        private final long[] lengths;
        private final long sequenceNumber;

        private Snapshot(String diskKey, long sequenceNumber, FileInputStream[] ins, long[] lengths) {
            this.diskKey = diskKey;
            this.sequenceNumber = sequenceNumber;
            this.ins = ins;
            this.lengths = lengths;
        }

        public Editor edit() throws IOException {
            return LruDiskCache.this.editByDiskKey(this.diskKey, this.sequenceNumber);
        }

        public FileInputStream getInputStream(int index) {
            return this.ins[index];
        }

        public String getString(int index) throws IOException {
            return LruDiskCache.inputStreamToString(getInputStream(index));
        }

        public long getLength(int index) {
            return this.lengths[index];
        }

        public void close() {
            for (Closeable in : this.ins) {
                IOUtils.closeQuietly(in);
            }
        }
    }

    private class StrictLineReader implements Closeable {
        private static final byte CR = (byte) 13;
        private static final byte LF = (byte) 10;
        private byte[] buf;
        private final Charset charset;
        private int end;
        private final InputStream in;
        private int pos;

        public StrictLineReader(LruDiskCache lruDiskCache, InputStream in) {
            this(in, 8192);
        }

        public StrictLineReader(InputStream in, int capacity) {
            this.charset = Charset.forName("US-ASCII");
            if (in == null) {
                throw new NullPointerException();
            } else if (capacity < 0) {
                throw new IllegalArgumentException("capacity <= 0");
            } else {
                this.in = in;
                this.buf = new byte[capacity];
            }
        }

        public void close() throws IOException {
            synchronized (this.in) {
                if (this.buf != null) {
                    this.buf = null;
                    this.in.close();
                }
            }
        }

        public String readLine() throws IOException {
            String res;
            synchronized (this.in) {
                if (this.buf == null) {
                    throw new IOException("LineReader is closed");
                }
                if (this.pos >= this.end) {
                    fillBuf();
                }
                int i = this.pos;
                while (i != this.end) {
                    if (this.buf[i] == LF) {
                        int lineEnd;
                        if (i == this.pos || this.buf[i - 1] != CR) {
                            lineEnd = i;
                        } else {
                            lineEnd = i - 1;
                        }
                        res = new String(this.buf, this.pos, lineEnd - this.pos, this.charset.name());
                        this.pos = i + 1;
                    } else {
                        i++;
                    }
                }
                ByteArrayOutputStream out = new ByteArrayOutputStream((this.end - this.pos) + 80) {
                    public String toString() {
                        int length = (this.count <= 0 || this.buf[this.count - 1] != StrictLineReader.CR) ? this.count : this.count - 1;
                        try {
                            return new String(this.buf, 0, length, StrictLineReader.this.charset.name());
                        } catch (UnsupportedEncodingException e) {
                            throw new AssertionError(e);
                        }
                    }
                };
                loop1:
                while (true) {
                    out.write(this.buf, this.pos, this.end - this.pos);
                    this.end = -1;
                    fillBuf();
                    i = this.pos;
                    while (i != this.end) {
                        if (this.buf[i] == LF) {
                            break loop1;
                        }
                        i++;
                    }
                }
                if (i != this.pos) {
                    out.write(this.buf, this.pos, i - this.pos);
                }
                out.flush();
                this.pos = i + 1;
                res = out.toString();
            }
            return res;
        }

        private void fillBuf() throws IOException {
            int result = this.in.read(this.buf, 0, this.buf.length);
            if (result == -1) {
                throw new EOFException();
            }
            this.pos = 0;
            this.end = result;
        }
    }

    private LruDiskCache(File directory, int appVersion, int valueCount, long maxSize) {
        this.directory = directory;
        this.appVersion = appVersion;
        this.journalFile = new File(directory, JOURNAL_FILE);
        this.journalFileTmp = new File(directory, JOURNAL_FILE_TEMP);
        this.journalFileBackup = new File(directory, JOURNAL_FILE_BACKUP);
        this.valueCount = valueCount;
        this.maxSize = maxSize;
    }

    public static LruDiskCache open(File directory, int appVersion, int valueCount, long maxSize) throws IOException {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        } else if (valueCount <= 0) {
            throw new IllegalArgumentException("valueCount <= 0");
        } else {
            File backupFile = new File(directory, JOURNAL_FILE_BACKUP);
            if (backupFile.exists()) {
                File journalFile = new File(directory, JOURNAL_FILE);
                if (journalFile.exists()) {
                    backupFile.delete();
                } else {
                    renameTo(backupFile, journalFile, false);
                }
            }
            LruDiskCache cache = new LruDiskCache(directory, appVersion, valueCount, maxSize);
            if (cache.journalFile.exists()) {
                try {
                    cache.readJournal();
                    cache.processJournal();
                    cache.journalWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(cache.journalFile, true), "US-ASCII"));
                    return cache;
                } catch (Throwable journalIsCorrupt) {
                    LogUtils.e("DiskLruCache " + directory + " is corrupt: " + journalIsCorrupt.getMessage() + ", removing", journalIsCorrupt);
                    cache.delete();
                }
            }
            if (directory.exists() || directory.mkdirs()) {
                cache = new LruDiskCache(directory, appVersion, valueCount, maxSize);
                cache.rebuildJournal();
            }
            return cache;
        }
    }

    private void readJournal() throws IOException {
        Throwable th;
        Closeable reader = null;
        try {
            Closeable reader2 = new StrictLineReader(this, new FileInputStream(this.journalFile));
            int lineCount;
            try {
                String magic = reader2.readLine();
                String version = reader2.readLine();
                String appVersionString = reader2.readLine();
                String valueCountString = reader2.readLine();
                String blank = reader2.readLine();
                if (MAGIC.equals(magic) && "1".equals(version) && Integer.toString(this.appVersion).equals(appVersionString) && Integer.toString(this.valueCount).equals(valueCountString) && "".equals(blank)) {
                    lineCount = 0;
                    while (true) {
                        readJournalLine(reader2.readLine());
                        lineCount++;
                    }
                } else {
                    throw new IOException("unexpected journal header: [" + magic + ", " + version + ", " + valueCountString + ", " + blank + RequestParameters.RIGHT_BRACKETS);
                }
            } catch (EOFException e) {
                this.redundantOpCount = lineCount - this.lruEntries.size();
                IOUtils.closeQuietly(reader2);
            } catch (Throwable th2) {
                th = th2;
                reader = reader2;
                IOUtils.closeQuietly(reader);
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            IOUtils.closeQuietly(reader);
            throw th;
        }
    }

    private void readJournalLine(String line) throws IOException {
        int firstSpace = line.indexOf(32);
        if (firstSpace == 1) {
            String diskKey;
            char lineTag = line.charAt(0);
            int keyBegin = firstSpace + 1;
            int secondSpace = line.indexOf(32, keyBegin);
            if (secondSpace == -1) {
                diskKey = line.substring(keyBegin);
                if (lineTag == DELETE) {
                    this.lruEntries.remove(diskKey);
                    return;
                }
            }
            diskKey = line.substring(keyBegin, secondSpace);
            Entry entry = (Entry) this.lruEntries.get(diskKey);
            if (entry == null) {
                entry = new Entry(diskKey);
                this.lruEntries.put(diskKey, entry);
            }
            switch (lineTag) {
                case 'C':
                    entry.readable = true;
                    entry.currentEditor = null;
                    String[] parts = line.substring(secondSpace + 1).split(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
                    if (parts.length > 0) {
                        try {
                            if (parts[0].charAt(0) == EXPIRY_PREFIX) {
                                entry.expiryTimestamp = Long.valueOf(parts[0].substring(1)).longValue();
                                entry.setLengths(parts, 1);
                                return;
                            }
                            entry.expiryTimestamp = Long.MAX_VALUE;
                            entry.setLengths(parts, 0);
                            return;
                        } catch (Throwable th) {
                            IOException iOException = new IOException("unexpected journal line: " + line);
                        }
                    } else {
                        return;
                    }
                case 'R':
                    return;
                case 'U':
                    entry.currentEditor = new Editor(entry);
                    return;
                default:
                    throw new IOException("unexpected journal line: " + line);
            }
        }
        throw new IOException("unexpected journal line: " + line);
    }

    private void processJournal() throws IOException {
        deleteIfExists(this.journalFileTmp);
        Iterator<Entry> i = this.lruEntries.values().iterator();
        while (i.hasNext()) {
            Entry entry = (Entry) i.next();
            int t;
            if (entry.currentEditor == null) {
                for (t = 0; t < this.valueCount; t++) {
                    this.size += entry.lengths[t];
                }
            } else {
                entry.currentEditor = null;
                for (t = 0; t < this.valueCount; t++) {
                    deleteIfExists(entry.getCleanFile(t));
                    deleteIfExists(entry.getDirtyFile(t));
                }
                i.remove();
            }
        }
    }

    private synchronized void rebuildJournal() throws IOException {
        Throwable th;
        if (this.journalWriter != null) {
            IOUtils.closeQuietly(this.journalWriter);
        }
        Closeable writer = null;
        try {
            Closeable writer2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFileTmp), "US-ASCII"));
            writer2.write(MAGIC);
            writer2.write("\n");
            writer2.write("1");
            writer2.write("\n");
            writer2.write(Integer.toString(this.appVersion));
            writer2.write("\n");
            writer2.write(Integer.toString(this.valueCount));
            writer2.write("\n");
            writer2.write("\n");
            for (Entry entry : this.lruEntries.values()) {
                if (entry.currentEditor != null) {
                    writer2.write("U " + entry.diskKey + '\n');
                } else {
                    try {
                        writer2.write("C " + entry.diskKey + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + EXPIRY_PREFIX + entry.expiryTimestamp + entry.getLengths() + '\n');
                    } catch (Throwable th2) {
                        th = th2;
                        writer = writer2;
                    }
                }
            }
            IOUtils.closeQuietly(writer2);
            if (this.journalFile.exists()) {
                renameTo(this.journalFile, this.journalFileBackup, true);
            }
            renameTo(this.journalFileTmp, this.journalFile, false);
            this.journalFileBackup.delete();
            this.journalWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFile, true), "US-ASCII"));
        } catch (Throwable th3) {
            th = th3;
            IOUtils.closeQuietly(writer);
            throw th;
        }
    }

    private static void deleteIfExists(File file) throws IOException {
        if (file.exists() && !file.delete()) {
            throw new IOException();
        }
    }

    private static void renameTo(File from, File to, boolean deleteDestination) throws IOException {
        if (deleteDestination) {
            deleteIfExists(to);
        }
        if (!from.renameTo(to)) {
            throw new IOException();
        }
    }

    public synchronized long getExpiryTimestamp(String key) throws IOException {
        long j;
        String diskKey = this.fileNameGenerator.generate(key);
        checkNotClosed();
        Entry entry = (Entry) this.lruEntries.get(diskKey);
        if (entry == null) {
            j = 0;
        } else {
            j = entry.expiryTimestamp;
        }
        return j;
    }

    public File getCacheFile(String key, int index) {
        File result = new File(this.directory, new StringBuilder(String.valueOf(this.fileNameGenerator.generate(key))).append(".").append(index).toString());
        if (result.exists()) {
            return result;
        }
        try {
            remove(key);
        } catch (IOException e) {
        }
        return null;
    }

    public Snapshot get(String key) throws IOException {
        return getByDiskKey(this.fileNameGenerator.generate(key));
    }

    private synchronized Snapshot getByDiskKey(String diskKey) throws IOException {
        Snapshot snapshot = null;
        synchronized (this) {
            checkNotClosed();
            Entry entry = (Entry) this.lruEntries.get(diskKey);
            if (entry != null) {
                if (entry.readable) {
                    int i;
                    if (entry.expiryTimestamp < System.currentTimeMillis()) {
                        i = 0;
                        while (i < this.valueCount) {
                            File file = entry.getCleanFile(i);
                            if (!file.exists() || file.delete()) {
                                this.size -= entry.lengths[i];
                                entry.lengths[i] = 0;
                                i++;
                            } else {
                                throw new IOException("failed to delete " + file);
                            }
                        }
                        this.redundantOpCount++;
                        this.journalWriter.append("D " + diskKey + '\n');
                        this.lruEntries.remove(diskKey);
                        if (journalRebuildRequired()) {
                            this.executorService.submit(this.cleanupCallable);
                        }
                    } else {
                        FileInputStream[] ins = new FileInputStream[this.valueCount];
                        i = 0;
                        while (i < this.valueCount) {
                            try {
                                ins[i] = new FileInputStream(entry.getCleanFile(i));
                                i++;
                            } catch (FileNotFoundException e) {
                                i = 0;
                                while (i < this.valueCount && ins[i] != null) {
                                    IOUtils.closeQuietly(ins[i]);
                                    i++;
                                }
                            }
                        }
                        this.redundantOpCount++;
                        this.journalWriter.append("R " + diskKey + '\n');
                        if (journalRebuildRequired()) {
                            this.executorService.submit(this.cleanupCallable);
                        }
                        snapshot = new Snapshot(diskKey, entry.sequenceNumber, ins, entry.lengths);
                    }
                }
            }
        }
        return snapshot;
    }

    public Editor edit(String key) throws IOException {
        return editByDiskKey(this.fileNameGenerator.generate(key), -1);
    }

    private synchronized Editor editByDiskKey(String diskKey, long expectedSequenceNumber) throws IOException {
        Editor editor = null;
        synchronized (this) {
            checkNotClosed();
            Entry entry = (Entry) this.lruEntries.get(diskKey);
            if (expectedSequenceNumber == -1 || (entry != null && entry.sequenceNumber == expectedSequenceNumber)) {
                if (entry == null) {
                    entry = new Entry(diskKey);
                    this.lruEntries.put(diskKey, entry);
                } else if (entry.currentEditor != null) {
                }
                editor = new Editor(entry);
                entry.currentEditor = editor;
                this.journalWriter.write("U " + diskKey + '\n');
                this.journalWriter.flush();
            }
        }
        return editor;
    }

    public File getDirectory() {
        return this.directory;
    }

    public synchronized long getMaxSize() {
        return this.maxSize;
    }

    public synchronized void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
        this.executorService.submit(this.cleanupCallable);
    }

    public synchronized long size() {
        return this.size;
    }

    private synchronized void completeEdit(Editor editor, boolean success) throws IOException {
        Entry entry = editor.entry;
        if (entry.currentEditor != editor) {
            throw new IllegalStateException();
        }
        int i;
        if (success) {
            if (!entry.readable) {
                i = 0;
                while (i < this.valueCount) {
                    if (!editor.written[i]) {
                        editor.abort();
                        throw new IllegalStateException("Newly created entry didn't create value for index " + i);
                    } else if (!entry.getDirtyFile(i).exists()) {
                        editor.abort();
                        break;
                    } else {
                        i++;
                    }
                }
            }
        }
        for (i = 0; i < this.valueCount; i++) {
            File dirty = entry.getDirtyFile(i);
            if (!success) {
                deleteIfExists(dirty);
            } else if (dirty.exists()) {
                File clean = entry.getCleanFile(i);
                dirty.renameTo(clean);
                long oldLength = entry.lengths[i];
                long newLength = clean.length();
                entry.lengths[i] = newLength;
                this.size = (this.size - oldLength) + newLength;
            }
        }
        this.redundantOpCount++;
        entry.currentEditor = null;
        if ((entry.readable | success) != 0) {
            entry.readable = true;
            this.journalWriter.write("C " + entry.diskKey + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + EXPIRY_PREFIX + entry.expiryTimestamp + entry.getLengths() + '\n');
            if (success) {
                long j = this.nextSequenceNumber;
                this.nextSequenceNumber = 1 + j;
                entry.sequenceNumber = j;
            }
        } else {
            this.lruEntries.remove(entry.diskKey);
            this.journalWriter.write("D " + entry.diskKey + '\n');
        }
        this.journalWriter.flush();
        if (this.size > this.maxSize || journalRebuildRequired()) {
            this.executorService.submit(this.cleanupCallable);
        }
    }

    private boolean journalRebuildRequired() {
        return this.redundantOpCount >= BufferRecycler.DEFAULT_WRITE_CONCAT_BUFFER_LEN && this.redundantOpCount >= this.lruEntries.size();
    }

    public boolean remove(String key) throws IOException {
        return removeByDiskKey(this.fileNameGenerator.generate(key));
    }

    private synchronized boolean removeByDiskKey(String diskKey) throws IOException {
        boolean z;
        checkNotClosed();
        Entry entry = (Entry) this.lruEntries.get(diskKey);
        if (entry == null || entry.currentEditor != null) {
            z = false;
        } else {
            int i = 0;
            while (i < this.valueCount) {
                File file = entry.getCleanFile(i);
                if (!file.exists() || file.delete()) {
                    this.size -= entry.lengths[i];
                    entry.lengths[i] = 0;
                    i++;
                } else {
                    throw new IOException("failed to delete " + file);
                }
            }
            this.redundantOpCount++;
            this.journalWriter.append("D " + diskKey + '\n');
            this.lruEntries.remove(diskKey);
            if (journalRebuildRequired()) {
                this.executorService.submit(this.cleanupCallable);
            }
            z = true;
        }
        return z;
    }

    public synchronized boolean isClosed() {
        return this.journalWriter == null;
    }

    private void checkNotClosed() {
        if (this.journalWriter == null) {
            throw new IllegalStateException("cache is closed");
        }
    }

    public synchronized void flush() throws IOException {
        checkNotClosed();
        trimToSize();
        this.journalWriter.flush();
    }

    public synchronized void close() throws IOException {
        if (this.journalWriter != null) {
            Iterator it = new ArrayList(this.lruEntries.values()).iterator();
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                if (entry.currentEditor != null) {
                    entry.currentEditor.abort();
                }
            }
            trimToSize();
            this.journalWriter.close();
            this.journalWriter = null;
        }
    }

    private void trimToSize() throws IOException {
        while (this.size > this.maxSize) {
            removeByDiskKey((String) ((java.util.Map.Entry) this.lruEntries.entrySet().iterator().next()).getKey());
        }
    }

    public void delete() throws IOException {
        IOUtils.closeQuietly((Closeable) this);
        deleteContents(this.directory);
    }

    private static String inputStreamToString(InputStream in) throws IOException {
        return readFully(new InputStreamReader(in, DownloadManager.UTF8_CHARSET));
    }

    private static String readFully(Reader reader) throws IOException {
        Throwable th;
        Closeable writer = null;
        try {
            Closeable writer2 = new StringWriter();
            try {
                char[] buffer = new char[1024];
                while (true) {
                    int count = reader.read(buffer);
                    if (count == -1) {
                        String stringWriter = writer2.toString();
                        IOUtils.closeQuietly((Closeable) reader);
                        IOUtils.closeQuietly(writer2);
                        return stringWriter;
                    }
                    writer2.write(buffer, 0, count);
                }
            } catch (Throwable th2) {
                th = th2;
                writer = writer2;
            }
        } catch (Throwable th3) {
            th = th3;
            IOUtils.closeQuietly((Closeable) reader);
            IOUtils.closeQuietly(writer);
            throw th;
        }
    }

    private static void deleteContents(File dir) throws IOException {
        File[] files = dir.listFiles();
        if (files == null) {
            throw new IOException("not a readable directory: " + dir);
        }
        int length = files.length;
        int i = 0;
        while (i < length) {
            File file = files[i];
            if (file.isDirectory()) {
                deleteContents(file);
            }
            if (!file.exists() || file.delete()) {
                i++;
            } else {
                throw new IOException("failed to delete file: " + file);
            }
        }
    }

    public FileNameGenerator getFileNameGenerator() {
        return this.fileNameGenerator;
    }

    public void setFileNameGenerator(FileNameGenerator fileNameGenerator) {
        if (fileNameGenerator != null) {
            this.fileNameGenerator = fileNameGenerator;
        }
    }
}
