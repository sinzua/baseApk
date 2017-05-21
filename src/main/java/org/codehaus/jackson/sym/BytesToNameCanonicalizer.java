package org.codehaus.jackson.sym;

import android.support.v4.view.InputDeviceCompat;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import org.codehaus.jackson.util.InternCache;

public final class BytesToNameCanonicalizer {
    protected static final int DEFAULT_TABLE_SIZE = 64;
    static final int INITIAL_COLLISION_LEN = 32;
    static final int LAST_VALID_BUCKET = 254;
    static final int MAX_COLL_CHAIN_FOR_REUSE = 63;
    static final int MAX_COLL_CHAIN_LENGTH = 255;
    static final int MAX_ENTRIES_FOR_REUSE = 6000;
    protected static final int MAX_TABLE_SIZE = 65536;
    static final int MIN_HASH_SIZE = 16;
    private static final int MULT = 33;
    private static final int MULT2 = 65599;
    private static final int MULT3 = 31;
    protected int _collCount;
    protected int _collEnd;
    protected Bucket[] _collList;
    private boolean _collListShared;
    protected int _count;
    private final int _hashSeed;
    protected final boolean _intern;
    protected int _longestCollisionList;
    protected int[] _mainHash;
    protected int _mainHashMask;
    private boolean _mainHashShared;
    protected Name[] _mainNames;
    private boolean _mainNamesShared;
    private transient boolean _needRehash;
    protected final BytesToNameCanonicalizer _parent;
    protected final AtomicReference<TableInfo> _tableInfo;

    static final class Bucket {
        private final int _length;
        protected final Name _name;
        protected final Bucket _next;

        Bucket(Name name, Bucket next) {
            this._name = name;
            this._next = next;
            this._length = next == null ? 1 : next._length + 1;
        }

        public int length() {
            return this._length;
        }

        public Name find(int hash, int firstQuad, int secondQuad) {
            if (this._name.hashCode() == hash && this._name.equals(firstQuad, secondQuad)) {
                return this._name;
            }
            for (Bucket curr = this._next; curr != null; curr = curr._next) {
                Name currName = curr._name;
                if (currName.hashCode() == hash && currName.equals(firstQuad, secondQuad)) {
                    return currName;
                }
            }
            return null;
        }

        public Name find(int hash, int[] quads, int qlen) {
            if (this._name.hashCode() == hash && this._name.equals(quads, qlen)) {
                return this._name;
            }
            for (Bucket curr = this._next; curr != null; curr = curr._next) {
                Name currName = curr._name;
                if (currName.hashCode() == hash && currName.equals(quads, qlen)) {
                    return currName;
                }
            }
            return null;
        }
    }

    private static final class TableInfo {
        public final int collCount;
        public final int collEnd;
        public final Bucket[] collList;
        public final int count;
        public final int longestCollisionList;
        public final int[] mainHash;
        public final int mainHashMask;
        public final Name[] mainNames;

        public TableInfo(int count, int mainHashMask, int[] mainHash, Name[] mainNames, Bucket[] collList, int collCount, int collEnd, int longestCollisionList) {
            this.count = count;
            this.mainHashMask = mainHashMask;
            this.mainHash = mainHash;
            this.mainNames = mainNames;
            this.collList = collList;
            this.collCount = collCount;
            this.collEnd = collEnd;
            this.longestCollisionList = longestCollisionList;
        }

        public TableInfo(BytesToNameCanonicalizer src) {
            this.count = src._count;
            this.mainHashMask = src._mainHashMask;
            this.mainHash = src._mainHash;
            this.mainNames = src._mainNames;
            this.collList = src._collList;
            this.collCount = src._collCount;
            this.collEnd = src._collEnd;
            this.longestCollisionList = src._longestCollisionList;
        }
    }

    private BytesToNameCanonicalizer(int hashSize, boolean intern, int seed) {
        this._parent = null;
        this._hashSeed = seed;
        this._intern = intern;
        if (hashSize < 16) {
            hashSize = 16;
        } else if (((hashSize - 1) & hashSize) != 0) {
            int curr = 16;
            while (curr < hashSize) {
                curr += curr;
            }
            hashSize = curr;
        }
        this._tableInfo = new AtomicReference(initTableInfo(hashSize));
    }

    private BytesToNameCanonicalizer(BytesToNameCanonicalizer parent, boolean intern, int seed, TableInfo state) {
        this._parent = parent;
        this._hashSeed = seed;
        this._intern = intern;
        this._tableInfo = null;
        this._count = state.count;
        this._mainHashMask = state.mainHashMask;
        this._mainHash = state.mainHash;
        this._mainNames = state.mainNames;
        this._collList = state.collList;
        this._collCount = state.collCount;
        this._collEnd = state.collEnd;
        this._longestCollisionList = state.longestCollisionList;
        this._needRehash = false;
        this._mainHashShared = true;
        this._mainNamesShared = true;
        this._collListShared = true;
    }

    private TableInfo initTableInfo(int hashSize) {
        return new TableInfo(0, hashSize - 1, new int[hashSize], new Name[hashSize], null, 0, 0, 0);
    }

    public static BytesToNameCanonicalizer createRoot() {
        long now = System.currentTimeMillis();
        return createRoot((((int) now) + (((int) now) >>> 32)) | 1);
    }

    protected static BytesToNameCanonicalizer createRoot(int hashSeed) {
        return new BytesToNameCanonicalizer(64, true, hashSeed);
    }

    public BytesToNameCanonicalizer makeChild(boolean canonicalize, boolean intern) {
        return new BytesToNameCanonicalizer(this, intern, this._hashSeed, (TableInfo) this._tableInfo.get());
    }

    public void release() {
        if (this._parent != null && maybeDirty()) {
            this._parent.mergeChild(new TableInfo(this));
            this._mainHashShared = true;
            this._mainNamesShared = true;
            this._collListShared = true;
        }
    }

    private void mergeChild(TableInfo childState) {
        int childCount = childState.count;
        TableInfo currState = (TableInfo) this._tableInfo.get();
        if (childCount > currState.count) {
            if (childCount > MAX_ENTRIES_FOR_REUSE || childState.longestCollisionList > 63) {
                childState = initTableInfo(64);
            }
            this._tableInfo.compareAndSet(currState, childState);
        }
    }

    public int size() {
        if (this._tableInfo != null) {
            return ((TableInfo) this._tableInfo.get()).count;
        }
        return this._count;
    }

    public int bucketCount() {
        return this._mainHash.length;
    }

    public boolean maybeDirty() {
        return !this._mainHashShared;
    }

    public int hashSeed() {
        return this._hashSeed;
    }

    public int collisionCount() {
        return this._collCount;
    }

    public int maxCollisionLength() {
        return this._longestCollisionList;
    }

    public static Name getEmptyName() {
        return Name1.getEmptyName();
    }

    public Name findName(int firstQuad) {
        int hash = calcHash(firstQuad);
        int ix = hash & this._mainHashMask;
        int val = this._mainHash[ix];
        if ((((val >> 8) ^ hash) << 8) == 0) {
            Name name = this._mainNames[ix];
            if (name == null) {
                return null;
            }
            if (name.equals(firstQuad)) {
                return name;
            }
        } else if (val == 0) {
            return null;
        }
        val &= 255;
        if (val > 0) {
            Bucket bucket = this._collList[val - 1];
            if (bucket != null) {
                return bucket.find(hash, firstQuad, 0);
            }
        }
        return null;
    }

    public Name findName(int firstQuad, int secondQuad) {
        int hash = secondQuad == 0 ? calcHash(firstQuad) : calcHash(firstQuad, secondQuad);
        int ix = hash & this._mainHashMask;
        int val = this._mainHash[ix];
        if ((((val >> 8) ^ hash) << 8) == 0) {
            Name name = this._mainNames[ix];
            if (name == null) {
                return null;
            }
            if (name.equals(firstQuad, secondQuad)) {
                return name;
            }
        } else if (val == 0) {
            return null;
        }
        val &= 255;
        if (val > 0) {
            Bucket bucket = this._collList[val - 1];
            if (bucket != null) {
                return bucket.find(hash, firstQuad, secondQuad);
            }
        }
        return null;
    }

    public Name findName(int[] quads, int qlen) {
        int i = 0;
        if (qlen < 3) {
            int i2 = quads[0];
            if (qlen >= 2) {
                i = quads[1];
            }
            return findName(i2, i);
        }
        int hash = calcHash(quads, qlen);
        int ix = hash & this._mainHashMask;
        int val = this._mainHash[ix];
        if ((((val >> 8) ^ hash) << 8) == 0) {
            Name name = this._mainNames[ix];
            if (name == null || name.equals(quads, qlen)) {
                return name;
            }
        } else if (val == 0) {
            return null;
        }
        val &= 255;
        if (val > 0) {
            Bucket bucket = this._collList[val - 1];
            if (bucket != null) {
                return bucket.find(hash, quads, qlen);
            }
        }
        return null;
    }

    public Name addName(String symbolStr, int q1, int q2) {
        if (this._intern) {
            symbolStr = InternCache.instance.intern(symbolStr);
        }
        int hash = q2 == 0 ? calcHash(q1) : calcHash(q1, q2);
        Name symbol = constructName(hash, symbolStr, q1, q2);
        _addSymbol(hash, symbol);
        return symbol;
    }

    public Name addName(String symbolStr, int[] quads, int qlen) {
        if (this._intern) {
            symbolStr = InternCache.instance.intern(symbolStr);
        }
        int hash = qlen < 3 ? qlen == 1 ? calcHash(quads[0]) : calcHash(quads[0], quads[1]) : calcHash(quads, qlen);
        Name symbol = constructName(hash, symbolStr, quads, qlen);
        _addSymbol(hash, symbol);
        return symbol;
    }

    public final int calcHash(int firstQuad) {
        int hash = firstQuad ^ this._hashSeed;
        hash += hash >>> 15;
        return hash ^ (hash >>> 9);
    }

    public final int calcHash(int firstQuad, int secondQuad) {
        int hash = firstQuad;
        hash = ((hash ^ (hash >>> 15)) + (secondQuad * 33)) ^ this._hashSeed;
        return hash + (hash >>> 7);
    }

    public final int calcHash(int[] quads, int qlen) {
        if (qlen < 3) {
            throw new IllegalArgumentException();
        }
        int hash = quads[0] ^ this._hashSeed;
        hash = (((hash + (hash >>> 9)) * 33) + quads[1]) * MULT2;
        hash = (hash + (hash >>> 15)) ^ quads[2];
        hash += hash >>> 17;
        for (int i = 3; i < qlen; i++) {
            hash = (hash * 31) ^ quads[i];
            hash += hash >>> 3;
            hash ^= hash << 7;
        }
        hash += hash >>> 15;
        return hash ^ (hash << 9);
    }

    protected static int[] calcQuads(byte[] wordBytes) {
        int blen = wordBytes.length;
        int[] result = new int[((blen + 3) / 4)];
        int i = 0;
        while (i < blen) {
            int x = wordBytes[i] & 255;
            i++;
            if (i < blen) {
                x = (x << 8) | (wordBytes[i] & 255);
                i++;
                if (i < blen) {
                    x = (x << 8) | (wordBytes[i] & 255);
                    i++;
                    if (i < blen) {
                        x = (x << 8) | (wordBytes[i] & 255);
                    }
                }
            }
            result[i >> 2] = x;
            i++;
        }
        return result;
    }

    private void _addSymbol(int hash, Name symbol) {
        if (this._mainHashShared) {
            unshareMain();
        }
        if (this._needRehash) {
            rehash();
        }
        this._count++;
        int ix = hash & this._mainHashMask;
        if (this._mainNames[ix] == null) {
            this._mainHash[ix] = hash << 8;
            if (this._mainNamesShared) {
                unshareNames();
            }
            this._mainNames[ix] = symbol;
        } else {
            if (this._collListShared) {
                unshareCollision();
            }
            this._collCount++;
            int entryValue = this._mainHash[ix];
            int bucket = entryValue & 255;
            if (bucket == 0) {
                if (this._collEnd <= LAST_VALID_BUCKET) {
                    bucket = this._collEnd;
                    this._collEnd++;
                    if (bucket >= this._collList.length) {
                        expandCollision();
                    }
                } else {
                    bucket = findBestBucket();
                }
                this._mainHash[ix] = (entryValue & InputDeviceCompat.SOURCE_ANY) | (bucket + 1);
            } else {
                bucket--;
            }
            Bucket newB = new Bucket(symbol, this._collList[bucket]);
            this._collList[bucket] = newB;
            this._longestCollisionList = Math.max(newB.length(), this._longestCollisionList);
            if (this._longestCollisionList > 255) {
                reportTooManyCollisions(255);
            }
        }
        int hashSize = this._mainHash.length;
        if (this._count > (hashSize >> 1)) {
            int hashQuarter = hashSize >> 2;
            if (this._count > hashSize - hashQuarter) {
                this._needRehash = true;
            } else if (this._collCount >= hashQuarter) {
                this._needRehash = true;
            }
        }
    }

    private void rehash() {
        this._needRehash = false;
        this._mainNamesShared = false;
        int len = this._mainHash.length;
        int newLen = len + len;
        if (newLen > 65536) {
            nukeSymbols();
            return;
        }
        int i;
        this._mainHash = new int[newLen];
        this._mainHashMask = newLen - 1;
        Name[] oldNames = this._mainNames;
        this._mainNames = new Name[newLen];
        int symbolsSeen = 0;
        for (i = 0; i < len; i++) {
            Name symbol = oldNames[i];
            if (symbol != null) {
                symbolsSeen++;
                int hash = symbol.hashCode();
                int ix = hash & this._mainHashMask;
                this._mainNames[ix] = symbol;
                this._mainHash[ix] = hash << 8;
            }
        }
        int oldEnd = this._collEnd;
        if (oldEnd == 0) {
            this._longestCollisionList = 0;
            return;
        }
        this._collCount = 0;
        this._collEnd = 0;
        this._collListShared = false;
        int maxColl = 0;
        Bucket[] oldBuckets = this._collList;
        this._collList = new Bucket[oldBuckets.length];
        for (i = 0; i < oldEnd; i++) {
            for (Bucket curr = oldBuckets[i]; curr != null; curr = curr._next) {
                symbolsSeen++;
                symbol = curr._name;
                hash = symbol.hashCode();
                ix = hash & this._mainHashMask;
                int val = this._mainHash[ix];
                if (this._mainNames[ix] == null) {
                    this._mainHash[ix] = hash << 8;
                    this._mainNames[ix] = symbol;
                } else {
                    this._collCount++;
                    int bucket = val & 255;
                    if (bucket == 0) {
                        if (this._collEnd <= LAST_VALID_BUCKET) {
                            bucket = this._collEnd;
                            this._collEnd++;
                            if (bucket >= this._collList.length) {
                                expandCollision();
                            }
                        } else {
                            bucket = findBestBucket();
                        }
                        this._mainHash[ix] = (val & InputDeviceCompat.SOURCE_ANY) | (bucket + 1);
                    } else {
                        bucket--;
                    }
                    Bucket newB = new Bucket(symbol, this._collList[bucket]);
                    this._collList[bucket] = newB;
                    maxColl = Math.max(maxColl, newB.length());
                }
            }
        }
        this._longestCollisionList = maxColl;
        if (symbolsSeen != this._count) {
            throw new RuntimeException("Internal error: count after rehash " + symbolsSeen + "; should be " + this._count);
        }
    }

    private void nukeSymbols() {
        this._count = 0;
        this._longestCollisionList = 0;
        Arrays.fill(this._mainHash, 0);
        Arrays.fill(this._mainNames, null);
        Arrays.fill(this._collList, null);
        this._collCount = 0;
        this._collEnd = 0;
    }

    private int findBestBucket() {
        Bucket[] buckets = this._collList;
        int bestCount = Integer.MAX_VALUE;
        int bestIx = -1;
        int len = this._collEnd;
        for (int i = 0; i < len; i++) {
            int count = buckets[i].length();
            if (count < bestCount) {
                if (count == 1) {
                    return i;
                }
                bestCount = count;
                bestIx = i;
            }
        }
        return bestIx;
    }

    private void unshareMain() {
        int[] old = this._mainHash;
        int len = this._mainHash.length;
        this._mainHash = new int[len];
        System.arraycopy(old, 0, this._mainHash, 0, len);
        this._mainHashShared = false;
    }

    private void unshareCollision() {
        Bucket[] old = this._collList;
        if (old == null) {
            this._collList = new Bucket[32];
        } else {
            int len = old.length;
            this._collList = new Bucket[len];
            System.arraycopy(old, 0, this._collList, 0, len);
        }
        this._collListShared = false;
    }

    private void unshareNames() {
        Name[] old = this._mainNames;
        int len = old.length;
        this._mainNames = new Name[len];
        System.arraycopy(old, 0, this._mainNames, 0, len);
        this._mainNamesShared = false;
    }

    private void expandCollision() {
        Bucket[] old = this._collList;
        int len = old.length;
        this._collList = new Bucket[(len + len)];
        System.arraycopy(old, 0, this._collList, 0, len);
    }

    private static Name constructName(int hash, String name, int q1, int q2) {
        if (q2 == 0) {
            return new Name1(name, hash, q1);
        }
        return new Name2(name, hash, q1, q2);
    }

    private static Name constructName(int hash, String name, int[] quads, int qlen) {
        if (qlen < 4) {
            switch (qlen) {
                case 1:
                    return new Name1(name, hash, quads[0]);
                case 2:
                    return new Name2(name, hash, quads[0], quads[1]);
                case 3:
                    return new Name3(name, hash, quads[0], quads[1], quads[2]);
            }
        }
        int[] buf = new int[qlen];
        for (int i = 0; i < qlen; i++) {
            buf[i] = quads[i];
        }
        return new NameN(name, hash, buf, qlen);
    }

    protected void reportTooManyCollisions(int maxLen) {
        throw new IllegalStateException("Longest collision chain in symbol table (of size " + this._count + ") now exceeds maximum, " + maxLen + " -- suspect a DoS attack based on hash collisions");
    }
}
